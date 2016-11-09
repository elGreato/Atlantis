package gameObjects;





import java.io.Serializable;

import gameObjects.ColorChoice;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LandTile extends Tile implements Serializable {
	
	private Pawn pawnOnTile;
	private  ColorChoice landTileColor;
	private int landValue;
	private Pawn tempPawn;

	public LandTile(int tileId,ColorChoice color, int value ){
		super(tileId);
	landTileColor=color;
	landValue=value;
	
	}
	
	public  static Paint getFillColor(LandTile landTile) {
		
			if (landTile.getColor().toString().equalsIgnoreCase("blue")) 
				return (Color.BLUE);
			else if (landTile.getColor().toString().equalsIgnoreCase("red")) 
				return (Color.RED);
			else if (landTile.getColor().toString().equalsIgnoreCase("gray")) 
				return(Color.GRAY);
			else if (landTile.getColor().toString().equalsIgnoreCase("yello")) 
				return(Color.YELLOW);
			else if (landTile.getColor().toString().equalsIgnoreCase("green")) 
				return(Color.GREEN);
			else if (landTile.getColor().toString().equalsIgnoreCase("purple")) 
				return(Color.PURPLE);
			else if (landTile.getColor().toString().equalsIgnoreCase("brown")) 
				return(Color.BROWN);
			return Color.ORANGE;

	
	}

	public Pawn getPawnOnTile() {
		return pawnOnTile;
	}
	public void setPawnOnTile(Pawn pawnOnTile) {
		this.pawnOnTile = pawnOnTile;
		if (pawnOnTile!=null)
		this.getChildren().add(pawnOnTile);
	}
	
	public ColorChoice getColor() {
		return landTileColor;
	}
	public void setLandTileColor(ColorChoice landTileColor) {
		this.landTileColor = landTileColor;
	}
	public int getLandValue() {
		return landValue;
	}
	public void setLandValue(int landValue) {
		this.landValue = landValue;
	}
	public boolean hasPawn(){
		return (pawnOnTile!=null);
	
	}
	
	public Pawn getPawn(){
		return pawnOnTile;
	}

	public Pawn getTempPawn() {
		return tempPawn;
	}

	public void setTempPawn(Pawn tempPawn) {
		this.tempPawn = tempPawn;
		if(tempPawn!=null)
			this.getChildren().add(tempPawn);
	}
		
	
}
