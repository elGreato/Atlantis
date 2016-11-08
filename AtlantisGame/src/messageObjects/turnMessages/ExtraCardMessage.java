package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Card;
import messageObjects.InGameMessage;

public class ExtraCardMessage extends InGameMessage implements Serializable{
	
	Card selectedCard;
	public ExtraCardMessage(String gameName, Card selectedCard) {
		super(gameName);
		this.selectedCard=selectedCard;
		
	}
	public Card getSelectedCard() {
		return selectedCard;
	}
	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

}
