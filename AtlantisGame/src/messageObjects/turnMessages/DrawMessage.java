package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

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
public class DrawMessage extends InGameMessage implements Serializable {
	
	ArrayList<Integer> winners;
	public DrawMessage(String gameName, ArrayList<Integer> winners) {
		super(gameName);
		this.winners=winners;
	}
	public ArrayList<Integer> getWinners() {
		return winners;
	}

}
