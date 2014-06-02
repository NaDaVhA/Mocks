package recommender.entites;



public class Song {

	private String name;
	private String artistName;
	private String id;
	
	
	public Song(String id, String name, String artistName){
		this.name = name;
		this.id = id;
		this.artistName = artistName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean equals(Object obj){
		
		if (!(obj instanceof Song))
			return false;
		
		Song other = (Song)obj;
		return other.id.toLowerCase().equals(this.id.toLowerCase());
	}

	public String getArtistName() {
		return artistName;
	}

	
}
