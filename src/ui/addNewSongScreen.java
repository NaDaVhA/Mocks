package ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
	
	//members 
	private Label head=null;
	private Label search_label=null;
	private Text search_box=null;

	private Button search_songs_button=null;	
	private Label search_results_label=null;
	private Table result_list=null;
	private Button back_button=null;
	private Button add_song_button=null;
	
	private boolean searchByArtist;
	private String text_to_search;
	private Pair<String,String> song_chosen;
	
	private Composite c=null;
	private Composite c1=null;
	
	
	private CCombo combo;
	
	private Thread t8;
	
	

	public addNewSongScreen(Display display, Shell shell,ApplicationInterface engine) {
		super(display, shell, engine);
	}
	
	
	
	@Override
	public void createScreen(){
	
	//add a song label
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
	
	
	//Composite
	c=new Composite(getShell(), SWT.NONE);
	c.setLayout(new GridLayout(4, false));
	FormData data2 = new FormData ();
	data2.width=800;
	data2.height=45;
	data2.right = new FormAttachment (head, 550);
	data2.top = new FormAttachment (head, 15);
	c.setLayoutData(data2);
	
	
			
	//search label
	search_label=new Label(c, SWT.BORDER);
	search_label.setAlignment(SWT.CENTER);
	search_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	search_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
	search_label.setText("Search:");
	search_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		
	//search box
	search_box=new Text(c, SWT.BORDER);
	search_box.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	search_box.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			
	GridData g=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
	g.widthHint=250;
	search_box.setLayoutData(g);
		
	
	
	//search by
	combo = new CCombo(c, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);	
	combo.add("Search by Artist");
	combo.add("Search by Song name");
	combo.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	
	combo.setText("Search by Artist");
	searchByArtist=true;
	
	GridData g1=new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
	g1.widthHint=150;
	combo.setLayoutData(g1);
	
	combo.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(combo.getSelectionIndex()==1){
				searchByArtist=false;
			}
			else{
				searchByArtist=true;
			}
		}
	});
			
			
	/*******/
		class searchSongs implements Runnable {

				@Override
				public void run() {
					final Pair<Integer,ArrayList<Pair<String, String>>> list ;
					if(searchByArtist){
						list=engine.getSearchResultsByArtist(text_to_search);
					}
					else{
						list=engine.getSearchResultsBySong(text_to_search);	 
					}
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							
							if(checkConnection(getShell(), list.getLeft())){
								result_list.removeAll();
								if(list.getRight().isEmpty()){
									PopUpinfo(getShell(), "Search results info", "The search results are empty!");
								}
								else{
									for(Pair<String, String> s:list.getRight()){
										TableItem item = new TableItem (result_list, SWT.NONE);
										item.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
										item.setText(0, s.getLeft());
										item.setText(1, s.getRight());
									}
								}
							}
							else{ // problem want to cont..
								
								//do nothing just stay in the screen
							}
							
							
							pool.remove(t8);
							if(pool.isEmpty()){
								getShell().layout();
							}
						}
					});
					
				}
			}
			
			/*******/
				
		//search button
		search_songs_button=new Button(c,SWT.NONE);
		search_songs_button.setText("Search");
		search_songs_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		search_songs_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				song_chosen=null;
				text_to_search=search_box.getText();
				if(text_to_search.isEmpty()){
					errorPop("Search error", "You didnt enter any text to search.\nPlease enter text and try again.");
				}
				else{				
				 getShell().layout();
				 t8 = new Thread(new searchSongs());
				 pool.add(t8);
				 t8.start();
				}
				
			}
		});
			
			
		//Composite
		c1=new Composite(getShell(), SWT.NONE);
		c1.setLayout(new GridLayout(2, false));
		FormData data3 = new FormData ();
		data3.width=800;
		data3.height=300;
		
		data3.right = new FormAttachment (c, 0,SWT.RIGHT);
		data3.top = new FormAttachment (c, 15,SWT.BOTTOM);
		c1.setLayoutData(data3);
		
		
			
		//friend result label
		search_results_label=new Label(c1, SWT.BORDER);
		search_results_label.setAlignment(SWT.CENTER);
		
		search_results_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		search_results_label.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		search_results_label.setText("Search Results:");
		search_results_label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 2));
			
			
			
			
		//search list
		
		result_list=new Table (c1, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		result_list.setLinesVisible (true);
		result_list.setHeaderVisible (true);
		TableColumn column = new TableColumn (result_list, SWT.NONE);
		column.setText ("Artist");
		TableColumn column1 = new TableColumn (result_list, SWT.NONE);
		column1.setText ("Song name");
		column.setWidth(310);
		column1.setWidth(310);
		
		result_list.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g3=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		g3.heightHint=200;
		g3.widthHint=550;
		result_list.setLayoutData(g3);
			
		result_list.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event event) {
			
				int [] selection = result_list.getSelectionIndices ();
				
				for (int i=0; i<selection.length; i++) {
						song_chosen=new Pair<String, String>(result_list.getItem(selection[i]).getText(0)
								, result_list.getItem(selection[i]).getText(1));
					}
				
			}
		});
			
		/*******/
		class addSong implements Runnable {

			@Override
			public void run() {
				final Pair<Integer,Boolean> b;
				b=engine.addSong(song_chosen.getRight(),song_chosen.getLeft());
			
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						
						if(checkConnection(getShell(), b.getLeft())){
							if(!b.getRight()){
								getShell().layout();
								errorPop("Add song error", "Failed to add song.");	
								pool.remove(t8);
							}
							else{
								
								pool.remove(t8);
								if(pool.isEmpty()){
									getShell().layout();
									PopUpinfo(getShell(),"Added Song", "The song was added to your song list successfully!");
								}
							}
						}
						
						else{ //problem with connection want to cont..
							
							//do nothing - stay on the screen
						}
				
					
					}
				});
				
			}
		}
		
		/*******/
			
			
			
			
		//add song button
		add_song_button=new Button(c1,SWT.NONE);
		add_song_button.setText("Add the Song!");
		add_song_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
 
		add_song_button.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected (SelectionEvent e) {
				
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
		data12.width=180;
		data12.height=30;
		data12.bottom = new FormAttachment (100, 0);
		back_button.setLayoutData(data12); 
		back_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				disposeScreen();
				mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,engine.getUsername()); 
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
		this.result_list.dispose();
		this.search_box.dispose();
		this.search_label.dispose();
		this.search_results_label.dispose();
		this.search_songs_button.dispose();
		this.combo.dispose();
		this.c.dispose();
		this.c1.dispose();
		
	}


}
