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
public class LastBillMessage extends InGameMessage implements Serializable {
	
	int waterPassedCount;
	int waterBill;
	public LastBillMessage(String gameName, int waterPassedCount, int waterBill) {
		super(gameName);
		this.waterBill=waterBill;
		this.waterPassedCount=waterPassedCount;
	}
	public int getWaterPassedCount() {
		return waterPassedCount;
	}
	public int getWaterBill() {
		return waterBill;
	}

}
