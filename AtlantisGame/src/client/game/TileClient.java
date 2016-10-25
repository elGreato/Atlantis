package client.game;

import java.io.Serializable;

import javafx.scene.layout.StackPane;

public abstract class TileClient extends StackPane implements Serializable {
	// protected to prevent mutation (create multiple instances of the same object )
	//which i read that it's good for garbage collection
	private  int tileId;
	private int col;
	private int row;
	private PawnClient pawn;
	
	//constructor
	public TileClient(int tileId){
		super();
		this.tileId=tileId;
		
	}
	// another constructor with locatin 
	public TileClient(int tileId, int col, int row){
		super();
		this.tileId=tileId;
		this.col=col;
		this.row=row;
		
	}
	
	public int getTileId() {
		return tileId;
	}



	public boolean hasPawn(){
		return pawn !=null;
	}
	
	public PawnClient getPawn(){
		return pawn;
	}
	
	public void setPawn(PawnClient pawn){
		this.pawn=pawn;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	

	
}
