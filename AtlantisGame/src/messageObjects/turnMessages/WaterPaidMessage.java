package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
import messageObjects.InGameMessage;

public class WaterPaidMessage extends InGameMessage implements Serializable {
	
	int playerIndex;
	ArrayList<LandTile> treasuresChosen;
	ArrayList<Card> cardsChosen;
	private boolean nextPlayer;
	private boolean gameOver;
	

	public WaterPaidMessage(String gameName, int playerIndex, ArrayList<LandTile> treasuresChosen,
			ArrayList<Card> cardsChosen, boolean nextPlayer, boolean gameOver) {
		super(gameName);
		this.playerIndex=playerIndex;
		this.treasuresChosen=treasuresChosen;
		this.cardsChosen=cardsChosen;
		this.nextPlayer=nextPlayer;
		this.gameOver=gameOver;
	}


	public int getPlayerIndex() {
		return playerIndex;
	}


	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}


	public ArrayList<LandTile> getTreasuresChosen() {
		return treasuresChosen;
	}


	public boolean isGameOver() {
		return gameOver;
	}


	public boolean isNextPlayer() {
		return nextPlayer;
	}


	public void setTreasuresChosen(ArrayList<LandTile> treasuresChosen) {
		this.treasuresChosen = treasuresChosen;
	}


	public ArrayList<Card> getCardsChosen() {
		return cardsChosen;
	}


	public void setCardsChosen(ArrayList<Card> cardsChosen) {
		this.cardsChosen = cardsChosen;
	}

}
