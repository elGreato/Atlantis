package client.game;





import java.io.Serializable;

import gameObjects.ColorChoice;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LandTileClient extends TileClient  {
	
	private PawnClient pawnOnTile;
	private ColorChoice landTileColor;
	private int landValue;
	private int col;
	private int row;
	private transient Rectangle rec;
	
	public LandTileClient(int tileId,ColorChoice color, int value ){
		super(tileId);
	landTileColor=color;
	landValue=value;
	rec = new Rectangle();
	rec.setWidth(62.00f);
	rec.setHeight(62.00f);
	rec.setFill(setTileColor(this));
	
	
	Text txtValue = new Text();
	txtValue.setText(String.valueOf(landValue)+"\n"+landTileColor.toString());
	getChildren().add(rec);
	getChildren().add(txtValue);
	
	}
	private Paint setTileColor(LandTileClient landTile) {
		
			if (landTile.getColor().toString().equalsIgnoreCase("blue")) 
				return (Color.BLUE);
			else if (landTile.getColor().toString().equalsIgnoreCase("red")) 
				return (Color.RED);
			else if (landTile.getColor().toString().equalsIgnoreCase("gray")) 
				return(Color.GRAY);
			else if (landTile.getColor().toString().equalsIgnoreCase("yello")) 
				return(Color.YELLOW);
			else if (landTile.getColor().toString().equalsIgnoreCase("green")) 
				return(Color.GREEN);
			else if (landTile.getColor().toString().equalsIgnoreCase("purple")) 
				return(Color.PURPLE);
			else if (landTile.getColor().toString().equalsIgnoreCase("brown")) 
				return(Color.BROWN);
			return Color.ORANGE;

	
	}
	public LandTileClient( int tileId,int co, int ro, ColorChoice color, int value) {
		super(tileId,co, ro);
		landTileColor=color;
		landValue=value;
		col=co;
		row=ro;
		

	}
	public PawnClient getPawnOnTile() {
		return pawnOnTile;
	}
	public void setPawnOnTile(PawnClient pawnOnTile) {
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
	
	public PawnClient getPawn(){
		return pawnOnTile;
	}
	
	
}
