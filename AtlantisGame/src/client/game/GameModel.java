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
import javafx.scene.paint.Color;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.ExtraCardMessage;
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

			for (int i = 0; i < (((PlayerMessage) msgIn).getCards().size()); i++) {
				Card c = (((PlayerMessage) msgIn).getCards().get(i));
				((PlayerMessage) msgIn).getCards().get(i).setOnMouseClicked(e -> view.handleCard(c));
				view.createCardView(c);

			}
			view.player = currentPlayer;
			view.showPlayer();
		}	
		
		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");

			for (int i = 0; i < ((OpponentMessage) msgIn).getOpponents().size(); i++) {
				if (!currentPlayer.getPlayerName()
						.equalsIgnoreCase((((OpponentMessage) msgIn).getOpponents().get(i).getPlayerName()))){
					view.setOpponent(currentPlayer, ((OpponentMessage) msgIn).getOpponents().get(i));
					currentPlayer.getOpponents().add(((OpponentMessage) msgIn).getOpponents().get(i));
				}
			}
		}
		if (msgIn instanceof GameStatusMessage) {

			if (((GameStatusMessage) msgIn).isStarted())
				startTurn(((GameStatusMessage) msgIn).getCurrentPlayer().getPlayerName());

		}
		if (msgIn instanceof TurnMessage) {

			if (((TurnMessage) msgIn).isYourTurn()) {
				Card selectedCard = null;
				Pawn selectedPawn = null;

				for (Card card : currentPlayer.getPlayerHand().getCards()) {
					if (card.isCardSelected()) {
						selectedCard = card;
						currentPlayer.getPlayerHand().removeCardFromHand(card);
						break;
					}
				}
				for (Pawn pawn : currentPlayer.getPawns()) {
					if (pawn.isPawnSelected()) {
						selectedPawn = pawn;
						break;
					}
				}
				System.out.println("card that client selected " + selectedCard.getCardId());
				msgOut.sendMessage(new PawnCardSelectedMessage(gameName, currentPlayer.getPlayerIndex(),
						selectedPawn.getPawnId(), selectedCard.getCardId()));

			} else
				view.showNotYourTurnAlert();
		}
		if (msgIn instanceof RefreshPlayerMessage) {
			RefreshPlayerMessage message = (RefreshPlayerMessage) msgIn;
			givePlayerTreasure(message.getIndexOfPlayer(), message.getTreasure());
			removeTreasureFromBoard(message.getTreasure());
			movePawn(message.getIndexOfPlayer(), message.getSelectedPawn(), message.getSelectedLand());

		}
		if(msgIn instanceof PlayAnotherCardMessage){
			view.playerAnother();
			
		}

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

	private void givePlayerTreasure(int indexOfPlayer, LandTile treasure) {
		if(treasure!=null){
		if (currentPlayer.getPlayerIndex() == indexOfPlayer) {
			currentPlayer.getPlayerHand().addTreasure(treasure);
			view.givePlayerTreasure(treasure);
		}
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
		msgOut.sendMessage(new GameStatusMessage(gameName, currentPlayer.getPlayerIndex()));
		System.out.println("tryPlayCard Method, message sent");
	}

	public void tryPlayExtra() {
		Card selectedCard=null;
		for (Card card : currentPlayer.getPlayerHand().getCards()) {
			if (card.isCardSelected()) {
				selectedCard = card;
				currentPlayer.getPlayerHand().removeCardFromHand(card);
				break;
			}
		}
		msgOut.sendMessage(new ExtraCardMessage(gameName, selectedCard));
		
	}

}
