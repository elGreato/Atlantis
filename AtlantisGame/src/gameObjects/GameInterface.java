package gameObjects;

import messageObjects.InGameMessage;
/**
* <h1>Interface between lobby and game </h1>
* 
* Kevin fill this one please
* @author  
* @version 1.2
* @since   2016-12-22
*/
public interface GameInterface {
	void processMessage(InGameMessage igm);
	void handlePlayerLeave(String leavingPlayer);
	String getName(); 
}
