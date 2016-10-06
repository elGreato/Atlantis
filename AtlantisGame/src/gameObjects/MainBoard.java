package gameObjects;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
// remember to check MVC slides for girdpane resizing
public class MainBoard extends GridPane {
	private final int numberOfTiles;
	
	/*
	 * public Map<Integer, WaterTile> initialBoard = createBaseBoard();
	 * 
	 * public Map<Integer, WaterTile> createBaseBoard(){ for (int i =0;
	 * i<numberOfTiles;i++){ initialBoard.put(i, new WaterTile(i)); } return
	 * initialBoard; }
	 */

	public MainBoard(int numberOfTiles){
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/water.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		this.numberOfTiles=numberOfTiles;
			//this is part of view for later
		this.setHgap(3);
		this.setVgap(3);
		
		// distribute water tiles as a base board
		for (int i = 0; i < Math.sqrt(numberOfTiles); i++) {
			for (int k=0; k<Math.sqrt(numberOfTiles); k++){
			this.add(new WaterTile(i),i,k);

			}
		}
		// put the Atlantis Tile
		this.add(new AtlantisTile(0), 0, 0,2,2);
		
		//put the mainland Tile
		this.add(new MainLand(999), 7, 7,2,2); 
		
		
		
		
	}
}