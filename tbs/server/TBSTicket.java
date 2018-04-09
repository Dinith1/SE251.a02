package tbs.server;

public class TBSTicket {
	private String ticketID;
	private String performanceID;
	int rowNumber;
	int seatNumber;
	
	TBSTicket(String performanceID, int rowNumber, int seatNumber) {
		this.performanceID = performanceID;
		this.rowNumber = rowNumber;
		this.seatNumber = seatNumber;
		this.ticketID = performanceID + "_TICKET_" + Integer.toString(rowNumber)+ "," + Integer.toString(seatNumber);
	}
	
	
	public String getID() {
		return this.ticketID;
	}
	
	
	public String getPerformanceID() {
		return this.performanceID;
	}
	
	
	public int getRowNumber() {
		return this.rowNumber;
	}
	
	
	public int getSeatNumber() {
		return this.seatNumber;
	}

}
