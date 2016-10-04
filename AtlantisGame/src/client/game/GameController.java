package client.game;

public class GameController {

	private GameView view;
	private GameModel model;
	public GameController(GameView gameView, GameModel gameModel) {
		this.view = gameView;
		this.model = gameModel;
	}

}
