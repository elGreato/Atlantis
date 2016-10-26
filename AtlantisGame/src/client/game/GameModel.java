package client.game;

import client.lobby.LobbyModel;
import gameObjects.DeckOfLandTiles;
import javafx.scene.layout.GridPane;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;

import messageObjects.WaterTileMessage;

public class GameModel {
	
	private String gameName;
	private GameView view;
	//send messages to server through method msgOut.sendMessage()
	private LobbyModel msgOut;
	
	public String getGameName() {
		return gameName;
	}

	public GameModel(String gameName, LobbyModel lobbyModel, GameView gameView)
	{
		this.gameName = gameName;
		msgOut = lobbyModel;
		this.view = gameView;
		
		
		
		
		
		
		
		
		
	}
	
	//Here messages from server arrive
	public void processMessage(InGameMessage msgIn)
	{
		/*if (msgIn instanceof WaterTileMessage){
			
			view.setWater((WaterTileMessage)msgIn);
			System.out.println("process Message method");
		}*/
		
		if(msgIn instanceof DeckLandTileMessage){
			System.out.println("DeckLandTileMessage RECEIVED!!!");
			
			view.distributeLandTiles(((DeckLandTileMessage)msgIn).getArrayA(),((DeckLandTileMessage)msgIn).getArrayB() );
			
		}
		
		
		
		
	}
}
