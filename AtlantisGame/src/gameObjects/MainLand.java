package gameObjects;

import java.io.Serializable;
import javafx.scene.layout.FlowPane;

public class MainLand  extends FlowPane implements Serializable{
	
	private final int  mainLandid=999;

	
	public MainLand(){
	
	}
	public int getMainLandid() {
		return mainLandid;
	}

}
