package client.splashscreen;

import javafx.scene.Group;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen  {
	protected Stage primaryStage=new Stage();
	protected Stage mainStage;
	public SplashScreen(Stage s) {
		mainStage=s;
		mainStage.hide();
		primaryStage.setTitle("Media");
		Group root = new Group();

		MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("Atlantis_v1.mp4").toExternalForm()));
		MediaView mediaView = new MediaView(player);
		player.play();
		mediaView.setFitWidth(1200);
		mediaView.setFitHeight(796);
		root.getChildren().add(mediaView);
		Scene scene = new Scene(root, 1200, 690);
		primaryStage.setScene(scene);
		primaryStage.show();
		PauseTransition delay = new PauseTransition(Duration.seconds(11));
		delay.setOnFinished(event -> showandhide());
		delay.play();
		
	}
	private void showandhide() {
		primaryStage.close();
		
		mainStage.show();
		
	}
	

}
