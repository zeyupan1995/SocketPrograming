// Java implementation for multithreaded chat client
// Save file as Client.java
 
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
 
public class ClientWithChat
{
    final static int ServerPort = 1234;
    static int id = -1;
    static ArrayList<Integer> localClock = new ArrayList<Integer>();
    static Queue<String> holdBackQueue = new LinkedList<String>();
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
    	    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
        
        Scanner scn = new Scanner(System.in);
         
        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");
         
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
         
        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        
        
        
        
        
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
 
                    // read the message to deliver.
                    String msg = scn.nextLine();
                     
                    try {
                    		localClock.set(id, localClock.get(id) + 1);
                    		String localClockStr = clockToString();
                        // write on the output stream
                    		msg = "Process " + id + ";" + localClockStr + ";" + msg;
                        dos.writeUTF(msg);
                        // creating Date object
                        Date date = new Date();                
                        System.out.println("Process " + id + " sent message at " + fortime.format(date));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         
        // readMessage thread
        Thread readMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
            		
            	 	int max_delay = 3000, min_delay = 1000;
            	 	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
            	 	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
                while (true) {
                    try {
                        // read the message sent to this client
                    		
                        String msg = dis.readUTF();
                        
                        if (msg.startsWith("Initialize Process")) {                        	
                        		id = Character.getNumericValue(msg.charAt(msg.length() - 1));
                        		System.out.println("##### Process " + id + " Chat Room #####");
                        		for (int i = 0; i < 10; i++) {
                        			localClock.add(0);
                        		}
                        		continue;
                        }
                        if (msg.startsWith("Process " + id)) {
                        		continue;
                        }
                        
                        
                        String[] strArr = msg.split(";");
                        String senderId = strArr[0];
                        String senderClockStr = strArr[1];
                        String senderMsg = strArr[2];
                        ArrayList<Integer> senderClock = getSenderClock(senderClockStr);
                        
                        boolean accept = isAcceptable(localClock, senderClock, msg);
                        boolean buffer = bufferable(localClock, senderClock);
                        boolean reject = rejectable(localClock, senderClock);
                        /*
                         * Each process keeps a sequence number for each other process (vector) v 
                         * When a message is received, 
                         * 		as expected (next sequence), accept 
                         * 		higher than expected, buffer in a queue 
                         * 		lower than expected, reject
                         * 
                         * If send(m1) → send(m2), then every recipient of both message m1 and m2 
                         * must “deliver” m1 before m2.
                         */
                        
                        if (accept) {
                        	   localClock.set(id, localClock.get(id) + 1);
	                        Random rand = new Random();
	                        int randomNum = rand.nextInt((max_delay - min_delay) + 1) + min_delay;
	                        Thread.sleep(randomNum);
	                        // creating Date object
	                        Date date = new Date();
	                        System.out.println("--------------");
	                        System.out.println("Message received from " + senderId);
	                        System.out.println(senderMsg);
	                        System.out.println(fortime.format(date));
	                        System.out.println("--------------");
                        } else if (buffer) {
                        		holdBackQueue.add(msg);
                        } else if (reject) {
                        		// do nothing
                        }
      
                    } catch (IOException | InterruptedException e) {
 
                        e.printStackTrace();
                    }
                }
            }
        });
 
        sendMessage.start();
        readMessage.start();
 
    }
    public static String clockToString() {
    		String s = "";
    		for (int i = 0; i < localClock.size(); i++) {
    			s += String.valueOf(localClock.get(i));
    		}
    		return s;
    }
    
    public static ArrayList<Integer> getSenderClock(String senderClockStr) {
    		ArrayList<Integer> rst = new ArrayList<Integer>();
    		for (int i = 0; i < senderClockStr.length(); i++) {
    			rst.add(Character.getNumericValue(senderClockStr.charAt(i)));
    		}
    		return rst;
    }
    
    public static boolean isAcceptable(ArrayList<Integer> local, ArrayList<Integer> sender, String msg) {
    		if (local.get(id) + 1 == sender.get(id)) {
    			for (int i = 0; i < local.size(); i++) {
    				if (i == id)
    					continue;
    				if (sender.get(i) != local.get(i))
    					return false;
    			}
    		}
    		
    		return true;
    }
    public static boolean bufferable(ArrayList<Integer> local, ArrayList<Integer> sender) {
    		for (int i = 0; i < local.size(); i++) {
    			if (local.get(i) + 1 > sender.get(i))
    				return false;
    		}    		
    		return true;
    }
    public static boolean rejectable(ArrayList<Integer> local, ArrayList<Integer> sender) {
	    	for (int i = 0; i < local.size(); i++) {
			if (local.get(i) + 1 > sender.get(i))
				return true;
		}  
	    	return false;
    }

    
}