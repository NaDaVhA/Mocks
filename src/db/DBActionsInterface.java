package db;

import java.sql.SQLException;
import java.util.ArrayList;


public interface DBActionsInterface {

	
	
	/////////////////////////////
	// 		Music Database 	
	/////////////////////////////
	
	/**
	 * general queries on db
	 */
	
	
	
	/**
	 * search songsArtistList in db
	 * @param
	 * @return ArrayList<String[song_name, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain user_songs , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getSongsArtistList() throws SQLException;
	
	
	/**
	 * search songsList in db
	 * @param
	 * @return ArrayList<String[song_name, song_id]>,
	 * @return ArrayList.size()== 0 if db does not contain user_songs , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getSongsList() throws SQLException;

	/**
	 * search BandsList in db
	 * @param
	 * @return ArrayList<String[artist_id, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain user_bands , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getBandsList() throws SQLException;

	/**
	 * search SingersList in db
	 * @param
	 * @return ArrayList<String[artist_id, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain user_singers , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getSingersList() throws SQLException;
	
	
	/**
	 * search ID in db
	 */
	
	/**
	 * search ID of a the song_name
	 * @param song_name
	 * @return int song_id, 0 if db does not contain song_name string, -1 if there is a problem in db execution
	 * @throws SQLException
	 */
	public int getSongID(String song_name) throws SQLException;
	
	
	/**
	 * search the ID of the artist_name
	 * @param artist_name
	 * @return int artist_id, 0 if db does not contain artist_name string, -1 if there is a problem in db execution
	 * @throws SQLException
	 */
	public int getArtistID(String artist_name) throws SQLException;


	/**
	 *  search the ID of the username
	 * @param username
	 * @return int artist_id, 0 if db does not contain username string, -1 if there is a problem in db execution
	 * @throws SQLException
	 */
	public int getUserId(String username) throws SQLException;

	/**
	 * general searches in db
	 */

	
	/**
	 * search friends name that contains the letters of Freindname
	 * @param Freindname
	 * @return ArrayList<String[user_name]>
	 * @return ArrayList.size()== 0 if db does not contain Freindname string , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getFindFreind(String Freindname) throws SQLException;
	
	/**
	 * search artist_name and all of his songs
	 * @param artist_name
	 * @return ArrayList<String[song_name, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain artist_name in artists , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getArtistList(String artist_name) throws SQLException;
	
	/**
	 * search song_name and all of his artists
	 * @param song_name
	 * @return ArrayList<String[song_name, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain song_name in songs , null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getSongsArtistList(String songname) throws SQLException;
	
	
/////////////////////////////
// 		Users Database 	
/////////////////////////////
	
	/**
	 * register in db a new user
	 * @param username,password
	 * @return boolean
	 * @return false if there is a problem in db execution, else true
	 * @throws SQLException
	 */
	public boolean registerNewUser(String username, String password) throws SQLException;
	
	/**
	 * check if there is username and password in db which are connected to each other
	 * @param username,password
	 * @return boolean
	 * @return false if there is a problem in db execution or there is not a matching, else true
	 * @throws SQLException
	 */
	public boolean authenticateUser(String username, String password) throws SQLException;	
	
	/**
	 * check if username is already exist
	 * @param username
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain username, else true
	 * @throws SQLException
	 */
	public boolean usernameExists(String username) throws SQLException;
	
	
	/**
	 * search all user's songs
	 * @param userID
	 * @return ArrayList<String[song_name, artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain songs of userID, null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getUserSongList(int userID) throws SQLException;
	
	/**
	 * search all user's artists
	 * @param userID
	 * @return ArrayList<String[artist_name]>
	 * @return ArrayList.size()== 0 if db does not contain artist of userID, null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getUserArtistList(int userID) throws SQLException;
	
	
	/**
	 * search user's status song
	 * @param userID
	 * @return ArrayList<String[song_name, artist_name]>
	 * @return ArrayList<String["",""]> if db does not contain a status song of a userID, null if there is a problem in db execution
	 * @throws SQLException
	 */
	public String[] getUserStatusSong(int userID) throws SQLException;
	
	
	
	/**
	 * add a friend to user
	 * @param userFreindID
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain usernames, else true
	 * @throws SQLException
	 */
	public boolean addFreindToUser(int userID, int userFreindID) throws SQLException;
	
	/**
	 * add an artist to user
	 * @param user_id, artist_id
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain users _ID, else true
	 * @throws SQLException
	 */
	public boolean addArtistToUser(String user_id, String artist_id) throws SQLException;
	
	/**
	 * add a song to user
	 * @param user_name,song_name,artist_name
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain (user_names,artist_name,song_name), else true
	 * @throws SQLException
	 */
	public boolean addSongToUser(String user_name, String song_name,String artist_name) throws SQLException;
	
	/**
	 * set user a status song 
	 * @param userID, songID, artistID
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain (userID,artistID,songID), else true
	 * @throws SQLException
	 */
	public boolean setUserStatusSong(int userID, int songID , int artistID) throws SQLException;
	

	/**
	 * delete a friend from user_friend list
	 * @param userID, userFriendID
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain users_ID, else true
	 * @throws SQLException
	 */
	public boolean removeFriendFromUser(int userID, int userFriendID) throws SQLException;
	
	/**
	 * delete a song from user_song list
	 * @param userID,songID
	 * @return boolean
	 * @return false if there is a problem in db execution or db does not contain users_ID, else true
	 * @throws SQLException
	 */
	public boolean removeSongFromUser(int userID, int songID) throws SQLException;
	
	
	
	/**
	 * insert a massage text to db between sender and receiver  
	 * @param username_send_id
	 * @param username_receive_id
	 * @param msg
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException
	 */
		public Boolean send_massage(int username_send_id, int username_receive_id,
				String msg) throws SQLException;

	
	/**
	 * get history massages between two users
	 * @param username_send_id
	 * @param username_receive_id
	 * @param sender_name
	 * @param receiver_name
	 * @return ArrayList<String[user_sender, user_receiver, massage]>
	 * @return ArrayList.size()== 0 if db does not contain history massage, null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> get_history_massage(int username_send_id,
			int username_receive_id,String sender_name, String receiver_name ) throws SQLException;


	/**
	 * search for all users in db
	 * @param
	 * @return ArrayList<String[user_id, user_name, status_song_id]>
	 * @return ArrayList.size()== 0 if db does not contain users, null if there is a problem in db execution
	 * @throws SQLException
	 */
	public ArrayList<String[]> getUsersList() throws SQLException;
			

	/**
	 * search a user_name that match to userID
	 * @param userID
	 * @return String[user_name], String[null] if db does not contain userID null if there is a problem in db execution
	 * @throws SQLException
	 */
	public String[] getUserName(int userID) throws SQLException;
	
	/**
	 * search a password that match to userID
	 * @param userID
	 * @return password, null if there is a problem in db execution or if db does not contain userID
	 * @throws SQLException
	 */
	public String getUserPassword(int userID) throws SQLException;
	
	/**
	 * search user's friends
	 * @param userID
	 * @return ArrayList<String[user_names]>
	 * @return ArrayList.size()== 0 if db does not contain friends of userID, null if there is a problem in db execution
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
