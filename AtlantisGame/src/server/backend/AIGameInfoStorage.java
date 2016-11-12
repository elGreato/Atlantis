package server.backend;

import java.util.ArrayList;

import gameObjects.WaterTile;
import messageObjects.InGameMessage;

public class AIGameInfoStorage {
	private String gameName;
	public String getGameName() {
		return gameName;
	}
	private ArrayList<WaterTile> path;
	public AIGameInfoStorage(String gameName)
	{
		this.gameName = gameName;
	}
	public void processMessage(InGameMessage igm) {
		// TODO Auto-generated method stub
		
	}
}
