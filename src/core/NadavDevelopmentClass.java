package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import ui.Application;
import utilities.Pair;

/**
 * 
 * @author Nadav    -  THIS CLASS IS JUST FOR TESTING THE GUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */

public class NadavDevelopmentClass implements Application {

	@Override
	public boolean isUserRegisterd(String username, String password) {
		// TODO Auto-generated method stub
		int i=0;
		while(i<1000){
			i++;
			System.out.println(i);
		}
		if((username.compareTo("nadav")==0)&&password.compareTo("as")==0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isUsernameTaken(String username) {
		// TODO Auto-generated method stub
				int i=0;
				while(i<1000){
					i++;
					System.out.println(i);
				}
				if((username.compareTo("nadav")==0)){
					return true;
				}
				return false;
	}

	@Override
	public boolean signUpUser(String username, String password) {
		// TODO Auto-generated method stub
				int i=0;
				while(i<1000){
					i++;
					System.out.println(i);
				}
				if((username.compareTo("nadavi")==0)&&password.compareTo("as")==0){
					return true;
				}
				return false;
	}

	@Override
	public Pair<String, String> getStatusSong(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeStatusSong(String username,String song) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getSongList(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<String> getFriendList(String username) {
		// TODO Auto-generated method stub
		int j=0;
		while(j<1000){
			j++;
			System.out.println(j);
		}
		LinkedList<String> l=new LinkedList<String>();
		if(username.compareTo("nadav")==0){
			
			for(int i=0;i<100;i++){
				l.add("friend "+i);
			}
		}
		return l;
	}

	@Override
	public boolean signOutUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getSearchResultsFriends(String friend_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addFriend(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getSearchResultsByArtist(String artist_name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getSearchResultsBySong(String song_name) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean addSong(String username, String artist, String song_name) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
