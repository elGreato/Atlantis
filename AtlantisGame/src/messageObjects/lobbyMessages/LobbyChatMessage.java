package messageObjects.lobbyMessages;

import java.io.Serializable;
/**
* <h1>Message for chats</h1>
* Distributes chat messages from and to clients
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class LobbyChatMessage extends LobbyMessage implements Serializable {
	
	private String author;
	private String message;
	
	public LobbyChatMessage(String author, String message) {
		
		this.author = author;
		this.message = message;
	}
	public String getAuthor()	{
		return author;
	}
	public String getMessage() {
		return message;
	}
}
