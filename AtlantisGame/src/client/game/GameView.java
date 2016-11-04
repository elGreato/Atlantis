package client.game;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.AtlantisTile;

import gameObjects.LandTile;
import gameObjects.MainLand;
import gameObjects.Pawn;
import gameObjects.Player;

import gameObjects.WaterTile;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameView {
	BorderPane root = new BorderPane();
	GridPane mainBoard = new GridPane();

	// base for other stacks
	ArrayList<StackPane> base = new ArrayList<>();

	// fx stuff
	Scene scene;
	Stage stage;

	// the main game controls
	Button btnPlayCard = new Button("Play a Selected Card");
	Button btnPayWithCard = new Button("Pay with a Card");
	Button btnPayWithTreasure = new Button("Pay with a treasure");

	// Labels for main game controls
	Label lblGameBtns = new Label("Action Buttons");

	// Vbox to hold buttons
	VBox vbMainControls = new VBox();

	// Atlantis and main land
	StackPane stackAtlantis = new StackPane();
	StackPane stackMainLand = new StackPane();

	// Player View

	private HBox hbPlayersInfo = new HBox();
	private HBox hbOpponents = new HBox();
	private HBox hboxCards = new HBox();

	// VBox to hold  players information
	private VBox vbPlayerInfo = new VBox();
	// VBox to hold individual players
	private VBox vbPlayer = new VBox();

	private Label lblName = new Label();

	private Label vpHolder = new Label();
	private Label lblPlayerImage = new Label();

	private Label lblCard;

	private int numberOfMaxCards = 10;

	// index for pawns on Atlantis
	int y = 5;
	int x = 5;

	int maxColIndex;
	int maxRowIndex;

	public GameView() {
		root.setCenter(mainBoard);
		mainBoard.setGridLinesVisible(false);

		// set Max indexes
		maxColIndex = 14;
		maxRowIndex = 10;

		// col and row constraints for the gridpane
		for (int i = 0; i < 10; i++) {
			RowConstraints con = new RowConstraints();
			con.setPrefHeight(50);
			mainBoard.getRowConstraints().add(con);
			ColumnConstraints colcon = new ColumnConstraints();
			colcon.setPrefWidth(50);
			mainBoard.getColumnConstraints().add(colcon);
		}

		// stacks to hold water panes
		for (int i = 1; i < 54; i++) {
			StackPane stack = new StackPane();
			WaterTile water = new WaterTile(i);
			stack.getChildren().add(water);
			base.add(stack);
			addStack(stack);
		}

		// Mainland

		stackMainLand.getChildren().add(new MainLand(999, 0, 8));
		mainBoard.add(stackMainLand, 0, 7, 2, 2);

		// the Atlantis

		stackAtlantis.getChildren().add(new AtlantisTile(0, 0, 0));
		mainBoard.add(stackAtlantis, 0, 0, 2, 2);

		// add Buttons
		vbMainControls.getChildren().addAll(lblGameBtns, btnPlayCard, btnPayWithCard, btnPayWithTreasure);
		// add players view stuff

		// empty Labels for cards
		for (int i = 0; i < numberOfMaxCards; i++) {
			lblCard = new Label(" ");
			// set class ID for css later
			lblCard.getStylesheets().add("card");
			hboxCards.getChildren().add(lblCard);

		}
		// set the picture
		int numberOfPicturesAvailable = 4;
		String[] paths = new String[numberOfPicturesAvailable];
		paths[0] = "images4players/player1.png";
		paths[1] = "images4players/player2.png";
		paths[2] = "images4players/player3.png";
		paths[3] = "images4players/player4.png";
		Random r = new Random();
		int index = (r.nextInt(paths.length));
		Image image = new Image(getClass().getResourceAsStream(paths[index]));
		lblPlayerImage.setGraphic(new ImageView(image));

		vbPlayerInfo.getChildren().add(lblName);
		vbPlayerInfo.getChildren().add(vpHolder);
		vbPlayerInfo.getChildren().add(lblPlayerImage);
		vbPlayerInfo.getChildren().add(hboxCards);
		vbPlayer.getChildren().add(vbPlayerInfo);
		
		hbPlayersInfo.getChildren().add(vbPlayer);
		
		root.setBottom(hbPlayersInfo);
		root.setRight(vbMainControls);

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

	// add stacks to the gridpane
	private void addStack(StackPane stack) {

		if (((ro == 1) || (ro == 5) || (ro == 9)) && co != maxColIndex) {
			mainBoard.add(stack, co, ro);
			co++;
		} else if (co == maxColIndex && ((ro == 1) || (ro == 5) || (ro == 9))) {

			mainBoard.add(stack, maxColIndex - 1, ro + 1);

			ro += 2;
			co -= 1;
		}

		else if (((ro == 3) || (ro == 7) || (ro == 11)) && co <= maxColIndex && co != 0) {

			mainBoard.add(stack, co, ro);
			co--;

		} else if (co == 0 && ((ro == 3) || (ro == 7) || (ro == 11))) {
			mainBoard.add(stack, 1, ro + 1);

			ro += 2;
			co += 1;
		}

	}

	public void distributeLandTiles(ArrayList<LandTile> deckA, ArrayList<LandTile> deckB) {
		// Distribution of Land Tiles

		// DeckA, according to the rules the first 10 stacks are single and then
		// they are double,

		for (int i = 0; i < 26; i++) {
			LandTile tile = deckA.get(i);
			tile.setLandTileColor(deckA.get(i).getColor());
			Rectangle rec = new Rectangle();
			rec.setWidth(48.00f);
			rec.setHeight(48.00f);
			rec.setFill(LandTile.getFillColor(tile));
			tile.getChildren().addAll(rec,
					new Text(String.valueOf(deckA.get(i).getLandValue()) + "\n" + tile.getColor().toString()));
			base.get(i).getChildren().add(tile);
		}
		for (int i = 11; i < 21; i++) {
			LandTile tile = deckA.get(i + 16);
			tile.setLandTileColor(deckA.get(i + 16).getColor());
			Rectangle rec = new Rectangle();
			rec.setWidth(48.00f);
			rec.setHeight(48.00f);
			rec.setFill(LandTile.getFillColor(tile));
			tile.getChildren().addAll(rec,
					new Text(String.valueOf(deckA.get(i + 16).getLandValue()) + "\n" + tile.getColor().toString()));
			base.get(i).getChildren().add(tile);
		}

		// DeckB

		for (int i = 27; i < 53; i++) {
			LandTile tile = deckB.get(i - 27);
			tile.setLandTileColor(deckB.get(i - 27).getColor());
			Rectangle rec = new Rectangle();
			rec.setWidth(48.00f);
			rec.setHeight(48.00f);
			rec.setFill(LandTile.getFillColor(tile));
			tile.getChildren().addAll(rec,
					new Text(String.valueOf(deckB.get(i - 27).getLandValue()) + "\n" + tile.getColor().toString()));
			base.get(i).getChildren().add(tile);
		}
		for (int i = 28; i < 38; i++) {
			LandTile tile = deckB.get(i - 2);
			tile.setLandTileColor(deckB.get(i - 2).getColor());
			Rectangle rec = new Rectangle();
			rec.setWidth(48.00f);
			rec.setHeight(48.00f);
			rec.setFill(LandTile.getFillColor(tile));
			tile.getChildren().addAll(rec,
					new Text(String.valueOf(deckB.get(i - 2).getLandValue()) + "\n" + tile.getColor().toString()));
			base.get(i).getChildren().add(tile);
		}

	}

	public void showPlayer(Player player) {

		lblName.setText(player.getPlayerName());
		Label lblColor = new Label(player.getColor().toString());
		vbPlayer.getChildren().add(lblColor);
		vpHolder.setText("Your Victory Points: " + String.valueOf(player.getVictoryPoints()));

		for (Pawn p : player.getPawns()) {
			Circle c = new Circle(x, y, 10.0f);
			System.out.println(player.getColor().toString() + "IN GAME VIEWWWWWWW");
			c.setFill(Pawn.FillColor(player));
			p.getChildren().add(c);
			stackAtlantis.getChildren().add(p);
			y += 5;
		}
		x += 10;
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

	public HBox getHboxCards() {
		return hboxCards;
	}

	public void setHboxCards(HBox hboxCards) {
		this.hboxCards = hboxCards;
	}

}
