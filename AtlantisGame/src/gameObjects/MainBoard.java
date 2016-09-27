package gameObjects;


import java.util.Hashtable;


public class MainBoard {
	private final int numberOfTiles=100;
	// i chose hashtable and not hashmap cuz it doesn't accept nulls
	// so in our case here it makes more sense
	public Hashtable<Integer, WaterTile> initialBoard = new Hashtable<>();
	
	public  Hashtable<Integer, WaterTile> createBoard(){
		for (int i =0; i<numberOfTiles;i++){
			initialBoard.put(i, new WaterTile(i));
		}
		return initialBoard; 
	}
	
}
