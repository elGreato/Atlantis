package gameObjects;

import java.io.Serializable;


import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WaterTile extends Tile  {
	transient Rectangle  water = new Rectangle(50, 50);

	int col;
	int row;
	
	public WaterTile(int tileId){
		super(tileId);
	}

	public WaterTile( int tileId, int co, int ro) {
		super(tileId,co,ro);
		col=co;
		row=ro;
		water.setFill(Color.TRANSPARENT);
		water.setStroke(Color.BLACK);
		
		this.getChildren().add(water);

		final Image im = new Image(getClass().getResourceAsStream("images4Tiles/water.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

//	@Override
	
//	public void dosmthin() {
		WaterTile wt = this;
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		
			@Override
			public void handle(MouseEvent e) {
				water.setStroke(Color.ORANGE);
				System.out.println(((WaterTile)e.getSource()).getTileId());
				
					int i = ((WaterTile) e.getSource()).getCol();
					int k = ((WaterTile) e.getSource()).getRow();
					wt.getChildren().remove(1);
				
				
				e.consume(); 
			}

		});}
	
	//}
	public boolean hasPawn() {
		return false;

	}
	public void addStack(LandTile tile){
		this.getChildren().add(tile);
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
