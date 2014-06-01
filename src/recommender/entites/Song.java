package recommender.entites;



public class Song {

	private String name;
	private String id;
	
	
	public Song(String id, String name){
		this.name = name;
		this.id = id;
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
	
}
