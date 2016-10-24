package client.game;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import messageObjects.InGameMessage;

public class GameView {
	GridPane root= new GridPane();
	public GameView()
	{
		Stage stage = new Stage();

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void setRoot(GridPane msgIn) {
		root= msgIn;
		
	}
}
