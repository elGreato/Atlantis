package client.game;

import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.DeckOfLandTiles;
import gameObjects.Player;
import javafx.scene.control.Label;

import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterTileMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private LobbyModel msgOut;

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
		if (msgIn instanceof PlayerMessage){
			System.out.println("A player Recieved");
			
			Player player = new Player(((PlayerMessage)msgIn).getPlayer().getPlayerName());
			player.setPlayerName(((PlayerMessage) msgIn).getPlayer().getPlayerName());
			
			// distribute cards to each player according to whom entered the game first 
			// first player 4 cards, second 5, ... so on
			int numberOfCards = 4+(((PlayerMessage)msgIn).getIndexOfPlayer());
			for (int i=0; i<numberOfCards;i++){
			Card card= ((PlayerMessage)msgIn).getCards().deal();
			player.addCard(card);
			
			
			 // Determine which label this is (index from 0 to 4)
	        int index = player.getPlayerHand().getNumCards() - 1;
	        
	        // Get the label from the HBox, and update it
	        Label cardLabel = (Label) player.getHboxCards().getChildren().get(index);
	        cardLabel.setGraphic(card.colorChoice.addImage());
			}
			view.showPlayer(player);
			
			
			
			
		}

	}
}
