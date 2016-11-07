package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class PawnCardSelectedMessage extends InGameMessage implements Serializable{

	private int playerIndex;
	private Pawn pawn;
	private Card card;
	
	public PawnCardSelectedMessage(String gameName, int playerIndex, Pawn pawn, Card card) {
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

	public Pawn getPawn() {
		return pawn;
	}

	public void setPawn(Pawn pawn) {
		this.pawn = pawn;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	
	
}
