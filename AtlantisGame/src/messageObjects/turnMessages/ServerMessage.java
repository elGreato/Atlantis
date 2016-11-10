package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;

public class ServerMessage extends InGameMessage implements Serializable {
	String theMessage;
	public ServerMessage(String gameName, String theMessage) {
		super(gameName);
		this.theMessage=theMessage;
	}
	public String getTheMessage() {
		return theMessage;
	}
	public void setTheMessage(String theMessage) {
		this.theMessage = theMessage;
	}
	
}
