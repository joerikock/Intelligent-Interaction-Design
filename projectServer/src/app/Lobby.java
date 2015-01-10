package app;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Lobby {
	private HashMap<Integer, Player> players;
	private String code;
	private int idCount = 0;
	private HashMap<Integer, Integer> targets;
	private HashMap<Integer, Integer> score;
	private int hintCount = 0;
	private HashMap<InetAddress, Integer> ipAddresses;
	
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
	
	public int getTarget(int playerId){
		return targets.get(playerId);
	}
	
	public void killTarget(int playerId){
		int target = targets.get(playerId);
		int newTarget = targets.get(targets);
		targets.remove(target);
		targets.put(playerId, newTarget);
		score.put(playerId, score.get(playerId)+1);
	}
	public HashMap<Integer, Player> getPlayers(){
		return players;
	}
	
	public void addPlayer(String name, String surname, InetAddress ipAddress){
		players.put(idCount, new Player(name, surname));
		ipAddresses.put(ipAddress, idCount);
		score.put(idCount, 0);
		idCount++;
	}
	
	public void addPlayer(String name, String surname, ArrayList<Hobby> hobbies, InetAddress ipAddress){
		players.put(idCount, new Player(name, surname, hobbies));
		ipAddresses.put(ipAddress, idCount);
		score.put(idCount, 0);
		idCount++;	
	}
	
	public String getHint(int playerId){
		int target = targets.get(playerId);
		ArrayList<Hobby> hobbies = players.get(target).getHobbies();
		Hobby hobby = hobbies.get(hintCount);
		if(hobby == Hobby.WATERSPORT){
			return "your target likes to play water sports";
		}
		if(hobby == Hobby.FIELDSPORT){
			return "your target likes to play field sports";
		}
		if(hobby == Hobby.EXTREMESPORT){
			return "your target likes to do extreme sports";
		}
		if(hobby == Hobby.SOLOSPORT){
			return "your target likes to do a solo sport";
		}
		if(hobby == Hobby.WINTERSPORT){
			return "your target enjoys winter sports";
		}
		if(hobby == Hobby.BOARDGAMES){
			return "your target likes playing boardgames";
		}
		if(hobby == Hobby.VIDEOGAMES){
			return "your target like playing videogames";
		}
		if(hobby == Hobby.MUSICALINSTRUMENTS){
			return "your target plays a musical instrument";
		}
		if(hobby == Hobby.WATCHINGMOVIESSERIES){
			return "your target likes watching movies/series";
		}
		if(hobby == Hobby.GOINGOUT){
			return "your target likes going out";
		}
		else{
			return null;
		}
	}
	
	public void doHints(){
		for(int i = 0; i < players.size(); i++){
			String message = getHint(i);
			//ToDo sent message here
		}
		hintCount++;
		if(hintCount == 4){
			hintCount = 0;
		}
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
}
