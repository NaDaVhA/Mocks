

import org.eclipse.swt.widgets.Display;

import swt.theMusicalNetwork;
import core.Application;


public class Main {

	public static void main(String[] args) {
					
		//Create new application
	/*	Application app = new Application("enter password here");
		
		//Run the application
		app.runApplication();*/
		Display display=Display.getDefault();
		 theMusicalNetwork app=new theMusicalNetwork(display);
		 app.openShell();
		
		System.out.println("End! ...");
		
	}

}
