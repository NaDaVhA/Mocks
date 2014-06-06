package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class dbActions implements DBActionsInterface{

	private boolean qaqa = false;
	private ConnectionPool connectionPool;

	//mmm
	//Constructor
	public dbActions(ConnectionPool cp){
		
		this.connectionPool = cp;
	}
	
	
	public ArrayList<String[]> getSongsList() throws SQLException{
		
		String sql_query ="Select song_name, song_id " + 
				"From songs";
		return executeQuery(sql_query,true); // true for  query is_select 
			
	}
	
	
	public int getSongID(String song_name) throws SQLException
	{
		String sql_query ="Select song_id " + 
				"From songs " +
				"Where songs.song_name = '" + song_name +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		
		if (result == null)
		{
			return -1;
		}
		
		if (result.size()==0)
		{
			return 0;
		}
		
		int song_id = Integer.parseInt(result.get(0)[0]);
		return song_id;
		
	}
	
	public ArrayList<String[]> getSongsArtistList() throws SQLException{
	
		String sql_query ="Select song_name, artist_name " + 
				"From songs,artist_song,artists " +
				"Where songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id";
		
	
		return executeQuery(sql_query,true);
			
	}
	
	public ArrayList<String[]> getSongsArtistList(String songname) throws SQLException{
		
		String sql_query ="Select song_name, artist_name " + 
				"From songs,artist_song,artists " +
				"Where songs.song_name LIKE " + '"' + '%'+ songname + '%'+  '"' + "And songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id";
	
		return executeQuery(sql_query,true);
			
	}
	
	public ArrayList<String[]> getArtistList(String artist_name) throws SQLException
	{
		String sql_query ="Select song_name, artist_name " + 
				"From artists, artist_song, songs " +
				"Where artists.artist_name LIKE " + '"' + '%'+ artist_name + '%'+  '"' +
				" And artists.artist_id = artist_song.artist_id And songs.song_id = artist_song.song_id";
		return executeQuery(sql_query,true);
		
	}
	

	public ArrayList<String[]> getUsersList() throws SQLException{
		
		String sql_query ="Select user_id, user_name, status_song_id " + 
				"From users ";	
		return executeQuery(sql_query,true); // true for  query is_select
		
	}
	
	
	//Users DB// need to check
	public boolean addSongToUser(String user_name, String song_name) throws SQLException{
		
		int user_id = getUserId(user_name);
		
		int song_id = getSongID(song_name);
		
		if ((user_id == -1) || (song_id == -1) || (user_id == 0) || (song_id == 0))
			return false;
		
		String sql_query ="INSERT INTO `user_songs` " +
				"(`user_id`, `song_id`) VALUES ('"+user_id+"', '"+song_id+"');";
			
		if (executeQuery(sql_query,false)==null)
			return false; // true for  query is_select
	
		return true;
	}
	
	// need to check 
	public boolean addArtistToUser(String user_id, String artist_id) throws SQLException{
		String sql_query ="INSERT INTO `project`.`user_artist` " +
				"(`user_id`, `artist_id`) VALUES ('"+user_id+"', '"+artist_id+"');";
		
		if (executeQuery(sql_query,false)==null)
			return false; // true for  query is_select
	
		return true;
		
	}
	
	//{null} if there is not match
	// need to check 
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
	//need to check
	public ArrayList<String[]> getUserFreindsList(int userID) throws SQLException
	{
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select user_name " + 
				"From users_freinds, users " +
				"Where users_freinds.user_id=" + StringUserID + " And users.user_id = users_freinds.user_freind_id "; 
	
		return executeQuery(sql_query,true);
		
	}

	@Override
	public boolean addFreindToUser(int userID, int userFreindID) throws SQLException
	{
				String sql_query ="INSERT INTO `users_freinds` " +
						"(`user_id`, `user_freind_id`) VALUES ('"+userID+"', '"+userFreindID+"');";
				if (executeQuery(sql_query,false)==null)
					return false; // true for  query is_select
				
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
	
	
	
	//needcheck
	//PORBLEM:  false on not - matching and also failurQuery..
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
			return false; // true for  query is_select
	
		return true;
	}
		
	//needcheck
	//PORBLEM:  false on not - matching and also failurQuery..
	@Override
	public boolean authenticateUser(String username, String password) throws SQLException {
		
		String sql_query ="Select user_name " + 
				"From users " +
				"Where users.user_name= '" + username + "' AND users.password=" + password;
		
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
	public ArrayList<String[]> getUserSongList(int userID) throws SQLException {
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select song_name, artist_name " + 
				"From user_songs, songs, artist_song, artists " +
				"Where user_songs.user_id=" + StringUserID + " And songs.song_id =user_songs.song_id And songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id"; 
		
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

	//if user isnt exist return also null
	@Override
	public String[] getUserStatusSong(int userID) throws SQLException {
		String StringUserID= ConvertIntegerToString(userID);
		
		
		String sql_query ="Select song_name, artist_name " + 
				"From users, songs , artist_song , artists " +
				"Where users.user_id=" + StringUserID + " And artist_song.song_id = users.status_song_id And songs.song_id = users.status_song_id And artists.artist_id = artist_song.artist_id "; 
		
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
	public boolean setUserStatusSong(int userID, int songID) throws SQLException {
		
		String sql_query ="UPDATE `users` " +
				" set status_song_id = " + songID 
				+ " where users.user_id =" + userID;
		if (executeQuery(sql_query,false)==null)
			return false; // true for  query is_select
		
	
		return true;
	}



	@Override
	public boolean disconnectUser(int userID) {
		// TODO Auto-generated method stub
		return false;
	}

	
	/**
	 * This method gets the userID corresponding to the given userName.
	 * @param userName
	 * @return userID if exists, -1 if no such user exists.
	 * @throws SQLException 
	 */
	@Override
	public int getUserId(String username) throws SQLException {
		
		String sql_query ="Select user_id " + 
				"From users " +
				"Where users.user_name ='" + username +"'";
		
		ArrayList<String[]> result= executeQuery(sql_query,true);
		
		if (result == null)  
		{
			return -1;
		}	
		if (result.size()==0)
		{
			return 0;
		}
		
		int user_id = Integer.parseInt(result.get(0)[0]);
		return user_id; // true for  query is_select 
		
	}
		
	
	//userID not exists  =  return null and also error
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


    /*
     *general for sql query 
     * 
     */
	
	
	private String ConvertIntegerToString(int num)
	{
		return Integer.toString(num);
	}
	
	
	//split columns of select
	private String[] parseSelectQuery(String Query)
	{
			String SelectColtemp = Query.substring(Query.indexOf("Select") +7, Query.indexOf(" From"));
			String[] SelectCol = SelectColtemp.split(", ");
			return SelectCol;
	}
	
	
	//mira
	//check what to do if there is not an add!!
	//return list by number of columns : if num col of select is 3 so list (a1,a2,a3,b1,b2,b3...) 
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
	
	
	
	/*( without select "*") 
	 *   null is error, else true
	 * */ 
	private  ArrayList<String[]> executeQuery(String sql_query,boolean is_Select) throws SQLException
	{
		System.out.println(sql_query);
		
		ArrayList<String[]> List = new ArrayList<String[]>();
		Statement stmt = null;
		ResultSet rs = null;
		boolean loopAround = true;
		Connection connection = null;
		
		while(loopAround){
			
			try {
				
				//Try to get connection from pool
				try {
					connection = this.connectionPool.getConnectionFromPool();
				} catch (SQLException e1) {
					// Connection is lost! Unable to get connection. Throw exception.
					e1.printStackTrace();
					throw e1;
				}
				
				
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
				
				loopAround = false;
				
			} catch (SQLException e) {
				
				List=null;
				System.out.println("ERROR execute_query_Failed - " + e.getMessage());
				
				if(connection.isValid(0)){
					loopAround = false;
				}
			
			} finally {
				this.connectionPool.returnConnectionToPool(connection);
				DataBaseManager.safelyClose(stmt, rs);
			}
				
		}
		
	
		return List;
		
	}
	
	
	
	///////////////////////////////////////
	// 		Database manager code
	///////////////////////////////////////
	
	
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
	

	/**
	 * Builds the music database.
	 * @param connection
	 * @param yagoFilesPath - path to the Yago files.
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException 
	 */
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
	
	
	/**
	 * Updates the music database.
	 * @param connection
	 * @param yagoFilesPath - path to the Yago files.
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException 
	 */
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
	
	
}
