package server.backend;

import java.sql.Connection;
import java.util.ArrayList;

import gameObjects.Game;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;

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
			onlineUsers.add(user);
			databaseAccess.addNewUserToDatabase(newUser);
			return newUser;
		}
		else
		{
			return null;
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
			if(g.getName()== gameName)
			{
				nameAvailable = false;
			}
		}
		for(Game g: runningGames)
		{
			if(g.getName()==gameName)
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
		for(Game g: waitingGames)
		{
			if(g.getName() == gameName && g.getPassword() == password && g.getMaxPlayers() > g.getNumOfRegisteredPlayers())
			{
				
			}
		}
		
		//look if same user already joined this game
		
		//add user to game
		
		//if players = maxPlayers -> game.start -> remove from waiting games and put in running games
		
		//updateLobby
		
		return serverAnswer;
	}
	public synchronized void updateLobby(Game game)
	{
		
	}
}
