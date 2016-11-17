package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

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
