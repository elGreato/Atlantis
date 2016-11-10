package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.DeckOfCards;
import gameObjects.Player;

public class PlayerMessage extends InGameMessage implements Serializable {

	private Player player;



	public PlayerMessage(String gameName, Player player) {
		super(gameName);
		this.player = player;
		

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}




}
