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
	
	
	
	
	
	
//	private class DeckLandTileProperties {

//		public DeckLandTileProperties(){
			
//		}
/*		public ArrayList<LandTile> shuffleTiles (DeckOfLandTiles d){
			Random r = new Random();
			for (int i=0; i<7;i++){
				int f=	r.nextInt(6);
				d.getDeckOfTiles().remove(i+6*f);	
			}
			 Collections.shuffle(d.getDeckOfTiles());
			
			System.out.println("WE SHUFFULED!!!");
			return d.getDeckOfTiles();
			
		}*/
//	}
}