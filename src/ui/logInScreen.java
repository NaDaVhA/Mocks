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


public class logInScreen extends Screen {
	
	//members
	private Composite c=null;
	private Composite c1=null;
	
	private Label headline=null;
	private Label username=null;
	private Label password=null;
	private Button logIn=null;
	private Button signUp=null;
	private Button updateDb=null;
	private Text user=null;
	private Text pass=null;
	
	String username_s;
	String password_s;
	


	public logInScreen(Display display,Shell shell,ApplicationInterface engine) {
		super(display, shell,engine);	
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
		
        //The musical network label
		headline= new Label(getShell(), SWT.NONE);
		headline.setAlignment(SWT.CENTER);
		headline.setText("The Musical Network ");
		headline.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		headline.setFont(SWTResourceManager.getFont("MV Boli", 32, SWT.BOLD));

		FormData data = new FormData ();
		data.width=1000;
		headline.setLayoutData(data);
		
		
		
		
		c=new Composite(getShell(), SWT.NONE);
		c.setLayout(new GridLayout(3, false));
		FormData data1 = new FormData ();
		data1.top= new FormAttachment (headline, 200);
		data1.right = new FormAttachment (headline,900);
		c.setLayoutData(data1);
		
		
		//user name label
		username= new Label(c, SWT.FILL);
		username.setAlignment(SWT.CENTER);
		username.setText("Username:");
		username.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		username.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		

		//user name text
		
		user=new Text(c, SWT.BORDER);
		user.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));;
		user.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		GridData g=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		g.widthHint=120;
		user.setLayoutData(g);
		
		
		/*******/
		class CheckUserRegisterd implements Runnable {

			@Override
			public void run() {
				final Pair<Integer,Boolean> isUserRegisterd;
				isUserRegisterd=engine.isUserRegisterd(getUsername_s(), getPassword_s());
		
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(checkConnection(getShell(),isUserRegisterd.getLeft())){ //want to try again
							if(isUserRegisterd.getRight()){ 		
								disposeScreen(); 
								mainScreen mainScreen=new mainScreen(getDisplay(),getShell(),engine,getUsername_s());
								mainScreen.createScreen();
							}
							else{
								getShell().layout();
								errorPop("Log In Error", "Details given do not match a valid user.\n"+"Retype username and password.");
								
							}
							return;
						}
						else{
							getShell().layout();
							return;
						}
					
					}
				});
				
			}
		}
		
		/*******/
		
		//log in button
		
		logIn=new Button(c,  SWT.NONE);
		logIn.setText("Log in");
		GridData g1=new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 2);
		g1.widthHint=120;
		logIn.setLayoutData(g1);
		logIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				
				System.out.println("qaqa - pressed log in"); //qaqa
				
				setUsername_s(user.getText());
				setPassword_s(pass.getText());
				
				
				if(getUsername_s().isEmpty() || getPassword_s().isEmpty()){
					errorPop("Log in Error", "Username or password fields are empty.");
				}

								
				else{
					if(!engine.checkConnection()){
						pop=false;
						checkConnection(getShell(), -1);
					}
					else{
						Thread t = new Thread(new CheckUserRegisterd());
						getShell().layout();
						t.start();
					}
				}
			}
		});
		
		//password label
		password= new Label(c, SWT.NONE);
		password.setAlignment(SWT.CENTER);
		password.setText("Password:");
		password.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		password.setFont(SWTResourceManager.getFont("MV Boli", 14, SWT.BOLD));
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		
		//password text	
		pass=new Text(c, SWT.PASSWORD | SWT.BORDER);
		pass.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		pass.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		pass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		c.pack();

		
		c1=new Composite(getShell(), SWT.NONE);
		c1.setLayout(new GridLayout(1, false));
		
		FormData data2 = new FormData ();
		data2.width=120;
		data2.height=75;
		data2.top= new FormAttachment (headline, 400);
		data2.right = new FormAttachment (headline,900);
		c1.setLayoutData(data2);
		
		
		//sign up button
		signUp=new Button(c1,  SWT.PUSH);
		signUp.setText("Sign Up!");
		
		GridData g2=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		
		signUp.setLayoutData(g2);
		signUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				if(!engine.checkConnection()){
					pop=false;
					checkConnection(getShell(), -1);
				}
				else{
					disposeScreen();
					//run other screen
					signUpScreen sign_up=new signUpScreen(getDisplay(), getShell(),engine);
					sign_up.createScreen();
				}
			}
		});
		
		
		//update db button
		updateDb=new Button(c1,  SWT.PUSH);
		updateDb.setText("Update the db");
		GridData g3=new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		updateDb.setLayoutData(g3);
		c1.pack();

		updateDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				getShell().setVisible(false);
				initUpdateScreen update=new initUpdateScreen(getDisplay(), getShell(), engine,"update");
				update.createScreen();
				getShell().setVisible(true);

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
		this.updateDb.dispose();
		this.c.dispose();
		this.c1.dispose();
	}



}
