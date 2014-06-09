package ui;

import java.util.ArrayList;





import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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

import recommender.RecommenderEngineAdapter;
import utilities.Pair;
import core.ApplicationInterface;

public class RecomendationsScreen extends Screen {


	private Label head=null;
	private Label song_list_label=null;
	private Label friend_list_label=null;
	private Button add_new_song=null;
	private Button add_new_friend=null;
	private Table  songList_t=null;
	private List friendList=null;	
	private Button back_button=null;
	
	private Pair<String, String> song_selcted_table=null;
	private String friend_name_selected;
	private Thread t13,t14,t15;
	
	

	private Composite c1=null;
	private Composite c2=null;
	
////////////////////////////////////////////////////////////////
	private java.util.List<String> recommendedFriends = null;
	private java.util.List<Pair<String, String>> recommendedSongs = null;
	private String loggedInUserName;
	//private Pair<String,String> chosenSong;
	//private String chosenFriend;
	
	
	
	
	//private Thread thread1;
	//private Thread addFriendThread;

	public RecomendationsScreen(Display display, Shell shell, ApplicationInterface engine) {
		super(display, shell, engine);
		// Get Logged In Username
		loggedInUserName = engine.getUsername();


		
		
		System.out.println("RecomendationsScreen-constructor()");
	}

	@Override
	public void createScreen() {

		//username label
		head=new Label(getShell(), SWT.NONE);
		head.setAlignment(SWT.RIGHT);
		
		head.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		head.setFont(SWTResourceManager.getFont("MV Boli", 20, SWT.BOLD));
		head.setText("recommended songs and friends");
		FormData data1 = new FormData ();
		data1.width=700;
		//data1.right=new FormAttachment (20);
	
		head.setLayoutData(data1);
		

		//composite
		
		c1=new Composite(getShell(), SWT.NONE);
		FormData data4 = new FormData ();
		data4.width=300;
		data4.height=300;
		data4.top=new FormAttachment (head, 100);
		data4.right = new FormAttachment (head, 400);
		c1.setLayoutData(data4);
		c1.setLayout(new GridLayout(2, false));
		
		
		
		

		//song list label
		
		song_list_label=new Label(c1, SWT.NONE);
		song_list_label.setAlignment(SWT.CENTER);
		song_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		song_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		song_list_label.setText("Recommended Songs");
		song_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
	
		


		
		//song list
		
			songList_t= new Table (c1, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
			songList_t.setLinesVisible (true);
			songList_t.setHeaderVisible (true);
			TableColumn column = new TableColumn (songList_t, SWT.NONE);
			column.setText ("Artist");
			TableColumn column1 = new TableColumn (songList_t, SWT.NONE);
			column1.setText ("Song name");
			column.setWidth(143);
			column1.setWidth(143);
			songList_t.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			GridData g=new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
			g.heightHint=200;
			songList_t.setLayoutData(g);
			
			
			songList_t.setLayoutData(g);
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
			class addSong implements Runnable {

				@Override
				public void run() {
					final Pair<Integer,Boolean> b;
					if(theMusicalNetwork.qaqa){
						b=theMusicalNetwork.nadav.addSong(song_selcted_table.getRight(),song_selcted_table.getLeft());
					}
					else{//true code
						b=engine.addSong(song_selcted_table.getRight(),song_selcted_table.getLeft());
					}
					
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							System.out.println("LL "+b.getRight());
							if(checkConnection(getShell(),b.getLeft())){
								if(!b.getRight()){
									closeWaiting();
									showScreen();
									errorPop("Error", "Failed to add song.");	
									pool.remove(t14);
								}
								else{
									
									pool.remove(t14);
									PopUpinfo(getShell(),"Added Song", "The song was added to your song list successfully!");
									if(pool.isEmpty()){
										closeWaiting();
										showScreen();
									}
									
								}	
							}
							
							else{//want to cont.
								//stay in screen
								//popSync=false;
								pool.remove(t14);
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
			
			
			//add new song button
				add_new_song=new Button(c1,SWT.PUSH);
				add_new_song.setText("Add Song");
				
				add_new_song.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
				add_new_song.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected (SelectionEvent e) {
						System.out.println("qaqa - pressed add song");
						//disposeScreen();
						//hideScreen();
						//addNewSongScreen newSong=new addNewSongScreen(getDisplay(), getShell(),engine);
						//newSong.createScreen();
						if(song_selcted_table==null){
							errorPop("Error", "Please select a song first");
						}
						else{
						t14 = new Thread(new addSong());
						pool.add(t14);
						t14.start();
						}
					}
				});
				
				c2=new Composite(getShell(), SWT.NONE);
				FormData data5 = new FormData ();
				data5.width=300;
				data5.height=300;
				data5.top=new FormAttachment (c1, 0,SWT.TOP);
				data5.left = new FormAttachment (c1, 200);
				c2.setLayoutData(data5);
				c2.setLayout(new GridLayout(2, false));
		

		
		
		
		
		//friend list label
		
		friend_list_label=new Label(c2, SWT.NONE);
		friend_list_label.setAlignment(SWT.CENTER);
		friend_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		friend_list_label.setText("Recommended Friends");
		friend_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		
		
	
		
		//friend list
		
		friendList=new List(c2, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		friendList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g1=new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		g1.heightHint=200;
		friendList.setLayoutData(g1);
		
		friendList.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				//System.out.println(event.data);//qaqa
				int[] selection = friendList.getSelectionIndices();
				
				for (int i=0; i<selection.length; i++) {
					System.out.println(friendList.getItem(selection[i])); //qaqa
						friend_name_selected=friendList.getItem(selection[i]);
					}
				
			}
		});	
	
		
	class addFriend implements Runnable {

		@Override
		public void run() {
			final Pair<Integer,Boolean> add;
			if(theMusicalNetwork.qaqa){
				add=theMusicalNetwork.nadav.addFriend(friend_name_selected);
			}
			else{
				add=engine.addFriend(friend_name_selected);
			}
	
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					//status_song=status;
					if(checkConnection(getShell(), add.getLeft())){
						if(!add.getRight()){
							closeWaiting();
							showScreen();
							errorPop("Error", "Failed to add friend.");	
							pool.remove(t15);
						}
						else{
							
							pool.remove(t15);
							PopUpinfo(getShell(),"Add Friend", "The friend was added to your friend list successfully!!");
							if(pool.isEmpty()){
								closeWaiting();
								showScreen();
								
							}
							
						}
					}
					else{
						pool.remove(t15);
						if(pool.isEmpty()){
							closeWaiting();
							showScreen();
							//PopUpinfo(getShell(),"added Friend", "qaqa-succes!!!");
						}
					}
					
					
					
				}
			});
			
		}
	}
	
	/*******/

		
		
		//add new friend button
	
	add_new_friend=new Button(c2,SWT.PUSH);
	add_new_friend.setText("Add friend");
	add_new_friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
	
	add_new_friend.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected (SelectionEvent e) {
			System.out.println("qaqa - pressed add_new_friend");
			 t15 = new Thread(new addFriend());
				pool.add(t15);	
				t15.start();
		}
	});
		
	
		
		
		//back button
			back_button=new Button(getShell(),SWT.NONE);
			back_button.setText("Go back to home screen");
			FormData data13 = new FormData ();
			data13.width=180;
			data13.height=30;
			//data12.right = new FormAttachment (87, 0);
			data13.top = new FormAttachment (head, 525);
			back_button.setLayoutData(data13); 
			back_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					System.out.println("qaqa - pressed back home");
					disposeScreen();
					if(theMusicalNetwork.qaqa){
					mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,theMusicalNetwork.nadav.getUsername()); //QAQA ADD USERNAME
					mainScreen.createScreen();
					}
					else{
						mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,engine.getUsername()); //QAQA ADD USERNAME
						mainScreen.createScreen();	
					}
				}
			});
			
			
			class runRecommendations implements Runnable {

				@Override
				public void run() {

					//here call to run recommendations
					engine.InitalizeRecommender();  //qaqa is this a problem to call to build all the songs and users every time
					
					// Get Recommendations From RecommenderEngine
					runRecommendations();
					
					getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
					// Update UI
					for (String recommendedUsername : recommendedFriends) {
						friendList.add(recommendedUsername);	
					}
					// Update UI
					for (Pair<String, String> song : recommendedSongs) {
						TableItem item = new TableItem(songList_t, SWT.NONE);
						item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
						item.setText(0, song.getLeft());
						item.setText(1, song.getRight());
					}
						pool.remove(t13);
						if(pool.isEmpty()){
							closeWaiting();
							showScreen();
							//PopUpinfo(getShell(),"added Friend", "qaqa-succes!!!");
					}
							
					}
						
	
						
				});
					
			}
		}
			
			/*******/
		
			
		t13 = new Thread(new runRecommendations());
		pool.add(t13);
		
		t13.start();
				
	
		openWaiting();
		this.getShell().layout();
		
		
	}

	private void runRecommendations() {

		// Get Friends Recommendations
		try {
			recommendedFriends = RecommenderEngineAdapter.getInstance().recommendFriends(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedFriends = new ArrayList<>();
		}

		
		// Get Songs Recommendations
		try {
			recommendedSongs = RecommenderEngineAdapter.getInstance().recommendSongs(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedSongs = new ArrayList<>();
		}

	
	}

	
	
	@Override
	protected void disposeScreen() {
		System.out.println("RecomendationsScreen-disposeScreen()");
		this.c1.dispose();
		this.c2.dispose();
		this.head.dispose();
		this.song_list_label.dispose();
	this.friend_list_label.dispose();
	this.add_new_song.dispose();
	this.add_new_friend.dispose();
	this.songList_t.dispose();
	this.friendList.dispose();
	this.back_button.dispose();
		

	}

	@Override
	protected void hideScreen() {
		System.out.println("RecomendationsScreen-hideScreen()");
	}

	@Override
	protected void showScreen() {
		System.out.println("RecomendationsScreen-showScreen()");
	}

}
