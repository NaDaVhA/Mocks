package core;

import java.util.ArrayList;
import java.util.List;

import utilities.Pair;

public interface ApplicationInterface {
	
	/**
	 * all the functions return a pair the left element represent connection status
	 */
	
	/**
	 * Checks database status.
	 * @return true if database is initialized, false otherwise.
	 */
	public Pair<Integer, Boolean> checkInitializationStatus();
	
	
	/**
	 * Initializes the application.
	 * @return true if succeeded, false otherwise. 
	 * Special return value: int = -1 if connection error, -2 if Path Error. 
	 */
	public Pair<Integer, Boolean> initializeApplication();
	
	
	
	/**
	 * Terminates connection to database.
	 * Closes all open (and valid) connections from the connection pool.
	 */
	public void terminateDBConnection();
	
	
	/**
	 * Updates  the application's music database.
	 * @return true if succeeded, false otherwise.
	 * Special return value: int = -1 if connection error, -2 if Path Error. 
	 */
	public Pair<Integer, Boolean> updateMusicDatabase();
	
	
	/**
	 *  for log in screen initialize user
	 * @param username - string representing the username
	 * @param password - string representing the password
	 * @return true if its an authentic user
	 */
	public Pair<Integer, Boolean> isUserRegisterd(String username,String password);
	
	/**
	 * 
	 * for sign up screen
	 * @param username
	 * @return true iff username is already in use
	 */
	public Pair<Integer, Boolean> isUsernameTaken(String username);
	
	
	/**
	 * for sign up screen
	 * @param username
	 * @param password
	 * @return true iff signing up the user succeeded
	 */
	public Pair<Integer, Boolean> signUpUser(String username, String password);
	
	/**
	 * 
	 * @param username
	 * @return user status song
	 */
	public Pair<Integer, Pair<String, String>> getStatusSong(String username); 
	
	/**
	 * for main screen
	 * @param username
	 * @return the status song of the user
	 */
	public Pair<String,String> getStatusSong();
	
	
	
	/**
	 * 
	 * @param Pair<artist_name,song_name>
	 * @return true if the change succeeded
	 */
	public Pair<Integer, Boolean> changeStatusSong(Pair<String,String> song); //qaqa
	
	
	/**
	 * 
	 * 
	 * @param friendusername
	 * @return true if the change succeeded
	 */
	public Pair<Integer, Boolean> removeFriendFromUser(String friendusername); //qaqa
	
	/**
	 * 
	 * @param friendusername
	 * @return true if the change succeeded
	 */
	public Pair<Integer, Boolean> removeSongFromUser(String songname ,  String artistname); //qaqa

	
	
	/**
	 * for main screen
	 * @param username
	 * @return the song list of the user
	 */
	public Pair<Integer, List<Pair<String, String>>> getSongList(String username);
	
	/**
	 * 
	 * @param
	 * @return song list of current user
	 */
	public List<Pair<String,String>> getSongList();
	
	/**
	 * 
	 * @param 
	 * @return friends list of a current user
	 */
	public ArrayList<String> getFriendList();
	
	/**
	 * for main screen
	 * @param username
	 * @return friends list 
	 */
	public Pair<Integer, ArrayList<String>> getFriendList(String username);
	
	/**
	 * for main screen
	 * @param username
	 * @return true if signed out
	 */
	public boolean signOutUser(String username);
	
	/**
	 * for add a friend screen
	 * @param friend_name - name to search in the users db
	 * @return list of the search results 
	 */
	public Pair<Integer, ArrayList<String>> getSearchResultsFriends(String friend_name);
	
	/**
	 * for add a friend screen
	 * @param username
	 * @return true iff the friend was added to the friend list
	 */
	public Pair<Integer,Boolean> addFriend(String username);
	
	/**
	 * for add song screen
	 * @param artist_name - the artist name to search by
	 * @return list of the search results
	 */
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsByArtist(String artist_name);
	
	/**
	 * for add song screen
	 * @param song_name -- the song name to search by
	 * @return list of the search results
	 */
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsBySong(String song_name);
	
	/**
	 * 
	 * @param username
	 * @param song
	 * @return true iff the song was added to the user song list
	 */
	public Pair<Integer, Boolean> addSong(String song,String artist);


	/**
	 * 
	 * @return the user username
	 */
	
	public String getUsername();
		
	
	public void InitalizeRecommender();

	
	

	/**
	 * @param username_send
	 * @param username_receive
	 * @return false if it is not work
	 */
	public Pair<Integer, Boolean> sendMassage(String username_send,String username_receive,String msg);



	/**
	 * @param username_send
	 * @param username_receive
	 * @return String[sender name, receiver name, msg] 
	 * if there is not history it is empty array (you can check its size..) if there was a problem it is null..
	 * the Integer is as * usual
	 */
	public Pair<Integer, ArrayList<String[]>> getHistoryMassages(String username_send,String username_receive);

	
	
}
