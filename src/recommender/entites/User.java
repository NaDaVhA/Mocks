package recommender.entites;

import java.util.LinkedList;

public class User {

	private String id;
	private String username;
	private LinkedList<Song> listensTo;
	private LinkedList<User> friends;
	
	
	public User(String id, String username){
		this.id = id;
		this.username = username;
		this.listensTo = new LinkedList<Song>();
		this.friends = new LinkedList<User>();
	}
	
	public String getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public LinkedList<Song> getListensTo() {
		return listensTo;
	}
		
	public LinkedList<User> getFriends() {
		return friends;
	}
	
	public void addSong(Song s){
		
		if (!isListenedToSong(s))
		{
			if (listensTo == null)
				listensTo = new LinkedList<Song>();
			
			listensTo.add(s);
		}
	}
	
	public boolean isListenedToSong(Song s){
		
		for (Song currentSong : listensTo)
		{
			if (currentSong.equals(s))
				return true;
		}
		
		return false;
	}
	
	public boolean equals(Object obj){
		
		if (!(obj instanceof User))
			return false;
		
		User other = (User)obj;
		return other.id.toLowerCase().equals(this.id.toLowerCase());
	}

	public void addFriend(User u){		
		if (!isFriendWith(u))
		{
			friends.add(u);
			u.addFriend(this);
		}
	}
	
	public boolean isFriendWith(User u){
		for (User currentUser : friends)
		{
			if (currentUser.equals(u))
				return true;
		}
		
		return false;
	}
	
}
