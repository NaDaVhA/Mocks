package ui;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import core.ApplicationInterface;


public abstract class Screen {
	
	private Display display=null;
	private Shell shell=null;
	protected ApplicationInterface engine;

	protected Set<Thread> pool;
	
	protected boolean pop ;
	

	
	
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
		pop=false;
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
		if(conection_value==0){
			return true;
		}
		else{
			if(conection_value==-2){
				if(!pop){
					pop=true;
				val=PopUpWarning(shell,"Yago path Problem", "Path to Yago files is wrong.\nPlease check configuration and press ok to try again\n or cancel to exit the program.");
				if(val){
					pop=true;
					return false;
				}
				else{
					display.dispose();
					engine.terminateDBConnection();
					System.exit(0);
					return val;
				}
				}
			}
			else{
				if(!pop){
					pop=true;
				val=PopUpWarning(shell,"Connection Problem", "Connection to the db was lost.\nPlease fix your connection and press ok to try again\n or cancel to exit the program.");
				if(val){
					return false;
				}
				else{
					display.dispose();
					engine.terminateDBConnection();
					System.exit(0);
					return val;
				}
			}
			
			}
			
			return false;
		}	
	}
	
	

	
	public abstract void createScreen();
	
	protected abstract void disposeScreen();
	

}
