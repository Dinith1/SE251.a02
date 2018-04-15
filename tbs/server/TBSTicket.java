package tbs.server;

import java.util.ArrayList;
import java.util.List;

public class TBSTicket {
	private String ticketID;
	private String performanceID;
	int rowNumber;
	int seatNumber;
	
	TBSTicket(String performanceID, int rowNumber, int seatNumber) {
		this.performanceID = performanceID;
		this.rowNumber = rowNumber;
		this.seatNumber = seatNumber;
		this.ticketID = "_TICKET_" + Integer.toString(rowNumber)+ "," + Integer.toString(seatNumber) + performanceID;
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
	
	
	public int checkPerformanceIDError(List<TBSPerformance> performanceList) {
		if (this.performanceID.equals("") || this.performanceID.equals(null)) {
			return 22;
		}
		
		for (TBSPerformance tempPerformance : performanceList) {
			if (this.performanceID.equals(tempPerformance.getID())) {
				return 0;
			}
		}
		
		return 23;
	}

	
	public int findSeatingDimension(List<TBSPerformance> performanceList, List<TBSTheatre> theatreList) {
		for (TBSPerformance tempPerformance : performanceList) {
			if (this.performanceID.equals(tempPerformance.getID())) {
				for (TBSTheatre tempTheatre : theatreList) {
					if (tempPerformance.getTheatreID().equals(tempTheatre.getID())) {
						return tempTheatre.getSeatingDimensions();
					}
				}
			}
		}
		// This line is impossible to reach given the way TBSServerImpl is written
		return 0;
	}
	
	
	public List<String> findSeatsAvailable(List<TBSTicket> ticketList, List<TBSPerformance> performanceList, List<TBSTheatre> theatreList) {
		List<String> seatsAvailable = new ArrayList<String>();
		int seatingDimension = this.findSeatingDimension(performanceList, theatreList);
		
		for (int row = 1; row <= seatingDimension; row++) {
			for (int seat = 1; seat <= seatingDimension; seat++) {
				boolean seatAvailable = true;
				
				for (TBSTicket tempTicket : ticketList) {
					seatAvailable = ((row == tempTicket.getRowNumber()) && (seat == tempTicket.getSeatNumber())) ? false : seatAvailable;
				}
				if (seatAvailable) {
					seatsAvailable.add(Integer.toString(row) + "\t" + Integer.toString(seat));
				}
			}
		}
		return seatsAvailable;
	}
	
}
