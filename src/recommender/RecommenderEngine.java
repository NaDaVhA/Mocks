package recommender;


import recommender.entites.Song;
import recommender.entites.SongsRecommendationScore;
import recommender.entites.User;
import recommender.entites.UsersSimilarityScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Hashtable;





public class RecommenderEngine {

	private List<User> allUsers;
	private List<Song> allSongs;
	
	private Hashtable<String, Hashtable<String, String>> userSongs; //[userId,[songId,songId]]
	private Hashtable<String, Hashtable<String, String>> songUsers; //[songId,[userId,userId]]
	
	private static RecommenderEngine instance = new RecommenderEngine();
	private boolean isInitalized = false;
	
	
	private RecommenderEngine(){
	}
	
	public static RecommenderEngine getInstance(){
		return instance;
	}
	
	public void initalize(List<User> allUsers, List<Song> allSongs){
		
		this.allSongs = allSongs;
		this.allUsers = allUsers;
		
		userSongs = new Hashtable<String, Hashtable<String,String>>();		 			
		for (User u : allUsers)
		{
			if (!userSongs.containsKey(u.getId()))
				userSongs.put(u.getId(), new Hashtable<String, String>());
				
			for (Song s : u.getListensTo())
			{
				if (!userSongs.get(u.getId()).containsKey(s.getId()))
					userSongs.get(u.getId()).put(s.getId(), s.getId());
				
			}						
		}
		
		
		songUsers = new Hashtable<String, Hashtable<String,String>>();
		for (Song s : allSongs)
		{
			if (!songUsers.containsKey(s.getId()))
				songUsers.put(s.getId(), new Hashtable<String, String>());
			
			for (User u : allUsers)
			{
				if (userSongs.get(u.getId()).containsKey(s.getId()))
					songUsers.get(s.getId()).put(u.getId(), u.getId());
			}
		}	
		
		isInitalized = true;
	}
			
	public List<String> recommendFriends(String userId, int topK) throws Exception
	{
		if (!isInitalized)
			throw new Exception("RecommenderEngine is not initalized");
		
		return getSimilarUsers(userId, topK, false);
	}
	
	public List<String> recommendSongs(String userId, int topK) throws Exception{
		
		if (!isInitalized)
			throw new Exception("RecommenderEngine is not initalized");
		
		User targetUser = getUserById(userId);
		List<String> similarUsers = getSimilarUsers(userId, (int)(allUsers.size()*0.05), true);		
		
		Hashtable<String,Integer> candidates = new Hashtable<String, Integer>();
		
		for (String similarUserId : similarUsers)
		{
			User similarUser = getUserById(similarUserId);
			for (Song s : similarUser.getListensTo())
			{
				if (!userSongs.get(targetUser.getId()).containsKey(s.getId()))
				{
					if (!candidates.containsKey(s.getId()))
						candidates.put(s.getId(), 0);
					
					candidates.put(s.getId(), candidates.get(s.getId())+1 );																
				}
			}
		}				
		
		List<SongsRecommendationScore> scores = new ArrayList<SongsRecommendationScore>();
		for (String songId : candidates.keySet())		
			scores.add(new SongsRecommendationScore(songId, targetUser, candidates.get(songId)));
				
		Collections.sort(scores);
		
		if (topK > scores.size())
			topK = scores.size();
		
		List<String> resultSet = new ArrayList<String>();
		for (int i=0;i<topK;i++)		
			resultSet.add(scores.get(i).getSongId());		
		
		return resultSet;
			
	}
		
	private List<String> getSimilarUsers(String userId, int topK, boolean includeFriends){
				
		User targetUser = getUserById(userId);
				
		List<UsersSimilarityScore> scores = new ArrayList<UsersSimilarityScore>();				
		for (User u : allUsers)
		{
			if (!u.equals(targetUser))
			{
				if (includeFriends)
				{
					UsersSimilarityScore similarityScore = getSimilarityScore(u,targetUser);
					scores.add(similarityScore);					
				}
				else
				{
					 if (!targetUser.isFriendWith(u))
					 {
						UsersSimilarityScore similarityScore = getSimilarityScore(u,targetUser);
						scores.add(similarityScore);					
					 }
				}
				
			}
		}
		
		Collections.sort(scores);
		
		if (topK > scores.size())
			topK = scores.size();
		
		List<String> resultSet = new ArrayList<String>();
		for (int i=0;i<topK;i++)		
			resultSet.add(scores.get(i).getUser().getId());		
		
		return resultSet;
	}
	
	private UsersSimilarityScore getSimilarityScore(User user, User targetUser) {
		
		int intersectionSize = 0;
		for (Song s : user.getListensTo())
		{
			if (userSongs.get(targetUser.getId()).containsKey(s.getId()))			
				intersectionSize++;								
		}
		
		return new UsersSimilarityScore(user,targetUser,intersectionSize);
	}

	private User getUserById(String userId){
		for (User u : allUsers)
		{
			if (u.getId().toLowerCase().equals(userId.toLowerCase()))
				return u;
		}
		return null;
	}
	
	
	
	
}
