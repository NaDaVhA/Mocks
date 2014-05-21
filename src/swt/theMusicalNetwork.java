/**
 * 
 */
package swt;



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Nadav
 *
 */
public class theMusicalNetwork {
	//members
	private Display display=null;
	private Shell shell=null;
	private Image bg_image=null;


	
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
		bg_image=new Image(display,"C:\\Users\\Nadav\\git\\Mocks\\images\\ear2.jpg");	
		shell.setBackgroundImage(bg_image);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
	}
	
	


public void openShell(){	

		welcomeScreen welcome=new welcomeScreen(display);
		logInScreen logIn=new logInScreen(display,shell);
		logIn.createLogInscreen();
		shell.open();
      
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
            
        }
        display.dispose();
  }
	
	
	

}
