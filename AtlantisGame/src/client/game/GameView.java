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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameView {
	private BorderPane root = new BorderPane();
	private GridPane mainBoard = new GridPane();

	// base for other stacks
	ArrayList<WaterTile> base;

	// fx stuff
	public Scene scene;
	public Stage stage;
	public Stage tempStage;

	// the main game controls
	public Button btnPlayCard = new Button("Play a Selected Card");
	public Button btnBuyCards = new Button("Buy Cards");
	public Button btnPay4Water = new Button("Pay");
	public Button btnPay4cards = new Button("Pay");
	public Button btnCalc = new Button("Calculate what I chose");
	public Button btnEndMyTurn = new Button ("End Turn");
	public Button btnRevert = new Button ("Cancel my turn and give me my money back");
	Button temp = new Button ("nigga");

	// Labels for main game controls
	private Label lblGameBtns = new Label("Action Buttons");
	private Label lblGameStatus = new Label();
	private Label lblTurn = new Label();
	protected Label lblWaterCalc = new Label();
	private Label lblPay= new Label();
	// Vbox to hold buttons and HBox to hold Game status
	private VBox vbMainControls = new VBox();
	private VBox vbGameStatus = new VBox();

	// Atlantis and mainland Holders
	private StackPane stackAtlantis = new StackPane();
	private StackPane stackMainLand = new StackPane();

	// Player View

	private HBox hbPlayersInfo = new HBox();

	private HBox hboxCards = new HBox();
	private HBox hbTreasures = new HBox();

	private HashMap<Integer, HBox> hbEnemiesTreasures = new HashMap<Integer, HBox>();
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

	// index for pawns on Atlantis
	private int y = 0;
	private int x = 5;

	protected boolean multiCardsMode = false;
	int maxColIndex;
	int maxRowIndex;

	public GameView() {
		root.setCenter(mainBoard);
		mainBoard.setGridLinesVisible(false);

		// set Max indexes
		maxColIndex = 14;
		maxRowIndex = 10;

		// col and row constraints for the gridpane
		for (int i = 0; i < 14; i++) {
			ColumnConstraints colcon = new ColumnConstraints();
			colcon.setPrefWidth(70);
			mainBoard.getColumnConstraints().add(colcon);
		}
		for (int i = 0; i < 9; i++) {
			RowConstraints con = new RowConstraints();
			con.setPrefHeight(70);
			mainBoard.getRowConstraints().add(con);
		}
		// add Buttons
		vbMainControls.getChildren().addAll(lblGameBtns, btnPlayCard, btnBuyCards,btnEndMyTurn,temp);

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

		vbPlayerInfo.getChildren().add(lblName);
		vbPlayerInfo.getChildren().add(vpHolder);
		vbPlayerInfo.getChildren().add(lblPlayerImage);
		vbPlayerInfo.getChildren().add(lblcards);
		vbPlayerInfo.getChildren().add(hboxCards);
		vbPlayerInfo.getChildren().add(lbltreasures);
		vbPlayerInfo.getChildren().add(hbTreasures);
		vbPlayer.getChildren().add(vbPlayerInfo);
		hbPlayersInfo.getChildren().add(vbPlayer);
	/*	for(int i=0; i<mapOpponents.size();i++){
			VBox vb = mapOpponents.get(i);
		hbPlayersInfo.getChildren().add(vb);
		}*/
		vbGameStatus.getChildren().addAll(lblGameStatus, lblTurn);
		root.setBottom(hbPlayersInfo);
		root.setRight(vbMainControls);
		root.setTop(vbGameStatus);

		stage = new Stage();
		mainBoard.setVgap(3);
		mainBoard.setHgap(3);
		scene = new Scene(root);
		final Image im = new Image(getClass().getResourceAsStream("images4mainBoard/waterback.jpg"));
		mainBoard.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		stage.setScene(scene);
		stage.setTitle("Atlantis GAME");
		stage.show();

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

			mainBoard.add(water, maxColIndex - 1, ro + 1);
			ro += 2;
			co -= 1;
		} else if (((ro == 3) || (ro == 7) || (ro == 11)) && co <= maxColIndex && co != 0) {

			mainBoard.add(water, co, ro);
			co--;

		} else if (co == 0 && ((ro == 3) || (ro == 7) || (ro == 11))) {
			mainBoard.add(water, 1, ro + 1);
			ro += 2;
			co += 1;
		}

	}

	// this method adds a Rectangle and Text to each landtile
	public void addRecAndText(ArrayList<WaterTile> base) {
		this.base = base;
		for (int i = 0; i < base.size(); i++) {

			WaterTile water = base.get(i);
			final Image im = new Image(getClass().getResourceAsStream("images4Tiles/water.jpg"));
			water.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			water.convertToChildren();

			if (water.getChildren().size() != 0) {
				for (int k = 0; k < water.getChildren().size(); k++) {

					LandTile tile = (LandTile) water.getChildren().get(k);
					Rectangle rec = new Rectangle();
					rec.setWidth(68.00f);
					rec.setHeight(68.00f);
					rec.setFill(LandTile.getFillColor(tile));
					tile.getChildren().addAll(rec,
							new Text(String.valueOf(tile.getLandValue()) + "\n" + tile.getColor().toString()));

				}
			}

			addToMainBoard(water);
		}

	}

	public void showPlayer(Player player) {

		lblName.setText(player.getPlayerName());
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
		lblopponentName.setText(opponent.getPlayerName());
		lblopponentCardCount
				.setText("This enemy has " + String.valueOf(opponent.getPlayerHand().getNumCards()) + " cards\t");
		HBox hbEnemyTreasures = new HBox();
		Integer index = opponent.getPlayerIndex();
		hbEnemiesTreasures.put(index, hbEnemyTreasures);
		Rectangle recColor = new Rectangle();
		recColor.setHeight(10);
		recColor.setWidth(150);
		recColor.setFill(Pawn.FillColor(opponent));
		vbOpponentInfo.getChildren().addAll(lblopponentName, lblopponentCardCount, hbEnemyTreasures, recColor);
		
		mapOpponents.put(opponent.getPlayerIndex(),vbOpponentInfo);

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

	public void placeAtlantisMainLand(AtlantisTile atlantis, MainLand mainland) {

		this.atlantis = atlantis;
		this.mainland = mainland;

		// the Atlantis

		stackAtlantis.getChildren().add(atlantis);
		mainBoard.add(stackAtlantis, 0, 0, 2, 2);
		Image img = new Image(getClass().getResourceAsStream("images4Tiles/atlantisTileImage.jpg"));
		atlantis.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

		// Mainland
		stackMainLand.getChildren().add(mainland);
		mainBoard.add(stackMainLand, 0, 7, 2, 2);
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/mainLand.jpg"));
		mainland.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

	}

	public void gameStarted() {
		lblGameStatus.setText("The GAME HAS BEGAN");
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
		Rectangle rec = new Rectangle();
		rec.setWidth(31);
		rec.setHeight(51);
		rec.setFill(Card.FillColor(c));
		c.setRec(rec);
		c.getChildren().add(rec);
		getHboxCards().getChildren().add(c);

	}

	public void handleCard(Card c) {
		if (!multiCardsMode) {
			// first unselected all the other cards in the hand
			for (Card cc : c.getOwner().getPlayerHand().getCards()) {
				if (cc.getCardId() != c.getCardId()) {
					cc.getRec().setStroke(Color.TRANSPARENT);
					cc.setCardSelected(false);
				}
			}}
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
			((Rectangle) treasure.getChildren().get(0)).setStroke(Color.BLACK);
			treasure.setSelected(true);
		} else {
			((Rectangle) treasure.getChildren().get(0)).setStroke(Color.TRANSPARENT);
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
		hbTreasures.getChildren().add(landTile);

	}

	public void removePlayerTreasure(LandTile landTile) {
		hbTreasures.getChildren().remove(landTile);

	}

	public void removeCardFromHand(Card card) {
		hboxCards.getChildren().remove(card);

	}

	public void giveEnemyTreasure(int indexOfPlayer, LandTile treasure) {
		Integer index = indexOfPlayer;
		HBox hbOpponentTreasure = hbEnemiesTreasures.get(index);
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

	public void showBuyCards() {
		VBox buyPane = new VBox();
		Label lblBuyCards = new Label("Choose the treasures that you would like to Sacrfice to buy cards"
				+ "\nRemember, half of what you pay, rounded down, will be refunded as cards");
		buyPane.getChildren().addAll(lblBuyCards, hbTreasures, btnPay4cards);
		Scene buyScene = new Scene(buyPane);
		tempStage = new Stage();
		tempStage.setScene(buyScene);
		tempStage.initModality(Modality.WINDOW_MODAL);
		tempStage.initOwner(stage);
		tempStage.show();
		tempStage.setAlwaysOnTop(true);
		tempStage.sizeToScene();

	}

	public void closeBuyScene() {
		vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 1, hbTreasures);
		tempStage.close();

	}

	public void removeEnemyTreasures(int index, LandTile soldLand) {
		HBox hbOpponentTreasure = hbEnemiesTreasures.get(index);
		for (int i = 0; i < hbOpponentTreasure.getChildren().size(); i++) {
			if (((LandTile) hbOpponentTreasure.getChildren().get(i)).getTileId() == soldLand.getTileId()) {
				hbOpponentTreasure.getChildren().remove(i);
				lblTurn.setText("A treasure got removed from an Enemy");
			}
		}
	}

	public void showWaterBill(int waterBill, int waterPassedCount, boolean gameFinished) {
		VBox payPane = new VBox();
		multiCardsMode=true;
		lblPay.setText("");
		 lblPay.setText("You have crossed " + String.valueOf(waterPassedCount) + " Water Tiles"
				+ "\n Now you have to pay " + String.valueOf(waterBill)
				+ " points, choose the treasures and cards that you wanna pay with");
		
		btnPay4Water.setDisable(true);
		payPane.getChildren().addAll(lblPay,lblWaterCalc, hbTreasures, hboxCards,btnCalc, btnPay4Water, btnRevert);
		Scene payScene = new Scene(payPane);
		tempStage = new Stage();
		tempStage.setScene(payScene);
		tempStage.initModality(Modality.WINDOW_MODAL);
		tempStage.initOwner(stage);
		tempStage.setTitle("Payment for water");
		tempStage.show();
		tempStage.setAlwaysOnTop(true);
		tempStage.setOnCloseRequest(e-> e.consume());
		if(gameFinished){
			 lblPay.setText("The Game is OVER, now all the pawns are moved to MainLand"
			 		+ "\n BUT you still have to pay for crossing " + String.valueOf(waterPassedCount) + " Water Tiles"
						+ "\n total amount:  " + String.valueOf(waterBill)
						+ " points, choose the treasures and cards that you wanna pay with");
			lblTurn.setText("The Game Has Finished");
			lblGameStatus.setText("Game Over!");
		}
		tempStage.sizeToScene();

	}
	public void closePayWaterScene(){
		vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 1, hbTreasures);
		vbPlayer.getChildren().add(vbPlayer.getChildren().size() - 2, hboxCards);
		lblPay.setText(" ");
		multiCardsMode=false;
		tempStage.close();
	}

	public void setCarsCountForEnemy(int playerIndex, int cardsCount) {
		VBox enemy = (VBox) mapOpponents.get(playerIndex);
		((Label)enemy.getChildren().get(1)).setText(String.valueOf(cardsCount));
		
	}
}
