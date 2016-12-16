package client.intro;

import client.serverconnect.*;
import javafx.application.Application;
import javafx.stage.Stage;
/**
* <h1>Starts client instance of Atlantis</h1>
* A User needs to start the game from this class
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class AtlantisGame extends Application{

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
