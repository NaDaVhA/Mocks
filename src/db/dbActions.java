package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import core.User;


public class dbActions implements databaseActions{

	private boolean qaqa = false;
	private ConnectionPool connectionPool;
	//aaa

	public Collection<String> getSongsList(){
		
		String sql_query ="Select song_name, song_id " + 
				"From songs";
		return executeQuery(sql_query,true); // true for  query is_select 
			
	}
	
	public Collection<String> getSongsArtistList(){
	
		String sql_query ="Select song_name, artist_name " + 
				"From songs,artist_song,artists " +
				"Where songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id";
		
	
		return executeQuery(sql_query,true);
			
	}

	public List<String> getUsersList(){
		
		String sql_query ="Select user_id, user_name, status_song_id " + 
				"From users ";	
		return executeQuery(sql_query,true); // true for  query is_select
		
	}
	
	
	//Users DB// need to check
	public boolean addSongToUser(String user_id, String song_id){
		String sql_query ="INSERT INTO `project`.`user_songs` " +
				"(`user_id`, `song_id`) VALUES ('"+user_id+"', '"+song_id+"');";
			
		if (executeQuery(sql_query,false)==null)
			return false; // true for  query is_select
	
		return true;
	}
	
	// need to check 
	public boolean addArtistToUser(String user_id, String artist_id){
		String sql_query ="INSERT INTO `project`.`user_artist` " +
				"(`user_id`, `artist_id`) VALUES ('"+user_id+"', '"+artist_id+"');";
		
		if (executeQuery(sql_query,false)==null)
			return false; // true for  query is_select
	
		return true;
		
	}
	

	// need to check 
	public String getUserName(int userID) {

		
		String userId_as_String = ConvertIntegerToString(userID);
		
		String sql_query ="Select user_name, " + 
				"From users" +
				"Where users.user_id = " + userId_as_String;
		

		return  executeQuery(sql_query,true).get(0); // true for  query is_select 
	}
	
	
	
	
	
	//gil
	public dbActions(ConnectionPool pool){
		this.connectionPool = pool;
	}

	
	@Override
	public List<String> getSingersList() {
		
		String sql_query ="Select artist_id, artist_name " + 
				"From artists " +
				"Where artists.artist_type=1 ";
		return  executeQuery(sql_query,true);
		
	}


	@Override
	public Collection<String> getBandsList() {
		String sql_query ="Select artist_id, artist_name " + 
				"From artists " +
				"Where artists.artist_type=0 ";
		return  executeQuery(sql_query,true);
	}



	//gil
	@Override
	public boolean usernameExists(String username) {

		Connection connection = this.connectionPool.getConnectionFromPool();
		
		String usernameExistsQuery = "SELECT username FROM users WHERE username = '" + username + "'" ;
		
		Statement stmt = null;
		ResultSet rs = null;
		boolean usernameStatus = false;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(usernameExistsQuery);
			
			if(rs.next() == true)
				usernameStatus = true;
	
		} catch (SQLException e) {
			System.out.println("ERROR getUserPassword - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt, rs);
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return usernameStatus;
		
	}


	//gil
	@Override
	public boolean registerNewUser(String userName, String password) {
		
		String[] columns = {"username", "password"};
		String[] values = {userName, password};
		
		Connection connection = this.connectionPool.getConnectionFromPool();
		
		String registerAction = "INSERT INTO users(username, password) " + " VALUES('" + userName + "', '" + password + "')";
		
		Statement stmt = null;
		boolean status = true;

		try {
			stmt = connection.createStatement();
			status = stmt.execute(registerAction);
				
		} catch (SQLException e) {
			System.out.println("ERROR registerNewUser - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt);
			this.connectionPool.returnConnectionToPool(connection);
		}
		
		//return tableAction(1, "users", columns, values, null);
		return status;
				
	}


	//gil
	@Override
	public boolean authenticateUser(String username, String password) {

		Boolean userAuthorized = false;
		
		//Get user's ID
		int userID = this.getUserId(username);
		if(userID == -1){
			if(qaqa) System.out.println("authenticateUser: username unregistered");
			return userAuthorized;
		}
		
		//Match given password to user's valid password
		String validPassword = this.getUserPassword(userID);
		if(validPassword.compareTo(password) == 0)
			userAuthorized = true;
		
		return userAuthorized;
	}


	@Override
	public List<String> getUserSongList(int userID) {
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select song_name, artist_name " + 
				"From user_songs, songs, artist_song, artists " +
				"Where user_songs.user_id=" + StringUserID + " And songs.song_id =user_songs.song_id And songs.song_id = artist_song.song_id And artists.artist_id = artist_song.artist_id"; 
		
		return executeQuery(sql_query,true);
	}


	@Override
	public String getUserStatusSong(int userID) {
		String StringUserID= ConvertIntegerToString(userID);
		
		String sql_query ="Select song_name " + 
				"From users, songs " +
				"Where users.user_id=" + StringUserID + " And songs.song_id =users.status_song_id"; 
		
		return executeQuery(sql_query,true).get(0);
	}


	//gil
	@Override
	public boolean setUserStatusSong(int userID, int songID) {
		
		String[] columns = {"status_song_id"};
		String[] values = {Integer.toString(songID)};
			
		String whereSentence = "WHERE userID = " + userID;
				
		return this.tableUpdate("users", columns, values, whereSentence);
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
	 */
	@Override
	public int getUserId(String username) {

		Connection connection = this.connectionPool.getConnectionFromPool();
		
		String getPasswordQuery = "SELECT userID FROM users WHERE username LIKE '" + username + "'";
		
		Statement stmt = null;
		ResultSet rs = null;
		int userID = -1;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(getPasswordQuery);
			
			if(rs.next() == true)
				userID = rs.getInt("userID");
			
		} catch (SQLException e) {
			System.out.println("ERROR getUserId - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt, rs);
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return userID;
		
	}


	@Override
	public String getUserPassword(int userID) {

		Connection connection = this.connectionPool.getConnectionFromPool();
		
		String getPasswordQuery = "SELECT password FROM users WHERE userID = " + userID;
		
		Statement stmt = null;
		ResultSet rs = null;
		String userPassword = null;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(getPasswordQuery);
			
			if(rs.next() == true)
				userPassword = rs.getString("password");
			
		} catch (SQLException e) {
			System.out.println("ERROR getUserPassword - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt, rs);
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return userPassword;
	}


	//////////////////////////////////////////////
	// 		General queries generic methods
	//////////////////////////////////////////////
	
	public boolean tableInsertion(String table, String[] columns, String[] values, String whereSentence) {
		
		Connection connection = this.connectionPool.getConnectionFromPool();
		boolean status = true;
		
		
		return true;
		
	}
	
	
	/**
	 * Updates table.
	 * @param table
	 * @param columns
	 * @param values
	 * @return
	 */
	public boolean tableUpdate(String table, String[] columns, String[] values, String whereSentence) {

		Connection connection = this.connectionPool.getConnectionFromPool();
		boolean status = true;
		
		if(columns.length != values.length){
			if(qaqa) System.out.println("tableUpdate: columns.length != values.length");
			return false;
		}
		
		String updateTableQuery = "UPDATE " + table + " SET ";
		for(int i=0; i<columns.length; i++){
			updateTableQuery = updateTableQuery.concat(columns[i]).concat(" = ").concat(values[i]);
			if(i!=columns.length-1)
				updateTableQuery = updateTableQuery.concat(",");
		}
		
		updateTableQuery = updateTableQuery.concat(" " + whereSentence);
		
		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			status = stmt.execute(updateTableQuery);
		} catch (SQLException e) {
			System.out.println("ERROR tableUpdate - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt);
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return status;
	}


	/**
	 * Executes generic action on a given table.
	 * @param action - 1 for INSERT INTO, 2 for UPDATE
	 * @param table
	 * @param columns
	 * @param values
	 * @param whereSentence - SQL syntax for "where" addition. Can be null.
	 * @return true if action succeeded, false otherwise.
	 */
	public boolean tableAction(int action, String table, String[] columns, String[] values, String whereSentence) {

		Connection connection = this.connectionPool.getConnectionFromPool();
		boolean status = true;
		
		
		String actionStr = null;
		switch(action){
			case 1: 
				actionStr = "INSERT INTO " ;
				break;
			case 2: 
				actionStr = "UPDATE ";
				break;
			default:
				System.out.println("Ilegal action!");
				return false;
		}
		
		
		if(columns.length != values.length){
			if(qaqa) System.out.println("tableUpdate: columns.length != values.length");
			return false;
		}
		
		String actionTableQuery = actionStr + table + " ";
		if(action == 1)
			
		if(action == 2)
			actionTableQuery = actionTableQuery.concat("SET ");

		for(int i=0; i<columns.length; i++){
			actionTableQuery = actionTableQuery.concat(columns[i]).concat(" = ").concat(values[i]);
			if(i!=columns.length-1)
				actionTableQuery = actionTableQuery.concat(",");
		}
		
		if(whereSentence != null)
			actionTableQuery = actionTableQuery.concat(" " + whereSentence);
		
		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			status = stmt.execute(actionTableQuery);
		} catch (SQLException e) {
			System.out.println("ERROR tableUpdate - " + e.getMessage());
		} finally {
			DataBaseManager.safelyClose(stmt);
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return status;
	}

	
	
	

	@Override
	public boolean connectUser(int userID) {
		// TODO Auto-generated method stub
		return false;
	}




	
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
	
	//return list by number of columns : if num col of select is 3 so list (a1,a2,a3,b1,b2,b3...) 
	private List<String> ReturnSelectQuery(ResultSet rs , int NumOfColInResult , String[] SelectCol)
	{
		
		List<String> songsList = new ArrayList<String>();
		
		try {
			while (rs.next() == true){
				
				for (int i =0; i<NumOfColInResult; i++)
				{
					String stringCol = rs.getString(SelectCol[i]); //same as select
					songsList.add(stringCol);
				}
				
			}	
		} catch (SQLException e) {
				System.out.println("ERROR executeFailed - " + e.getMessage());
		}
		
		return songsList;
	}
	
	
	
	/*( without select "*") 
	 *   null is error, else true
	 * */ 
	private  List<String> executeQuery(String sql_query,boolean is_Select)
	{
		Connection connection = this.connectionPool.getConnectionFromPool();
		List<String> List = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql_query);
			if (is_Select)
			{
				String[] SelectCol = parseSelectQuery(sql_query);
				int  SelectColLength = SelectCol.length;
				List = ReturnSelectQuery(rs,SelectColLength,SelectCol);
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
	

	
}
