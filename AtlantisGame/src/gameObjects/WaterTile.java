package gameObjects;

import java.util.ArrayList;
/**
* <h1>Water</h1>
* an extended class of tile, water can't holds pawn
* 
* @author  Ali Habbabeh
* @version 1.2
* @since   2016-12-22
*/
public class WaterTile extends Tile {

	ArrayList<LandTile> childrenTiles = new ArrayList<>();

	public WaterTile(int tileId) {
		super(tileId);

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
// this is to handle the problem of javafx with serializable 
	public void convertToChildren() {
		if (this.getChildrenTiles() != null) {
			for (LandTile land : this.getChildrenTiles()) {
				this.getChildren().add(land);
			}
		} else System.out.println("this water has no landtiles above it");
	}

}
