package client.game;

import java.util.ArrayList;
import java.util.Iterator;

import gameObjects.AtlantisTile;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;
import gameObjects.MainLand;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import messageObjects.PlayerMessage;

public class GameView {
	BorderPane root = new BorderPane();
	GridPane mainBoard = new GridPane();
	Scene scene;
	Stage stage;
	private final int numberOfTiles = 120;
	int maxColIndex;
	int maxRowIndex;

	public GameView() {
		root.setCenter(mainBoard);
		// distribute water tiles as a base board
		for (int i = 0; i < (Math.sqrt(numberOfTiles) * 1.5); i++) {
			for (int k = 0; k < Math.sqrt(numberOfTiles); k++) {
				mainBoard.add(new WaterTile(i, i, k), i, k);

			}

		}
		// set Max indexes
		maxColIndex = (int) (Math.sqrt(numberOfTiles) * 1.5);
		maxRowIndex = (int) (Math.sqrt(numberOfTiles));
		// Mainland
		mainBoard.add(new MainLand(999, 7, 7), maxColIndex - 2, maxRowIndex - 1, 3, 2);

		// the Atlantis
		mainBoard.add(new AtlantisTile(0, 0, 0), 0, 0, 2, 2);

		// add the landTiles

		stage = new Stage();
		mainBoard.setVgap(3);
		mainBoard.setHgap(3);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void distributeLandTiles(ArrayList<LandTile> deckA, ArrayList<LandTile> deckB) {
		Iterator<LandTile> it = deckA.iterator();
		// we start with index 2 since the Atlantis booked the index 0 and span
		// to four cells
		int co = 2;
		int ro = 1;
		// these two booleans are to add the extra tile between every other row
		boolean reachedMaxIndex = false;
		boolean reachedFirstCol = false;
		while (it.hasNext()) {
			LandTile temp = it.next();
			LandTile tile = new LandTile(temp.getTileId(), temp.getColor(), temp.getLandValue());

			mainBoard.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			tile.setTxtValue(new Text(String.valueOf(tile.getLandValue())));
			co++;
			it.remove();
			if (co == maxColIndex + 1) {
				if (reachedMaxIndex == false) {
					LandTile temp2 = it.next();
					LandTile temp3 = new LandTile(temp2.getTileId(), temp2.getColor(), temp2.getLandValue());
					mainBoard.add(temp3, maxColIndex, ro + 1);
					temp3.setCol(maxColIndex);
					temp3.setRow(ro);
					reachedMaxIndex = true;
				}
				co = 0;
				ro += 2;
			}
			if (co == 0) {
				if (reachedFirstCol == false) {
					LandTile temp2 = it.next();
					LandTile temp3 = new LandTile(temp2.getTileId(), temp2.getColor(), temp2.getLandValue());
					mainBoard.add(temp3, 0, ro + 1);
					temp3.setCol(0);
					temp3.setRow(ro);
					reachedFirstCol = true;
				}
			}

		}
		it = deckB.iterator();
		while (it.hasNext()) {
			LandTile temp = it.next();
			LandTile tile = new LandTile(temp.getTileId(), temp.getColor(), temp.getLandValue());

			mainBoard.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			tile.setTxtValue(new Text(String.valueOf(tile.getLandValue())));
			co++;
			it.remove();
			if (co == maxColIndex + 1) {
				if (reachedMaxIndex == true) {
					LandTile temp2 = it.next();
					LandTile temp3 = new LandTile(temp2.getTileId(), temp2.getColor(), temp2.getLandValue());

					mainBoard.add(temp3, maxColIndex, ro + 1);
					temp3.setCol(maxColIndex);
					temp3.setRow(ro);
					reachedMaxIndex = false;
				}
				co = 0;
				ro += 2;
			}
			if (co == 0) {
				if (reachedFirstCol == true) {
					LandTile temp2 = it.next();
					LandTile temp3 = new LandTile(temp2.getTileId(), temp2.getColor(), temp2.getLandValue());
					mainBoard.add(temp3, 0, ro + 1);
					temp3.setCol(0);
					temp3.setRow(ro);
					reachedFirstCol = false;
				}
			}

		}

	}

	public void showPlayer(Player player) {
		
		player.getLblName().setText(player.getPlayerName());
		root.setBottom(player);
		
	}

	/*
	 * public void setWater(WaterTileMessage msgIn) {
	 * 
	 * 
	 * scene = new Scene(root); System.out.println("set root done ");
	 * stage.show();
	 * 
	 * }
	 */
}
