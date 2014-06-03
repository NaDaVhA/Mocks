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
		
		User u = engine.getUserByUsername(username);
		if (u==null)
			return new ArrayList<String>();
			
		List<User> tResult = engine.recommendFriends(u, topK);
		
		List<String> result = new ArrayList<String>();
		for (User user : tResult)
			result.add(user.getUsername());
		
		return result;
	}
	
	public List<Pair<String,String>> recommendSongs(String username, int topK) throws Exception{
		
		RecommenderEngine engine = RecommenderEngine.getInstance();
		
		User u = engine.getUserByUsername(username);
		if (u==null)
			return new ArrayList<Pair<String,String>>();
		
		List<Song> tResult = engine.recommendSongs(u, topK);
		
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
		
		//Create Users
		ArrayList<String[]> allUsersSet = db.getUsersList();
		for (int i=0;i<allUsersSet.size();i++)
		{
			String[] currentUserArr = allUsersSet.get(i);
			String userName = currentUserArr[1];
			String userIdStr = currentUserArr[0];
			
			recommender.entites.User u = new User(userIdStr,userName);
			resultList.add(u);
		}
		
		//Add User Friends & Songs
		for (User u : resultList)
		{
			ArrayList<String[]> userFriends = db.getUserFreindsList(Integer.parseInt(u.getId()));
			for (String[] friendArr : userFriends)
			{
				String friendUsername = friendArr[0];
				u.addFriend(getUserByUsername(resultList,friendUsername));
			}
			
			ArrayList<String[]> userSongs = db.getUserSongList(Integer.parseInt(u.getId()));
			for (String[] userSongArr : userSongs)
			{
				String artistName = userSongArr[1];
				String songName = userSongArr[0];
				recommender.entites.Song s = new Song(artistName+"@"+songName, songName, artistName);
				u.addSong(s);
			}
		}
		
		return resultList;
		
		
	}

	public User getUserByUsername(List<User> users, String username){
		for (User u : users)
		{
			if (u.getUsername().toLowerCase().equals(username.toLowerCase()))
				return u;
		}
		return null;
	}
	
	

}
