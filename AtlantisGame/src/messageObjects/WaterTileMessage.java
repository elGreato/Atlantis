package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.WaterTile;

public class WaterTileMessage extends InGameMessage implements Serializable{
	
	ArrayList<WaterTile> water;

	public WaterTileMessage (String gameName, ArrayList<WaterTile> array){
		super(gameName);
		water=array;
		
		
		
	}

	public ArrayList<WaterTile> getWater() {
		return water;
	}

	public void setWater(ArrayList<WaterTile> water) {
		this.water = water;
	}

	
}
