package messageObjects;

import java.io.Serializable;

import gameObjects.DeckOfCards;
import gameObjects.Player;

public class PlayerMessage extends InGameMessage implements Serializable {

	private Player player;
	private DeckOfCards cards;
	int indexOfPlayer;
	public PlayerMessage(String gameName, Player player, DeckOfCards cards, int indexOfPlayer) {
		super(gameName);
		this.cards= cards;
		this.player = player;
		this.indexOfPlayer=indexOfPlayer;
	}
	
	public DeckOfCards getCards() {
		return cards;
	}

	public void setCards(DeckOfCards cards) {
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

}
