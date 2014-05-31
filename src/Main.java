

import java.util.Scanner;

import org.eclipse.swt.widgets.Display;

import core.AppEngine;
import core.ApplicationInterface;
import ui.theMusicalNetwork;


public class Main {

	public static void main(String[] args) {
				
		boolean status = true;
		
		//Create a new ApplicationInterface instance
		ApplicationInterface app = new AppEngine();
		
		//Initialize the application
		status = app.initializeApplication();
		
		//Continue with GUI loop
		if(status){
			//NADAV - Continue with GUI ...
			System.out.println("Great! we can run the GUI!");
			//use ApplicationInterface app here.
			
			
		}else{
			System.out.println("Error: Coulnd'nt initialize database. Try again!");
			//End run
			return;
		}
		
		
	//	Scanner in = new Scanner(System.in);	 //just for qa
		//Create new db
	//	String pass;
		
	//	System.out.println("enter password here");
	//	pass=in.nextLine();
	//	Application app = new Application(pass);
		//app.runApplication();
		//Run the application gui
		
		Display display=Display.getDefault();
		theMusicalNetwork app1=new theMusicalNetwork(display);
		app1.openShell();
		
		System.out.println("End! ...");
		
	}

}
