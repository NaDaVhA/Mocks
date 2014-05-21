package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class welcomeScreen {

	private Display display=null;
	private Label welcome=null;
	
	
	public welcomeScreen(Display display) {
		this.display = display;
		
		showWelcome(display);
	}
	
	public void showWelcome(Display display){
		
		final Shell splash = new Shell(SWT.ON_TOP);
		FormLayout layout = new FormLayout();
		splash.setLayout(layout);
		
		Image bg1_image=new Image(display,"C:\\Users\\Nadav\\git\\Mocks\\images\\ear2.jpg");	//qaqa put here welcome img
		splash.setBackgroundImage(bg1_image);
		splash.setBackgroundMode(SWT.INHERIT_FORCE);
				
		welcome=new Label(splash, SWT.NONE);
		welcome.setAlignment(SWT.CENTER);
		welcome.setText("Welcome! -QAQA add app logo here!");
		welcome.setForeground(display.getSystemColor(SWT.COLOR_WHITE)); //change color to white
		welcome.setFont(SWTResourceManager.getFont("MV Boli", 16, SWT.BOLD));

		FormData data = new FormData ();
		//data.width=1000;
		data.right = new FormAttachment (90, 0);
		data.bottom= new FormAttachment(50,0);
		welcome.setLayoutData(data);
		
		splash.open();
		try {
			Thread.sleep(3000);  // == 3 SECONDS
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		splash.dispose();
	}
	
	
}
