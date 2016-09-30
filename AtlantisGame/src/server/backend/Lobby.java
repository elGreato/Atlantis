package server.backend;

import java.sql.Connection;
import java.util.ArrayList;

import gameObjects.Game;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;

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
		
	}
	
	
	//checks if username already exists and creates new user if it doesn't
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
			sendWholeLobby();
			onlineUsers.add(user);
			databaseAccess.addNewUserToDatabase(newUser);
			return newUser;
		}
		else
		{
			return null;
		}
		
	}
	private synchronized void sendWholeLobby() {
		
		ArrayList<GameListItem> allGamesInLobby = new ArrayList<GameListItem>(); 
		for(Game game: waitingGames)
		{
			boolean hasPassword = true;
			if(game.getPassword().equals(""));
			{
				hasPassword = false;
			}
			GameListItem prepToSendGame = new GameListItem(game.getName(),hasPassword, game.getNumOfRegisteredPlayers(), game.getMaxPlayers());
			allGamesInLobby.add(prepToSendGame);
		}
		
		
		
		
	}


	//Initiate last database update before closing server
	public void lastDbUpdate()
	{
		databaseAccess.lastUpdate();
	}

	//Checks validity of entered passwords from login requests
	public synchronized UserInfo loginUser(String username, String password, User user) {
		for(UserInfo ui: userInfoAllUsers)
		{
			
			if(ui.getUsername().equals(username) && ui.getPassword().equals(password))
			{
				sendWholeLobby();
				onlineUsers.add(user);
				return ui;
			}
		}
		
		return null;
	}
	
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
	
	public synchronized String joinGame(User user, GameJoinMessage joinMsg)
	{
		String serverAnswer = new String("");
		String gameName = joinMsg.getGameName();
		String password = joinMsg.getGamePassword();
		
		boolean gameFound = false;
		for(Game g: waitingGames)
		{
			
			if(g.getName().equals(gameName) && g.getPassword().equals(password))
			{
				gameFound = true;
				 if(g.getMaxPlayers() > g.getNumOfRegisteredPlayers())
				 {
					 for(User u: g.getUsers())
					 {
						 if (user.getUserInfo().getUsername().equals(u.getUserInfo().getUsername()))
						 {
							 serverAnswer = "You have already registered for this game.";
						 }
						 else
						 {
							 g.addUser(user);
							 serverAnswer = "You have successfully registered for this game.";
							
							 //update lobby
							 
							 
							 if(g.getNumOfRegisteredPlayers()==g.getMaxPlayers())
							 {
								 initiateGameStart(g);
								 
							 }
							 
							 updateLobby(g);
						 }
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
	
	private void initiateGameStart(Game g) {
		//Start game, inform users
		 g.start();
		 runningGames.add(g);
		 waitingGames.remove(g);
		
	}


	public synchronized void updateLobby(Game game)
	{
		boolean hasPassword = true;
		
		if(game.getPassword().equals(""));
		{
			hasPassword = false;
		}
		GameListItem updatedGame = new GameListItem(game.getName(),hasPassword, game.getNumOfRegisteredPlayers(), game.getMaxPlayers());
		
		for(User u: onlineUsers)
		{
			u.sendMessage(updatedGame);
		}
	}
}
