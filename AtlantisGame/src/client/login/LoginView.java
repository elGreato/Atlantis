package client.login;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginView {
	Stage stage;
	Scene scene;
	GridPane root;
	
	public LoginView()
	{
		stage = new Stage();
		root = new GridPane();
		scene = new Scene(root);
		stage.setScene(scene);
	}
	
	public void start()
	{
		stage.show();
	}
	
}
