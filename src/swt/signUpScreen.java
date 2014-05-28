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

public class signUpScreen extends Screen{
	
	//private Display display=null;
	//private Shell shell=null;
	private String username_s;
	private String password_s;
	private String password_repeat_s;
	
	private Label username=null;
	private Text user=null;
	private Label password=null;
	private Text pass=null;
	private Label R_password=null;
	private  Text R_pass=null;
	private Button signUp=null;
	
	public signUpScreen(Display display,Shell shell) {
		// TODO Auto-generated constructor stub
		super(display, shell);
	}
	
	private void setUsername(String username){
		this.username_s=username;
	}
	private void setPassword(String password){
		this.password_s=password;
	}
	private void setPasswordRepeat(String password){
		this.password_repeat_s=password;
	}
	
	
	@Override
	public void createScreen() {
		// TODO Auto-generated method stub
		username= new Label(getShell(), SWT.NONE);
		username.setAlignment(SWT.CENTER);
		username.setText("Select Username:");
		username.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//username.setFont(SWTResourceManager.getFont("David", 12, SWT.BOLD));
		FormData data1 = new FormData ();
		data1.width=100;
		
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
		password.setText("Select Password:");
		password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data3 = new FormData ();
		data3.width=100;
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
		
		R_password= new Label(getShell(), SWT.NONE);
		R_password.setAlignment(SWT.CENTER);
		R_password.setText("Repeat Password:");
		R_password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData data5 = new FormData ();
		data5.width=100;
		data5.right = new FormAttachment (65, 2);
		data5.bottom = new FormAttachment (65, 0);
		R_password.setLayoutData(data5);
		
		
		R_pass=new Text(getShell(), SWT.PASSWORD | SWT.BORDER);
		R_pass.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		R_pass.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		FormData data6 = new FormData ();
		data6.width=110;
		data6.right = new FormAttachment (78, 0);
		data6.bottom = new FormAttachment (65, 0);
		R_pass.setLayoutData(data6);
		
		signUp=new Button(getShell(),  SWT.NONE);
		signUp.setText("Sign Up!");
		FormData data7 = new FormData ();
		data7.width=110;
		data7.height=40;
		data7.right = new FormAttachment (97, 0);
		data7.bottom = new FormAttachment (98, 0);
		signUp.setLayoutData(data7);
		
		
		/*******/
		class CheckUserTaken implements Runnable {

			@Override
			public void run() {
				final boolean isUserTaken=theMusicalNetwork.nadav.isUsernameTaken(signUpScreen.this.username_s);
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(isUserTaken){ //QAQA  - PUT REAL FUNCTION HERE 
							errorPop("Sign up Error", "Username already taken");
						}
						else{//username is not taken and pass is ok..  sign up the user
							//QAQA-in interface signUpUser
							/*******/
							class SignUpUser implements Runnable {

								@Override
								public void run() {
									final boolean signUpUser=theMusicalNetwork.nadav.signUpUser(signUpScreen.this.username_s,signUpScreen.this.password_s);
									getDisplay().asyncExec(new Runnable() {
										public void run() {
											if(!signUpUser){ //QAQA  - PUT REAL FUNCTION HERE 
												errorPop("Sign up Error", "Failed to Sign up..");
											}
											else{//Sign up success.. go to main screen
												
												disposeScreen();
												mainScreen main_screen=new mainScreen(getDisplay(), getShell(),signUpScreen.this.username_s);
												main_screen.createScreen();
												//else{...}
											}
										}
									});
									
								}
							}
							
							/*******/
							
							/***/
							// create a thread to check if user registerd
							Thread t1 = new Thread(new SignUpUser());
							t1.start();
							/***/
						}
					}
				});
				
			}
		}
		
		/*******/
		
		
		signUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				System.out.println("qaqa - pressed sign up"); //qaqa
				setUsername(user.getText());
				setPassword(pass.getText());
				setPasswordRepeat(R_pass.getText());
				System.out.println("qaqa - username: "+signUpScreen.this.username_s); //qaqa
				System.out.println("qaqa - pass: "+signUpScreen.this.password_s); //qaqa
				System.out.println("qaqa - pass repeat: "+signUpScreen.this.password_repeat_s); //qaqa
				if(signUpScreen.this.password_s.compareTo(signUpScreen.this.password_repeat_s)!=0){
					errorPop("Sign up Error", "Passwords doesnt match.\nPlease try again.");
				}
				else{
					
					/***/
					// create a thread to check if user registerd
					Thread t = new Thread(new CheckUserTaken());
					t.start();
					/***/
				
				}
				
			}
		});
		
		this.getShell().layout();
		
	}
	
	@Override
	protected void disposeScreen(){
		this.pass.dispose();
		this.password.dispose();
		this.R_pass.dispose();
		this.R_password.dispose();
		this.user.dispose();
		this.username.dispose();
		this.signUp.dispose();
	}

	
}
