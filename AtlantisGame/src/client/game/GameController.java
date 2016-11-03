package client.game;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class GameController {

	private GameView view;
	private GameModel model;
	public GameController(GameView gameView, GameModel gameModel) {
		this.view = gameView;
		this.model = gameModel;
		for (StackPane s: view.getBase()){
		s.setOnMouseClicked(e-> handleBase(e));
		 
	}
	

}
	private void handleBase(MouseEvent e) {
		((StackPane) e.getSource()).getChildren().remove(((StackPane) e.getSource()).getChildren().size()-1);
	
	}
}