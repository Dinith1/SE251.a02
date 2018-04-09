package tbs.server;

public class TBSPerformance {
	private String performanceID;
	private String actID;
	private String theatreID;
	private String startTime;
	private int premiumSeatPrice;
	private int cheapSeatPrice;
	
	TBSPerformance(String actID, String theatreID, String startTime, int premiumPrice, int cheapPrice) {
		this.actID = actID;
		this.theatreID = theatreID;
		this.startTime = startTime;
		this.premiumSeatPrice = premiumPrice;
		this.cheapSeatPrice = cheapPrice;
		this.performanceID = actID + "_PERFORMANCE_" + startTime;
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
	

}
