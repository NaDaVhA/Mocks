

import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import core.AppEngine;
import core.ApplicationInterface;
import core.NadavDevelopmentClass;
import ui.theMusicalNetwork;
import ui.initUpdateScreen;
import utilities.Pair;


public class Main {

	public static void main(String[] args) {
		//mira2 check
		boolean status = true;
	
		
		//Create a new ApplicationInterface instance
		
		ApplicationInterface app = new AppEngine();



		
		//this code is for inti db
	/*	ApplicationInterface app1=new NadavDevelopmentClass();
		initUpdateScreen init=new initUpdateScreen(display,shell, app,"init");
		init.createScreen();*/
		//
		
		
	//	if(status)
			//status = app.updateMusicDatabase();


		//Continue with GUI loop
		//if(status){
			//NADAV - Continue with GUI ...
			Display display=Display.getDefault();
			theMusicalNetwork net=new theMusicalNetwork(display,app);
			net.runApp();


	//	}else{
			//System.out.println("Error: Coulnd'nt initialize database. Try again!");
			//End run
		//	return;
		//}

		System.out.println("End! ...");
		
		app.terminateDBConnection();
	}

}