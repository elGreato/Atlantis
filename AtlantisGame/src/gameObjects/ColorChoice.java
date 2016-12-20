package gameObjects;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * I know this might look stupid to create a color clas while there exist a color class
 * however, this will help us in creating the dick using limited values, 7 in this case. Moreover, 
 * this allow us to replace normal colors with images of cooler colors
 */
public enum ColorChoice implements Serializable {
	blue, white, grey, yellow, green, pink, brown;

	public ImageView addLandTileImage(int tileValue) {

		Image image = new Image(getClass()
				.getResourceAsStream("images4Tiles/" + this.toString() + "_" + String.valueOf(tileValue) + ".jpg"));
		ImageView iv = new ImageView(image);
		iv.setFitHeight(67);
		iv.setFitWidth(67);
		return iv;
	}


	public ImageView addCardImage() {
		Image image = new Image(getClass().getResourceAsStream("images4Cards/card_" + this.toString() + ".jpg"));
		ImageView iv = new ImageView(image);
		iv.setFitHeight(75);//49
		iv.setFitWidth(38);
		return iv;
	}

}