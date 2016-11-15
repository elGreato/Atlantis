package messageObjects.turnMessages;

import java.io.Serializable;

import gameObjects.Pawn;
import messageObjects.InGameMessage;

public class PlayAnotherCardMessage extends InGameMessage implements Serializable {
	Pawn selectedPawn;
	
	
	public PlayAnotherCardMessage(String gameName, Pawn selectedPawn) {
		super(gameName);
		this.selectedPawn=selectedPawn;
	}


	public Pawn getSelectedPawn() {
		return selectedPawn;
	}

}
