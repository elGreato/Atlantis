package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Player;

/**
 * <h1>Opponenet Message</h1> 
 * send list of players to all the players
 * 
 * 
 * 
 * @author Ali Habbabeh
 * @version 1.2
 * @since 2016-12-22
 */
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
