package client.game;

import java.util.ArrayList;

import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.DeckOfLandTiles;
import gameObjects.Player;
import javafx.scene.control.Label;

import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterTileMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private LobbyModel msgOut;
	private ArrayList<Player> players = new ArrayList<>();

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

		if (msgIn instanceof DeckLandTileMessage) {
			System.out.println("DeckLandTileMessage RECEIVED!!!");

			view.distributeLandTiles(((DeckLandTileMessage) msgIn).getArrayA(),
					((DeckLandTileMessage) msgIn).getArrayB());

		}
		if (msgIn instanceof PlayerMessage) {
			System.out.println("A player Recieved");

			Player player = new Player(((PlayerMessage) msgIn).getPlayer().getPlayerName());
			player.setPlayerName(((PlayerMessage) msgIn).getPlayer().getPlayerName());

			player.setVictoryPoints(((PlayerMessage) msgIn).getInitialVictoryPoints());

			for (int i = 0; i < (((PlayerMessage) msgIn).getCards().size()); i++) {
				((Label) (player.getHboxCards().getChildren().get(i)))
						.setGraphic((((PlayerMessage) msgIn).getCards().get(i)).colorChoice.addImage());

			}
			players.add(player);
			view.showPlayer(player);

		}

		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");

			for (Player p : players) {
				for (int i = 0; i < ((OpponentMessage) msgIn).getOpponents().size(); i++) {
					if (!p.getPlayerName().equalsIgnoreCase((((OpponentMessage) msgIn).getOpponents().get(i).getPlayerName())))
					view.setOpponent(p, ((OpponentMessage) msgIn).getOpponents().get(i));
				}

				System.out.println("now we finished givving opponents");

			}

		}

	}
}
