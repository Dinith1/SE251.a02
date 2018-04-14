package tbs.server;

import java.util.List;

public class TBSArtist {
	private String artistID;
	private String name;
	
	TBSArtist(String name) {
		this.name = name.toLowerCase();
		this.artistID = "_ARTIST_" + name; 	
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
	public String getID() {
		return this.artistID;
	}
	
	
	public int checkArtistNameError(List<TBSArtist> artistList) {
		if (this.name.equals("") || this.name.equals(null)) {
			return 4;
		}
		
		for (TBSArtist tempArtist : artistList) {
			if (this.name.toLowerCase().equals(tempArtist.getName())) {
				return 5;
			}
		}
		
		return 0;
	}

}
