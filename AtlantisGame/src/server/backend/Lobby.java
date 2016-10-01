package server.backend;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import gameObjects.Game;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;
import messageObjects.GameListItemList;

public class Lobby {
	private ArrayList<User> onlineUsers;
	private ArrayList<UserInfo> userInfoAllUsers;
	private ArrayList<Game> waitingGames;
	private ArrayList<Game> runningGames;
	private Connection dbAccessCon;
	private DatabaseInterface databaseAccess;
	
	
	//Constructor
	public Lobby(Connection dbAccessCon)
	{
		this.dbAccessCon = dbAccessCon;
		
		databaseAccess = new DatabaseInterface(dbAccessCon);
		
		userInfoAllUsers = databaseAccess.getUsers();
		
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
			if(ui.getUsername().equals(username))
			{
				userNameAvailable = false;
			}
		}
		UserInfo newUser = new UserInfo(username, password,0,0,0);
		if(userNameAvailable)
		{
			userInfoAllUsers.add(newUser);
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
		System.out.println("Try to send lobby");
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
		onlineUsers.add(user);
		
		
		
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
}
