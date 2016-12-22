package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.LandTile;
import gameObjects.Player;
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