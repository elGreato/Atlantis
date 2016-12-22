package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

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
public class PlayAnotherCardMessage extends InGameMessage implements Serializable {
	Pawn selectedPawn;
	ArrayList<Card>extraCards;
	
	public PlayAnotherCardMessage(String gameName, Pawn selectedPawn, ArrayList<Card> extraCards) {
		super(gameName);
		this.selectedPawn=selectedPawn;
		this.extraCards=extraCards;
	}


	public Pawn getSelectedPawn() {
		return selectedPawn;
	}


	public ArrayList<Card> getExtraCards() {
		return extraCards;
	}
	

}
