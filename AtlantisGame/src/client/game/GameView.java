package client.game;

import gameObjects.MainLand;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView {
	GridPane root= new GridPane();
	Scene scene;
	Stage  stage ;
	private final int numberOfTiles=120;
	int maxColIndex ;
	int maxRowIndex;
	DeckOfLandTilesClient deckA;
	DeckOfLandTilesClient deckB; 
	
	public GameView()
	{
		
		// distribute water tiles as a base board
		for (int i = 0; i < (Math.sqrt(numberOfTiles)*1.5); i++) {
			for (int k=0; k<Math.sqrt(numberOfTiles); k++){
			root.add(new WaterTileClient(i,i,k),i,k);
			
			}
			
		}
		//set Max indexes
		maxColIndex =(int) (Math.sqrt(numberOfTiles)*1.5);
		maxRowIndex =(int) (Math.sqrt(numberOfTiles));
		// Mainland
		root.add(new MainLand(999,7,7), maxColIndex-2, maxRowIndex-1,3,2); 
		
		//the Atlantis
		root.add(new AtlantisTileClient(0,0,0), 0,0,2,2); 
		
		
		
		
		
		stage = new Stage();
		root.setVgap(3);
		root.setHgap(3);
		 scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
	
	
	
	

	/*public void setWater(WaterTileMessage msgIn) {

		
		scene = new Scene(root);
		System.out.println("set root done ");
		stage.show();
		
	}*/
}
