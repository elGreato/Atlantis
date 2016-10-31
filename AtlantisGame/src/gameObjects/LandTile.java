package gameObjects;





import java.io.Serializable;

import gameObjects.ColorChoice;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LandTile extends Tile  {
	
	private Pawn pawnOnTile;
	private  ColorChoice landTileColor;
	private int landValue;
	private int col;
	private int row;
	private transient Rectangle rec;
	private transient Text txtValue;
	
	public LandTile(int tileId,ColorChoice color, int value ){
		super(tileId);
	landTileColor=color;
	landValue=value;
	rec = new Rectangle();
	rec.setWidth(48.00f);
	rec.setHeight(48.00f);
	rec.setFill(setTileColor(this));
	
	
	 txtValue = new Text();
	txtValue.setText(String.valueOf(landValue)+"\n"+landTileColor.toString());
	getChildren().add(rec);
	getChildren().add(txtValue);
	
	}
	public Text getTxtValue() {
		return txtValue;
	}
	public void setTxtValue(Text txtValue) {
		this.txtValue = txtValue;
	}
	private Paint setTileColor(LandTile landTile) {
		
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
