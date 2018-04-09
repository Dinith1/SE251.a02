package tbs.server;

public class TBSAct {
	private String actID;
	private String title;
	private int duration;
	private String artistID;
	
	TBSAct(String title, String artistID, int minsDuration) {
		this.title = title;
		this.artistID = artistID;
		this.duration = minsDuration;
		this.actID = artistID + "_ACT_" + title;
	}
	
	public String getID() {
		return this.actID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public String getArtistID() {
		return this.artistID;
	}

}
