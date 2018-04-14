package tbs.server;

import java.util.ArrayList;
import java.util.List;

public class TBSPerformance {
	private String performanceID;
	private String actID;
	private String theatreID;
	private String startTime;
	private String premiumSeatPrice;
	private String cheapSeatPrice;
	
	TBSPerformance(String actID, String theatreID, String startTime, String premiumPrice, String cheapPrice) {
		this.actID = actID;
		this.theatreID = theatreID;
		this.startTime = startTime;
		this.premiumSeatPrice = premiumPrice;
		this.cheapSeatPrice = cheapPrice;
		this.performanceID = "_PERFORMANCE_" + startTime + actID;
	}
	
	
	public String getID() {
		return this.performanceID;
	}
	
	
	public String getActID() {
		return this.actID;
	}
	
	
	public String getTheatreID() {
		return this.theatreID;
	}
	
	
	public String getStartTime() {
		return this.startTime;
	}
	
	
	public int getPremiumPrice() {
		return Integer.parseInt(this.premiumSeatPrice.substring(1, this.premiumSeatPrice.length()));
	}
	
	
	public int getCheapPrice() {
		return Integer.parseInt(this.cheapSeatPrice.substring(1, this.cheapSeatPrice.length()));
	}
	
	
	public int checkActIDError(List<TBSAct> actList) {
		if (this.actID.equals("") || this.actID.equals(null)) {
			return 10;
		}
		
		for (TBSAct tempAct : actList) {
			if (this.actID.equals(tempAct.getID())) {
				return 0;
			}
		}
		return 11;
	}
	
	
	public int checkTheatreIDError(List<TBSTheatre> theatreList) {
		if (this.theatreID.equals("") || this.theatreID.equals(null)) {
			return 12;
		}
		
		for (TBSTheatre tempTheatre : theatreList) {
			if (this.theatreID.equals(tempTheatre.getID())) {
				return 0;
			}
		}
		
		return 13;
	}
	

	public int checkStartTimeError() {
		if (this.startTime.equals("") || this.startTime.equals(null)) {
			return 14;
		}
		else if (!this.startTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
			return 15;
		}
		
		return 0;
	}
	
	
	public int checkPremiumPriceError() {
		if (this.premiumSeatPrice.equals("") || this.premiumSeatPrice.equals(null)) {
			return 16;
		}
		else if (!this.premiumSeatPrice.matches("\\$\\d+")) {
			return 17;
		}
		else if (Integer.parseInt(this.premiumSeatPrice.substring(1, this.premiumSeatPrice.length())) < 0) {
			return 18;
		}
		
		return 0;
	}
	
	
	public int checkCheapPriceError() {
		if (this.cheapSeatPrice.equals("") || this.cheapSeatPrice.equals(null)) {
			return 19;
		}
		else if (!this.cheapSeatPrice.matches("\\$\\d+")) {
			return 20;
		}
		else if (Integer.parseInt(this.cheapSeatPrice.substring(1, this.cheapSeatPrice.length())) < 0) {
			return 21;
		}
		
		return 0;
	}

	
	public List<String> createSalesReport(List<TBSTheatre> theatreList, List<TBSPerformance> performanceList, List<TBSTicket> ticketList) {
		List<String> salesReport = new ArrayList<String>();
		
		// Check which performances belong to actID
		for (TBSPerformance tempPerformance : performanceList) {
			if (this.actID.equals(tempPerformance.getActID())) {
				TBSTicket checkTicket = new TBSTicket(tempPerformance.getID(), 5, 5);
				int seatingDimension = checkTicket.findSeatingDimension(performanceList, theatreList);
				
				// Check which tickets belong to the performance and find the total number of tickets/income
				int numTickets = 0;
				int income = 0;
				for (TBSTicket tempTicket : ticketList) {
					if (tempPerformance.getID().equals(tempTicket.getPerformanceID())) {
						numTickets++;
						income += (tempTicket.getRowNumber() <= seatingDimension) ? tempPerformance.getPremiumPrice() : tempPerformance.getCheapPrice();
					}
				}
				salesReport.add(tempPerformance.getID() + "/t" + tempPerformance.getStartTime() + "\t" + numTickets + "\t" + income);
			}
		}
		return salesReport;
	}
}
