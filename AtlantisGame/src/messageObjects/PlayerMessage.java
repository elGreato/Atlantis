package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.DeckOfCards;
import gameObjects.Player;

public class PlayerMessage extends InGameMessage implements Serializable {

	private Player player;
	private ArrayList<Card> cards;
	private int indexOfPlayer;
	private int initialVictoryPoints;

	public PlayerMessage(String gameName, Player player, ArrayList<Card> cards, int indexOfPlayer) {
		super(gameName);
		this.cards= cards;
		this.player = player;
		this.indexOfPlayer=indexOfPlayer;
		

	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}


	public int getIndexOfPlayer() {
		return indexOfPlayer;
	}

	public void setIndexOfPlayer(int indexOfPlayer) {
		this.indexOfPlayer = indexOfPlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getInitialVictoryPoints() {
		return initialVictoryPoints;
	}

	public void setInitialVictoryPoints(int initialVictoryPoints) {
		this.initialVictoryPoints = initialVictoryPoints;
	}



}
