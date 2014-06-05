package ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import utilities.Pair;
import core.ApplicationInterface;

public class viewFriendScreen extends Screen {
	
	private String username;
	private Pair<String,String> status_song;
	
	private Label user_label=null;
	private Label status_song_label=null;
	private Label song_list_label=null;
	private Label friend_list_label=null;
	//private Button sign_out=null;
	private Button add_new_song=null;
	private Button add_new_friend=null;
	//private Button change_status_song=null;
	private Table  songList_t=null;
	private List friendList=null;
	
	
	private Button view_friend=null;
	
	private Button back_button=null;
	
private Thread t11,t12,t13,t14;
	
	//QAQA add here the rest
	private Pair<String, String> song_selcted_table=null;
	
	private String user_name;
	
	
	public viewFriendScreen(Display display,Shell shell , ApplicationInterface engine,String username){
		super(display, shell, engine);
		this.user_name=username;
	}

	@Override
	public void createScreen() {
		// TODO Auto-generated method stub

		/*******/
		class getStatusSong implements Runnable {

			@Override
			public void run() {
				//final Pair<String,String> status=theMusicalNetwork.nadav.getStatusSong(username);
				//final Pair<String,String> status=engine.getStatusSong(username); //1.6.14
				final Pair<String,String> status;
				if(theMusicalNetwork.qaqa){
					status=theMusicalNetwork.nadav.getStatusSong(user_name);
				}
				else{ //real code
					 status=engine.getStatusSong(user_name);
				}
				//final Pair<String,String> status=engine.getStatusSong();
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;
						status_song_label.setText("Status song:  "+status.getLeft()+" "+status.getRight());
						//updateStatusSong(status);
						//Thread.currentThread();
						pool.remove(t11);
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}
					}
				});
				
			}
		}
		
		/*******/
		 t11 = new Thread(new getStatusSong());
		pool.add(t11);
		t11.start();
		
		//this.status_song="qaqa -  status song";//qaqa add call to getStatusSong in interface
	
		//username label
		user_label=new Label(getShell(), SWT.NONE);
		user_label.setAlignment(SWT.LEFT);
		
		user_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		user_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		user_label.setText("Username:  "+this.user_name);
		FormData data1 = new FormData ();
		data1.width=700;
		
		//data1.right = new FormAttachment (35, 0);
		//data1.bottom = new FormAttachment (5, 0);
		user_label.setLayoutData(data1);
		
		
		
		
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
		
		
		
		
		

		//song list label
		
		song_list_label=new Label(getShell(), SWT.NONE);
		song_list_label.setAlignment(SWT.CENTER);
		song_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		song_list_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		song_list_label.setText(this.user_name+" Songs");
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
		friend_list_label.setText(this.user_name+" Friends");
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
				addNewFriendScreen newFriend=new addNewFriendScreen(getDisplay(), getShell(),engine,username);
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
		songList_t.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data9 = new FormData ();
		data9.width=250;
		data9.height=200;
		data9.right = new FormAttachment (40, 0);
		data9.bottom = new FormAttachment (83, 0);
		songList_t.setLayoutData(data9);
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
				final ArrayList<Pair<String,String>> f;
				if(theMusicalNetwork.qaqa){
					f= (ArrayList<Pair<String, String>>) theMusicalNetwork.nadav.getSongList();
				}
				else{
					f= (ArrayList<Pair<String, String>>) engine.getSongList();
				}
				//final ArrayList<Pair<String,String>> f= (ArrayList<Pair<String, String>>) engine.getSongList();
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
					
						for(Pair<String,String> s:f){
							//friendList.add (s);
							TableItem item = new TableItem (songList_t, SWT.NONE);
							item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
							//item.setText(0, "artist "+s.getLeft());
							//item.setText(1, "songggggggggggggggggggggggggggggggggggg "+s.getRight());
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
				});
				
			}
		}
		
		/*******/
		t12 = new Thread(new getSongList());
		pool.add(t12);
		t12.start();
		
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
				final ArrayList<String> f;
				if(theMusicalNetwork.qaqa){
					 f=theMusicalNetwork.nadav.getFriendList();
				}
				else{
					f=engine.getFriendList();
				}
				
				//final ArrayList<String> f=engine.getFriendList();
						
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						for(String s:f){
							friendList.add (s);
						}
						
						pool.remove(t13);
						
						System.out.println(pool.size());
						if(pool.isEmpty()){
							
							closeWaiting();
							showScreen();
						}
					}
				});
				
			}
		}
		
		/*******/
		 t13 = new Thread(new getFriendList());
		pool.add(t13);
		
		t13.start();
		
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
			}
		});
		
		//back button
		back_button=new Button(getShell(),SWT.NONE);
		back_button.setText("Go back to home screen");
		FormData data13 = new FormData ();
		data13.width=150;
		data13.height=30;
		//data12.right = new FormAttachment (87, 0);
		data13.bottom = new FormAttachment (100, 0);
		back_button.setLayoutData(data13); 
		back_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed back home");
				disposeScreen();
				mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,"QAQA - USERNAME"); //QAQA ADD USERNAME
				mainScreen.createScreen();
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
		this.view_friend.dispose();
		this.back_button.dispose();
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
		this.view_friend.setVisible(false);
		this.back_button.setVisible(false);
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
		this.view_friend.setVisible(true);
		this.back_button.setVisible(true);
		this.getShell().layout();
	}

}
