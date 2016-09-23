package client.serverconnect;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ServerconnectView {
	protected Stage stage;
	private Scene scene;
	private VBox root;
	private HBox controls;
	private Label descriptionlbl;
	protected TextField iptxt;
	protected Button okButton; 
	
	
	
	
	public ServerconnectView(Stage stage)
	{
		this.stage = stage;
		stage.setTitle("Atlantis");
		
		root = new VBox();
		
		descriptionlbl = new Label("Please enter the IP adress of the server!");
		controls = new HBox();
		
		iptxt = new TextField("127.0.0.1");
		okButton = new Button("Ok");
		
		controls.getChildren().addAll(iptxt, okButton);
		root.getChildren().addAll(descriptionlbl, controls);
		
		scene = new Scene(root);
		
		stage.setScene(scene);
		
	}
	
	public void start()
	{
		stage.show();
	}
	
	public void close() {
		stage.close();
	}
}
