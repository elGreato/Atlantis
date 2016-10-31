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
		// send hbox Player for each player
		// give each player an arraylist and delete it from cards here
		for (int i = 0; i < numberOfPlayers; i++) {
			Player player = new Player(users.get(i).getUserInfo().getUsername());
			
			users.get(i).sendMessage(new PlayerMessage(getName(), player, cardsForPlayers(i, player), player.countVictoryPoints(),i));
			

		}

	}

	private ArrayList<Card> cardsForPlayers(int playerIndex, Player player) {
		ArrayList<Card> result = new ArrayList<>();
		if (playerIndex == 0) {
			for (int i = 0; i < 4; i++) {
				Card card = cards.deal();
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player1 a card");
				System.out.println("size of player 1 hand now is " + player.getPlayerHand().getNumCards());
			}
		} else if (playerIndex == 1) {
			for (int i = 0; i < 5; i++) {
				Card card = cards.deal();
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player2 a card");
				System.out.println("size of player 2 hand now is " + player.getPlayerHand().getNumCards());
			}
		} else if (playerIndex == 2) {
			for (int i = 0; i < 6; i++) {
				Card card = cards.deal();
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player3 a card");
			}
		} else if (playerIndex == 3) {
			for (int i = 0; i < 7; i++) {
				Card card = cards.deal();
				result.add(card);
				player.getPlayerHand().addCard(card);
				System.out.println("sent player4 a card");
			}
		}

		return result;
	}

	// Here messages from clients arrive
	public synchronized void processMessage(InGameMessage igm) {

	}

}
