

import java.util.Scanner;

import org.eclipse.swt.widgets.Display;

import ui.theMusicalNetwork;


public class Main {

	public static void main(String[] args) {
		
		System.out.println("Hello Mocks!");
		
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
