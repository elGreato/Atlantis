package gameObjects;

import java.io.Serializable;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;

public class MainLand  extends HBox implements Serializable{
	
	private final int  mainLandid=999;

	
	public MainLand(){
	
	}


	public int getMainLandid() {
		return mainLandid;
	}



}
