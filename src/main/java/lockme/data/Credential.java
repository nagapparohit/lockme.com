package lockme.data;

public class Credential {
	
	private String url;
	private String password;
	
	public Credential() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * 
	 * @param url
	 * @param password
	 */
	public Credential(String url, String password) {
		super();
		this.url = url;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}
	/**
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
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
	@Override
	public String toString() {
		return "Credential [url=" + url + ", password=" + password + "]";
	}
	
	
	
	
}
