//Ali and KevinTheKing
package client.intro;
	
import gameObjects.Player;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			
			//For testing purposes, to be removed later
		Player p1 = new Player("ali");
		Player p2= new Player  ("kevin");
		Player p3= new Player("idiot");
		
		
			root.setTop(p1);
			root.setBottom(p2);
			root.setCenter(p3);
		
			
			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
