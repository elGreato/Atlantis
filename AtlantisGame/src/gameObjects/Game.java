package gameObjects;

// This class is part of the Gamer Server, should be moved from this package
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import messageObjects.AtlantisMainLandMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerLeftMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.BuyCardsMessage;
import messageObjects.turnMessages.CardsBoughtMessage;
import messageObjects.turnMessages.CloseGameMessage;
import messageObjects.turnMessages.DrawMessage;
import messageObjects.turnMessages.EndMYTurnMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.LastBillMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PaymentDoneMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ResultMessage;
import messageObjects.turnMessages.RevertTurnMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.TurnMessage;
import messageObjects.turnMessages.WaterPaidMessage;
import server.backend.AIUser;
import server.backend.Lobby;
import server.backend.LobbyInterface;
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
	private LobbyInterface lobby;
	private DeckOfCards cards;
	private DeckOfLandTiles deckA;
	private DeckOfLandTiles deckB;
	private ArrayList<Player> players = new ArrayList<>();
	// arraylist to save removed cards and treasures in case of revert
	private ArrayList<Card> removedCards = new ArrayList<>();
	private LandTile removedTreasure = null;
	private int removedTreasureIndex = -1;
	private AtlantisTile atlantis = new AtlantisTile();
	private MainLand mainland = new MainLand();
	// base for water
	private ArrayList<WaterTile> base;
	private int currentPlayerIndex;
	// index for players who paid their last bill
	private int paidLastBill = 0;

	private Player currentPlayer;
	private Pawn selectedPawn;
	private LandTile selectedLand;
	private LandTile initialLand;

	private boolean gameOver = false;
	private int playersFinished = 0;

	// Constructor (doesn't start game)
	public Game(String name, String password, int maxPlayers, User creator, Lobby lobby) {

		this.name = name;
		this.password = password;
		this.maxPlayers = maxPlayers;
		users = new ArrayList<User>();
		users.add(creator);
		this.lobby = lobby;
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
		for (LandTile tile : deckA.getDeckOfTiles()) {
			tile.setDeckAorB("a");
		}
		deckB = new DeckOfLandTiles();
		for (LandTile tile : deckB.getDeckOfTiles()) {
			tile.setDeckAorB("b");
		}
		cards = new DeckOfCards();
		// create water tiles which will hold land tiles later on
		for (int i = 0; i < 53; i++) {
			WaterTile water = new WaterTile(i);
			base.add(water);
		}
		distributeLandTiles();

		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new WaterMessage(getName(), base));

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
			player.getPlayerHand().setCards(cardsForPlayers(i, player));
			for (Pawn p : player.getPawns()) {
				p.setOwner(player);
				p.setPawnColor(player.getColor());
			}
			users.get(i).sendMessage(new PlayerMessage(getName(), player));
			players.add(player);
		}

		currentPlayerIndex = 0;
		currentPlayer = players.get(currentPlayerIndex);

		// send the list of players for client to set opponents
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new OpponentMessage(getName(), players));
		}

		// check whose turn it is and inform client to start
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new GameStatusMessage(getName(), true, currentPlayer));

		}

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
			currentPlayer = players.get(message.getPlayerIndex());
			currentPlayerIndex = currentPlayer.getPlayerIndex();
			Card selectedCard = null;

			for (Card card : currentPlayer.getPlayerHand().getCards()) {
				if (message.getCard().getColor() != null && card.getCardId() == message.getCard().getCardId()) {
					selectedCard = card;
					removedCards.add(card);
					currentPlayer.getPlayerHand().removeCardFromHand(card);
					break;
				} else if (message.getCard().getColor() == null) {
					selectedCard = message.getCard();
					break;
				}
			}
			selectedPawn = null;

			for (Pawn p : currentPlayer.getPawns()) {
				if (p.getPawnId() == message.getPawn().getPawnId()) {
					selectedPawn = p;
					selectedPawn.setPawnSelected(true);
				} else
					p.setPawnSelected(false);

			}
			if (removedCards.size() <= 1) {
				selectedPawn.setStartingLocation(selectedPawn.getNewLocation());
			}
			System.out.println("Starting location: " + selectedPawn.getStartingLocation());
			performTurn(selectedCard, selectedPawn);
		}
		if (igm instanceof BuyCardsMessage)

		{
			BuyCardsMessage message = ((BuyCardsMessage) igm);
			ArrayList<Card> purchase = new ArrayList<>();
			ArrayList<LandTile> sold = new ArrayList<>();
			if (message.getCurrentPlayerIndex() == currentPlayerIndex) {
				int amount = 0;
				for (LandTile t : message.getTreasuresChosen()) {
					amount += t.getLandValue();
				}
				if (amount < cards.getNumberOfCardsInDeck()) {
					purchase = cardsPurchase(amount);
				} else {
					cards.shuffle();
					purchase = cardsPurchase(amount);

				}
				for (LandTile gone : message.getTreasuresChosen()) {
					sold.add(gone);
					currentPlayer.getPlayerHand().getTreasures().remove(gone);
				}
				int numberOfPlayers = getNumOfRegisteredPlayers();
				for (int i = 0; i < numberOfPlayers; i++) {
					users.get(i).sendMessage(new CardsBoughtMessage(getName(), currentPlayer, purchase, sold,
							currentPlayer.countVictoryPoints()));

				}

			}
		}
		if (igm instanceof WaterPaidMessage) {
		
			WaterPaidMessage message = (WaterPaidMessage) igm;
			System.out.println("waterpaid message received from : "+message.getPlayerIndex()+" name: "+users.get(message.getPlayerIndex()).getUserInfo().getUsername()+
					" to make sure, player is also in players list "+players.get(message.getPlayerIndex()));
			Player player = players.get(message.getPlayerIndex());
			System.out.println("Cards chosen SERVER size333 "+message.getCardsChosen().size());
			System.out.println("land chosen SERVER size333 "+message.getTreasuresChosen().size());
			Iterator<Card> it = player.getPlayerHand().getCards().iterator();
			while (it.hasNext()) {
				Card c = it.next();
	
				for (int k = 0; k < message.getCardsChosen().size(); k++) {
					if (c.getCardId() == message.getCardsChosen().get(k).getCardId()) {
						System.out.println("Card from owner "+c.getOwner().getPlayerName()+ " Color: "+c.getColor().toString());
						it.remove();					
					}
				}
			}
			Iterator<LandTile> it2 = player.getPlayerHand().getTreasures().iterator();
			while (it2.hasNext()) {
				LandTile t = it2.next();
				for (int k = 0; k < message.getTreasuresChosen().size(); k++) {
					if (t.getTileId() == message.getTreasuresChosen().get(k).getTileId()) {
						it2.remove();			
					}
				}
			}
			System.out.println("land size after delete " + player.getPlayerHand().getTreasures().size());
			// HERE ADD THE REFERSH TO COUNT AND VP
			int numberOfPlayers = getNumOfRegisteredPlayers();
			for (int i = 0; i < numberOfPlayers; i++) {
				users.get(i)
						.sendMessage(new PaymentDoneMessage(getName(), player.getPlayerIndex(),
								message.getCardsChosen(), message.getTreasuresChosen(), player.countVictoryPoints(),
								player.getPlayerHand().getNumCards()));

			}
			if (message.isNextPlayer() && !gameOver) {
				endTurn();

			}
			// ending the game
			else if (gameOver) {
				paidLastBill++;
				System.out.println("paidLastBill count " + paidLastBill);
				for (int i = 0; i < numberOfPlayers; i++) {
					users.get(i).sendMessage(
							new ServerMessage(getName(), "Game Over, wait for other players to finish paying"));

				}
				if (paidLastBill == numberOfPlayers - 1) {
					ArrayList<Integer> winners = checkWinner();
					if (winners.size() == 1) {
						lobby.addWin(users.get(winners.get(0)));
						for (int i = 0; i < numberOfPlayers; i++) {
							users.get(i).sendMessage(new ResultMessage(getName(), winners.get(0),
									players.get(winners.get(0)).getPlayerName()));
						}
						for (int k = 0; k < users.size(); k++) {
							if (k != winners.get(0)) {
								lobby.addLoss(users.get(k));
								System.out.println(" the player lose: " + users.get(k).getUserInfo().getUsername());
							}

						}
						// in case we have a draw between two or more players
					} else if (winners.size() > 1) {
						for (int i = 0; i < numberOfPlayers; i++) {
							users.get(i).sendMessage(new DrawMessage(getName(), winners));
						}
						for (Integer k = 0; k < users.size(); k++) {
							if (winners.contains(k)) {
								lobby.addTie(users.get(k));
							} else
								lobby.addLoss(users.get(k));
						}

					}

				}

			}

		}
		// in case of a player ending his turn, he gets an extra card
		if (igm instanceof EndMYTurnMessage) {
			EndMYTurnMessage message = (EndMYTurnMessage) igm;
			performEndTurn(message.getPlayerIndex(), message.isNormalEnd());

		}
		if (igm instanceof PlayerLeftMessage) {
			PlayerLeftMessage message = (PlayerLeftMessage) igm;
			int leavingPlayer = message.getPlayerIndex();
			handlePlayerLeave(leavingPlayer);
		}
		if (igm instanceof RevertTurnMessage) {
			RevertTurnMessage message = (RevertTurnMessage) igm;
			revertThisTurn(message.getPlayerIndex());

		}
		if (igm instanceof CloseGameMessage) {
			playersFinished++;
			if (playersFinished == getNumOfRegisteredPlayers()) {
				lobby.endGame(this);
			}
		}
	}

	private void revertThisTurn(int playerIndex) {
		if (currentPlayerIndex == playerIndex) {
			for (Card c : removedCards) {
				currentPlayer.addCard(c);
				System.out.println("one card found to give back to player");
			}
			if (removedTreasure != null) {
				System.out.println("a treasure is found to give back to player");
				base.get(removedTreasureIndex).getChildren().add(removedTreasure);
				currentPlayer.getPlayerHand().getTreasures().remove(removedTreasure);
			}
			removePawnFromNewTile(selectedPawn);
			selectedPawn.setNewLocation(selectedPawn.getStartingLocation());
			selectedPawn.setOldLocation(selectedPawn.getNewLocation());
			if (selectedPawn.getStartingLocation() != -1) {
				// initialLand.setPawnOnTile(selectedPawn);
				WaterTile wt = base.get(selectedPawn.getStartingLocation());
				LandTile lt = (LandTile) wt.getChildren().get(wt.getChildren().size() - 1);
				lt.setPawnOnTile(selectedPawn);

				System.out.println("pawn new location" + selectedPawn.getNewLocation());
			} else {
				atlantis.getChildren().add(selectedPawn);

			}
		}
		System.out.println("Argument: " + playerIndex + " ,CurrPlayerIndex: " + currentPlayerIndex);
		int numberOfPlayers = getNumOfRegisteredPlayers();
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new RevertTurnMessage(getName(), currentPlayerIndex, removedCards, removedTreasure,
					removedTreasureIndex, selectedPawn, selectedPawn.getStartingLocation(), initialLand));
		}
		performEndTurn(playerIndex, true);

	}

	// Method created by Kevin Neuschwander
	private void handlePlayerLeave(int leavingPlayer) {
		System.out.println("Player left");
		int numAI = 0;
		for (User u : users) {
			if (u instanceof AIUser) {
				numAI += 1;
			}
		}

		lobby.addLoss(users.get(leavingPlayer));
		if (numAI == users.size() - 1) {
			System.out.println("No humans left");
			for (User u : users) {
				if (users.indexOf(u) != leavingPlayer) {
					if (maxPlayers > 2) {
						lobby.addTie(u);
					} else {
						lobby.addWin(u);
					}

				}
			}
			lobby.endGame(this);
		} else {
			System.out.println("There are humans left");
			lobby.endGameForSpecificUser(this, users.get(leavingPlayer));
			AIUser replacement = lobby.requestAI(users);
			System.out.println("Replacement = " + replacement.getUserInfo().getUsername());
			users.set(leavingPlayer, replacement);
			players.get(leavingPlayer).setPlayerName(replacement.getUserInfo().getUsername());
			replacement.initiateGameStart(this);
			replacement.sendMessage(new WaterMessage(name, base));
			replacement.sendMessage(new PlayerMessage(name, players.get(leavingPlayer)));
			ArrayList<Player> opponents = new ArrayList<Player>();
			for (Player pl : players) {
				if (pl.getPlayerIndex() != leavingPlayer) {
					opponents.add(pl);
				}
			}
			replacement.sendMessage(new OpponentMessage(name, opponents));
			for (User u : users) {
				u.sendMessage(new PlayerLeftMessage(name, leavingPlayer, replacement.getUserInfo().getUsername()));
			}
			if (currentPlayerIndex == leavingPlayer && !removedCards.isEmpty()) {
				System.out.println("Revert turn");
				revertThisTurn(leavingPlayer);
			} else if (currentPlayerIndex == leavingPlayer) {
				System.out.println("End turn");
				performEndTurn(leavingPlayer, false);
			}

		}
	}

	// Method created by Kevin Neuschwander
	@Override
	public void handlePlayerLeave(String leavingPlayer) {
		for (User u : users) {
			if (u.getUserInfo().getUsername().equals(leavingPlayer)) {
				handlePlayerLeave(users.indexOf(u));
				break;
			}
		}

	}

	private ArrayList<Card> cardsPurchase(int amount) {

		ArrayList<Card> result = new ArrayList<>();
		amount = amount / 2;
		while (amount != 0) {

			Card c = cards.deal();
			c.setOwner(currentPlayer);
			currentPlayer.addCard(c);
			result.add(c);
			amount--;
		}
		return result;
	}

	private ArrayList<Integer> checkWinner() {
		ArrayList<Integer> result = new ArrayList<>();
		int size = players.size();
		int winner = -1;

		ArrayList<Player> sorted = new ArrayList<>(players);
		Collections.sort(sorted, new PlayersComparator());

		if (sorted.get(0).countVictoryPoints() == sorted.get(1).countVictoryPoints()) {
			result.add(sorted.get(0).getPlayerIndex(), sorted.get(1).getPlayerIndex());
			if (size == 3) {
				if (sorted.get(0).countVictoryPoints() == sorted.get(2).countVictoryPoints()) {
					result.add(sorted.get(2).getPlayerIndex());
				}
			}
			if (size == 4) {
				if (sorted.get(0).countVictoryPoints() == sorted.get(3).countVictoryPoints()) {
					result.add(sorted.get(3).getPlayerIndex());
				}
			}
		} else {
			winner = Collections.max(sorted, new PlayersComparator()).getPlayerIndex();
			result.clear();
			result.add(winner);
		}
		return result;

	}

	private void performEndTurn(int playerIndex, boolean normalEnd) {
		if (!normalEnd) {
			Player player = players.get(playerIndex);
			ArrayList<Card> newCards = dealCards(player);
			Card extra = cards.deal();
			extra.setOwner(player);
			player.addCard(extra);
			newCards.add(extra);
			users.get(player.getPlayerIndex())
					.sendMessage(new EndMYTurnMessage(getName(), player.getPlayerIndex(), newCards));
			endTurn();
		} else
			endTurn();

	}

	private void performTurn(Card selectedCard, Pawn selectedPawn) {

		ArrayList<Card> newCards = null;

		boolean foundLand = false;
		boolean giveTreasure = false;
		boolean waterHasTile = false;
		boolean nextPlayer = false;
		boolean connectedWater = false;

		int waterPassedCount = 0;
		int waterBill = 0;
		LandTile treasure = null;
		selectedLand = null;
		// in case of revert
		for (int i = 0; selectedPawn.getNewLocation() != -1
				&& i < base.get(selectedPawn.getNewLocation()).getChildren().size(); i++) {
			if (((LandTile) base.get(selectedPawn.getNewLocation()).getChildren().get(i)).hasPawn()
					&& (((LandTile) base.get(selectedPawn.getNewLocation()).getChildren().get(i)).getPawnOnTile()
							.getPawnId() == selectedPawn.getPawnId())) {
				initialLand = ((LandTile) base.get(selectedPawn.getNewLocation()).getChildren().get(i));
			}
		}

		// loop through the base and assign watertile
		for (int f = selectedPawn.getNewLocation(); f < base.size() && !foundLand; f++) {
			WaterTile water = null;
			if (f == 52) {
				water = base.get(f);
			} else {
				water = base.get(f + 1);
			}
			// check if water has tiles
			int topNode = 0;
			if (water.getChildren().size() > 0) {
				topNode = water.getChildren().size() - 1;
				waterHasTile = true;
				connectedWater = false;
			} else {
				if (!connectedWater) {
					connectedWater = true;
					waterPassedCount++;
					int i = payForWater(water);
					waterBill += i;
				}
			}
			// get the top tile on that water
			if (waterHasTile && water.getChildren().get(topNode) instanceof LandTile) {
				waterHasTile = false;
				LandTile land = (LandTile) water.getChildren().get(topNode);
				// does this tile has the same color ?
				if (selectedCard.getColor() != null && land.getColor().equals(selectedCard.getColor())
						&& !land.hasPawn()) {

					land.setPawnOnTile(selectedPawn);
					selectedLand = land;
					selectedPawn.setNewLocation(base.indexOf(water));
					foundLand = true;
					giveTreasure = true;
					nextPlayer = true;
					selectedPawn.setPawnSelected(false);
					// give a new card
					newCards = dealCards(currentPlayer);
					// remove pawn stamp
					removePawnFromOldTile(selectedPawn);

				}
				// here a player played a pawn and landed on another that has a
				// pawn, if he has no other cards, he gets 2 extra free
				if (selectedCard.getColor() != null && land.getColor().equals(selectedCard.getColor()) && land.hasPawn()
						&& !foundLand) {
					ArrayList<Card> extraCards = new ArrayList<>();
					selectedPawn.setNewLocation(base.indexOf(water));
					land.setTempPawn(selectedPawn);
					removePawnFromOldTile(selectedPawn);
					selectedPawn.setPawnSelected(false);
					selectedLand = land;
					foundLand = true;
					giveTreasure = false;
					if (currentPlayer.getPlayerHand().getNumCards() == 0) {
						Card c1 = cards.deal();
						Card c2 = cards.deal();
						c1.setOwner(currentPlayer);
						c2.setOwner(currentPlayer);
						currentPlayer.addCard(c1);
						currentPlayer.addCard(c2);
						extraCards.add(c1);
						extraCards.add(c2);

					}
					users.get(currentPlayer.getPlayerIndex())
							.sendMessage(new PlayAnotherCardMessage(getName(), selectedPawn, extraCards));

				}
			}
			if (((!foundLand && f == base.size() - 1)) || (selectedCard.getColor() == null && f == base.size() - 1)) {
				System.out.println("Reached Main Land , index of new location is " + f + 1);
				selectedPawn.setNewLocation(f + 1);
				selectedPawn.setReachedMainLand(true);
				mainland.getPawns().add(selectedPawn);
				removePawnFromOldTile(selectedPawn);
				foundLand = true;
				selectedLand = null;
				selectedPawn.setPawnSelected(false);
				newCards = dealCards(currentPlayer);

				if (mainland.getPawns().size() >= 3) {
					int currentPawnsCount = 0;
					for (Pawn p : currentPlayer.getPawns()) {
						if (p.ReachedMainLand() == true)
							currentPawnsCount++;
						else
							currentPawnsCount--;
					}
					if (currentPawnsCount == 3) {
						gameOver = true;
						nextPlayer = true;
					}
				}

				if (!gameOver) {
					nextPlayer = true;
					giveTreasure = true;

				}

			}

		}
		if (selectedPawn.getNewLocation() > 0 && foundLand && giveTreasure) {
			treasure = giveTreasureToPlayer(selectedPawn.getNewLocation());
			if (treasure != null) {
				users.get(currentPlayerIndex)
						.sendMessage(new ServerMessage(getName(), "found Treasure: " + treasure.getColor().toString()));

			} else
				users.get(currentPlayerIndex).sendMessage(new ServerMessage(getName(), "No treasure found"));

		}
		int victoryPoints = currentPlayer.countVictoryPoints();
		int cardsCount = currentPlayer.getPlayerHand().getNumCards();
		int numberOfPlayers = getNumOfRegisteredPlayers();

		if (!gameOver) {
			for (int i = 0; i < numberOfPlayers; i++) {
				users.get(i)
						.sendMessage(new RefreshPlayerMessage(getName(), currentPlayer, selectedLand, selectedPawn,
								selectedCard, treasure, newCards, waterBill, waterPassedCount, nextPlayer,
								victoryPoints, cardsCount));
			}
		} else if (gameOver) {

			endThisGame();
		}
	}

	private void endThisGame() {

		// now similar stuff to a normal turn just without giving treasures and

		System.out.println("End this game method");
		for (Player p : players) {
			// stuff
			boolean connectedWater = false;
			int waterPassedCount = 0;
			int waterBill = 0;
			System.out.println("looping players");
			for (Pawn pawn : p.getPawns()) {
				System.out.println("looping pawns");
				System.out.println(pawn.ReachedMainLand());
				if (!pawn.ReachedMainLand()) {
					System.out.println("*****");
					for (int f = pawn.getNewLocation() + 1; f < base.size(); f++) {
						WaterTile water = base.get(f);

						if (water.getChildren().size() > 0) {
							connectedWater = false;
						} else if (!connectedWater) {
							connectedWater = true;
							waterPassedCount++;
							int i = payForWater(water);
							waterBill += i;
						}

					}
				}
				pawn.setReachedMainLand(true);

			}

			users.get(p.getPlayerIndex()).sendMessage(new LastBillMessage(getName(), waterPassedCount, waterBill));
		}
	}

	private int payForWater(WaterTile water) {
		int waterIndex = base.indexOf(water);
		WaterTile waterBefore;
		WaterTile waterAfter;
		LandTile landBefore;
		LandTile landAfter;
		int valueBefore = 0;
		int valueAfter = 0;
		boolean foundAfterWaterWithTiles = false;

		if (waterIndex != 0) {
			waterBefore = base.get(waterIndex - 1);
			if (waterBefore.getChildren() != null) {
				landBefore = (LandTile) waterBefore.getChildren().get(waterBefore.getChildren().size() - 1);
				valueBefore = landBefore.getLandValue();
			}
		} else {
			waterBefore = null;
			landBefore = null;
			valueBefore = 0;

		}

		if (waterIndex != base.size() - 1) {

			int waterAfterIndex = waterIndex + 1;
			while (!foundAfterWaterWithTiles && waterAfterIndex < 53) {
				waterAfter = base.get(waterAfterIndex);
				if (waterAfter.getChildren().size() != 0) {
					landAfter = (LandTile) waterAfter.getChildren().get(waterAfter.getChildren().size() - 1);

					valueAfter = landAfter.getLandValue();
					foundAfterWaterWithTiles = true;
				} else
					waterAfterIndex++;
			}

		} else if (waterIndex == base.size() - 1) {
			waterAfter = null;
			landAfter = null;
			valueAfter = 0;
		}
		if (valueBefore < valueAfter)
			return valueBefore;
		else
			return valueAfter;

	}

	public void endTurn() {

		// end current player turn
		removedTreasure = null;
		removedCards.clear();
		if (currentPlayerIndex == players.size() - 1) {
			currentPlayerIndex = 0;
			currentPlayer = players.get(currentPlayerIndex);
		} else {
			currentPlayerIndex++;
			currentPlayer = players.get(currentPlayerIndex);
		}
		int numberOfPlayers = getNumOfRegisteredPlayers();
		for (int i = 0; i < numberOfPlayers; i++) {
			users.get(i).sendMessage(new GameStatusMessage(getName(), true, currentPlayer));

		}

	}

	public ArrayList<Card> dealCards(Player player) {
		ArrayList<Card> result = new ArrayList<>();
		// check if there is enough cards
		// Max number is 3 cards for each pawn
		if (cards.getNumberOfCardsInDeck() < 4) {
			cards.shuffle();
		}

		// after each round a player gets a card anyway
		Card turnCard = cards.deal();
		turnCard.setOwner(player);
		player.getPlayerHand().addCard(turnCard);
		result.add(turnCard);
		// for each pawn in mainland he gets an extra one
		for (Pawn p : player.getPawns()) {
			if (p.ReachedMainLand()) {
				Card newCard = cards.deal();
				newCard.setOwner(player);
				player.getPlayerHand().addCard(newCard);
				result.add(newCard);
			}
		}
		// Here we add our own rule, the JOKER Card which allows player to use
		// it to jump
		// with a pawn right away to main land, however the chance to get it is
		// relatively low
		Random rand = new Random();
		int lucky = rand.nextInt(30);
		if (lucky == 13) {
			Card joker = new Card(666, null);
			joker.setOwner(currentPlayer);
			player.getPlayerHand().addCard(joker);
			result.add(joker);
			users.get(currentPlayerIndex).sendMessage(new ServerMessage(getName(), "WOW! YOU GOT A JOKER CARD!"));
		}
		return result;
	}

	private void removePawnFromOldTile(Pawn selectedPawn) {
		WaterTile w = null;
		if (selectedPawn.getOldLocation() != -1) {
			w = base.get(selectedPawn.getOldLocation());
			((LandTile) w.getChildren().get((w.getChildren().size() - 1))).removePawn(selectedPawn);

		} else
			atlantis.getChildren().remove(selectedPawn);
	}

	private void removePawnFromNewTile(Pawn selectedPawn) {
		WaterTile w = null;
		if (selectedPawn.getNewLocation() != -1 && selectedPawn.getNewLocation() != 53) {
			w = base.get(selectedPawn.getNewLocation());
			((LandTile) w.getChildren().get((w.getChildren().size() - 1))).removePawn(selectedPawn);

		} else if (selectedPawn.getNewLocation() == 53) {
			mainland.getChildren().remove(selectedPawn);
			selectedPawn.setReachedMainLand(false);
		}
	}

	private LandTile giveTreasureToPlayer(int f) {
		boolean gotIt = false;
		LandTile treasure = null;
		int waterIndex = -1;
		while (!gotIt && f + waterIndex >= 0) {
			WaterTile previousWater = base.get(f + waterIndex);
			if (previousWater.getChildren().size() != 0
					&& previousWater.getChildren().get(previousWater.getChildren().size() - 1) instanceof LandTile
					&& !((LandTile) previousWater.getChildren().get(previousWater.getChildren().size() - 1))
							.hasPawn()) {

				treasure = (LandTile) previousWater.getChildren().remove(previousWater.getChildren().size() - 1);
				currentPlayer.getPlayerHand().addTreasure(treasure);
				removedTreasure = treasure;
				removedTreasureIndex = base.indexOf(previousWater);

				gotIt = true;
			}

			waterIndex -= 1;

		}
		return treasure;
	}

	private boolean checkTurn(int playerIndex) {
		if (playerIndex == currentPlayerIndex) {
			return true;
		} else
			return false;
	}

	public void setPlayerColorAndTurn(Player player, int index) {
		if (index == 0) {
			player.setColor(ColorChoice.blue);
		} else if (index == 1) {
			player.setColor(ColorChoice.pink);
		} else if (index == 2) {
			player.setColor(ColorChoice.green);
		} else if (index == 3) {
			player.setColor(ColorChoice.brown);
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
		for (int i = 0; i < base.size(); i++) {
			base.get(i).convertToChildren();
		}

	}

	private ArrayList<Card> cardsForPlayers(int playerIndex, Player player) {
		ArrayList<Card> result = new ArrayList<>();

		for (int i = playerIndex + 4; i > 0; i--) {
			Card card = cards.deal();
			card.setOwner(player);
			result.add(card);
			player.addCard(card);
		}
		return result;
	}

	// Getters for kevin
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
		return users.size();
	}

	// adds new player to game
	public void addUser(User user) {
		users.add(user);

	}

}
