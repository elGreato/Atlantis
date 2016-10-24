package client.game;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView {
	public GameView()
	{
		Stage stage = new Stage();
		GridPane root = new GridPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
