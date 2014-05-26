/**
 * 
 */
package swt;

/**
 * @author Nadav
 *
 */
public interface Application {
	
	/**
	 * 
	 * @param username - string representing the username
	 * @param password - string representing the password
	 * @return true iff its an authentic user
	 */
	public boolean isUserRegisterd(String username,String password);
	
	/**
	 * 
	 * @param username
	 * @return true iff username is already in use
	 */
	public boolean isUsernameTaken(String username);
	
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return true iff signing up the user succeeded
	 */
	public boolean signUpUser(String username, String password);
	
	
	/**
	 * 
	 * @param username
	 * @return the status song of the user
	 */
	public String getStatusSong(String username);
	
	/**
	 * 
	 * @param song
	 * @return
	 */
	
	public boolean changeStatusSong(String song); //qaqa

}
