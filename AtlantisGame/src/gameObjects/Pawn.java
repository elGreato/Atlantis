package gameObjects;

import java.io.Serializable;

import gameObjects.ColorChoice;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class Pawn extends StackPane implements Serializable{

	private  ColorChoice pawnColor;
	private int oldLocation;
	private int newLocation;
	private Player owner;
	private int pawnId;
	private transient Circle circle;
	private boolean isPawnSelected = false;
	
	public Pawn(Player player, int pawnId){
		
		owner=player;
		this.pawnId=pawnId;
		
	}

	public static Paint FillColor(Player c ) {
		if (c.getColor().toString().equalsIgnoreCase("blue")) 
			return (Color.BLUE);
		else if (c.getColor().toString().equalsIgnoreCase("red")) 
			return (Color.RED);
		else if (c.getColor().toString().equalsIgnoreCase("gray")) 
			return(Color.GRAY);
		else if (c.getColor().toString().equalsIgnoreCase("yello")) 
			return(Color.YELLOW);
		else if (c.getColor().toString().equalsIgnoreCase("green")) 
			return(Color.GREEN);
		else if (c.getColor().toString().equalsIgnoreCase("purple")) 
			return(Color.PURPLE);
		else if (c.getColor().toString().equalsIgnoreCase("brown")) 
			return(Color.BROWN);
		return Color.ORANGE;
		
			
		
	}

	public ColorChoice getPawnColor() {
		return pawnColor;
	}

	public void setPawnColor(ColorChoice pawnColor) {
		this.pawnColor = pawnColor;
	}

	public Player getOwner() {
		return owner;
	}

	public int getPawnId() {
		return pawnId;
	}

	public void setPawnId(int pawnId) {
		this.pawnId = pawnId;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}


	public int getOldLocation() {
		return oldLocation;
	}

	public void setOldLocation(int oldLocation) {
		this.oldLocation = oldLocation;
	}

	public int getNewLocation() {
		return newLocation;
	}

	public void setNewLocation(int newLocation) {
		this.newLocation = newLocation;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public boolean isPawnSelected() {
		return isPawnSelected;
	}

	public void setPawnSelected(boolean isPawnSelected) {
		this.isPawnSelected = isPawnSelected;
	}

	
	
	

}
