package messageObjects;

import java.io.Serializable;

public class LobbyChatMessage extends LobbyMessage implements Serializable {
	
	private String author;
	private String message;
	
	public LobbyChatMessage(String author, String message) {
		
		this.author = author;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
