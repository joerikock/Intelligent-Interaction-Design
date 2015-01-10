package app;

import java.net.InetAddress;
import java.util.ArrayList;

public class Player {
	private String name;
	private String surname;
	private ArrayList<Hobby> hobbies;
	private InetAddress ipAddress;
	
	public Player (String name, String surname, ArrayList<Hobby> hobbies) {
		this.name = name;
		this.surname = surname;
		this.hobbies = hobbies;
	}
	
	public Player (String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Hobby> getHobbies() {
		return hobbies;
	}
	public void setHobbies(ArrayList<Hobby> hobbies) {
		this.hobbies = hobbies;
	}
	
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
}
