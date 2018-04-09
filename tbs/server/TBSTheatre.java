package tbs.server;

public class TBSTheatre {
	private String theatreID;
	private int seatDimension;
	private int floorArea;
	
	TBSTheatre(String theatreID, String seatDimension, String floorArea) {
		this.theatreID = theatreID;
		this.seatDimension = Integer.parseInt(seatDimension);
		this.floorArea = Integer.parseInt(floorArea);
	}
	
	
	public String getID() {
		return this.theatreID;
	}
	
	public int getSeatingDimensions() {
		return seatDimension;
	}
	
	public int getFloorArea() {
		return floorArea;
	}

}
