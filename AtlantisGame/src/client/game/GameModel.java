package client.game;

import client.lobby.LobbyModel;
import messageObjects.InGameMessage;

public class GameModel {
	
	private String gameName;
	private GameView view;
	//send messages to server through method msgOut.sendInGameMessage()
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
		
	}
}
