package core;

import java.sql.Connection;
import java.util.List;

import db.ConnectionPool;
import db.DataBaseManager;
import db.databaseActions;
import db.dbActions;

public class AppEngine implements ApplicationInterface{
	
	private databaseActions dbActionRunner;
	private User user;
	
	
	//Constructor
	public AppEngine(){
		
		//Extract connection parameters from configuration file and initialize a connectionPool
		String[] confParameters = Configurator.getConnectionParameters("projectConfig.xml");
		ConnectionPool cp = new ConnectionPool(confParameters[0], confParameters[1], confParameters[2], confParameters[3], confParameters[4]); 
		
		//Initialize dbActionRunner
		this.dbActionRunner = new dbActions(cp);
			
	}
	
	
	
	/////////////////////////////////////////////////
	// 		Application's initialization code 
	/////////////////////////////////////////////////
	
	/**
	 * First, checks whether database is initialized. If needed - initializes application's database.
	 * @return true if succeeded, false otherwise.
	 */
	public boolean initializeApplication(){
		
		boolean status = true;
		
		String yagoFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml"); 
		
		status = this.dbActionRunner.initializeDatabase(yagoFilesPath);
				
		return status;
		
	}
	
	
	///////////////////////////////////////
	// 		Music management code
	///////////////////////////////////////
	
	
	/**
	 * Updates the music database.
	 * @return true if succeeded, false otherwise.
	 */
	public boolean updateMusicDatabase(){
		
		boolean status = true;
		
		String yagoFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml"); 
		
		status = this.dbActionRunner.updateMusicDB(yagoFilesPath);
				
		return status;
		
	}
	
	
	
	///////////////////////////////////////
	// 		User management code
	///////////////////////////////////////
	
	
	@Override
	public boolean isUserRegisterd(String username, String password) {
		return this.dbActionRunner.authenticateUser(username, password);
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return this.dbActionRunner.usernameExists(username);
	}

	@Override
	public boolean signUpUser(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/**
	 * Authenticates user, and creates user instance.
	 * @param username
	 * @param password
	 * @return true if user is authenticated, false otherwise.
	 */
	public boolean signInUser(String username, String password) {
		
		boolean stat=true;
		stat = this.dbActionRunner.authenticateUser(username, password);
		
		if(!stat){
			return false;
		}else {
			stat= initializeUserInstance(this.dbActionRunner.getUserId(username));	
		}
		
		return stat;
	}

	
	@Override
	public String getStatusSong() {
		return this.user.getStatusSong();
	}
	
	
	@Override
	public boolean changeStatusSong(String song) {
		boolean stat=true;
		int user_id =this.user.getUserId();
		int song_id = this.dbActionRunner.getSongID(song);
	
		stat = this.dbActionRunner.setUserStatusSong(user_id, song_id);
		if (!stat)
		{
			return stat;
		}
		
		this.user.setStatusSong(song);
	
		return stat;
	}

	@Override
	public List<String[]> getSongList() {
		
		return  this.user.getSongList();
	}

	@Override
	public List<String> getFriendList(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean signOutUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getSearchResultsFriends(String friend_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addFriend(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getSearchResultsByArtist(String artist_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSearchResultsBySong(String song_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addSong(String song,String artist) {
		
		String user_name=this.user.getUsername();
		boolean stat = this.dbActionRunner.addSongToUser(user_name, song);
		if (stat = false)
			return stat;
		this.user.addSongToMySongsList(song, artist);
		return stat;
		
	}

	
	
	
	//////////////////////////////////
	//			Help methods
	//////////////////////////////////
	
	/**
	 * 
	 * @return
	 */
	private boolean initializeUserInstance(int userID){
		
		boolean status = true;
		
		this.user = new User(userID);
		
		//Initialize user's fields
		String username = this.dbActionRunner.getUserName(userID);
		if(username == null)
			return false;
		this.user.setUsername(username); 
		
		String statusSong = this.dbActionRunner.getUserStatusSong(userID);
		if(statusSong == null)
			return false;
		this.user.setStatusSong(statusSong); 
		
		//Mira - decide what to do here
		List<String[]> mySongsList = this.dbActionRunner.getUserSongList(userID);
		if(mySongsList == null)
			return false;
		this.user.setSongsList(mySongsList); 
		
		List<String[]> myArtistList = this.dbActionRunner.getUserArtistList(userID);
		if(myArtistList == null)
			return false;
		this.user.setArtistList(myArtistList); 
		
		return status;
	}
	
}
