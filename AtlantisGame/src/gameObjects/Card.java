package gameObjects;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/*
 * @author Ali Habbabeh
 */

public class Card extends Pane implements Serializable {

	private final int cardValue=1;
	public ColorChoice colorChoice;
	private boolean isCardSelected = false;
	
	public Card(ColorChoice colorChoice){
		
		this.colorChoice=colorChoice;
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
	 
}
