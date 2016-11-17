package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
import messageObjects.InGameMessage;

public class PaymentDoneMessage extends InGameMessage implements Serializable {

	int playerIndex;
	ArrayList<Card> cardsChosen;
	ArrayList<LandTile> treasuresChosen;
	
	
	public PaymentDoneMessage(String name, int playerIndex, ArrayList<Card> cardsChosen,
			ArrayList<LandTile> treasuresChosen) {
		super(name);
		this.playerIndex=playerIndex;
		this.cardsChosen=cardsChosen;
		this.treasuresChosen=treasuresChosen;
	}


	public int getPlayerIndex() {
		return playerIndex;
	}


	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}


	public ArrayList<Card> getCardsChosen() {
		return cardsChosen;
	}


	public void setCardsChosen(ArrayList<Card> cardsChosen) {
		this.cardsChosen = cardsChosen;
	}


	public ArrayList<LandTile> getTreasuresChosen() {
		return treasuresChosen;
	}


	public void setTreasuresChosen(ArrayList<LandTile> treasuresChosen) {
		this.treasuresChosen = treasuresChosen;
	}

}
