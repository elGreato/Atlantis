package client.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.sun.java.swing.plaf.windows.resources.windows_zh_TW;

import gameObjects.AtlantisTile;
import gameObjects.Card;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;
import gameObjects.MainLand;
import gameObjects.Player;
import gameObjects.PlayerHand;
import gameObjects.WaterTile;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import messageObjects.PlayerMessage;

public class GameView {
	BorderPane root = new BorderPane();
	HBox hbPlayersInfo = new HBox();
	GridPane mainBoard = new GridPane();
	Scene scene;
	Stage stage;
	private final int numberOfTiles = 120;
	int maxColIndex;
	int maxRowIndex;
	// base for other stacks
	ArrayList<StackPane> base = new ArrayList<>();

	// the main game controls
	Button btnPlayCard = new Button("Play a Selected Card");
	Button btnPayWithCard = new Button("Pay with a Card");
	Button btnPayWithTreasure = new Button("Pay with a treasure");

	// Labels for main game controls
	Label lblGameBtns = new Label("Action Buttons");

	// Vbox to hold buttons

	VBox vbMainControls = new VBox();

	public GameView() {
		root.setCenter(mainBoard);
		mainBoard.setGridLinesVisible(true);
		// set Max indexes
		maxColIndex = 17;// (int) (Math.sqrt(numberOfTiles) * 1.5);
		maxRowIndex = 10;// (int) (Math.sqrt(numberOfTiles));
		// distribute water tiles as a base board
		/*
		 * for (int i = 0; i < (Math.sqrt(numberOfTiles) * 1.5); i++) { for (int
		 * k = 0; k < Math.sqrt(numberOfTiles); k++) {
		 * 
		 * StackPane stack = new StackPane(); WaterTile water = new WaterTile(10
		 * * i + k); water.setCol(k); water.setRow(i);
		 * stack.getChildren().add(water); base.add(stack); addStack(stack);
		 * 
		 * }
		 */
		for (int i = 1; i < 47; i++) {
			StackPane stack = new StackPane();
			WaterTile water = new WaterTile(i);
			stack.getChildren().add(water);
			base.add(stack);
			addStack(stack);
		}

		// Mainland
		mainBoard.add(new MainLand(999, 7, 7), 15, 9);

		// the Atlantis
		mainBoard.add(new AtlantisTile(0, 0, 0), 0, 0);
		// add Buttons
		vbMainControls.getChildren().addAll(lblGameBtns, btnPlayCard, btnPayWithCard, btnPayWithTreasure);
		// add players info
		root.setBottom(hbPlayersInfo);
		root.setRight(vbMainControls);

		// make mainboard scalable
		mainBoard.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

		stage = new Stage();
		mainBoard.setVgap(3);
		mainBoard.setHgap(3);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// those are index for stackpane
	int co = 2;
	int ro = 1;

	private void addStack(StackPane stack) {

		boolean reachedMaxIndex = false;
		boolean reachedFirstCol = true;

		if (ro == 1 && co != 15) {
			mainBoard.add(stack, co, ro);
			co++;
		} else if (co == 15&&ro==1) {

			mainBoard.add(stack, 14, ro + 1);
			reachedMaxIndex = true;
			co = 0;
			ro += 2;

		}

		else if (ro == 3 && co != 15) {

			mainBoard.add(stack, co, ro);
			co++;

		} else if (co == 15&&ro==3) {
			mainBoard.add(stack, 0, ro + 1);
			co = 0;
			ro += 2;
		}
		else	if (ro == 5 && co != 15) {
			mainBoard.add(stack, co, ro);
			co++;
		} else if (co == 15 && ro==5) {

			mainBoard.add(stack, 14, ro + 1);
			reachedMaxIndex = true;
			co = 0;
			ro += 2;

		}
		else if (ro == 7 && co != 15) {
			mainBoard.add(stack, co, ro);
			co++;
		} else if (co == 15&&ro==7) {

			mainBoard.add(stack, 14, ro + 1);
			reachedMaxIndex = true;
			co = 0;
			ro += 2;

		}
		else if (ro == 9 && co != 15) {
			mainBoard.add(stack, co, ro);
			co++;
		} else if (co == 15&&ro==9) {

			mainBoard.add(stack, 14, ro + 1);
			reachedMaxIndex = true;
			co = 0;
			ro += 2; 

		}

	}

	/*
	 * public void distributeLandTiles(ArrayList<LandTile> deckA,
	 * ArrayList<LandTile> deckB) { Iterator<LandTile> it = deckA.iterator(); //
	 * we start with index 2 since the Atlantis booked the index 0 and span //
	 * to four cells int co = 2; int ro = 1;
	 * 
	 * // these two booleans are to add the extra tile between every other row
	 * boolean reachedMaxIndex = false; boolean reachedFirstCol = false; while
	 * (it.hasNext()) { LandTile temp = it.next(); LandTile tile = new
	 * LandTile(temp.getTileId(), temp.getColor(), temp.getLandValue());
	 * 
	 * base.get(co).getChildren().add(tile);
	 * 
	 * tile.setCol(co); tile.setRow(ro); tile.setTxtValue(new
	 * Text(String.valueOf(tile.getLandValue()))); co++; it.remove(); if (co ==
	 * maxColIndex + 1) { if (reachedMaxIndex == false) { LandTile temp2 =
	 * it.next(); LandTile temp3 = new LandTile(temp2.getTileId(),
	 * temp2.getColor(), temp2.getLandValue());
	 * base.get(co).getChildren().add(temp3); temp3.setCol(maxColIndex);
	 * temp3.setRow(ro); reachedMaxIndex = true;
	 * 
	 * } co = 0; ro += 2; } if (co == 0) { if (reachedFirstCol == false) {
	 * LandTile temp2 = it.next(); LandTile temp3 = new
	 * LandTile(temp2.getTileId(), temp2.getColor(), temp2.getLandValue());
	 * base.get(ro + 1).getChildren().add(temp3); temp3.setCol(0);
	 * temp3.setRow(ro); reachedFirstCol = true; } }
	 * 
	 * } it = deckB.iterator(); while (it.hasNext()) { LandTile temp =
	 * it.next(); LandTile tile = new LandTile(temp.getTileId(),
	 * temp.getColor(), temp.getLandValue());
	 * 
	 * base.get(co * 10 + ro).getChildren().add(tile); tile.setCol(co);
	 * tile.setRow(ro); tile.setTxtValue(new
	 * Text(String.valueOf(tile.getLandValue()))); co++; it.remove(); if (co ==
	 * maxColIndex + 1) { if (reachedMaxIndex == true) { LandTile temp2 =
	 * it.next(); LandTile temp3 = new LandTile(temp2.getTileId(),
	 * temp2.getColor(), temp2.getLandValue());
	 * 
	 * base.get(10 * maxColIndex + (ro + 1)).getChildren().add(temp3);
	 * temp3.setCol(maxColIndex); temp3.setRow(ro); reachedMaxIndex = false; }
	 * co = 0; ro += 2; } if (co == 0) { if (reachedFirstCol == true) { LandTile
	 * temp2 = it.next(); LandTile temp3 = new LandTile(temp2.getTileId(),
	 * temp2.getColor(), temp2.getLandValue()); base.get(ro +
	 * 1).getChildren().add(temp3); temp3.setCol(0); temp3.setRow(ro);
	 * reachedFirstCol = false; } }
	 * 
	 * }
	 * 
	 * }
	 */
	public void showPlayer(Player player) {

		player.getLblName().setText(player.getPlayerName());
		player.getVpHolder().setText("Your Victory Points: " + String.valueOf(player.getVictoryPoints()));
		hbPlayersInfo.getChildren().add(player);
		stage.sizeToScene();

	}

	public void setOpponent(Player player, Player opponent) {
		VBox vbOpponentInfo = new VBox();
		Label lblopponentName = new Label();
		Label lblopponentCardCount = new Label();
		lblopponentName.setText(opponent.getPlayerName());
		lblopponentCardCount
				.setText("This enemy has " + String.valueOf(opponent.getPlayerHand().getNumCards()) + " cards\t");
		vbOpponentInfo.getChildren().add(lblopponentName);
		vbOpponentInfo.getChildren().add(lblopponentCardCount);
		hbPlayersInfo.getChildren().add(vbOpponentInfo);

	}

	public ArrayList<StackPane> getBase() {
		return base;
	}

	public void setBase(ArrayList<StackPane> base) {
		this.base = base;
	}

}
