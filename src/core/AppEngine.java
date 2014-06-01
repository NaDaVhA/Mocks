package core;


import java.util.ArrayList;
import java.util.List;

import utilities.Pair;
import db.ConnectionPool;

import db.DBActionsInterface;
import db.dbActions;

public class AppEngine implements ApplicationInterface{
	
	private DBActionsInterface dbActionRunner;
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
		if(!status)
			System.out.println("Database initialization failure!");
		
		
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
	
	//also log in user
	@Override
	public boolean isUserRegisterd(String username, String password) {
		boolean stat = true;
		stat = this.dbActionRunner.authenticateUser(username, password);
		if (!stat)
			return stat;
		stat = initializeUserInstance(username);
		return stat;
		 
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return this.dbActionRunner.usernameExists(username);
	}

	@Override
	public boolean signUpUser(String username, String password) {
		return this.dbActionRunner.registerNewUser(username, password);
	}
	

	
	@Override
	public Pair<String,String> getStatusSong() {
		return this.user.getStatusSong();
	}
	
	
	@Override
	public boolean changeStatusSong(Pair<String,String> song) {
		boolean stat=true;
		String song_name = song.getLeft();
		
		int user_id =this.user.getUserID();
		int song_id = this.dbActionRunner.getSongID(song_name);
	
		stat = this.dbActionRunner.setUserStatusSong(user_id, song_id);
		if (!stat)
		{
			return stat;
		}
		
		this.user.setStatusSong(song);
	
		return stat;
	}

	@Override
	public List<Pair<String,String>> getSongList() {
		
		return  this.user.getSongArtistList();
	}

	@Override
	public ArrayList<String> getFriendList() {
		return this.user.getfreindsList();
	}

	@Override
	public boolean signOutUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public ArrayList<String> getSearchResultsFriends(String friend_name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean addFriend(String username) {
		
		int user_id =this.user.getUserID();
		int freind_user_id = this.dbActionRunner.getUserId(username);
		
		boolean stat = this.dbActionRunner.addFreindToUser(user_id, freind_user_id);
		
		if (stat = false)
		return stat;
		
		
		this.user.addFreindTofreindsList(username);;
		return stat;
	
	}

	
	
	@Override
	public ArrayList<Pair<String,String>> getSearchResultsByArtist(String artist_name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public ArrayList<Pair<String,String>> getSearchResultsBySong(String song_name){
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean addSong(String song,String artist) {
		
		String user_name=this.user.getUserName();
		boolean stat = this.dbActionRunner.addSongToUser(user_name, song);
		if (stat = false)
			return stat;
		Pair<String,String> pair = new Pair<String,String>(song,artist);
		this.user.addSongToSongArtistlist(pair);
		return stat;
		
	}

	
	
	
	//////////////////////////////////
	//			Help methods
	//////////////////////////////////
	
	/**
	 * 
	 * @return
	 */
	private boolean initializeUserInstance(String username){
		
		
		int userID =this.dbActionRunner.getUserId(username);
		if (userID==-1)
			return false;
		
		this.user = new User(userID,username);
		
		//Initialize user's fields
		
		String[] statusSongTemp = this.dbActionRunner.getUserStatusSong(userID);
		if(statusSongTemp == null)
			return false;
		Pair<String,String> statusSong =ConvertArrayToPair(statusSongTemp);
		this.user.setStatusSong(statusSong); 
		
	
		ArrayList<String[]> mySongsListTemp = this.dbActionRunner.getUserSongList(userID);
		if(mySongsListTemp == null)
			return false;
		List<Pair<String,String>> mySongsList = ConvertListArrayToListPair(mySongsListTemp);
		this.user.setSongArtistlist(mySongsList); 
		
		ArrayList<String[]> myArtistListTemp = this.dbActionRunner.getUserArtistList(userID);
		if(myArtistListTemp == null)
			return false;
		List<Pair<String,String>> myArtistList = ConvertListArrayToListPair(myArtistListTemp);
		this.user.setArtistlist(myArtistList); 
		
		ArrayList<String[]> myFreindsListTemp = this.dbActionRunner.getUserFreindsList(userID);
		if(myFreindsListTemp == null)
			return false;
		ArrayList<String> myFreindsList = ConvertListArrayToList(myArtistListTemp);
		this.user.setfreindsList(myFreindsList); 
	
		return true;
	}
	
	private Pair<String,String> ConvertArrayToPair(String[] str)
	{
		Pair<String,String> pair=null;
		if (str.length == 1)
			pair= new Pair<String,String>(str[0],null);
		if (str.length == 2)
			pair= new Pair<String,String>(str[0],str[1]);
		return pair;
		
		
	}
	
	private List<Pair<String,String>> ConvertListArrayToListPair(ArrayList<String[]> arraylst)
	{
		List<Pair<String,String>> pairList = new ArrayList<Pair<String,String>>();
		for (String[] s : arraylst)
		{
			pairList.add(ConvertArrayToPair(s));
		}
		return pairList;
	}
	
	private ArrayList<String> ConvertListArrayToList(ArrayList<String[]> arraylst)
	{
		ArrayList<String> List = new ArrayList<String>();
		for (String[] s : arraylst)
		{
			List.add(s[0]);
		}
		return List;
		
	}
	
	
	
	
}
