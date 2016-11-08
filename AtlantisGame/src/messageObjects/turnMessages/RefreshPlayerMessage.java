package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import gameObjects.LandTile;
import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class RefreshPlayerMessage extends InGameMessage implements Serializable {

	int indexOfPlayer;
	Pawn selectedPawn;
	Card selectedCard;
	LandTile treasure;
	LandTile selectedLand;

	public RefreshPlayerMessage(String gameName, int indexOfPlayer, LandTile selectedLand, Pawn selectedPawn, Card selectedCard,
			LandTile treasure) {
		super(gameName);
		this.selectedCard = selectedCard;
		this.selectedPawn = selectedPawn;
		this.treasure = treasure;
		this.selectedLand=selectedLand;
	}

	public int getIndexOfPlayer() {
		return indexOfPlayer;
	}

	public void setIndexOfPlayer(int indexOfPlayer) {
		this.indexOfPlayer = indexOfPlayer;
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

}
