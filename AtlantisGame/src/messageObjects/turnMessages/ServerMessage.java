package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;
/**
 * <h1>message</h1> 
 * this is a class that represent an object sent from server to client or vice versa
 * they are all self explanatory 
 * 
 * 
 * @author Ali Habbabeh
 * @version 1.2
 * @since 2016-12-22
 */
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
