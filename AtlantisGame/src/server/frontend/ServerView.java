package server.frontend;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
* <h1>View of server GUI</h1>
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class ServerView {
	
	protected Stage stage;
	private Scene scene;
	
	private VBox root;
	
	protected TitledPane serverState;
	protected TitledPane dbState;
	
	private GridPane serverPane;
	private Label serverlbl;
	protected Button serverstartbtn;
	
	private GridPane dbPane;
	private Label dblbl;
	private Label dbusernamelbl;
	protected TextField dbusernametxt;
	private Label dbpasswordlbl;
	protected PasswordField dbpasswordtxt;
	protected Button dbconnectbtn;
	
	public ServerView(Stage stage)
	{
		this.stage = stage;
		
		serverState = new TitledPane();
		serverState.setText("Server");
		serverState.setCollapsible(false);
		
		serverPane = new GridPane();
		serverlbl = new Label("Start server (Connect to database first): ");
		serverstartbtn = new Button("Start");
		serverstartbtn.setDisable(true);
		
		serverPane.add(serverlbl, 0, 0, 3, 1);
		serverPane.add(serverstartbtn, 0, 1);
		
		dbState = new TitledPane();
		dbState.setText("Database");
		dbState.setCollapsible(false);
		
		dbPane = new GridPane();
		dblbl = new Label("Enter database credentials: ");
		dbusernamelbl = new Label("Username: ");
		dbusernametxt = new TextField();
		dbpasswordlbl = new Label("Password: ");
		dbpasswordtxt = new PasswordField();
		dbconnectbtn = new Button("Connect");
		
		dbPane.add(dblbl, 0, 0, 3, 1);
		dbPane.add(dbusernamelbl, 0, 1);
		dbPane.add(dbusernametxt, 1, 1);
		dbPane.add(dbpasswordlbl, 0, 2);
		dbPane.add(dbpasswordtxt, 1, 2);
		dbPane.add(dbconnectbtn, 1, 3);
		
		serverState.setContent(serverPane);
		dbState.setContent(dbPane);
		
		root = new VBox();
		root.getChildren().addAll(dbState, serverState);
		
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Atlantis server frontroend");
		stage.setResizable(false);
	}
	public void start()
	{
		stage.show();
		dbusernametxt.requestFocus();
	}
}
