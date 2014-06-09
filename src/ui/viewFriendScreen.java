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

import utilities.Pair;
import core.ApplicationInterface;

public class viewFriendScreen extends Screen {
	
	private String username;
	private Pair<String,String> status_song;
	
	private Label user_label=null;
	private Label status_song_label=null;
	private Label song_list_label=null;
	private Label friend_list_label=null;
	private Button add_new_song=null;
	private Button add_new_friend=null;
	private Table  songList_t=null;
	private List friendList=null;
		
	private Button back_button=null;
	
	
	
	private Composite c=null;
	private Composite c1=null;
	private Composite c2=null;
	
	
	
private Thread t11,t12,t13,t14,t15;
	
	//QAQA add here the rest
	private Pair<String, String> song_selcted_table=null;
	
	private String friend_user_name;
	private String friend_name_selected;
	
	public viewFriendScreen(Display display,Shell shell , ApplicationInterface engine,String username){
		super(display, shell, engine);
		this.friend_user_name=username;
	}

	@Override
	public void createScreen() {
		// TODO Auto-generated method stub

		/*******/
		class getStatusSong implements Runnable {

			@Override
			public void run() {
				
				final Pair<Integer,Pair<String,String>> status;
				if(theMusicalNetwork.qaqa){
					status=theMusicalNetwork.nadav.getStatusSong(friend_user_name);
				}
				else{ //real code
					 status=engine.getStatusSong(friend_user_name);
				}
				
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;
						if(checkConnection(getShell(), status.getLeft())){
							status_song_label.setText("Status song:  "+status.getRight().getLeft()+" "+status.getRight().getRight());
							//updateStatusSong(status);
							//Thread.currentThread();
							pool.remove(t11);
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}
						}
						
						else{//want to cont.
							pool.remove(t11);
							if(pool.isEmpty()){
								
								//back to main screen
								closeWaiting();
								disposeScreen();
								if(theMusicalNetwork.qaqa){
									mainScreen main=new mainScreen(getDisplay(), getShell(), engine, theMusicalNetwork.nadav.getUsername());
									main.createScreen();
								}
								else{
									mainScreen main=new mainScreen(getDisplay(), getShell(), engine, engine.getUsername());
									main.createScreen();	
								}
								
							}
						}
						
					}
				});
				
			}
		}
		
		/*******/
		
		
		
	
		//username label
		user_label=new Label(getShell(), SWT.NONE);
		user_label.setAlignment(SWT.LEFT);
		
		user_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		user_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		user_label.setText("Username:  "+this.friend_user_name);
		FormData data1 = new FormData ();
		data1.width=700;
		
	
		user_label.setLayoutData(data1);
		
		
		c=new Composite(getShell(), SWT.NONE);
		FormData data3 = new FormData ();
		data3.width=900;
		data3.height=40;
		data3.top=new FormAttachment (user_label, 45);
		//data3.right = new FormAttachment (user_label, 10);
		c.setLayoutData(data3);
		c.setLayout(new GridLayout(1, false));
		
		//status song label
		
		status_song_label=new Label(c, SWT.BORDER);
		status_song_label.setAlignment(SWT.LEFT);
		
		status_song_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		status_song_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		status_song_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		t11 = new Thread(new getStatusSong());
		pool.add(t11);
		t11.start();
		

		//composite
		
		c1=new Composite(getShell(), SWT.NONE);
		FormData data4 = new FormData ();
		data4.width=300;
		data4.height=300;
		data4.top=new FormAttachment (user_label, 145);
		data4.right = new FormAttachment (user_label, 400);
		c1.setLayoutData(data4);
		c1.setLayout(new GridLayout(2, false));
		
		
		
		

		//song list label
		
		song_list_label=new Label(c1, SWT.NONE);
		song_list_label.setAlignment(SWT.CENTER);
		song_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		song_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		song_list_label.setText("Songs list");
		song_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
	
		
		/*******/
	 class getSongList implements Runnable {

			@Override
			public void run() {
				
			//	final ArrayList<Pair<String,String>> f= engine.getSongList(username); //1.6.14
				final Pair<Integer, java.util.List<Pair<String, String>>> f;
				if(theMusicalNetwork.qaqa){
					f=  theMusicalNetwork.nadav.getSongList(friend_user_name);
				}
				else{
					f=  engine.getSongList(friend_user_name);
				}
				//final ArrayList<Pair<String,String>> f= (ArrayList<Pair<String, String>>) engine.getSongList();
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(checkConnection(getShell(), f.getLeft())){
							for(Pair<String,String> s:f.getRight()){
								//friendList.add (s);
								TableItem item = new TableItem (songList_t, SWT.NONE);
								item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
															
								item.setText(0, s.getLeft());
								item.setText(1, s.getRight());
							}
							
							
							//System.out.println(pool.size());
							
							pool.remove(t12);
							if(pool.isEmpty()){
								
								closeWaiting();
								showScreen();
							}
						}
						
						
						else{ //want to cont..
							//back to main screen
							closeWaiting();
							disposeScreen();
							if(theMusicalNetwork.qaqa){
								mainScreen main=new mainScreen(getDisplay(), getShell(), engine, theMusicalNetwork.nadav.getUsername());
								main.createScreen();
							}
							else{
								mainScreen main=new mainScreen(getDisplay(), getShell(), engine, engine.getUsername());
								main.createScreen();	
							}
						}
					
				
					}
				});
				
			}
		}
		
		/*******/

		
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
			
			t12 = new Thread(new getSongList());
			pool.add(t12);
			t12.start();
			
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
					//final boolean b=theMusicalNetwork.nadav.addSong("qaqa", song_chosen.getLeft(), song_chosen.getRight());
					//final boolean b=engine.addSong(song_chosen.getLeft(), song_chosen.getRight());
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							//status_song=status;
							if(checkConnection(getShell(), b.getLeft())){
								if(!b.getRight()){
									closeWaiting();
									showScreen();
									errorPop("Error", "Failed to add song.");	
									pool.remove(t14);
								}
								else{
									
									PopUpinfo(getShell(),"Added Song", "The song was added to your song list successfully!");
									pool.remove(t14);
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
		friend_list_label.setText("Friends list");
		friend_list_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		
		
		class getFriendList implements Runnable {

		@Override
		public void run() {
			final Pair <Integer,ArrayList<String>> f;
			if(theMusicalNetwork.qaqa){
				 f=theMusicalNetwork.nadav.getFriendList(friend_user_name);
			}
			else{
				f=engine.getFriendList(friend_user_name);
			}
			
			//final ArrayList<String> f=engine.getFriendList();
					
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					if(checkConnection(getShell(), f.getLeft())){
						for(String s:f.getRight()){
							friendList.add (s);
						}
						
						pool.remove(t13);
						
						System.out.println(pool.size());
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}
					}
					else{ //no connection and want to continue
						pool.remove(t13);
						if(theMusicalNetwork.qaqa){
							mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,"a");
							mainScreen.createScreen();
						}
						else{
						mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,engine.getUsername());
						mainScreen.createScreen();
						}
					}
					
				}
			});
			
		}
	}
	
	/*******/

	
		
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
		
		t13 = new Thread(new getFriendList());
		pool.add(t13);
		
		t13.start();
		
		
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
			//final boolean add=theMusicalNetwork.nadav.addFriend(friend_name_to_show);
			//final boolean add=engine.addFriend(friend_name_to_show);
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
							if(pool.isEmpty()){
								closeWaiting();
								showScreen();
								PopUpinfo(getShell(),"Add Friend", "The friend was added to your friend list successfully!!");
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
			data13.top = new FormAttachment (user_label, 525);
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
		
		
		

		


		
	
		openWaiting();
		this.getShell().layout();
		
	}

	@Override
	protected void disposeScreen() {
		// TODO Auto-generated method stub
		this.add_new_friend.dispose();
		this.add_new_song.dispose();
		this.friend_list_label.dispose();
		this.friendList.dispose();
		this.song_list_label.dispose();
		this.songList_t.dispose();
		this.status_song_label.dispose();
		this.user_label.dispose();
		this.back_button.dispose();
		this.c.dispose();
		this.c1.dispose();
		this.c2.dispose();
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		this.add_new_friend.setVisible(false);
		this.add_new_song.setVisible(false);
		this.friend_list_label.setVisible(false);
		this.friendList.setVisible(false);
		this.song_list_label.setVisible(false);
		this.songList_t.setVisible(false);
		this.status_song_label.setVisible(false);
		this.user_label.setVisible(false);
		this.back_button.setVisible(false);
		this.c.setVisible(false);
		this.c1.setVisible(false);
		this.c2.setVisible(false);
		this.getShell().layout();
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		this.add_new_friend.setVisible(true);
		this.add_new_song.setVisible(true);
		this.friend_list_label.setVisible(true);
		this.friendList.setVisible(true);
		this.song_list_label.setVisible(true);
		this.songList_t.setVisible(true);
		this.status_song_label.setVisible(true);
		this.user_label.setVisible(true);
		this.back_button.setVisible(true);
		this.c.setVisible(true);
		this.c1.setVisible(true);
		this.c2.setVisible(true);
		this.getShell().layout();
	}

}
