package gameObjects;

import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class MainLand extends Tile{
	
	private int mainLandid;
	private Map<Integer, Pawn> finalPawns ;
	
	public MainLand(int id){
		super(id);
		
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/mainLand.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		
	}
	
	

}
