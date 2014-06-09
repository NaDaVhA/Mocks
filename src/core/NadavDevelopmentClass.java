package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import ui.initUpdateScreen;
import utilities.Pair;

/**
 * 
 * @author Nadav    -  THIS CLASS IS JUST FOR TESTING THE GUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */

public class NadavDevelopmentClass implements ApplicationInterface {

	@Override
	public Pair<Integer, Boolean> isUserRegisterd(String username, String password) {
		// TODO Auto-generated method stub
		int i=0;
		while(i<100000){
			i++;
			//System.out.println(i);
		}
		if((username.compareTo("a")==0)&&password.compareTo("a")==0){
			return new Pair<Integer, Boolean>(0, true);
		}
		return new Pair<Integer, Boolean>(0, false);
	}

	@Override
	public Pair<Integer, Boolean> isUsernameTaken(String username) {
		// TODO Auto-generated method stub
				int i=0;
				while(i<1000){
					i++;
					System.out.println(i);
				}
				if((username.compareTo("nadav")==0)){
					return new Pair<Integer, Boolean>(0, true);
				}
				return new Pair<Integer, Boolean>(0, false);
	}

	@Override
	public Pair<Integer, Boolean> signUpUser(String username, String password) {
		// TODO Auto-generated method stub
				int i=0;
				while(i<1000){
					i++;
					System.out.println(i);
				}
				if((username.compareTo("nadavi")==0)&&password.compareTo("as")==0){
					return new Pair<Integer, Boolean>(0, true);
				}
				return new Pair<Integer, Boolean>(0, false);
	}

	@Override
	public Pair<Integer, Pair<String, String>> getStatusSong(String username) {
		// TODO Auto-generated method stub
		if(username.compareTo("friend 4")==0){
			return new Pair<Integer, Pair<String, String>>(-1, new Pair<String, String>("song artist", "song title"));
		}
		return new Pair<Integer, Pair<String, String>>(-1, new Pair<String, String>("", ""));
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
	public Pair<Integer, ArrayList<String>> getSearchResultsFriends(String friend_name) {
		// TODO Auto-generated method stub
		ArrayList< String> l=new ArrayList<String>();
		for(int i=0;i<10;i++){
			l.add("friend "+i);
		}
		return new Pair<Integer, ArrayList<String>>(0, l);
	
	}

	@Override
	public Pair<Integer, Boolean> addFriend(String username) {
		// TODO Auto-generated method stub
		return new Pair<Integer, Boolean>(0, true);
	}

	@Override
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsByArtist(String artist_name) {
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist by a "+i, "song by a "+i));
		}
		return  new Pair<Integer, ArrayList<Pair<String, String>>>(0, l);
	}
	
	@Override
	public Pair<Integer, ArrayList<Pair<String, String>>> getSearchResultsBySong(String song_name) {
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist by s "+i, "song by s"+i));
		}
		return new Pair<Integer, ArrayList<Pair<String, String>>>(0,l);
	}




	@Override
	public Pair<Integer, Boolean> initializeApplication() {
		// TODO Auto-generated method stub
		
		int i,j;
		for(i=0;i<1000;i++){
			initUpdateScreen.updateProgressBar();
			j=0;
			while(j<10000){
				j++;
				//System.out.println(j);
			}
		}
		
		//initUpdateScreen.setFinished(true);
		return new Pair<Integer, Boolean>(0, true);
	}

	@Override
	public Pair<Integer, Boolean> updateMusicDatabase() {
		// TODO Auto-generated method stub
		int i,j;
		for(i=0;i<1000;i++){
			initUpdateScreen.updateProgressBar();
			j=0;
			while(j<10000){
				j++;
				//System.out.println(j);
			}
		}
		
		//initUpdateScreen.setFinished(true);
		return new Pair<Integer, Boolean>(0, true);
	}

	@Override
	public Pair<String, String> getStatusSong() {
		// TODO Auto-generated method stub
		return new Pair<String, String>("this artist", "this song");
	}

	@Override
	public Pair<Integer, Boolean> changeStatusSong(Pair<String, String> song) {
		// TODO Auto-generated method stub
		return new Pair<Integer, Boolean>(-1, false);
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
	public Pair<Integer, Boolean> addSong(String song, String artist) {
		// TODO Auto-generated method stub
		int i=0;
		while(i<100000){
			i++;
			System.out.println(i);
		}
		return new Pair<Integer, Boolean>(0, false);
	}

	@Override
	public Pair<Integer, List<Pair<String, String>>> getSongList(String username) {
		
		ArrayList<Pair<String, String>> l=new ArrayList<Pair<String,String>>();
		for(int i=0;i<10;i++){
			l.add(new Pair<String, String>("artist user "+i, "song user "+i));
		}
		return new Pair<Integer, List<Pair<String, String>>>(-1,l);
	
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return "nadav qa";
	}

	@Override
	public void terminateDBConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Pair<Integer, ArrayList<String>> getFriendList(String friend_name) {
		// TODO Auto-generated method stub
		ArrayList< String> l=new ArrayList<String>();
		for(int i=0;i<10;i++){
			l.add("friend "+i);
		}
		if(friend_name.compareTo("friend 4")==0){
			return new Pair<Integer, ArrayList<String>>(-1, l);
		}
		return new Pair<Integer, ArrayList<String>>(-1, l);
	}

	@Override
	public Pair<Integer, Boolean> removeFriendFromUser(String friendusername) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Integer, Boolean> removeSongFromUser(String songname,
			String artistname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void InitalizeRecommender() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Pair<Integer, Boolean> checkInitializationStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	


	@Override
	public Pair<Integer, Boolean> sendMassage(String username_send,
			String username_receive, String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Integer, ArrayList<String[]>> getHistoryMassages(
			String username_send, String username_receive) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
