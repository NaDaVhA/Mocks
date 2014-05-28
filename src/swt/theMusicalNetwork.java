package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

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
	
	//QAQA-DELETE AFTER QA
	public static NadavDevelopmentClass nadav;  //qaqa this  design is not good


	
	public theMusicalNetwork(Display d){ //CTOR
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
		bg_image=new Image(display,"C:\\Users\\Nadav\\git\\Mocks\\images\\ear23.jpg");	
		shell.setBackgroundImage(bg_image);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		//qaqa - to delete
		this.nadav=new NadavDevelopmentClass();
		
	}
	
	


public void openShell(){	

		welcomeScreen welcome=new welcomeScreen(display);
		welcome.showWelcome();
		logInScreen logIn=new logInScreen(display,shell);
		logIn.createScreen();
		shell.open();
      
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
            
        }
        display.dispose();
  }
	
	
	

}
