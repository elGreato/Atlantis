package client.splashscreen;

import javafx.scene.Group;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SplashScreen extends Application{


	@Override
	public void start(Stage primaryStage) throws Exception {
	    primaryStage.setTitle("Media");
	    Group root = new Group();
	    Media media = new Media("/Atlantis_v1.mp4");
	    MediaPlayer mediaPlayer = new MediaPlayer(media);
	    

	    MediaView mediaView = new MediaView(mediaPlayer);
	    mediaPlayer.play();
	    root.getChildren().add(mediaView);
	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	public static void main(String[] args) {
	    launch(args);
	}
}
