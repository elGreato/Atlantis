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
	private int startingLocation;
	private Player owner;
	private int pawnId;
	private transient Circle circle;
	private boolean isPawnSelected = false;
	private boolean reachedMainLand =false;
	
	public Pawn(Player player, int pawnId){
		
		owner=player;
		this.pawnId=pawnId;
		
	}

	public static Paint FillColor(Player c ) {
		if (c.getColor().toString().equalsIgnoreCase("blue")) 
			return (Color.web("0x0099FF"));
		else if (c.getColor().toString().equalsIgnoreCase("pink")) 
			return (Color.web("#FF1493"));
		else if (c.getColor().toString().equalsIgnoreCase("green")) 
			return(Color.DARKGREEN);
		else if (c.getColor().toString().equalsIgnoreCase("brown")) 
			return(Color.MEDIUMPURPLE);
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
		oldLocation=this.newLocation;
		this.newLocation = newLocation;
	}

	public int getStartingLocation() {
		return startingLocation;
	}

	public void setStartingLocation(int startingLocation) {
		this.startingLocation = startingLocation;
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

	public boolean ReachedMainLand() {
		return reachedMainLand;
	}

	public void setReachedMainLand(boolean reachedMainLand) {
		this.reachedMainLand = reachedMainLand;
	}

	
	
	

}
