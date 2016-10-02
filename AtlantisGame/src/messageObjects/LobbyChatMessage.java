package messageObjects;

import java.io.Serializable;

public class LobbyChatMessage extends LobbyMessage implements Serializable {

	private String message;
	
	public LobbyChatMessage(String message) {
		
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
