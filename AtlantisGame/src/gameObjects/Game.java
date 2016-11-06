package gameObjects;

// This class is part of the Gamer Server, should be moved from this package
import java.util.ArrayList;

import messageObjects.AtlantisMainLandMessage;
import messageObjects.DeckLandTileMessage;
import messageObjects.GameStatusMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.TurnMessage;

import server.backend.Lobby;
import server.backend.User;

public class Game implements GameInterface{

	private String name;
	private String password;
	// send Message trough sendMessage method of User, get player name stats etc
	// through userInfo field of User
	private ArrayList<User> users;
	private int maxPlayers;
	// Invoke lobby.addWin, lobby.addLoss & lobby.addTie methods on users at the
	// end of the game
	private Lobby lobby;
	// waterTiles number
	private final int numberOfWaterTiles = 120;

	private DeckOfCards cards;
	private DeckOfLandTiles deckA;
	private DeckOfLandTiles deckB;
	private ArrayList<Player> players = new ArrayList<>();
	private AtlantisTile atlantis= new AtlantisTile();
	private MainLand mainland=new MainLand();
	
	private int currentPlayerIndex;

	// Constructor (doesn't start game)
	public Game(String name, String password, int maxPlayers, User creator, Lobby lobby) {

		this.name = name;
		this.password = password;
		this.maxPlayers = maxPlayers;
		users = new ArrayList<User>();
		users.add(creator);
		this.lobby = lobby;
	}

	// Getters I need
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

	// adds new player to game
	public void addUser(User user) {
		users.add(user);

	}

	// Here the game starts
	public void start() {
		// Informs clients about game start
		for (User u : users) {
			u.initiateGameStart(this);
		}
		// send waterTiles to all the players
		int numberOfPlayers = getNumOfRegisteredPlayers();

		deckA = new DeckOfLandTiles();
		deckB = new DeckOfLandTiles();
		cards = new DeckOfCards();
	

		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i)
					.sendMessage(new DeckLandTileMessage(getName(), deckA.getDeckOfTiles(), deckB.getDeckOfTiles()));

		}
		// send the atlantis and main land
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i)
					.sendMessage(new AtlantisMainLandMessage(getName(), atlantis, mainland));

		}
		// send  Player for each user

		for (int i = 0; i < numberOfPlayers; i++) {
			Player player = new Player(users.get(i).getUserInfo().getUsername());
			setPlayerColorAndTurn(player,i);
			player.setPlayerIndex(i);
			
			users.get(i).sendMessage(
					new PlayerMessage(getName(), player, cardsForPlayers(i, player), i));
			players.add(player);
			}
		currentPlayerIndex=0;
	
		// send the list of players for client to set opponents
		for (int i = 0; i < numberOfPlayers; i++) {

			users.get(i).sendMessage(new OpponentMessage(getName(), players));

		}
		for (int i = 0; i < numberOfPlayers; i++) {
			
			users.get(i).sendMessage(new GameStatusMessage(getName(), true,players.get(currentPlayerIndex)));

		}
		
	}



	private ArrayList<Card> cardsForPlayers(int playerIndex, Player player) {
		ArrayList<Card> result = new ArrayList<>();
		if (playerIndex == 0) {
			for (int i = 0; i < 4; i++) {
				Card card = cards.deal();
				card.setOwner(player);
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player1 a card");
				System.out.println("size of player 1 hand now is " + player.getPlayerHand().getNumCards());
			}
		} else if (playerIndex == 1) {
			for (int i = 0; i < 5; i++) {
				Card card = cards.deal();
				card.setOwner(player);
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player2 a card");
				System.out.println("size of player 2 hand now is " + player.getPlayerHand().getNumCards());
			}
		} else if (playerIndex == 2) {
			for (int i = 0; i < 6; i++) {
				Card card = cards.deal();
				card.setOwner(player);
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player3 a card");
			}
		} else if (playerIndex == 3) {
			for (int i = 0; i < 7; i++) {
				Card card = cards.deal();
				card.setOwner(player);
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player4 a card");
			}
		}

		return result;
	}

	// Here messages from clients arrive
	public synchronized void processMessage(InGameMessage igm) {
		if (igm instanceof GameStatusMessage){
			System.out.println("Game STATUS MESSAGE RECEUVED FROM CLIENT");
			new TurnMessage(((GameStatusMessage)igm).getGameName(),checkTurn(((GameStatusMessage)igm).getPlayerIndex()));
			System.out.println("NOW SEND PLAYERINDEX FOR CLEINT ,, TURN MESSAGE");
		}
		
	}
	
	
	
	
	private boolean checkTurn(int playerIndex) {
		if (playerIndex==currentPlayerIndex)
		return true;
		else return false;
	}

	public  void setPlayerColorAndTurn(Player player, int index) {
			if (index == 0){
				player.setColor(ColorChoice.blue);
				player.setYourTurn(true);
			}
			else if(index==1){
				player.setColor(ColorChoice.red);
				player.setYourTurn(false);
			}
			else if(index==2){
				player.setColor(ColorChoice.green);
				player.setYourTurn(false);
			}
			else if(index==3){
				player.setColor(ColorChoice.purple);
				player.setYourTurn(false);
			}
			
		}

}

