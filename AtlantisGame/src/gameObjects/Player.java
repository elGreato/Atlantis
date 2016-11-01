package gameObjects;

import java.io.Serializable;
import java.util.Random;

import gameObjects.ColorChoice;
/*
 * @author Ali Habbabeh
 */
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Player extends HBox implements Serializable {
	private transient Label lblName = new Label();

	private String name;
	
	private transient VBox playerInfo= new VBox();
	
	
	private int victoryPoints = 0;

	private transient Label vpHolder = new Label(); // label to hold int value

	private transient Label lblPlayerImage = new Label();

	private transient HBox hboxCards = new HBox();

	private PlayerHand playerHand;

	private int numberOfMaxCards = 10;

	private Pawn pawn;

	private transient ColorChoice color;

	private transient Label lblCard;
	
	private transient HBox hbOpponents=new HBox();
	


	public Player(String name) {
		// the player extends vbox
		super();
		this.name = name;
		// create the player hand
		playerHand = new PlayerHand(name);

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

		// set CSS ID for player
		this.getStylesheets().add("player");

		// set the victory Points
		// vpHolder.setText(String.valueOf(victoryPoints));

		// create 3 pawns for each player
		for (int i = 0; i > 3; i++) {
			Pawn pawn = new Pawn(this);

		}

		playerInfo.getChildren().add(lblName);
		playerInfo.getChildren().add(vpHolder);
		playerInfo.getChildren().add(lblPlayerImage);
		playerInfo.getChildren().add(hboxCards);
		
		this.getChildren().add(playerInfo);

	}

	// this method is from the first semester as well
	public void addCard(Card card) {
		// Add card to the hand
		playerHand.addCard(card);

	}

	public int getNumberOfMaxCards() {
		return numberOfMaxCards;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public Label getLblName() {
		return this.lblName;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public int countVictoryPoints() {
		int result = 0;
		result += this.playerHand.getCards().size();
		result += this.playerHand.getTreasuresValue(this.playerHand.getTreasures());
		return result;
	}

	public Label getVpHolder() {
		return vpHolder;
	}

	public void setVpHolder(Label vpHolder) {
		this.vpHolder = vpHolder;
	}

	public String getPlayerName() {
		return name;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public Label getLblPlayerImage() {
		return lblPlayerImage;
	}

	public void setLblPlayerImage(Label lblPlayerImage) {
		this.lblPlayerImage = lblPlayerImage;
	}

	public PlayerHand getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(PlayerHand playerHand) {
		this.playerHand = playerHand;
	}

	public int getNumberOfMAxCards() {
		return numberOfMaxCards;
	}

	public void setNumberOfMaxCards(int numberOfCards) {
		this.numberOfMaxCards = numberOfCards;
	}

	public Pawn getPawn() {
		return pawn;
	}

	public void setPawn(Pawn pawn) {
		this.pawn = pawn;
	}

	public ColorChoice getColor() {
		return color;
	}

	public void setColor(ColorChoice color) {
		this.color = color;
	}

	public HBox getHboxCards() {
		return hboxCards;
	}

	public void setHboxCards(HBox hboxCards) {
		this.hboxCards = hboxCards;
	}

	public HBox getHbOpponents() {
		return hbOpponents;
	}

	public void setHbOpponents(HBox hbOpponents) {
		this.hbOpponents = hbOpponents;
	}

}
