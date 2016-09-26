package gameObjects;

public  class WaterTile extends Tile{

	
	public WaterTile(final int tileId) {
		super(tileId);
		
	}
	@Override 
	public boolean hasPawn(){
		return false;
		
	}
}
