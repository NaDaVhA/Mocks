package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class dbActions implements DBActionsInterface{

	private ConnectionPool connectionPool;

	
	//Constructor
	public dbActions(ConnectionPool cp){
		
		this.connectionPool = cp;
	}
	


	@Override
	public ArrayList<String[]> getSongsList() throws SQLException{
		
		String sql_query ="Select song_name, song_id " + 
				"From songs";
		return executeQuery(sql_query,true); 
			
	}
	
	
	@Override
	public int getArtistID(String artist_name) throws SQLException
	{
		String artist_name_c = ConvertStringCharToLegal(artist_name);
		
		String sql_query ="Select artist_id " + 
				"From artists " +
				"Where artists.artist_name = '" + artist_name_c +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		return checkID(result);
		
		
	}
	
	@Override
	public int getSongID(String song_name) throws SQLException
	{
		String song_name_c = ConvertStringCharToLegal(song_name);
		
		String sql_query ="Select song_id " + 
				"From songs " +
				"Where songs.song_name = '" + song_name_c +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		return checkID(result);
	}
	
	@Override
	public ArrayList<String[]> getSongsArtistList() throws SQLException{
	
		String sql_query ="Select song_name, artist_name " + 
				"From songs,artist_song,artists " +
				"Where songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id";
		
		return executeQuery(sql_query,true);
			
	}
	
	@Override
	public ArrayList<String[]> getSongsArtistList(String songname) throws SQLException{
		
		String song_name_c =  ConvertStringCharToLegal(songname);
		String sql_query ="Select song_name, artist_name " + 
				"From songs,artist_song,artists " +
				"Where songs.song_name LIKE " + '"' + '%'+ song_name_c + '%'+  '"' + "And songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id";
	
		return executeQuery(sql_query,true);
			
	}
	
	@Override
	public ArrayList<String[]> getArtistList(String artist_name) throws SQLException
	{
		String artist_name_c =  ConvertStringCharToLegal(artist_name);
		String sql_query ="Select song_name, artist_name " + 
				"From artists, artist_song, songs " +
				"Where artists.artist_name LIKE " + '"' + '%'+ artist_name_c + '%'+  '"' +
				" And artists.artist_id = artist_song.artist_id And songs.song_id = artist_song.song_id";
		return executeQuery(sql_query,true);
		
	}
	
	@Override
	public ArrayList<String[]> getUsersList() throws SQLException{
		
		String sql_query ="Select user_id, user_name, status_song_id " + 
				"From users ";	
		return executeQuery(sql_query,true); 
		
	}
	
	@Override
	public boolean addSongToUser(String user_name, String song_name,String artist_name) throws SQLException{
		
		boolean status = false;
		int user_id = getUserId(user_name);
		
		if (user_id == 0 || user_id == -1)
			return status;
		
		int song_id = getSongID(song_name);
		if (user_id == 0 || user_id == -1)
			return status;
		
		int artist_id = getArtistID(artist_name);
		if (artist_id == 0 || artist_id == -1)
			return status;
		
		String sql_query ="INSERT INTO `user_songs` " +
				"(`user_id`, `song_id`, `artist_id` ) VALUES ('"+user_id+"', '"+song_id+"', '"+artist_id+"');";
			
		if (executeQuery(sql_query,false)==null)
			return false; 
	
		return true;
	}
	
	@Override
	public boolean addArtistToUser(String user_id, String artist_id) throws SQLException{
		String sql_query ="INSERT INTO `user_artist` " +
				"(`user_id`, `artist_id`) VALUES ('"+user_id+"', '"+artist_id+"');";
		
		if (executeQuery(sql_query,false)==null)
			return false; 
	
		return true;
		
	}
	
	@Override
	public boolean removeFriendFromUser(int userID, int userFriendID) throws SQLException {
		String sql_query ="DELETE FROM users_friends " + 
				"Where users_friends.user_id =" + userID + " And users_friends.user_friend_id = "+ userFriendID;
		if (executeQuery(sql_query,false)==null)
			return false;
		
		return true;
	}


	@Override
	public boolean removeSongFromUser(int userID, int songID) throws SQLException {
		String sql_query ="DELETE FROM user_songs " + 
				"Where user_songs.user_id =" + userID + " And user_songs.song_id = "+ songID;
		if (executeQuery(sql_query,false)==null)
			return false;
		
		return true;
	}
	
	
	@Override
	public String[] getUserName(int userID) throws SQLException {

		
		String userId_as_String = ConvertIntegerToString(userID);
		
		String sql_query ="Select user_name, " + 
				"From users" +
				"Where users.user_id = " + userId_as_String;
		
		ArrayList<String[]> result = executeQuery(sql_query,true);
		
		if (result == null)  
		{
			return null;
		}	
		if (result.size()==0)
		{
			String[] nothing = {null};
			return nothing;
		}
		
		return  result.get(0); // true for  query is_select 
	}
	
	
	@Override
	public ArrayList<String[]> getSingersList() throws SQLException {
		
		String sql_query ="Select artist_id, artist_name " + 
				"From artists " +
				"Where artists.artist_type=1 ";
		return  executeQuery(sql_query,true);
		
	}


	@Override
	public ArrayList<String[]> getBandsList() throws SQLException {
		String sql_query ="Select artist_id, artist_name " + 
				"From artists " +
				"Where artists.artist_type=0 ";
		return  executeQuery(sql_query,true);
	}

	
	@Override
	public ArrayList<String[]> getUserFreindsList(int userID) throws SQLException
	{
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select user_name " + 
				"From users_friends, users " +
				"Where users_friends.user_id=" + StringUserID + " And users.user_id = users_friends.user_friend_id "; 
	
		return executeQuery(sql_query,true);
		
	}

	@Override
	public boolean addFreindToUser(int userID, int userFreindID) throws SQLException
	{
				String sql_query ="INSERT INTO `users_friends` " +
						"(`user_id`, `user_friend_id`) VALUES ('"+userID+"', '"+userFreindID+"');";
				if (executeQuery(sql_query,false)==null)
					return false;
				
				System.out.println("aaa\n");
			
				return true;
	}
	
	@Override
	public ArrayList<String[]> getFindFreind(String Freindname) throws SQLException
	{
		
		String sql_query ="Select user_name " + 
				"From users " +
				"Where users.user_name LIKE " + '"' + '%'+ Freindname + '%'+  '"'; 
		return executeQuery(sql_query,true);
	}
	
	
	@Override
	public boolean usernameExists(String username) throws SQLException {
		 
		String sql_query ="Select user_name " + 
				"From users " +
				"Where users.user_name= '" + username +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		
		if (result == null)
		{
			return false;
		}
		
		if (result.size()==0)
		{
			return false;
		}
		
		return true;
	}


	@Override
	public boolean registerNewUser(String userName, String password) throws SQLException {
		
		String sql_query ="INSERT INTO `users` " +
		"(`user_name`, `password`) VALUES ('"+userName+"', '"+password+"');";
			
		if (executeQuery(sql_query,false)==null)
			return false; 
	
		return true;
	}
		
	//mira
	@Override
	public boolean authenticateUser(String username, String password) throws SQLException{
		
		String sql_query ="Select user_name " + 
				"From users " +
				"Where users.user_name= '" + username + "' AND users.password= '" + password + "'";
		
		
		ArrayList<String[]> result;
		try {
			result = executeQuery(sql_query,true);
		} catch (SQLException e) {
			System.out.println("authenticateUser: Connection exception.");
			e.printStackTrace();
			throw e;
		}
		if (result == null)
		{
			return false;
		}
		
		if (result.size()==0)
		{
			return false;
		}
		
		return true;
	}	
		
	

	@Override
	public ArrayList<String[]> getUserSongList(int userID) throws SQLException {
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select song_name, artist_name " + 
				"From user_songs, songs, artist_song, artists " +
				"Where user_songs.user_id=" + StringUserID + " And user_songs.artist_id = artists.artist_id And songs.song_id =user_songs.song_id And songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id"; 
		
		return executeQuery(sql_query,true);
	}
	
	@Override
	public ArrayList<String[]> getUserArtistList(int userID) throws SQLException {
		String StringUserID= ConvertIntegerToString(userID);
		String sql_query ="Select artist_name " + 
				"From user_artist, artists " +
				"Where user_artist.user_id=" + StringUserID + " And artists.artist_id =user_artist.artist_id"; 
		
		return executeQuery(sql_query,true);
		
		
		
	}

	@Override
	public String[] getUserStatusSong(int userID) throws SQLException {
		String StringUserID= ConvertIntegerToString(userID);
		
		
		String sql_query ="Select song_name, artist_name " + 
				"From users, songs , artist_song , artists " +
				"Where users.user_id=" + StringUserID + " And artists.artist_id = users.status_artist_id And artist_song.song_id = users.status_song_id And songs.song_id = users.status_song_id And artists.artist_id = artist_song.artist_id "; 
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		
		if (result == null)  
		{
			return null;
		}	
		if (result.size()==0)
		{
			String[] nothing = {"",""};
			return nothing;
		}
		
		return executeQuery(sql_query,true).get(0);
	}

	@Override
	public boolean setUserStatusSong(int userID, int songID , int artistID) throws SQLException {
		
		String sql_query ="UPDATE `users` " +
				" set status_song_id = " + songID + ", status_artist_id =" + artistID
				+ " where users.user_id =" + userID;
		if (executeQuery(sql_query,false)==null)
			return false;
		
	
		return true;
	}


	
	@Override
	public Boolean send_massage(int username_send_id, int username_receive_id,
			String msg) throws SQLException {
		
		String msg_c = ConvertStringCharToLegal(msg);
		String sql_query ="INSERT INTO `users_massages` " +
				"(`user_sender_id`, `user_receiver_id` , `massage_cont`) VALUES ('"+username_send_id+"', '"+username_receive_id+"', '"+msg_c+"');";
		if (executeQuery(sql_query,false)==null)
			return false; 
		
		return true;
	}

	
	@Override
	public ArrayList<String[]> get_history_massage(int username_send_id,
			int username_receive_id,String sender_name, String receiver_name ) throws SQLException {
		
		String sql_query ="Select user_sender_id, user_receiver_id, massage_cont " + 
				"From users_massages " +
				"Where (users_massages.user_sender_id =" +username_send_id+ " OR users_massages.user_sender_id = " + username_receive_id +") AND " +
				 "(users_massages.user_receiver_id =" +username_send_id+ " OR users_massages.user_receiver_id = " + username_receive_id +") Order by massages_id";
		ArrayList<String[]> history_temp =  executeQuery(sql_query,true);
		return ConvertHistoryToUsersName(history_temp, username_send_id,username_receive_id, sender_name,receiver_name );
	}
	
	
	@Override
	public int getUserId(String username) throws SQLException {
		
		String sql_query ="Select user_id " + 
				"From users " +
				"Where users.user_name ='" + username +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		return checkID(result);
		
	}
		
	
	@Override
	public String getUserPassword(int userID) throws SQLException {
		
		String StringUserID= ConvertIntegerToString(userID);
		String sql_query ="Select password " + 
				"From users " +
				"Where users.user_id =" + StringUserID;
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		
		if (result == null)  
		{
			return null;
		}	
		if (result.size()==0)
		{
			return null;
		}
		
		
		return result.get(0)[0];

	}


	//////////////////////////////////////////////
	// 		General queries generic methods
	//////////////////////////////////////////////
	
	
	private String ConvertIntegerToString(int num)
	{
		return Integer.toString(num);
	}
	
	

	/**
	 * split the return columns of db
	 * @param Query
	 * @return  to String[col1,col2..]
	 */
	private String[] parseSelectQuery(String Query)
	{
			String SelectColtemp = Query.substring(Query.indexOf("Select") +7, Query.indexOf(" From"));
			String[] SelectCol = SelectColtemp.split(", ");
			return SelectCol;
	}
	
	
/**
 * 
 * @param rs
 * @param NumOfColInResult
 * @param SelectCol
 * @return Array list that consist of an attribute of select cols in an String[]  
 */
	private ArrayList<String[]> ReturnSelectQuery(ResultSet rs , int NumOfColInResult , String[] SelectCol)
	{
		
		ArrayList<String[]> List = new ArrayList<String[]>();
	
		try {
			while (rs.next() == true){
				String[] groupOfStrings=new String[NumOfColInResult];
				for (int i =0; i<NumOfColInResult; i++)
				{
					
					groupOfStrings[i]= rs.getString(SelectCol[i]);//same as select
					
				}
				List.add(groupOfStrings);
				
			}	
		} catch (SQLException e) {
				System.out.println("ERROR executeFailed - " + e.getMessage());
		}
		
		return List;
	}
	
	

	private  ArrayList<String[]> executeQuery(String sql_query,boolean is_Select) throws SQLException
	{
		System.out.println(sql_query);
		
		ArrayList<String[]> List = new ArrayList<String[]>();
		Statement stmt = null;
		ResultSet rs = null;
		boolean loopAround = true;
		Connection connection = null;
		
		
		//Try to get connection from pool
		try {
			connection = this.connectionPool.getConnectionFromPool();
			loopAround = false;
		} catch (SQLException e1) {
			// Connection is lost! Unable to get connection. Throw exception.
			System.out.println("executeQuery: Connection exception.");
			e1.printStackTrace();
			throw e1;
		}


		System.out.println("executeQuery: Got connection.");

		//Execute query
		try {

			stmt = connection.createStatement();
			
			if (is_Select)
			{
				rs = stmt.executeQuery(sql_query);
				String[] SelectCol = parseSelectQuery(sql_query);
				int  SelectColLength = SelectCol.length;
				List = ReturnSelectQuery(rs,SelectColLength,SelectCol);
			}
			else
			{
				stmt.executeUpdate(sql_query);
				
			}
						
		} catch (SQLException e) {
			
			List=null;
			System.out.println("ERROR execute_query_Failed - " + e.getMessage());
		
		} finally {
			this.connectionPool.returnConnectionToPool(connection);
			DataBaseManager.safelyClose(stmt, rs);
		}
		
	
		return List;
		
	}
	
	
	
	///////////////////////////////////////
	// 		Database manager code
	///////////////////////////////////////
	

	
	public boolean isDatabaseInitialized() throws SQLException{
		
		boolean status = false;
		
		Connection connection;
		try {
			connection = this.connectionPool.getConnectionFromPool();
		} catch (SQLException e) {
			// Connection is lost! Unable to get connection. Throw exception.
			e.printStackTrace();
			throw e;
		}
		
		status = DataBaseManager.checkDatabaseStatus(connection);

		this.connectionPool.returnConnectionToPool(connection);
		
		return status;
		
		
	}
	
	
	public boolean initializeDatabase(String yagoFilesPath) throws SQLException{
		
		boolean status = true;
		
		Connection connection;
		try {
			connection = this.connectionPool.getConnectionFromPool();
		} catch (SQLException e) {
			// Connection is lost! Unable to get connection. Throw exception.
			e.printStackTrace();
			throw e;
		}
		
		status = this.buildMusicDB(yagoFilesPath);
			
		this.connectionPool.returnConnectionToPool(connection);
		
		return status;
		
	}
	

	public boolean buildMusicDB(String yagoFilesPath) throws SQLException{
		
		Connection connection = null;
		try {
			connection = this.connectionPool.getConnectionFromPool();
		} catch (SQLException e) {
			// Connection is lost! Unable to get connection. Throw exception.
			e.printStackTrace();
			throw e;
		}
		
		boolean status = DataBaseManager.buildMusicDatabase(connection, yagoFilesPath);
		
		this.connectionPool.returnConnectionToPool(connection);
		
		return status;
		
	}
	
	
	public boolean updateMusicDB(String yagoFilesPath) throws SQLException{
		
		Connection connection;
		try {
			connection = this.connectionPool.getConnectionFromPool();
		} catch (SQLException e) {
			// Connection is lost! Unable to get connection. Throw exception.
			e.printStackTrace();
			throw e;
		}
		
		boolean status = DataBaseManager.updateMusicDatabase(connection, yagoFilesPath);
		
		this.connectionPool.returnConnectionToPool(connection);
		
		return status;
		
	}


	public void terminateConnectionToDB(){
		
		boolean status = this.connectionPool.closeConnectionPool();
		if(!status)
			System.out.println("Connection to database is lost. terminating application anyway.");
	}

	
	
	///////////////////////////////////////
	// 		Other help methods
	///////////////////////////////////////
	
	
	
	private String ConvertStringCharToLegal(String s)
	{	
		if (s == null)
			return s;
		String  ans = s.replaceAll("'","\\\\'");
		return ans;
	}
	
	
	private int checkID(ArrayList<String[]> arrayList)
	{
		if (arrayList == null)
		{
			return -1;
		}
		
		if (arrayList.size()==0)
		{
			return 0;
		}
		
		int song_id = Integer.parseInt(arrayList.get(0)[0]);
		return song_id;
		
	}
	


	private  ArrayList<String[]> ConvertHistoryToUsersName(ArrayList<String[]> history_temp,int username_send_id,
			int username_receive_id,String sender_name, String receiver_name ) {
		
		if (history_temp == null || history_temp.size() == 0)
			return history_temp;
		
		for (String[] s: history_temp)
		{
			int i = Integer.parseInt(s[0]);
			
			if ( i == username_send_id )
			{
				s[0] = sender_name;
				s[1] = receiver_name;
			}
				
			else if ( i == username_receive_id )
			{
				s[0] = receiver_name;
				s[1] = sender_name;
			}
		}
		return history_temp;
		
	}
	
	
	
}
