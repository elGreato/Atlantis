package client.game;



import javafx.event.EventHandler;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WaterTileClient extends Tile  {
	Rectangle water = new Rectangle(65, 65);
	int waterId;
	int col;
	int row;

	public WaterTileClient( int tileId, int co, int ro) {
		super(tileId,co,ro);
		waterId=tileId;
		col=co;
		row=ro;
		water.setFill(Color.TRANSPARENT);
		water.setStroke(Color.BLACK);
		
		this.getChildren().add(water);

		Image im = new Image(getClass().getResourceAsStream("images4Tiles/water.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));


	
//	public void dosmthin() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				water.setStroke(Color.ORANGE);
				e.consume(); 
			}

		});}
	public int getWaterId() {
		return waterId;
	}
	public void setWaterId(int id){
		waterId=id;
	}
	
	public Rectangle getWater() {
		return water;
	}
	public void setWater(Rectangle water) {
		this.water = water;
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
	//}
	public boolean hasPawn() {
		return false;

	}

}
