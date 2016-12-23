package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;

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
public class EndMYTurnMessage extends InGameMessage  implements Serializable {
	
	int playerIndex;
	private ArrayList<Card> newCards;
	private boolean normalEnd;
	private int vp;
	
	// from client to Server
	public EndMYTurnMessage(String gameName, int playerIndex, boolean normalEnd) {
		super(gameName);
		this.playerIndex=playerIndex;
		this.normalEnd=normalEnd;
	}
	
	// from Server to Client
		public EndMYTurnMessage(String gameName,int playerIndex,ArrayList<Card>newCards, int vp){
		super(gameName);
		this.playerIndex=playerIndex;
		this.newCards=newCards;
		this.vp=vp;
	}
	
	
	
	
	public int getVp() {
			return vp;
		}

	public ArrayList<Card> getNewCards() {
		return newCards;
	}

	public void setNewCards(ArrayList<Card> newCards) {
		this.newCards = newCards;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public boolean isNormalEnd() {
		return normalEnd;
	}


}
