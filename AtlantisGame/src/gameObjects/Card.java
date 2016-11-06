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

	private final int cardValue=1;
	public ColorChoice colorChoice;
	private boolean isCardSelected = false;
	private Player owner;
	private int cardId;
	private Rectangle rec;
	
	public Card(int cardId,ColorChoice colorChoice){
		
		this.colorChoice=colorChoice;
		this.cardId=cardId;
	}
	
	public static Paint FillColor(Card c ) {
		if (c.getColor().toString().equalsIgnoreCase("blue")) 
			return (Color.BLUE);
		else if (c.getColor().toString().equalsIgnoreCase("red")) 
			return (Color.RED);
		else if (c.getColor().toString().equalsIgnoreCase("gray")) 
			return(Color.GRAY);
		else if (c.getColor().toString().equalsIgnoreCase("yello")) 
			return(Color.YELLOW);
		else if (c.getColor().toString().equalsIgnoreCase("green")) 
			return(Color.GREEN);
		else if (c.getColor().toString().equalsIgnoreCase("purple")) 
			return(Color.PURPLE);
		else if (c.getColor().toString().equalsIgnoreCase("brown")) 
			return(Color.BROWN);
		return Color.ORANGE;
	
	}

	
	public ColorChoice getColor(){
		return colorChoice;
	}
	
	public int getCardValue(){  // in case we will introduce something like super card this can be changed
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
