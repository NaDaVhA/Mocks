package db;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import core.User;


public interface databaseActions {

	
	//Database Getters

	//Music DB
	public Collection<String> getBandsList();


	public LinkedHashMap<String, String> getSingersList();


	public Collection<String> getSongsList();
	
	
	
	//Users DB
	
	public Collection<User> getUsersList();
		
	public String getUserName(int userID);
	
	public int getUserId(String username);
	
	public boolean usernameExists(String username);
	
	public String getUserPassword(int userID);
	
	public boolean disconnectUser(int userID);
	
	public LinkedHashMap<Integer, String> getUserSongList(int userID);
	
	public String getUserStatusSong(int userID);
	
	
	
	
	
	//Database Modifiers
	
	//Music DB
	public boolean addSongToDB();
	
	public boolean addArtistToDB();
	

	//Users DB
	public boolean registerNewUser(String username, String password);
	
	public boolean authenticateUser(String username, String password);
	
	public boolean connectUser(int userID);
	
	public boolean setUserStatusSong(int userID, int songID);
	
	public String addSongToUserSongList(int userID);
	
}
