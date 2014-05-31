package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
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
			
			//Execute statement (create the table...)
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
	 * Populates a table out of the given RawDataUnit.
	 * Uses batch insertion.
	 * @param table
	 * @param dataUnit
	 * @return
	 */
	public static boolean batchInsertionIntoDB(Connection connection, String tableName, RawDataUnit dataUnit) {
		
		//Prepare statement arguments
		int schemaSize = 0;// dataUnit.getAttributes().length;
		String tableSchema = tableName + "(";
		String[] attributes = null;//dataUnit.getAttributes();
		for(int i=0; i<attributes.length; i++){
			tableSchema = tableSchema.concat(attributes[i]);
			if(i!=attributes.length-1)
				tableSchema = tableSchema.concat(",");
		}
			
		
		tableSchema = tableSchema.concat(")");
		String dbInsertAction = "INSERT INTO " + tableSchema + "VALUES(";
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
			Iterator<String[]> linesIter = dataUnit.iterator();
			int count = 0;
			int batch = 0;
			
			while(linesIter.hasNext()){
								
				String[] currentLine = linesIter.next();
				for(int i=0; i<schemaSize; i++)
					pstmt.setString(i+1, currentLine[i]);
				
				pstmt.addBatch();
				count++;
				
				//Limit insertion - execute batch every 1000 entries
				if(count==1000){
					count = 0;
					pstmt.executeBatch();
					if(qaqa) System.out.println("batchInsertionIntoDB: batch #" + ++batch);
				};
			}
				
			
			if(qaqa) System.out.println("Success - completed batchInsertionIntoDB");
						
		} catch (SQLException e) {
			System.out.println("ERROR batchInsertionIntoDB - "
					+ e.getMessage());
			return false;
		}finally{
			safelyClose(pstmt);
		}
		
		return true;
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
	 * 	QAQA IMPL - might be needed for UPDATE.
	 * @param connection
	 * @param tableName
	 * @param columns
	 * @return LinkedHashMap<String, Integer> of the required columns or null if error was encountered.
	 */
	private static LinkedHashMap<String, Integer> getTableFromDB(Connection connection, String tableName, String key, String[] columns){
		
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		
		//Build query
		String selectQuery = "SELECT ";
		for(String col : columns){
			selectQuery.concat(col + " ");
		}
		selectQuery.concat(" FROM " + tableName);
		
		//Prepare statement and execute query
		Statement stmt = null;
		ResultSet rs = null;

		try {
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(selectQuery);
			
			while (rs.next() == true){
				String objectName = rs.getString("QAQA FILL IN WHEN IMPL");
				int objectID = rs.getInt(123);
				
				result.put(objectName, objectID);
			}
				
		} catch (SQLException e) {
			System.out.println("ERROR getSingersList - " + e.getMessage());
			return null;
		} finally {
			DataBaseManager.safelyClose(stmt, rs);			
		}
						
		return result;
		
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
		
		LinkedList<String[]> result = new LinkedList<String[]>();
		
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
		
		LinkedList<String[]> resultDataStructure = new LinkedList<String[]>();
		int size = indices.length;
		
		for(String[] line : rdu){
			
			String[] array = new String[size];
			for(int i=0; i<size; i++){
				array[i] = line[indices[i]];
			}
			
			//Filter duplicates
			if(!resultDataStructure.contains(array))
				resultDataStructure.add(array);
			
			if(resultDataStructure.size() == 50000)
				break;
		}
		
		return resultDataStructure;
		
	}
	
	
	
	////////////////////////////////////
	// 		initialize app's database
	////////////////////////////////////
	
	
	public static boolean initializeDatabaseConfiguration(Connection connection){
		
		boolean status = true;
		
		//If table already exists, it will just skip creating table again
		createTable(connection, "configuration", null, "parameter VARCHAR(45) NOT NULL, ", "dbStatus INT NOT NULL DEFAULT 0, ", "PRIMARY KEY (`dbStatus`)");
		
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
		
		
		String insertaionAction = "INSERT INTO configuration(parameter, dbStatus) VALUES('initialized',1)";

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
	 * Checks whether database is already initialized.
	 * @param connection
	 * @return true if database is already initialized, false otherwise.
	 */
	public static boolean getDatabaseInitializationStatus(Connection connection){
				
		//If table already exists, it will just skip creating table again
		createTable(connection, "configuration", null, "parameter VARCHAR(45) NOT NULL, ", "dbStatus INT NOT NULL DEFAULT 0");
		
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
		String insertaionAction = "INSERT INTO configuration(parameter, dbStatus) VALUES('initialized',1)";

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
	
	
	////////////////////////////////////
	// 		Music database builder
	////////////////////////////////////

	
	
	/**
	 * Builds the music database tables.
	 * @param connection
	 * @param pathToYagoFiles - path to the folder that contains the yago files.
	 * @return
	 */
	public static boolean buildMusicDatabase(Connection connection, String pathToYagoFiles){
		
		boolean status = true;		
		
		//Yago files' paths
		String yagoFactsFile = pathToYagoFiles + "yagoFacts.tsv";
		String YagoTransitiveTypeFile = pathToYagoFiles + "YagoTransitiveType.tsv";
		String yagoTypesFile = pathToYagoFiles + "yagoTypes.tsv";
		
		//Prepare data
		
		//Mine relevant data from Yago in one batch-
		String[] transitiveAttributes = {"_groups>", "_singers>", "_songs>"};
		RawDataUnit rawMusicData = yagoMiner.mineDataUnit(YagoTransitiveTypeFile, transitiveAttributes, 2);
		String[] factsAttributes = {"<created>"};
		RawDataUnit rawCreatorCreationsData = yagoMiner.mineDataUnit(yagoFactsFile, factsAttributes, 2);
		
		//Extract raw data from Yago - singers, musical groups, songs
		RawDataUnit rawSinger = new RawDataUnit();
		rawSinger = extractRawLinesByAttribute(rawMusicData, 2, "_singers>", rawSinger);
		RawDataUnit rawMusicalGroups = new RawDataUnit();
		rawMusicalGroups = extractRawLinesByAttribute(rawMusicData, 2, "_groups>", rawMusicalGroups);
		RawDataUnit rawSongs = new RawDataUnit();
		rawSongs = extractRawLinesByAttribute(rawMusicData, 2, "_songs>", rawSongs);
	
		//Build database
		
		//Artists data base creation
		status = buildArtistsTable(connection, rawSinger, rawMusicalGroups);
		
		//Songs data base creation
		status = buildSongsTables(connection, rawSongs);
		
		//	Prepare Creator-Creations data structure
		status = buildCreatorCreationsTable(connection, rawCreatorCreationsData);
				
		return status;
		
	}
	
	
	/**
	 * Builds the artists tables: artists, categories_of_artists, artist_category. 
	 * @param connection
	 * @param rawSinger
	 * @param rawMusicalGroups
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean buildArtistsTable(Connection connection, RawDataUnit rawSinger, RawDataUnit rawMusicalGroups) {
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
	
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
		
		status = populateTable(connection, "artists_temp", tableColumns, types, sigersCategories);
		status = populateTable(connection, "artists_temp", tableColumns, types, musicalGroupsCategories);

		//populate artists table
		statementToExecute = "INSERT INTO artists(artist_name, artist_type) "
				+ "SELECT DISTINCT artist_name, artist_type "
				+ "FROM artists_temp";
		status = executeStatement(connection, statementToExecute);
	
		
		//populate categories_of_artists table
		statementToExecute = "INSERT INTO categories_of_artists(category_name) "
				+ "SELECT DISTINCT artist_category "
				+ "FROM artists_temp";
		status = executeStatement(connection, statementToExecute);
			
		
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
		status = executeStatement(connection, statementToExecute);
		
		//populate artist_category table
		statementToExecute = "INSERT INTO artist_category(artist_id, category_id) "
				+ "SELECT DISTINCT Artist.artist_id, Category.category_artist_id "
				+ "FROM artist_category_temp AS Temp, artists AS Artist, categories_of_artists AS Category "
				+ "WHERE Temp.artist = Artist.artist_name  AND  Temp.category = Category.category_name "
				+ "LIMIT 2000";
		status = executeStatement(connection, statementToExecute);
		
		
		//Drop artist_category_temp table
		dropTable(connection, "artist_category_temp");
		
		return status;
	
	}
	
	
	/**
	 * Builds the CreatorCreations table which holds all the links between our DB's songs and artists.
	 * @param connection
	 * @param rawCreatorCreationsData
	 * @return true if succeeded, false otherwise.
	 */
	private static boolean buildCreatorCreationsTable(Connection connection, RawDataUnit rawCreatorCreationsData) {
		
		boolean status = true;
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		
		//Prepare data
		int[] indices = {1,3};
		LinkedList<String[]> creatorCreation = extractDataFromRDT(rawCreatorCreationsData, indices);
		
		//Create temporary table: song_category_temp
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

		//Populate DISTINCT values
		createTable(connection, "artist_song_temp_DISTINCT", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_song_temp(artist, song)'", 
				"artist varchar(100) NOT NULL, ",
				"song varchar(100) NOT NULL, ", 
				"PRIMARY KEY (artist, song) ");
		
		statementToExecute = "INSERT INTO artist_song_temp_DISTINCT(artist, song) "
				+ "SELECT DISTINCT artist, song "
				+ "FROM artist_song_temp";
		status = executeStatement(connection, statementToExecute);
		
		// (FINALLY !!!) populate artist_song table
		statementToExecute = "INSERT INTO artist_song(song_id, artist_id) "
				+ "SELECT DISTINCT Song.song_id, Artist.artist_id "
				+ "FROM artists as Artist, songs AS Song, artist_song_temp_distinct AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.artist = Artist.artist_name";
		status = executeStatement(connection, statementToExecute);
		
		
		//Drop temp tables: artist_song_temp, artist_song_temp_DISTINCT
		dropTable(connection, "artist_song_temp");
		dropTable(connection, "artist_song_temp_DISTINCT");
		
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
		
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		boolean status;
			
		//Extract song-category data from RDU
		int[] indices = {0,2};

		LinkedList<String[]> songsAndCategories = extractDataFromRDT(rawSongs, indices);
		
		//Create temporary table: song_category_temp
		createTable(connection, "song_category_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='song_category_temp(song, category)'", 
				"song varchar(100) NOT NULL, ",
				"category varchar(100) NOT NULL, ",
				"PRIMARY KEY (song, category)");
		
		//populate songs_categories_temp with songs-categories
		tableColumns = new String[2];
		tableColumns[0] = "song";
		tableColumns[1] = "category";
		types = new int[2];
		types[0] = 2;
		types[1] = 2;
		populateTable(connection, "song_category_temp", tableColumns, types, songsAndCategories);

		//populate songs table
		statementToExecute = "INSERT INTO songs(song_name) "
				+ "SELECT DISTINCT song "
				+ "FROM song_category_temp";
		status = executeStatement(connection, statementToExecute);
		
		//populate categories_of_songs table
		statementToExecute = "INSERT INTO categories_of_songs(category_name) "
				+ "SELECT DISTINCT category "
				+ "FROM song_category_temp";
		status = executeStatement(connection, statementToExecute);
		
		
		//populate song_category table
		statementToExecute = "INSERT INTO song_category(song_id, category_id) "
				+ "SELECT DISTINCT Song.song_id, Category.category_id "
				+ "FROM songs AS Song, categories_of_songs as Category, song_category_temp AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.category = Category.category_name";
		status = executeStatement(connection, statementToExecute);
		
		//Drop temp table: song_category_temp
		dropTable(connection, "song_category_temp");
		
		System.out.println("Good! we have songs!");
		
		return status;
	}
	
	
	
	
	/**
	 * Builds the following tables:
	 * songs, categories_of_songs, song_category.
	 * @param connection
	 * @param rawSongs
	 * @return true if no problems were encountered, false otherwise.
	 */
	private static boolean buildArtistsTablesOLDVERSIONNOTSURE(Connection connection, RawDataUnit rawSongs) {
		
		String[] tableColumns;
		int[] types;
		String statementToExecute;
		boolean status;
			
		//Extract song-category data from RDU
		int[] indices = {0,2};

		LinkedList<String[]> songsAndCategories = extractDataFromRDT(rawSongs, indices);
		
		//Create temporary table: song_category_temp
		createTable(connection, "song_category_temp", 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='song_category_temp(song, category)'", 
				"song varchar(100) NOT NULL, ",
				"category varchar(100) NOT NULL, ",
				"PRIMARY KEY (song, category)");
		
		//populate songs_categories_temp with songs-categories
		tableColumns = new String[2];
		tableColumns[0] = "song";
		tableColumns[1] = "category";
		types = new int[2];
		types[0] = 2;
		types[1] = 2;
		populateTable(connection, "song_category_temp", tableColumns, types, songsAndCategories);

		//populate songs table
		statementToExecute = "INSERT INTO songs(song_name) "
				+ "SELECT DISTINCT song "
				+ "FROM song_category_temp";
		status = executeStatement(connection, statementToExecute);
		
		//populate categories_of_songs table
		statementToExecute = "INSERT INTO categories_of_songs(category_name) "
				+ "SELECT DISTINCT category "
				+ "FROM song_category_temp";
		status = executeStatement(connection, statementToExecute);
		
		
		//populate song_category table
		statementToExecute = "INSERT INTO song_category(song_id, category_id) "
				+ "SELECT Song.song_id, Category.category_id "
				+ "FROM songs AS Song, categories_of_songs as Category, song_category_temp AS Temp "
				+ "WHERE Temp.song = Song.song_name AND Temp.category = Category.category_name "
				+ "LIMIT 2000";
		status = executeStatement(connection, statementToExecute);
		
		//Drop temp table: song_category_temp
		dropTable(connection, "song_category_temp");
				
		return status;
	}
	
	
	
	////////////////////////////////////
	// 		Music database updater
	////////////////////////////////////
	
	
	public static boolean updateMusicDatabase(Connection connection, String pathToYagoFiles){
		
		//TODO
		
		return true;
		
	}
}
