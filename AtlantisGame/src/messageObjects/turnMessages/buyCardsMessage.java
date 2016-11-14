package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.LandTile;
import gameObjects.Player;
import messageObjects.InGameMessage;

public class buyCardsMessage extends InGameMessage implements Serializable{
	Player currentPlayer;
	ArrayList<LandTile> treasuresChosen;
	
	
	public buyCardsMessage(String gameName, Player currentPlayer, ArrayList<LandTile> treasuresChosen) {
		super(gameName);
		
	}


	public Player getCurrentPlayer() {
		return currentPlayer;
	}


	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}


	public ArrayList<LandTile> getTreasuresChosen() {
		return treasuresChosen;
	}


	public void setTreasuresChosen(ArrayList<LandTile> treasuresChosen) {
		this.treasuresChosen = treasuresChosen;
	}

}
