package lockme.business;

import java.util.Comparator;

import lockme.data.Credential;

public class CredentialComparator implements Comparator<Credential>{
	
	public int compare(Credential o1, Credential o2) {
		return (o1.getUrl()).compareTo(o2.getUrl());
	}
	
}
