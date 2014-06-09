package core;


import java.io.File;
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
	
	
	
	public Pair<Integer, Boolean> checkInitializationStatus(){
		
		boolean status = false;
		
		try {
			status = this.dbActionRunner.isDatabaseInitialized();
		} catch (SQLException e) {
			// Connection is lost.
			System.out.println("checkInitializationStatus: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, false);
		}

		return new Pair<Integer, Boolean>(0, status);
		
	}
	
	
	public Pair<Integer, Boolean> initializeApplication(){
		
		boolean status = true;
		
		String yagoFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml", false); 
		
		File f = new File(yagoFilesPath);
		if(!f.exists()){
			System.out.println("checkInitializationStatus: Error in given path: " + yagoFilesPath);
			return new Pair<Integer, Boolean>(-2, false);
		}
		
		try {
			status = this.dbActionRunner.initializeDatabase(yagoFilesPath);
		} catch (SQLException e) {
			// Connection is lost.
			System.out.println("checkInitializationStatus: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, false);
		}
		if(!status)
			System.out.println("Database initialization failure!");
		
		
		return new Pair<Integer, Boolean>(0, status);
		
	}
	
	
	public void terminateDBConnection(){
		
		this.dbActionRunner.terminateConnectionToDB();
		
	}
	
	
	
	///////////////////////////////////////
	// 		Music management code
	///////////////////////////////////////
	
	

	public Pair<Integer, Boolean> updateMusicDatabase(){
		
		boolean status = true;
		
		String yagoUpdateFilesPath =  Configurator.getYagoFolderPath("projectConfig.xml", true); 
		
		File f = new File(yagoUpdateFilesPath);
		if(!f.exists()){
			System.out.println("updateMusicDatabase: Error in given path: " + yagoUpdateFilesPath);
			return new Pair<Integer, Boolean>(-2, false);
		}
		
		try {
			status = this.dbActionRunner.updateMusicDB(yagoUpdateFilesPath);
		} catch (SQLException e) {
			// Connection is lost.
			System.out.println("updateMusicDatabase: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Pair<String,String>>(-1, null);

		}
		
		Pair<String, String> result = ConvertArrayToPair(str);
		return new Pair<Integer, Pair<String,String>>(0, result);
	}
	
	
	
	@Override
	public Pair<Integer, Boolean> changeStatusSong(Pair<String,String> song) {
	
		boolean stat=false;
		String song_name = song.getRight();
		String artist_name = song.getLeft();
		
		int user_id =this.user.getUserID();
		int song_id, artist_id;
		try {
			
			song_id = this.dbActionRunner.getSongID(song_name);
			if (song_id == 0 ||song_id ==-1)
				return new Pair<Integer, Boolean>(0, stat);
			
			artist_id = this.dbActionRunner.getArtistID(artist_name);
			if (artist_id == 0 ||artist_id ==-1)
				return new Pair<Integer, Boolean>(0, stat);
		
			stat = this.dbActionRunner.setUserStatusSong(user_id, song_id, artist_id);
			if (!stat)
			{
				return new Pair<Integer, Boolean>(0, stat);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
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
	public Pair<Integer, ArrayList<String>> getFriendList(String username) {
		int user_id;
		ArrayList<String[]> lst = null;
		
		try {
			user_id = this.dbActionRunner.getUserId(username);
			
			if (user_id==-1)
				return new Pair<Integer, ArrayList<String>>(0, null);
			
			lst = this.dbActionRunner.getUserFreindsList(user_id);
			if (lst == null)
				return new Pair<Integer, ArrayList<String>>(0, null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, ArrayList<String>>(-1, null);

		}
		
		ArrayList<String> result = ConvertListArrayToList(lst);
		return new Pair<Integer, ArrayList<String>>(0, result);
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
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, ArrayList<String>>(-1, null);
		}
		
	}
	

	//mira-
	@Override
	public Pair<Integer, Boolean> addFriend(String username) {
		
		int user_id =this.user.getUserID();
		int freind_user_id;
		boolean stat = false;
		
		try {
			freind_user_id = this.dbActionRunner.getUserId(username);
			if (freind_user_id == -1 || freind_user_id == 0)
				return new Pair<Integer, Boolean>(0, stat);
			stat = this.dbActionRunner.addFreindToUser(user_id, freind_user_id);
			
			if (stat == false)
			return new Pair<Integer, Boolean>(0, stat);
			
			this.user.addFreindTofreindsList(username);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, stat);
		}
		
		return new Pair<Integer, Boolean>(0, stat);
	
	}

	
	
	@Override
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsByArtist(String artist_name) {
		
		ArrayList<String[]> result;
		try {
			result = this.dbActionRunner.getArtistList(artist_name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
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
			stat = this.dbActionRunner.addSongToUser(user_name,song,artist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, stat);

		}
		
		if (stat == false)
			return new Pair<Integer, Boolean>(0, stat);
		
		Pair<String,String> pair = new Pair<String,String>(song,artist);
		this.user.addSongToSongArtistlist(pair);
		
		return new Pair<Integer, Boolean>(0, stat);
		
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
			System.out.println("isUserRegisterd: Connection exception.");
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
			System.out.println("isUserRegisterd: Connection exception.");
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
		
		if (status)
			this.user.removeSongFromSongArtistList(songArtist);
		
		return new Pair<Integer, Boolean>(0, status);
	}
	
	
	
	
	//////////////////////////////////
	//			Messaging
	//////////////////////////////////
	




	@Override
	public Pair<Integer, Boolean> sendMassage(String username_send,
			String username_receive, String msg) {
		boolean status = false;
		
		try {
			
			int username_send_id = this.dbActionRunner.getUserId(username_send);
			if (username_send_id == -1 || username_send_id == 0)
				return new Pair<Integer, Boolean>(0, status);
			
			
			int username_receive_id = this.dbActionRunner.getUserId(username_receive);
			if (username_receive_id == -1 || username_receive_id == 0)
				return new Pair<Integer, Boolean>(0, status);
			
			status = this.dbActionRunner.send_massage(username_send_id,username_receive_id,msg);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, Boolean>(-1, status);
		}
		
		return new Pair<Integer, Boolean>(0, status);
		
	}



	@Override
	public Pair<Integer, ArrayList<String[]>> getHistoryMassages(
			String username_send, String username_receive) {
		
		ArrayList<String[]> msgHistory = new ArrayList<String[]>();
		try {
			
			int username_send_id = this.dbActionRunner.getUserId(username_send);
			if (username_send_id == -1 || username_send_id == 0)
				return new Pair<Integer, ArrayList<String[]>>(0, null);
			
			
			int username_receive_id = this.dbActionRunner.getUserId(username_receive);
			if (username_receive_id == -1 || username_receive_id == 0)
				return new Pair<Integer, ArrayList<String[]>>(0, null);
			
			msgHistory = this.dbActionRunner.get_history_massage(username_send_id,username_receive_id,username_send,username_receive);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Pair<Integer, ArrayList<String[]>>(-1, msgHistory);
		}
		
		return new Pair<Integer, ArrayList<String[]>>(0, msgHistory);
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




	


}
