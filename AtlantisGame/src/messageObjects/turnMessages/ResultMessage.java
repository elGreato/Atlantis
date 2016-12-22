package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Player;
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
public class ResultMessage extends InGameMessage implements Serializable{
	
	int winner;
	private String winnerName;
	public ResultMessage(String gameName, int winner, String winnerName) {
		super(gameName);
		this.winner=winner;
		this.winnerName=winnerName;
	}
	public int getWinner() {
		return winner;
	}
	public String getWinnerName() {
		return winnerName;
	}

}
