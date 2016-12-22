package messageObjects;

import java.io.Serializable;

import gameObjects.AtlantisTile;
import gameObjects.MainLand;

/**
 * <h1>atlantis main land message</h1> 
 * this is to send the same object to all the clients 
 * 
 * 
 * 
 * @author Ali Habbabeh
 * @version 1.2
 * @since 2016-12-22
 */
public class AtlantisMainLandMessage extends InGameMessage implements Serializable{

	AtlantisTile atlantis;
	MainLand mainland;
	
	public AtlantisMainLandMessage(String gameName, AtlantisTile atlantis, MainLand mainland) {
		super(gameName);
		this.atlantis=atlantis;
		this.mainland=mainland;
		
		
		
	}

	public AtlantisTile getAtlantis() {
		return atlantis;
	}

	public void setAtlantis(AtlantisTile atlantis) {
		this.atlantis = atlantis;
	}

	public MainLand getMainland() {
		return mainland;
	}

	public void setMainland(MainLand mainland) {
		this.mainland = mainland;
	}
	
	
	
	

}
