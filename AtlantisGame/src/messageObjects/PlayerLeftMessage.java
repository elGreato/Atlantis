package messageObjects;

import java.io.Serializable;

import gameObjects.Player;

public class PlayerLeftMessage extends InGameMessage implements Serializable {
	
	int playerIndex;
	String newPlayerName;
	public PlayerLeftMessage(String gameName, int playerIndex) {
		super(gameName);
		this.playerIndex=playerIndex;
	}
	public PlayerLeftMessage(String gameName, int playerIndex, String newPlayerName) {
		super(gameName);
		this.playerIndex=playerIndex;
		this.newPlayerName = newPlayerName;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public String getNewPlayerName() {
		return newPlayerName;
	}

}
