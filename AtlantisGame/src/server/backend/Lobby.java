package server.backend;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import gameObjects.Game;
import messageObjects.lobbyMessages.CreateGameMessage;
import messageObjects.lobbyMessages.GameJoinMessage;
import messageObjects.lobbyMessages.GameListItem;
import messageObjects.lobbyMessages.GameListItemList;
import messageObjects.lobbyMessages.LobbyChatMessage;
import messageObjects.lobbyMessages.UserInfoListMessage;
import messageObjects.userMessages.UserInfoMessage;
/**
* <h1>The game's lobby</h1>
* This class represents the Lobby of the game and is responsible for managing games, users and their stats.
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class Lobby implements LobbyInterface{
	private ArrayList<HumanUser> onlineUsers;
	private ArrayList<AIUser> aiUsers;
	private ArrayList<UserInfo> userInfoAllUsers;
	private ArrayList<Game> waitingGames;
	private ArrayList<Game> runningGames;
	private Connection dbAccessCon;
	private DatabaseInterface databaseAccess;
	
	private final int leaderboardSize = 10;
	
	//Constructor
	public Lobby(Connection dbAccessCon)
	{
		this.dbAccessCon = dbAccessCon;
		
		databaseAccess = new DatabaseInterface(dbAccessCon);
		
		userInfoAllUsers = databaseAccess.getUsers();
		aiUsers = new ArrayList<AIUser>();
		//find AIUsers
		for(String aiName: AIUser.aiNames)
		{	
			boolean found = false;
			for(UserInfo ui : userInfoAllUsers)
			{
				if(ui.getUsername().equals(aiName))
				{
					aiUsers.add(new AIUser(ui));
					found = true;
				}
					
			}
			if(!found)
			{
				UserInfo newUI = new UserInfo(aiName,"",0,0,0);
				userInfoAllUsers.add(newUI);
				databaseAccess.addNewUserToDatabase(newUI);
				aiUsers.add(new AIUser(newUI));
			}
		}
		Collections.sort(userInfoAllUsers);
		onlineUsers = new ArrayList<HumanUser>();
		
		waitingGames = new ArrayList<Game>();
		runningGames = new ArrayList<Game>();

	}
	
	
	//checks if username already exists and creates new user if it doesn't. If create user successful user will be informed and logged in.
	@Override
	public synchronized UserInfo createNewUser(String username, String password, HumanUser user)
	{
		
		boolean userNameAvailable = true;
		
		for(UserInfo ui: userInfoAllUsers)
		{
			if(ui.getUsername().toLowerCase().equals(username.toLowerCase()))
			{
				userNameAvailable = false;
			}
		}
		UserInfo newUser = new UserInfo(username, password,0,0,0);
		if(userNameAvailable)
		{
			userInfoAllUsers.add(newUser);
			Collections.sort(userInfoAllUsers);
			databaseAccess.addNewUserToDatabase(newUser);
			
			return newUser;
		}
		else
		{
			return null;
		}
		
	}

	//Checks validity of entered passwords from login requests
	@Override
	public synchronized UserInfo loginUser(String username, String password, HumanUser user) {
		
		for(AIUser aiu : aiUsers)
		{
			if(username.equals(aiu.getUserInfo().getUsername()))
			{
				return null;
			}
		}
		for(UserInfo ui: userInfoAllUsers)
		{
			
			if(ui.getUsername().equals(username) && ui.getPassword().equals(password))
			{
				return ui;
			}
		}
		
		return null;
	}
	
	//Creates new game from user input
	@Override
	public synchronized String createGame(HumanUser user, CreateGameMessage createMsg)
	{
		String serverAnswer = new String("");
		String gameName = createMsg.getGameName();
		String password = createMsg.getPassword();
		int maxPlayers = createMsg.getMaxPlayers();
		int numAIPlayers = createMsg.getaiPlayers();
		
		boolean nameAvailable = true;
		for(Game g: waitingGames)
		{
			if(g.getName().equals(gameName))
			{
				nameAvailable = false;
			}
		}
		for(Game g: runningGames)
		{
			if(g.getName().equals(gameName))
			{
				nameAvailable=false;
			}
		}
		if(nameAvailable)
		{
			Game newGame = new Game(gameName,password, maxPlayers, user, this);
		
			Collections.shuffle(aiUsers);
			for(int i = 0; i<numAIPlayers;i++)
			{
				newGame.addUser(aiUsers.get(i));
				System.out.println("AI User added");
			}
			if(newGame.getMaxPlayers() == newGame.getNumOfRegisteredPlayers())
			{
				System.out.println("I'm here");
				newGame.start();
				runningGames.add(newGame);
			}
			else	
			{
				waitingGames.add(newGame);
				updateLobby(newGame);
			}
			
			serverAnswer = "Game successfully created!";
		}
		else
		{
			serverAnswer = "Game name currently taken. Please select another name.";
		}
		
		return serverAnswer;
	}
	
	//Makes user join a game if preconditions are met
	@Override
	public synchronized String joinGame(HumanUser user, GameJoinMessage joinMsg)
	{
		String serverAnswer = new String("");
		String gameName = joinMsg.getGameName();
		String password = joinMsg.getGamePassword();
		
		boolean gameFound = false;
		Iterator<Game> gameIt = waitingGames.iterator();
		while(gameIt.hasNext() && !gameFound)
		{
			Game g = gameIt.next();
			if(g.getName().equals(gameName) && g.getPassword().equals(password))
			{
				gameFound = true;
				 if(g.getMaxPlayers() > g.getNumOfRegisteredPlayers())
				 {
					 boolean alreadyRegistered = false;
					 for(User u:g.getUsers())
					 {
						 if (user.getUserInfo().getUsername().equals(u.getUserInfo().getUsername()))
						 {
							 alreadyRegistered = true;
							 serverAnswer = "You have already registered for this game.";
						 }
					 }
					 if(!alreadyRegistered)
					 {
						 g.addUser(user);
						 serverAnswer = "You have successfully registered for this game.";
					
						 if(g.getNumOfRegisteredPlayers()==g.getMaxPlayers())
						 {
							 //starts game
							 g.start();
							 runningGames.add(g);
							 gameIt.remove();
							
						 
						 }
						 updateLobby(g);
					 }

				 }
				 else
				 {
					 serverAnswer = "The game is full. Please select another game.";
				 }

			}
			else if(g.getName().equals(gameName))
			{
				 gameFound = true;
				 serverAnswer = "Wrong password entered!";
			
			}

		}
		
		if(!gameFound)
		{
			serverAnswer = "Game could not be found anymore in lobby. Please select another game";
		}
		
		return serverAnswer;
	}
	

	//After informing the user about the successful login, all the lobby data including games has to be sent to the user. After that the user is added to the group that gets automatic updates called onlineUsers
	@Override
	public synchronized void sendWholeLobby(HumanUser user) {

		ArrayList<GameListItem> allGamesInLobby = new ArrayList<GameListItem>(); 
		for(Game game: waitingGames)
		{
			boolean hasPassword = true;
			if(game.getPassword().equals(""))
			{
				hasPassword = false;
			}
			GameListItem prepToSendGame = new GameListItem(game.getName(),hasPassword, game.getNumOfRegisteredPlayers(), game.getMaxPlayers());
			allGamesInLobby.add(prepToSendGame);
		}
		GameListItemList listOfGames = new GameListItemList(allGamesInLobby);
		user.sendMessage(listOfGames);
		sendLeaderboard(user);
		onlineUsers.add(user);
		
		
		
	}
	//Sends leaderboard to newly logged in users
	private synchronized void sendLeaderboard(HumanUser user) {
		ArrayList<UserInfoMessage> leaderboard = new ArrayList<UserInfoMessage>();
		int leaderboardEntries = 0;
		if(userInfoAllUsers.size()< leaderboardSize)
		{
			leaderboardEntries = userInfoAllUsers.size();
		}
		else
		{
			leaderboardEntries = leaderboardSize;
		}
		for(int i = 0; i < leaderboardEntries; i++)
		{
			UserInfo ui = userInfoAllUsers.get(i);
			leaderboard.add(new UserInfoMessage(ui,getPosition(ui)));
		}
		user.sendMessage(new UserInfoListMessage(leaderboard));
		
		
	}


	//Informs logged in users about changes in lobby
	public synchronized void updateLobby(Game game)
	{
		boolean hasPassword = true;
		
		if(game.getPassword().equals(""))
		{
			hasPassword = false;
		}
		GameListItem updatedGame = new GameListItem(game.getName(),hasPassword, game.getNumOfRegisteredPlayers(), game.getMaxPlayers());
		
		for(User u: onlineUsers)
		{
			u.sendMessage(updatedGame);
		}
	}
	


	//Initiate last database update before closing server
	public void lastDbUpdate()
	{
		databaseAccess.lastUpdate();
	}

	@Override
	public synchronized void processChatMessage(LobbyChatMessage chatMessage) {
		for(User u : onlineUsers)
		{
			u.sendMessage(chatMessage);
		}
	}
	
	//Method that should be invoked after the user has won a game
	@Override
	public synchronized void addWin(User user)
	{
		user.getUserInfo().gameWon();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);

	}

	//Method that should be invoked after the user has lost a game
	@Override
	public synchronized void addLoss(User user)
	{
		user.getUserInfo().gameLost();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);
	}
	
	//Method that should be invoked after the user has had a tie in a game
	@Override
	public synchronized void addTie(User user)
	{
		user.getUserInfo().gameTie();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);
	}
	
	//Updates leaderboard and sends messages to users that need a update on their leaderboard
	private synchronized void updateLeaderBoard(User user) {
		int listPositionBefore = userInfoAllUsers.indexOf(user.getUserInfo());
		Collections.sort(userInfoAllUsers);
		int listPositionAfter = userInfoAllUsers.indexOf(user.getUserInfo());
		
		//Inform other users about change in leaderboard if they are affected
		if(listPositionBefore < listPositionAfter)
		{
			for(int i = listPositionBefore; i < listPositionAfter; i++)
			{
				for(User u : onlineUsers)
				{
					if(userInfoAllUsers.get(i).equals(u.getUserInfo()))
					{
						u.sendMessage(new UserInfoMessage(u.getUserInfo(),getPosition(u.getUserInfo())));
					}
				}
			}
		}
		else if(listPositionBefore > listPositionAfter)
		{
			for(int i = listPositionBefore; i > listPositionAfter; i--)
			{
				for(User u : onlineUsers)
				{
					if(userInfoAllUsers.get(i).equals(u.getUserInfo()))
					{
						u.sendMessage(new UserInfoMessage(u.getUserInfo(),getPosition(u.getUserInfo())));
					}
				}
			}

		}
		if(listPositionAfter < leaderboardSize || listPositionBefore < leaderboardSize)
		{
			for(HumanUser u : onlineUsers)
			{
				sendLeaderboard(u);
			}
		}
		//Inform the user that has caused the update about change in leaderboard and stats
		user.sendMessage(new UserInfoMessage(user.getUserInfo(), getPosition(user.getUserInfo())));
	}

	//returns position in leaderboard of a user
	@Override
	public int getPosition(UserInfo userInfo) {
		for(int i = 0; i < userInfoAllUsers.indexOf(userInfo); i++)
		{
			if (userInfoAllUsers.get(i).compareTo(userInfo) == 0)
			{
				return i + 1;
			}
			
		}
		return userInfoAllUsers.indexOf(userInfo)+1;
	}
	//Gets called by HumanUser class when a user logs out. removes user from online user and removes user from waiting games the user has registered for
	@Override
	public synchronized void logoutFromOnlineUsers(HumanUser user) {
		
		for(Game g : waitingGames)
		{
			Iterator<User> userIt = g.getUsers().iterator();
			while(userIt.hasNext())
			{
				User u = userIt.next();
				if(u == user)
				{
					userIt.remove();
					updateLobby(g);
				}
			}
		}
		onlineUsers.remove(user);
		
	}
	//When a game is finished this method is called and the game is removed so that the game name can be used again
	@Override
	public void endGame(Game game)
	{
		for(User u : game.getUsers())
		{
			u.endGame(game);
		}
		runningGames.remove(game);
		
	}

	//When a user leaves mid-game this method is called by the game to return a AI user to take the place of the leaving user
	@Override
	public synchronized AIUser requestAI(ArrayList<User> playersRegistered) {
		for(AIUser possibleNewOpponent : aiUsers)
		{
			if(!playersRegistered.contains(possibleNewOpponent))
			{
				return possibleNewOpponent;
			}
		}
		return null;
	}


	@Override
	public void endGameForSpecificUser(Game game, User user) {
		user.endGame(game);
		
	}
}
