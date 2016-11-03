package gameObjects;

import messageObjects.InGameMessage;

public interface GameInterface {
	void processMessage(InGameMessage igm);

	String getName();
}
