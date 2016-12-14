package messageObjects;

import java.io.Serializable;

import gameObjects.Player;

public class PlayerLeftMessage extends InGameMessage implements Serializable {
	
	int playerIndex;
	public PlayerLeftMessage(String gameName, int playerIndex) {
		super(gameName);
		this.playerIndex=playerIndex;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}

}
