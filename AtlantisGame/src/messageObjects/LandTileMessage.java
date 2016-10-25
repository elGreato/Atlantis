package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

import gameObjects.ColorChoice;

public class LandTileMessage extends InGameMessage implements Serializable {
	String gameName;
	ArrayList<landTileProperties> array;
	public LandTileMessage(String gameName) {
		super(gameName);
		this.gameName = gameName;
		array=new ArrayList<>();

	}

	
	
	
	
	
	
	private class landTileProperties {
		int id;
		ColorChoice color;
		int value;
		
	}
}