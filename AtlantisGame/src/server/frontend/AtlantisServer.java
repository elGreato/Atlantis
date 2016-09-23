package server.frontend;

import javafx.application.Application;
import javafx.stage.Stage;

public class AtlantisServer extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	 @Override
	 public void start(Stage primaryStage) {
	
		 ServerView view = new ServerView(primaryStage);
		 ServerModel model = new ServerModel(view);
		 ServerController controller = new ServerController(view, model);
		 view.start();
		 
		 
	}

}
