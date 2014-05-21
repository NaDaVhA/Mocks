package swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class mainScreen {
	private Display display=null;
	private Shell shell=null;

	public mainScreen(Display display,Shell shell) {
		// TODO Auto-generated constructor stub
		this.display=display;
		this.shell=shell;
	}

	/**
	 * @return the display
	 */
	private Display getDisplay() {
		return display;
	}

	/**
	 * @return the shell
	 */
	private Shell getShell() {
		return shell;
	}

	public void runMainWindow() {
		// TODO Auto-generated method stub
		
	}

}
