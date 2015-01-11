package app;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Lobby {
	private String name;
	private HashMap<Integer, Player> players;
	private String code;
	private int idCount = 0;
	private HashMap<Integer, Integer> targets;
	private HashMap<Integer, Integer> killers;
	private HashMap<Integer, Integer> score;
	private int hintCount = 0;
	private HashMap<InetAddress, Integer> ipAddresses;
	private HashMap<Integer, InetAddress> ipReversed;
	
	public Lobby(String name) {
		this.name = name;
		StringBuilder sb = new StringBuilder();
		String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i= 0; i < 10; i++) {
		    Random r = new Random();
		    int k = r.nextInt(set.length()-1);
		    sb.append(set.charAt(k));
		}
		code = sb.toString();
	}
	
	public int getIdFromIp(InetAddress Ip) {
		return ipAddresses.get(Ip);
	}
	
	public InetAddress getIpFromId(int id) {
		return ipReversed.get(id);
	}
	public String getName() {
		return name;
	}
	public void defineTargets(){
		ArrayList<Integer> idArrayList = new ArrayList<Integer>();
		idArrayList.addAll(players.keySet());
		Collections.shuffle(idArrayList);
		for(int i = 0; i < idArrayList.size(); i++){
			targets.put(idArrayList.get(i), idArrayList.get(i + 1));
			killers.put(idArrayList.get(i + 1), idArrayList.get(i));
		}
		targets.put(idArrayList.get(idArrayList.size()-1), idArrayList.get(0));
		killers.put(idArrayList.get(0), idArrayList.get(idArrayList.size()-1));
	}
	
	public HashMap<Integer, Integer> getTargets(){
		return targets;
	}
	
	public int getTarget(int playerId){
		return targets.get(playerId);
	}
	
	public int getKiller(int target){
		return killers.get(target);
	}
	
	public void killTarget(int playerId){
		int target = targets.get(playerId);
		int newTarget = targets.get(targets);
		targets.remove(target);
		killers.remove(target);
		targets.put(playerId, newTarget);
		killers.put(newTarget, playerId);
		score.put(playerId, score.get(playerId)+1);
	}
	public HashMap<Integer, Player> getPlayers(){
		return players;
	}
	
	public void addPlayer(String name, String surname, InetAddress ipAddress, String code){
		players.put(idCount, new Player(name, surname, code));
		ipAddresses.put(ipAddress, idCount);
		ipReversed.put(idCount, ipAddress);
		score.put(idCount, 0);
		idCount++;
	}
	
	public void addPlayer(String name, String surname, String[] hobbies, InetAddress ipAddress, String code){
		players.put(idCount, new Player(name, surname, hobbies, code));
		ipAddresses.put(ipAddress, idCount);
		ipReversed.put(idCount, ipAddress);
		score.put(idCount, 0);
		idCount++;	
	}
	
	public String getHint(int playerId){
		int target = targets.get(playerId);
		String[] hobbies = players.get(target).getHobbies();
		String hint = hobbies[hintCount];
		return hint;
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
