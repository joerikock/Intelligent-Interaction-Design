package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * This is a simple server application. This server receive a string message
 * from the Android mobile phone and show it on the console.
 * Author by Lak J Comspaceddd
 */
public class Server {
	
	private static Socket client;
	private static PrintWriter printwriter;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;
	private static int lobbyCount;
	private static HashMap<InetAddress, String> addresses = new HashMap<InetAddress, String>();
	private static HashMap<String, app.Lobby> lobbyCodes = new HashMap<String, app.Lobby>();
	private static HashMap<String, app.Lobby> lobbyNames = new HashMap<String, app.Lobby>();

	public static void doAction(String message) {
		if (message.startsWith("1")) {
			//lobby
			handleLobby(message.substring(1));
		} else if (message.startsWith("2")) {
			//player
			handlePlayer(message.substring(1));
		} else if (message.startsWith("3")) {
			//start
			handleStart(message.substring(1));
		} else if (message.startsWith("4")) {
			//kill
			handleKill(message.substring(1));
		} else if (message.startsWith("5")) {
			//kill confirmed
			handleKillConfirmed(message.substring(1));
		} else {

		}
	}

	public static void handleLobby(String message) {
		String[] info = message.split(", ");
		String name = info[0] + "#" + lobbyCount;
		try {
			InetAddress ip = InetAddress.getByName(info[1]);
			app.Lobby lobby = new app.Lobby(name);
			lobbyCount++;
			String code = lobby.getCode();
			lobbyCodes.put(code, lobby);
			sendMessage("1" + code, ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void handlePlayer(String message) {
		String[] info = message.split(", ");
		String code = info[0];
		String name = info[1];
		String surname = info[2];
		InetAddress ipAddress;
		try {
			ipAddress = InetAddress.getByName(info[3]);
			if(message.contains("[")){
				String hobbies = message.split("[")[1];
				hobbies.replace("]", "");
				String[] hobbyArray = hobbies.split(", ");
				app.Lobby lobby = lobbyCodes.get(code);
				lobby.addPlayer(name, surname, hobbyArray, ipAddress, code);
				addresses.put(ipAddress, code);
			} else{
				app.Lobby lobby = lobbyCodes.get(code);
				lobby.addPlayer(name, surname, ipAddress, code);
				addresses.put(ipAddress, code);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void handleStart(String message) {
		//send start message to all players
		app.Lobby lobby = lobbyNames.get(message);
		for(int i = 0; i < lobby.getPlayers().size(); i++ ) {
			sendMessage("3", lobby.getIpFromId(i));
		}
	}

	public static void handleKill(String message) {
		try {
			InetAddress playerIp = InetAddress.getByName(message);
			app.Lobby lobby = lobbyCodes.get(addresses.get(playerIp));
			int player = lobby.getIdFromIp(playerIp);
			int target = lobby.getTarget(player);
			InetAddress targetIp = lobby.getIpFromId(target);
			//send confirmation request to target
			sendMessage("4", targetIp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void handleKillConfirmed(String message) {
		try {
			InetAddress targetIp = InetAddress.getByName(message);
			app.Lobby lobby = lobbyCodes.get(addresses.get(targetIp));
			int target = lobby.getIdFromIp(targetIp);
			int killer = lobby.getKiller(target);
			InetAddress killerIp = lobby.getIpFromId(killer);
			//send confirmation to killer
			sendMessage("5", killerIp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String message, InetAddress ip){
		try {

            client = new Socket(ip, 4445); // connect to the server
            printwriter = new PrintWriter(client.getOutputStream(), true);
            printwriter.write(message); // write the message to output stream
            printwriter.flush();
            printwriter.close();
            client.close(); // closing the connection

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(4444); // Server socket
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}
		System.out.println("Server started. Listening to the port 4444");

		while (true) {
			try {
				clientSocket = serverSocket.accept(); // accept the client connection
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); // get the client message
				message = bufferedReader.readLine();
				System.out.println(message);
				doAction(message);
				inputStreamReader.close();
				clientSocket.close();
			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}
	}
}