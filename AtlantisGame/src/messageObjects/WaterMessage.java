package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.WaterTile;

/**
 * <h1>water mesage</h1> 
 * sends the water tiles to all the players 
 * has to be the same objects that is why we create it on server side
 * 
 * 
 * @author Ali Habbabeh
 * @version 1.2
 * @since 2016-12-22
 */
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
