package client.game;

import gameObjects.Pawn;
import gameObjects.WaterTile;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class GameController {

	private GameView view;
	private GameModel model;

	public GameController(GameView gameView, GameModel gameModel) {
		this.view = gameView;
		this.model = gameModel;
		// for (StackPane s: view.getBase()){
		// s.setOnMouseClicked(e-> handleBase(e));

		// }
		view.btnPlayCard.setOnAction(e -> handlePlayCard());
		view.btnBuyCards.setOnAction(e -> handleBuyCard());
		view.btnPay4cards.setOnAction(e-> handlePay4cards());
		view.btnPay4Water.setOnAction(e-> handlePay4Water());
	}



	private Object handlePay4Water() {
		// TODO Auto-generated method stub
		return null;
	}



	private void handlePay4cards() {
		model.pay4Cards();
	
	}



	private void handleBuyCard() {
		model.tryBuyCards();
		
	}

	private void handlePlayCard() {
		model.tryPlayCard();

	}

	private void handleBase(MouseEvent e) {
		if (!(((StackPane) e.getSource()).getChildren()
				.get(((StackPane) e.getSource()).getChildren().size() - 1) instanceof WaterTile))
			((StackPane) e.getSource()).getChildren().remove(((StackPane) e.getSource()).getChildren().size() - 1);

	}

}