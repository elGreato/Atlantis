package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
import gameObjects.Pawn;
import gameObjects.Player;
import messageObjects.InGameMessage;

public class RefreshPlayerMessage extends InGameMessage implements Serializable {

	Player currentPlayer;
	Pawn selectedPawn;
	Card selectedCard;
	LandTile treasure;
	LandTile selectedLand;
	ArrayList<Card> newCards;
	private int waterBill;
	private int waterPassedCount;
	private boolean nextPlayer;

	public RefreshPlayerMessage(String gameName, Player currentPlayer, LandTile selectedLand, Pawn selectedPawn, Card selectedCard,
			LandTile treasure, ArrayList<Card> newCards, int waterBill, int waterPassedCount, boolean nextPlayer) {
		super(gameName);
		this.selectedCard = selectedCard;
		this.selectedPawn = selectedPawn;
		this.treasure = treasure;
		this.selectedLand=selectedLand;
		this.newCards=newCards;
		this.currentPlayer=currentPlayer;
		this.waterBill =waterBill;
		this.waterPassedCount=waterPassedCount;
		this.nextPlayer=nextPlayer;
	}

	

	public Player getCurrentPlayer() {
		return currentPlayer;
	}



	public int getWaterPassedCount() {
		return waterPassedCount;
	}


	public int getWaterBill() {
		return waterBill;
	}



	public Pawn getSelectedPawn() {
		return selectedPawn;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public LandTile getTreasure() {
		return treasure;
	}


	public LandTile getSelectedLand() {
		return selectedLand;
	}

	

	public boolean isNextPlayer() {
		return nextPlayer;
	}



	public ArrayList<Card> getNewCards() {
		return newCards;
	}

}
