package ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
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

	private Button homeBtn = null;
	private Table songList = null;
	private Table userList = null;
	private Label title = null;
	private Button addSongBtn = null;
	private Button addFriendBtn = null;

	private List<String> recommendedFriends = null;
	private List<Pair<String, String>> recommendedSongs = null;
	private String loggedInUserName;
	private Pair<String,String> chosenSong;
	private String chosenFriend;
	
	private Thread thread1;
	private Thread addFriendThread;

	public RecomendationsScreen(Display display, Shell shell,
			ApplicationInterface engine) {
		super(display, shell, engine);
		System.out.println("RecomendationsScreen-constructor()");
	}

	@Override
	public void createScreen() {

		System.out.println("RecomendationsScreen-createScreen()");

		// Get Logged In Username
		loggedInUserName = engine.getUsername();

		// Initialize UI
		initializeControls();

		engine.InitalizeRecommender();
		
		// Get Recommendations From RecommenderEngine
		runRecommendations();
	}

	private void runRecommendations() {

		// Get Friends Recommendations
		try {
			recommendedFriends = RecommenderEngineAdapter.getInstance().recommendFriends(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedFriends = new ArrayList<>();
		}

		// Update UI
		for (String recommendedUsername : recommendedFriends) {
			TableItem item = new TableItem(userList, SWT.NONE);
			item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			item.setText(recommendedUsername);
		}

		// Get Songs Recommendations
		try {
			recommendedSongs = RecommenderEngineAdapter.getInstance().recommendSongs(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedSongs = new ArrayList<>();
		}

		// Update UI
		for (Pair<String, String> song : recommendedSongs) {
			TableItem item = new TableItem(songList, SWT.NONE);
			item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			item.setText(0, song.getLeft());
			item.setText(1, song.getRight());
		}
		
		
	}

	private void initializeControls() {

		initTitle();

		initHomeBtn();
		
		initSongList();
		
		initAddSong();
		 
		initUserList();
		
		initAddFriend();
		
		// Refresh Layout
		this.getShell().layout();
	}

	private void initUserList() {

		// User List
		userList = new Table(getShell(), SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		userList.setLinesVisible(true);
		userList.setHeaderVisible(true);

		TableColumn userColumn = new TableColumn(userList, SWT.NONE);
		userColumn.setText("Username");
		userColumn.setWidth(270);

		FormData userListFormData = new FormData();
		userListFormData.width = 250;
		userListFormData.height = 200;
		userListFormData.right = new FormAttachment(76, 0);
		userListFormData.bottom = new FormAttachment(60, 0);
		userList.setLayoutData(userListFormData);
		userList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		userList.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				chosenFriend = item.getText(0);
			}
		});
		
		
	}

	private void initAddSong() {
				
		/*******/
		class addSong implements Runnable {

			@Override
			public void run() {
				final Pair<Integer,Boolean> b;
				if(theMusicalNetwork.qaqa){
					b=theMusicalNetwork.nadav.addSong(chosenSong.getRight(),chosenSong.getLeft());
				}
				else{//true code
					b=engine.addSong(chosenSong.getRight(),chosenSong.getLeft());
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
								pool.remove(thread1);
							}
							else{
								
								pool.remove(thread1);
								if(pool.isEmpty()){
									closeWaiting();
									showScreen();
									PopUpinfo(getShell(),"added song", "qaqa-succes!!!");
								}
							}
						}
						
						else{ //problem with connection want to cont..
							
						}
				
					
					}
				});
				
			}
		}
		
		
		
		// Add Song
		addSongBtn=new Button(getShell(),SWT.NONE);
		addSongBtn.setText("Add Song");
		FormData addSongFormData = new FormData ();
		addSongFormData.width=100;
		addSongFormData.height=26;
		addSongFormData.right = new FormAttachment (45, 0);
		addSongFormData.bottom = new FormAttachment (30, 0);
		addSongBtn.setLayoutData(addSongFormData);
				
		addSongBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				
				if(chosenSong==null){
					errorPop("Error", "Please choose a song first.");
				}
				else{
					thread1 = new Thread(new addSong());
					pool.add(thread1);
					thread1.start();
				}
			}
		});
	}

	private void initSongList() {
		// Song List
		songList = new Table(getShell(), SWT.SINGLE | SWT.BORDER
				| SWT.FULL_SELECTION);
		songList.setLinesVisible(true);
		songList.setHeaderVisible(true);

		TableColumn artistColumn = new TableColumn(songList, SWT.NONE);
		artistColumn.setText("Artist");
		artistColumn.setWidth(140);

		TableColumn songNameColumn = new TableColumn(songList, SWT.NONE);
		songNameColumn.setText("Song name");
		songNameColumn.setWidth(140);

		FormData songListFormData = new FormData();
		songListFormData.width = 250;
		songListFormData.height = 200;
		songListFormData.right = new FormAttachment(35, 0);
		songListFormData.bottom = new FormAttachment(60, 0);
		songList.setLayoutData(songListFormData);
		songList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		songList.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				System.out.println(item.getText(0));
				System.out.println(item.getText(1));
			}
		});
		
		songList.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				//System.out.println(event.data);//qaqa
				int [] selection = songList.getSelectionIndices ();
				
				for (int i=0; i<selection.length; i++) {
					System.out.println(songList.getItem(selection[i])); //qaqa
						chosenSong = new Pair<String, String>(songList.getItem(selection[i]).getText(0)
								, songList.getItem(selection[i]).getText(1));
					}
				//openWaiting();
				
				
			}
		});
	}

	private void initHomeBtn() {
		// Home Button
		homeBtn = new Button(getShell(), SWT.NONE);
		homeBtn.setText("Home");
		FormData homeBtnFormData = new FormData();
		homeBtnFormData.width = 110;
		homeBtnFormData.height = 30;
		homeBtnFormData.right = new FormAttachment(20, 0);
		homeBtnFormData.bottom = new FormAttachment(100, 0);
		homeBtn.setLayoutData(homeBtnFormData);

		homeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeScreen();
				mainScreen mainScreen = new mainScreen(getDisplay(),
						getShell(), engine, engine.getUsername());
				mainScreen.createScreen();
			}
		});
	}

	private void initTitle() {
		// Title
		title = new Label(getShell(), SWT.NONE);
		title.setAlignment(SWT.LEFT);
		title.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		title.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		title.setText("Recommendations:");
		FormData titleFormData = new FormData();
		titleFormData.width = 700;
		title.setLayoutData(titleFormData);
	}

	private void initAddFriend(){
		

		class addFriend implements Runnable {

						
			@Override
			public void run() {
				
				final Pair<Integer,Boolean> addResult = engine.addFriend(chosenFriend);
		
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
						if(checkConnection(getShell(), addResult.getLeft()))
						{
							if(!addResult.getRight())
							{
								closeWaiting();
								showScreen();
								errorPop("Error", "Failed to add friend.");	
								pool.remove(addFriendThread);
							}
							else{
								
								pool.remove(addFriendThread);
								if(pool.isEmpty()){
									closeWaiting();
									showScreen();
									PopUpinfo(getShell(),"added Friend", "qaqa-succes!!!");
								}
								
							}
						}
						else
						{
							pool.remove(addFriendThread);
							if(pool.isEmpty())
							{
								closeWaiting();
								showScreen();
								//PopUpinfo(getShell(),"added Friend", "qaqa-succes!!!");
							}
						}
				
						
					}
				});
				
			}
		}
		

		
		//add_friend_button
		addFriendBtn=new Button(getShell(),SWT.NONE);
		addFriendBtn.setText("Add Friend");
		FormData addFriendFormData = new FormData ();
		addFriendFormData.width=100;
		addFriendFormData.height=26;
		addFriendFormData.right = new FormAttachment (85, 0);
		addFriendFormData.bottom = new FormAttachment (30, 0);
		addFriendBtn.setLayoutData(addFriendFormData); 
		addFriendBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				
				if(chosenFriend==null || chosenFriend.equals(""))
				{
					errorPop("Error", "Please selet a friend first.");
				}
				else
				{
					openWaiting();		
					addFriendThread = new Thread(new addFriend());
					pool.add(addFriendThread);
					addFriendThread.start();
				}
			}
		});
		
				
	}
	
	@Override
	protected void disposeScreen() {
		System.out.println("RecomendationsScreen-disposeScreen()");
		homeBtn.dispose();
		songList.dispose();
		userList.dispose();
		title.dispose();
		addSongBtn.dispose();
		addFriendBtn.dispose();
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
