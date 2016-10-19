package gameObjects;
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



public class Player extends VBox{
	private Label lblName= new Label();
	
	private int victoryPoints;
	
	private Label vpHolder =  new Label();  //label to hold int value
	
	private Label lblPlayerImage= new Label();
	
	private HBox hboxCards= new HBox();
	
	private PlayerHand playerHand;
	
	private int numberOfCards=5;
	
	private Pawn pawn;
	
	private ColorChoice color;
	
	
	public Player(String name){
		//the player extends vbox 
		super();  
		
		//create the player hand
		playerHand = new PlayerHand(name);
		
		// empty Labels for cards
		for (int i=0; i<numberOfCards; i++){
		Label lblCard = new Label(" ");
		// set class ID for css later
		lblCard.getStylesheets().add("card");	
		hboxCards.getChildren().add(lblCard);
		}
		
		//set the name
		lblName.setText(name);
		
		//set the picture
		int numberOfPicturesAvailable=4;
		String[] paths= new String[numberOfPicturesAvailable]; 
		paths[0]="images4players/player1.png";
		paths[1]="images4players/player2.png";
		paths[2]="images4players/player3.png";
		paths[3]="images4players/player4.png";
		Random r= new Random();
		int index =  (r.nextInt(paths.length));
		Image image = new Image(getClass().getResourceAsStream(paths[index]));
		lblPlayerImage.setGraphic(new ImageView(image));

		
		//set CSS ID for player
		this.getStylesheets().add("player");
		
		// set the victory Points
		vpHolder.setText(String.valueOf(victoryPoints));
		
		// create 3 pawns for each player
		for (int i=0; i>3;i++){
			Pawn pawn = new Pawn(this);
		
		}
		
		this.getChildren().add(lblName);
		this.getChildren().add(vpHolder);    
		this.getChildren().add(lblPlayerImage);
		this.getChildren().add(hboxCards);
		
	}	
	
	//this method is from the first semester as well
	 public void addCard(Card card) {
	        // Add card to the hand
	        playerHand.addCard(card);
	        
	        // Determine which label this is (index from 0 to 4)
	        int index = playerHand.getNumCards() - 1;
	        
	        // Get the label from the HBox, and update it
	        Label cardLabel = (Label) hboxCards.getChildren().get(index);
	        cardLabel.setGraphic(card.colorChoice.addImage());
	    }	
	 
	 
	 public String getPlayerName() {
			return lblName.getText();
		}
		public void setLblName(Label lblName) {
			this.lblName = lblName;
		}
		public int getVictoryPoints() {
			return victoryPoints;
		}
		 // get each tile in treasuers and take its vallue
		public int countVictoryPoints() {
			int result=0;
			result+=this.playerHand.getCards().size();
			result+= this.playerHand.getTreasuresValue(this.playerHand.getTreasures());
			
			
			return result;
			
			
		}
		public Label getVpHolder() {
			return vpHolder;
		}
		public void setVpHolder(Label vpHolder) {
			this.vpHolder = vpHolder;
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
		public int getNumberOfCards() {
			return numberOfCards;
		}
		public void setNumberOfCards(int numberOfCards) {
			this.numberOfCards = numberOfCards;
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
}
