package gameObjects;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Rectangle;

public class AtlantisTile extends Tile {

	private  int atlantisId=0;
	private  int  col=0;
	private  int row = 0;
	
	//Rectangle recAtlantis = new Rectangle(50, 50);

	public AtlantisTile( int atlantisId, int col, int row) {
		super(atlantisId,col, row);
		this.atlantisId=atlantisId;
		this.col=col;
		this.row=row;
		
		// this should be in view
	
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/atlantisTileImage.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
	}
	


}
