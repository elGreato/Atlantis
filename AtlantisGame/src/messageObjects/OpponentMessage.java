package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Player;

public class OpponentMessage extends InGameMessage implements Serializable{
	
	ArrayList<Player> opponents;
	
	

	public OpponentMessage(String gameName, ArrayList<Player> opponents) {
		super(gameName);
		this.opponents=opponents;
	}


	public ArrayList<Player> getOpponents() {
		return opponents;
	}


	public void setOpponents(ArrayList<Player> opponents) {
		this.opponents = opponents;
	}

}
