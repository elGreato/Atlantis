package client.intro;

import client.serverconnect.*;
import gameObjects.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainKevin extends Application{

	ServerconnectView view;
	ServerconnectModel model;
	ServerconnectController controller;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	
	 @Override
	 public void start(Stage primaryStage) {
	
		 view = new ServerconnectView(primaryStage);
		 model = new ServerconnectModel(view);
		 controller = new ServerconnectController(model, view);
		 view.start();
		 
		 
	}

}
