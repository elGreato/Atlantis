package gameObjects;





import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.ColorChoice;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LandTile extends Tile implements Serializable {
	
	private Pawn pawnOnTile;
	private Pawn tempPawn;
	private  ColorChoice landTileColor;
	private int landValue;
	private ArrayList<Pawn> pawns=new ArrayList<>();

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
			pawns.add(pawnOnTile);
		
	}
	public void setTempPawn(Pawn pawn){
		this.tempPawn=pawn;
		if(pawn!=null)
			pawns.add(pawn);
			
	}
	public void convertPawns(){
		this.getChildren().add(pawnOnTile);
		this.getChildren().add(tempPawn);
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
	public boolean hasTempPawn(){
		return(tempPawn!=null);
	}
	
	public Pawn getTempPawn() {
		return tempPawn;
	}

	public ArrayList<Pawn> getPawns() {
		return pawns;
	}

	public void setPawns(ArrayList<Pawn> pawns) {
		this.pawns = pawns;
	}


	
}
