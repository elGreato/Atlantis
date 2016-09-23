//Ali and KevinTheKing
package client.intro;
	
import client.serverconnect.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private ServerconnectModel scm;
	private ServerconnectView scv;
	private ServerconnectController scc;
	
	@Override
	public void start(Stage primaryStage) {
		scm = new ServerconnectModel();
		scv = new ServerconnectView(primaryStage);
		scc = new ServerconnectController();
		
		scv.start();
	}
	
	
}
