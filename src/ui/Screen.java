package ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;

public abstract class Screen {
	
	private Display display=null;
	private Shell shell=null;
	protected ApplicationInterface engine;

	protected Set<Thread> pool;
	

	
	
	/**
	 * CTOR
	 * @param display
	 * @param shell
	 */
	protected Screen(Display display,Shell shell,ApplicationInterface engine){
		this.display=display;
		this.shell=shell;
		this.engine=engine;
		pool=new HashSet<Thread>();
		
	}
	
	/**
	 * @return the display
	 */
	protected Display getDisplay() {
		return display;
	}

	/**
	 * @return the shell
	 */
	protected Shell getShell() {
		return shell;
	}
	
	
	
	/**
	 * opens a pop up window 
	 * @param head - the title of the pop up message
	 * @param body - the body of the pop up message
	 * n
	 */
	protected boolean PopUpinfo(Shell shell,String head,String body){
		MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.ICON_INFORMATION);
		messageBox.setText(head);
		messageBox.setMessage(body);
		if(messageBox.open()==SWT.OK)
			return true;
		else return false;
	}
	/**
	 * opens a pop up window 
	 * @param head - the title of the pop up message
	 * @param body - the body of the pop up message
	 * @return true iff OK button was chosen
	 */
	protected boolean PopUpWarning(Shell shell,String head,String body){
		MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.CANCEL| SWT.ICON_WARNING);
		messageBox.setText(head);
		messageBox.setMessage(body);
		if(messageBox.open()==SWT.OK)
			return true;
		else return false;
	}
	
	/**
	 * opens an error pop up
	 * @param head- the title of the pop up message
	 * @param body- the body of the pop up message
	 */
	
	protected void errorPop(String head, String body){
		MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.ICON_ERROR);
		messageBox.setText(head);
		messageBox.setMessage(body);
		messageBox.open();
	}
	
	/**
	 * 
	 * @param conection_value
	 * @return true if no problem , false if problem and want to continue
	 */
	protected boolean checkConnection(Shell shell,int conection_value){
		boolean val;
		System.out.println("QAQA - CONNECTION VALUE: "+conection_value);
		if(conection_value==0){
			return true;
		}
		else{
			
			val=PopUpWarning(shell,"Connection Problem", "Connection to the db was lost.\nPlease fix your connection and press ok to try again\n or cancel to exit the program.");
			if(val){
				return false;
			}
			else{
				display.dispose();
				engine.terminateDBConnection();//qaqa - i think it not necessary because there is no connection!!
				System.exit(0);
				return val;
			}
			
		}	
	}
	
	protected void openWaiting(){
	//	this.hideScreen();   //qaqa 1.6.14
	/*	waiting= new Label(getShell(), SWT.INHERIT_DEFAULT);
		waiting.setAlignment(SWT.CENTER);
		waiting.setText("Please wait while we are processing your request");
		waiting.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		waiting.setFont(SWTResourceManager.getFont("MV Boli", 22, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (15, 0);
		waiting.setLayoutData(data);*/
		
		this.getShell().layout();    //qaqa 1.6.14
		
	}
	
	protected void closeWaiting(){
		
		//this.waiting.dispose();
	
		//this.showScreen();
	}
	
	//QAQA - maybe add create screen method
	public abstract void createScreen();
	
	protected abstract void disposeScreen();
	
	protected abstract  void hideScreen();
	
	protected abstract void showScreen();

}
