import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

public class Message implements Serializable{
	Socket s;
	HashMap<Socket, Integer> tsMap;
	String msg;
	public Message(Socket s, HashMap<Socket, Integer> tsMap, String msg) {
		this.s = s;
		this.tsMap = tsMap;
		this.msg = msg;
	}
}
