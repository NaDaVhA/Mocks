package core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Scanner;

import db.ConnectionPool;
import db.DataBaseManager;
import db.dbActions;


public class Application {
	
	private boolean qaqa = true;
	private ConnectionPool connectionPool;
	private boolean applicationStatus;
	private boolean DdbStatus;
	private dbActions dbActionRunner;
	
	
	public Application(){
		
		this.connectionPool = new ConnectionPool("localhost", "3306", "db_project_1", "root", "!om3GAsilverhead"); 
		this.applicationStatus = false;
		this.DdbStatus = false;
		this.dbActionRunner = new dbActions(this.connectionPool);
	}
	
	
	public void runApplication(){
		
		if(qaqa) System.out.println("\nrunApplication: Running...");
		//////////////////////////////////
		// Load configuration file  ???
		//////////////////////////////////
		if(qaqa) System.out.println("runApplication: Faching configuration file...");

		// TODO
		
	
		Connection connection = this.connectionPool.getConnectionFromPool();
		
		//Check database status
		this.DdbStatus = DataBaseManager.initializeDatabaseConfiguration(connection);
		
		//Initialize database if needed
		if(this.DdbStatus == false){
			if(qaqa) System.out.println("runApplication: Initializing database...\n");
			this.initializeApplicationDataBase();
		}else{
			if(qaqa) System.out.println("runApplication: No need to initialize database...\n");
		}
			

		if(qaqa) System.out.println("\n\nrunApplication: ************************\nApplication is ready to go...\n");
		
		
		///////////////////////////////////////////////////////////
		//	First screen
		///////////////////////////////////////////////////////////
		
		boolean registeredFlag = false;
		System.out.print("Hello dear unknown user. Are you already registered? (Y/N) ");
		String firstAnswer = getUserInput();
		if(firstAnswer.compareTo("Y") == 0)
			registeredFlag = true;
		System.out.println();
		
		///////////////////////////////////////////////////////////
		//	Registration screen loop
		///////////////////////////////////////////////////////////
		
		if(!registeredFlag)
			this.registrationScreenLoop();
		
		///////////////////////////////////////////////////////////
		//	Login screen loop
		///////////////////////////////////////////////////////////
				
		this.loginScreenLoop();				
		
		//Testing zone
		dbActionRunner.setUserStatusSong(1, 44);
		
		
		///////////////////////////////////////////////////////////
		//	User screen loop
		///////////////////////////////////////////////////////////
		
		// TODO

		
		
		
		
		///////////////////////////////////////////////////////////
		// Close application
		///////////////////////////////////////////////////////////
		
		// TODO

		
		
	}
	
	
	
	/////////////////////////////////////////////////
	// 			Application's Help methods 
	/////////////////////////////////////////////////
	
	
	
	/**
	 * This method triggers the initialization of the application's database.
	 */
	private void initializeApplicationDataBase(){
		
		// TODO - get configuration file path from user
		
		// TODO extract yagoFilesPath from configuration file
		String yagoFilesPath = "D:\\YAGO\\";  //QAQA - we must take this path from a configuration file (Integrate with gui)

		Connection connection = this.connectionPool.getConnectionFromPool();
		
		System.out.println("\nInitializing application database...\n");
		
		// Build music database
		//DataBaseManager.buildMusicDatabase(connection, yagoFilesPath);
		this.connectionPool.returnConnectionToPool(connection);
		connection = null;
		
		// Build Users database
		connection = this.connectionPool.getConnectionFromPool();
		DataBaseManager.buildSocialNetworkDatabase(connection);
		this.connectionPool.returnConnectionToPool(connection);
		connection = null;
		
		this.DdbStatus = true;

		this.connectionPool.returnConnectionToPool(connection);

	}
	
	
	
	/////////////////////////////////////////////////
	// 			Registration screen module
	/////////////////////////////////////////////////
	
	/**
	 * This method operates the application's registration screen.
	 */
	private void registrationScreenLoop(){
		
		System.out.println("\n*******************************************************");
		System.out.println("***************** Registration screen *****************");
		System.out.println("*******************************************************\n");
		
		//Get username and password from user
		
		//Get/select new username
		
		String username = null;
		
		boolean usernameValidity = false;
		while(!usernameValidity){
			System.out.println("*****************************************************************");
			System.out.print("Please select a username (restricted up to 10 characters): ");
			username = getUserInput();
			
			usernameValidity = !this.dbActionRunner.usernameExists(username);
			
			if(usernameValidity){
				//Check all sort of other username checks...	QAQA IMPL
			}else{
				System.out.println("Username is already taken. Please select a different username\n");
				continue;
			}
		
		}
		
		
		
		boolean pw_flag = false;
		String password = null;
		while(!pw_flag){
			System.out.print("\nPlease select a password: (alphanumeric only, restricted to 10 characters)");
			String password_1 = getUserInput();
			System.out.print("\nPlease repeat the selected password: (alphanumeric only, restricted to 10 characters)");
			String password_2 = getUserInput();
			if(password_1.compareTo(password_2)!=0){
				System.out.print("\nPasswords don't match. Please try again");
				continue;
			}else{
				//checkPasswordValidity(password_1);
				password = password_1;
				pw_flag = true;
			}

		}
		
		
		//Register new user
		boolean registrationStatus = this.dbActionRunner.registerNewUser(username, password);
		
		if(!registrationStatus){
			System.out.println("registrationStatus: registration failed. Try again.");
		}
		
		System.out.println("*****************************************************************\n");
		System.out.println("User " + username + " is registered. Welcome aboard!");
		
		
	}
	
	
	/////////////////////////////////////////////////
	// 			Login screen module
	/////////////////////////////////////////////////
	
	/**
	 * This method operates the application login screen.
	 */
	private boolean loginScreenLoop(){
		
		Boolean userAuthentication = false;

		String username = null;
		
		while(!userAuthentication){
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("*****************************************************");
			System.out.println("*****************ourApp Login screen*****************");
			System.out.println("*****************************************************\n");
			
			//Get username and password from user
			
			System.out.println("*****************************************************************");
			System.out.print("Please enter your username:");
			username = getUserInput();
			System.out.print("\nPlease enter your password: ");
			String password = getUserInput();
			System.out.println("*****************************************************************\n");
			System.out.println("System is validating username and password. Please wait...");

			if((username == null)||(password == null)){
				System.out.println("invalid details given! Please try again.");
			}
			
			//validate username and password 
			userAuthentication = this.dbActionRunner.authenticateUser(username, password);
			
			if(!userAuthentication)
				System.out.println("User details doesn't match. Please try again.\n\n");
			
		}
		
		System.out.println("\n*****************************************************");
		System.out.println("Dear " + username + ", welcome to our application. Have fun!");
		System.out.println("*****************************************************\n");
		
		return userAuthentication;
		
	}
	
	
	

	/**
	 * Gets user input...
	 * @return a string containing valid user input, or NULL if input is not valid.
	 */
	private String getUserInput(){
		
		String userInput = null;
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			userInput = input.readLine();
			if(!this.inputCheck(userInput, 10))
				userInput = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return userInput;
		
	}
	
	
	


	
	
	/**
	 * Check users input validity.
	 * @param input
	 * @return
	 */
	private boolean inputCheck(String input, int inputLengthRestriction){
		
		boolean validInput = false;
		
		if(input.length()<=inputLengthRestriction)
			validInput = true;
		
		return validInput;
		
	}
}
