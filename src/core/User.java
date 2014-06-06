package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import utilities.Pair;
import db.ConnectionPool;
import db.DBActionsInterface;
import db.dbActions;

public class User {
	
	private int userID;
	private String username;
	private Pair<String,String> statusSong;
	private List<Pair<String,String>> SongArtistlist;
	private List<Pair<String,String>> Artistlist;
	private ArrayList<String> freindsList;
	
	
	
	//Constructor
	public User(int userID,String username){
		
		this.userID = userID;	
		this.username=username;
	}

	
	//Getters
	
	
	public int getUserID(){
		return this.userID;
	}
	
	public String getUserName(){
		return this.username;
	}
	
	
	public Pair<String,String> getStatusSong(){
		return this.statusSong;
	}
	
	
	public List<Pair<String,String>> getSongArtistList(){
		return this.SongArtistlist;
	}
	
	public List<Pair<String,String>> getArtistList(){
		return this.Artistlist;
	}
	
	public ArrayList<String> getfreindsList(){
		return this.freindsList;
	}
	
	
	//Setters
	
	public void setuserID(int userID)
	{
		this.userID=userID;
	}
	
	public void setUserName(String username)
	{
		this.username=username;
	}
	
	public void setStatusSong(Pair<String,String> statusSong)
	{
		this.statusSong =statusSong;
	}
	
	
	public void setSongArtistlist(List<Pair<String,String>> SongArtistlist)
	{
		this.SongArtistlist=SongArtistlist;
	}
	
	public void setArtistlist(List<Pair<String,String>> Artistlist)
	{
		this.Artistlist=Artistlist;
	}
	
	
	public void setfreindsList(ArrayList<String> freindsList)
	{
		this.freindsList=freindsList;
	}
	
	
	
	//adds
	public void addSongToSongArtistlist(Pair<String,String> song_artist){  //QAQA - we should use songID - change tables structure
		this.SongArtistlist.add(song_artist);
	}
	
	public void addArtistToArtistlist(Pair<String,String> artist_null){  //QAQA - we should use songID - change tables structure
		this.Artistlist.add(artist_null);
	}
	
	
	
	public void addFreindTofreindsList(String freind)
	{
		this.freindsList.add(freind);
	}
	
	public void removeFriendFromFriendList(String freind)
	{
		this.freindsList.remove(freind);
	}
	
	public void removeSongFromSongArtistList(Pair<String,String> song_artist)
	{
		this.SongArtistlist.remove(song_artist);
	}
	
	
	
	
	
	
	
	private boolean disconnectUser(){
		// TODO Auto-generated method stub
		return false;
	}
	
}
