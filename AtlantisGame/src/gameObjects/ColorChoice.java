package gameObjects;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/*
 * I know this might look stupid to create a color clas while there exist a color class
 * however, this will help us in creating the dick using limited values, 7 in this case. Moreover, 
 * this allow us to replace normal colors with images of cooler colors
 */
public enum ColorChoice implements Serializable{ blue,red,gray,yellow,green,purple,brown;
/*	 
	public ImageView addImage(){
	
	String[] paths= new String[7];
	paths[0]="images4cards/blue.png";
	paths[1]="images4cards/red.jpg";
	paths[2]="images4cards/grey.jpg";
	paths[3]="images4cards/yellow.png";
	paths[4]="images4cards/green.png";
	paths[5]="images4cards/purple.jpg";
	paths[6]="images4cards/brown.jpg";
	
	int index=0;
	
	switch (this){
	case blue:index=0;break;
	case red: index=1;break;
	case gray: index=2;break;
	case yellow: index=3;break;
	case green: index=4;break;
	case purple: index=5;break;
	case brown: index=6; break;
	
	
	}
	Image image = new Image(getClass().getResourceAsStream(paths[index]));
	ImageView iv = new ImageView(image);
	iv.setFitHeight(50);
	iv.setFitWidth(30);
	return iv ;
}*/

}