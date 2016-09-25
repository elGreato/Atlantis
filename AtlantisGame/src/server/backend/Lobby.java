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
	
	
	//Constructor
	private Lobby()
	{
		
	}
}
