package gameObjects;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/*
 * @author Ali Habbabeh
 */

public class Card extends StackPane implements Serializable {

	private final int cardValue = 1;
	public ColorChoice colorChoice;
	private boolean isCardSelected = false;
	private Player owner;
	private int cardId;
	private transient Rectangle rec;

	public Card(int cardId, ColorChoice colorChoice) {

		this.colorChoice = colorChoice;
		this.cardId = cardId;
	}

	public ColorChoice getColor() {
		return colorChoice;
	}

	public int getCardValue() { 
								
		return 1;
	}

	public boolean isCardSelected() {
		return isCardSelected;
	}

	public void setCardSelected(boolean isCardSelected) {
		this.isCardSelected = isCardSelected;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}

}
