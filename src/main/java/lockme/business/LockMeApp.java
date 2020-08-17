package lockme.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import lockme.data.Credential;
import lockme.data.User;

public class LockMeApp {

	private static File db; 
	private static JSONObject database;
	private static Scanner input;
	private static PrintWriter cruddb;
	private static User user;
	private static boolean loggedInStatus;
	
	public static void main(String[] args) {
		initApp();
		welcomeApp();
		loginScreen();
	}

	@SuppressWarnings("unchecked")
	static void signUp() {
		signUpBanner();
		System.out.print("Enter username : ");
		String username = input.nextLine();
		if (!checkUserExist(username)) {
			System.out.print("Enter password : ");
			String password = input.nextLine();
			JSONObject user = new JSONObject();
			JSONObject cred = new JSONObject();
			user.put("password", password);
			user.put("credentials", cred);
			database.put(username, user);
			updateDatabase();
			System.out.println("User is succesfully registered");
		}else {
			System.out.println("User already Exists.");
		}
		loginScreen();
	}

	static void loginScreen() {
		loginScreenOptions();
		int choose = 0;
		boolean correctInput=false;
		int retry=3;
		do {
			try {
				choose = Integer.parseInt(input.nextLine());
				correctInput = true;
			} catch (Exception e) {
				retry--;
				if(retry !=0) {
				System.out.println("Invalid Input.choose input from 1,2 or 3. retry left -->"+retry);
				}
			} 
		} while (!correctInput && retry!=0 && !(choose==1 || choose==2 || choose==3));
		
		switch(choose) {
		case 1:
			logIn();
			break;
		case 2:
			signUp();
			break;
		case 3:
			input.close();
			thankYouBanner();
			System.exit(0);
			break;
		default:
			System.out.println("Invalid Input 3 times, So exiting from App.");
		}	
	}


	static void logIn() {
		logInBanner();
		System.out.print("Enter username : ");
		String username = input.nextLine();
		System.out.print("Enter password : ");
		String password = input.nextLine();
		boolean userExistence = checkUserExist(username);
		if (userExistence) {
			boolean flag = validateLogin(username, password);
			if (flag) {
				user = new User();
				user.setUsername(username);
				successfulLogin();
			}else {
				System.out.println("password is not correct");
			} 
		}else {
			System.out.println("User does not exists. Please signup.");
			loginScreen();
		}
	}

	static boolean checkUserExist(String username) {
		return database.containsKey(username)? true:false;
	}

	static void successfulLogin() {
		if (!loggedInStatus) {
			successfullLoginBanner();
			
		}
		loggedInStatus=true;
		successfullLoginOptions();
		System.out.print("choose from above options : ");
		int choose = 0;
		boolean correctInput=false;
		int retry=3;
		do {
			try {
				choose = Integer.parseInt(input.nextLine());
				correctInput = true;
			} catch (Exception e) {
				retry--;
				if(retry !=0) {
				System.out.println("Invalid Input.choose input from 1,2,3,4,5 or 6. retry left -->"+retry);
				}
			} 
		} while (!correctInput && retry!=0 && !(choose==1 || choose==2 || choose==3
				 || choose==4 || choose==5 || choose==6));
		
		switch(choose) {
		case 1:
			storeCredentials();
			successfulLogin();
			break;
		case 2:
			fetchCredentials();
			successfulLogin();
			break;
		case 3:
			fetchSingleCredential();
			successfulLogin();
			break;
		case 4:
			deleteOrUpdateCredential(choose);
			successfulLogin();
			break;
		case 5:
			deleteOrUpdateCredential(choose);
			successfulLogin();
			break;
		case 6:
			input.close();
			thankYouBanner();
			System.exit(0);
			break;
		default:
			System.out.println("Invalid Input 3 times, So existing from App.");
		}
		
	}
		

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void deleteOrUpdateCredential(int task) {
		Map credentials = (Map)((JSONObject)database.get(user.getUsername())).get("credentials");
		if(credentials.isEmpty()) {
			System.out.println("No credentials exists for user  "+user.getUsername()+" .Please Store credentials");
			return;
		}else {
			JSONObject credJson = (JSONObject) ((JSONObject)database.get(user.getUsername())).get("credentials");
			Iterator<Map.Entry> itr = credentials.entrySet().iterator();
			List<Credential> list = new ArrayList<Credential>();
			List<Integer> index = new ArrayList<Integer>();
			int count = 1;
			while (itr.hasNext()) { 
				Map.Entry pair = itr.next(); 
				list.add(new Credential(pair.getKey().toString(),pair.getValue().toString()));
		    }
			Collections.sort(list,new CredentialComparator());
			System.out.println("\n*********************************************************");
			for(Credential c:list) {
				index.add(count);
				System.out.println(count+". "+c.getUrl());
				count++;
			}
			System.out.println("*********************************************************\n");
			count--;
			if (task==4) {
				System.out.print("Choose from above credentials to delete : ");
				int choose = 0;
				boolean correctInput=false;
				int retry=3;
				do {
					try {
						choose = Integer.parseInt(input.nextLine());
						if(index.contains(choose)) {
							correctInput = true;
						}else {
							throw new Exception();
						}
					} catch (Exception e) {
						retry--;
						if(retry !=0) {
						System.out.println("Invalid Input.choose input between 1 to " +(count)+" retry left -->"+retry);
						}
					} 
				} while (!correctInput && retry!=0 );
				if(correctInput && choose !=0) {
					Credential cred = list.get(choose - 1);
					String key = cred.getUrl();
					credJson.remove(key);
					updateDatabase();
					System.out.println("credential sucessfully deleted");
				}else {
					System.out.println("All try exhausted");
				}
				
			}else {
				System.out.print("Choose from above credentials to Update : ");
				int choose = 0;
				boolean correctInput=false;
				int retry=3;
				do {
					try {
						choose = Integer.parseInt(input.nextLine());
						if(index.contains(choose)) {
							correctInput = true;
						}else {
							throw new Exception();
						}
					} catch (Exception e) {
						retry--;
						if(retry !=0) {
						System.out.println("Invalid Input.choose input between 1 to " +(count)+" retry left -->"+retry);
						}
					} 
				} while (!correctInput && retry!=0 );
				if (correctInput && choose!=0) {
					Credential cred = list.get(choose - 1);
					String key = cred.getUrl();
					System.out.print("Enter new password for update : ");
					String newPass = input.nextLine();
					credJson.put(key, newPass);
					updateDatabase();
					System.out.println("Credential successfully updated.");
				}else {
					System.out.println("All try exhausted");
				}
				
			}
			
		}
	}

	static void fetchSingleCredential() {
		
		@SuppressWarnings("rawtypes")
		Map credentials = (Map)((JSONObject)database.get(user.getUsername())).get("credentials");
		if(credentials.isEmpty()) {
			System.out.println("No credentials exists for user  "+user.getUsername()+" .Please Store credentials");
			return;
		}
        @SuppressWarnings({ "unchecked", "rawtypes" })
		Iterator<Map.Entry> itr = credentials.entrySet().iterator();
        List<Credential> list = new ArrayList<Credential>();
        List<Integer> index = new ArrayList<Integer>();
        int count = 1;
        while (itr.hasNext()) { 
            @SuppressWarnings("rawtypes")
			Map.Entry pair = itr.next(); 
            list.add(new Credential(pair.getKey().toString(),pair.getValue().toString()));
        }
        Collections.sort(list,new CredentialComparator());
        System.out.println("\n*********************************************************");
        for(Credential c:list) {
			index.add(count);
			System.out.println(count+". "+c.getUrl());
			count++;
			
		}
        System.out.println("*********************************************************\n");
		count--;
        System.out.print("Choose from above options : ");
        
        int choose = 0; 
        boolean correctInput=false;
		int retry=3;
		do {
			try {
				choose = Integer.parseInt(input.nextLine());
				if(index.contains(choose)) {
					correctInput = true;
				}else {
					throw new Exception();
				}
			} catch (Exception e) {
				retry--;
				if(retry !=0) {
				System.out.println("Invalid Input.choose input between 1 to " +(count)+" retry left -->"+retry);
				}
			} 
		} while (!correctInput && retry!=0 );
		if(correctInput &&choose!=0) {
			System.out.println("\nYour credential for "+(list.get(choose-1)).getUrl()+" is "+(list.get(choose-1)).getPassword()+"\n");
		}else {
			System.out.println("you retry exhausted");
		}
	}

	@SuppressWarnings("unchecked")
	static void storeCredentials() {
		 JSONObject credJson=null;
		 System.out.print("Enter url : ");
		 String url = input.nextLine();
		 System.out.print("Enter url password : ");
		 String password = input.nextLine();
		 try {
			credJson = (JSONObject) ((JSONObject)database.get(user.getUsername())).get("credentials");
			credJson.put(url, password);
		 } catch (Exception e) {
			credJson = new JSONObject();
			credJson.put(url, password);
			((JSONObject)database.get(user.getUsername())).put("credentials",credJson);
		}
		updateDatabase();	
		System.out.println("Credentials are  succesfully stored\n");
	}

	static void fetchCredentials() {
		@SuppressWarnings("rawtypes")
		Map credentials = (Map)((JSONObject)database.get(user.getUsername())).get("credentials");
		if(credentials.isEmpty()) {
			System.out.println("No credentials exists for user  "+user.getUsername()+" .Please Store credentials");
			return;
		}
		
        @SuppressWarnings({ "unchecked", "rawtypes" })
		Iterator<Map.Entry> itr = credentials.entrySet().iterator();
        List<Credential> list = new ArrayList<Credential>();
        int count=1;
        System.out.println("\nBelow are your credentials \n");
        System.out.println("\n*********************************************************");
        while (itr.hasNext()) { 
            @SuppressWarnings("rawtypes")
			Map.Entry pair = itr.next(); 
            list.add(new Credential(pair.getKey().toString(),pair.getValue().toString()));
        } 
        Collections.sort(list,new CredentialComparator());
        for(Credential c:list) {
        	System.out.println(count+". "+c.getUrl()+"  <<=====>>  "+c.getPassword());
        	count++;
        }
        System.out.println("*********************************************************\n");
        
	}

	static boolean validateLogin(String username, String password) {
			String pass = (String)((JSONObject)database.get(username)).get("password");
			return password.equals(pass)?true:false;		
		}
		
	

	static void initApp() {
		db = new File("database.json");
		input = new Scanner(System.in);
		loggedInStatus = false;
		try {
			if(db.exists()) {
				if(db.length()!=0) {
					/**
					 * if database.json file exists and is not empty then 
					 * read from file and create database json object from it.
					 */
					JSONParser parser = new JSONParser();
					Object obj = parser.parse(new FileReader(db));	
				    database = (JSONObject) obj;
				}else {
					/**
					 * if file database.json exists and  is empty 
					 * then database object is created from here
					 */
					database = new JSONObject();
				}
			}else {
				/**
				 * if file database.json does not exists , then file is first created
				 * then database json object is created
				 */
				db.createNewFile();
				database = new JSONObject();
			}
		
		}catch(IOException e) {
			System.out.println("Exception while creating a file.");
		}catch (ParseException e) {
			System.out.println("Exception  not proper content format");
		}
	}
	
	static void updateDatabase() {
		 try {
				cruddb = new PrintWriter(db);
				cruddb.write(database.toString());
				cruddb.flush();
				cruddb.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Exception occur while updating database.");
			}
	}
	static void loginScreenOptions() {
		System.out.println("1. LogIn");
		System.out.println("2. SignUp");
		System.out.println("3. Exit");
		System.out.print("Please Choose from above options : ");
	}
	static void successfullLoginOptions() {
		System.out.println("1. Store credentials    \n");
		System.out.println("2. Fetch All credentials\n");
		System.out.println("3. Fetch  1 Credential  \n");
		System.out.println("4. Delete a credential  \n");
		System.out.println("5. Update a credential  \n");
		System.out.println("6. Exit App.\n");
	}
	static void welcomeApp() {
		System.out.println("*************************************************");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*      WELCOME TO LOCK ME APP VERSION 0.0.1     *");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*************************************************");
	}
	static void signUpBanner() {
		System.out.println("*************************************************");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*           WELCOME TO SIGN UP PAGE             *");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*************************************************");
	}
	static void logInBanner() {
		System.out.println("*************************************************");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*           WELCOME TO LOGIN PAGE               *");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*************************************************");
	}
	static void successfullLoginBanner() {
		System.out.println("*************************************************");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*           LOGIN SUCCESSFULLY!!                *");
		System.out.println("*         YOUR CRED's ARE SAFE WITH US          *");
		System.out.println("*                                               *");
		System.out.println("*************************************************");
	}
	static void thankYouBanner() {
		System.out.println("*************************************************");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*           LOGOUT SUCCESSFULLY!!               *");
		System.out.println("*         THANK YOU!! TO SAVE CRED's WITH US    *");
		System.out.println("*          HOPE YOU VISIT US AGAIN!!            *");
		System.out.println("*                                               *");
		System.out.println("*                                               *");
		System.out.println("*************************************************");
	}



}
