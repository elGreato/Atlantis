package client.game;

import messageObjects.InGameMessage;

public class GameModel {
	private String gameName;
	
	private GameView view;
	
	public String getGameName() {
		return gameName;
	}

	public GameModel(String gameName, GameView gameView)
	{
		this.gameName = gameName;
		this.view = gameView;
	}
	
	public void processMessage(InGameMessage msg)
	{
		
	}
}
