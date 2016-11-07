package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.WaterTile;

public class WaterMessage extends InGameMessage implements Serializable {
	
	ArrayList<WaterTile> base;
	
	
	public WaterMessage(String gameName, ArrayList<WaterTile> base) {
		super(gameName);
		this.base=base;
	}


	public ArrayList<WaterTile> getBase() {
		return base;
	}


	public void setBase(ArrayList<WaterTile> base) {
		this.base = base;
	}

	
	
}
