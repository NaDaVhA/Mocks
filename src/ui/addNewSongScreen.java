package ui;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import utilities.Pair;

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
	private Table result_list=null;
	private Button back_button=null;
	private Button add_song_button=null;
	
	private boolean searchByArtist;
	private String text_to_search;
	private Pair<String,String> song_chosen;
	
	private Thread t8;
	
	

	public addNewSongScreen(Display display, Shell shell,ApplicationInterface engine) {
		super(display, shell, engine);
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
								if ((button.getStyle () & SWT.RADIO) != 0){ button.setSelection (false);}
							}
							
						}
						Button button = (Button) event.widget;
						button.setSelection (true);
						System.out.println(button.getText()); //chosen button
						if(button.getText().contains("Song")){
							searchByArtist=false;
						}
						else{
							searchByArtist=true;
						}
						System.out.println(searchByArtist);
					}
				};
			
			radio_search1=new Button(radio_search, SWT.RADIO);
			radio_search1.setText("Search by Artist");
			//radio_search1.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			radio_search1.addListener (SWT.Selection, radioGroup);
			radio_search1.setSelection(true);
			searchByArtist=true;
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
			
			/*******/
			class searchSongs implements Runnable {

				@Override
				public void run() {
					final Pair<Integer,ArrayList<Pair<String, String>>> list ;
					if(searchByArtist){
						if(theMusicalNetwork.qaqa){
							list=theMusicalNetwork.nadav.getSearchResultsByArtist(text_to_search);
						}
						else{ //true code
							 list=engine.getSearchResultsByArtist(text_to_search);
						}
					
					}
					else{
						if(theMusicalNetwork.qaqa){
							 list=theMusicalNetwork.nadav.getSearchResultsBySong(text_to_search);
						}
						else{//real code
							list=engine.getSearchResultsBySong(text_to_search);
						}
							 
					}
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							//status_song=status;
							if(checkConnection(getShell(), list.getLeft())){
								result_list.removeAll();
								if(list.getRight().isEmpty()){
									PopUpinfo(getShell(), "Search results info", "The search results are empty!");
								}
								else{
									for(Pair<String, String> s:list.getRight()){
										TableItem item = new TableItem (result_list, SWT.NONE);
										item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
										//item.setText(0, "artist "+s.getLeft());
										//item.setText(1, "songggggggggggggggggggggggggggggggggggg "+s.getRight());
										item.setText(0, s.getLeft());
										item.setText(1, s.getRight());
									}
								}
							}
							else{ // problem want to cont..
								
							}
							
							
							pool.remove(t8);
							if(pool.isEmpty()){
								closeWaiting();
								showScreen();
							}
						}
					});
					
				}
			}
			
			/*******/
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
					song_chosen=null;
					text_to_search=search_box.getText();
					System.out.println("qaqa - search with :"+text_to_search);
					if(text_to_search.isEmpty()){
						errorPop("Search error", "You didnt enter any text to search.\nPlease enter text and try again.");
					}
					else{		
					 openWaiting();			
					 t8 = new Thread(new searchSongs());
					 pool.add(t8);
					 t8.start();
					}
					
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
			data6.bottom = new FormAttachment (26, 0);
			search_results_label.setLayoutData(data6);
			
			
			
			
			//search list
			
			result_list=new Table (getShell(), SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
			result_list.setLinesVisible (true);
			result_list.setHeaderVisible (true);
			TableColumn column = new TableColumn (result_list, SWT.NONE);
			column.setText ("Artist");
			TableColumn column1 = new TableColumn (result_list, SWT.NONE);
			column1.setText ("Song name");
			column.setWidth(160);
			column1.setWidth(160);
			
			
			
			
			//result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			FormData data7 = new FormData ();
			data7.width=304;
			data7.height=250;
			data7.right = new FormAttachment (64);
			data7.bottom = new FormAttachment (78, 0);
			result_list.setLayoutData(data7);
			result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			
			result_list.addListener(SWT.Selection, new Listener () {
				@Override
				public void handleEvent (Event event) {
					//System.out.println(event.data);//qaqa
					int [] selection = result_list.getSelectionIndices ();
					
					for (int i=0; i<selection.length; i++) {
						System.out.println(result_list.getItem(selection[i])); //qaqa
							song_chosen=new Pair<String, String>(result_list.getItem(selection[i]).getText(0)
									, result_list.getItem(selection[i]).getText(1));
						}
					//openWaiting();
					
					
				}
			});
			
			/*******/
			class addSong implements Runnable {

				@Override
				public void run() {
					final Pair<Integer,Boolean> b;
					if(theMusicalNetwork.qaqa){
						b=theMusicalNetwork.nadav.addSong(song_chosen.getRight(),song_chosen.getLeft());
					}
					else{//true code
						b=engine.addSong(song_chosen.getRight(),song_chosen.getLeft());
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
									pool.remove(t8);
								}
								else{
									
									pool.remove(t8);
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
			
			/*******/
			
			
			
			
			//search button
			add_song_button=new Button(getShell(),SWT.NONE);
			add_song_button.setText("Add the Song!");
			FormData data8 = new FormData ();
			data8.width=324;
			data8.height=26;
			data8.right = new FormAttachment (64, 0);
			data8.bottom = new FormAttachment (88, 0);
			add_song_button.setLayoutData(data8); 
			add_song_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected (SelectionEvent e) {
					System.out.println("qaqa - pressed Add song");
					if(song_chosen==null){
						errorPop("Error", "Please choose a song first.");
					}
					else{
					t8 = new Thread(new addSong());
					 pool.add(t8);
					 t8.start();
					}
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
						mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,engine.getUsername()); //QAQA ADD USERNAME
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

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		this.add_song_button.setVisible(false);
		this.back_button.setVisible(false);
		this.head.setVisible(false);
		this.radio_search.setVisible(false);
		this.radio_search1.setVisible(false);
		this.radio_search2.setVisible(false);
		this.result_list.setVisible(false);
		this.search_box.setVisible(false);
		this.search_label.setVisible(false);
		this.search_results_label.setVisible(false);
		this.search_songs_button.setVisible(false);
		this.getShell().layout();
		
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		this.add_song_button.setVisible(true);
		this.back_button.setVisible(true);
		this.head.setVisible(true);
		this.radio_search.setVisible(true);
		this.radio_search1.setVisible(true);
		this.radio_search2.setVisible(true);
		this.result_list.setVisible(true);
		this.search_box.setVisible(true);
		this.search_label.setVisible(true);
		this.search_results_label.setVisible(true);
		this.search_songs_button.setVisible(true);
		this.getShell().layout();
		
	}

}
