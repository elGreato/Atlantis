package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class PawnCardSelectedMessage extends InGameMessage implements Serializable{

	private int playerIndex;
	private Pawn pawn;
	private Card card;
	
	public PawnCardSelectedMessage(String gameName, int playerIndex, Pawn selectedPawn, Card selectedCard) {
		super(gameName);
		this.playerIndex=playerIndex;
		this.pawn=selectedPawn;
		this.card=selectedCard;
		
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


	public Card getCard() {
		return card;
	}


}
