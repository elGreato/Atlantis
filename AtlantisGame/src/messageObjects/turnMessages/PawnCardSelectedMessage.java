package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class PawnCardSelectedMessage extends InGameMessage implements Serializable{

	private int playerIndex;
	private int pawn;
	private int card;
	
	public PawnCardSelectedMessage(String gameName, int playerIndex, int pawn, int card) {
		super(gameName);
		this.playerIndex=playerIndex;
		this.pawn=pawn;
		this.card=card;
		
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public int getPawnId() {
		return pawn;
	}


	public int getCardId() {
		return card;
	}


}
