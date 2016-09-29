package gameObjects;

import java.util.ArrayList;

import server.backend.User;

public class Game {

	private String name;
	private String password;
	private ArrayList<User> users; //connection to communication interface(players are sepearte entity)
	private int maxPlayers;
	
	//Constructor (doesn't start game)
	public Game(String name, String password, int maxPlayers, User creator) {
		
		this.name = name;
		this.password = password;
		this.maxPlayers = maxPlayers;
		users = new ArrayList<User>();
		users.add(creator);
	}
	
	//Getters I need
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public ArrayList<User> getUsers() {
		return users;
	}

	public int getNumOfRegisteredPlayers() {
		// TODO Auto-generated method stub
		return users.size();
	}
	//adds new player to game
	public void addUser(User user)
	{
		users.add(user);
		
	}
	
	public void start()
	{
		//Here the game starts
	}


	
}
