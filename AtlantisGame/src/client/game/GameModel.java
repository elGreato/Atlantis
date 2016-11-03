package client.game;

import java.util.ArrayList;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import gameObjects.Player;
import javafx.scene.control.Label;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private ClientLobbyInterface msgOut;
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
		// first we receive the main board stuff
		if (msgIn instanceof DeckLandTileMessage) {
			view.distributeLandTiles(((DeckLandTileMessage) msgIn).getArrayA(),
					((DeckLandTileMessage) msgIn).getArrayB());

		}
		// now the players
		if (msgIn instanceof PlayerMessage) {
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
		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			System.out.println("Opponent message received");

			for (Player p : players) {
				for (int i = 0; i < ((OpponentMessage) msgIn).getOpponents().size(); i++) {
					if (!p.getPlayerName()
							.equalsIgnoreCase((((OpponentMessage) msgIn).getOpponents().get(i).getPlayerName())))
						view.setOpponent(p, ((OpponentMessage) msgIn).getOpponents().get(i));
				}

			}

		}
		

	}
}
