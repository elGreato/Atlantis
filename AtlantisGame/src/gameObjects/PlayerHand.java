package gameObjects;

import java.util.ArrayList;

//Ali
public class PlayerHand {
	
	private int handSize=5;
	private String playerName; 
	private ArrayList<Card> cards = new ArrayList<>(); 
	private ArrayList<LandTile> treasures= new ArrayList<>();
	
	
	public PlayerHand(String name){
	 playerName=name;
	}
	
	public ArrayList<LandTile> getTreasures() {
		return treasures;
	}

	public void addTreasure(LandTile treasure) {
		treasures.add(treasure);
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
	cards.add(card);
	}
 
	public void clear() {
     cards.clear();
   
	}
 
	public int getNumCards() {
     return cards.size();
	}
	 
	 
}

