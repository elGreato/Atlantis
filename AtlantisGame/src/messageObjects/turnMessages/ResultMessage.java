package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;

public class ResultMessage extends InGameMessage implements Serializable{
	
	int winner;
	public ResultMessage(String gameName, int winner) {
		super(gameName);
		this.winner=winner;
	}
	public int getWinner() {
		return winner;
	}

}
