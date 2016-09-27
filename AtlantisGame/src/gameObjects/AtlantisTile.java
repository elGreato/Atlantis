package gameObjects;

import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Rectangle;

public class AtlantisTile extends Tile {

	// it will hold from 6 to 12 Pawns
	private Map<Integer, Pawn> initialPawns ;
	
	private  int atlantisId;
	
	//Rectangle recAtlantis = new Rectangle(50, 50);

	public AtlantisTile(int tileId) {
		super(tileId);
		// this should be in view
	
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/atlantisTileImage.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
	}

}
