package server.backend;

import java.util.ArrayList;

import gameObjects.WaterTile;

public class AIGameInfoStorage {
	private String gameName;
	private ArrayList<WaterTile> path;
	public AIGameInfoStorage(String gameName)
	{
		this.gameName = gameName;
	}
}
