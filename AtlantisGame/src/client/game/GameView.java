package client.game;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import messageObjects.InGameMessage;

public class GameView {
	GridPane root= new GridPane();
	Scene scene;
	Stage  stage ;
	public GameView()
	{
		stage = new Stage();
		 scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void setWater(GridPane msgIn) {
		
		
		
		
		
		
		
		
		scene = new Scene(root);
		System.out.println("set root done ");
		stage.show();
		
	}
}
