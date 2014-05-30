package ui;



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
import org.eclipse.wb.swt.SWTResourceManager;


public class logInScreen extends Screen {
	
	//members
	private Label headline=null;
	private Label username=null;
	private Label password=null;
	private Button logIn=null;
	private Button signUp=null;
	private Text user=null;
	private Text pass=null;
	
	String username_s;
	String password_s;
	


	public logInScreen(Display display,Shell shell) {
		super(display, shell);	
	}
	
	
	private String getUsername_s() {
		return username_s;
	}


	private void setUsername_s(String username_s) {
		this.username_s = username_s;
	}


	private String getPassword_s() {
		return password_s;
	}


	private void setPassword_s(String password_s) {
		this.password_s = password_s;
	}
	
	@Override
	public void createScreen(){
        //set the label (headline of the app)
		headline= new Label(getShell(), SWT.NONE);
		headline.setAlignment(SWT.CENTER);
		headline.setText("The Musical Network ");
		headline.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		headline.setFont(SWTResourceManager.getFont("MV Boli", 32, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		data.right = new FormAttachment (100, 0);
		headline.setLayoutData(data);
		
		username= new Label(getShell(), SWT.NONE);
		username.setAlignment(SWT.CENTER);
		username.setText("Username:");
		username.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//username.setFont(SWTResourceManager.getFont("David", 12, SWT.BOLD));
		FormData data1 = new FormData ();
		data1.width=65;
		
		data1.right = new FormAttachment (65, 0);
		data1.bottom = new FormAttachment (45, 0);
		username.setLayoutData(data1);
		
		
		user=new Text(getShell(), SWT.BORDER);
		user.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));;
		user.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		FormData data2 = new FormData ();
		data2.width=110;
		data2.right = new FormAttachment (78, 0);
		data2.bottom = new FormAttachment (45, 0);
		user.setLayoutData(data2);

		
		
		password= new Label(getShell(), SWT.NONE);
		password.setAlignment(SWT.CENTER);
		password.setText("Password:");
		password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data3 = new FormData ();
		data3.width=70;
		data3.right = new FormAttachment (65, 2);
		data3.bottom = new FormAttachment (55, 0);
		password.setLayoutData(data3);
		
		
		pass=new Text(getShell(), SWT.PASSWORD | SWT.BORDER);
		pass.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		pass.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		FormData data4 = new FormData ();
		data4.width=110;
		data4.right = new FormAttachment (78, 0);
		data4.bottom = new FormAttachment (55, 0);
		pass.setLayoutData(data4);
		
		
		logIn=new Button(getShell(),  SWT.NONE);
		logIn.setText("Log in");
		FormData data5 = new FormData ();
		data5.width=110;
		data5.height=70;
		data5.right = new FormAttachment (92, 0);
		data5.bottom = new FormAttachment (55, 0);
		logIn.setLayoutData(data5);
		/*******/
		class CheckUserRegisterd implements Runnable {

			@Override
			public void run() {
				final boolean isUserRegisterd=theMusicalNetwork.nadav.isUserRegisterd(getUsername_s(), getPassword_s());
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(isUserRegisterd){ //QAQA  - PUT REAL FUNCTION HERE 
							disposeScreen(); //he is registerd!
							mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),getUsername_s());
							mainScreen.createScreen();
						}
						else{
							errorPop("Log In Error", "Details given do not match a valid user.\n"+"Retype username and password.");
						}
					}
				});
				
			}
		}
		
		/*******/
		logIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed log in"); //qaqa
				setUsername_s(user.getText());
				setPassword_s(pass.getText());
				System.out.println(getUsername_s());//qaqa
				System.out.println(getPassword_s());//qaqa
				//QAQA - isUserRegistred in the interface
				//QAQA - use asyncExec or syncExec runnable
								
				/***/
				// create a thread to check if user registerd
				Thread t = new Thread(new CheckUserRegisterd());
				t.start();
				/***/
			
			}
		});
		
		signUp=new Button(getShell(),  SWT.NONE);
		signUp.setText("Sign Up!");
		FormData data6 = new FormData ();
		data6.width=110;
		data6.height=40;
		data6.right = new FormAttachment (97, 0);
		data6.bottom = new FormAttachment (98, 0);
		signUp.setLayoutData(data6); 
		signUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed sign up"); //qaqa
				disposeScreen();
				//run other screen
				signUpScreen sign_up=new signUpScreen(getDisplay(), getShell());
				sign_up.createScreen();
			}
		});
		
		this.getShell().layout();

	}
	
	
	@Override
	protected void disposeScreen(){
		this.headline.dispose();
		this.logIn.dispose();
		this.password.dispose();
		this.signUp.dispose();
		this.username.dispose();
		this.pass.dispose();
		this.user.dispose();
	}
	
	

}
