package gameObjects;

import java.util.ArrayList;

import java.util.Collections;


/*
 * @author Ali Habbabeh
 */
public class DeckOfCards {
	
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
        for (Card.ColorChoice color : Card.ColorChoice.values()) {
                Card card = new Card(color);
                cards.add(card);
            
        }
		// now since we added them on a row we shuffle them
        
        Collections.shuffle(cards);  // credits goes to Prof. Bradly Richard from first semester poker example
	}
	
	public Card deal(){
		  return (cards.size() > 0) ? cards.remove(cards.size()-1) : null;  // same source
	}
}
