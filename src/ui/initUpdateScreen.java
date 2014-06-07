package ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import core.ApplicationInterface;
import core.NadavDevelopmentClass;
import utilities.Pair;

public class initUpdateScreen extends Screen{

	//private Display display=null;
	//private ApplicationInterface engine;
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
		//this.display = display;
		//this.engine=app;
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
		
		//if(type.compareTo("update")==0){
	//		bar_shell = new Shell(getShell(),SWT.ON_TOP|SWT.MIN);
	//	}
	//	else{//init
			bar_shell = new Shell(getDisplay(),SWT.ON_TOP|SWT.MIN);
	//	}
		
		
			GridLayout layout=new GridLayout(2, false);
			bar_shell.setLayout(layout);
			
			String path = new File("").getAbsolutePath();
			String npath= new File(path+"\\images\\ear23.jpg").getAbsolutePath();
			
			Image bg1_image=new Image(getDisplay(),npath);	//qaqa put here welcome img
			bar_shell.setBackgroundImage(bg1_image);
			bar_shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		
				
			head_label=new Label(bar_shell, SWT.NONE);
			head_label.setAlignment(SWT.CENTER);
		if(type.compareTo("update")==0){
			head_label.setText("Please Wait while we are updating the db\nPlease be patient");
		}
		else{
			head_label.setText("Welcome!\nPlease Wait while we are building the db\nPlease be patient");
		}
		
		head_label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); //change color to white
		head_label.setFont(SWTResourceManager.getFont("MV Boli", 16, SWT.BOLD));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2; // span 2 columns
		data.widthHint=800;
	
		head_label.setLayoutData(data);
		
		
		bar=new ProgressBar(bar_shell, SWT.SMOOTH);
		
		
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.horizontalSpan = 2; // span 2 columns
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
				/*	if(checkConnection(bar_shell,zookini.getLeft())){//no problem in connection
						b = zookini.getRight();
						System.out.println(b);
						if(b){
							
							//for(int i=0;i<maximum;i++){
								bar.setSelection(maximum);
								
							//}
							//PopUpinfo("b==true", "");//qaqa
						}
						else{
							//PopUpinfo("b==false", "");//qaqa
						}
						//getDisplay().dispose();
						status=maximum+1; //qaqa 6.6
						initUpdateScreen.setFinished(true);
						
						}
					
					else{//problem in connection
						//bar_shell.dispose();
						status=maximum+1; //qaqa 6.6
						initUpdateScreen.setFinished(true);
					}*/
					
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
				/*	if(checkConnection(bar_shell,zookini.getLeft())){//no problem in connection
						b = zookini.getRight();
						System.out.println(b);
						if(b){
							
							bar.setSelection(maximum);
							status=maximum+1;
							//PopUpinfo("b==true", "");//qaqa
							//getShell().dispose();
							
						}
						else{
							//PopUpinfo("b==false", "");//qaqa
							//getShell().dispose();
							status=maximum+1; //qaqa 6.6
						}
						
					}
					
					
					else{
						status=maximum+1;
						//return;
					}*/
					
				
					
					}
				});
				
			}
		}
		
		/*******/
		
		
		new Thread() {
			@Override
			public void run() {
				//here code to initilize
				if(type.compareTo("update")==0){
					Thread t = new Thread(new updateApp());
					t.start();
				}
				else{
					Thread t = new Thread(new initApp());
					t.start();	
				}
								
				//
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
								//System.out.println(status);
							
								bar.setSelection(status);
							}
							else{
								System.out.println("QAQA finished!!!");
								//status=maximum+1;
								if(checkConnection(bar_shell,zookini.getLeft())){//no problem in connection
									b = zookini.getRight();
									System.out.println(b);
									if(b){
										
										bar.setSelection(maximum);
										//status=maximum+1;
										if(type.compareTo("update")==0){
											PopUpinfo(bar_shell,"Db update", "Finished to update db.");//qaqa
										}
										else{
											PopUpinfo(bar_shell,"Db build", "Finished to build db.");//qaqa
										}
										
										//getShell().dispose();
										bar_shell.dispose();
										return;
										
									}
									else{
										
										if(type.compareTo("update")==0){
											PopUpinfo(bar_shell,"Db update", "Failed to update db.");//qaqa
										}
										else{
											PopUpinfo(bar_shell,"Db build", "Failed to build db.");//qaqa
										}
										
										//getShell().dispose();
										//status=maximum+1; //qaqa 6.6
										bar_shell.dispose();
										return;
									}
								}
								else{
									bar_shell.dispose();
									return;
								}
						
								
							}
						}
						}
					});
				}
			}
		}.start();
		
		getShell().setVisible(false);
		//getShell().open();
		bar_shell.open();
		
		while (!bar_shell.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	//	getDisplay().dispose();
	
	}



	@Override
	protected void disposeScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		
	}

}
	
	

