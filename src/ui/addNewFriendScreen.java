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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import utilities.Pair;

public class addNewFriendScreen extends Screen {
	
	//add members here

	private Label head=null;
	private Label search_label=null;
	private Text search_text=null;
	private Label friend_result_label=null;
	private List friend_result_list=null;
	private Button add_friend_button=null;
	private Label friend_status_label=null;
	private Label friend_status_song=null;
	private Label songs_list_label=null;
	private Table friend_songs=null;
	private Button back_button=null;
	private Button search_button=null;
	private String name_to_search;
	
	//private String username;
	private String friend_name_to_show;
	
	private Thread t5,t6,t7;
	
	private Composite c=null;
	private Composite c1=null;
	private Composite c2=null;
	private Composite c3=null;
	
	public addNewFriendScreen(Display display, Shell shell,ApplicationInterface engine){
		super(display, shell,engine);	
		//username=user;
	}

	@Override
	public void createScreen(){
		//headline
		head=new Label(getShell(), SWT.NONE);
		head.setAlignment(SWT.CENTER);
		
		head.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		head.setFont(SWTResourceManager.getFont("MV Boli", 16, SWT.BOLD));
		head.setText("Add a Friend");
		FormData data1 = new FormData ();
		data1.width=300;
		
		data1.right = new FormAttachment (65, 0);
		data1.bottom = new FormAttachment (5, 0);
		head.setLayoutData(data1);
		
		
		c=new Composite(getShell(), SWT.NONE);
		FormData data2 = new FormData ();
		data2.width=500;
		data2.height=40;
		data2.top=new FormAttachment (head, 5,SWT.BOTTOM);
		data2.right = new FormAttachment (head, 400);
		c.setLayoutData(data2);
		c.setLayout(new GridLayout(3, false));
		
		//search label
		search_label=new Label(c, SWT.NONE);
		search_label.setAlignment(SWT.CENTER);
		
		search_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		search_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		search_label.setText("Search a Friend:");
		search_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		
		//search text
		search_text=new Text(c, SWT.BORDER);
		search_text.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));;
		search_text.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//search_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		GridData g=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		g.widthHint=150;
		search_text.setLayoutData(g);
		
		/*******/
			class searchFriends implements Runnable {

				@Override
				public void run() {
					final  Pair<Integer, ArrayList<String>> list_p;
					if(theMusicalNetwork.qaqa){
						 list_p=theMusicalNetwork.nadav.getSearchResultsFriends(name_to_search);
					}
					else{//true code
						 list_p=engine.getSearchResultsFriends(name_to_search);
					}
					
					//final  ArrayList<String> list=engine.getSearchResultsFriends(name_to_search);
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							//status_song=status;
							if(checkConnection(getShell(), list_p.getLeft())){
								friend_result_list.removeAll();
								if(list_p.getRight().isEmpty()){
									PopUpinfo(getShell(), "Search results info", "The search results are empty!");
								}
								else{
									for(String s:list_p.getRight()){
										friend_result_list.add(s);
									}
								}
							}
							else{
								
								
							}
							
							
							pool.remove(t5);
							if(pool.isEmpty()){
								closeWaiting();
								showScreen();
							}
						}
					});
					
				}
			}
			
			/*******/
		
		//search button
			search_button=new Button(c,SWT.NONE);
			search_button.setText("Search Friend");
			search_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			
			search_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					System.out.println("qaqa - pressed search friend");
					friend_name_to_show="";
					name_to_search=null; //qaqa ?? check search function
					name_to_search=search_text.getText();
					
					System.out.println("qaqa - name to search: "+name_to_search);
					
					if(name_to_search.compareTo("")==0){
						errorPop("Error", "you didnt enter a name to search.\nPlease enter the name.");
					}
					else{
						//friend_result_list.removeAll();
						friend_status_song.setText("");
						friend_songs.removeAll();
						openWaiting();
						
						 t5 = new Thread(new searchFriends());
						 pool.add(t5);
						 t5.start();
					}
				}
			});
			
			
		c1=new Composite(getShell(), SWT.NONE);
		FormData data3 = new FormData ();
		data3.width=500;
		data3.height=175;
		data3.top=new FormAttachment (c, 60,SWT.TOP);
		data3.right = new FormAttachment (c, 0,SWT.RIGHT);
		c1.setLayoutData(data3);
		c1.setLayout(new GridLayout(2, false));
		
		
		
		
		//friend result label
		friend_result_label=new Label(c1, SWT.NONE);
		friend_result_label.setAlignment(SWT.CENTER);
		
		friend_result_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_result_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_result_label.setText("Search Results:");
		friend_result_label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
	
		/*******/
			class updateFriendShow implements Runnable {

					@Override
					public void run() {
						final Pair<Integer, Pair<String, String>> friend_song;
						final  Pair<Integer, java.util.List<Pair<String, String>>> list_p;
						
						if(theMusicalNetwork.qaqa){
							 friend_song=theMusicalNetwork.nadav.getStatusSong(friend_name_to_show);
							 list_p= theMusicalNetwork.nadav.getSongList(friend_name_to_show);
						}
						else{ //true code
						 friend_song=engine.getStatusSong(friend_name_to_show); //1.6.14	
						 list_p= engine.getSongList(friend_name_to_show); //1.6.14
							
						}
						
						
						getDisplay().asyncExec(new Runnable() {
							public void run() {
								//status_song=status;
								if(checkConnection(getShell(),friend_song.getLeft())&&checkConnection(getShell(),list_p.getLeft())){
									friend_status_song.setText(friend_song.getRight().getLeft()+" "+friend_song.getRight().getRight());
									
									for(Pair<String, String> s:list_p.getRight()){
										TableItem item = new TableItem (friend_songs, SWT.NONE);
										item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
										//item.setText(0, "artist "+s.getLeft());
										item.setText(0, s.getLeft());
										//item.setText(1, "songggggggggggggggggggggggggggggggggggg "+s.getRight());
										item.setText(1, s.getRight());
									}	
								}
								else{
									
								}
								
								//
								pool.remove(t6);
								if(pool.isEmpty()){
									closeWaiting();
									showScreen();
								}
							}
						});
						
					}
				}
				
				/*******/
		
		
		//search list
		
		friend_result_list=new List(c1, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		
		friend_result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//friend_result_list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridData g1=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		g1.widthHint=250;
		
		friend_result_list.setLayoutData(g1);
		
		
		friend_result_list.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
				//System.out.println(event.data);//qaqa
				int[] selection = friend_result_list.getSelectionIndices();
				
				for (int i=0; i<selection.length; i++) {
					System.out.println(friend_result_list.getItem(selection[i])); //qaqa
						friend_name_to_show=friend_result_list.getItem(selection[i]);
					}
				//openWaiting();
				
				 t6 = new Thread(new updateFriendShow());
				 pool.add(t6);
				 t6.start();
			}
		});		
		
		
		c2=new Composite(getShell(), SWT.NONE);
		FormData data4 = new FormData ();
		data4.width=800;
		data4.height=40;
		data4.top=new FormAttachment (c1, 5,SWT.BOTTOM);
		data4.right = new FormAttachment (c1, 125,SWT.RIGHT);
		c2.setLayoutData(data4);
		c2.setLayout(new GridLayout(2, false));
		
		
		//friend status song label
		friend_status_label=new Label(c2, SWT.NONE);
		friend_status_label.setAlignment(SWT.CENTER);
		
		friend_status_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_status_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_status_label.setText("Friend status song:");
		friend_status_label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		
		
		
		//friend_status_song
		friend_status_song=new Label(c2, SWT.BORDER);
		friend_status_song.setAlignment(SWT.LEFT);
		
		friend_status_song.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_status_song.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_status_song.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		GridData g2=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		g2.widthHint=600;
		
		friend_status_song.setLayoutData(g2);
		//friend_status_song.setText("qaqa");
		
		c3=new Composite(getShell(), SWT.NONE);
		FormData data5 = new FormData ();
		data5.width=650;
		data5.height=200;
		data5.top=new FormAttachment (c2, 5,SWT.BOTTOM);
		data5.right = new FormAttachment (c2, 0,SWT.RIGHT);
		c3.setLayoutData(data5);
		c3.setLayout(new GridLayout(3, false));
		
		//songs_list_label
		
		songs_list_label=new Label(c3, SWT.NONE);
		songs_list_label.setAlignment(SWT.CENTER);
		
		songs_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		songs_list_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		songs_list_label.setText("Friend Song List:");
		songs_list_label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
		
		//friend song list 
		friend_songs=new Table (c3, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		friend_songs.setLinesVisible (true);
		friend_songs.setHeaderVisible (true);
		TableColumn column = new TableColumn (friend_songs, SWT.NONE);
		column.setText ("Artist");
		TableColumn column1 = new TableColumn (friend_songs, SWT.NONE);
		column1.setText ("Song name");
		column.setWidth(180);
		column1.setWidth(180);
	
		
		
		friend_songs.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g3=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		//g3.heightHint=200;
		g3.widthHint=300;
		friend_songs.setLayoutData(g3);
		
		/*******/
		class addFriend implements Runnable {

			@Override
			public void run() {
				final Pair<Integer,Boolean> add;
				if(theMusicalNetwork.qaqa){
					add=theMusicalNetwork.nadav.addFriend(friend_name_to_show);
				}
				else{
					add=engine.addFriend(friend_name_to_show);
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
								pool.remove(t7);
							}
							else{
								
								pool.remove(t7);
								if(pool.isEmpty()){
									closeWaiting();
									showScreen();
									PopUpinfo(getShell(),"added Friend", "qaqa-succes!!!");
								}
								
							}
						}
						else{
							pool.remove(t7);
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
		
		//add_friend_button
			add_friend_button=new Button(c3,SWT.NONE);
			add_friend_button.setText("Add Friend");
			//add_friend_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			GridData g4=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			g4.widthHint=50;
			add_friend_button.setLayoutData(g4);
			add_friend_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					//System.out.println("qaqa - pressed add friend");
					System.out.println("qaqa - pressed add friend - "+friend_name_to_show);

					if(friend_name_to_show.compareTo("")==0){
						errorPop("Error", "Please selet a friend first.");
					}
					else{
						openWaiting();
						
						 t7 = new Thread(new addFriend());
						 pool.add(t7);
						 t7.start();
					}
				}
			});
			
	
		
		//back button
		back_button=new Button(getShell(),SWT.NONE);
		back_button.setText("Go back to home screen");
		FormData data12 = new FormData ();
		data12.width=180;
		data12.height=30;
		//data12.right = new FormAttachment (87, 0);
		data12.bottom = new FormAttachment (100, 0);
		back_button.setLayoutData(data12); 
		back_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed back home");
				disposeScreen();
				mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,engine.getUsername()); //QAQA ADD USERNAME
				mainScreen.createScreen();
			}
		});
		
		
		this.getShell().layout();
	}
	
	@Override
	protected void disposeScreen(){
		this.add_friend_button.dispose();
		this.back_button.dispose();
		this.friend_result_label.dispose();
		this.friend_result_list.dispose();
		this.friend_songs.dispose();
		this.friend_status_label.dispose();
		this.friend_status_song.dispose();
		this.head.dispose();
		this.search_button.dispose();
		this.search_label.dispose();
		this.search_text.dispose();
		this.songs_list_label.dispose();
		this.c.dispose();
		this.c1.dispose();
		this.c2.dispose();
		this.c3.dispose();
		
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		this.add_friend_button.setVisible(false);
		this.back_button.setVisible(false);
		this.friend_result_label.setVisible(false);
		this.friend_result_list.setVisible(false);
		this.friend_songs.setVisible(false);
		this.friend_status_label.setVisible(false);
		this.friend_status_song.setVisible(false);
		this.head.setVisible(false);
		this.search_button.setVisible(false);
		this.search_label.setVisible(false);
		this.search_text.setVisible(false);
		this.songs_list_label.setVisible(false);
		this.c.setVisible(false);
		this.c1.setVisible(false);
		this.c2.setVisible(false);
		this.c3.setVisible(false);
		this.getShell().layout();
		
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		this.add_friend_button.setVisible(true);
		this.back_button.setVisible(true);
		this.friend_result_label.setVisible(true);
		this.friend_result_list.setVisible(true);
		this.friend_songs.setVisible(true);
		this.friend_status_label.setVisible(true);
		this.friend_status_song.setVisible(true);
		this.head.setVisible(true);
		this.search_button.setVisible(true);
		this.search_label.setVisible(true);
		this.search_text.setVisible(true);
		this.songs_list_label.setVisible(true);
		this.c.setVisible(true);
		this.c1.setVisible(true);
		this.c2.setVisible(true);
		this.c3.setVisible(true);
		this.getShell().layout();
		
	}
}
