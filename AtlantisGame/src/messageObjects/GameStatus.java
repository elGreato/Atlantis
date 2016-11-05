package messageObjects;

import java.io.Serializable;

public class GameStatus extends InGameMessage implements Serializable {

	boolean startGame;
	
	
	
	
	public GameStatus(String gameName) {
		super(gameName);
		
	}
	public GameStatus(String gameName, , boolean startGame){
		super(gameName);
		this.startGame=startGame;
	}
	
	
	
	

}
