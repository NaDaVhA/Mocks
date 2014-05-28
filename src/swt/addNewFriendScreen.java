package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

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
	private List friend_songs=null;
	private Button back_button=null;
	private Button search_button=null;
	
	private String name_to_search;
	
	public addNewFriendScreen(Display display, Shell shell) {
		super(display, shell);
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
		
		//search label
		search_label=new Label(getShell(), SWT.BORDER);
		search_label.setAlignment(SWT.CENTER);
		
		search_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		search_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		search_label.setText("Search a Friend:");
		FormData data2 = new FormData ();
		data2.width=200;
		
		data2.right = new FormAttachment (50, 0);
		data2.bottom = new FormAttachment (15, 0);
		search_label.setLayoutData(data2);
		
		//search text
		search_text=new Text(getShell(), SWT.BORDER);
		search_text.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));;
		search_text.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		FormData data3 = new FormData ();
		data3.width=200;
		data3.height=18;
		data3.right = new FormAttachment (73, 0);
		data3.bottom = new FormAttachment (15, 0);
		search_text.setLayoutData(data3);
		
		
		//friend result label
		friend_result_label=new Label(getShell(), SWT.BORDER);
		friend_result_label.setAlignment(SWT.CENTER);
		
		friend_result_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_result_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_result_label.setText("Search Results:");
		FormData data4 = new FormData ();
		data4.width=200;
		
		data4.right = new FormAttachment (30, 0);
		data4.bottom = new FormAttachment (25, 0);
		friend_result_label.setLayoutData(data4);
		
		//search list
		
		friend_result_list=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		//for (int i=0; i<12; i++) friend_result_list.add ("Friend resault " + i); //QAQA CHANGE WITH REAL SONGS
		
		friend_result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data5 = new FormData ();
		data5.width=300;
		data5.height=100;
		data5.right = new FormAttachment (65);
		data5.bottom = new FormAttachment (42, 0);
		friend_result_list.setLayoutData(data5);
		
		//search button
		search_button=new Button(getShell(),SWT.NONE);
		search_button.setText("Search Friend");
		FormData data6 = new FormData ();
		data6.width=115;
		data6.height=26;
		data6.right = new FormAttachment (87, 0);
		data6.bottom = new FormAttachment (15, 0);
		search_button.setLayoutData(data6); 
		search_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed search friend");
				name_to_search=search_text.getText();
				System.out.println("qaqa - name to search: "+name_to_search);
			}
		});
		
		//friend status song label
		friend_status_label=new Label(getShell(), SWT.BORDER);
		friend_status_label.setAlignment(SWT.CENTER);
		
		friend_status_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_status_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_status_label.setText("Friend status song:");
		FormData data7 = new FormData ();
		data7.width=200;
		
		data7.right = new FormAttachment (30, 0);
		data7.bottom = new FormAttachment (50, 0);
		friend_status_label.setLayoutData(data7);
		
		//friend_status_song
		friend_status_song=new Label(getShell(), SWT.BORDER);
		friend_status_song.setAlignment(SWT.LEFT);
		
		friend_status_song.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		friend_status_song.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		friend_status_song.setText("qaqa");
		FormData data8 = new FormData ();
		data8.width=600;
		
		data8.right = new FormAttachment (95, 0);
		data8.bottom = new FormAttachment (50, 0);
		friend_status_song.setLayoutData(data8);
		
		//songs_list_label
		
		songs_list_label=new Label(getShell(), SWT.BORDER);
		songs_list_label.setAlignment(SWT.CENTER);
		
		songs_list_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		songs_list_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		songs_list_label.setText("Friend Song List:");
		FormData data9 = new FormData ();
		data9.width=200;
		
		data9.right = new FormAttachment (30, 0);
		data9.bottom = new FormAttachment (63, 0);
		songs_list_label.setLayoutData(data9);
		
		//friend song list 
		friend_songs=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		//for (int i=0; i<12; i++) friend_result_list.add ("Friend resault " + i); //QAQA CHANGE WITH REAL SONGS
		
		friend_songs.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data10 = new FormData ();
		data10.width=300;
		data10.height=100;
		data10.right = new FormAttachment (65);
		data10.bottom = new FormAttachment (80, 0);
		friend_songs.setLayoutData(data10);
		
		//add_friend_button
		add_friend_button=new Button(getShell(),SWT.NONE);
		add_friend_button.setText("Add Friend");
		FormData data11 = new FormData ();
		data11.width=115;
		data11.height=106;
		data11.right = new FormAttachment (85, 0);
		data11.bottom = new FormAttachment (80, 0);
		add_friend_button.setLayoutData(data11); 
		add_friend_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed add friend");
				
			}
		});
		
		//back button
		back_button=new Button(getShell(),SWT.NONE);
		back_button.setText("Go back to home screen");
		FormData data12 = new FormData ();
		data12.width=150;
		data12.height=30;
		//data12.right = new FormAttachment (87, 0);
		data12.bottom = new FormAttachment (100, 0);
		back_button.setLayoutData(data12); 
		back_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed back home");
				disposeScreen();
				mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),"QAQA - USERNAME"); //QAQA ADD USERNAME
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
		
	}
}
