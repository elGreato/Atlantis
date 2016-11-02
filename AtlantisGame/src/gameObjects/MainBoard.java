package gameObjects;


import java.awt.Stroke;
import java.io.Serializable;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.StrokeLineCap;
// remember to check MVC slides for girdpane resizing
public class MainBoard extends GridPane implements Serializable, EventHandler<MouseEvent>{
	private final int numberOfTiles=120;
	int maxColIndex ;
	int maxRowIndex;
	DeckOfLandTiles deckA;
	DeckOfLandTiles deckB; 
	
	
	private static final double WINDOW_WIDTH = 600 ;
    private static final double WINDOW_HEIGHT = 400 ;

	public MainBoard(){
		
			//this is part of view for later
		this.setHgap(3);
		this.setVgap(3);
		
		// distribute water tiles as a base board
		for (int i = 0; i < (Math.sqrt(numberOfTiles)*1.5); i++) {
			for (int k=0; k<Math.sqrt(numberOfTiles); k++){
			this.add(new WaterTile(10*i+k,i,k),i,k);
			
			}
			
		}
		maxColIndex =(int) (Math.sqrt(numberOfTiles)*1.5);
		maxRowIndex =(int) (Math.sqrt(numberOfTiles));
		// put the Pawns

		
		
		// put the Atlantis Tile
		this.add(new AtlantisTile(0,0,0), 0, 0,2,2);
		
		//put the mainland Tile, -1 is cuz we span on two 
		this.add(new MainLand(999,7,7), maxColIndex-1, maxRowIndex-1,2,2); 
		
		
		// Distribute  the landTiles
		 deckA = new DeckOfLandTiles();
		 deckB = new DeckOfLandTiles();
		Iterator<LandTile> it = deckA.getDeckOfTiles().iterator();
		
		// we start with index 2 since the Atlantis booked the index 0 and span to four cells
		int co=2; int ro=1;
		//these two booleans are to add the extra tile between every other row 
			
		boolean reachedMaxIndex=false;
	    boolean reachedFirstCol=false;
		while (it.hasNext()){
			LandTile tile = it.next();
			tile.setOnMouseClicked(e-> handle(e));
		((WaterTile) getNodebyIndex(co, ro, this)).getChildren().add(tile);
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
					((WaterTile) getNodebyIndex(0, ro+1, this)).getChildren().add(it.next());
					
					reachedFirstCol=true;}
			}
			
		}
		co++;
		it=deckB.getDeckOfTiles().iterator();
		while(it.hasNext()){
			LandTile tile = it.next();
			tile.setOnMouseClicked(e-> handle(e));
			((WaterTile) getNodebyIndex(co, ro, this)).getChildren().add(tile);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it.remove();
			if (co==maxColIndex+1) {
				while (reachedMaxIndex==true){
					((WaterTile) getNodebyIndex(maxColIndex, ro+1, this)).getChildren().add(it.next());
					
					reachedMaxIndex=false;}
				co=0;
				ro+=2;
				}
			if(co==0){
				if (reachedFirstCol==true){
					this.add(it.next(), 0, ro+1);
					reachedFirstCol=false;}
			}
		
		}
		
		
	}

	@Override
	public void handle(MouseEvent e) {
		for (Node node: this.getChildren()){
			if (node instanceof LandTile){
				if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                    System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));

			int i=(int)  GridPane.getColumnIndex( node);
			int k= (int) GridPane.getRowIndex( node);
			((WaterTile) getNodebyIndex(i, k, this)).getChildren().removeAll();
			/*for (LandTile t: deckB.getDeckOfTiles()){
				if (t.getCol()==i&&t.getRow()==k)
				this.getChildren().remove(0)	;*/
			//deckB.getDeckOfTiles().remove(t.getTileId());
				}
			}}
		
	}
	public Node getNodebyIndex(int col, int row, GridPane grid){
		Node result = null;
		ObservableList<Node> childrens= grid.getChildren();
		for (Node node: childrens){
			if(grid.getColumnIndex(node)==col && grid.getRowIndex(node)==row){
				result = node;
				break;
			}
		}
		
		
		return result;
		
	}
}