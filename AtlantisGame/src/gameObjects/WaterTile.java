package gameObjects;

import java.util.ArrayList;

public class WaterTile extends Tile {

	int col;
	int row;
	ArrayList<LandTile> childrenTiles = new ArrayList<>();

	public WaterTile(int tileId) {
		super(tileId);

		/*
		 * water.setFill(Color.TRANSPARENT); water.setStroke(Color.BLACK);
		 * 
		 * this.getChildren().add(water);
		 */
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

	public ArrayList<LandTile> getChildrenTiles() {
		return childrenTiles;
	}

	public void setChildrenTiles(ArrayList<LandTile> childrenTiles) {
		this.childrenTiles = childrenTiles;
	}

	public void addLand(LandTile landtile) {
		childrenTiles.add(landtile);
	}

	public void convertToChildren() {
		if (this.getChildrenTiles() != null) {
			for (LandTile land : this.getChildrenTiles()) {
				this.getChildren().add(land);
			}
		} else System.out.println("this water has no landtiles above it");
	}

}
