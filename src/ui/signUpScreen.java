package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import utilities.Pair;
import core.ApplicationInterface;

public class signUpScreen extends Screen{
	

	private String username_s;
	private String password_s;
	private String password_repeat_s;
	
	private Label headline=null;
	private Label username=null;
	private Text user=null;
	private Label password=null;
	private Text pass=null;
	private Label R_password=null;
	private  Text R_pass=null;
	private Button signUp=null;
	private Composite c;
	
	
	
	public signUpScreen(Display display,Shell shell,ApplicationInterface engine) {
		// TODO Auto-generated constructor stub
		super(display, shell, engine);
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
		
		//the musical network label
		headline= new Label(getShell(), SWT.NONE);
		headline.setAlignment(SWT.CENTER);
		headline.setText("The Musical Network ");
		headline.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		headline.setFont(SWTResourceManager.getFont("MV Boli", 32, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		data.right = new FormAttachment (100, 0);
		headline.setLayoutData(data);
		
		c=new Composite(getShell(), SWT.NONE);
		c.setLayout(new GridLayout(2, false));
			
		FormData data1 = new FormData ();
		data1.width=300;
		data1.height=150;
		data1.top= new FormAttachment (headline, 200);
		data1.right = new FormAttachment (headline,900);
		c.setLayoutData(data1);
		
		
		//user name label
		username= new Label(c, SWT.NONE);
		username.setAlignment(SWT.CENTER);
		username.setText("Select Username:");
		username.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		username.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		//user name text		
		user=new Text(c, SWT.BORDER);
		user.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));;
		user.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		g.widthHint=120;
		user.setLayoutData(g);

		
		//password label
		password= new Label(c, SWT.NONE);
		password.setAlignment(SWT.CENTER);
		password.setText("Select Password:");
		password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		password.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		
		//password text
		pass=new Text(c, SWT.PASSWORD | SWT.BORDER);
		pass.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		pass.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g1=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		g1.widthHint=120;
		pass.setLayoutData(g1);

		
		//repeat password label
		R_password= new Label(c, SWT.NONE);
		R_password.setAlignment(SWT.CENTER);
		R_password.setText("Repeat Password:");
		R_password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		R_password.setFont(SWTResourceManager.getFont("MV Boli", 12, SWT.BOLD));
		R_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		
		//repeat password text
		R_pass=new Text(c, SWT.PASSWORD | SWT.BORDER);
		R_pass.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		R_pass.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData g2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		g2.widthHint=120;
		R_pass.setLayoutData(g2);
		
		c.pack();

		/*******/
		class CheckUserTaken implements Runnable {

			@Override
			public void run() {
				//final boolean isUserTaken=theMusicalNetwork.nadav.isUsernameTaken(signUpScreen.this.username_s);
				final Pair<Integer,Boolean> isUserTaken;
				if(theMusicalNetwork.qaqa){
					isUserTaken=theMusicalNetwork.nadav.isUsernameTaken(signUpScreen.this.username_s);
				}
				else{
					isUserTaken=engine.isUsernameTaken(signUpScreen.this.username_s);
				}
				//final boolean isUserTaken=engine.isUsernameTaken(signUpScreen.this.username_s);
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(checkConnection(getShell(), isUserTaken.getLeft())){ //no problem
							if(isUserTaken.getRight()){ //QAQA  - PUT REAL FUNCTION HERE 
								closeWaiting();
								showScreen();
								errorPop("Sign up Error", "Username already taken");
							}
							else{//username is not taken and pass is ok..  sign up the user
								//QAQA-in interface signUpUser
								/*******/
								class SignUpUser implements Runnable {

									@Override
									public void run() {
										final Pair<Integer,Boolean> signUpUser;
										//final boolean signUpUser=theMusicalNetwork.nadav.signUpUser(signUpScreen.this.username_s,signUpScreen.this.password_s);
										if(theMusicalNetwork.qaqa){
											signUpUser=theMusicalNetwork.nadav.signUpUser(signUpScreen.this.username_s,signUpScreen.this.password_s);
										}
										else{
											signUpUser=engine.signUpUser(signUpScreen.this.username_s,signUpScreen.this.password_s);
										}
										//final boolean signUpUser=engine.signUpUser(signUpScreen.this.username_s,signUpScreen.this.password_s);
										getDisplay().asyncExec(new Runnable() {
											public void run() {
												if(checkConnection(getShell(), signUpUser.getLeft())){
													if(!signUpUser.getRight()){ //QAQA  - PUT REAL FUNCTION HERE 
														closeWaiting();
														showScreen();
														errorPop("Sign up Error", "Failed to Sign up..");
													}
													else{//Sign up success.. go to main screen
														PopUpinfo(getShell(),"Sign up", "sign up succes!");
														closeWaiting();
														disposeScreen();
														logInScreen log=new logInScreen(getDisplay(), getShell(), engine);
														log.createScreen();
														//mainScreen main_screen=new mainScreen(getDisplay(), getShell(),engine,signUpScreen.this.username_s);
														//main_screen.createScreen();
														//else{...}
													}
												}
												else{
													closeWaiting();
													showScreen();
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
						else{ //problem and want to continue
							closeWaiting();
							showScreen();
						}
						
					}
				});
				
			}
		}
		
		/*******/
		
		//sign up button
		signUp=new Button(getShell(),  SWT.NONE);
		signUp.setText("Sign Up!");

		FormData data7 = new FormData ();
		data7.width=110;
		data7.height=40;
		data7.top=new FormAttachment(headline,450);
		data7.right = new FormAttachment (headline, 900);
		signUp.setLayoutData(data7);
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
					if(signUpScreen.this.password_s.isEmpty() || signUpScreen.this.password_repeat_s.isEmpty()){
						errorPop("Sign up Error", "One or more of the passwords fields are empty.\nPlease try again.");
					}
					else{
						
						// create a thread to check if user registerd
						Thread t = new Thread(new CheckUserTaken());
						openWaiting();
						t.start();
						
					
					}
				}
			}
		});
		

		
		this.getShell().layout();
		
	}
	
	@Override
	protected void disposeScreen(){
		this.headline.dispose();
		this.pass.dispose();
		this.password.dispose();
		this.R_pass.dispose();
		this.R_password.dispose();
		this.user.dispose();
		this.username.dispose();
		this.signUp.dispose();
		this.c.dispose();
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		this.headline.setVisible(false);
		this.pass.setVisible(false);
		this.password.setVisible(false);
		this.R_pass.setVisible(false);
		this.R_password.setVisible(false);
		this.user.setVisible(false);
		this.username.setVisible(false);
		this.signUp.setVisible(false);
		this.c.setVisible(false);
		this.getShell().layout();
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		this.headline.setVisible(true);
		this.pass.setVisible(true);
		this.password.setVisible(true);
		this.R_pass.setVisible(true);
		this.R_password.setVisible(true);
		this.user.setVisible(true);
		this.username.setVisible(true);
		this.signUp.setVisible(true);
		this.c.setVisible(true);
		this.getShell().layout();
		
	}

	
}
