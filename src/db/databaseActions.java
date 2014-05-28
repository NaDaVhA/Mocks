package db;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import core.User;


public interface databaseActions {

	public Collection<String> getSongsList();
	
	public Collection<String> getSongsArtistList();
	
	public Collection<String> getBandsList();

	public List<String> getSingersList();



	
	
	
	//Users DB
	
	public List<String> getUsersList();
		
	public String getUserName(int userID);
	
	public int getUserId(String username);
	
	public boolean usernameExists(String username);
	
	public String getUserPassword(int userID);
	
	public boolean disconnectUser(int userID);
	
	public List<String> getUserSongList(int userID);
	
	public String getUserStatusSong(int userID);
	
	
	
	
	
	//connectiion


	//Users DB
	public boolean registerNewUser(String username, String password);
	
	public boolean authenticateUser(String username, String password);
	
	public boolean connectUser(int userID);
	
	public boolean setUserStatusSong(int userID, int songID);
	
	public boolean addArtistToUser(String user_id, String artist_id);
	
	public boolean addSongToUser(String user_id, String song_id);
	
}
