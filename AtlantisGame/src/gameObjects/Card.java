package gameObjects;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/*
 * @author Ali Habbabeh
 */

public class Card implements Serializable {

	private final int cardValue=1;
	public ColorChoice colorChoice;
	
	public Card(ColorChoice colorChoice){
		
		this.colorChoice=colorChoice;
	}
	
	public ColorChoice getColor(){
		return colorChoice;
	}
	
	public int getCardValue(){  // in case we will introduce something like super card this can be changed
		return 1;
	}
	 @Override
	 // to test in console
	    public String toString() {
	        return (this.getColor()).toString();
	    }
}
