package client.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import gameObjects.AtlantisTile;
import gameObjects.Card;
import gameObjects.LandTile;
import gameObjects.MainLand;
import gameObjects.Pawn;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameView {
	private BorderPane root = new BorderPane();
	private GridPane mainBoard = new GridPane();

	// base for other stacks
	ArrayList<WaterTile> base;

	// fx stuff
	protected Scene scene;
	protected Stage stage;
	protected Stage tempStage;

	// the main game controls
	protected Button btnPlayCard = new Button("Play a Selected Card");
	protected Button btnBuyCards = new Button("Buy Cards");
	protected Button btnPay4Water = new Button("Pay");
	protected Button btnPay4cards = new Button("Pay");
	protected Button btnCalc = new Button("Calculate what I chose");
	protected Button btnEndMyTurn = new Button("End Turn");
	protected Button btnRevert = new Button("Cancel my turn and give me my money back");
	protected Button btnFinish = new Button();
	protected Button btnNotEnough = new Button("I Don't have enough :(");

	// Labels for main game controls
	private Label lblGameBtns = new Label("Action Buttons");
	private Label lblGameStatus = new Label();
	private Label lblTurn = new Label();
	protected Label lblWaterCalc = new Label();
	private Label lblPay = new Label();
	// Vbox to hold buttons and HBox to hold Game status
	private VBox vbMainControls = new VBox();
	private VBox vbGameStatus = new VBox();

	// Player View

	private HBox hbPlayersInfo = new HBox();

	private HBox hboxCards = new HBox();
	private FlowPane fpTreasures = new FlowPane();

	private HashMap<Integer, FlowPane> fpEnemiesTreasures = new HashMap<Integer, FlowPane>();
	private HashMap<Integer, VBox> mapOpponents = new HashMap<>();

	private MainLand mainland;
	private AtlantisTile atlantis;

	// VBox to hold players information+ VBox to hold individual players
	private VBox vbPlayerInfo = new VBox();
	private VBox vbPlayer = new VBox();

	private Label lblName = new Label();
	protected Label vpHolder = new Label();
	private Label lblPlayerImage = new Label();
	private Label lblcards = new Label("Your Cards");
	private Label lbltreasures = new Label("Your Treasures");

	protected boolean multiCardsMode = false;
	int maxColIndex;
	int maxRowIndex;

	// animation stuff for buttons
	protected RotateTransition rotate;

	public GameView() {
		root.setCenter(mainBoard);
		mainBoard.setGridLinesVisible(false);

		// set Max indexes
		maxColIndex = 14;
		maxRowIndex = 10;

		// col and row constraints for the gridpane
		for (int i = 0; i < 14; i++) {
			ColumnConstraints colcon = new ColumnConstraints();
			colcon.setMinWidth(65);
			colcon.setMaxWidth(65);
			mainBoard.getColumnConstraints().add(colcon);
		}
		for (int i = 0; i < 9; i++) {
			RowConstraints con = new RowConstraints();
			con.setMinHeight(65);
			mainBoard.getRowConstraints().add(con);
		}
		// add Buttons
		vbMainControls.getChildren().addAll(lblGameBtns, btnPlayCard, btnBuyCards, btnEndMyTurn);
		vbMainControls.setPadding(new Insets(10, 50, 50, 50));
		vbMainControls.setSpacing(10);

		btnPlayCard.setId("btnPlay");
		btnBuyCards.setId("btnBuy");
		btnEndMyTurn.setId("btnEnd");

		styleButton(btnPlayCard);
		styleButton(btnBuyCards);

		// set a random picture for each player
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

		vbPlayerInfo.getChildren().addAll(lblName, vpHolder, lblPlayerImage, lblcards, hboxCards, lbltreasures,
				fpTreasures);
		vbPlayer.getChildren().add(vbPlayerInfo);
		hbPlayersInfo.getChildren().add(vbPlayer);

		vbGameStatus.getChildren().addAll(lblGameStatus, lblTurn);
		root.setBottom(hbPlayersInfo);
		root.setRight(vbMainControls);
		root.setTop(vbGameStatus);
	
		stage = new Stage();
		mainBoard.setVgap(3);
		mainBoard.setHgap(3);
		scene = new Scene(root, 1280, 1000);
		root.setId("root");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle("Atlantis GAME");
		stage.setResizable(false);

	}

	private void styleButton(Button btn) {
		System.out.println(btn.getId());
		if (btn.getId().equals("btnPlay")) {
			// Create a rotating image and set it as the graphic for the button
			Image img = new Image(getClass().getResourceAsStream("images4Btns/yellow.jpg"));
			ImageView iv = new ImageView(img);
			iv.setFitHeight(30);
			iv.setFitWidth(20);
			final Pane holder = new Pane();
			holder.getChildren().add(iv);
			rotate = new RotateTransition(Duration.seconds(1), iv);
			rotate.setByAngle(360);
			rotate.setCycleCount(Animation.INDEFINITE);
			rotate.setInterpolator(Interpolator.LINEAR);
			btn.setMinWidth(180);
			btn.setMinHeight(90);
			btn.setGraphic(holder);
		}
		if (btn.getId().equals("btnBuy")) {

			Image img = new Image(getClass().getResourceAsStream("images4Btns/dollar.png"));
			ImageView iv = new ImageView(img);
			iv.setFitHeight(30);
			iv.setFitWidth(20);
			final Pane holder = new Pane();
			holder.getChildren().add(iv);
			btn.setMinWidth(180);
			btn.setMinHeight(90);
			btn.setGraphic(holder);

		}

	}

	// those are index for base Children
	// we start with column two for the Atlantis node
	int co = 2;
	int ro = 1;

	// add stacks to the mainBoard
	private void addToMainBoard(WaterTile water) {

		if (((ro == 1) || (ro == 5) || (ro == 9)) && co != maxColIndex) {

			mainBoard.add(water, co, ro);
			co++;
		} else if (co == maxColIndex && ((ro == 1) || (ro == 5) || (ro == 9))) {

			mainBoard.add(water, maxColIndex - 1, ++ro);
			ro++;
			co--;
		} else if (((ro == 3) || (ro == 7) || (ro == 11)) && co <= maxColIndex && co != 0) {

			mainBoard.add(water, co, ro);
			co--;

		} else if (co == 0 && ((ro == 3) || (ro == 7) || (ro == 11))) {
			mainBoard.add(water, 1, ++ro);
			ro++;
			co++;
		}

	}

	// this method adds a Rectangle and Text to each landtile
	public void addRecAndText(ArrayList<WaterTile> base) {
		this.base = base;
		for (int i = 0; i < base.size(); i++) {

			WaterTile water = base.get(i);

			water.convertToChildren();

			if (water.getChildren().size() != 0) {
				for (int k = 0; k < water.getChildren().size(); k++) {

					LandTile tile = (LandTile) water.getChildren().get(k);
					Rectangle rec = new Rectangle();
					rec.setWidth(65);
					rec.setHeight(65);
					rec.setFill(Color.TRANSPARENT);
					tile.getChildren().add(tile.getColor().addLandTileImage(tile.getLandValue()));
					tile.getChildren().add(rec);

				}
			}

			addToMainBoard(water);
		}

	}

	public void showPlayer(Player player) {

		lblName.setText(player.getPlayerName());
		lblName.setFont(Font.font(null, FontWeight.BOLD, 22));
		Rectangle recColor = new Rectangle();
		recColor.setHeight(10);
		recColor.setWidth(150);
		recColor.setFill(Pawn.FillColor(player));
		vbPlayer.getChildren().add(recColor);
		vpHolder.setText("Your Victory Points: " + String.valueOf(player.countVictoryPoints()));

		for (Pawn p : player.getPawns()) {
			Circle c = new Circle();
			c.setRadius(14);
			p.setCircle(c);
			c.setFill(Pawn.FillColor(player));
			p.getChildren().add(c);
			atlantis.getChildren().add(p);
		}
		stage.sizeToScene();

	}

	protected void handlePawn(Pawn p) {
		for (Pawn pp : (p.getOwner()).getPawns()) {
			if (pp.getPawnId() != p.getPawnId()) {
				pp.getCircle().setStroke(Color.TRANSPARENT);
				pp.setPawnSelected(false);
			}
		}
		if (!p.isPawnSelected()) {
			p.getCircle().setStroke(Color.BLACK);
			p.setPawnSelected(true);
		} else {
			p.getCircle().setStroke(Color.TRANSPARENT);
			p.setPawnSelected(false);
		}
	}

	public void setOpponent(Player opponent) {
		VBox vbOpponentInfo = new VBox();
		Label lblopponentName = new Label();
		Label lblopponentCardCount = new Label();
		Label lblOpponentVp = new Label();

		lblopponentName.setText(opponent.getPlayerName());
		lblopponentCardCount
				.setText("This enemy has " + String.valueOf(opponent.getPlayerHand().getNumCards()) + " cards\t");
		lblOpponentVp.setText("And he has " + String.valueOf(opponent.countVictoryPoints() + " Victory Points\t"));
		FlowPane fpEnemyTreasures = new FlowPane();
		Integer index = opponent.getPlayerIndex();
		fpEnemiesTreasures.put(index, fpEnemyTreasures);
		Rectangle recColor = new Rectangle();
		recColor.setHeight(10);
		recColor.setWidth(150);
		recColor.setFill(Pawn.FillColor(opponent));
		vbOpponentInfo.getChildren().addAll(lblopponentName, lblopponentCardCount, lblOpponentVp, fpEnemyTreasures,
				recColor);

		mapOpponents.put(opponent.getPlayerIndex(), vbOpponentInfo);

		hbPlayersInfo.getChildren().add(vbOpponentInfo);

		// i need to find a way to get the circle of the pawn
		for (Pawn p : opponent.getPawns()) {
			Circle c = new Circle();
			c.setRadius(14);
			c.setFill(Pawn.FillColor(opponent));
			p.setCircle(c);
			p.getChildren().add(c);
			atlantis.getChildren().add(p);
		}
	}

	public void replaceLeavingPlayer(int playerIndex, String playerName) {
		VBox opponentToChange = mapOpponents.get(playerIndex);
		Label nameChanging = (Label) opponentToChange.getChildren().get(0);
		nameChanging.setText(playerName);
	}

	public void placeAtlantisMainLand(AtlantisTile atlantis, MainLand mainland) {

		this.atlantis = atlantis;
		this.mainland = mainland;

		// the Atlantis

		mainBoard.add(atlantis, 0, 0, 2, 2);
		Image img = new Image(getClass().getResourceAsStream("images4Tiles/atlantis.png"));
		atlantis.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

		// Mainland

		mainBoard.add(mainland, 0, 7, 2, 2);

		Image im = new Image(getClass().getResourceAsStream("images4Tiles/land.png"));
		mainland.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

	}

	public void gameStarted() {
		lblGameStatus.setText("The GAME HAS BEGUN");
		lblGameStatus.setTextFill(Color.web("#ce2323"));
		lblGameStatus.setFont(new Font("Cambria", 32));
		vbGameStatus.setAlignment(Pos.CENTER);

	}

	public void yourTurn() {
		lblTurn.setText("It is YOUR turn");

	}

	public void notYourTurn(String curPlayer) {
		lblTurn.setText("It is " + curPlayer + " turn");
		lblTurn.setTextFill(Color.BLACK);
		lblTurn.setFont(new Font("Cambria", 25));

	}

	public void playerAnother() {
		lblTurn.setText("Please play another card");
		lblTurn.setTextFill(Color.RED);
		lblTurn.setFont(new Font("Cambria", 32));
	}

	public void createCardView(Card c) {
		c.getChildren().clear();
		c.setCardSelected(false);
		Rectangle rec = new Rectangle();
		rec.setWidth(39);
		rec.setHeight(70);
		rec.setFill(Color.TRANSPARENT);
		c.setRec(rec);
		c.getChildren().add(rec);
		if (c.getColor() != null) {
			c.getChildren().add(c.getColor().addCardImage());
		} else {
			c.getChildren().add(getJokerView());
		}
		// fix duplicate children added exception, that occurs from time to time
		if (getHboxCards().getChildren().contains(c)) {
			getHboxCards().getChildren().remove(c);
		}
		getHboxCards().getChildren().add(c);

	}

	private ImageView getJokerView() {
		Image image = new Image(getClass().getResourceAsStream("joker.jpg"));
		ImageView iv = new ImageView(image);
		iv.setFitHeight(70);
		iv.setFitWidth(39);
		return iv;
	}

	public void handleCard(Card c) {
		if (!multiCardsMode) {
			// first unselected all the other cards in the hand
			for (Card cc : c.getOwner().getPlayerHand().getCards()) {
				if (cc.getCardId() != c.getCardId()) {
					cc.getRec().setStroke(Color.TRANSPARENT);
					cc.setCardSelected(false);
				}
			}
		}
		// this is to give the user ability to unselect a card when he
		// clicks again
		if (!c.isCardSelected()) {
			c.getRec().setStroke(Color.BLACK);
			c.setCardSelected(true);
		} else {
			c.getRec().setStroke(Color.TRANSPARENT);
			c.setCardSelected(false);
		}

	}

	private void handleTreasure(LandTile treasure) {
		if (!treasure.isSelected()) {
			((Rectangle) treasure.getChildren().get(1)).setStroke(Color.BLACK);
			treasure.setSelected(true);
		} else {
			((Rectangle) treasure.getChildren().get(1)).setStroke(Color.TRANSPARENT);
			treasure.setSelected(false);
		}

	}

	public ArrayList<WaterTile> getBase() {
		return base;
	}

	public void setBase(ArrayList<WaterTile> base) {
		this.base = base;
	}

	public HBox getHboxCards() {
		return hboxCards;
	}

	public void setHboxCards(HBox hboxCards) {
		this.hboxCards = hboxCards;
	}

	public void showNotYourTurnAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("not your Turn");

		alert.setContentText("Please Wait for your Turn to play");
		alert.showAndWait();

	}

	public void movePawn(Pawn selectedPawn, LandTile target) {
		// first we check if it is still on the atlnatis
		for (int i = 0; i < atlantis.getChildren().size(); i++) {
			if (((Pawn) atlantis.getChildren().get(i)).getPawnId() == selectedPawn.getPawnId())
				atlantis.getChildren().remove(i);
		}
		LandTile tempLand = null;
		WaterTile tempWater = null;

		for (int i = 0; i < base.size(); i++) {
			tempWater = base.get(i);
			if (tempWater.getChildren().size() != 0) {
				if (((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)) != null)
					tempLand = ((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1));
			}
			if (tempLand != null && tempLand.getPawnOnTile() != null) {

				if (tempLand.getPawnOnTile().getPawnId() == (selectedPawn.getPawnId())) {
					tempLand.setPawnOnTile(null);
				}
			}

		}
	}

	public void givePlayerTreasure(LandTile landTile) {
		landTile.setOnMouseClicked(e -> handleTreasure(landTile));
		fpTreasures.getChildren().add(landTile);

	}

	public void removePlayerTreasure(LandTile landTile) {
		fpTreasures.getChildren().remove(landTile);

	}

	public void removeCardFromHand(Card card) {

		hboxCards.getChildren().remove(card);

	}

	public void giveEnemyTreasure(int indexOfPlayer, LandTile treasure) {
		Integer index = indexOfPlayer;
		FlowPane hbOpponentTreasure = fpEnemiesTreasures.get(index);
		hbOpponentTreasure.getChildren().add(treasure);

	}

	public void selectPawnPlease() {
		lblTurn.setText("Please SELECT A PAWN");
		lblTurn.setTextFill(Color.RED);
		lblTurn.setFont(new Font("Cambria", 25));

	}

	public void showMessageFromServer(String theMessage) {
		lblTurn.setText(theMessage);

	}

	public void selectCardPlease() {
		lblTurn.setText("Please Select a CARD");
		lblTurn.setTextFill(Color.RED);
		lblTurn.setFont(new Font("Cambria", 25));

	}

	public void addPawnToMainLand(Pawn selectedPawn) {
		mainland.getPawns().add(selectedPawn);
		mainland.convertToChildren();

	}

	public void addPawnToAtlantis(Pawn selectedPawn) {
		// prevent duplicate children added exception
		if (atlantis.getChildren().contains(selectedPawn)) {
			atlantis.getChildren().remove(selectedPawn);
		}
		atlantis.getChildren().add(selectedPawn);

	}

	public void showBuyCards() {
		VBox buyPane = new VBox();
		Label lblBuyCards = new Label("Choose the treasures that you would like to Sacrfice to buy cards"
				+ "\nRemember, half of what you pay, rounded down, will be refunded as cards");
		buyPane.getChildren().addAll(lblBuyCards, fpTreasures, btnPay4cards);
		Scene buyScene = new Scene(buyPane,500,400);
		tempStage = new Stage();
		tempStage.setScene(buyScene);
		tempStage.initModality(Modality.WINDOW_MODAL);
		tempStage.initOwner(stage);
		tempStage.show();
		tempStage.setAlwaysOnTop(true);
		tempStage.sizeToScene();
		tempStage.setOnCloseRequest(e -> closeBuyScene());
		

	}

	public void closeBuyScene() {
		vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 1, fpTreasures);
		tempStage.close();

	}

	public void removeEnemyTreasures(int index, LandTile soldLand) {
		FlowPane hbOpponentTreasure = fpEnemiesTreasures.get(index);
		for (int i = 0; i < hbOpponentTreasure.getChildren().size(); i++) {
			if (((LandTile) hbOpponentTreasure.getChildren().get(i)).getTileId() == soldLand.getTileId()) {
				hbOpponentTreasure.getChildren().remove(i);
				lblTurn.setText("A treasure got removed from an Enemy");
			}
		}
	}

	public void setVpForEnemy(int index, int vp) {
		VBox enemy = (VBox) mapOpponents.get(index);
		((Label) enemy.getChildren().get(2)).setText("Victory Points: " + String.valueOf(vp));

	}

	public void setCardCountForEnemy(int playerIndex, int cardsCount) {
		VBox enemy = (VBox) mapOpponents.get(playerIndex);
		((Label) enemy.getChildren().get(1)).setText("Number of Cards: " + String.valueOf(cardsCount));

	}

	public void showWaterBill(int waterBill, int waterPassedCount, boolean gameFinished) {
		VBox payPane = new VBox();
		multiCardsMode = true;
		lblPay.setText("");
		lblWaterCalc.setText("");
		lblPay.setText("You have crossed " + String.valueOf(waterPassedCount) + " Water Tiles"
				+ "\n Now you have to pay " + String.valueOf(waterBill)
				+ " points, choose the treasures and cards that you wanna pay with");

		btnPay4Water.setDisable(true);
		payPane.getChildren().addAll(lblPay, lblWaterCalc, fpTreasures, hboxCards, btnCalc, btnPay4Water, btnRevert);
		Scene payScene = new Scene(payPane,500,400);
		tempStage = new Stage();
		tempStage.setScene(payScene);
		tempStage.initModality(Modality.WINDOW_MODAL);
		tempStage.initOwner(stage);
		tempStage.setTitle("Payment for water");
		tempStage.show();
		tempStage.setAlwaysOnTop(true);
		tempStage.setOnCloseRequest(e -> e.consume());
		if (gameFinished) {
			lblPay.setText("The Game is OVER, now all the pawns are moved to MainLand"
					+ "\n BUT you still have to pay for crossing " + String.valueOf(waterPassedCount) + " Water Tiles"
					+ "\n total amount:  " + String.valueOf(waterBill)
					+ " points, choose the treasures and cards that you wanna pay with\nIf you don't have enough, then we will register negative score for you");
			lblTurn.setText("The Game Has Finished");
			lblGameStatus.setText("Game Over!");
			payPane.getChildren().remove(btnRevert);
			payPane.getChildren().add(btnNotEnough);
			btnNotEnough.setDisable(true);
			tempStage.sizeToScene();

		}
		tempStage.sizeToScene();
		tempStage.setTitle(lblName.getText() + " Water Bill");

	}

	public void closePayWaterScene() {
		if (tempStage != null && tempStage.isShowing()) {
			vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 1, fpTreasures);
			vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 2, hboxCards);
			lblPay.setText(" ");
			multiCardsMode = false;
			tempStage.close();
		}
	}

	public void IWin(String winner) {
		lblTurn.setText("Game is Over");
		tempStage.setOnCloseRequest(e -> e.consume());
		tempStage = new Stage();
		tempStage.initStyle(StageStyle.TRANSPARENT);
		Text text = new Text("Congratulation " + winner + " YOU WON!!!!!!");
		text.setFont(Font.font("Tahoma", 80));
		text.setFill(Color.DARKBLUE);
		VBox box = new VBox();
		box.getChildren().add(text);
		final Scene scene = new Scene(box);
		scene.setFill(null);
		tempStage.setScene(scene);
		tempStage.show();
		tempStage.sizeToScene();
		PauseTransition delay = new PauseTransition(Duration.seconds(6));
		delay.setOnFinished(event -> tempStage.close());
		delay.play();

	}

	public void ILose(String winner) {
		lblTurn.setText("Game is Over");
		tempStage.setOnCloseRequest(e -> e.consume());
		tempStage = new Stage();
		tempStage.initStyle(StageStyle.TRANSPARENT);
		Text text = new Text("Unfortunatly You lost \nThe Winner is  " + winner);
		text.setFont(Font.font("Tahoma", 80));
		text.setFill(Color.RED);
		VBox box = new VBox();
		box.getChildren().add(text);
		final Scene scene = new Scene(box);
		scene.setFill(null);
		tempStage.setScene(scene);
		tempStage.show();
		tempStage.sizeToScene();
		PauseTransition delay = new PauseTransition(Duration.seconds(6));
		delay.setOnFinished(event -> tempStage.close());
		delay.play();

	}

	public void showDontUserAllCardsAlert() {
		lblPay.setText("you are not allowed to pay all your cards\n when you land on a tile that has a pawn already");

	}

	// Added by Kevin
	public void removePawnFromMainLand(Pawn selectedPawn, boolean thisPlayerReverted) {
		mainland.getPawns().remove(selectedPawn);
		mainland.getChildren().remove(selectedPawn);
		mainland.convertToChildren();
		if(thisPlayerReverted)
		{
			selectedPawn.setOnMouseClicked((e) -> handlePawn(selectedPawn));
			selectedPawn.setPawnSelected(true);
		}

	}

}
