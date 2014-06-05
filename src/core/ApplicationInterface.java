package core;

import java.util.ArrayList;
import java.util.List;

import utilities.Pair;

public interface ApplicationInterface {
	
	/**
	 * Initializes the application.
	 * @return true if succeeded, false otherwise.
	 */
	public boolean initializeApplication();
	
	
	/**
	 * Updates  the application's music database.
	 * @return true if succeeded, false otherwise.
	 */
	public boolean updateMusicDatabase();
	
	
	/**
	 *  for log in screen
	 * @param username - string representing the username
	 * @param password - string representing the password
	 * @return true iff its an authentic user
	 */
	public boolean isUserRegisterd(String username,String password);
	
	/**
	 * for sign up screnn
	 * @param username
	 * @return true iff username is already in use
	 */
	public boolean isUsernameTaken(String username);
	
	
	/**
	 * for sign up screen
	 * @param username
	 * @param password
	 * @return true iff signing up the user succeeded
	 */
	public boolean signUpUser(String username, String password);
	
	
	//username of a friend
	public Pair<String,String> getStatusSong(String username); 
	
	/**
	 * for main screen
	 * @param username
	 * @return the status song of the user
	 */
	public Pair<String,String> getStatusSong();
	
	
	
	/**
	 * 
	 * @param username
	 * @param song
	 * @return true iff the change succeeded
	 */
	public boolean changeStatusSong(Pair<String,String> song); //qaqa
	
	
	
	/**
	 * for main screen
	 * @param username
	 * @return the song list of the user
	 */
	
	//username of a friend
	public List<Pair<String,String>> getSongList(String username);
	
	
	public List<Pair<String,String>> getSongList();
	
	/**
	 * for main screen
	 * @param username
	 * @return
	 */
	public ArrayList<String> getFriendList();
	
	/**
	 * for main screen
	 * @param username
	 * @return true iff signed out
	 */
	public boolean signOutUser(String username);
	
	/**
	 * for add a friend screen
	 * @param friend_name - name to search in the users db
	 * @return list of the search results 
	 */
	public ArrayList<String> getSearchResultsFriends(String friend_name);
	
	/**
	 * for add a friend screen
	 * @param username
	 * @return true iff the friend was added to the friend list
	 */
	public boolean addFriend(String username);
	
	/**
	 * for add song screen
	 * @param artist_name - the artist name to search by
	 * @return list of the search results
	 */
	public ArrayList<Pair<String,String>> getSearchResultsByArtist(String artist_name);
	
	/**
	 * for add song screen
	 * @param song_name -- the song name to search by
	 * @return list of the search results
	 */
	public ArrayList<Pair<String,String>> getSearchResultsBySong(String song_name);
	
	/**
	 * 
	 * @param username
	 * @param song
	 * @return true iff the song was added to the user song list
	 */
	public boolean addSong(String song,String artist);


	/**
	 * 
	 * @return the user username
	 */
	
	public String getUsername();
	
	
	//till here!!!!!
	
}
