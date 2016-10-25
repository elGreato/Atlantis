package client.game;

import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class MainLandClient extends TileClient{
	
	private int mainLandid;
	private int col;
	private int row;
	
	public MainLandClient(int id, int co, int ro){
		super(id,co,ro);
		mainLandid=id;
		col=co;
		row=ro;
		
		Image im = new Image(getClass().getResourceAsStream("images4Tiles/mainLand.jpg")); 
		this.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		
	}
	
	

}
