package lockme.data;

import java.util.List;

public class User {

	private String username;
	private String password;
	private List<Credential> credentialsList;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param credentialsList
	 */
	public User(String username, String password, List<Credential> credentialsList) {
		super();
		this.username = username;
		this.password = password;
		this.credentialsList = credentialsList;
	}
	
	public String getUsername() {
		return username;
	}
	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Credential> getCredentialsList() {
		return credentialsList;
	}
	/**
	 * 
	 * @param credentialsList
	 */
	public void setCredentialsList(List<Credential> credentialsList) {
		this.credentialsList = credentialsList;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", credentialsList=" + credentialsList + "]";
	}
	
	
}
