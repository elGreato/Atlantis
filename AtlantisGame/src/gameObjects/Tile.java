package gameObjects;

import javafx.scene.layout.StackPane;

public abstract class Tile extends StackPane {
	// we set it final because we only set it once at construction time
	//and protected to prevent mutation (create multiple instances of the same object )
	//which i read that it's good for garbage collection
	protected final int tileId;
	
	private Pawn pawn;
	
	//constructor
	public Tile(int tileId){
		super();
		this.tileId=tileId;
		
	}
	
	public int getTileId() {
		return tileId;
	}



	public boolean hasPawn(){
		return pawn !=null;
	}
	
	public Pawn getPawn(){
		return pawn;
	}
	
	public void setPawn(Pawn pawn){
		this.pawn=pawn;
	}
	

	
}
