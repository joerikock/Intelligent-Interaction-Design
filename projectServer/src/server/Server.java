package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

import app.Lobby;

/**
 * This is a simple server application. This server receive a string message
 * from the Android mobile phone and show it on the console.
 * Author by Lak J Comspaceddd
 */
public class Server {

	private static ServerSocket serverSocket;
	private static String message;
	private static int lobbyCount;
	private static int killCount;
	private static int killsPerHint = 3;
	private static HashMap<InetAddress, String> addresses = new HashMap<InetAddress, String>();
	private static HashMap<String, Lobby> lobbyCodes = new HashMap<String, Lobby>();
	private static HashMap<String, Lobby> lobbyNames = new HashMap<String, Lobby>();
	private static HashMap<Integer, Connection> connections = new HashMap<Integer, Connection>();

	public static String doAction(String message) {
		if (message.startsWith("1")) {
			//lobby
			return handleLobby(message.substring(1));
		} else if (message.startsWith("2")) {
			//player
			return handlePlayer(message.substring(1));
		} else if (message.startsWith("3")) {
			//start
			return handleStart(message.substring(1));
		} else if (message.startsWith("4")) {
			//kill
			return handleKill(message.substring(1));
		} else if (message.startsWith("5")) {
			//kill confirmed
			return handleKillConfirmed(message.substring(1));
		} else {
			return null;
		}
	}

	public static String handleLobby(String message) {

		String[] info = message.split(", ");
		for(int i = 0; i < info.length; i++ ){
			System.out.println(info[i]);
		}
		String name = info[0] + "#" + lobbyCount;
		try {
			InetAddress ip = InetAddress.getByName(info[1]);
			Lobby lobby = new Lobby(name);
			lobbyCount++;
			String code = lobby.getCode();
			lobbyCodes.put(code, lobby);
			return "1" + name + ", " + code;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return null;
	}

	public static String handlePlayer(String message) {
		String[] info = message.split(", ");
		String code = info[0];
		String name = info[1];
		String surname = info[2];
		int id;
		if(message.contains("[")){
			System.out.println(message);
			String[] meh = message.split("\\[");
			System.out.println(Arrays.toString(meh));
			String hobbies = meh[1];
			System.out.println(hobbies);
			hobbies = hobbies.replace("]", "");
			System.out.println(hobbies);
			String[] hobbyArray = hobbies.split(", ");
			Lobby lobby = lobbyCodes.get(code);
			id = lobby.addPlayer(name, surname, hobbyArray, code);
		} else{
			Lobby lobby = lobbyCodes.get(code);
			id = lobby.addPlayer(name, surname, code);
		}
		return "2" + id;
	}



	public static String handleStart(String message) {
		//send start message to all players
		String code = message.substring(1);
		Lobby lobby = lobbyCodes.get(code);
		lobby.defineTargets();
		return "3"+code;
	}

	public static String handleKill(String message) {
		String[] info = message.substring(1).split(", ");
		int player = Integer.parseInt(info[0]);
		Lobby lobby = lobbyCodes.get(info[1]);
		int target = lobby.getTarget(player);
		int id = Integer.parseInt(message.substring(1));
		killCount++;
		if(killCount > killsPerHint) {
			for(int i = 0; i < connections.size(); i++){
				connections.get(i).sendMessage("6" + lobby.getHint(player));
			}
			lobby.doHints();
		}

		//send confirmation request to target
		return "4" + id + ", " + lobby.getCode();
	}

	public static String handleKillConfirmed(String message) {
		String[] info = message.substring(1).split(", ");
		int target = Integer.parseInt(info[0]);
		Lobby lobby = lobbyCodes.get(info[1]);
		int killer = lobby.getKiller(target);
		lobby.killTarget(killer);
		Connection connection = connections.get(target);
		connection.sendMessage("7" + lobby.getScore(target));
		connections.remove(target);
		connection.close();
		//send confirmation to killer
		return "5" + killer + ", " + lobby.getCode();
	} 

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Server socket
		while(true){
			Connection connection = new Connection();
			connection.start();
			message = doAction(connection.receiveMessage());
			System.out.println("message received: " + message);
			if(message.startsWith("1")) {
				connection.sendMessage(message);
				connection.close();
			}
			else if(message.startsWith("2")) {
				connections.put(Integer.parseInt(message.substring(1)), connection);
				connection.sendMessage(message);
			}
			else if(message.startsWith("3")) {
				connection.sendMessage(message);
				connection.close();
				String code = message.substring(1);
				Lobby lobby = lobbyCodes.get(code);
				for(int i = 0; i < connections.size(); i++){
					connections.get(i).sendMessage("3" + lobby.getPlayerName(lobby.getTarget(i)));
				}
			}
			else if(message.startsWith("4")) {
				String[] info = message.substring(1).split(", ");
				int id = Integer.parseInt(info[0]);
				String code = info[1];
				Lobby lobby = lobbyCodes.get(code);
				int target = lobby.getTarget(id);
				connections.get(target).sendMessage("4");
				connection.close();
			}
			else if(message.startsWith("5")) {
				String[] info = message.substring(1).split(", ");
				int id = Integer.parseInt(info[0]);
				String code = info[1];
				Lobby lobby = lobbyCodes.get(code);
				int killer = lobby.getKiller(id);
				connections.get(killer).sendMessage("5" + lobby.getPlayerName(lobby.getTarget(killer)));
				connection.close();
			}
		}
	}
}