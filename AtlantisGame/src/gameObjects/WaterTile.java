package gameObjects;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WaterTile extends Tile {
	Rectangle water = new Rectangle(50, 50);
	int waterId;

	public WaterTile(final int tileId) {
		super(tileId);
		waterId=tileId;

		water.setFill(Color.LIGHTBLUE);
		this.getChildren().add(water);

	

//	@Override
	
//	public void dosmthin() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				water.setFill(Color.BLUE);
				//e.consume(); 
			}

		});}
	public int getWaterId() {
		return waterId;
	}
	
	//}
	public boolean hasPawn() {
		return false;

	}

}
