package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;

public class CloseGameMessage extends InGameMessage implements Serializable {

	public CloseGameMessage(String gameName) {
		super(gameName);
		
	}

}
