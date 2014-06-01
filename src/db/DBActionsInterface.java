package db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import core.User;


public interface DBActionsInterface {

	//[song_name, song_id]
	public ArrayList<String[]> getSongsList();
	
	//[song_name, artist_name]
	public ArrayList<String[]> getSongsArtistList();
	
	//[artist_id, artist_name]
	public ArrayList<String[]> getBandsList();

	//[artist_id, artist_name]
	public ArrayList<String[]> getSingersList();
	
	public int getSongID(String song_name);
	
	public String getFindFreind(String Freindname);

	
	//Users DB
	public boolean registerNewUser(String username, String password);
	
	public boolean authenticateUser(String username, String password);
	
	public boolean connectUser(int userID);
	
	
	public boolean usernameExists(String username);
	
	public boolean disconnectUser(int userID);
	
	
	
	
	
	
	//[song_name, artist_name]
	public ArrayList<String[]> getUserSongList(int userID);
	
	//[artist_name]
	public ArrayList<String[]> getUserArtistList(int userID);
	
	//[song_name, artist_name]
	public String[] getUserStatusSong(int userID);
	
	

	//Users DB
	
	//adds
	public boolean addFreindToUser(int userID, int userFreindID);
	
	public boolean addArtistToUser(String user_id, String artist_id);
	
	public boolean addSongToUser(String user_id, String song_id);
	
	public boolean setUserStatusSong(int userID, int songID);
	
	//gets
	
	//[user_id, user_name, status_song_id]
	public ArrayList<String[]> getUsersList();
			
	//[user_name]
	public String[] getUserName(int userID);
		
	public int getUserId(String username);
	
	public String getUserPassword(int userID);
	
	//[user_name]
	public ArrayList<String[]> getUserFreindsList(int userID);
	
	
	
	
	//Database management	
	public boolean initializeDatabase(String yagoFilesPath);

	public boolean buildMusicDB(String yagoFilesPath);
	
	public boolean updateMusicDB(String yagoFilesPath);
	
}
