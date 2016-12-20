package gameObjects;





import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.ColorChoice;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LandTile extends Tile implements Serializable {
	
	private volatile Pawn pawnOnTile;
	private  Pawn tempPawn;
	private  ColorChoice landTileColor;
	private int landValue;
	private ArrayList<Pawn> pawns=new ArrayList<>();
	private boolean selected=false;
	// assign deck name in case of revert
	private String deckAorB;

	public LandTile(int tileId,ColorChoice color, int value ){
		super(tileId);
	landTileColor=color;
	landValue=value;
	
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
		if(pawnOnTile!=null)
		this.getChildren().add(pawnOnTile);
		if(tempPawn!=null)
		this.getChildren().add(tempPawn);
	}
	public void removePawn(Pawn pawn){
		for (int i=0;i<pawns.size();i++){
			Pawn p=pawns.get(i);
			if (p.getPawnColor().equals(pawn.getPawnColor())&&p.getPawnId()==pawn.getPawnId()){
				pawn=p;
				pawns.remove(pawn);
				
			}
			
		}
		if(pawns.isEmpty())
			setPawnOnTile(null);
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public String getDeckAorB() {
		return deckAorB;
	}


	public void setDeckAorB(String deckAorB) {
		this.deckAorB = deckAorB;
	}

	


	
}
