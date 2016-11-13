package gameObjects;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.layout.FlowPane;

public class MainLand extends FlowPane implements Serializable {

	private final int mainLandid = 999;
	private ArrayList<Pawn> pawns = new ArrayList<>();

	public MainLand() {

	}

	public int getMainLandid() {
		return mainLandid;
	}

	public ArrayList<Pawn> getPawns() {
		return pawns;
	}

	public void setPawns(ArrayList<Pawn> pawns) {
		this.pawns = pawns;
	}

	public void convertToChildren() {
		if (pawns != null) {
			for (Pawn p : pawns) {
				if(!this.getChildren().contains(p))
				this.getChildren().add(p);
			}
		}
	}

}
