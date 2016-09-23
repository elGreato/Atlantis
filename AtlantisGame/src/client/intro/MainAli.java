package client.intro;

import gameObjects.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainAli extends Application {
	public static void main(String[] args) {
		launch();

	}

	 @Override
	    public void start(Stage primaryStage) {
	        primaryStage.setTitle("Atlantis");
	       
	     
	        Player p1 = new Player("Ali");
	        Player p2 = new Player ("Kevin");
	        
	        
	        
	        BorderPane root = new BorderPane();
	        
	        root.setTop(p1);
	        root.setBottom(p2);
	        primaryStage.setScene(new Scene(root, 800, 850));
	        primaryStage.show();
	    }
	}