package server;

public class DataHandler {
	
	public String data;
	public enum Type {LOBBY, PLAYER, START, KILL, DEATH};
	
	public void setMessage(String message) {
		data = message;
	}
	
	public Type determineType() {
		if (data.startsWith("1")) {
			return Type.LOBBY;
		} else if (data.startsWith("2")) {
			return Type.PLAYER;
		} else if (data.startsWith("3")) {
			return Type.START;
		} else if (data.startsWith("4")) {
			return Type.KILL;
		} else if (data.startsWith("5")) {
			return Type.DEATH;
		} else {
			return null;
		}
	}
	
	public static void main (String[] args) {
		DataHandler dh = new DataHandler();
		dh.setMessage("4code");
		System.out.println(dh.determineType());
	}
}
