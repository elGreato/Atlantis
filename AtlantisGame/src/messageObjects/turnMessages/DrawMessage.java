package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import messageObjects.InGameMessage;

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
