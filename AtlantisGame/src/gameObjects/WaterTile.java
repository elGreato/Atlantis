package gameObjects;

import java.util.ArrayList;

public class WaterTile extends Tile {

	ArrayList<LandTile> childrenTiles = new ArrayList<>();

	public WaterTile(int tileId) {
		super(tileId);

	}

	public boolean hasPawn() {
		return false;
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
