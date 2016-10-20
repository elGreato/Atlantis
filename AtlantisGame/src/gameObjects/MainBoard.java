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
		
		
		// Distribute  the landTiles
		DeckOfLandTiles deckA = new DeckOfLandTiles();
		DeckOfLandTiles deckB = new DeckOfLandTiles();
		Iterator<LandTile> it = deckA.getDeckOfTiles().iterator();
		
		// we start with index 2 since the Atlantis booked the index 0 and span to four cells
		int co=2; int ro=1;
		//these two booleans are to add the extra tile between every other row 
		   boolean reachedMaxIndex=false;
		   boolean reachedFirstCol=false;
		while (it.hasNext()){
			LandTile tile = it.next();
			this.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it.remove();
			if (co==maxColIndex+1) {
				if (reachedMaxIndex==false){
				this.add(it.next(), maxColIndex, ro+1);System.out.println("how many times");reachedMaxIndex=true;}
				co=0;
				ro+=2;
				}
			if(co==0){
				if (reachedFirstCol==false){
					this.add(it.next(), 0, ro+1);System.out.println("yo yo"); reachedFirstCol=true;}
			}
			
		}
		it=deckB.getDeckOfTiles().iterator();
		while(it.hasNext()){
			LandTile tile = it.next();
			this.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it.remove();
			if (co==maxColIndex+1) {
				while (reachedMaxIndex==true){
					this.add(it.next(), maxColIndex, ro+1);System.out.println("how many "); reachedMaxIndex=false;}
				co=0;
				ro+=2;
				}
			if(co==0){
				if (reachedFirstCol==true){
					this.add(it.next(), 0, ro+1);System.out.println("yo yo"); reachedFirstCol=false;}
			}
		
		}
		
		
	}
}