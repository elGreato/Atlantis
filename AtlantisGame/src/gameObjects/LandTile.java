package gameObjects;

import gameObjects.Card.ColorChoice;

public class LandTile extends Tile {
	private Pawn pawnOnTile;
	private ColorChoice landTileColor;
	private int landValue;
	
	public LandTile(final int tileId, ColorChoice color, int value) {
		super(tileId);
		landTileColor=color;
		landValue=value;
		

	}
	public boolean hasPawn(){
		return (pawnOnTile!=null);
	
	}
	
	public Pawn getPawn(){
		return pawnOnTile;
	}
	
	
}
