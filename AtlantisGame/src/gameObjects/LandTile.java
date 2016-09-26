package gameObjects;

public class LandTile extends Tile {
	private Pawn pawnOnTile;
	
	public LandTile(final int tileId) {
		super(tileId);

	}
	public boolean hasPawn(){
		return (pawnOnTile!=null);
	
	}
	
	public Pawn getPawn(){
		return pawnOnTile;
	}
	
	
}
