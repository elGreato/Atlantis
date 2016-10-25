package client.game;

import gameObjects.ColorChoice;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class PawnClient extends StackPane {
	private ColorChoice pawnColor;
	private int location;
	private PlayerClient owner;
	Circle cir ;
	
	public PawnClient(PlayerClient player){
		super();
		owner=player;
		pawnColor=player.getColor();
	
		cir = new Circle();
		cir.setRadius(10.0f);
		cir.setFill(setPawnColor(owner));
		
		getChildren().add(cir);
		
	}

	private Color setPawnColor(PlayerClient c ) {
		if (c.getColor().toString().equalsIgnoreCase("blue")) 
			return (Color.BLUE);
		else if (c.getColor().toString().equalsIgnoreCase("red")) 
			return (Color.RED);
		else if (c.getColor().toString().equalsIgnoreCase("gray")) 
			return(Color.GRAY);
		else if (c.getColor().toString().equalsIgnoreCase("yello")) 
			return(Color.YELLOW);
		else if (c.getColor().toString().equalsIgnoreCase("green")) 
			return(Color.GREEN);
		else if (c.getColor().toString().equalsIgnoreCase("purple")) 
			return(Color.PURPLE);
		else if (c.getColor().toString().equalsIgnoreCase("brown")) 
			return(Color.BROWN);
		return Color.ORANGE;
		
			
		
	}

	public ColorChoice getPawnColor() {
		return pawnColor;
	}

	

	public PlayerClient getOwner() {
		return owner;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	
	
	

}
