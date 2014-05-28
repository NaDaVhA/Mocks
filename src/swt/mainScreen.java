package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class mainScreen extends Screen{
	//members
	private String username;
	private String status_song;
	
	private Label user_label=null;
	private Label status_song_label=null;
	private Label song_list_label=null;
	private Label friend_list_label=null;
	private Button sign_out=null;
	private Button add_new_song=null;
	private Button add_new_friend=null;
	private Button change_status_song=null;
	private List songList=null;
	private List friendList=null;
	
	private Button entertain_me=null;
	private Button view_friend=null;
	
	
	//QAQA add here the rest
	
	
	public mainScreen(Display display,Shell shell,String username) {
		// TODO Auto-generated constructor stub
		super(display, shell);
		this.username=username;
	}
	
	

	@Override
	public void createScreen() {
		
		this.status_song="qaqa -  status song";//qaqa add call to getStatusSong in interface
	
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
				disposeScreen();
				logInScreen logIn=new logInScreen(getDisplay(),getShell());
				logIn.createScreen();
			}
		});
		
		//status song label
		
		status_song_label=new Label(getShell(), SWT.BORDER);
		status_song_label.setAlignment(SWT.LEFT);
		
		status_song_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		status_song_label.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		status_song_label.setText("Status song:  "+this.status_song);
		FormData data3 = new FormData ();
		data3.width=700;
		
		//data2.right = new FormAttachment (10, 0);
		data3.bottom = new FormAttachment (20, 0);
		status_song_label.setLayoutData(data3);
		
		//change status song button
		
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
				addNewSongScreen newSong=new addNewSongScreen(getDisplay(), getShell());
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
				addNewFriendScreen newFriend=new addNewFriendScreen(getDisplay(), getShell());
				newFriend.createScreen();
			}
		});
		
		//song list
		songList=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		for (int i=0; i<128; i++) songList.add ("Song " + i); //QAQA CHANGE WITH REAL SONGS
		
		songList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data9 = new FormData ();
		data9.width=250;
		data9.height=200;
		data9.right = new FormAttachment (40, 0);
		data9.bottom = new FormAttachment (80, 0);
		songList.setLayoutData(data9); 
		
		//friend list
		friendList=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		for (int i=0; i<128; i++) friendList.add ("Friend " + i); //QAQA CHANGE WITH REAL SONGS
		
		friendList.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data10 = new FormData ();
		data10.width=270;
		data10.height=200;
		data10.right = new FormAttachment (90, 0);
		data10.bottom = new FormAttachment (80, 0);
		friendList.setLayoutData(data10);
		
		//entertain me button
		entertain_me=new Button(getShell(),SWT.NONE);
		entertain_me.setText("Entertain Me!");
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
			}
		});
			
		
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
		this.songList.dispose();
		this.status_song_label.dispose();
		this.user_label.dispose();
		this.view_friend.dispose();
		this.entertain_me.dispose();
	}

}
