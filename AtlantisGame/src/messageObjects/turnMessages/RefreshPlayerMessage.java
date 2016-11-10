package messageObjects.turnMessages;

import java.io.Serializable;

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
	Card newCard;

	public RefreshPlayerMessage(String gameName, Player currentPlayer, LandTile selectedLand, Pawn selectedPawn, Card selectedCard,
			LandTile treasure, Card newCard) {
		super(gameName);
		this.selectedCard = selectedCard;
		this.selectedPawn = selectedPawn;
		this.treasure = treasure;
		this.selectedLand=selectedLand;
		this.newCard=newCard;
		this.currentPlayer=currentPlayer;
	}

	

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
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

	public Card getNewCard() {
		return newCard;
	}

	public void setNewCard(Card newCard) {
		this.newCard = newCard;
	}

}
