package gameObjects;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Player extends VBox{
	private Label lblName= new Label();
	
	private int victoryPoints;
	
	private Label lblPlayerImage;
	
	private PlayerHand playerHand;
	
	
	
	
	public Player(String name){
		super();  //the player extends vbox 
		
		this.getChildren().addAll(lblName,lblPlayerImage);   // i need to add vp as well somehow
		
		
		
		
		
	}			
							
}
