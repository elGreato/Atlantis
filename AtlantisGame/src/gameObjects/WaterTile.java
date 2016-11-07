package gameObjects;


import java.io.Serializable;

import javafx.scene.image.Image;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class WaterTile extends Tile implements Serializable{
	

	int col;
	int row;

	public WaterTile(int tileId) {
		super(tileId);

	/*	water.setFill(Color.TRANSPARENT);
		water.setStroke(Color.BLACK);

		this.getChildren().add(water);*/

	}

	public boolean hasPawn() {
		return false;

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
