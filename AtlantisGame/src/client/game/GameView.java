package client.game;

import java.util.ArrayList;
import java.util.Iterator;


import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import messageObjects.InGameMessage;
import messageObjects.WaterTileMessage;

public class GameView {
	GridPane root= new GridPane();
	Scene scene;
	Stage  stage ;
	public GameView()
	{
		
		// distribute water tiles as a base board
		for (int i = 0; i < (Math.sqrt(120)*1.5); i++) {
			for (int k=0; k<Math.sqrt(120); k++){
			root.add(new WaterTileClient(i,i,k),i,k);
			
			}
			
		}
		stage = new Stage();
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
