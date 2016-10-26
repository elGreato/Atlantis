package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import gameObjects.ColorChoice;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;

public class DeckLandTileMessage extends InGameMessage implements Serializable {
	String gameName;
	ArrayList<LandTile> arrayA;
	ArrayList<LandTile> arrayB;
	public DeckLandTileMessage(String gameName, ArrayList<LandTile> arrayA, ArrayList<LandTile> arrayB) {
		super(gameName);
		this.gameName = gameName;
		this.arrayA=arrayA;
		this.arrayB=arrayB;
	}
	public ArrayList<LandTile> getArrayA() {
		return arrayA;
	}
	public void setArrayA(ArrayList<LandTile> arrayA) {
		this.arrayA = arrayA;
	}
	public ArrayList<LandTile> getArrayB() {
		return arrayB;
	}
	public void setArrayB(ArrayList<LandTile> arrayB) {
		this.arrayB= arrayB;
	}	

}