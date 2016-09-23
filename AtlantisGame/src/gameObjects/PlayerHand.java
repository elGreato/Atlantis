package gameObjects;

import java.util.ArrayList;

//Ali
public class PlayerHand {
	
	private int handSize;
	private String playerName; 
	private ArrayList<Card> cards = new ArrayList<>(); 
	
	
	public PlayerHand(String name){
	 playerName=name;
	}
	
	public String getPlayerName() {
     return playerName;
	}

	public ArrayList<Card> getCards() {
     return cards;
	}
	
	public int getHandSize(){
		return handSize;
	}
	public void setHandSize(int handsize){
		this.handSize = handsize;
	}
	public void addCard(Card card) {
     if (cards.size() < handSize) cards.add(card);
	}
 
	public void clear() {
     cards.clear();
   
	}
 
	public int getNumCards() {
     return cards.size();
	}
	 
	 
}

