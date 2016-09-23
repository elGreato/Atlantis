package gameObjects;
import java.util.Random;

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
	
	
	
	
	public Player(String name){
		super();  //the player extends vbox 
	
		// empty Labels for cards
		for (int i=0; i<numberOfCards; i++){
			Label lblCard = new Label();
			
		// set class ID for css later
		lblCard.getStylesheets().add("card");	
		
		hboxCards.getChildren().add(lblCard);
		}
		
		//set the name
		lblName.setText(name);
		
		//set the picture
		int numberOfPicturesAvailable=4;
		String[] paths= new String[numberOfPicturesAvailable]; //the idea of using array to save paths was inspired by a stackoverflow commentor http://stackoverflow.com/questions/21428050/load-random-image-from-a-directory-using-javafx
		paths[0]="images4players/player1.png";
		paths[1]="images4players/player2.png";
		paths[2]="images4players/player3.png";
		paths[3]="images4players/player4.png";
		
		Random r= new Random();
		int index =  (r.nextInt(paths.length));
		Image image = new Image(getClass().getResourceAsStream(paths[index]));
		lblPlayerImage.setGraphic(new ImageView(image));
		
		
		
		
		//create the player hand
		
		playerHand = new PlayerHand(name);
		
		//set CSS ID for player
		this.getStylesheets().add("player");
		
		// set the victory Points
		
		vpHolder.setText(String.valueOf(victoryPoints));
		
		
		this.getChildren().add(lblName);
		this.getChildren().add(vpHolder);    //,/*lblPlayerImage,*/ vpHolder);   // i need to add vp as well somehow
		this.getChildren().add(lblPlayerImage);
	}		
							
}
