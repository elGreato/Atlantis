package gameObjects;


import java.io.Serializable;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


public class AtlantisTile extends FlowPane implements Serializable{

	private  final int atlantisId=0;


	public AtlantisTile(  ) {

	}

	public int getAtlantisId() {
		return atlantisId;
	}



}
