package client.lobby;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LobbyView {
	private Stage stage;
	
	public LobbyView()
	{
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
	}

	public void start() {
		// TODO Auto-generated method stub
		stage.show();
	}
}
