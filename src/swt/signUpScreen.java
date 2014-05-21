package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class signUpScreen {
	
	private Display display=null;
	private Shell shell=null;
	
	public signUpScreen(Display display,Shell shell) {
		// TODO Auto-generated constructor stub
		this.display=display;
		this.shell=shell;
	}
	
	/**
	 * @return the display
	 */
	private Display getDisplay() {
		return display;
	}

	/**
	 * @return the shell
	 */
	private Shell getShell() {
		return shell;
	}
	
	public void signUpScreen() {
		// TODO Auto-generated method stub
		Label username= new Label(getShell(), SWT.NONE);
		username.setAlignment(SWT.CENTER);
		username.setText("Select Username:");
		username.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		//username.setFont(SWTResourceManager.getFont("David", 12, SWT.BOLD));
		FormData data1 = new FormData ();
		data1.width=100;
		
		data1.right = new FormAttachment (65, 0);
		data1.bottom = new FormAttachment (45, 0);
		username.setLayoutData(data1);
		
		
		Text user=new Text(getShell(), SWT.BORDER);
		user.setForeground(display.getSystemColor(SWT.COLOR_BLACK));;
		user.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		FormData data2 = new FormData ();
		data2.width=110;
		data2.right = new FormAttachment (78, 0);
		data2.bottom = new FormAttachment (45, 0);
		user.setLayoutData(data2);

		
		
		Label password= new Label(getShell(), SWT.NONE);
		password.setAlignment(SWT.CENTER);
		password.setText("Select Password:");
		password.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		FormData data3 = new FormData ();
		data3.width=100;
		data3.right = new FormAttachment (65, 2);
		data3.bottom = new FormAttachment (55, 0);
		password.setLayoutData(data3);
		
		
		Text pass=new Text(getShell(), SWT.PASSWORD | SWT.BORDER);
		pass.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		pass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		FormData data4 = new FormData ();
		data4.width=110;
		data4.right = new FormAttachment (78, 0);
		data4.bottom = new FormAttachment (55, 0);
		pass.setLayoutData(data4);
		
		Label R_password= new Label(getShell(), SWT.NONE);
		R_password.setAlignment(SWT.CENTER);
		R_password.setText("Repeat Password:");
		R_password.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		FormData data5 = new FormData ();
		data5.width=100;
		data5.right = new FormAttachment (65, 2);
		data5.bottom = new FormAttachment (65, 0);
		R_password.setLayoutData(data5);
		
		
		Text R_pass=new Text(getShell(), SWT.PASSWORD | SWT.BORDER);
		R_pass.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		R_pass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		FormData data6 = new FormData ();
		data6.width=110;
		data6.right = new FormAttachment (78, 0);
		data6.bottom = new FormAttachment (65, 0);
		R_pass.setLayoutData(data6);
		
		Button signUp=new Button(getShell(),  SWT.NONE);
		signUp.setText("Sign Up!");
		FormData data7 = new FormData ();
		data7.width=110;
		data7.height=40;
		data7.right = new FormAttachment (97, 0);
		data7.bottom = new FormAttachment (98, 0);
		signUp.setLayoutData(data7); 
		signUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed sign up"); //qaqa
				
			}
		});
		
		this.shell.layout();
		
	}

	
}
