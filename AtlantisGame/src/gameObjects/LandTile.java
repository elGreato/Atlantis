package gameObjects;

import gameObjects.ColorChoice;

public class LandTile extends Tile {
	
	private Pawn pawnOnTile;
	private ColorChoice landTileColor;
	private int landValue;
	
	public LandTile( int tileId, ColorChoice color, int value) {
		super(tileId);
		landTileColor=color;
		landValue=value;
		

	}
	public Pawn getPawnOnTile() {
		return pawnOnTile;
	}
	public void setPawnOnTile(Pawn pawnOnTile) {
		this.pawnOnTile = pawnOnTile;
	}
	public ColorChoice getLandTileColor() {
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
	
	
}
