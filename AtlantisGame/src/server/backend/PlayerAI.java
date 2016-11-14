package server.backend;

import java.util.ArrayList;

import gameObjects.Pawn;
import gameObjects.Player;

public class PlayerAI extends Player{
	
	public PlayerAI(String name) {
		super(name);
	}

	protected int calculateMeanDistance()
	{
		int meanDistance = 0;
		for(Pawn p: super.getPawns())
		{
			meanDistance +=p.getNewLocation();
		}
		meanDistance = meanDistance/super.getPawns().size();
		return meanDistance;
	}
}
