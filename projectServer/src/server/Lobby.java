package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Lobby {
	private HashMap<Integer, Player> players;
	private String name;
	private String code;
	private int idCount = 0;
	private HashMap<Integer, Integer> targets;
	private HashMap<Integer, Integer> score;
	
	public Lobby() {
		StringBuilder sb = new StringBuilder();
		String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i= 0; i < 10; i++) {
		    Random r = new Random();
		    int k = r.nextInt(set.length()-1);
		    sb.append(set.charAt(k));
		}
		code = sb.toString();
	}
	
	public void defineTargets(){
		ArrayList<Integer> idArrayList = new ArrayList<Integer>();
		idArrayList.addAll(players.keySet());
		Collections.shuffle(idArrayList);
		for(int i = 0; i < idArrayList.size(); i++){
			targets.put(idArrayList.get(i), idArrayList.get(i + 1));
		}
		targets.put(idArrayList.get(idArrayList.size()-1), idArrayList.get(0));
	}
	
	public HashMap<Integer, Integer> getTargets(){
		return targets;
	}
	
	public int getTarget(int player){
		return targets.get(player);
	}
	
	public void killPlayer(int target){
		
	}
	public HashMap<Integer, Player> getPlayers(){
		return players;
	}
	
	public void addPlayer(String name, String surname, ArrayList<Hobby> hobbies){
		players.put(idCount, new Player(name, surname, hobbies));
		idCount++;	
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
}
