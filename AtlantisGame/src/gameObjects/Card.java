package gameObjects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/*
 * @author Ali Habbabeh
 */

public class Card {
	/*Color blue = Color.BLUE;
	Color red = Color.RED;
	Color gray = Color.GRAY;
	Color yellow = Color.YELLOW;
	Color green = Color.LIGHTSEAGREEN;
	Color purple = Color.PURPLE;
	Color brown = Color.BROWN;*/
	
	public enum ColorChoice { blue,red,gray,yellow,green,purple,brown
		 
		public ImageView addImage(){
		
		String[] paths= new String[2];
		paths[0]="images4cards/blue.png";
		paths[1]="images4cards/green.png";
		paths[2]="images4cards/yellow.png";
		int index;
		Image image = new Image(getClass().getResourceAsStream(paths[index]));
		switch (this){
		case blue:index=0;break;
		case green: index=1;break;
		default:index=2;
		}
		return ImageView
	}
	
	
	
	};
	
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
