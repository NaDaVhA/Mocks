package ui;

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
import core.ApplicationInterface;
import utilities.Pair;
import java.util.ArrayList;



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
	private Button recommend_me=null;
	private Button view_friend=null;
	private String friend_name;
	private Button delete_song=null;
	private Button delete_friend=null;
	private Composite c=null;
	private Composite c1=null;
	private Composite c2=null;
	
	private Thread t1,t2,t3,t4,t6,t7;
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

				final Pair<Integer, Pair<String, String>> status;
				status=engine.getStatusSong(engine.getUsername());
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(checkConnection(getShell(), status.getLeft())){
							status_song_label.setText("Status song:  "+status.getRight().getLeft()+" "+status.getRight().getRight());
							updateStatusSong(status.getRight());
							
							pool.remove(t1);
							if(pool.isEmpty()){
								getShell().layout();
							}
						}
						else{			
							pool.remove(t1);
							disposeScreen();
							logInScreen logIn=new logInScreen(getDisplay(),getShell(),engine);
							logIn.createScreen();
						}
					}
				});
				
			}
		}
		
		/*******/
		t1 = new Thread(new getStatusSong());
		pool.add(t1);
		t1.start();
	
		
	
		//username label
		user_label=new Label(getShell(), SWT.NONE);
		user_label.setAlignment(SWT.LEFT);
		
		user_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		user_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		user_label.setText("Username:  "+engine.getUsername());
		FormData data1 = new FormData ();
		data1.width=700;
		user_label.setLayoutData(data1);
		

		
		
		//sign out button
		sign_out=new Button(getShell(),SWT.NONE);
		sign_out.setText("Sign Out");
		FormData data2 = new FormData ();
		data2.width=110;
		data2.height=40;
		data2.left = new FormAttachment (user_label, 160);
		sign_out.setLayoutData(data2); 
		sign_out.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				disposeScreen();
				logInScreen logIn=new logInScreen(getDisplay(),getShell(),engine);
				logIn.createScreen();
			}
		});
		
		
		//Composite
		c=new Composite(getShell(), SWT.NONE);
		FormData data3 = new FormData ();
		data3.width=1000;
		data3.height=40;
		data3.top=new FormAttachment (user_label, 45);
		c.setLayoutData(data3);
		c.setLayout(new GridLayout(2, false));
		
		
		
		//status song label
		status_song_label=new Label(c, SWT.BORDER);
		status_song_label.setAlignment(SWT.LEFT);
		status_song_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		status_song_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		status_song_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
				
		
		
		/*******/
		class changeStatusSong implements Runnable {

			@Override
			public void run() {
				final Pair<Integer,Boolean> b;
				b=engine.changeStatusSong(song_selcted_table);

				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
						if(checkConnection(getShell(), b.getLeft())){
							if(b.getRight()){
								status_song_label.setText("Status song:  "+song_selcted_table.getLeft()+" "+song_selcted_table.getRight());
								updateStatusSong(song_selcted_table);
								
									pool.remove(t4);
									if(pool.isEmpty()){
										getShell().layout();
									}
								}
								else{
									pool.remove(t4);
									if(pool.isEmpty()){
										getShell().layout();
										errorPop("Status song error", "Failed to update status song");
									}
									
									
								}
						}
						
						else{ //problem with connection want to cont.
							pool.remove(t4);
							if(pool.isEmpty()){
								getShell().layout();
							}
						}
					
					}
				});
				}
				
			
		}
		
		/*******/
		
		//change status song button
		change_status_song=new Button(c,SWT.PUSH);
		change_status_song.setText("Change Status Song");
		status_song_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		change_status_song.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				if(song_selcted_table==null){
					errorPop("Error", "Please select a song from song list.\nIf you want to search for a new song press Add new Song.");
				}
				else{
				getShell().layout();
				t4 = new Thread(new changeStatusSong());
				pool.add(t4);
				t4.start();
				}
			}
		});
		

		//Composite
		c1=new Composite(getShell(), SWT.NONE);
		FormData data4 = new FormData ();
		data4.width=300;
		data4.height=300;
		data4.top=new FormAttachment (user_label, 145);
		data4.right = new FormAttachment (user_label, 425);
		c1.setLayoutData(data4);
		c1.setLayout(new GridLayout(2, false));
		
		
		//song list label
		song_list_label=new Label(c1, SWT.NONE);
		song_list_label.setAlignment(SWT.CENTER);
		song_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		song_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		song_list_label.setText("My Songs");
		song_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));


		//entertain me button
		recommend_me=new Button(c1,SWT.PUSH);
		recommend_me.setText("recommend Me!");
		recommend_me.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
				
		recommend_me.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				
				RecomendationsScreen recomendationsScreen = new RecomendationsScreen(getDisplay(),getShell(),engine);
				disposeScreen();
				recomendationsScreen.createScreen();
			}
		});
			
		
		
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
		
		GridData g=new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		g.heightHint=200;
		songList_t.setLayoutData(g);
		songList_t.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		songList_t.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				TableItem item=(TableItem)event.item;
				Pair<String, String> s=new Pair<String, String>(item.getText(0), item.getText(1));
				song_selcted_table=s;
			}
		});
				
				
		//add new song button
		add_new_song=new Button(c1,SWT.NONE);
		add_new_song.setText("Add New Song");
		
		add_new_song.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		add_new_song.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				disposeScreen();
				addNewSongScreen newSong=new addNewSongScreen(getDisplay(), getShell(),engine);
				newSong.createScreen();
			}
		});
		
		/*******/
		class deleteSong implements Runnable {

			@Override
			public void run() {
				
				final Pair<Integer, Boolean> b; 
				b=engine.removeSongFromUser(song_selcted_table.getRight(), song_selcted_table.getLeft());

				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
						if(checkConnection(getShell(), b.getLeft())){
							if(b.getRight()){
								
								songList_t.remove(songList_t.getSelectionIndices());
										
									pool.remove(t6);
									if(pool.isEmpty()){
										getShell().layout();
									}
								}
								else{
									pool.remove(t6);
									if(pool.isEmpty()){
										getShell().layout();
										errorPop("Delete song error", "Failed to delete song");
									}
										
								}
						}
						
						else{ //problem with connection want to cont.
							pool.remove(t6);
							if(pool.isEmpty()){
								getShell().layout();
							}
						}
					
					}
				});
				}
				
			
		}
		
		/*******/
		
		
		//delete song button	
		delete_song=new Button(c1,SWT.PUSH);
		delete_song.setText("Delete Song");
		delete_song.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		delete_song.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				if(song_selcted_table==null){
					errorPop("Error", "Please select a song from song list.\nIf you want to search for a new song press Add new Song.");
				}
				else{
					t6 = new Thread(new deleteSong());
					pool.add(t6);
					t6.start();
				}
			}  
		});
		
		
		//Composite
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
		friend_list_label.setText("My Friends");
		friend_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		
		//view friend button
		view_friend=new Button(c2,SWT.NONE);
		view_friend.setText("View friend");
		view_friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));		
		view_friend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				
				if(friend_name==null){
					errorPop("Error", "Please select a friend first.");
				}
				else{
				viewFriendScreen view=new viewFriendScreen(getDisplay(),getShell(),engine,friend_name);
				disposeScreen();
				view.createScreen();
				}
			}
		});
		
		
		
		//friend list	
		friendList=new List(c2, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		friendList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g1=new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		g1.heightHint=200;
		friendList.setLayoutData(g1);
		
		friendList.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				int [] selection = friendList.getSelectionIndices();
				for (int i=0; i<selection.length; i++) {
					friend_name=friendList.getItem(selection[i]);
					}
			}
		});
		
		
		
		//add new friend button
		add_new_friend=new Button(c2,SWT.PUSH);
		add_new_friend.setText("Add New Friend");
		add_new_friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
	
		add_new_friend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				disposeScreen();
				addNewFriendScreen newFriend=new addNewFriendScreen(getDisplay(), getShell(),engine);
				newFriend.createScreen();
			}
		});
		
		
	class deleteFriend implements Runnable {

		@Override
		public void run() {	
			final Pair<Integer, Boolean> b; 
			b=engine.removeFriendFromUser(friend_name);

			getDisplay().asyncExec(new Runnable() {
				public void run() {

					if(checkConnection(getShell(), b.getLeft())){
						if(b.getRight()){
							friendList.remove(friendList.getSelectionIndices());
							
								pool.remove(t7);
								if(pool.isEmpty()){

								getShell().layout();
								}
							}
							else{
								pool.remove(t7);
								if(pool.isEmpty()){
									getShell().layout();
									errorPop("Delete song error", "Failed to delete song");
								}
								
								
							}
					}
					
					else{ //problem with connection want to cont.
						pool.remove(t7);
						if(pool.isEmpty()){
							getShell().layout();
						}
					}
				
				}
			});
			}
			
		
	}
	
	/*******/
	
	
		//delete friend button
		delete_friend=new Button(c2,SWT.PUSH);
			delete_friend.setText("Delete Friend");
			delete_friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
			delete_friend.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					if(friend_name==null){
						errorPop("Error", "Please select a friend first.");
					}
					else{
						t7 = new Thread(new deleteFriend());
						pool.add(t7);
						t7.start();
					
					}
				}
			});
				
			
			
		
		/*******/
		class getSongList implements Runnable {

			@Override
			public void run() {
				
				final Pair<Integer, java.util.List<Pair<String, String>>> f;
				f=  engine.getSongList(engine.getUsername());

						
				getDisplay().asyncExec(new Runnable() {
					public void run() {

					if(checkConnection(getShell(), f.getLeft())){
							for(Pair<String,String> s:f.getRight()){
								TableItem item = new TableItem (songList_t, SWT.NONE);
								item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
								item.setText(0, s.getLeft());
								item.setText(1, s.getRight());
							}

							
							pool.remove(t2);
							if(pool.isEmpty()){
								getShell().layout();
							}
						}
					
						else{//problem with connection
							//back to log in
							pool.remove(t2);
							disposeScreen();
							logInScreen logIn=new logInScreen(getDisplay(),getShell(),engine);
							logIn.createScreen();
						}
						
				
					}
				});
				
			}
		}
		
		/*******/
		t2 = new Thread(new getSongList());
		pool.add(t2);
		t2.start();
				
		
		class getFriendList implements Runnable {

			@Override
			public void run() {
				final Pair<Integer, ArrayList<String>> f;
				f=engine.getFriendList(engine.getUsername());
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
					if(checkConnection(getShell(), f.getLeft())){
							for(String s:f.getRight()){
								friendList.add (s);
							}
							
							pool.remove(t3);
							
							if(pool.isEmpty()){
								getShell().layout();
							}	
						}
						else{
							//back to log in
							pool.remove(t3);
							disposeScreen();
							logInScreen logIn=new logInScreen(getDisplay(),getShell(),engine);
							logIn.createScreen();
						}
					}
				});
				
			}
		}
		
		/*******/
		 t3 = new Thread(new getFriendList());
		pool.add(t3);
		
		t3.start();

		this.getShell().layout();
		
	}
	
	@Override
	protected void disposeScreen(){
		this.c.dispose();
		this.c1.dispose();
		this.c2.dispose();
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
		this.recommend_me.dispose();
		this.delete_friend.dispose();
		this.delete_song.dispose();

	}


	private void updateStatusSong(Pair<String, String> song){
		this.status_song=song;
	
	}

}
