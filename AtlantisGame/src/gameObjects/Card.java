package gameObjects;

import javafx.scene.paint.Color;

/*
 * @author Ali Habbabeh
 */

public class Card {
	Color blue = Color.BLUE;
	Color red = Color.RED;
	Color gray = Color.GRAY;
	Color yellow = Color.YELLOW;
	Color green = Color.LIGHTSEAGREEN;
	Color purple = Color.PURPLE;
	Color brown = Color.BROWN;
	
	public enum ColorChoice { blue,red,gray,yellow,green,purple,brown};
	
	private final int cardValue=1;
	private ColorChoice colorChoice;
	
	public Card(ColorChoice colorChoice){
		
		this.colorChoice=colorChoice;
	}
	
	public ColorChoice getColor(){
		return colorChoice;
	}
	
	public int getCardValue(){  // in case we will introduce something like super card this can be changed
		return 1;
	}

}
