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

	public RefreshPlayerMessage(String gameName, Player currentPlayer, LandTile selectedLand, Pawn selectedPawn, Card selectedCard,
			LandTile treasure, ArrayList<Card> newCards, int waterBill, int waterPassedCount) {
		super(gameName);
		this.selectedCard = selectedCard;
		this.selectedPawn = selectedPawn;
		this.treasure = treasure;
		this.selectedLand=selectedLand;
		this.newCards=newCards;
		this.currentPlayer=currentPlayer;
		this.waterBill =waterBill;
		this.waterPassedCount=waterPassedCount;
	}

	

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}



	public int getWaterPassedCount() {
		return waterPassedCount;
	}



	public void setWaterPassedCount(int waterPassedCount) {
		this.waterPassedCount = waterPassedCount;
	}



	public int getWaterBill() {
		return waterBill;
	}



	public void setWaterBill(int waterBill) {
		this.waterBill = waterBill;
	}



	public Pawn getSelectedPawn() {
		return selectedPawn;
	}

	public void setSelectedPawn(Pawn selectedPawn) {
		this.selectedPawn = selectedPawn;
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

	public void setTreasure(LandTile treasure) {
		this.treasure = treasure;
	}

	public LandTile getSelectedLand() {
		return selectedLand;
	}

	public void setSelectedLand(LandTile selectedLand) {
		this.selectedLand = selectedLand;
	}

	public ArrayList<Card> getNewCards() {
		return newCards;
	}

}
