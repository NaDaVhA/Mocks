package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashMap;

import core.User;


public class dbActions implements databaseActions{

	private boolean qaqa = false;
	private ConnectionPool connectionPool;
	
	
	
	public dbActions(ConnectionPool pool){
		this.connectionPool = pool;
	}

	
	@Override
	public LinkedHashMap<String, String> getSingersList() {
		
		//Get connection from pool
		Connection connection = this.connectionPool.getConnectionFromPool();
		
		LinkedHashMap<String, String> singersList = new LinkedHashMap<String, String>();
		
		String selectAllBandsQuery = "SELECT singer, singer_classification " + 
				"FROM singers";
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(selectAllBandsQuery);
			
			while (rs.next() == true){
				String singerName = rs.getString("singer");
				String singerClass = rs.getString("singer_classification");
				
				System.out.println("Singer: " + rs.getString("singer") + ", Singer's category: " + rs.getString("singer_classification"));
				singersList.put(singerName, singerClass);
			}
			
			
		} catch (SQLException e) {
			System.out.println("ERROR getSingersList - " + e.getMessage());
		} finally {
			
			DataBaseManager.safelyClose(stmt, rs);
			
			//Return connection to pool
			this.connectionPool.returnConnectionToPool(connection);
		}
				
		return singersList;
		
	}


	@Override
	public Collection<String> getBandsList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<String> getSongsList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<User> getUsersList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getUserName(int userID) {
		// TODO Auto-generated method stub
		return null;
	}

	
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

	@Override
	public boolean addSongToDB() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean addArtistToDB() {
		// TODO Auto-generated method stub
		return false;
	}


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
	public boolean connectUser(int userID) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public LinkedHashMap<Integer, String> getUserSongList(int userID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getUserStatusSong(int userID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean setUserStatusSong(int userID, int songID) {
		
		String[] columns = {"status_song_id"};
		String[] values = {Integer.toString(songID)};
			
		String whereSentence = "WHERE userID = " + userID;
				
		return this.tableUpdate("users", columns, values, whereSentence);
	}


	@Override
	public String addSongToUserSongList(int userID) {
		// TODO Auto-generated method stub
		return null;
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


	


	
}
