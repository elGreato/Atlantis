package messageObjects;

import java.io.Serializable;

public class TurnMessage extends InGameMessage implements Serializable {

	private boolean yourTurn;
	
	//reply from server to client if it is his turn
		public TurnMessage (String gameName, boolean yourTurn){
			super(gameName);
			this.yourTurn=yourTurn;
		}

		public boolean isYourTurn() {
			return yourTurn;
		}

		public void setYourTurn(boolean yourTurn) {
			this.yourTurn = yourTurn;
		}
	
	
}
