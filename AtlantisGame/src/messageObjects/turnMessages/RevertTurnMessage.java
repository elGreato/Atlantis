package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
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
public class RevertTurnMessage extends InGameMessage implements Serializable {

	int playerIndex;
	private LandTile removedTreasure;
	private ArrayList<Card> removedCards;
	private int removedIndex;
	private Pawn selectedPawn;
	private LandTile selectedLand;
	
	//Added by Kevin
	private int startingLocation;
	
	
	// from Client to server
	public RevertTurnMessage(String gameName, int playerIndex) {
		super(gameName);
		this.playerIndex=playerIndex;
	}

	//from Server to Client
	public RevertTurnMessage(String gameName, int playerIndex, ArrayList<Card> removedCards, LandTile removedTreasure, int removedIndex, Pawn selectedPawn, int startingLocation, LandTile selectedLand){
		super(gameName);
		this.playerIndex=playerIndex;
		this.removedCards= removedCards;
		this.removedTreasure= removedTreasure;
		this.removedIndex = removedIndex;
		this.selectedPawn = selectedPawn;
		this.selectedLand = selectedLand;
		this.startingLocation = startingLocation;
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

	public int getStartingLocation() {
		return startingLocation;
	}

	public void setStartingLocation(int startingLocation) {
		this.startingLocation = startingLocation;
	}
	
	
	

}
