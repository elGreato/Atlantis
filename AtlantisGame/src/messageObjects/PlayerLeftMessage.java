package messageObjects;

import java.io.Serializable;

public class PlayerLeftMessage extends InGameMessage implements Serializable {
	
	int playerIndex;
	public PlayerLeftMessage(String gameName, int playerIndex) {
		super(gameName);
		
	}
	public int getPlayerIndex() {
		return playerIndex;
	}

}
