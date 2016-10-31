package gameObjects;
// This class is part of the Gamer Server, should be moved from this package
import java.util.ArrayList;

import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterTileMessage;
import server.backend.Lobby;
import server.backend.User;

public class Game {

	private String name;
	private String password;
	//send Message trough sendMessage method of User, get player name stats etc through userInfo field of User
	private ArrayList<User> users; 
	private int maxPlayers;
	//Invoke lobby.addWin, lobby.addLoss & lobby.addTie methods on users at the end of the game
	private Lobby lobby;
	// waterTiles number
	private final int numberOfWaterTiles=120;
	
	private DeckOfCards cards ;
	private DeckOfLandTiles deckA;
	private DeckOfLandTiles deckB;
	
	
	//Constructor (doesn't start game)
	public Game(String name, String password, int maxPlayers, User creator, Lobby lobby) {
		
		this.name = name;
		this.password = password;
		this.maxPlayers = maxPlayers;
		users = new ArrayList<User>();
		users.add(creator);
		this.lobby = lobby;
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
	//Here the game starts
	public void start()
	{
		//Informs clients about game start
		for(User u : users)
		{
			u.initiateGameStart(this);
		}
		// send waterTiles to all the players 
		int numberOfPlayers= getNumOfRegisteredPlayers();
		
		 deckA= new DeckOfLandTiles();
		 deckB= new DeckOfLandTiles();
		 cards = new DeckOfCards();
		for (int i=0; i<numberOfPlayers; i++){
		users.get(i).sendMessage(new DeckLandTileMessage(getName(), deckA.getDeckOfTiles(),deckB.getDeckOfTiles()));
		
		}
		// send hbox Player for each player
		for(int i=0; i<numberOfPlayers; i++){
		users.get(i).sendMessage(new PlayerMessage(getName(), new Player(users.get(i).getUserInfo().getUsername()),cards, i));
		
		}
		
		
		
	}
	
	//Here messages from clients arrive
	public synchronized void processMessage(InGameMessage igm) {
		
	}


	
}
