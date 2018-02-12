// Java implementation for a client
// Save file as Client.java
 
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
 
// Client class
public class ClientWithMulThread
{
    public static void main(String[] args) throws IOException 
    {
    		DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    		int min_delay = 1000;
        int max_delay = 3000; // ms
        
        try
        {
            Scanner scn = new Scanner(System.in);
             
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");
     
            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);
     
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
     
            // the following loop performs the exchange of
            // information between client and client handler
            while (true) 
            {
                System.out.println(dis.readUTF());
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);
                 
                // creating Date object
                Date date = new Date();                
                System.out.println("Message sent at " + fortime.format(date));
                
                // If client sends exit,close this connection 
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
                String received = dis.readUTF();
                System.out.println(received);
            }
             
            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}