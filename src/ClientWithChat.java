// Java implementation for multithreaded chat client
// Save file as Client.java
 
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
 
public class ClientWithChat
{
    final static int ServerPort = 1234;
 
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
                        // write on the output stream
                        dos.writeUTF(msg);
                        // creating Date object
                        Date date = new Date();                
                        System.out.println("Message sent at " + fortime.format(date));
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
                        
                        Random rand = new Random();
                        int randomNum = rand.nextInt((max_delay - min_delay) + 1) + min_delay;
                        Thread.sleep(randomNum);
                        // creating Date object
                        Date date = new Date();
                        System.out.println("Message received at " + fortime.format(date));
                        
                        System.out.println(msg);
                    } catch (IOException | InterruptedException e) {
 
                        e.printStackTrace();
                    }
                }
            }
        });
 
        sendMessage.start();
        readMessage.start();
 
    }
}