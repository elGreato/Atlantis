package gameObjects;

// This class is part of the Gamer Server, should be moved from this package
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.TurnMessage;
import server.backend.Lobby;
import server.backend.User;

public class Game implements GameInterface {

	private String name;
	private String password;
	// send Message trough sendMessage method of User, get player name stats etc
	// through userInfo field of User
	private ArrayList<User> users;
	private int maxPlayers;
	// Invoke lobby.addWin, lobby.addLoss & lobby.addTie methods on users at the
	// end of the game
	private Lobby lobby;
	private DeckOfCards cards;
	private DeckOfLandTiles deckA;
	private DeckOfLandTiles deckB;
	private ArrayList<Player> players = new ArrayList<>();
	private AtlantisTile atlantis = new AtlantisTile();
	private MainLand mainland = new MainLand();
	// base for water
	private ArrayList<WaterTile> base;
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

		base = new ArrayList<>();
		deckA = new DeckOfLandTiles();
		deckB = new DeckOfLandTiles();
		cards = new DeckOfCards();
		// create water tiles which will hold land tiles later on
		for (int i = 1; i < 54; i++) {

			WaterTile water = new WaterTile(i);
			base.add(water);
		}
		distributeLandTiles();

		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new WaterMessage(getName(), base));

		}

		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i)
					.sendMessage(new DeckLandTileMessage(getName(), deckA.getDeckOfTiles(), deckB.getDeckOfTiles()));

		}
		// send the atlantis and main land
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new AtlantisMainLandMessage(getName(), atlantis, mainland));

		}
		// send Player for each user

		for (int i = 0; i < numberOfPlayers; i++) {
			Player player = new Player(users.get(i).getUserInfo().getUsername());
			setPlayerColorAndTurn(player, i);
			player.setPlayerIndex(i);

			users.get(i).sendMessage(new PlayerMessage(getName(), player, cardsForPlayers(i, player), i));
			players.add(player);
		}
		currentPlayerIndex = 0;

		// send the list of players for client to set opponents
		for (int i = 0; i < numberOfPlayers; i++) {

			users.get(i).sendMessage(new OpponentMessage(getName(), players));

		}
		for (int i = 0; i < numberOfPlayers; i++) {

			users.get(i).sendMessage(new GameStatusMessage(getName(), true, players.get(currentPlayerIndex)));

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
		// message from client asking who is first player and shall we start ?
		if (igm instanceof GameStatusMessage) {
			users.get(((GameStatusMessage) igm).getPlayerIndex()).sendMessage(new TurnMessage(
					((GameStatusMessage) igm).getGameName(), checkTurn(((GameStatusMessage) igm).getPlayerIndex())));

		}
		// message from client telling server what pawn and card are selected
		// from which player
		if (igm instanceof PawnCardSelectedMessage) {
			PawnCardSelectedMessage message = (PawnCardSelectedMessage) igm;
			Player player = players.get(message.getPlayerIndex());
			for (Card c : player.getPlayerHand().getCards()) {
				System.out.println("cards that player has in server" + c.getCardId());
			}
			ColorChoice selectedColor = null;
			for (Card card : player.getPlayerHand().getCards()) {
				if (card.getCardId() == message.getCardId()) {
					selectedColor = card.getColor();
					player.getPlayerHand().removeCardFromHand(card);
					System.out.println(card.getCardId() + "remmoved from hand from server side");
					break;
				}
			}

			player.getPawns().get(message.getPawnId()).setPawnSelected(true);

			Pawn selectedPawn = null;

			for (Pawn pawn : player.getPawns()) {
				if (pawn.isPawnSelected()) {
					selectedPawn = pawn;
					System.out.println("found the selected pawn in server its ID is : " + pawn.getPawnId());
				}
			}
			// now the server shall give the player a treasure if any and move
			// the pawn
			// THIS IS NOT WORKING
			boolean foundLand = false;
			for (int f = 0; f < base.size() && !foundLand; f++) {
				WaterTile water = base.get(f);
			
				int topNode = water.getChildren().size()-1;

				if (water.getChildren().get(topNode) != null) {
					LandTile land = (LandTile) water.getChildren().get(topNode);
					if (land.getColor().equals(selectedColor)) {
						System.out.println("found a landtile with the color" + land.getColor().toString() + "and ID: "
								+ land.getTileId());
						land.setPawnOnTile(selectedPawn);
						selectedPawn.setLocation(base.indexOf(water));
						foundLand = true;
					}

				}
				if (f >= 1&&foundLand) {
					WaterTile previousWater = base.get(f - 1);

					if (previousWater.getChildren() != null) {

						LandTile treasure = (LandTile) previousWater.getChildren()
								.remove(previousWater.getChildren().size() - 1);
						player.getPlayerHand().addTreasure(treasure);
						System.out.println("foudnd a treasure with value " + treasure.getLandValue() + "and color "
								+ treasure.getColor().toString());
					}
				}
			}
		}

	}

	private boolean checkTurn(int playerIndex) {
		if (playerIndex == currentPlayerIndex)
			return true;
		else
			return false;
	}

	public void setPlayerColorAndTurn(Player player, int index) {
		if (index == 0) {
			player.setColor(ColorChoice.blue);
			player.setYourTurn(true);
		} else if (index == 1) {
			player.setColor(ColorChoice.red);
			player.setYourTurn(false);
		} else if (index == 2) {
			player.setColor(ColorChoice.green);
			player.setYourTurn(false);
		} else if (index == 3) {
			player.setColor(ColorChoice.purple);
			player.setYourTurn(false);
		}

	}

	public void distributeLandTiles() {
		// Distribution of Land Tiles according to the rules the first 10 stacks
		// are doubled ..

		// DeckA
		for (int i = 0; i < 26; i++) {
			LandTile tile = deckA.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}
		for (int i = 0; i < 10; i++) {
			LandTile tile = deckA.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}
		for (int i = 21; i < 26; i++) {
			LandTile tile = deckA.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}

		// DeckB
		for (int i = 27; i < 53; i++) {
			LandTile tile = deckB.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}
		for (int i = 27; i < 33; i++) {
			LandTile tile = deckB.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}
		for (int i = 43; i < 53; i++) {
			LandTile tile = deckB.getDeckOfTiles().remove(0);
			base.get(i).addLand(tile);
		}
		// now convert the arraylist to children
		for(int i=0; i<base.size();i++){
			base.get(i).convertToChildren();
		}

	}

}
