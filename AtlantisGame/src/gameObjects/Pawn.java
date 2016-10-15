package gameObjects;

import gameObjects.Card.ColorChoice;


public class Pawn {
	private ColorChoice pawnColor;
	
	private Player owner;
	
	public Pawn(Player player){
		owner=player;
		pawnColor=owner.getColor();
	}

	public ColorChoice getPawnColor() {
		return pawnColor;
	}

	

	public Player getOwner() {
		return owner;
	}

	
	
	

}
