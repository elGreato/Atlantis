package client.login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
	private Stage stage;
	private Scene scene;
	private VBox root;
	private TitledPane loginPane;
	private TitledPane createPane;
	
	private GridPane login;
	private Label logindescriptionlbl;
	private Label loginusernamelbl;
	private Label loginpasswordlbl;
	private TextField loginusernametxt;
	private PasswordField loginpasswordtxt;
	
	private GridPane create;
	private Label createdescriptionlbl;
	private Label createusernamelbl;
	private Label createpasswordlbl;
	private TextField createusernametxt;
	private PasswordField createpasswordtxt;
	
	
	public LoginView()
	{
		stage = new Stage();
		
		login = new GridPane();
		logindescriptionlbl = new Label("Enter your username and the password below: ");
		loginusernamelbl = new Label("Username: ");
		loginpasswordlbl = new Label("Password: ");
		loginusernametxt = new TextField();
		loginpasswordtxt = new PasswordField();
		
		login.add(logindescriptionlbl, 0, 0, 2, 1);
		login.add(loginusernamelbl, 0, 1);
		login.add(loginpasswordlbl, 0, 2);
		login.add(loginusernametxt, 1, 1);
		login.add(loginpasswordtxt, 1, 2);
		
		create = new GridPane();
		createdescriptionlbl = new Label("Enter your preferred username and the password below: ");
		createusernamelbl = new Label("Username: ");
		createpasswordlbl = new Label("Password: ");
		createusernametxt = new TextField();
		createpasswordtxt = new PasswordField();
		
		create.add(createdescriptionlbl, 0, 0, 2, 1);
		create.add(createusernamelbl, 0, 1);
		create.add(createpasswordlbl, 0, 2);
		create.add(createusernametxt, 1, 1);
		create.add(createpasswordtxt, 1, 2);
		
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
	}
	
	public void start()
	{
		stage.show();
	}
	
}
