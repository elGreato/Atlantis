package gameObjects;





import gameObjects.ColorChoice;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LandTile extends Tile {
	
	private Pawn pawnOnTile;
	private ColorChoice landTileColor;
	private int landValue;
	private int col;
	private int row;
	private Rectangle rec;
	
	public LandTile(int tileId,ColorChoice color, int value ){
		super(tileId);
	landTileColor=color;
	landValue=value;
	rec = new Rectangle();
	rec.setWidth(62.00f);
	rec.setHeight(62.00f);
	rec.setFill(Color.ORANGE);
	getChildren().add(rec);
	
	}
	public LandTile( int tileId,int co, int ro, ColorChoice color, int value) {
		super(tileId,co, ro);
		landTileColor=color;
		landValue=value;
		col=co;
		row=ro;
		

	}
	public Pawn getPawnOnTile() {
		return pawnOnTile;
	}
	public void setPawnOnTile(Pawn pawnOnTile) {
		this.pawnOnTile = pawnOnTile;
	}
	public ColorChoice getColor() {
		return landTileColor;
	}
	public void setLandTileColor(ColorChoice landTileColor) {
		this.landTileColor = landTileColor;
	}
	public int getLandValue() {
		return landValue;
	}
	public void setLandValue(int landValue) {
		this.landValue = landValue;
	}
	public boolean hasPawn(){
		return (pawnOnTile!=null);
	
	}
	
	public Pawn getPawn(){
		return pawnOnTile;
	}
	
	
}
