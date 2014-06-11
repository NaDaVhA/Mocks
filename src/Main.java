

import org.eclipse.swt.widgets.Display;
import core.AppEngine;
import core.ApplicationInterface;
import ui.theMusicalNetwork;



public class Main {

	public static void main(String[] args) {

		
		//Create a new ApplicationInterface instance
		ApplicationInterface app = new AppEngine();

		//run the application
		Display display=Display.getDefault();
		theMusicalNetwork net=new theMusicalNetwork(display,app);
		net.runApp();


		System.out.println("End! ...");
		
		//Terminate connection sources
		app.terminateDBConnection();
	}

}