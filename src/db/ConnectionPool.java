package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {

	private Stack<Connection> pool;
	private Stack<Connection> unclosedConnections;
	private int availableConnectionsNumber;
	private int unclosedConnectionNumber;

	//A connection pool instance is associated with exactly one user and database pair.
	private String host;
	private String port;
	private String dataBase;
	private String dbUser;
	private String dbPassword;
	
	
	//Constructor
	
	public ConnectionPool(String hostname, String port, String dbName, String dbUser, String dbPassword){
		
		this.pool = new Stack<Connection>();
		this.availableConnectionsNumber = 0;
		
		this.host = hostname;
		this.port = port;
		this.dataBase = dbName;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		
		this.createNewConnectionInPool(this.host, this.port, this.dataBase, this.dbUser, this.dbPassword);

	}
	
	
	
	//////////////////////////////////////////////////////////
	// 		Connection methods - connect, disconnect
	//////////////////////////////////////////////////////////
	
	
	
	/**
	 * Opens a new connection to DB and adds it to the connection pool.
	 * @param host - the connection's host
	 * @param port - the connection's port
	 * @param dataBase - the name of the DB
	 * @param dbUser - the database user on whose behalf the connection is being made
	 * @param dbPassword - the user's password (This is not secure!)
	 * @return true if the connection was successfully set
	 */
	public boolean createNewConnectionInPool(String host, String port, String dataBase, String dbUser, String dbPassword) {

		// loading the driver
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver. Exiting application.");
			return false;
		}

		// creating the new connection		
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dataBase;
		
		Connection connection;
		
		try {
			
			//open connection
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			
			//Add connection to pool
			this.pool.push(connection);
			this.availableConnectionsNumber++;
		
		
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			int errorCode = e.getErrorCode();
			System.out.println("Error code = " + errorCode + ", SQLState = " + e.getSQLState());
			connection = null;
			
			return false;
		}
		
		return true;
	}

	
	/**
	 * Closes the given connection.
	 * @return true if connection was closed, false otherwise.
	 */
	public boolean closeConnection(Connection connection) {
		
		// closing the connection
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection - "
					+ e.getMessage());
			return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * Retrieves connection from connection pool. 
	 * Connection is removed from pool.
	 * if no connections are available, creates and Retrieves a new connection.
	 * @return a DataBaseConnection object.
	 */
	public Connection getConnectionFromPool(){
		
		if(this.availableConnectionsNumber == 0)
			this.createNewConnectionInPool(this.host, this.port, this.dataBase, this.dbUser, this.dbPassword);
		
		this.availableConnectionsNumber--;
		
		return this.pool.pop();
		
	};
	
	
	
	public Connection SAFEgetConnectionFromPool() throws SQLException{
		
		if(this.availableConnectionsNumber == 0)
			this.createNewConnectionInPool(this.host, this.port, this.dataBase, this.dbUser, this.dbPassword);
						
		Connection con = null;
		boolean validConnection = false;
		
		while(this.availableConnectionsNumber > 0){
			
			con = this.pool.pop();
			this.availableConnectionsNumber--;
			
			//Check whether connection is valid (using 10 seconds timeout)
			try {
				validConnection = con.isValid(10);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(validConnection){
				break;
			}else{
				//Try to close connection. If failed, insert connection to unclosedConnections and try again later.
				if(!this.closeConnection(con)){
					unclosedConnections.push(con);
					this.unclosedConnectionNumber++;
				};
				
			}
				
		}
		
		if(!validConnection)
			throw new SQLException("connectionLost");
		
		return con;
		
	};
	
	
	
	/**
	 * Returns connection to the connection pool
	 * @param dbConn
	 */
	public void returnConnectionToPool(Connection connection){
		
		this.pool.push(connection);

		this.availableConnectionsNumber++;
			
	};
	
	
	/**
	 * Closes all connections in connection pool.
	 * @return true if all connections are closed, false otherwise.
	 */
	public boolean closeConnectionPool(){
		
		boolean status = true;
		
		while(this.availableConnectionsNumber > 0){
			
			Connection con = this.getConnectionFromPool();
			
			//Try to close connection at most 5 times
			for(int i=0; i< 5; i++){
				status = this.closeConnection(con);
				if(status)
					break;
			}
			
			if(!status)
				break;
			
		}
		
		return status;
		
	}
	
	
}
