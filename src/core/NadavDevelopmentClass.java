package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


import utilities.Pair;

/**
 * 
 * @author Nadav    -  THIS CLASS IS JUST FOR TESTING THE GUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */

public class NadavDevelopmentClass implements ApplicationInterface {

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
		if(username.compareTo("chen")==0){
			return new Pair<String, String>("song artist", "song title");
		}
		return new Pair<String, String>("", "");
	}





	@Override
	public ArrayList<String> getFriendList() {
		// TODO Auto-generated method stub
		int j=0;
		while(j<1000){
			j++;
			System.out.println(j);
		}
		ArrayList<String> l=new ArrayList<String>();
		//if(username.compareTo("nadav")==0){
			
			for(int i=0;i<100;i++){
				l.add("friend "+i);
			}
		//}
		return l;
	}

	@Override
	public boolean signOutUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> getSearchResultsFriends(String friend_name) {
		// TODO Auto-generated method stub
		ArrayList< String> l=new ArrayList<String>();
		for(int i=0;i<10;i++){
			l.add("friend "+i);
		}
		return l;
	
	}

	@Override
	public boolean addFriend(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Pair<String, String>> getSearchResultsByArtist(String artist_name) {
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist by a "+i, "song by a "+i));
		}
		return l;
	}
	
	@Override
	public ArrayList<Pair<String, String>> getSearchResultsBySong(String song_name) {
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist by s "+i, "song by s"+i));
		}
		return l;
	}




	@Override
	public boolean initializeApplication() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateMusicDatabase() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Pair<String, String> getStatusSong() {
		// TODO Auto-generated method stub
		return new Pair<String, String>("this artist", "this song");
	}

	@Override
	public boolean changeStatusSong(Pair<String, String> song) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Pair<String, String>> getSongList() {
		// TODO Auto-generated method stub
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist "+i, "song "+i));
		}
		return l;
	}



	@Override
	public boolean addSong(String song, String artist) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Pair<String, String>> getSongList(String username) {
		
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist user "+i, "song user "+i));
		}
		return l;
	
	}

	

}
