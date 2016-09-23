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
	
	private Label logindescriptionlbl;
	private Label usernamelbl;
	private Label passwordlbl;
	
	private TextField usernametxt;
	private PasswordField passwordtxt;
	
	private Label createdescriptionlbl;
	
	public LoginView()
	{
		stage = new Stage();
		
		logindescriptionlbl = new Label("Enter your name and password");
		
		createdescriptionlbl = new Label("Enter your preferred name and password");
		
		loginPane = new TitledPane();
		loginPane.setText("Login");
		loginPane.setContent(logindescriptionlbl);
		loginPane.setExpanded(true);
		
		createPane = new TitledPane();
		createPane.setText("Create account");
		createPane.setContent(createdescriptionlbl);
		createPane.setExpanded(false);
		
		root = new VBox();
		root.getChildren().addAll(loginPane, createPane);
		scene = new Scene(root);
		
		stage.setScene(scene);
	}
	
	public void start()
	{
		stage.show();
	}
	
}
