package recommender.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import recommender.RecommenderEngine;
import recommender.entites.Song;
import recommender.entites.User;

public class Startup {

	
	
	
	public static User getUser(List<User> allUsers, String userId){
		for (User u : allUsers)
		{
			if (u.getId().toLowerCase().equals(userId.toLowerCase()))
				return u;
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static void mainTest(){
		
		
		int usersCount = 100;
		int songsCount = 600;
		
		Random randomGenerator = new Random();
		
		//Generate Users
		List<User> allUsers = new LinkedList<User>();
		for (int i=0;i<usersCount;i++)
			allUsers.add(new User(""+i));
		
		//Generate Friends
		for (User u : allUsers)
		{
			int howManyFriends = randomGenerator.nextInt((int)(usersCount * 0.05));
			for (int j=0;j<howManyFriends;j++)
			{
				int friendId = randomGenerator.nextInt(usersCount);
				User friend = getUser(allUsers, ""+friendId);
				u.addFriend(friend);
			}			
		}
		
		//Generate Songs
		List<Song> allSongs = new LinkedList<Song>();
		for (int i=0;i<songsCount;i++)
		{
			Song s = new Song(""+i,""+i);
			allSongs.add(s);
			
			//Generate User Listens To Songs
			int howManyUsers = randomGenerator.nextInt(usersCount+1);
			for (int j=0;j<howManyUsers;j++)
			{
				int currentUserId = randomGenerator.nextInt(usersCount);
				User currentUser = getUser(allUsers, ""+currentUserId);
				currentUser.addSong(s);
			}
		}
		
		
		
				
		RecommenderEngine engine = RecommenderEngine.getInstance();
		engine.initalize(allUsers, allSongs);		
		try {
			List<String> suggestedFriends = engine.recommendFriends(allUsers.get(0).getId(),10);
			List<String> suggestedSongs = engine.recommendSongs(allUsers.get(0).getId(), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
