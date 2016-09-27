package server.backend;

import java.sql.Connection;
import java.util.ArrayList;

import gameObjects.Game;

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
}
