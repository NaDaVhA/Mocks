package core;

import java.util.LinkedList;

import db.ConnectionPool;
import db.databaseActions;
import db.dbActions;

public class User {
	
	private int userID;
	private boolean connectionStatus;
	private String username;
	private String statusSong;
	private LinkedList<String> playlist;
	private dbActions dbActions;
	
	
	//Constructor
	public User(int userID, ConnectionPool pool){
		
		this.userID = userID;
		this.connectionStatus = true;

		//Update connectionStatus in DB
		this.dbActions.connectUser(this.userID);
		
		//Get user's info
		this.username = this.dbActions.getUserName(userID);
		this.statusSong = this.dbActions.getUserStatusSong(userID);
		this.playlist = new LinkedList<String>();
		this.dbActions = new dbActions(pool);
	}
	

	
	//Getters
	
	public String getStatusSong(){
		return this.statusSong;
	}
	
	public LinkedList<String> getPlayList(){
		return this.playlist;
	}
	
	
	//Setters
	
	
	public void addSongToPlaylist(String song){  //QAQA - we should use songID - change tables structure
		
	}
	
	
	
	
	private boolean disconnectUser(){
		
		boolean status = true;
		
		this.dbActions.disconnectUser(userID);
		
		
		return status;
	}
	
}
