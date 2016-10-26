package client.game;

import java.util.Iterator;

import gameObjects.AtlantisTile;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;
import gameObjects.MainLand;
import gameObjects.WaterTile;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView {
	GridPane root = new GridPane();
	Scene scene;
	Stage stage;
	private final int numberOfTiles = 120;
	int maxColIndex;
	int maxRowIndex;
	
	public GameView() {

		// distribute water tiles as a base board
		for (int i = 0; i < (Math.sqrt(numberOfTiles) * 1.5); i++) {
			for (int k = 0; k < Math.sqrt(numberOfTiles); k++) {
				root.add(new WaterTile(i, i, k), i, k);

			}

		}
		// set Max indexes
		maxColIndex = (int) (Math.sqrt(numberOfTiles) * 1.5);
		maxRowIndex = (int) (Math.sqrt(numberOfTiles));
		// Mainland
		root.add(new MainLand(999, 7, 7), maxColIndex - 2, maxRowIndex - 1, 3, 2);

		// the Atlantis
		root.add(new AtlantisTile(0, 0, 0), 0, 0, 2, 2);

		// add the landTiles
	
		stage = new Stage();
		root.setVgap(3);
		root.setHgap(3);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void distributeLandTiles(DeckOfLandTiles deckA) {
		Iterator<LandTile> it = deckA.getDeckOfTiles().iterator();
		// we start with index 2 since the Atlantis booked the index 0 and span
		// to four cells
		int co = 2;
		int ro = 1;
		// these two booleans are to add the extra tile between every other row
		boolean reachedMaxIndex = false;
		boolean reachedFirstCol = false;
		while (it.hasNext()) {
			LandTile tile = it.next();
			root.add(tile, co, ro);
			tile.setCol(co);
			tile.setRow(ro);
			co++;
			it.remove();
			if (co == maxColIndex + 1) {
				if (reachedMaxIndex == false) {
					root.add(it.next(), maxColIndex, ro + 1);
					System.out.println("how many times");
					reachedMaxIndex = true;
				}
				co = 0;
				ro += 2;
			}
			if (co == 0) {
				if (reachedFirstCol == false) {
					root.add(it.next(), 0, ro + 1);
					System.out.println("yo yo");
					reachedFirstCol = true;
				}
			}

		}

		
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
