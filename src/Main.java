

import java.util.Scanner;

import org.eclipse.swt.widgets.Display;

import core.AppEngine;
import core.ApplicationInterface;
import ui.theMusicalNetwork;


public class Main {

	public static void main(String[] args) {
		//mira2 check
		boolean status = true;
		
		//Create a new ApplicationInterface instance
		ApplicationInterface app = new AppEngine();
		
		//Initialize the application
		status = app.initializeApplication();
		
	//	if(status)
			//status = app.updateMusicDatabase();

		
		//Continue with GUI loop
		if(status){
			//NADAV - Continue with GUI ...
			Display display=Display.getDefault();
			theMusicalNetwork app1=new theMusicalNetwork(display,app);
			app1.openShell();
			
			
		}else{
			System.out.println("Error: Coulnd'nt initialize database. Try again!");
			//End run
			return;
		}

		System.out.println("End! ...");
		
	}

}
