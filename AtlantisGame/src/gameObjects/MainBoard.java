package gameObjects;


import java.util.Iterator;

import javafx.scene.layout.GridPane;
// remember to check MVC slides for girdpane resizing
public class MainBoard extends GridPane {
	private final int numberOfTiles;
	int maxColIndex ;
	int maxRowIndex;
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
		for (int i = 0; i < (Math.sqrt(numberOfTiles)*2); i++) {
			for (int k=0; k<Math.sqrt(numberOfTiles); k++){
			this.add(new WaterTile(i,i,k),i,k);
			maxColIndex =(int) (Math.sqrt(numberOfTiles)*2);
			maxRowIndex =(int) (Math.sqrt(numberOfTiles));
			}
		}
		
		// put the Pawns

		
		
		// put the Atlantis Tile
		this.add(new AtlantisTile(0,0,0), 0, 0,2,2);
		
		//put the mainland Tile, -1 is cuz we span on two 
		this.add(new MainLand(999,7,7), maxColIndex-1, maxRowIndex-1,2,2); 
		
		
		// put the landTiles
		DeckOfLandTiles deckA = new DeckOfLandTiles();
		DeckOfLandTiles deckB = new DeckOfLandTiles();
		Iterator<LandTile> it = deckA.getDeckOfTiles().iterator();
		Iterator<LandTile> it2 = deckB.getDeckOfTiles().iterator();
		int co=2; int ro=1;
		 
		   
		while (it.hasNext()){
			LandTile tile = it.next();
			this.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it.remove();
			if (co==maxColIndex+1) {
				this.add(it.next(), maxColIndex, ro+1);
				co=0;
				ro+=2;
				}
			
			
		}

		while(it2.hasNext()){
			LandTile tile = it2.next();
			this.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it2.remove();
			if (co==maxColIndex+1) {
				this.add(it2.next(), maxColIndex, ro+1);
				co=0;
				ro+=2;
				}
		}
		
		
	}
}