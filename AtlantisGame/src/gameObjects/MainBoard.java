package gameObjects;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.GridPane;

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
		this.add(new MainLand(999), 8, 8,2,2); 
		
		
		
		
	}
}