

import java.util.Scanner;

import org.eclipse.swt.widgets.Display;

import recommender.RecommenderEngineAdapter;
import core.AppEngine;
import core.ApplicationInterface;
import ui.theMusicalNetwork;
import utilities.Pair;


public class Main {

	public static void main(String[] args) {
		//mira2 check
		boolean status = true;

		//Create a new ApplicationInterface instance
		ApplicationInterface app = new AppEngine();

		//Initialize the application
		//Add while loop here
		Pair<Integer, Boolean> zookini = app.initializeApplication();
		if(zookini.getLeft() == -1){
			//We encountered a connection problem. We need to alert the user.
			status = zookini.getRight();
		}else{
			//NADAV, we need to alert the user about the connection problem, and re-initialize the database. 
		}
		
		
	//	if(status)
			//status = app.updateMusicDatabase();


		//Continue with GUI loop
		if(status){
			
			app.InitalizeRecommender();
			
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
		
		app.terminateDBConnection();
	}

}