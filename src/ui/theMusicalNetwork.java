package ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

import core.AppEngine;
import core.ApplicationInterface;
import core.NadavDevelopmentClass;

/**
 * @author Nadav
 *
 */
public class theMusicalNetwork {
	//members
	private Display display=null;
	private Shell shell=null;
	private Image bg_image=null;
	
	private ApplicationInterface engine;
	
	/*private logInScreen loginScreen;
	private signUpScreen signupScreen;
	private mainScreen mainScreen;
	private addNewFriendScreen addFriendScreen;
	private addNewSongScreen addSongScreen;
	private viewFriendScreen viewFriendScreen;*/
	//QAQA-DELETE AFTER QA
	public static NadavDevelopmentClass nadav;  //qaqa this  design is not good
	public static boolean qaqa=false;






	
	public theMusicalNetwork(Display d,ApplicationInterface engine){ //CTOR
		display=d;
		shell=new Shell(display,SWT.CLOSE|SWT.MIN); //QAQA - THIS disables maximize
		shell.setText("The Musical Network");	//the text at the top left of the window
		//set the shell layout
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10; 
		formLayout.spacing = 10;
		shell.setLayout(formLayout);
		
		
		
		//set the bg image
		
		String path = new File("").getAbsolutePath();
		String npath= new File(path+"\\images\\ear23.jpg").getAbsolutePath();
		
		
		bg_image=new Image(display,npath);	
		shell.setBackgroundImage(bg_image);
		
		//shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		/*this.addFriendScreen=new addNewFriendScreen(display, shell, "bug!!"); //qaqa
		this.addSongScreen =new addNewSongScreen(display, shell);
		this.loginScreen=new logInScreen(display, shell);
		this.mainScreen=new mainScreen(display, shell, "BUg!!");
		this.signupScreen= new signUpScreen(display, shell);
		this.viewFriendScreen=new viewFriendScreen(display, shell);*/
		
		//qaqa - to delete
		this.nadav=new NadavDevelopmentClass();
		this.engine=engine;
	}
	
	


public void openShell(){	

		welcomeScreen welcome=new welcomeScreen(display);
		welcome.showWelcome();
		logInScreen logIn=new logInScreen(display,shell,engine);
		logIn.createScreen();
		shell.open();
      
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
            
        }
        display.dispose();
       
  }
	
	
	

}
