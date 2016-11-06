package client.game;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.ColorChoice;
import gameObjects.Pawn;
import gameObjects.LandTile;
import gameObjects.Player;
import javafx.event.Event;
import javafx.scene.paint.Color;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.turnMessages.GameStatusMessage;
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
		if (msgIn instanceof DeckLandTileMessage) {
			view.distributeLandTiles(((DeckLandTileMessage) msgIn).getArrayA(),
					((DeckLandTileMessage) msgIn).getArrayB());

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

			view.showPlayer(currentPlayer);

		}
		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");

			for (int i = 0; i < ((OpponentMessage) msgIn).getOpponents().size(); i++) {
				if (!currentPlayer.getPlayerName()
						.equalsIgnoreCase((((OpponentMessage) msgIn).getOpponents().get(i).getPlayerName())))
					view.setOpponent(currentPlayer, ((OpponentMessage) msgIn).getOpponents().get(i));
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
						break;
					}
				}
				for (Pawn pawn : currentPlayer.getPawns()) {
					if (pawn.isPawnSelected()) {
						selectedPawn = pawn;
						break;
					}
				}

				// move the pawn to the landTile
				for (int i = 0; i < view.base.size(); i++) {
					int topNode = (view.getBase().get(i).getChildren().size() - 1);
					ColorChoice currentColor = selectedCard.getColor();
					// if the water has a landtile
					if (view.getBase().get(i).getChildren() != null) {
						// doubld check 
						if (((LandTile) view.getBase().get(i).getChildren().get(topNode)) != null) {
							LandTile target = ((LandTile) view.getBase().get(i).getChildren().get(topNode));
							// if the land same color as card
							if (target.getColor().equals(currentColor)) {

								target.setPawnOnTile(selectedPawn);

								view.movePawn(selectedPawn, target);

								if (view.getBase().get(i - 1) != null) {
									int indexOfTarget = view.getBase().get(i).getChildren().indexOf(target);
									view.givePlayerTreasure(
											((LandTile) view.getBase().get(i - 1).getChildren().remove(indexOfTarget)));
								}
								break;
							}
						}
					}
				}

			} else
				view.showNotYourTurnAlert();
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

}
