package db;

import java.sql.SQLException;
import java.util.ArrayList;


public interface DBActionsInterface {

	//[song_name, song_id]
	public ArrayList<String[]> getSongsList() throws SQLException;
	
	//[song_name, artist_name]
	public ArrayList<String[]> getSongsArtistList() throws SQLException;
	
	//[artist_id, artist_name]
	public ArrayList<String[]> getBandsList() throws SQLException;

	//[artist_id, artist_name]
	public ArrayList<String[]> getSingersList() throws SQLException;
	
	public int getSongID(String song_name) throws SQLException;
	
	
	//mira
	//[user_name]
	public ArrayList<String[]> getFindFreind(String Freindname) throws SQLException;
	
	//[song, artist]
	public ArrayList<String[]> getArtistList(String artist_name) throws SQLException;
	
	//[song, artist]
	public ArrayList<String[]> getSongsArtistList(String songname) throws SQLException;
//mira-
	
	//Users DB
	public boolean registerNewUser(String username, String password) throws SQLException;
	
	public boolean authenticateUser(String username, String password) throws SQLException;	
	
	public boolean usernameExists(String username) throws SQLException;
	
	public boolean disconnectUser(int userID) throws SQLException;
	
	
	
	
	
	
	//[song_name, artist_name]
	public ArrayList<String[]> getUserSongList(int userID) throws SQLException;
	
	//[artist_name]
	public ArrayList<String[]> getUserArtistList(int userID) throws SQLException;
	
	//[song_name, artist_name]
	public String[] getUserStatusSong(int userID) throws SQLException;
	
	

	//Users DB
	
	//adds
	public boolean addFreindToUser(int userID, int userFreindID) throws SQLException;
	
	public boolean addArtistToUser(String user_id, String artist_id) throws SQLException;
	
	//mira
	public boolean addSongToUser(String user_name, String song_name) throws SQLException;
	//mira-
	
	public boolean setUserStatusSong(int userID, int songID) throws SQLException;
	
	//gets
	
	
	
	//[user_id, user_name, status_song_id]
	public ArrayList<String[]> getUsersList() throws SQLException;
			
	//[user_name]
	public String[] getUserName(int userID) throws SQLException;
		
	public int getUserId(String username) throws SQLException;
	
	public String getUserPassword(int userID) throws SQLException;
	
	//[user_name]
	public ArrayList<String[]> getUserFreindsList(int userID) throws SQLException;
	
	
	
	//Database management	
	public boolean initializeDatabase(String yagoFilesPath) throws SQLException;

	public boolean buildMusicDB(String yagoFilesPath) throws SQLException;
	
	public boolean updateMusicDB(String yagoFilesPath) throws SQLException;
	
	public void terminateConnectionToDB();
	
}
