package server.backend;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import gameObjects.Game;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;
import messageObjects.GameListItemList;
import messageObjects.LobbyChatMessage;
import messageObjects.UserInfoListMessage;
import messageObjects.UserInfoMessage;

public class Lobby {
	private ArrayList<User> onlineUsers;
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
		Collections.sort(userInfoAllUsers);
		onlineUsers = new ArrayList<User>();
		
		waitingGames = new ArrayList<Game>();
		runningGames = new ArrayList<Game>();
	}
	
	
	//checks if username already exists and creates new user if it doesn't. If create user successful user will be informed and logged in.
	public synchronized UserInfo createNewUser(String username, String password, User user)
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
	public synchronized UserInfo loginUser(String username, String password, User user) {
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
	public synchronized String createGame(User user, CreateGameMessage createMsg)
	{
		String serverAnswer = new String("");
		String gameName = createMsg.getGameName();
		String password = createMsg.getPassword();
		int maxPlayers = createMsg.getMaxPlayers();
		
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
			Game newGame = new Game(gameName,password, maxPlayers, user);
			waitingGames.add(newGame);
			updateLobby(newGame);
			
			serverAnswer = "Game successfully created!";
		}
		else
		{
			serverAnswer = "Game name currently taken. Please select another name.";
		}
		
		return serverAnswer;
	}
	
	//Makes user join a game if preconditions are met
	public synchronized String joinGame(User user, GameJoinMessage joinMsg)
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
				System.out.println(g.getMaxPlayers() + " " + g.getNumOfRegisteredPlayers());
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
						 System.out.println("I'm here");
						 g.addUser(user);
						 serverAnswer = "You have successfully registered for this game.";
					
						 if(g.getNumOfRegisteredPlayers()==g.getMaxPlayers())
						 {
							 //starts game
							 g.start();
							 runningGames.add(g);
							 gameIt.remove();
							
						 
						 }
						 System.out.println(g.getNumOfRegisteredPlayers());
						 updateLobby(g);
						 System.out.println("updated lobby");
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
	public synchronized void sendWholeLobby(User user) {

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
	private synchronized void sendLeaderboard(User user) {
		ArrayList<UserInfoMessage> leaderboard = new ArrayList<UserInfoMessage>();
		for(int i = 0; i < leaderboardSize; i++)
		{
			UserInfo ui = userInfoAllUsers.get(i);
			leaderboard.add(new UserInfoMessage(ui,getPosition(ui)));
			System.out.println(ui.getUsername());
		}
		user.sendMessage(new UserInfoListMessage(leaderboard));
		System.out.println("I was sending the leaderboard");
		
		
	}


	//Informs logged in users about changes in lobby
	public synchronized void updateLobby(Game game)
	{
		boolean hasPassword = true;
		
		if(game.getPassword().equals(""))
		{
			System.out.println(game.getPassword());
			hasPassword = false;
		}
		GameListItem updatedGame = new GameListItem(game.getName(),hasPassword, game.getNumOfRegisteredPlayers(), game.getMaxPlayers());
		
		for(User u: onlineUsers)
		{
			u.sendMessage(updatedGame);
			System.out.println(updatedGame.getRegisteredPlayers());
		}
	}
	


	//Initiate last database update before closing server
	public void lastDbUpdate()
	{
		databaseAccess.lastUpdate();
	}


	public synchronized void processChatMessage(LobbyChatMessage chatMessage) {
		for(User u : onlineUsers)
		{
			u.sendMessage(chatMessage);
		}
	}
	
	//Method that should be invoked after the user has won a game
	public synchronized void addWin(User user)
	{
		user.getUserInfo().gameWon();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);

	}

	//Method that should be invoked after the user has lost a game
	public synchronized void addLoss(User user)
	{
		user.getUserInfo().gameLost();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);
	}
	
	//Method that should be invoked after the user has had a tie in a game
	public synchronized void addTie(User user)
	{
		user.getUserInfo().gameTie();
		databaseAccess.updateUserOnDatabase(user.getUserInfo());
		updateLeaderBoard(user);
	}
	
	//Updates leaderboard and sends messages to users that need a update on their leaderboard
	private synchronized void updateLeaderBoard(User user) {
		// TODO Auto-generated method stub
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
			if(listPositionBefore < leaderboardSize)
			{
				for(User u : onlineUsers)
				{
					sendLeaderboard(u);
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
						u.sendMessage(new UserInfoMessage(u.getUserInfo(),i+1));
					}
				}
			}
			if(listPositionAfter < leaderboardSize)
			{
				for(User u : onlineUsers)
				{
					sendLeaderboard(u);
				}
			}
		}
		
		//Inform the user that has caused the update about change in leaderboard and stats
		user.sendMessage(new UserInfoMessage(user.getUserInfo(), getPosition(user.getUserInfo())));
	}

	//returns position in leaderboard of a user
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
}
