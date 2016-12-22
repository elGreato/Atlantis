package messageObjects.turnMessages;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.Card;
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
public class CardsBoughtMessage extends InGameMessage implements Serializable{
	
	ArrayList<Card> purchase;
	ArrayList<LandTile> sold;
	Player currentPlayer;
	private int vp;
	
	public CardsBoughtMessage(String gameName, Player currentPlayer, ArrayList<Card> purchase, ArrayList<LandTile> sold, int vp) {
		super(gameName);
		this.purchase=purchase;
		this.currentPlayer=currentPlayer;
		this.sold=sold;
		this.vp=vp;
		
	}

	public ArrayList<Card> getPurchase() {
		return purchase;
	}

	public void setPurchase(ArrayList<Card> purchase) {
		this.purchase = purchase;
	}

	public ArrayList<LandTile> getSold() {
		return sold;
	}

	public void setSold(ArrayList<LandTile> sold) {
		this.sold = sold;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getVp() {
		return vp;
	}

}
