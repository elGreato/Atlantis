package gameObjects;

import messageObjects.InGameMessage;

public interface GameInterface {
	void processMessage(InGameMessage igm);
	void handlePlayerLeave(String leavingPlayer);
	String getName(); 
}
