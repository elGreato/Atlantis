package messageObjects;

import java.io.Serializable;

import gameObjects.MainBoard;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ServerViewMessage extends InGameMessage implements Serializable{
	
	MainBoard root;
	String gameName;
	
	public ServerViewMessage(String gameName)
	{
		super(gameName);
		this.gameName=gameName;
	//	Stage stage = new Stage();
		 root = new MainBoard(120);
	/*	Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();*/
	}
}
