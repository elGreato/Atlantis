package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import gameObjects.Pawn;
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
