package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import parsing.RawDataUnit;
import parsing.yagoMiner;


public class DataBaseManager {
	
	
	private static boolean qaqa = true;	
	
	
	///////////////////////////////////////
	// 		Atomic database actions 
	///////////////////////////////////////
	
	
	/**
	 * Execute a general SQL Statement.
	 * @param connection
	 * @param statement - The query statement to execute
	 * @return true if no problem was encountered, false otherwise.
	 */
	private static boolean executeStatement(Connection connection, String statementToExecute){
		
		boolean status = true;
		
		Statement stmt = null;
		
		try {
			//Create a statement from the connection object
			stmt = connection.createStatement();
			
			//Execute statement 
			stmt.execute(statementToExecute);
			
		} catch (SQLException e) {
			
			System.out.println("executeStatement: Couldn't execute statement");
			e.printStackTrace();
			status = false;
			
		}finally{
			safelyClose(stmt);
		}
		
		return status;
	}
	
	
	/**
	 * Creates a new table in the DB.
	 * @param tableName - the table name to be created. //QAQA - add a check here? if table already exists?
	 * @param schema - Strings of the following format: COLUMN_NAME	TYPE[(SIZE)] - QAQA - is it all?
	 */
	public static void createTable(Connection connection, String tableName, String closingStatements , String... schema){
		
		int schemaSize = schema.length;
		
		//Create the "CREATE" statement
		String dbCreateTableAction = "CREATE TABLE IF NOT EXISTS " + tableName + "(";
		for(int i=0; i<schemaSize-1; i++){
			dbCreateTableAction = dbCreateTableAction.concat(schema[i]).concat(" ");
		}
		dbCreateTableAction = dbCreateTableAction.concat(schema[schemaSize-1]).concat(")");
		if(closingStatements!=null)
			dbCreateTableAction = dbCreateTableAction.concat(" " + closingStatements);
		
		Statement stmt = null;
		
		try {
			//Create a statement from the connection object
			stmt = connection.createStatement();
			
			//Execute statement (create the table...)
			stmt.execute(dbCreateTableAction);
			
		} catch (SQLException e) {
			System.out.println("createTable: Couldn't create statement");
			e.printStackTrace();
			System.out.println("Query: " + dbCreateTableAction);
		}finally{
			safelyClose(stmt);
		}
		
	}
	

	/**
	 * Drops table.
	 * @param tableName - The table to execute DROP on.
	 * @return true if succeeded, false otherwise.
	 */
	public static boolean dropTable(Connection connection, String tableName){
		
		boolean status = false;
		String dbDropTableAction = "DROP TABLE IF EXISTS " + tableName;
		Statement stmt = null;
		
		try {
			
			stmt = connection.createStatement();			
			status = stmt.execute(dbDropTableAction);

		} catch (SQLException e) {
			System.out.println("dropTable: Couldn't create statement");
			e.printStackTrace();
			return false;	//QAQA - should I even bother?
		}finally{
			safelyClose(stmt);
		}
		
		return status;
		
	}
	
	
	/**
	 * Attempts to close all the given resources, ignoring errors
	 * 
	 * @param resources
	 */
	public static void safelyClose(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			try {
				resource.close();
			} catch (Exception e) {
			}
		}
	}
	
	
	/**
	 * Execute the given statements as a complete transaction.
	 * @param conn
	 * @param statements
	 * @return false if failed, true if succeded.
	 */
	public static boolean executeTransaction(Connection conn, String[] statements) {
		
		Statement stmt = null;
		boolean status = false;
		
		try {
			conn.setAutoCommit(false);

			stmt = conn.createStatement();
			
			int numberOfStatements = statements.length;
			status = false;
			
			for(int i=0; i<numberOfStatements; i++){
				status = stmt.execute(statements[i]);
			}
			
			//Commit transaction
			if(qaqa) System.out.println("executeTransaction: Commiting transaction..");
			conn.commit();

		} catch (SQLException e) {
			System.out
					.println("We have an exception, transaction is not complete: Exception: "
							+ e.getMessage());
			try {
				conn.rollback();
				System.out.println("Rollback Successfully :)");
			} catch (SQLException e2) {
				System.out
						.println("ERROR demoTransactions (when rollbacking) - "
								+ e.getMessage());
			}
		} finally {
			safelyClose(stmt);
			safelySetAutoCommit(conn);
		}
		
		return status;
	}

	
	/**
	 * Attempts to set the connection back to auto-commit, ignoring errors.
	 * @param conn
	 */
	private static void safelySetAutoCommit(Connection conn) {
		try {
			conn.setAutoCommit(true);
		} catch (Exception e) {
		}
	}
	
	
	/**
	 * If connection is valid returns status, else throws SQLException informing that connection to database is lost (given connection is not valid).
	 * @param connection
	 * @param status
	 * @return status unless connection is not valid. 
	 * @throws SQLException
	 */
	private static boolean checkConnectionValidity(Connection connection, boolean status) throws SQLException {
		
		try {
			if(connection.isValid(3))
				return status;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return status;
		}
		
		//If connection is not valid, throw exception
		throw new SQLException("connectionLost");
	
	}
	
	
	
	//////////////////////////////////////////////////////////
	// 		Database modifiers - DB Creation & Maintenance
	//////////////////////////////////////////////////////////
	
	
	
	/**
	 * Inserts data into table by batches.
	 * @param connection
	 * @param tableName
	 * @param tableColumns
	 * @param valuesType
	 * @param data
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean populateTable(Connection connection, String tableName, String[] tableColumns, int[] valuesType, LinkedList<String[]> data){
		
		boolean status = true;
		
		//Prepare statement arguments
		int schemaSize = tableColumns.length;
		String tableSchema = "(";
		for(int i=0; i<schemaSize; i++){
			tableSchema = tableSchema.concat(tableColumns[i]);
			if(i!=schemaSize-1)
				tableSchema = tableSchema.concat(", ");
		}	
		tableSchema = tableSchema.concat(")");
		
		String dbInsertAction = "INSERT INTO " + tableName + tableSchema + " VALUES(";
		for(int i=0; i<schemaSize; i++){
			dbInsertAction = dbInsertAction.concat("?");
			if(i!=schemaSize-1)
				dbInsertAction = dbInsertAction.concat(", ");
		}
			
		
		dbInsertAction = dbInsertAction.concat(")");
		PreparedStatement pstmt = null;
		
		try {
			
			pstmt = connection.prepareStatement(dbInsertAction);
		
			//Begin batch insertion line-by-line
			Iterator<String[]> linesIter = data.iterator();
			int count = 0;
			int batch = 0;
			
			while(linesIter.hasNext()){
								
				String[] currentLine = linesIter.next();
				for(int i=0; i<schemaSize; i++){
					int valueType = valuesType[i];
					switch (valueType) {
		            case 1:  pstmt.setInt(i+1, Integer.parseInt(currentLine[i]));
		            		break;
		            case 2:  pstmt.setString(i+1, currentLine[i]);
                    		break;
		            default: pstmt.setString(i+1, currentLine[i]);
                    		break;
					}
				}
				
				pstmt.addBatch();
				count++;
				
				//Limit insertion - execute batch every 1000 entries
				if(count==1000){
					count = 0;
					pstmt.executeBatch();
					if(qaqa) System.out.println("populateTable: batch #" + ++batch);
				}else{
					if(!linesIter.hasNext()){
						pstmt.executeBatch();
						if(qaqa) System.out.println("populateTable: LAST batch #" + ++batch);
					}
				}
			}
				
			
			if(qaqa) System.out.println("Success - completed batchInsertionIntoDB");
						
		} catch (SQLException e) {
			System.out.println("ERROR populateTable - "
					+ e.getMessage());
			return false;
		}finally{
			safelyClose(pstmt);
		}
				
		return status;
	}

	
	/**
	 * Extracts lines corresponding to the given index and attribute value.
	 * @param rdu
	 * @param index
	 * @param attribute
	 * @return LinkedList<String[]> of lines.
	 */
	private static RawDataUnit extractRawLinesByAttribute(RawDataUnit source, int index, String attribute, RawDataUnit target){
				
		for(String[] line : source){
			if(line[index].contains(attribute))
				target.addLine(line);
		}
		
		return target;
		
	}
	

	/**
	 * 
	 * @param rdu
	 * @param itemIndex
	 * @param itemType - 0 for singer, 1 for musical group
	 * @return
	 */
	private static LinkedList<String[]> extractDataFromRDT(RawDataUnit rdu, int[] indices){
		
		System.out.println("Proccessing raw data from Yago. Please be patient.");
		
		LinkedList<String[]> resultDataStructure = new LinkedList<String[]>();
		int size = indices.length;
		int count = 0;
		
		for(String[] line : rdu){
			
			String[] array = new String[size];
			for(int i=0; i<size; i++){
				array[i] = line[indices[i]];
			}
			
			//Filter duplicates
			if(!resultDataStructure.contains(array))
				resultDataStructure.add(array);
			
			count++;
			
			if(count == 1000){
				count = 0;
				System.out.print(".");
			}
				
		}
		
		System.out.println();
		
		return resultDataStructure;
		
	}

	
	
	////////////////////////////////////
	// 		initialize app's database
	////////////////////////////////////
	

	
	/**
	 * Checks whether database is already initialized.
	 * @param connection
	 * @return true if database is already initialized, false otherwise.
	 */
	public static boolean getDatabaseInitializationStatus(Connection connection){
				
		//If table already exists, it will just skip creating table again
		//createTable(connection, "configuration", null, "parameter VARCHAR(45) NOT NULL, ", "dbStatus INT NOT NULL DEFAULT 0");
		
		PreparedStatement stmt = null;
		
		try {
			
			Statement tstmt = connection.createStatement();
			ResultSet rs = tstmt.executeQuery("SELECT * FROM configuration WHERE parameter = 'initialized'");
			int dbStatusValue = 99; //QAQA
			if(rs.next() == true)
				dbStatusValue = rs.getInt("dbStatus");
			
			if(dbStatusValue == 1)
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			safelyClose(stmt);
		}
		
		return false;
		
	}
	
	
	/**
	 * Sets database initialization status to true.
	 * @param connection
	 * @return true if succeeded, false otherwise.
	 */
	public static boolean setDatabaseStatusToInitialized(Connection connection){
		
		boolean status = true;
				
		PreparedStatement stmt = null;
		String insertaionAction = "UPDATE configuration SET dbStatus = 1 WHERE parameter LIKE 'initialized'";
		
		stmt = null;
		
		try {
			stmt = connection.prepareStatement(insertaionAction);
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			safelyClose(stmt);
		}
		
		return status;
		
	}
	

	/**
	 * Checks value of an attribute in configuration table.
	 * @param connection
	 * @param attribute - the requested attribute.
	 * @param tuppleName - the requested tuple to extract the value from.
	 * @return the requested value or -1 on failure.
	 */
	private static int checkConfiguration(Connection connection, String attribute, String tupleName){
		
		String statement = "SELECT `"+ attribute + "` FROM `configuration` WHERE `operation` = '" + tupleName + "'";

		Statement stmt = null;
		ResultSet rs = null;
		int value = -2;
		
		try {
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(statement);

			if(rs.next() == true)
				value = rs.getInt(attribute);
						
		} catch (SQLException e) {
			System.out.println("ERROR executeQuery - " + e.getMessage());
			return -1;
		} finally {
			safelyClose(rs, stmt);
		}
		
		return value;
	}
	
	
	/**
	 * Updates the value of the requested attribute in configuration table.
	 * @param connection
	 * @param attribute
	 * @param value
	 * @param tupleName
	 * @param tupleValue
	 * @return
	 */
	private static boolean updateConfiguration(Connection connection, String attribute, String value, String tupleName, String tupleValue){
		
		String statement = "UPDATE `configuration` SET `" + attribute + "` = " + value + " WHERE `" + tupleName + "` = '" + tupleValue + "'";
		boolean status = executeStatement(connection, statement);
		
		return status;
	}
	
		
	/**
	 * Insert data into the given primary table while updating table's status in configuration table.
	 * @param connection
	 * @param statementToExecute
	 * @param atribute - the requested attribute to check and update.
	 * @param tuple - the tupple to check and update.
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean insertPrimaryDataCycle(Connection connection, String statementToExecute, String atribute, String tuple) {
		
		boolean status = false;
		
		if(checkConfiguration(connection, atribute, tuple) == 0){
			status = executeStatement(connection, statementToExecute);
			if(status)
				status = updateConfiguration(connection, atribute, "1", "operation", tuple);
		}else{
			status = true;
		}

		return status;
	}
	
	
	
	////////////////////////////////////
	// 		Music database builder
	////////////////////////////////////

	
	
	/**
	 * Builds the music database tables.
	 * @param connection
	 * @param pathToYagoFiles - path to the folder that contains the yago files.
	 * @return
	 * @throws SQLException 
	 */
	public static boolean buildMusicDatabase(Connection connection, String pathToYagoFiles) throws SQLException{
		
		//Check database status
		int dbStatus = checkConfiguration(connection, "status", "general");
		if(dbStatus == 1)
			return true;
		
		boolean status = true;		
				
		System.out.println("Building music database. This might take more than a few minutes...");

		//Yago files' paths
		String yagoFactsFile = pathToYagoFiles + "yagoFacts.tsv";
		String YagoTransitiveTypeFile = pathToYagoFiles + "YagoTransitiveType.tsv";
		
		//Prepare data
		System.out.println("Extracting data from Yago. Please wait.");
		
		//Mine relevant data from Yago in one batch-
		String[] transitiveAttributes = {"_groups>", "_singers>", "_songs>"};
		RawDataUnit rawMusicData = yagoMiner.mineDataUnit(YagoTransitiveTypeFile, transitiveAttributes, 2);
		String[] factsAttributes = {"<created>"};
		RawDataUnit rawCreatorCreationsData = yagoMiner.mineDataUnit(yagoFactsFile, factsAttributes, 2);
		
		if((rawMusicData == null)||(rawCreatorCreationsData == null)){
			System.out.println("Data extraction failed. Check Yago files, or <YagoFolderPath> value correctness.");
			return false;
		}
		
		//Extract raw data from mined data - singers, musical groups, songs
		RawDataUnit rawSinger = new RawDataUnit();
		rawSinger = extractRawLinesByAttribute(rawMusicData, 2, "_singers>", rawSinger);
		RawDataUnit rawMusicalGroups = new RawDataUnit();
		rawMusicalGroups = extractRawLinesByAttribute(rawMusicData, 2, "_groups>", rawMusicalGroups);
		RawDataUnit rawSongs = new RawDataUnit();
		rawSongs = extractRawLinesByAttribute(rawMusicData, 2, "_songs>", rawSongs);
	
		//Build database
		
		//Artists data base creation
		status = buildArtistsTables(connection, rawSinger, rawMusicalGroups);
		if(!status)
			return checkConnectionValidity(connection, status);
		
		//Songs data base creation
		status = buildSongsTables(connection, rawSongs);
		if(!status)
			return checkConnectionValidity(connection, status);
		
		
		//Creator-Creations links data creation
		status = buildCreatorCreationsTables(connection, rawCreatorCreationsData);
		if(!status)
			return checkConnectionValidity(connection, status);
		
		status = updateConfiguration(connection, "status", "1", "operation", "general");
		if(!status)
			return checkConnectionValidity(connection, status);
		
		System.out.println("Database creation succeeded. Hooray!");
		
		return status;
		
	}


	/**
	 * Builds the artists tables: artists, categories_of_artists, artist_category. 
	 * @param connection
	 * @param rawSinger
	 * @param rawMusicalGroups
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean buildArtistsTables(Connection connection, RawDataUnit rawSinger, RawDataUnit rawMusicalGroups) {
		
		//Check configuration
		int dbStatus = checkConfiguration(connection, "artistsTables", "general");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Building the artists database.");
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		
		//Add generic artist name
		String[] unknownArtist = {"<unknownartist>", "garbage", "<unknowncategory>"};
		rawSinger.addLine(unknownArtist);
		
		
		//Temporary data extraction and construction
		dbStatus = checkConfiguration(connection, "artists_temp", "general");
		if(dbStatus == 0){
			
			//Prepare data
			int[] indices = {0,2,0};
			LinkedList<String[]> sigersCategories = extractDataFromRDT(rawSinger, indices);
			LinkedList<String[]> musicalGroupsCategories = extractDataFromRDT(rawMusicalGroups, indices);
			
			//Filter duplicates from musicalGroupsCategories and sigersCategories
			HashSet<String> singers = new HashSet<String>();
			for(String[] singer : sigersCategories)
				singers.add(singer[0]);
			
			Iterator<String[]> iter = musicalGroupsCategories.iterator();
			while(iter.hasNext()){
				String[] tupple = iter.next();
				String group = tupple[0];
				if(singers.contains(group))
					iter.remove();
			}
					
			for(String[] arr: sigersCategories)
				arr[2] = "0";
			
			for(String[] arr: musicalGroupsCategories)
				arr[2] = "1";
			
			//Create temporary table: artists_temp (includes duplicates)
			createTable(connection, "artists_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artists_temp(artist_name, artist_category, artist_type)'", 
				"artist_name varchar(100) NOT NULL, ",
				"artist_category varchar(100) NOT NULL, ",
				"artist_type BIT NOT NULL");
			
		
			//populate artists_temp table with Singers
			tableColumns = new String[3];
			tableColumns[0] = "artist_name";
			tableColumns[1] = "artist_category";
			tableColumns[2] = "artist_type";
			types = new int[3];
			types[0] = 2;
			types[1] = 2;
			types[2] = 1;
			
			dbStatus = checkConfiguration(connection, "artists_temp_after_singers", "general");
			if(dbStatus == 0){
				
				status = populateTable(connection, "artists_temp", tableColumns, types, sigersCategories);
				if(!status) return false;
				status = updateConfiguration(connection, "artists_temp_after_singers", "1", "operation", "general");
				if(!status) return false;
			}
				
			dbStatus = checkConfiguration(connection, "artists_temp", "general");
			if(dbStatus == 0){
				status = populateTable(connection, "artists_temp", tableColumns, types, musicalGroupsCategories);
				if(!status) return false;
				status = updateConfiguration(connection, "artists_temp", "1", "operation", "general");
				if(!status) return false;
			}
		}
		System.out.println("Artist database: Completed step 1/5.");
		
		//populate artists table
		statementToExecute = "INSERT INTO artists(artist_name, artist_type) "
				+ "SELECT DISTINCT artist_name, artist_type "
				+ "FROM artists_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artists", "general");
		if(!status) return false;
		System.out.println("Artist database: Completed step 2/5.");

		
		//populate categories_of_artists table
		statementToExecute = "INSERT INTO categories_of_artists(category_name) "
				+ "SELECT DISTINCT artist_category "
				+ "FROM artists_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "categories_of_artists", "general");
		if(!status) return false;	
		System.out.println("Artist database: Completed step 3/5.");

		
		//populate artist_category_temp table
		//Create temp table: artist_category_temp
		createTable(connection, "artist_category_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_category(artist, category)'", 
				"artist varchar(100) NOT NULL, ",
				"category varchar(100) NOT NULL, ",
				"PRIMARY KEY (artist, category)");
		
		statementToExecute = "INSERT INTO artist_category_temp(artist, category) "
				+ "SELECT DISTINCT artist_name, artist_category "
				+ "FROM artists_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_category_temp", "general");
		if(!status) return false;
		System.out.println("Artist database: Completed step 4/5.");

		//populate artist_category table
		statementToExecute = "INSERT INTO artist_category(artist_id, category_id) "
				+ "SELECT DISTINCT Artist.artist_id, Category.category_artist_id "
				+ "FROM artist_category_temp AS Temp, artists AS Artist, categories_of_artists AS Category "
				+ "WHERE Temp.artist = Artist.artist_name  AND  Temp.category = Category.category_name "
				+ "LIMIT 2000";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_category", "general");
		if(!status) return false;
		System.out.println("Artist database: Completed step 5/5.");

		//Update configuration - finished artistsTables
		status = updateConfiguration(connection, "artistsTables", "1", "operation", "general");
		
		//Drop artist_category_temp table
		dropTable(connection, "artists_temp");
		updateConfiguration(connection, "artists_temp", "0", "operation", "general");
		dropTable(connection, "artist_category_temp");
		updateConfiguration(connection, "artist_category_temp", "0", "operation", "general");
		
		return status;
	
	}
	
	
	/**
	 * Builds the following tables:
	 * songs, categories_of_songs, song_category.
	 * @param connection
	 * @param rawSongs
	 * @return true if no problems were encountered, false otherwise.
	 */
	private static boolean buildSongsTables(Connection connection, RawDataUnit rawSongs) {
		
		//Check configuration
		int dbStatus = checkConfiguration(connection, "songsTables", "general");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Building the songs database. Please wait...");
		
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		boolean status;

		
		//Temporary data extraction and construction
		dbStatus = checkConfiguration(connection, "song_category_temp", "general");
		if(dbStatus == 0){
			
			//Extract song-category data from RDU
			int[] indices = {0,2};

			LinkedList<String[]> songsAndCategories = extractDataFromRDT(rawSongs, indices);
			
			//Create temporary table: song_category_temp
			createTable(connection, "song_category_temp", 
					"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='song_category_temp(song, category)'", 
					"song varchar(100) NOT NULL, ",
					"category varchar(100) NOT NULL");
			
			//populate songs_categories_temp with songs-categories
			tableColumns = new String[2];
			tableColumns[0] = "song";
			tableColumns[1] = "category";
			types = new int[2];
			types[0] = 2;
			types[1] = 2;
			
			status = populateTable(connection, "song_category_temp", tableColumns, types, songsAndCategories);
			if(!status) return false;
			status = updateConfiguration(connection, "song_category_temp", "1", "operation", "general");
			if(!status) return false;
		}		
		System.out.println("Songs database: Completed step 1/5.");

		//Create temporary table: song_category_distinct
		createTable(connection, "song_category_distinct", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='song_category_distinct(song, category)'", 
				"song varchar(100) NOT NULL, ",
				"category varchar(100) NOT NULL, ",
				"PRIMARY KEY (song, category)");
		
		//populate song_category_distinct with songs-categories distinct values
		statementToExecute = "INSERT INTO song_category_distinct(song, category) "
				+ "SELECT DISTINCT song, category "
				+ "FROM song_category_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "song_category_distinct", "general");
		//status = executeStatement(connection, statementToExecute);		
		if(!status) return false;
		System.out.println("Songs database: Completed step 2/5.");

		//populate songs table
		statementToExecute = "INSERT INTO songs(song_name) "
				+ "SELECT DISTINCT song "
				+ "FROM song_category_distinct";
		status = insertPrimaryDataCycle(connection, statementToExecute, "songs", "general");
		if(!status) return false;
		System.out.println("Songs database: Completed step 3/5.");

		
		//Categories of songs - 
		
		//populate categories_of_songs table
		statementToExecute = "INSERT INTO categories_of_songs(category_name) "
				+ "SELECT DISTINCT category "
				+ "FROM song_category_distinct";
		status = insertPrimaryDataCycle(connection, statementToExecute, "categories_of_songs", "general");
		if(!status) return false;
		System.out.println("Songs database: Completed step 4/5.");

		//populate song_category table
		statementToExecute = "INSERT INTO song_category(song_id, category_id) "
				+ "SELECT DISTINCT Song.song_id, Category.category_id "
				+ "FROM songs AS Song, categories_of_songs as Category, song_category_distinct AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.category = Category.category_name";
		status = insertPrimaryDataCycle(connection, statementToExecute, "song_category", "general");
		if(!status) return false;
		System.out.println("Songs database: Completed step 5/5.");

		//Update configuration - finished songsTables
		status = updateConfiguration(connection, "songsTables", "1", "operation", "general");
		
		//Drop temp tables: song_category_temp, song_category_distinct
		dropTable(connection, "song_category_temp");
		updateConfiguration(connection, "song_category_temp", "0", "operation", "general");
		dropTable(connection, "song_category_distinct");
		updateConfiguration(connection, "song_category_distinct", "0", "operation", "general");
		
		return status;
	}
	
	
	/**
	 * Builds the CreatorCreations table which holds all the links between our DB's songs and artists.
	 * @param connection
	 * @param rawCreatorCreationsData
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean buildCreatorCreationsTables(Connection connection, RawDataUnit rawCreatorCreationsData) {
		
		//Check configuration
		int dbStatus = checkConfiguration(connection, "creatorCreationTables", "general");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Finishing the database creation. Hang on a while longer!");
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		
		
		//Temporary data extraction and construction
		dbStatus = checkConfiguration(connection, "artist_song_temp", "general");
		if(dbStatus == 0){
			
			//Prepare data
			int[] indices = {1,3};
			LinkedList<String[]> creatorCreation = extractDataFromRDT(rawCreatorCreationsData, indices);
			
			//Create temporary table: artist_song_temp
			createTable(connection, "artist_song_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_song_temp(artist, song)'", 
				"artist varchar(100) NOT NULL, ",
				"song varchar(100) NOT NULL");
		
			//populate artist_song_temp with artist-songs
			tableColumns = new String[2];
			tableColumns[0] = "artist";
			tableColumns[1] = "song";
			types = new int[2];
			types[0] = 2;
			types[1] = 2;
			status = populateTable(connection, "artist_song_temp", tableColumns, types, creatorCreation);
			if(!status) return false;
			
		}
		System.out.println("Creations database: Completed step 1/5.");

		//Populate DISTINCT values
		createTable(connection, "artist_song_temp_distinct", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_song_temp(artist, song)'", 
				"artist varchar(100) NOT NULL, ",
				"song varchar(100) NOT NULL, ", 
				"PRIMARY KEY (artist, song) ");
		
		statementToExecute = "INSERT INTO artist_song_temp_distinct(artist, song) "
				+ "SELECT DISTINCT artist, song "
				+ "FROM artist_song_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_song_temp_distinct", "general");
		if(!status) return false;
		System.out.println("Creations database: Completed step 2/5.");

		//Populate artist_song table
		statementToExecute = "INSERT INTO artist_song(song_id, artist_id) "
				+ "SELECT DISTINCT Song.song_id, Artist.artist_id "
				+ "FROM artists as Artist, songs AS Song, artist_song_temp_distinct AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.artist = Artist.artist_name";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_song", "general");
		if(!status) return false;
		System.out.println("Creations database: Completed step 3/5.");

		
		//Add unknown connections
		
		//Create temp table: unknown_song_id
		createTable(connection, "unknown_song_id", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='unknown_song_id(song_id)'", 
				"song_id int NOT NULL, ",
				"PRIMARY KEY (song_id) ");
		
		//Populate unknown_song_id table
		statementToExecute = "INSERT INTO unknown_song_id(song_id) "
				+ "SELECT DISTINCT song_id "
				+ "FROM songs "
				+ "WHERE song_id NOT IN (SELECT DISTINCT song_id FROM artist_song)";
		status = insertPrimaryDataCycle(connection, statementToExecute, "unknown_song_id", "general");
		if(!status) return false;
		System.out.println("Creations database: Completed step 4/5.");

		//Add songs with no artists to artist_song table
		statementToExecute = "INSERT INTO artist_song(song_id, artist_id) "
				+ "SELECT DISTINCT Song.song_id, UnknownArtist.artist_id "
				+ "FROM songs AS Song, artists AS UnknownArtist "
				+ "WHERE Song.song_id NOT IN (SELECT DISTINCT song_id FROM artist_song) "
				+ "AND UnknownArtist.artist_name = '<unknownartist>'";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_song_additions", "general");
		if(!status) return false;
		System.out.println("Creations database: Completed step 5/5.");
		
		//Update configuration - finished CreatorCreationsTables
		status = updateConfiguration(connection, "creatorCreationTables", "1", "operation", "general");
		if(!status) return false;
		
		//Drop temp tables: artist_song_temp, artist_song_temp_distinct, unknown_song_id and update configuration
		dropTable(connection, "artist_song_temp");
		updateConfiguration(connection, "artist_song_temp", "0", "operation", "general");
		dropTable(connection, "artist_song_temp_distinct");
		updateConfiguration(connection, "artist_song_temp_distinct", "0", "operation", "general");
		dropTable(connection, "unknown_song_id");
		updateConfiguration(connection, "unknown_song_id", "0", "operation", "general");

		return status;
	}

	
	
	////////////////////////////////////
	// 		Music database updater
	////////////////////////////////////
	
	
	
	/**
	 * Updates music database.
	 * Leaves the old database unchanged and only inserts new information.
	 * If one stage gets error, continues updating the rest anyway.
	 * @param connection
	 * @param pathToYagoFiles
	 * @return true if succeeded, false otherwise.
	 * @throws SQLException 
	 */
	public static boolean updateMusicDatabase(Connection connection, String pathToYagoFiles) throws SQLException{
		
		int dbStatus = checkConfiguration(connection, "status", "updateOp");
		if(dbStatus == 1)	
			return true;
		
		boolean status = true;	
		
		System.out.println("Updating music database. This might take more than a few minutes...");

		//Yago files' paths
		String yagoFactsFile = pathToYagoFiles + "yagoFacts.tsv";
		String YagoTransitiveTypeFile = pathToYagoFiles + "YagoTransitiveType.tsv";
		
		//Prepare data
		
		//Mine relevant data from Yago in one batch-
		String[] transitiveAttributes = {"_groups>", "_singers>", "_songs>"};
		RawDataUnit rawMusicData = yagoMiner.mineDataUnit(YagoTransitiveTypeFile, transitiveAttributes, 2);
		String[] factsAttributes = {"<created>"};
		RawDataUnit rawCreatorCreationsData = yagoMiner.mineDataUnit(yagoFactsFile, factsAttributes, 2);
		
		if((rawMusicData == null)||(rawCreatorCreationsData == null)){
			System.out.println("Data extraction failed. Check Yago files, or <UpdateYagoFolderPath> value correctness.");
			return false;
		}
		
		
		//Extract raw data from Yago - singers, musical groups, songs
		RawDataUnit rawSinger = new RawDataUnit();
		rawSinger = extractRawLinesByAttribute(rawMusicData, 2, "_singers>", rawSinger);
		RawDataUnit rawMusicalGroups = new RawDataUnit();
		rawMusicalGroups = extractRawLinesByAttribute(rawMusicData, 2, "_groups>", rawMusicalGroups);
		RawDataUnit rawSongs = new RawDataUnit();
		rawSongs = extractRawLinesByAttribute(rawMusicData, 2, "_songs>", rawSongs);
	
		//Update database
		System.out.println("Finished extracting data from yago. Updating tables...");

		//Update artists data base 
		status = updateArtistsTable(connection, rawSinger, rawMusicalGroups);
		if(!status)
			return checkConnectionValidity(connection, status);
		
		//Update songs data base 
		status = updateSongsTables(connection, rawSongs);
		if(!status) return false;

		//Update creator-Creations links data 
		status = updateCreatorCreationsTable(connection, rawCreatorCreationsData);
		if(!status) return false;
	
		//Initialize configuration table - update values !!!  QAQA 
		status = initializeConfigurationTuple(connection, "updateOp");
		if(!status)
			return checkConnectionValidity(connection, status);
		
		System.out.println("Finished updating the database.");
		
		return status;
		
	}

	
	/**
	 * Updates the links between artists and songs.
	 * @param connection
	 * @param rawCreatorCreationsData
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean updateCreatorCreationsTable(Connection connection, RawDataUnit rawCreatorCreationsData) {
		
		//Check configuration
		int dbStatus = checkConfiguration(connection, "creatorCreationTables", "updateOp");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Finishing the database update proccess. Hang on a while longer!");
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		
		if(checkConfiguration(connection, "artist_song_temp", "updateOp") == 0){
			
			//Prepare data
			int[] indices = {1,3};
			LinkedList<String[]> updateCreatorCreation = extractDataFromRDT(rawCreatorCreationsData, indices);
			
			//Create temporary table: update_artist_song_temp
			createTable(connection, "update_artist_song_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_song_temp(artist, song)'", 
				"artist varchar(100) NOT NULL, ",
				"song varchar(100) NOT NULL");
		
			//populate artist_song_temp with artist-songs
			tableColumns = new String[2];
			tableColumns[0] = "artist";
			tableColumns[1] = "song";
			types = new int[2];
			types[0] = 2;
			types[1] = 2;
			
			status = populateTable(connection, "update_artist_song_temp", tableColumns, types, updateCreatorCreation);
			if(status){
				status = updateConfiguration(connection, "artist_song_temp", "1", "operation", "updateOp");
			}else{
				return false;
			}
			
		}
		System.out.println("Update creations database: Completed step 1/4.");
		
		//Populate DISTINCT values
		createTable(connection, "update_artist_song_distinct", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_song_distinct(artist, song)'", 
				"artist varchar(100) NOT NULL, ",
				"song varchar(100) NOT NULL, ", 
				"PRIMARY KEY (artist, song) ");
		
		statementToExecute = "INSERT INTO update_artist_song_distinct(artist, song) "
				+ "SELECT DISTINCT artist, song "
				+ "FROM update_artist_song_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artist_song_distinct", "updateOp");
		if(!status) return false;
		System.out.println("Update creations database: Completed step 2/4.");

		//Create temp table: update_artist_song_id
		createTable(connection, "update_artist_song_id", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_song_id(song_id, artist_id)'", 
				"song_id int NOT NULL, ",
				"artist_id int NOT NULL");
		
		//Add new entries to update_artist_song_id table (including duplications).
		statementToExecute = "INSERT INTO update_artist_song_id(song_id, artist_id) "
				+ "SELECT DISTINCT Song.song_id, Artist.artist_id "
				+ "FROM artists as Artist, songs AS Song, update_artist_song_distinct AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.artist = Artist.artist_name";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artist_song_id_update", "updateOp");
		if(!status) return false;
		
		//Add new entries to update_artist_song_id table from artist_song
		statementToExecute = "INSERT INTO update_artist_song_id(song_id, artist_id) "
				+ "SELECT DISTINCT song_id, artist_id "
				+ "FROM artist_song";
		status = executeStatement(connection, statementToExecute);
		if(!status) return false;
				
		//"Clean" artist_song table
		statementToExecute = "DELETE from artist_song WHERE artist_id > 0";
		status = executeStatement(connection, statementToExecute);
		if(!status) return false;
		
		//Update artist_song table
		statementToExecute = "INSERT INTO artist_song(song_id, artist_id) "
				+ "SELECT DISTINCT song_id, artist_id "
				+ "FROM update_artist_song_id";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_song", "updateOp");
		if(!status) return false;
		System.out.println("Update creations database: Completed step 3/4.");

		
		//Update unknown connections
		//Add songs with no artists to artist_song table
		statementToExecute = "INSERT INTO artist_song(song_id, artist_id) "
				+ "SELECT DISTINCT Song.song_id, UnknownArtist.artist_id "
				+ "FROM songs AS Song, artists AS UnknownArtist "
				+ "WHERE Song.song_id NOT IN (SELECT DISTINCT song_id FROM artist_song) "
				+ "AND UnknownArtist.artist_name = '<unknownartist>'";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_song_additions", "updateOp");
		if(!status) return false;
		System.out.println("Update creations database: Completed step 4/4.");
		
		//Update configuration - finished updating creatorCreationTables
		status = updateConfiguration(connection, "creatorCreationTables", "1", "operation", "updateOp");
		
		//Drop temp tables: update_artist_song_temp, update_artist_song_temp_distinct
		dropTable(connection, "update_artist_song_temp");
		updateConfiguration(connection, "update_artist_song_temp", "0", "operation", "updateOp");
		dropTable(connection, "update_artist_song_distinct");
		updateConfiguration(connection, "update_artist_song_distinct", "0", "operation", "updateOp");
		dropTable(connection, "update_artist_song_id");
		updateConfiguration(connection, "update_artist_song_id_update", "0", "operation", "updateOp");

		
		return status;
	}


	/**
	 * Updates the songs tables: songs, categories_of_songs.
	 * @param connection
	 * @param rawSongs
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean updateSongsTables(Connection connection, RawDataUnit rawSongs) {

		//Check configuration
		int dbStatus = checkConfiguration(connection, "songsTables", "updateOp");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Updating the songs database. Please wait...");
		
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		boolean status;
		
		//Temporary data extraction and construction
		dbStatus = checkConfiguration(connection, "song_category_temp", "updateOp");
		if(dbStatus == 0){
			
			//Extract song-category data from RDU
			int[] indices = {0,2};
	
			LinkedList<String[]> songsAndCategories = extractDataFromRDT(rawSongs, indices);
			
			//Create temporary tables: update_song_category_distinct, update_song_category_temp
			createTable(connection, "update_song_category_distinct", 
					"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_song_category_distinct(song, category)'", 
					"song varchar(100) NOT NULL, ",
					"category varchar(100) NOT NULL, ",
					"PRIMARY KEY (song, category)");
			
			createTable(connection, "update_song_category_temp", 
					"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_song_category_temp(song, category)'", 
					"song varchar(100) NOT NULL, ",
					"category varchar(100) NOT NULL");
			
			//populate update_song_category_temp with songs-categories (might have some duplicate tupples)
			tableColumns = new String[2];
			tableColumns[0] = "song";
			tableColumns[1] = "category";
			types = new int[2];
			types[0] = 2;
			types[1] = 2;
			status = populateTable(connection, "update_song_category_temp", tableColumns, types, songsAndCategories);
			if(!status) return false;
			status = updateConfiguration(connection, "update_song_category_temp", "1", "operation", "updateOp");
			if(!status) return false;	
		}
		System.out.println("Update songs database: Completed step 1/6.");

		//populate update_song_category_distinct with songs-categories distinct values
		statementToExecute = "INSERT INTO update_song_category_distinct(song, category) "
				+ "SELECT DISTINCT song, category "
				+ "FROM update_song_category_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_song_category_distinct", "updateOp");
		if(!status) return false;
		System.out.println("Update songs database: Completed step 2/6.");

		//Update songs table
		statementToExecute = "INSERT INTO songs(song_name) "
				+ "SELECT DISTINCT Temp.song "
				+ "FROM update_song_category_distinct AS Temp "
				+ "WHERE Temp.song NOT IN (SELECT DISTINCT song_name FROM songs)";
		status = insertPrimaryDataCycle(connection, statementToExecute, "songs", "updateOp");
		if(!status) return false;
		System.out.println("Update songs database: Completed step 3/6.");

		
		//Categories of songs update - 
		
		//Create temporary table: update_songs_categories_distinct
		createTable(connection, "update_categories_of_songs_distinct", 
			"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_categories_of_songs_distinct(category_name)'", 
			"category_name varchar(100) NOT NULL");

		//populate update_categories_of_songs_distinct table
		statementToExecute = "INSERT INTO update_categories_of_songs_distinct(category_name) "
				+ "SELECT DISTINCT category "
				+ "FROM update_song_category_distinct";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_categories_of_songs_distinct", "updateOp");
		if(!status) return false;
		
		//Update categories_of_songs table
		statementToExecute = "INSERT INTO categories_of_songs(category_name) "
				+ "SELECT DISTINCT Temp.category_name "
				+ "FROM update_categories_of_songs_distinct AS Temp "
				+ "WHERE Temp.category_name NOT IN (SELECT category_name FROM categories_of_songs)";
		status = insertPrimaryDataCycle(connection, statementToExecute, "categories_of_songs", "updateOp");
		if(!status) return false;
		System.out.println("Update songs database: Completed step 4/6.");
		
		
		// Song-Category update - 
		
		//Create temp table: update_artist_category_id
		createTable(connection, "update_song_category_id", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_category_id(song_id, category_id)'", 
				"song_id int NOT NULL, ",
				"category_id int NOT NULL, ",
				"PRIMARY KEY (song_id, category_id)");
		
		//populate update_song_category_id
		statementToExecute = "INSERT INTO update_song_category_id(song_id, category_id) "
				+ "SELECT DISTINCT Song.song_id, Category.category_id "
				+ "FROM update_song_category_distinct AS Temp, songs AS Song, categories_of_songs AS Category "
				+ "WHERE Temp.song = Song.song_name AND Temp.category = Category.category_name "
				+ "LIMIT 2000";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_song_category_id", "updateOp");
		if(!status) return false;
		System.out.println("Update songs database: Completed step 5/6.");


		//Update song_category table
		statementToExecute = "INSERT INTO song_category(song_id, category_id) "
				+ "SELECT DISTINCT Temp.song_id, Temp.category_id "
				+ "FROM update_song_category_id AS Temp "
				+ "WHERE NOT EXISTS (SELECT * "
				+ "FROM song_category AS Old "
				+ "WHERE Temp.song_id = Old.song_id AND Temp.category_id = Old.category_id) "
				+ "LIMIT 2000";
		status = insertPrimaryDataCycle(connection, statementToExecute, "song_category", "updateOp");
		if(!status) return false;
		System.out.println("Update songs database: Completed step 6/6.");

		//Update configuration - finished updating songsTables
		status = updateConfiguration(connection, "songsTables", "1", "operation", "updateOp");
		
		//Drop temp tables and update configuration
		dropTable(connection, "update_song_category_distinct");
		updateConfiguration(connection, "update_song_category_distinct", "0", "operation", "updateOp");
		dropTable(connection, "update_song_category_temp");
		updateConfiguration(connection, "update_song_category_temp", "0", "operation", "updateOp");
		dropTable(connection, "update_categories_of_songs_distinct");
		updateConfiguration(connection, "update_categories_of_songs_distinct", "0", "operation", "updateOp");
		dropTable(connection, "update_song_category_id");
		updateConfiguration(connection, "update_song_category_id", "0", "operation", "updateOp");

		return status;
		
	}


	/**
	 * Updates the artists tables: artists, categories_of_artists.
	 * @param connection
	 * @param rawSinger
	 * @param rawMusicalGroups
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean updateArtistsTable(Connection connection, RawDataUnit rawSinger, RawDataUnit rawMusicalGroups) {

		//Check configuration
		int dbStatus = checkConfiguration(connection, "artistsTables", "updateOp");
		if(dbStatus == 1)	
			return true;
		
		System.out.println("Updating the artists database...");
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
	
		//Temporary data extraction and construction
		dbStatus = checkConfiguration(connection, "artist_song_temp", "updateOp");
		if(dbStatus == 0){
			
			//Create temporary tables: update_artists_temp (includes duplicates), update_artists_distinct
			createTable(connection, "update_artists_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artists_temp(artist_name, artist_category, artist_type)'", 
				"artist_name varchar(100) NOT NULL, ",
				"artist_category varchar(100) NOT NULL, ",
				"artist_type BIT NOT NULL");
			
			createTable(connection, "update_artists_distinct", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artists_distinct(artist_name, artist_type)'", 
				"artist_name varchar(100) NOT NULL, ",
				"artist_type BIT NOT NULL");
			
			//Prepare data
			int[] indices = {0,2,0};
			LinkedList<String[]> sigersCategories = extractDataFromRDT(rawSinger, indices);
			LinkedList<String[]> musicalGroupsCategories = extractDataFromRDT(rawMusicalGroups, indices);
			
			//Filter duplicates from musicalGroupsCategories and sigersCategories
			HashSet<String> singers = new HashSet<String>();
			for(String[] singer : sigersCategories)
				singers.add(singer[0]);
			
			Iterator<String[]> iter = musicalGroupsCategories.iterator();
			while(iter.hasNext()){
				String[] tupple = iter.next();
				String group = tupple[0];
				if(singers.contains(group))
					iter.remove();
			}
					
			for(String[] arr: sigersCategories)
				arr[2] = "0";
			
			for(String[] arr: musicalGroupsCategories)
				arr[2] = "1";
			
			
			//populate update_artists_temp table with Singers and musicak groups
			tableColumns = new String[3];
			tableColumns[0] = "artist_name";
			tableColumns[1] = "artist_category";
			tableColumns[2] = "artist_type";
			types = new int[3];
			types[0] = 2;
			types[1] = 2;
			types[2] = 1;
			
			dbStatus = checkConfiguration(connection, "artists_temp_after_singers", "updateOp");
			if(dbStatus == 0){
				
				status = populateTable(connection, "update_artists_temp", tableColumns, types, sigersCategories);
				if(!status) return false;
				status = updateConfiguration(connection, "artists_temp_after_singers", "1", "operation", "updateOp");
				if(!status) return false;
			}
			
			dbStatus = checkConfiguration(connection, "artists_temp", "updateOp");
			if(dbStatus == 0){
	
				status = populateTable(connection, "update_artists_temp", tableColumns, types, musicalGroupsCategories);
				if(!status) return false;
				status = updateConfiguration(connection, "artists_temp", "1", "operation", "updateOp");
				if(!status) return false;
			}
		}
		System.out.println("Update artists database: Completed step 1/5.");

		//populate update_artists_distinct table
		statementToExecute = "INSERT INTO update_artists_distinct(artist_name, artist_type) "
				+ "SELECT DISTINCT artist_name, artist_type "
				+ "FROM update_artists_temp ";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artists_distinct", "updateOp");
		if(!status) return false;
		
		//Update artists table
		statementToExecute = "INSERT INTO artists(artist_name, artist_type) "
				+ "SELECT DISTINCT UpdateArtist.artist_name, UpdateArtist.artist_type "
				+ "FROM update_artists_distinct AS UpdateArtist, artists AS Artists "
				+ "WHERE  UpdateArtist.artist_name NOT IN (SELECT DISTINCT artist_name FROM artists)";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artists", "updateOp");
		if(!status) return false;
		System.out.println("Update artists database: Completed step 2/5.");

		
		//Artist categories - 
		
		//Create temporary table: update_artists_categories_distinct
		createTable(connection, "update_artists_categories_distinct", 
			"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artists_categories_distinct(category_name)'", 
			"category_name varchar(100) NOT NULL");

		//populate update_artists_categories_distinct table
		statementToExecute = "INSERT INTO update_artists_categories_distinct(category_name) "
				+ "SELECT DISTINCT artist_category "
				+ "FROM update_artists_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artists_categories_distinct", "updateOp");
		if(!status) return false;

		//Update categories_of_artists table
		statementToExecute = "INSERT INTO categories_of_artists(category_name) "
				+ "SELECT DISTINCT category_name "
				+ "FROM update_artists_categories_distinct AS UpdateCategory "
				+ "WHERE  UpdateCategory.category_name NOT IN (SELECT DISTINCT categories_of_artists.category_name FROM categories_of_artists)";
		status = insertPrimaryDataCycle(connection, statementToExecute, "categories_of_artists", "updateOp");
		if(!status) return false;
		System.out.println("Update artists database: Completed step 3/5.");

		//Update artist_category table -
		
		//Create temp table: update_artist_category_temp
		createTable(connection, "update_artist_category_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_category_temp(artist, category)'", 
				"artist varchar(100) NOT NULL, ",
				"category varchar(100) NOT NULL, ",
				"PRIMARY KEY (artist, category)");
		
		createTable(connection, "update_artist_category_id_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='update_artist_category_temp(artist_id, category_id)'", 
				"artist_id int NOT NULL, ",
				"category_id int NOT NULL, ",
				"PRIMARY KEY (artist_id, category_id)");
		
		statementToExecute = "INSERT INTO update_artist_category_temp(artist, category) "
				+ "SELECT DISTINCT artist_name, artist_category "
				+ "FROM update_artists_temp";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artist_category_temp", "updateOp");
		if(!status) return false;

		statementToExecute = "INSERT INTO update_artist_category_id_temp(artist_id, category_id) "
				+ "SELECT DISTINCT Artist.artist_id, Category.category_artist_id "
				+ "FROM update_artist_category_temp AS Temp, artists AS Artist, categories_of_artists AS Category "
				+ "WHERE Temp.artist = Artist.artist_name AND Temp.category = Category.category_name "
				+ "LIMIT 2000";
		status = insertPrimaryDataCycle(connection, statementToExecute, "update_artist_category_id_temp", "updateOp");
		if(!status) return false;
		System.out.println("Update artists database: Completed step 4/5.");

		
		//Update artist_category table
		statementToExecute = "INSERT INTO artist_category(artist_id, category_id) "
				+ "SELECT DISTINCT Temp.artist_id, Temp.category_id "
				+ "FROM update_artist_category_id_temp AS Temp "
				+ "WHERE NOT EXISTS (SELECT * "
				+ "FROM artist_category AS Old "
				+ "WHERE Temp.artist_id = Old.artist_id AND Temp.category_id = Old.category_id) "
				+ "LIMIT 2000";
		status = insertPrimaryDataCycle(connection, statementToExecute, "artist_category", "updateOp");
		//status = executeStatement(connection, statementToExecute);
		if(!status) return false;
		System.out.println("Update artists database: Completed step 5/5.");

		//Update configuration - finished updating artistsTables
		status = updateConfiguration(connection, "artistsTables", "1", "operation", "updateOp");
		
		//Drop temp tables and update configuration
		dropTable(connection, "update_artists_temp");
		updateConfiguration(connection, "update_artists_temp", "0", "operation", "updateOp");
		dropTable(connection, "update_artists_distinct");
		updateConfiguration(connection, "update_artists_distinct", "0", "operation", "updateOp");
		dropTable(connection, "update_artists_categories_distinct");
		updateConfiguration(connection, "update_artists_categories_distinct", "0", "operation", "updateOp");
		dropTable(connection, "update_artist_category_temp");
		updateConfiguration(connection, "update_artist_category_temp", "0", "operation", "updateOp");
		dropTable(connection, "update_artist_category_id_temp");
		updateConfiguration(connection, "update_artist_category_id_temp", "0", "operation", "updateOp");
		
		return status;
		
	}
	
	
	/**
	 * Initializes update tuple in configuration table to 0's, in order to enable other updates.
	 * @param tuple
	 * @return
	 */
	private static boolean initializeConfigurationTuple(Connection connection, String tuple){
		
		String[] statement = new String[2]; 

		statement[0] = "DELETE FROM configuration WHERE operation LIKE 'updateOp'";
		statement[1] = "INSERT INTO `configuration` (`operation`) VALUES ('" + tuple + "')";
		
		boolean status = true;

		status = executeTransaction(connection, statement);
		
		return status;
	}
	
	
	
	
}
