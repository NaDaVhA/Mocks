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
	private Label waiting=null;
	protected ApplicationInterface engine;
	//protected ProgressBar bar=null;
	//private Composite bar_c=null;
	
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
	 * @return true iff OK button was chosen
	 */
	protected boolean PopUp(String head,String body){
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
	
	protected void openWaiting(){
		//this.hideScreen();   //qaqa 1.6.14
		waiting= new Label(getShell(), SWT.NONE);
		waiting.setAlignment(SWT.CENTER);
		waiting.setText("Please wait while we are processing your request");
		waiting.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		waiting.setFont(SWTResourceManager.getFont("MV Boli", 22, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (15, 0);
		waiting.setLayoutData(data);
		
	/*	bar_c=new Composite(getShell(), SWT.NONE);
		bar = new ProgressBar(bar_c, SWT.SMOOTH|SWT.FILL);
		
		
		FormData data1 = new FormData ();
		data1.width=1000;
		data1.right = new FormAttachment (100, 0);
		data1.bottom = new FormAttachment (50, 0);
		
		bar_c.setLayoutData(data1);
		Rectangle clientArea = bar_c.getClientArea ();
		//
		bar.setBounds (clientArea.x, clientArea.y, 1000, 32);
		//final int maximum = bar.getMaximum();
		display.timerExec(200, new Runnable() {
			int i = 0;
			@Override
			public void run() {
				if (bar.isDisposed()) return;
				bar.setSelection(i++);
				if (i <= bar.getMaximum()) display.timerExec(200, this);
			}
		});*/
		
	//	this.getShell().layout();    //qaqa 1.6.14
		
	}
	
	protected void closeWaiting(){
		
		this.waiting.dispose();
		//this.bar.dispose();
		//this.bar_c.dispose();
		//this.showScreen();
	}
	
	//QAQA - maybe add create screen method
	public abstract void createScreen();
	
	protected abstract void disposeScreen();
	
	protected abstract  void hideScreen();
	
	protected abstract void showScreen();

}
