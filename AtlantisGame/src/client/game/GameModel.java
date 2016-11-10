package client.game;

import java.util.Iterator;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.ColorChoice;
import gameObjects.Pawn;
import gameObjects.LandTile;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import messageObjects.AtlantisMainLandMessage;

import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.TurnMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private ClientLobbyInterface msgOut;

	private Player currentPlayer;

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
			view.player = currentPlayer;
			view.showPlayer();
		}

		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");

			for (int i = 0; i < ((OpponentMessage) msgIn).getOpponents().size(); i++) {
				if (!currentPlayer.getPlayerName()
						.equalsIgnoreCase((((OpponentMessage) msgIn).getOpponents().get(i).getPlayerName()))) {
					view.setOpponent(currentPlayer, ((OpponentMessage) msgIn).getOpponents().get(i));
					currentPlayer.getOpponents().add(((OpponentMessage) msgIn).getOpponents().get(i));
				}
			}
		}
		// message about state of the game
		if (msgIn instanceof GameStatusMessage) {

			if (((GameStatusMessage) msgIn).isStarted())
				startTurn(((GameStatusMessage) msgIn).getCurrentPlayer().getPlayerName());

		}

		// a turn message
		if (msgIn instanceof TurnMessage) {

			if (((TurnMessage) msgIn).isYourTurn()) {
				Card selectedCard = null;
				Pawn selectedPawn = null;
				
				for (Pawn pawn : currentPlayer.getPawns()) {
					if (pawn.isPawnSelected()) {
						selectedPawn = pawn;
						break;
					}
				}
				for (Card card : currentPlayer.getPlayerHand().getCards()) {
					if (card.isCardSelected()) {
						selectedCard = card;
						currentPlayer.getPlayerHand().removeCardFromHand(card);
						view.removeCardFromHand(card);
						break;
					}
				}
				

				msgOut.sendMessage(new PawnCardSelectedMessage(gameName, currentPlayer.getPlayerIndex(),
						selectedPawn.getPawnId(), selectedCard.getCardId()));
/*				for (Pawn pawn : currentPlayer.getPawns()) {
					if (pawn.isPawnSelected()) {
						Event.fireEvent(pawn, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY,
								1, true, true, true, true, true, true, true, true, true, true, null));
					}
				}*/

			} else
				view.showNotYourTurnAlert();
		}
		// update players
		if (msgIn instanceof RefreshPlayerMessage) {
			RefreshPlayerMessage message = (RefreshPlayerMessage) msgIn;
		
			LandTile treasure = message.getTreasure();
			Card newCard = message.getNewCard();
			if (newCard != null && message.getCurrentPlayer().getPlayerIndex()  == currentPlayer.getPlayerIndex()) {
				addCardToPlayer(newCard);

			}
			if (treasure != null) {
				if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					givePlayerTreasure(treasure);
				} else {
					giveEnemyTreasure(message.getCurrentPlayer().getPlayerIndex() , treasure);
				}
				removeTreasureFromBoard(treasure);
			}
			movePawn(message.getCurrentPlayer().getPlayerIndex(), message.getSelectedPawn(), message.getSelectedLand());

		}
		// inform player to play another card
		if (msgIn instanceof PlayAnotherCardMessage) {
			view.playerAnother();

		}

	}

	private boolean scanPawns() {
		boolean pawnSelected = false;
		for (Pawn pawn : currentPlayer.getPawns()) {
			if (pawn.isPawnSelected()) {
				pawnSelected=true;
				break;
			}
		}
		return pawnSelected;
	}

	private void addClickToCard(Card c) {
		c.setOnMouseClicked(e -> view.handleCard(c));
		view.createCardView(c);

	}

	private void addCardToPlayer(Card newCard) {
		currentPlayer.addCard(newCard);
		newCard.setOwner(currentPlayer);
		newCard.setOnMouseClicked(e -> view.handleCard(newCard));
		view.createCardView(newCard);

	}

	private void movePawn(int indexOfPlayer, Pawn selectedPawn, LandTile selectedLand) {
		Pawn viewPawn = null;

		if (currentPlayer.getPawns().contains(selectedPawn)) {
			viewPawn = selectedPawn;
		} else {
			for (int i = 0; i < currentPlayer.getOpponents().size(); i++) {
				if (currentPlayer.getOpponents().get(i).getPawns().contains(selectedPawn)) {
					viewPawn = selectedPawn;
				}
			}
		}
		for (int g = 0; g < view.getBase().size(); g++) {
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(selectedLand)) {
				((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).setPawnOnTile(viewPawn);
			}

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
			System.out.println("looping through the base " + g);
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(treasure)) {
				System.out.println("Foudn the treasure ");
				tempWater.getChildren().remove(treasure);
			}

		}

	}

	public void startTurn(String curPlayer) {

		view.gameStarted();
		if (currentPlayer.getPlayerName().equalsIgnoreCase(curPlayer)) {
			view.yourTurn();

		} else
			view.notYourTurn(curPlayer);

	}

	public void tryPlayCard() {
		if(scanPawns()){
		
			
		
		msgOut.sendMessage(new GameStatusMessage(gameName, currentPlayer.getPlayerIndex()));
		System.out.println("tryPlayCard Method, message sent");
		}else 	view.selectPawnPlease();
	}
}
