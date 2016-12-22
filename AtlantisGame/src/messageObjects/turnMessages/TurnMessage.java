package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;
/**
 * <h1>message</h1> 
 * this is a class that represent an object sent from server to client or vice versa
 * they are all self explanatory 
 * 
 * 
 * @author Ali Habbabeh
 * @version 1.2
 * @since 2016-12-22
 */
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
