package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.LandTile;
import gameObjects.Player;
import messageObjects.InGameMessage;

public class BuyCardsMessage extends InGameMessage implements Serializable{
	int currentPlayerIndex;
	ArrayList<LandTile> treasuresChosen;
	
	
	public BuyCardsMessage(String gameName, int i, ArrayList<LandTile> treasuresChosen) {
		super(gameName);
		this.currentPlayerIndex=i;
		this.treasuresChosen=treasuresChosen;
		
	}


	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}


	public ArrayList<LandTile> getTreasuresChosen() {
		return treasuresChosen;
	}


	public void setTreasuresChosen(ArrayList<LandTile> treasuresChosen) {
		this.treasuresChosen = treasuresChosen;
	}

}