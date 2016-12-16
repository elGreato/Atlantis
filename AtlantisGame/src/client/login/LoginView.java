package client.login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
/**
* <h1>View for login screen</h1>
* Creates the view of the login screen
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class LoginView {
	private Stage stage;
	private Scene scene;
	private VBox root;
	protected TitledPane loginPane;
	protected TitledPane createPane;
	
	private GridPane login;
	private Label logindescriptionlbl;
	private Label loginusernamelbl;
	private Label loginpasswordlbl;
	protected TextField loginusernametxt;
	protected PasswordField loginpasswordtxt;
	protected Button loginButton;
	
	private GridPane create;
	private Label createdescriptionlbl;
	private Label createusernamelbl;
	private Label createpasswordlbl;
	protected TextField createusernametxt;
	protected PasswordField createpasswordtxt;
	protected Button createButton;
	
	
	public LoginView()
	{
		stage = new Stage();
		
		login = new GridPane();
		logindescriptionlbl = new Label("Enter your username and the password below: ");
		loginusernamelbl = new Label("Username: ");
		loginpasswordlbl = new Label("Password: ");
		loginusernametxt = new TextField();
		loginpasswordtxt = new PasswordField();
		loginButton = new Button("Login");
		
		login.add(logindescriptionlbl, 0, 0, 3, 1);
		login.add(loginusernamelbl, 0, 1);
		login.add(loginpasswordlbl, 0, 2);
		login.add(loginusernametxt, 1, 1);
		login.add(loginpasswordtxt, 1, 2);
		login.add(loginButton, 1, 3);
		
		create = new GridPane();
		createdescriptionlbl = new Label("Enter your preferred username and the password below: ");
		createusernamelbl = new Label("Username: ");
		createpasswordlbl = new Label("Password: ");
		createusernametxt = new TextField();
		createpasswordtxt = new PasswordField();
		createButton = new Button("Create");
		
		create.add(createdescriptionlbl, 0, 0, 3, 1);
		create.add(createusernamelbl, 0, 1);
		create.add(createpasswordlbl, 0, 2);
		create.add(createusernametxt, 1, 1);
		create.add(createpasswordtxt, 1, 2);
		create.add(createButton, 1, 3);
		
		loginPane = new TitledPane();
		loginPane.setText("Login");
		loginPane.setContent(login);
		loginPane.setExpanded(true);
		
		createPane = new TitledPane();
		createPane.setText("Create account");
		createPane.setContent(create);
		createPane.setExpanded(false);
		
		root = new VBox();
		root.getChildren().addAll(loginPane, createPane);
		scene = new Scene(root);
		
		stage.setScene(scene);
		stage.setTitle("Atlantis");
		stage.setResizable(false);
		
	}
	
	public void start()
	{
		stage.show();
		loginusernametxt.requestFocus();  
	}
	public void close() {
		stage.close();
	}
}
