package recommender;

import java.util.ArrayList;
import java.util.List;

import core.ApplicationInterface;
import db.DBActionsInterface;
import recommender.entites.Song;
import recommender.entites.User;
import utilities.Pair;

public class RecommenderEngineAdapter{

	private DBActionsInterface db;
	private static RecommenderEngineAdapter instance = new RecommenderEngineAdapter();
	

	private RecommenderEngineAdapter() {
	}

	
	
	public static RecommenderEngineAdapter getInstance(){
		return instance;
	}
	
	public void initalize(DBActionsInterface db){
		this.db = db;
		RecommenderEngine.getInstance().initalize(createAllUsers(), createAllSongs());
	}
	
	public List<String> recommendFriends(String username, int topK) throws Exception{
		RecommenderEngine engine = RecommenderEngine.getInstance();
		List<User> tResult = engine.recommendFriends(engine.getUserById(username), topK);
		List<String> result = new ArrayList<String>();
		for (User u : tResult)
			result.add(u.getId());
		return result;
	}
	
	public List<Pair<String,String>> recommendSongs(String username, int topK) throws Exception{
		
		RecommenderEngine engine = RecommenderEngine.getInstance();
		List<Song> tResult = engine.recommendSongs(engine.getUserById(username), topK);
		
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		for (Song s : tResult)
			result.add(new Pair<String, String>(s.getArtistName(), s.getName()));
		
		return result;
	}
	
	

	
	private List<Song> createAllSongs() {
		
		List<Song> resultList = new ArrayList<Song>();
		
		ArrayList<String[]> songPairs = db.getSongsArtistList();
		
		for (int i=0;i<songPairs.size();i++)
		{
			String[] currentSongPair = songPairs.get(i);
			String artistName = currentSongPair[1];
			String songName = currentSongPair[0];
			
			recommender.entites.Song s = new Song(artistName+"@"+songName, songName, artistName);
			resultList.add(s);
		}
		
		return resultList;
	}

	private List<User> createAllUsers() {
		
		List<User> resultList = new ArrayList<User>();
		
		ArrayList<String[]> allUsersSet = db.getUsersList();
		for (int i=0;i<allUsersSet.size();i++)
		{
			String[] currentUserArr = allUsersSet.get(i);
			String userName = currentUserArr[1];
			
			recommender.entites.User u = new User(userName);
			resultList.add(u);
			
			//Add User Friends!
			
			//Add User Songs
		}
		
		return resultList;
		
		
	}

	
	
	

}
