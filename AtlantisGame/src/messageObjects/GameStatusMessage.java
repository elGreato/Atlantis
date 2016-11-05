package messageObjects;

import java.io.Serializable;

import gameObjects.Player;

public class GameStatusMessage extends InGameMessage implements Serializable {

	private boolean startGame;
	private Player currentPlayer;
	
	
	
	public GameStatusMessage(String gameName) {
		super(gameName);
		
	}
	
	// start game
	public GameStatusMessage(String gameName,  boolean startGame, Player currentPlayer){
		super(gameName);
		this.startGame=startGame;
		this.currentPlayer=currentPlayer;
	}
	
	// who's turn
	public GameStatusMessage(String gameName, Player currentPlayer){
		super(gameName);
	}
	
	
	
	
	
	
	
	public boolean isStarted() {
		return startGame;
	}
	public void setStartGame(boolean startGame) {
		this.startGame = startGame;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	
	
	

}
