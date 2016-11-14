package server.backend;

import java.util.ArrayList;

import gameObjects.GameInterface;
import gameObjects.Player;
import gameObjects.WaterTile;
import messageObjects.DeckLandTileMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;

public class GameAI {
	private GameInterface game;
	private ArrayList<WaterTile> path;
	private ArrayList<PlayerAI> opponentInfo;
	public PlayerAI me;
	public String getGameName() {
		return game.getName();
	}
	
	public GameAI(GameInterface game)
	{
		this.game = game;
	}
	public void processMessage(InGameMessage igm) {
		if(igm instanceof WaterMessage)
		{
			path = ((WaterMessage)igm).getBase();
		}
		else if(igm instanceof PlayerMessage)
		{
		}
		else if(igm instanceof OpponentMessage)
		{
			opponentInfo = new ArrayList<PlayerAI>();
			for(Player p:((OpponentMessage)igm).getOpponents())
			{
			}
		}
		
	}
}
