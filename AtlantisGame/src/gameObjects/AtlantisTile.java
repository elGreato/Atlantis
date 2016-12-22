package gameObjects;


import java.io.Serializable;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
/**
* <h1> Atlantis Tile</h1>
* 	just the atlantis tile
* 
* @author  Ali Habbabeh
* @version 1.2
* @since   2016-12-22
*/

public class AtlantisTile extends FlowPane implements Serializable{

	private  final int atlantisId=0;


	public AtlantisTile(  ) {

	}

	public int getAtlantisId() {
		return atlantisId;
	}



}
