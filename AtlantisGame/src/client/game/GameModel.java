package client.game;

import client.lobby.LobbyModel;
import javafx.scene.layout.GridPane;
import messageObjects.InGameMessage;
import messageObjects.ServerViewMessage;

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
		if (msgIn instanceof ServerViewMessage){
			Object pg = msgIn;
			GridPane gg = (GridPane) pg;
			view.setRoot(gg);
			System.out.println("process Message method");
		}
	}
}
