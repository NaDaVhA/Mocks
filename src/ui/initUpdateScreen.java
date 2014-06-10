package ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import utilities.Pair;

public class initUpdateScreen extends Screen{

	private Shell bar_shell=null;
	private Label head_label=null;
	private  ProgressBar bar = null;
	public static int status;
	public static boolean finished;
	private boolean b;
	private Pair<Integer, Boolean> zookini;
	private String type;
	

	
	
	public initUpdateScreen(Display display,Shell shell,ApplicationInterface app,String type) {
		super(display, shell, app);
		this.type=type;
		status=0;
		finished=false;
		b=false;
		zookini=null;
	}
	
	public static void updateProgressBar(){
		initUpdateScreen.status++;
	}
	
	public static void setFinished(boolean value){
		finished=value;
	}
	
	public ApplicationInterface getApp(){
		return engine;
	}
	
	@Override
	public void createScreen(){
		
	//bar shell
	bar_shell = new Shell(getDisplay(),SWT.MIN);

	GridLayout layout=new GridLayout(2, false);
	bar_shell.setLayout(layout);
	
	
	//path to image
	String path = new File("").getAbsolutePath();
	String os=System.getProperty("os.name");
	String npath;
	if(os.contains("windows")){
		 npath= new File(path+"\\images\\ear23.jpg").getAbsolutePath();
	}
	else{//linux
		 npath= new File(path+"/images/ear23.jpg").getAbsolutePath();
	}
	
	//bg image
	Image bg1_image=new Image(getDisplay(),npath);	
	bar_shell.setBackgroundImage(bg1_image);
	bar_shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		
	//shell label			
	head_label=new Label(bar_shell, SWT.NONE);
	head_label.setAlignment(SWT.CENTER);
	if(type.compareTo("update")==0){
		head_label.setText("Please Wait while we are updating the db");
	}
	else{
		head_label.setText("Welcome!\nPlease Wait while we are building the db");
	}
	
	head_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); 
	head_label.setFont(SWTResourceManager.getFont("MV Boli", 16, SWT.BOLD));
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2; 
	data.widthHint=800;

	head_label.setLayoutData(data);
		
	//Progress bar
	bar=new ProgressBar(bar_shell, SWT.SMOOTH);
	
	GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
	data1.horizontalSpan = 2; 
	bar.setLayoutData(data1);
	
	bar.setMaximum(5000);
	final int maximum = 5000;
	
	bar_shell.pack();
	
		/****************/
		
	class initApp implements Runnable {

		@Override
		public void run() {
			zookini = getApp().initializeApplication();
			
			getDisplay().asyncExec(new Runnable() {
			public void run() {
				status=maximum+1;
				setFinished(true);				
			}
						
			});
			
		}
	}
	
	/*******/
		

	/****************/
	
	class updateApp implements Runnable {

		@Override
		public void run() {
			zookini = getApp().updateMusicDatabase();

			getDisplay().asyncExec(new Runnable() {
			public void run() {
				status=maximum+1;
				setFinished(true);								
				}
			});
			
		}
	}
	
	/*******/
		
		
		new Thread() {
			@Override
			public void run() {
				if(type.compareTo("update")==0){
					Thread t = new Thread(new updateApp());
					t.start();
				}
				else{
					Thread t = new Thread(new initApp());
					t.start();
				}
								
				
				for (;status <= maximum;) {
				try {
					Thread.sleep (1000);
					} catch (Throwable th) {}
					if (getDisplay().isDisposed()) return;
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							
						if (bar.isDisposed ()) {
							return;
							}
						else{
							if(!finished){
								bar.setSelection(status);
							}
							else{ //finished
								
								if(type.compareTo("update")==0){
									
									if(checkConnection(bar_shell,zookini.getLeft())){//no problem in connection
										b = zookini.getRight();
										
										if(b){ 
											bar.setSelection(maximum);
											PopUpinfo(bar_shell,"Db update info", "Finished to update db.");
											bar_shell.dispose();
											return;
											
										}
										else{
										
											PopUpinfo(bar_shell,"Db update info", "Failed to update db.");
											bar_shell.dispose();
											return;
										}
									}
									else{ //problem in connection
										bar_shell.dispose();
										return;
									}
								}
								
							else{ //type = init
									if(checkConnectionInit(bar_shell, zookini.getLeft())){
										b = zookini.getRight();
										if(b){
											
											bar.setSelection(maximum);
											PopUpinfo(bar_shell,"Db build info", "Finished to build db.");
											bar_shell.dispose();
											return;
											
										}
										else{
											
											PopUpinfo(bar_shell,"Db build info", "Failed to build db.");
											bar_shell.dispose();
											return;
										}
									}
									else{//problem with connection 
										getDisplay().dispose();
										System.exit(0);
										
									}
								}
									
	
							}
						}
						}
					});
				}
			}
		}.start();
		
		
		getShell().setVisible(false);
		
		bar_shell.open();
		
		while (!bar_shell.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	
	}



	@Override
	protected void disposeScreen() {
		// TODO Auto-generated method stub
		//not relevant for this screen
	}




	
	private boolean checkConnectionInit(Shell shell,int conection_value){

		if(conection_value==0){
			return true;
		}
		else{
			if(conection_value==-2){
				MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.ICON_WARNING);
				messageBox.setText("Yago path Problem");
				messageBox.setMessage("Path to Yago files is wrong ,please check configuration.\nThe program will exit, please fix the problem and try again.");
				if(messageBox.open()==SWT.OK)
					return false;
					else return false;
			}
		else{
			MessageBox messageBox =   new MessageBox(shell, SWT.OK| SWT.ICON_WARNING);
			messageBox.setText("Connection Problem");
			messageBox.setMessage("Connection to the db was lost.\nThe program will exit, please fix the problem and try again.");
			if(messageBox.open()==SWT.OK)
				return false;
				else return false;
		}
	}
	}

}
	
	

