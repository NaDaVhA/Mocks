package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class Screen {
	
	private Display display=null;
	private Shell shell=null;
	
	/**
	 * CTOR
	 * @param display
	 * @param shell
	 */
	protected Screen(Display display,Shell shell){
		this.display=display;
		this.shell=shell;
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
	
	//QAQA - maybe add create screen method

}
