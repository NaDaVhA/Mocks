package ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ole.win32.DISPPARAMS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import utilities.Pair;

public class initilazeDbScreen extends Screen {
	

	public static ProgressBar bar=null;
	private Label label=null;
	public static int bar_count;
	private int bar_max;
	private boolean init_bool;

	
	public initilazeDbScreen(Display display, Shell shell,ApplicationInterface engine){
		super(display, shell, engine);
		bar_count=0;
		bar_max=0;
	}
	
	
	
	public boolean initilize(){
		
	
		
		getShell().setLayout(new GridLayout(3, false));
		
		
		label =new Label(getShell(), SWT.NONE);
		label.setText("Please wait while we are initilizintg the db");
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan=3;
		label.setLayoutData(data);
		label.setFont(SWTResourceManager.getFont("MV Boli", 26, SWT.BOLD));
		bar = new ProgressBar(getShell(), SWT.SMOOTH);
		//Rectangle clientArea = shell.getClientArea ();
		//bar.setBounds (clientArea.x, clientArea.y, 200, 32);
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.horizontalSpan=3;
		bar.setLayoutData(data1);
		bar_max=bar.getMaximum();
		getShell().pack();
		getShell().open();
		
		/*******/
		class initilizer implements Runnable {
			
			
			@Override
			public void run() {
				//final Pair<String, String> friend_song=theMusicalNetwork.nadav.getStatusSong(friend_name_to_show);
				boolean b = engine.initializeApplication();
				
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						//status_song=status;
						bar.setSelection(bar_count);

						//Thread.currentThread();
						
					}
				});
				
			}
		}
		
		/*******/
		
		 Thread t = new Thread(new initilizer());
		 
		 t.start();
		
		   while (!getShell().isDisposed()) {
	            if (!getDisplay().readAndDispatch())
	                getDisplay().sleep();
	            
	        }
		   getDisplay().dispose();
		   
		   
		return false;
	    
	}
	
	public static void updateBar(){
		bar_count++;
	}



	@Override
	public void createScreen() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void disposeScreen() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		
	}

}


