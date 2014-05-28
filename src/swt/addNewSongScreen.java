package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class addNewSongScreen extends Screen{
	
	//add members here
	private Label head=null;
	private Label search_label=null;
	private Text search_box=null;
	private Composite radio_search=null;
	private Button radio_search1=null;
	private Button radio_search2=null;
	private Button search_songs_button=null;	
	private Label search_results_label=null;
	private List result_list=null;
	private Button back_button=null;
	private Button add_song_button=null;

	public addNewSongScreen(Display display, Shell shell) {
		super(display, shell);
	}
	
	@Override
	public void createScreen(){
	
			//headline
			head=new Label(getShell(), SWT.NONE);
			head.setAlignment(SWT.CENTER);
			
			head.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			head.setFont(SWTResourceManager.getFont("MV Boli", 16, SWT.BOLD));
			head.setText("Add a Song");
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
			search_label.setText("Search:");
			FormData data2 = new FormData ();
			data2.width=150;
			
			data2.right = new FormAttachment (30, 0);
			data2.bottom = new FormAttachment (15, 0);
			search_label.setLayoutData(data2);
		
			//search box
			search_box=new Text(getShell(), SWT.BORDER);
			search_box.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			search_box.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

			FormData data3 = new FormData ();
			data3.width=200;
			data3.height=18;
			data3.right = new FormAttachment (53, 0);
			data3.bottom = new FormAttachment (15, 0);
			search_box.setLayoutData(data3);
			
			//radio search
			 radio_search = new Composite (getShell(),  SWT.NO_RADIO_GROUP);
			 FormData data4 = new FormData ();
				data4.width=250;
				data4.height=26;
				data4.right = new FormAttachment (80, 0);
				data4.bottom = new FormAttachment (15, 0);
				radio_search.setLayoutData(data4); 
				//radio_search.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				radio_search.setLayout (new RowLayout ());
				
				Listener radioGroup = new Listener () {
					@Override
					public void handleEvent (Event event) {
						Control [] children = radio_search.getChildren ();
						for (int j=0; j<children.length; j++) {
							Control child = children [j];
							if (child instanceof Button) {
								Button button = (Button) child;
								if ((button.getStyle () & SWT.RADIO) != 0) button.setSelection (false);
							}
						}
						Button button = (Button) event.widget;
						button.setSelection (true);
						System.out.println(button.getText()); //chosen button
					}
				};
			
			radio_search1=new Button(radio_search, SWT.RADIO);
			radio_search1.setText("Search by Artist");
			//radio_search1.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			radio_search1.addListener (SWT.Selection, radioGroup);
			radio_search1.addPaintListener(new PaintListener() {
				
				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
					e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			        int x=radio_search1.getLocation().x+11;
			        int y=radio_search1.getLocation().y-4;
			       // e.gc.drawString(string, x, y, isTransparent);
			        e.gc.drawString("Search by Artist", x,y, true );
				}
			});
			
		
			
			
			radio_search2=new Button(radio_search, SWT.RADIO);
			radio_search2.setText("Search by Song name");
			//radio_search2.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			radio_search2.addListener (SWT.Selection, radioGroup);
			
			radio_search2.addPaintListener(new PaintListener() {
				
				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
					e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			        int x1=radio_search2.getLocation().x-91;
			        int y1=radio_search2.getLocation().y-4;
			       // e.gc.drawString(string, x, y, isTransparent);
			        e.gc.drawString("Search by Song name", x1,y1, true );
				}
			});
			
			search_songs_button=new Button(getShell(),SWT.NONE);
			search_songs_button.setText("Search");
			FormData data5 = new FormData ();
			data5.width=115;
			data5.height=26;
			data5.right = new FormAttachment (93, 0);
			data5.bottom = new FormAttachment (15, 0);
			search_songs_button.setLayoutData(data5); 
			search_songs_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					System.out.println("qaqa - pressed search");
					
				}
			});
			
			
			
			
			//friend result label
			search_results_label=new Label(getShell(), SWT.BORDER);
			search_results_label.setAlignment(SWT.CENTER);
			
			search_results_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			search_results_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
			search_results_label.setText("Search Results:");
			FormData data6 = new FormData ();
			data6.width=200;
			
			data6.right = new FormAttachment (30, 0);
			data6.bottom = new FormAttachment (25, 0);
			search_results_label.setLayoutData(data6);
			
			
			
			
			//search list
			
			result_list=new List(getShell(), SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			//for (int i=0; i<12; i++) friend_result_list.add ("Friend resault " + i); //QAQA CHANGE WITH REAL SONGS
			
			result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			FormData data7 = new FormData ();
			data7.width=300;
			data7.height=100;
			data7.right = new FormAttachment (64);
			data7.bottom = new FormAttachment (42, 0);
			result_list.setLayoutData(data7);
			
			
			
			
			//search button
			add_song_button=new Button(getShell(),SWT.NONE);
			add_song_button.setText("Add the Song!");
			FormData data8 = new FormData ();
			data8.width=300;
			data8.height=26;
			data8.right = new FormAttachment (63, 0);
			data8.bottom = new FormAttachment (55, 0);
			add_song_button.setLayoutData(data8); 
			add_song_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					System.out.println("qaqa - pressed Add song");
					
				}
			});
			
			
			/*
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
			
		*/
				
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
		this.add_song_button.dispose();
		this.back_button.dispose();
		this.head.dispose();
		this.radio_search.dispose();
		this.radio_search1.dispose();
		this.radio_search2.dispose();
		this.result_list.dispose();
		this.search_box.dispose();
		this.search_label.dispose();
		this.search_results_label.dispose();
		this.search_songs_button.dispose();
		
	}

}
