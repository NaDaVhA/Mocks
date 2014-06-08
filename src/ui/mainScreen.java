package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import utilities.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class mainScreen extends Screen{
	//members
	private String username;
	private Pair<String,String> status_song;
	
	private Label user_label=null;
	private Label status_song_label=null;
	private Label song_list_label=null;
	private Label friend_list_label=null;
	private Button sign_out=null;
	private Button add_new_song=null;
	private Button add_new_friend=null;
	private Button change_status_song=null;
	private Table  songList_t=null;
	private List friendList=null;
	
	private Button entertain_me=null;
	private Button view_friend=null;
	private String friend_name;
	
	//private Button recommendBtn=null;
	
	private Thread t1,t2,t3,t4,t5;
	
	//QAQA add here the rest
	private Pair<String, String> song_selcted_table=null;
	
	
	public mainScreen(Display display,Shell shell,ApplicationInterface engine,String username) {
		// TODO Auto-generated constructor stub
		super(display, shell, engine);
		this.username=username;
	
	}
	
	

	@Override
	public void createScreen() {
		
		/*******/
		class getStatusSong implements Runnable {

			@Override
			public void run() {
				//final Pair<String,String> status=theMusicalNetwork.nadav.getStatusSong(username);
				//final Pair<String,String> status=engine.getStatusSong(username); //1.6.14
				final Pair<Integer, Pair<String, String>> status;
				if(theMusicalNetwork.qaqa){
					status=theMusicalNetwork.nadav.getStatusSong(engine.getUsername());
				}
				else{ //real code
					
					// status=engine.getStatusSong();//7.6
					status=engine.getStatusSong(engine.getUsername());
				}
				//final Pair<String,String> status=engine.getStatusSong();
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;
						//7.6
					/*	status_song_label.setText("Status song:  "+status.getLeft()+" "+status.getRight());
						updateStatusSong(status);
						//Thread.currentThread();
						pool.remove(t1);
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}*/
						//7.6
						if(checkConnection(getShell(), status.getLeft())){
							status_song_label.setText("Status song:  "+status.getRight().getLeft()+" "+status.getRight().getRight());
							updateStatusSong(status.getRight());
							//Thread.currentThread();
							pool.remove(t1);
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}
						}
						else{
							//back to log in
						}
					}
				});
				
			}
		}
		
		/*******/
		 t1 = new Thread(new getStatusSong());
		pool.add(t1);
		t1.start();
		
		//this.status_song="qaqa -  status song";//qaqa add call to getStatusSong in interface
	
		//username label
		user_label=new Label(getShell(), SWT.NONE);
		user_label.setAlignment(SWT.LEFT);
		
		user_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		user_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		user_label.setText("Username:  "+this.username);
		FormData data1 = new FormData ();
		data1.width=700;
		
		//data1.right = new FormAttachment (35, 0);
		//data1.bottom = new FormAttachment (5, 0);
		user_label.setLayoutData(data1);
		
		/*******/
		class signout implements Runnable {

			@Override
			public void run() {
				//final Pair<String,String> status=theMusicalNetwork.nadav.getStatusSong(username);
				//final Pair<String,String> status=engine.getStatusSong(username); //1.6.14
				final boolean status;
				if(theMusicalNetwork.qaqa){
					status=theMusicalNetwork.nadav.signOutUser(theMusicalNetwork.nadav.getUsername());
				}
				else{ //real code
					 status=engine.signOutUser(engine.getUsername());
				}
				//final Pair<String,String> status=engine.getStatusSong();
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;	
						//Thread.currentThread();
						disposeScreen();
						logInScreen logIn=new logInScreen(getDisplay(),getShell(),engine);
						logIn.createScreen();
						
						pool.remove(t5);
						if(pool.isEmpty()){
							
							closeWaiting();
							//showScreen();
						}
					}
				});
				
			}
		}
		
		/*******/
		
		
		//sign out button
		sign_out=new Button(getShell(),SWT.NONE);
		sign_out.setText("Sign Out");
		FormData data2 = new FormData ();
		data2.width=110;
		data2.height=40;
		data2.right = new FormAttachment (97, 0);
		//data2.bottom = new FormAttachment (98, 0);
		sign_out.setLayoutData(data2); 
		sign_out.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed sign out");
				//signOutUser in interface qaqa
				 t5 = new Thread(new signout());
					pool.add(t5);
					t5.start();
			}
		});
		
		//status song label
		
		status_song_label=new Label(getShell(), SWT.BORDER);
		status_song_label.setAlignment(SWT.LEFT);
		
		status_song_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		status_song_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		
		FormData data3 = new FormData ();
		data3.width=700;
		
		//data2.right = new FormAttachment (10, 0);
		data3.bottom = new FormAttachment (20, 0);
		status_song_label.setLayoutData(data3);
		
		//change status song button
		
		/*******/
		class changeStatusSong implements Runnable {

			@Override
			public void run() {
				
				//final boolean b=theMusicalNetwork.nadav.changeStatusSong(username, song_selcted_table);
				final Pair<Integer,Boolean> b;
				if(theMusicalNetwork.qaqa){
					b=theMusicalNetwork.nadav.changeStatusSong(song_selcted_table);
				}
				else{
					b=engine.changeStatusSong(song_selcted_table);
				}
				//final boolean b=engine.changeStatusSong(song_selcted_table);
				
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;
						if(checkConnection(getShell(), b.getLeft())){
							if(b.getRight()){
								status_song_label.setText("Status song:  "+song_selcted_table.getLeft()+" "+song_selcted_table.getRight());
								updateStatusSong(song_selcted_table);
								//Thread.currentThread();
									pool.remove(t4);
									if(pool.isEmpty()){
										
										closeWaiting();
										showScreen();
									}
								}
								else{
									pool.remove(t4);
									if(pool.isEmpty()){
										
										closeWaiting();
										showScreen();
										errorPop("Error", "Failed to update status song");
									}
									
									
								}
						}
						
						else{ //problem with connection want to cont.
							pool.remove(t4);
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}
						}
					
					}
				});
				}
				
			
		}
		
		/*******/
		
		change_status_song=new Button(getShell(),SWT.NONE);
		change_status_song.setText("Change Status Song");
		FormData data4 = new FormData ();
		data4.width=115;
		data4.height=30;
		data4.right = new FormAttachment (83, 0);
		data4.bottom = new FormAttachment (20, 0);
		change_status_song.setLayoutData(data4); 
		change_status_song.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed change status song");
				if(song_selcted_table==null){
					errorPop("Error", "Please select a song from song list.\nIf you want to search for a new song press Add new Song.");
				}
				else{
				openWaiting();
				t4 = new Thread(new changeStatusSong());
				pool.add(t4);
				t4.start();
				}
			}
		});
		

		//song list label
		
		song_list_label=new Label(getShell(), SWT.NONE);
		song_list_label.setAlignment(SWT.CENTER);
		song_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		song_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		song_list_label.setText("My Songs");
		FormData data5 = new FormData ();
		data5.width=100;
		
		data5.right = new FormAttachment (23, 0);
		data5.bottom = new FormAttachment (35, 0);
		song_list_label.setLayoutData(data5);
		
		//add new song button
		add_new_song=new Button(getShell(),SWT.NONE);
		add_new_song.setText("Add New Song");
		FormData data6 = new FormData ();
		data6.width=115;
		data6.height=30;
		data6.right = new FormAttachment (40, 0);
		data6.bottom = new FormAttachment (35, 0);
		add_new_song.setLayoutData(data6); 
		add_new_song.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed add new song");
				disposeScreen();
				//hideScreen();
				addNewSongScreen newSong=new addNewSongScreen(getDisplay(), getShell(),engine);
				newSong.createScreen();
			}
		});
		
		
		//friend list label
		
		friend_list_label=new Label(getShell(), SWT.NONE);
		friend_list_label.setAlignment(SWT.CENTER);
		friend_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		friend_list_label.setText("My Friends");
		FormData data7 = new FormData ();
		data7.width=150;
		
		data7.right = new FormAttachment (75, 0);
		data7.bottom = new FormAttachment (35, 0);
		friend_list_label.setLayoutData(data7);
		
		//add new friend button
		add_new_friend=new Button(getShell(),SWT.NONE);
		add_new_friend.setText("Add New Friend");
		FormData data8 = new FormData ();
		data8.width=115;
		data8.height=30;
		data8.right = new FormAttachment (90, 0);
		data8.bottom = new FormAttachment (35, 0);
		add_new_friend.setLayoutData(data8); 
		add_new_friend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed add new friend");
				disposeScreen();
				addNewFriendScreen newFriend=new addNewFriendScreen(getDisplay(), getShell(),engine);
				newFriend.createScreen();
			}
		});
		
		//song list
		/*songList_t= new Table (getShell(), SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		songList_t.setLinesVisible (true);
		songList_t.setHeaderVisible (true);
		TableColumn column = new TableColumn (songList_t, SWT.NONE);
		column.setText ("Artist");
		TableColumn column1 = new TableColumn (songList_t, SWT.NONE);
		column1.setText ("Song name");*/
		songList_t= new Table (getShell(), SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		songList_t.setLinesVisible (true);
		songList_t.setHeaderVisible (true);
		TableColumn column = new TableColumn (songList_t, SWT.NONE);
		column.setText ("Artist");
		TableColumn column1 = new TableColumn (songList_t, SWT.NONE);
		column1.setText ("Song name");
		column.setWidth(135);
		column1.setWidth(135);
		
		
		FormData data9 = new FormData ();
		data9.width=250;
		data9.height=200;
		data9.right = new FormAttachment (40, 0);
		data9.bottom = new FormAttachment (83, 0);
		songList_t.setLayoutData(data9);
		songList_t.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		songList_t.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				System.out.println(event.item);//qaqa
				TableItem item=(TableItem)event.item;
				System.out.println(item.getText(0));//qaqa
				System.out.println(item.getText(1));//qaqa
				Pair<String, String> s=new Pair<String, String>(item.getText(0), item.getText(1));
				song_selcted_table=s;
			}
		});
		
		/*******/
		class getSongList implements Runnable {

			@Override
			public void run() {
				
			//	final ArrayList<Pair<String,String>> f= engine.getSongList(username); //1.6.14
				final Pair<Integer, java.util.List<Pair<String, String>>> f;
				if(theMusicalNetwork.qaqa){
					f=  theMusicalNetwork.nadav.getSongList("TT");
				}
				else{
					//f= (ArrayList<Pair<String, String>>) engine.getSongList(); //7.6
					f=  engine.getSongList(engine.getUsername());
					
				}
				//final ArrayList<Pair<String,String>> f= (ArrayList<Pair<String, String>>) engine.getSongList();
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
					//7.6
					/*	for(Pair<String,String> s:f){
							//friendList.add (s);
							TableItem item = new TableItem (songList_t, SWT.NONE);
							item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
							//item.setText(0, "artist "+s.getLeft());
							//item.setText(1, "songggggggggggggggggggggggggggggggggggg "+s.getRight());
							item.setText(0, s.getLeft());
							item.setText(1, s.getRight());
						}
						
						
						//System.out.println(pool.size());
						
						pool.remove(t2);
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}*/
						//7.6
						if(checkConnection(getShell(), f.getLeft())){
							for(Pair<String,String> s:f.getRight()){
								//friendList.add (s);
								TableItem item = new TableItem (songList_t, SWT.NONE);
								item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
								//item.setText(0, "artist "+s.getLeft());
								//item.setText(1, "songggggggggggggggggggggggggggggggggggg "+s.getRight());
								item.setText(0, s.getLeft());
								item.setText(1, s.getRight());
							}
							
							
							//System.out.println(pool.size());
							
							pool.remove(t2);
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}
						}
						else{
							//back to log in
						}
						
				
					}
				});
				
			}
		}
		
		/*******/
		t2 = new Thread(new getSongList());
		pool.add(t2);
		t2.start();
		
		/*for (int i=0; i<128; i++) {
			TableItem item = new TableItem (songList_t, SWT.NONE);
			item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			item.setText(0, "artist "+i);
			item.setText(1, "songggggggggggggggggggggggggggggggggggg "+i);
		}
		songList_t.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data9 = new FormData ();
		data9.width=250;
		data9.height=200;
		data9.right = new FormAttachment (40, 0);
		data9.bottom = new FormAttachment (83, 0);
		songList_t.setLayoutData(data9);
		
		column.setWidth(125);
		column1.setWidth(125); */
		
		//songList_t.getColumn (0).pack ();
		//songList_t.getColumn (1).pack ();
		
		//friend list
		friendList=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		/*******/
		class getFriendList implements Runnable {

			@Override
			public void run() {
				final Pair<Integer, ArrayList<String>> f;
				if(theMusicalNetwork.qaqa){
					 f=theMusicalNetwork.nadav.getFriendList("TT");
				}
				else{
					//f=engine.getFriendList(); //7.6
					f=engine.getFriendList(engine.getUsername());
				}
				
				//final ArrayList<String> f=engine.getFriendList();
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//7.6
					/*	for(String s:f){
							friendList.add (s);
						}
						
						pool.remove(t3);
						
						System.out.println(pool.size());
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}*/
						//7.6
						if(checkConnection(getShell(), f.getLeft())){
							for(String s:f.getRight()){
								friendList.add (s);
							}
							
							pool.remove(t3);
							
							System.out.println(pool.size());
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}	
						}
						else{
							//back to log in
						}
					}
				});
				
			}
		}
		
		/*******/
		 t3 = new Thread(new getFriendList());
		pool.add(t3);
		
		t3.start();
		
		//LinkedList<String> l = theMusicalNetwork.nadav.getFriendList("nadav"); //QAQA CHANGE WITH REAL friend
	/*	for(String s:l){
			friendList.add (s);
		}*/
		//for (int i=0; i<l.size(); i++) friendList.add (l.);
		
		friendList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data10 = new FormData ();
		data10.width=270;
		data10.height=200;
		data10.right = new FormAttachment (90, 0);
		data10.bottom = new FormAttachment (80, 0);
		friendList.setLayoutData(data10);
		
		friendList.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				int [] selection = friendList.getSelectionIndices ();
				for (int i=0; i<selection.length; i++) {
					System.out.println(friendList.getItem(selection[i])); //qaqa
					friend_name=friendList.getItem(selection[i]);
					}
			}
		});
		
		//entertain me button
		entertain_me=new Button(getShell(),SWT.NONE);
		entertain_me.setText("Recomend Me!");
		FormData data11 = new FormData ();
		data11.width=115;
		data11.height=30;
		data11.right = new FormAttachment (32, 0);
		data11.bottom = new FormAttachment (90, 0);
		entertain_me.setLayoutData(data11); 
		entertain_me.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed entertain me");
				RecomendationsScreen recomendationsScreen = new RecomendationsScreen(getDisplay(),getShell(),engine);
				disposeScreen();
				recomendationsScreen.createScreen();
			}
		});
		
	
		
		
		
		//whats humming button
		view_friend=new Button(getShell(),SWT.NONE);
		view_friend.setText("View friend");
		FormData data12 = new FormData ();
		data12.width=115;
		data12.height=30;
		data12.right = new FormAttachment (82, 0);
		data12.bottom = new FormAttachment (90, 0);
		view_friend.setLayoutData(data12); 
		view_friend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed view friend");
				if(friend_name==null){
					errorPop("Error", "Please select a friend first.");
				}
				else{
				//hideScreen();
				viewFriendScreen view=new viewFriendScreen(getDisplay(),getShell(),engine,friend_name);
				disposeScreen();
				view.createScreen();
				}
			}
		});
		
	/*	while(!pool.isEmpty())	{
		Thread t=pool.peek();
		if(t!=null){
		t.start();
		System.out.println("started thread");
		while(t.isAlive()){
			
			}
		pool.poll();
		}
		}*/
		//status_song_label.setText("Status song:  "+this.status_song.getLeft()+" "+this.status_song.getRight());
		//System.out.println(this.status_song.getLeft()==null);
		openWaiting();
		this.getShell().layout();
		
	}
	
	@Override
	protected void disposeScreen(){
		this.add_new_friend.dispose();
		this.add_new_song.dispose();
		this.change_status_song.dispose();
		this.friend_list_label.dispose();
		this.friendList.dispose();
		this.sign_out.dispose();
		this.song_list_label.dispose();
		this.songList_t.dispose();
		this.status_song_label.dispose();
		this.user_label.dispose();
		this.view_friend.dispose();
		this.entertain_me.dispose();
		//this.recommendBtn.dispose();
	}



	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		this.add_new_friend.setVisible(false);
		this.add_new_song.setVisible(false);
		this.change_status_song.setVisible(false);
		this.friend_list_label.setVisible(false);
		this.friendList.setVisible(false);
		this.sign_out.setVisible(false);
		this.song_list_label.setVisible(false);
		this.songList_t.setVisible(false);
		this.status_song_label.setVisible(false);
		this.user_label.setVisible(false);
		this.view_friend.setVisible(false);
		this.entertain_me.setVisible(false);
		this.getShell().layout();
	}



	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		this.add_new_friend.setVisible(true);
		this.add_new_song.setVisible(true);
		this.change_status_song.setVisible(true);
		this.friend_list_label.setVisible(true);
		this.friendList.setVisible(true);
		this.sign_out.setVisible(true);
		this.song_list_label.setVisible(true);
		this.songList_t.setVisible(true);
		this.status_song_label.setVisible(true);
		this.user_label.setVisible(true);
		this.view_friend.setVisible(true);
		this.entertain_me.setVisible(true);
		this.getShell().layout();
		
	}
	
	private void updateStatusSong(Pair<String, String> song){
		this.status_song=song;
	
	}

}
