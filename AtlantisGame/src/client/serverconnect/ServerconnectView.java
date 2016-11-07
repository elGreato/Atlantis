package client.serverconnect;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ServerconnectView {
	protected Stage stage;
	protected Scene scene;
	private VBox root;
	protected TitledPane autoConnect;
	private VBox autoControls;
	private Label autoDescriptionlbl;
	protected Button autoConnectButton;
	protected TitledPane manualConnect;
	private GridPane manualControls;
	private Label manualDescriptionlbl;
	protected TextField iptxt;
	protected Button manualConnectButton; 
	
	
	
	
	public ServerconnectView(Stage stage)
	{
		this.stage = stage;
		stage.setTitle("Atlantis"); 
		
		root = new VBox();
		
		autoDescriptionlbl = new Label("Scan LAN for active servers:");
		autoConnectButton = new Button("Start");
		autoControls = new VBox();
		autoControls.getChildren().addAll(autoDescriptionlbl, autoConnectButton);
		autoConnect = new TitledPane("Connect automatically", autoControls);
		
		
		manualControls = new GridPane();
		manualConnect = new TitledPane("Connect manually", manualControls);
		manualConnect.setExpanded(false);
		manualDescriptionlbl = new Label("Enter the IP adress of the server below:");
		iptxt = new TextField("127.0.0.1");
		manualConnectButton = new Button("Ok");
		manualControls.add(manualDescriptionlbl,0,0,2,1);
		manualControls.add(iptxt,0,1);
		manualControls.add(manualConnectButton,1,1);
		
		root.getChildren().addAll(autoConnect, manualConnect);
		
		scene = new Scene(root);
		
		stage.setScene(scene);
		stage.setResizable(false);
	}
	
	public void start()
	{
		stage.show();
	}
	
	public void close() {
		stage.close();
	}
}
