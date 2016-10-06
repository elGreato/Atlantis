package gameObjects;

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

public class WaterTile extends Tile {
	Rectangle water = new Rectangle(75, 75);
	int waterId;

	public WaterTile(final int tileId) {
		super(tileId);
		waterId=tileId;

		water.setFill(Color.TRANSPARENT);
		water.setStroke(Color.BLACK);
		
		this.getChildren().add(water);

	 

//	@Override
	
//	public void dosmthin() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				water.setStroke(Color.BLACK);
				e.consume(); 
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
