package swt;



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class logInScreen {
	//members
	private Display display=null;
	private Shell shell=null;
	private Label headline=null;
	private Label username=null;
	private Label password=null;
	private Button logIn=null;
	private Button signUp=null;
	private Text user=null;
	private Text pass=null;
	
	String username_s;
	String password_s;
	
	
	public String getUsername_s() {
		return username_s;
	}


	public void setUsername_s(String username_s) {
		this.username_s = username_s;
	}


	public String getPassword_s() {
		return password_s;
	}


	public void setPassword_s(String password_s) {
		this.password_s = password_s;
	}


	public logInScreen(Display display,Shell shell) {
		 this.display=display;
		 this.shell=shell;
		
	}
	
	
public void createLogInscreen(){
        //set the label (headline of the app)
		headline= new Label(shell, SWT.NONE);
		headline.setAlignment(SWT.CENTER);
		headline.setText("The Musical Network ");
		headline.setForeground(display.getSystemColor(SWT.COLOR_WHITE)); //change color to white
		headline.setFont(SWTResourceManager.getFont("MV Boli", 32, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		data.right = new FormAttachment (100, 0);
		headline.setLayoutData(data);
		
		username= new Label(shell, SWT.NONE);
		username.setAlignment(SWT.CENTER);
		username.setText("Username:");
		username.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		//username.setFont(SWTResourceManager.getFont("David", 12, SWT.BOLD));
		FormData data1 = new FormData ();
		data1.width=65;
		
		data1.right = new FormAttachment (65, 0);
		data1.bottom = new FormAttachment (45, 0);
		username.setLayoutData(data1);
		
		
		 user=new Text(shell, SWT.BORDER);
		user.setForeground(display.getSystemColor(SWT.COLOR_BLACK));;
		user.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		FormData data2 = new FormData ();
		data2.width=110;
		data2.right = new FormAttachment (78, 0);
		data2.bottom = new FormAttachment (45, 0);
		user.setLayoutData(data2);

		
		
		password= new Label(shell, SWT.NONE);
		password.setAlignment(SWT.CENTER);
		password.setText("Password:");
		password.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		FormData data3 = new FormData ();
		data3.width=70;
		data3.right = new FormAttachment (65, 2);
		data3.bottom = new FormAttachment (55, 0);
		password.setLayoutData(data3);
		
		
		pass=new Text(shell, SWT.PASSWORD | SWT.BORDER);
		pass.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		pass.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		FormData data4 = new FormData ();
		data4.width=110;
		data4.right = new FormAttachment (78, 0);
		data4.bottom = new FormAttachment (55, 0);
		pass.setLayoutData(data4);
		
		
		logIn=new Button(shell,  SWT.NONE);
		logIn.setText("Log in");
		FormData data5 = new FormData ();
		data5.width=110;
		data5.height=70;
		data5.right = new FormAttachment (92, 0);
		data5.bottom = new FormAttachment (55, 0);
		logIn.setLayoutData(data5);
		logIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed log in"); //qaqa
				setUsername_s(user.getText());
				setPassword_s(pass.getText());
				System.out.println(getUsername_s());//qaqa
				System.out.println(getPassword_s());//qaqa
				//isUserRegistred in the interface
				if(getUsername_s().compareTo("true")==0){ //QAQA  - PUT REAL FUNCTION HERE!
					disposeLogIn();
					mainScreen mainScreen=new mainScreen(display,shell);
					mainScreen.runMainWindow();
				}
				else{
					MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.ICON_ERROR);
					messageBox.setText("Log In Error");
					messageBox.setMessage("Details given do not match a valid user.\n"+"Retype username and password.");
					messageBox.open();
				}
			}
		});
		
		signUp=new Button(shell,  SWT.NONE);
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
				disposeLogIn();
				//run other screen
				signUpScreen sign_up=new signUpScreen(display, shell);
				sign_up.signUpScreen();
			}
		});

	}

public void disposeLogIn(){
	this.headline.dispose();
	this.logIn.dispose();
	this.password.dispose();
	this.signUp.dispose();
	this.username.dispose();
	this.pass.dispose();
	this.user.dispose();
}
	
	

}
