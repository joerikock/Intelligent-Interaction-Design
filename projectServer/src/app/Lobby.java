package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Lobby {
	private String name;
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private String code;
	private int idCount = 0;
	private HashMap<Integer, Integer> targets = new HashMap<Integer, Integer>();;
	private HashMap<Integer, Integer> killers = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> score = new HashMap<Integer, Integer>();
	private int hintCount = 0;

	public Lobby(String name) {
		this.name = name;
		StringBuilder sb = new StringBuilder();
		String set = "abcdefghijklmnopqrstuvwxyzZ1234567890";

		for (int i= 0; i < 3; i++) {
		    Random r = new Random();
		    int k = r.nextInt(set.length()-1);
		    sb.append(set.charAt(k));
		}
		code = sb.toString();
	}
	
	public String getPlayerName(int id) {
		return players.get(id).getName();
	}
	public int getScore(int player) {
		return score.get(player);
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
	
	public int addPlayer(String name, String surname, String code){
		players.put(idCount, new Player(name, surname, code));
		score.put(idCount, 0);
		idCount++;
		return idCount;
	}
	
	public int addPlayer(String name, String surname, String[] hobbies, String code){
		players.put(idCount, new Player(name, surname, hobbies, code));
		score.put(idCount, 0);
		idCount++;	
		return idCount;
	}
	
	public String getHint(int playerId){
		int target = targets.get(playerId);
		String[] hobbies = players.get(target).getHobbies();
		String hint = hobbies[hintCount];
		return hint;
	}
	
	public void doHints(){
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
