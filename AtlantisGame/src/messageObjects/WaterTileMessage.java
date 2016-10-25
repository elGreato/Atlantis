package messageObjects;

import java.io.Serializable;

public class WaterTileMessage extends InGameMessage implements Serializable{
	
	int waterId;
	int col;
	int row;
	
	public WaterTileMessage (String gameName){
		super(gameName);
		
		
		
		
	}
	

}
