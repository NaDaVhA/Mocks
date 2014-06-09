package db;

import java.sql.SQLException;
import java.util.ArrayList;


public interface DBActionsInterface {

	
	/////////////////////////////
	// 		Music Database 	
	/////////////////////////////
	
	
	
	/**
	 * 
	 * @return [song_name, song_id]
	 * @throws SQLException
	 */
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
	
	
	int getArtistID(String artist_name) throws SQLException;
	
	
	
	/////////////////////////////
	// 		Users Database 	
	/////////////////////////////
	
	
	
	//adds
	public boolean addFreindToUser(int userID, int userFreindID) throws SQLException;
	
	public boolean addArtistToUser(String user_id, String artist_id) throws SQLException;
	
	public boolean addSongToUser(String user_name, String song_name,String artist_name) throws SQLException;
	
	public boolean setUserStatusSong(int userID, int songID , int artistID) throws SQLException;
	
	//delete
	
	public boolean removeFriendFromUser(int userID, int userFriendID) throws SQLException;
	
	public boolean removeSongFromUser(int userID, int songID) throws SQLException;
	
	
	
	/////////////////////////////
	//	Users - Getters	
	/////////////////////////////

	
	
	/**
	 * Returns all users.
	 * @return [user_id, user_name, status_song_id]
	 * @throws SQLException
	 */
	public ArrayList<String[]> getUsersList() throws SQLException;
			

	/**
	 * @param userID
	 * @return [user_name]
	 * @throws SQLException
	 */
	public String[] getUserName(int userID) throws SQLException;
		
	public int getUserId(String username) throws SQLException;
	
	public String getUserPassword(int userID) throws SQLException;
	
	/**
	 * 
	 * @param userID
	 * @return [user_name]
	 * @throws SQLException
	 */
	public ArrayList<String[]> getUserFreindsList(int userID) throws SQLException;
	
	
	
	/////////////////////////////
	// Database management	
	/////////////////////////////

	
	
	/**
	 * Checks whether the database is initialized or not.
	 * Returns true if initialized, false otherwise.
	 * @return
	 * @throws SQLException
	 */
	public boolean isDatabaseInitialized() throws SQLException;
	
	
	/**
	 * Initializes the database. 
	 * Builds the database from scratch or starts from the last successful point.
	 * @param yagoFilesPath
	 * @return
	 * @throws SQLException
	 */
	public boolean initializeDatabase(String yagoFilesPath) throws SQLException;

	
	/**
	 * Builds the music database.
	 * @param connection
	 * @param yagoFilesPath - path to the Yago files.
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException 
	 */
	public boolean buildMusicDB(String yagoFilesPath) throws SQLException;
	
	
	/**
	 * Updates the music database.
	 * @param connection
	 * @param yagoFilesPath - path to the Yago files.
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException 
	 */
	public boolean updateMusicDB(String yagoFilesPath) throws SQLException;
	
	
	/**
	 * Terminates the connection to the database, including closing all open connections from 
	 * the connection pool.
	 */
	public void terminateConnectionToDB();


	
}
