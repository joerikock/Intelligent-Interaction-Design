package app;

import java.net.InetAddress;
import java.util.ArrayList;

public class Player {
	private String name;
	private String surname;
	private String[] hobbies;
	private InetAddress ipAddress;
	private String code;
	
	public Player (String name, String surname, String[] hobbies, String code) {
		this.code = code;
		this.name = name;
		this.surname = surname;
		this.hobbies = hobbies;
	}
	
	public Player (String name, String surname, String code) {
		this.code = code;
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
	
	public String[] getHobbies() {
		return hobbies;
	}
	public void setHobbies(String[] hobbies) {
		this.hobbies = hobbies;
	}
	
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
}
