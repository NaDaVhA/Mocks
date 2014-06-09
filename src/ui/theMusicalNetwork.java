package ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import utilities.Pair;
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
	private Shell main_shell=null;
	//private Shell bar_shell=null;
	private Image bg_image=null;
	
	private ApplicationInterface engine;
	private Pair<Integer, Boolean> zookini1=null;
	private boolean init=false;
	
	public static NadavDevelopmentClass nadav;  //qaqa this  design is not good
	public static boolean qaqa=true;



	
	public theMusicalNetwork(Display d,ApplicationInterface engine){ //CTOR
		display=d;
		main_shell=new Shell(display,SWT.CLOSE|SWT.MIN); //QAQA - THIS disables maximize
		main_shell.setText("The Musical Network");	//the text at the top left of the window
		//set the shell layout
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10; 
		formLayout.spacing = 10;
		main_shell.setLayout(formLayout);
		
		
		
		//set the bg image
		
		String path = new File("").getAbsolutePath();
		String os=System.getProperty("os.name");
		String npath;
		if(os.contains("windows")){
			 npath= new File(path+"\\images\\ear23.jpg").getAbsolutePath();
		}
		else{//linux
			 npath= new File(path+"/images/ear23.jpg").getAbsolutePath();
		}
		
		
		
		bg_image=new Image(display,npath);	
		Rectangle rec= bg_image.getBounds();
		main_shell.setBackgroundImage(bg_image);
		
		//shell.setBackgroundMode(SWT.INHERIT_FORCE);
		main_shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		main_shell.setBounds(rec.x, rec.y, rec.width, rec.height);	
				

		//qaqa - to delete
		this.nadav=new NadavDevelopmentClass();
		this.engine=engine;
	}
	


public void runApp(){	
	
	zookini1=engine.checkInitializationStatus();
	System.out.println(zookini1.getLeft());
	System.out.println(zookini1.getRight());
	
	if(zookini1.getLeft()==-1){//no connection
		MessageBox messageBox =   new MessageBox(main_shell, SWT.OK| SWT.ICON_ERROR);
		messageBox.setText("Connection Problem");
		messageBox.setMessage("you have no Connection to db\nPlease reconnect and try again.");
		messageBox.open();
		display.dispose();
		System.exit(0);
	}
	else{//we have connection
		if(zookini1.getRight()){ //db already initilized
			init=true;
			
		}
		else{
			init=false;
			
		}
		
	}

	if(!init){
		if(qaqa){
			initUpdateScreen init=new initUpdateScreen(display,main_shell, nadav,"init");
			init.createScreen();
			//nadav.InitalizeRecommender();
		}
		else{//real code
			initUpdateScreen init=new initUpdateScreen(display,main_shell, engine,"init");
			init.createScreen();
			
		}
		
	}
		
	logInScreen logIn=new logInScreen(display,main_shell,engine);
	logIn.createScreen();
		
	main_shell.setVisible(true);
		
	while (!main_shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		

  }


	
	
	

}
