package core;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import recommender.RecommenderEngineAdapter;
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
	// 		Application's management code 
	/////////////////////////////////////////////////
	
	/**
	 * First, checks whether database is initialized. If needed - initializes application's database.
	 * @return Pair<0,true> if succeeded, Pair<0,false> if not and Pair<-1,something>.
	 */
	public Pair<Integer, Boolean> initializeApplication(){
		
		boolean status = true;
		
		String yagoFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml", false); 
		
		try {
			status = this.dbActionRunner.initializeDatabase(yagoFilesPath);
		} catch (SQLException e) {
			// Connection is lost.
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, false);
		}
		if(!status)
			System.out.println("Database initialization failure!");
		
		
		return new Pair<Integer, Boolean>(0, status);
		
	}
	
	
	/**
	 * Terminates connection to database.
	 * Closes all open (and valid) connections from the connection pool.
	 */
	public void terminateDBConnection(){
		
		this.dbActionRunner.terminateConnectionToDB();
		
	}
	
	
	
	///////////////////////////////////////
	// 		Music management code
	///////////////////////////////////////
	
	
	/**
	 * Updates the music database.
	 * @return true if succeeded, false otherwise.
	 */
	public Pair<Integer, Boolean> updateMusicDatabase(){
		
		boolean status = true;
		
		String yagoFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml", true); 
		
		try {
			status = this.dbActionRunner.updateMusicDB(yagoFilesPath);
		} catch (SQLException e) {
			// Connection is lost.
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, false);
		}
				
		return new Pair<Integer, Boolean>(0, status);		
	}
	
	
	
	///////////////////////////////////////
	// 		User management code
	///////////////////////////////////////
	
	//also log in user
	@Override
	public Pair<Integer, Boolean> isUserRegisterd(String username, String password) {
		
		boolean status = true;
		
		try {
			status = this.dbActionRunner.authenticateUser(username, password);
			if (!status)
				return new Pair<Integer, Boolean>(0, status);
			
			status = initializeUserInstance(username);
		} catch (SQLException e) {
			// Connection is lost.
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
		
		return new Pair<Integer, Boolean>(0, status);		 
	}

	
	@Override
	public Pair<Integer, Boolean> isUsernameTaken(String username) {
		
		boolean status = false;
		
		try {
			status = this.dbActionRunner.usernameExists(username);
			return new Pair<Integer, Boolean>(0, status);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
	}

	
	@Override
	public Pair<Integer, Boolean> signUpUser(String username, String password) {
		
		boolean stat = false;
		try {
			
			stat = this.dbActionRunner.registerNewUser(username, password);
			if (!stat)
				return new Pair<Integer, Boolean>(0, stat);
			
			stat = initializeUserInstance(username);
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, stat);

		}
		
		return new Pair<Integer, Boolean>(0, stat);
	}
	

	
	@Override
	public Pair<String,String> getStatusSong() {
		return this.user.getStatusSong();
	}
	
	
	@Override
	public Pair<Integer, Pair<String, String>> getStatusSong(String username) {
		
		int freind_user_id;
		String[] str = null;
		
		try {
			freind_user_id = this.dbActionRunner.getUserId(username);
			if (freind_user_id==-1)
				return new Pair<Integer, Pair<String,String>>(0, null);
			
			str = this.dbActionRunner.getUserStatusSong(freind_user_id);
			if (str==null)
				return new Pair<Integer, Pair<String,String>>(0, null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Pair<String,String>>(-1, null);

		}
		
		Pair<String, String> result = ConvertArrayToPair(str);
		return new Pair<Integer, Pair<String,String>>(0, result);
	}
	
	
	
	@Override
	public Pair<Integer, Boolean> changeStatusSong(Pair<String,String> song) {
		
		boolean stat=true;
		String song_name = song.getRight();
		
		int user_id =this.user.getUserID();
		int song_id;
		try {
			
			song_id = this.dbActionRunner.getSongID(song_name);
		
			stat = this.dbActionRunner.setUserStatusSong(user_id, song_id);
			if (!stat)
			{
				return new Pair<Integer, Boolean>(0, stat);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, stat);

		}
		
		this.user.setStatusSong(song);
	
		return new Pair<Integer, Boolean>(0, stat);
	}

	@Override
	public Pair<Integer, List<Pair<String, String>>> getSongList(String username)
	{
		int freind_user_id;
		ArrayList<String[]> lst = null;
		
		try {
			freind_user_id = this.dbActionRunner.getUserId(username);
			
			if (freind_user_id==-1)
				return new Pair<Integer, List<Pair<String,String>>>(0, null);
			
			lst = this.dbActionRunner.getUserSongList(freind_user_id);
			if (lst == null)
				return new Pair<Integer, List<Pair<String,String>>>(0, null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, List<Pair<String,String>>>(-1, null);

		}
		
		ArrayList<Pair<String, String>> result = ConvertListArrayToListPair(lst);
		return new Pair<Integer, List<Pair<String,String>>>(0, result);

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

	
	//mira
	
	@Override
	public  Pair<Integer, ArrayList<String>> getSearchResultsFriends(String friend_name) {
		
		try {
			ArrayList<String> result = ConvertListArrayToList(this.dbActionRunner.getFindFreind(friend_name));
			return new Pair<Integer, ArrayList<String>>(0, result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, ArrayList<String>>(-1, null);
		}
		
	}
	

	//mira-
	@Override
	public boolean addFriend(String username) {
		
		int user_id =this.user.getUserID();
		int freind_user_id;
		boolean stat = false;
		
		try {
			freind_user_id = this.dbActionRunner.getUserId(username);
			stat = this.dbActionRunner.addFreindToUser(user_id, freind_user_id);
			
			if (stat == false)
			return stat;
			
			this.user.addFreindTofreindsList(username);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stat;
	
	}

	
	
	@Override
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsByArtist(String artist_name) {
		
		ArrayList<String[]> result;
		try {
			result = this.dbActionRunner.getArtistList(artist_name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, ArrayList<Pair<String,String>>>(-1, null);
		}
		
		if (result == null)
			return new Pair<Integer, ArrayList<Pair<String,String>>>(0, null);
		
		ArrayList<Pair<String,String>> KeithRichards =  ConvertListArrayToListPair(result);
		return new Pair<Integer, ArrayList<Pair<String,String>>>(0, KeithRichards);

	}

	
	@Override
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsBySong(String song_name){
		
		ArrayList<String[]> result;
		
		try {
			result = this.dbActionRunner.getSongsArtistList(song_name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, ArrayList<Pair<String,String>>>(-1, null);
		}
		if (result == null)
			return null;
		
		ArrayList<Pair<String, String>> MickJagger = ConvertListArrayToListPair(result);
		return new Pair<Integer, ArrayList<Pair<String,String>>>(0, MickJagger);
	}

	
	@Override
	public Pair<Integer, Boolean> addSong(String song,String artist) {
		
		System.out.println("Song = " + song);
		
		String user_name=this.user.getUserName();
		boolean stat = false;
		
		try {
			stat = this.dbActionRunner.addSongToUser(user_name, song);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, stat);

		}
		
		if (stat == false)
			return new Pair<Integer, Boolean>(0, stat);
		
		Pair<String,String> pair = new Pair<String,String>(song,artist);
		this.user.addSongToSongArtistlist(pair);
		
		return new Pair<Integer, Boolean>(0, stat);
		
	}

	
	
	
	//////////////////////////////////
	//			Help methods
	//////////////////////////////////
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private boolean initializeUserInstance(String username) throws SQLException{
		
		System.out.println("mira1");
		
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
		ArrayList<String> myFreindsList = ConvertListArrayToList(myFreindsListTemp);	
		this.user.setfreindsList(myFreindsList); 
	
	
		
		return true;
	}
	
	
	/**
	 * Converts array to pair...
	 * @param str
	 * @return Pair<artist_name,song_name> or Pair<null,artist_name>
	 */
	private Pair<String,String> ConvertArrayToPair(String[] str)
	{
		Pair<String,String> pair=null;
		if (str.length == 1)
			pair= new Pair<String,String>(null,str[0]);
		if (str.length == 2)
			pair= new Pair<String,String>(str[1],str[0]);
		return pair;
		
		
	}
	
	private ArrayList<Pair<String,String>> ConvertListArrayToListPair(ArrayList<String[]> arraylst)
	{
		ArrayList<Pair<String,String>> pairList = new ArrayList<Pair<String,String>>();
		for (String[] s : arraylst)
		{
			pairList.add(ConvertArrayToPair(s));
		}
		return pairList;
	}
	
	private ArrayList<String> ConvertListArrayToList(ArrayList<String[]> arraylst)
	{
		ArrayList<String> List = new ArrayList<String>();

		if(arraylst==null)
			return null;
		
		for (String[] s : arraylst)
		{
			List.add(s[0]);
		}
		return List;
		
	}



	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.user.getUserName();
	}



	@Override
	public void InitalizeRecommender() {
		RecommenderEngineAdapter.getInstance().initalize(dbActionRunner);
	}


//if user friend is not exist or a problem in dbAction than return false
	@Override
	public Pair<Integer, Boolean> removeFriendFromUser(String friendusername) {
		
		boolean status = false;
		
		int user_id = this.user.getUserID();
		
		int user_friend_id;
		try {
			user_friend_id = this.dbActionRunner.getUserId(friendusername);
			if (user_friend_id == -1 || user_friend_id == 0)
				return new Pair<Integer, Boolean>(0, status);
			
			status = this.dbActionRunner.removeFriendFromUser(user_id, user_friend_id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
		
		if (status)
			this.user.removeFriendFromFriendList(friendusername);
		
		return new Pair<Integer, Boolean>(0, status);
		
	}



	@Override
	public Pair<Integer, Boolean> removeSongFromUser(String songname,String artistname) {
		boolean status = false;
		Pair<String,String> songArtist = new Pair<String,String>(songname, artistname);
		
		int user_id = this.user.getUserID();
		
		int user_song_id;
		try {
			user_song_id = this.dbActionRunner.getSongID(songname);
			if (user_song_id == -1 || user_song_id == 0)
				return new Pair<Integer, Boolean>(0, status);
			
			status = this.dbActionRunner.removeSongFromUser(user_id, user_song_id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
		
		if (status)
			this.user.removeSongFromSongArtistList(songArtist);
		
		return new Pair<Integer, Boolean>(0, status);
	}
	
	
	
	
}
