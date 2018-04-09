package tbs.server;

public class TBSArtist {
	private String artistID;
	private String name;
	
	TBSArtist(String name) {
		this.name = name.toLowerCase();
		this.artistID = "ARTIST_" + name.toUpperCase(); 
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getID() {
		return this.artistID;
	}

}
