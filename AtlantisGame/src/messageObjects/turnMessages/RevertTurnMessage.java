package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class RevertTurnMessage extends InGameMessage implements Serializable {

	int playerIndex;
	private LandTile removedTreasure;
	private ArrayList<Card> removedCards;
	private int removedIndex;
	private Pawn selectedPawn;
	private LandTile selectedLand;
	
	
	// from Client to server
	public RevertTurnMessage(String gameName, int playerIndex) {
		super(gameName);
		this.playerIndex=playerIndex;
	}

	//from Server to Client
	public RevertTurnMessage(String gameName, int playerIndex, ArrayList<Card> removedCards, LandTile removedTreasure, int removedIndex, Pawn selectedPawn, LandTile selectedLand){
		super(gameName);
		this.playerIndex=playerIndex;
		this.removedCards= removedCards;
		this.removedTreasure= removedTreasure;
		this.removedIndex = removedIndex;
		this.selectedPawn = selectedPawn;
		this.selectedLand = selectedLand;
	}
	
	
	
	
	public int getPlayerIndex() {
		return playerIndex;
	}

	public LandTile getRemovedTreasure() {
		return removedTreasure;
	}

	public ArrayList<Card> getRemovedCards() {
		return removedCards;
	}

	public int getRemovedIndex() {
		return removedIndex;
	}

	public Pawn getSelectedPawn() {
		return selectedPawn;
	}

	public LandTile getSelectedLand() {
		return selectedLand;
	}

	public void setSelectedLand(LandTile selectedLand) {
		this.selectedLand = selectedLand;
	}
	
	
	

}
