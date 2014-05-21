package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import parsing.RawDataUnit;
import parsing.yagoMiner;


public class DataBaseManager {
	
	private static boolean qaqa = true;
	
	
	///////////////////////////////////////
	// 		Atomic database modifiers 
	///////////////////////////////////////
	
	
	/**
	 * Creates a new table in the DB.
	 * @param tableName - the table name to be created. //QAQA - add a check here? if table already exists?
	 * @param schema - Strings of the following format: COLUMN_NAME	TYPE[(SIZE)] - QAQA - is it all?
	 */
	public static void createTable(Connection connection, String tableName, String... schema){
		
		int schemaSize = schema.length;
		
		//Create the "CREATE" statement
		String dbCreateTableAction = "CREATE TABLE IF NOT EXISTS " + tableName + "(";
		for(int i=0; i<schemaSize-1; i++){
			dbCreateTableAction = dbCreateTableAction.concat(schema[i]).concat(",");
		}
		dbCreateTableAction = dbCreateTableAction.concat(schema[schemaSize-1]).concat(")");
		
		Statement stmt = null;
		
		try {
			//Create a statement from the connection object
			stmt = connection.createStatement();
			
			//Execute statement (create the table...)
			stmt.execute(dbCreateTableAction);
			
		} catch (SQLException e) {
			System.out.println("createTable: Couldn't create statement");
			e.printStackTrace();
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
	 * Builds a table out of the given RawDataUnit.
	 * Uses batch insertion.
	 * @param table
	 * @param dataUnit
	 * @return
	 */
	public static boolean batchInsertionIntoDB(Connection connection, String tableName, RawDataUnit dataUnit) {
		
		//Prepare statement arguments
		int schemaSize = dataUnit.getAttributes().length;
		String tableSchema = tableName + "(";
		String[] attributes = dataUnit.getAttributes();
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
	
	//////////////////////////////////////////////////////////
	// 		Database modifiers - DB Creation & Maintenance
	//////////////////////////////////////////////////////////
	
	
	/**
	 * Builds a table in the DB from YAGO data
	 * @param connection
	 * @param filename
	 * @param tableName
	 * @param miningSubject
	 * @param miningIndex
	 * @param attributes
	 * @param schema
	 * @return
	 */
	private static boolean buildTableFromYago(Connection connection, String filename, String tableName, String miningSubject, int miningIndex, String[] attributes, String[] schema){
		
		boolean status = true;
		
		if(qaqa) System.out.println("buildTableFromYago: building table " + tableName + ", from " + filename);
		

		//Temporarily, I've added dbConnection to the buildTableFromYago signature...
		
		//Mine data from Yago
		RawDataUnit rdu = yagoMiner.mineDataUnit(filename, miningSubject, miningIndex, attributes);
		
		//Create table in DB
		DataBaseManager.createTable(connection, tableName, schema);
		//if(status == false) return false; //if no table was created - returns false  QAQA IMPL
		
		//Insert data to table
		status = DataBaseManager.batchInsertionIntoDB(connection, tableName, rdu);

		
		return status;
	}
	
	
	
	
	////////////////////////////////////
	// 		initialize app's database
	////////////////////////////////////
	
	public static boolean initializeDatabaseConfiguration(Connection connection){
		
		boolean status = true;
		
		//If table already exists, it will just skip creating table again
		createTable(connection, "configuration", "parameter VARCHAR(45) NOT NULL", "dbStatus INT NOT NULL DEFAULT 0", "PRIMARY KEY (`dbStatus`)");
		
		PreparedStatement stmt = null;
		
		try {
			
			Statement tstmt = connection.createStatement();
			ResultSet rs = tstmt.executeQuery("SELECT * FROM configuration WHERE parameter = 'initialized'");
			int shocko = 99; //QAQA
			if(rs.next() == true)
				shocko = rs.getInt("dbStatus");
			
			if(shocko == 1)
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
	
	
	////////////////////////////////////
	// 		Music database code
	////////////////////////////////////
	
	
	/**
	 * Builds the entire music database from scratch.
	 * @param dbConnection
	 * @param pathToYagoFiles
	 * @return
	 */
	public static boolean buildMusicDatabase(Connection connection, String pathToYagoFiles){
		
		boolean status = true;		
		
		//Yago files' paths
		String yagoFactsFile = pathToYagoFiles + "yagoFacts.tsv";
		String YagoTransitiveTypeFile = pathToYagoFiles + "YagoTransitiveType.tsv";
		String yagoTypesFile = pathToYagoFiles + "yagoTypes.tsv";
		
		
		///////////////////////
		//	Build temp tables -
		///////////////////////
		
		
		//Build singers table 
		
		//attrsYagoTransitiveTypeSingers member names must be identical to schemaYagoTransitiveTypeSingers member names:
		String[] attrsYagoTransitiveTypeSingers = {"singer", "rdf_type", "singer_classification"};
		String[] schemaYagoTransitiveTypeSingers = {"singer		CHARACTER(100)", "rdf_type	CHARACTER(150)", "singer_classification	CHARACTER(150)"};
		dropTable(connection, "singers");
		buildTableFromYago(connection, YagoTransitiveTypeFile, "singers", "_singers>", 2, attrsYagoTransitiveTypeSingers, schemaYagoTransitiveTypeSingers);
		
		
		//Build yago_creations table
		String[] attrsYagoFacts = {"id", "yago_name", "relation", "relation_goal"};
		String[] schemaYagoFacts = {"id		CHARACTER(50)", "yago_name	CHARACTER(150)", "relation	CHARACTER(50)", "relation_goal	CHARACTER(250)"};
		dropTable(connection, "yago_creations");
		buildTableFromYago(connection, yagoFactsFile, "yago_creations", "created", 2, attrsYagoFacts, schemaYagoFacts);
		
		
		//Build music_groups table [Data taken from yagoTypes]
		String[] attrsYagoTypes = {"id", "subject", "type", "category"};
		String[] schemaYagoTypes = {"id		CHARACTER(50)", "subject	CHARACTER(150)", "type	CHARACTER(50)", "category	CHARACTER(250)"};
		dropTable(connection, "musical_groups");
		buildTableFromYago(connection, yagoTypesFile, "musical_groups", "Musical_groups_established", 3, attrsYagoTypes, schemaYagoTypes);

		
		//Build songs table [Data taken from yagoTransitiveType]
		String[] attrsYagoTransitiveType = {"song_name", "rdf_type", "song_clasification"};
		String[] schemaYagoTransitiveType = {"song_name		CHARACTER(100)", "rdf_type	CHARACTER(150)", "song_clasification	CHARACTER(150)"};
		dropTable(connection, "songs");
		buildTableFromYago(connection, YagoTransitiveTypeFile, "songs", "_songs", 2, attrsYagoTransitiveType, schemaYagoTransitiveType);

		
		//Build albums table [Data taken from yagoTypes]
		String[] attrsYagoTypes2 = {"id", "albumName", "type", "category"};
		String[] schemaYagoTypes2 = {"id		CHARACTER(50)", "albumName	CHARACTER(150)", "type	CHARACTER(50)", "category	CHARACTER(250)"};
		dropTable(connection, "albums");
		buildTableFromYago(connection, yagoTypesFile, "albums", "_albums", 3, attrsYagoTypes2, schemaYagoTypes2);
		
		
		
		//Other mining options -
		
		//Mining from yagoTypes.tsv
		//String[] attrsYagoTypes = {"id", "subject", "type", "wikicategory"};
		//RawDataUnit duYagoTypes = mineDataUnit("yagoTypes.tsv", miningSubject, miningIndex, attrsYagoTypes);
		
		//Mining from yagoTaxonomy.tsv
		//String[] attrsYagoTaxonomy = {"subFact", "rdfs:subClassOf", "superFact"};
		//RawDataUnit duYagoTaxonomy = mineDataUnit("yagoTaxonomy.tsv", miningSubject, miningIndex, attrsYagoTaxonomy);
		
		//Mining from yagoTransitiveType.tsv
		//String[] attrsYagoTransitiveType = {"hardToClassify1", "rdf:type/rdfs:subClassOf", "hardToClassify2"};
		//RawDataUnit duYagoTransitiveType = mineDataUnit("yagoTransitiveType.tsv", miningSubject, miningIndex, attrsYagoTransitiveType);
		
		//Mining from yagoLiteralFacts.tsv
		//String[] attrsYagoLiteralFacts = {"id", "subject", "relation", "WHAT"};
		//RawDataUnit duYagoLiteralFacts = mineDataUnit("yagoLiteralFacts.tsv", miningSubject, miningIndex, attrsYagoLiteralFacts);
		

		if(qaqa) System.out.println("*****************************************\nbuildDatabase: Finished building the database...\n");
		
		return status;
		
	}


	
	public static boolean buildMusicDatabaseNextGeneration(Connection connection, String pathToYagoFiles){
		
		boolean status = true;
		
		//createTable(connection, "NG_songs", 

				
				
				

		return status;
		
	}
	
	
	////////////////////////////////////
	// 		Music database code
	////////////////////////////////////
	
	
	public static boolean buildSocialNetworkDatabase(Connection connection){
		
		boolean status = false;
		
		//Create users table - this is only a temporary implementation
		
		//createTable(connection, "usersNew", "userID	INT", "username	CHARACTER(20)", "password	CHARACTER(10)");
		
		createTable(connection, "users", 
				"userID INT UNSIGNED NOT NULL AUTO_INCREMENT",
				"username VARCHAR(10) NOT NULL",
				"password VARCHAR(15) NOT NULL",
				"status_song_id INT UNSIGNED NULL",
				" PRIMARY KEY (`userID`)",
				" UNIQUE INDEX `userID_UNIQUE` (`userID` ASC)");
		
		//Create user-song table
		
		
		
		return status;
		
	}
	
	
}
