package gameObjects;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collections;


/*
 * @author Ali Habbabeh
 */
public class DeckOfCards implements Serializable{
	
	private ArrayList<Card> cards= new ArrayList<>();
	
	public DeckOfCards(){
		
		shuffle();
	}
	
	public int getNumberOfCardsInDeck(){
		return cards.size();
	}

	public void shuffle(){
		// first we remove any cards in the deck 
		cards.clear();
		
		// Add all 105 cards
		for (int i=0; i<15; i++) {
			
			for (ColorChoice c: ColorChoice.values()) //used nested loop to multiply # of colors with #of cards	
                cards.add(new Card(c));
        }
		
		// now since we added them on a row we shuffle them
        
        Collections.shuffle(cards);  
	}
	
	public Card deal(){
		  return (cards.size() > 0) ? cards.remove(cards.size()-1) : null;  // Prof Bradly Richard from the first semester
	}
}
