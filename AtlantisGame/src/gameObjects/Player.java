package gameObjects;
/*
 * @author Ali Habbabeh
 */
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Player extends VBox{
	private Label lblName= new Label();
	
	private int victoryPoints;
	
	private Label vpHolder;  //label to hold int value
	
	private Label lblPlayerImage;
	
	private HBox hboxCards= new HBox();
	
	private PlayerHand playerHand;
	
	private int numberOfCards=5;
	
	
	
	
	public Player(String name){
		super();  //the player extends vbox 
		
		this.getChildren().addAll(lblName,lblPlayerImage, vpHolder);   // i need to add vp as well somehow
		
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
		String[] paths= new String[numberOfPicturesAvailable-1]; //the idea of using array to save paths was inspired by a stackoverflow commentor http://stackoverflow.com/questions/21428050/load-random-image-from-a-directory-using-javafx
		paths[0]="player1.png"; paths[1]="player2.png"; paths[2]="player3.png"; paths[3]="player4.png";
		
		int index = (int) (Math.random()*paths.length);
		Image image = new Image( getClass().getResourceAsStream(paths[index]));
		
		//create the player hand
		
		playerHand = new PlayerHand(name);
		
		//set CSS ID for player
		this.getStylesheets().add("player");
		
		
	}			
							
}
