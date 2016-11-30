package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Player;
import messageObjects.InGameMessage;

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
