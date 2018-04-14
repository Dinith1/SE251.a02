package tbs.server;

import java.util.List;

public class TBSAct {
	private String actID;
	private String title;
	private int duration;
	private String artistID;
	
	TBSAct(String title, String artistID, int minsDuration) {
		this.title = title;
		this.artistID = artistID;
		this.duration = minsDuration;
		this.actID = "_ACT_" + title + artistID;
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
	
	
	public int checkArtistIDError(List<TBSArtist> artistList) {
		if (this.artistID.equals("") || this.artistID.equals(null)) {
			return 6;
		}

		for (TBSArtist tempArtist : artistList) {
			if (this.artistID.equals(tempArtist.getID())) {
				return 0;
			}
		}
		
		return 7;
	}
	
}
