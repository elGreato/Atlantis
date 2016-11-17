package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;

import messageObjects.InGameMessage;

public class EndMYTurnMessage extends InGameMessage  implements Serializable {
	
	int playerIndex;
	private ArrayList<Card> newCards;
	
	// from client to Server
	public EndMYTurnMessage(String gameName, int playerIndex) {
		super(gameName);
		this.playerIndex=playerIndex;
	}
	
	// from Server to Client
		public EndMYTurnMessage(String gameName,int playerIndex,ArrayList<Card>newCards){
		super(gameName);
		this.playerIndex=playerIndex;
		this.newCards=newCards;
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


}
