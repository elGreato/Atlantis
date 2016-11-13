package client.game;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.Pawn;
import gameObjects.LandTile;
import gameObjects.Player;
import gameObjects.WaterTile;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.TurnMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private ClientLobbyInterface msgOut;

	protected Player currentPlayer;

	public String getGameName() {
		return gameName;
	}

	public GameModel(String gameName, LobbyModel lobbyModel, GameView gameView) {
		this.gameName = gameName;
		msgOut = lobbyModel;
		this.view = gameView;

	}

	// Here messages from server arrive
	public void processMessage(InGameMessage msgIn) {
		// first we receive the main board stuff
		if (msgIn instanceof WaterMessage) {
			view.addRecAndText(((WaterMessage) msgIn).getBase());
		}

		// the atlantis and the main land
		if (msgIn instanceof AtlantisMainLandMessage) {
			view.placeAtlantisMainLand(((AtlantisMainLandMessage) msgIn).getAtlantis(),
					((AtlantisMainLandMessage) msgIn).getMainland());
		}

		// now the players
		if (msgIn instanceof PlayerMessage) {
			currentPlayer = (((PlayerMessage) msgIn).getPlayer());

			for (Card c : currentPlayer.getPlayerHand().getCards()) {
				addClickToCard(c);
			}
			for (Pawn p : currentPlayer.getPawns()) {
				p.setOnMouseClicked(e -> view.handlePawn(p));
			}

			view.showPlayer(currentPlayer);

		}

		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");
			OpponentMessage message = ((OpponentMessage) msgIn);
			for (int i = 0; i < message.getOpponents().size(); i++) {
				if (currentPlayer.getPlayerIndex() != message.getOpponents().get(i).getPlayerIndex()) {
					view.setOpponent(message.getOpponents().get(i));
					currentPlayer.getOpponents().add((message).getOpponents().get(i));
				}
			}
		}
		// message about state of the game
		if (msgIn instanceof GameStatusMessage) {
			if (((GameStatusMessage) msgIn).isStarted())
				startTurn(((GameStatusMessage) msgIn).getCurrentPlayer());
		}

		if (msgIn instanceof ServerMessage) {
			view.showMessageFromServer(((ServerMessage) msgIn).getTheMessage());
		}

		// a turn message
		if (msgIn instanceof TurnMessage) {
			System.out.println("checking if my turn");
			if (((TurnMessage) msgIn).isYourTurn()) {
				if (scanPawns() != null) {
					if (scanCards() != null) {
						System.out.println("sent my index" + currentPlayer.getPlayerIndex());
						currentPlayer.setYourTurn(true);
						Card selectedCard = null;
						Pawn selectedPawn = null;

						selectedPawn = scanPawns();
						selectedCard = scanCards();

						currentPlayer.getPlayerHand().removeCardFromHand(selectedCard);
						view.removeCardFromHand(selectedCard);
						msgOut.sendMessage(new PawnCardSelectedMessage(gameName, currentPlayer.getPlayerIndex(),
								selectedPawn, selectedCard));
					} else
						view.selectCardPlease();
				} else
					view.selectPawnPlease();

			} else
				view.showNotYourTurnAlert();

		}
		// update players
		if (msgIn instanceof RefreshPlayerMessage) {
			RefreshPlayerMessage message = (RefreshPlayerMessage) msgIn;
			System.out.println("REFERESH REC");
			LandTile treasure = message.getTreasure();
			Card newCard = message.getNewCard();
			if (newCard != null && message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				addCardToPlayer(newCard);

			} else
				System.out.println("didn't get new card or nt ur turn");
			if (treasure != null) {
				if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					givePlayerTreasure(treasure);
				} else {
					giveEnemyTreasure(message.getCurrentPlayer().getPlayerIndex(), treasure);
				}
				removeTreasureFromBoard(treasure);
			}
			Pawn selectedPawn = null;
			if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				for (Pawn pp : currentPlayer.getPawns()) {
					if (message.getSelectedPawn().getPawnId() == pp.getPawnId())
						selectedPawn = pp;
				}
			} else {
				for (Player enemy : currentPlayer.getOpponents()) {
					if (enemy.getPlayerIndex() == message.getCurrentPlayer().getPlayerIndex()) {
						for (Pawn pp : enemy.getPawns()) {
							if (pp.getPawnId() == message.getSelectedPawn().getPawnId()) {
								selectedPawn = pp;
							}
						}
					}
				}
			}

			movePawn(currentPlayer.getPlayerIndex(), selectedPawn, message.getSelectedLand());

		}
		// inform player to play another card
		if (msgIn instanceof PlayAnotherCardMessage) {
			view.playerAnother();

		}

	}

	private Pawn scanPawns() {
		Pawn foundPawn = null;
		for (Pawn pawn : currentPlayer.getPawns()) {
			if (pawn.isPawnSelected()) {
				foundPawn = pawn;
				System.out.println("found pown in client");
				break;
			}
		}
		return foundPawn;
	}

	private void addClickToCard(Card c) {
		c.setOnMouseClicked(e -> view.handleCard(c));
		view.createCardView(c);

	}

	private void addCardToPlayer(Card newCard) {
		currentPlayer.addCard(newCard);
		newCard.setOwner(currentPlayer);
		addClickToCard(newCard);

	}

	private void movePawn(int indexOfPlayer, Pawn selectedPawn, LandTile selectedLand) {
		Pawn viewPawn = null;

		if (currentPlayer.getPawns().contains(selectedPawn)) {
			System.out.println("pawn selectedddddddd");
			viewPawn = selectedPawn;
		} else {
			for (int i = 0; i < currentPlayer.getOpponents().size(); i++) {
				if (currentPlayer.getOpponents().get(i).getPawns().contains(selectedPawn)) {
					viewPawn = selectedPawn;
				}
			}
		}
		for (int g = 0; g < view.getBase().size()&&selectedLand!=null; g++) {
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(selectedLand)) {
				((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).setPawnOnTile(viewPawn);
				((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).convertPawns();
			}
			

		} if (selectedLand==null){
			System.out.println("raeched the last in client");
			selectedPawn.setOnMouseClicked(null);
			view.addPawnToMainLand(selectedPawn);
			
		}

	}

	private void givePlayerTreasure(LandTile treasure) {
		if (treasure != null) {
			currentPlayer.getPlayerHand().addTreasure(treasure);
			view.givePlayerTreasure(treasure);

		}
	}

	private void giveEnemyTreasure(int indexOfPlayer, LandTile treasure) {
		if (treasure != null) {
			System.out.println("GIVE ENEMY TREASURE ACT");
			view.giveEnemyTreasure(indexOfPlayer, treasure);
		}

	}

	private void removeTreasureFromBoard(LandTile treasure) {
		for (int g = 0; g < view.getBase().size(); g++) {
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(treasure)) {
				System.out.println("Foudn the treasure ");
				tempWater.getChildren().remove(treasure);
			}

		}

	}

	public void startTurn(Player player) {

		view.gameStarted();
		if (currentPlayer.getPlayerIndex() == player.getPlayerIndex()) {
			view.yourTurn();
			currentPlayer.setYourTurn(true);

		} else {
			view.notYourTurn(player.getPlayerName());
			currentPlayer.setYourTurn(false);
		}
	}

	public void tryPlayCard() {

		msgOut.sendMessage(new GameStatusMessage(gameName, currentPlayer.getPlayerIndex()));

	}

	private Card scanCards() {
		Card cardSelected = null;
		for (Card card : currentPlayer.getPlayerHand().getCards()) {
			if (card.isCardSelected()) {
				cardSelected = card;

				break;
			}
		}

		return cardSelected;
	}
}