package messageObjects.turnMessages;

import java.io.Serializable;

import messageObjects.InGameMessage;

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
