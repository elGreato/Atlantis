package gameObjects;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collections;

/**
* <h1>Deck of Cards</h1>
*  daa 
* 
* @author  Ali Habbabeh
* @version 1.2
* @since   2016-12-22
*/
public class DeckOfCards implements Serializable {

	private ArrayList<Card> cards = new ArrayList<>();
	int k = 0;

	public DeckOfCards() {

		shuffle();
	}

	public int getNumberOfCardsInDeck() {
		return cards.size();
	}

	public void shuffle() {
		// first we remove any cards in the deck
		cards.clear();

		// Add all 105 cards
		for (int i = 0; i < 15; i++) {
			// used nested loop to	 multiply # of colors with #of cards
			for (ColorChoice c : ColorChoice.values()) { 
				cards.add(new Card(k, c));
				k++;
			}
		}

		// now since we added them on a row we shuffle them

		Collections.shuffle(cards);
	}

	public Card deal() {
		return (cards.size() > 0) ? cards.remove(cards.size() - 1) : null; // Prof
																			// Bradly
																			// Richard
																			// from
																			// the
																			// first
																			// semester
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
}
