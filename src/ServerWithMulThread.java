// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
 
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;


// Server class
public class ServerWithMulThread
{
    public static void main(String[] args) throws IOException 
    {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);
         
        // running infinite loop for getting
        // client request
        
        
        
        while (true) 
        {
            Socket s = null;
             
            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();
                 
                System.out.println("A new client is connected : " + s);
                 
                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                 
                System.out.println("Assigning new thread for this client");
 
                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);
 
                // Invoking the start() method
                t.start();
                 
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
 
// ClientHandler class
class ClientHandler extends Thread 
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
     
    int min_delay = 1000;
    int max_delay = 3000; // ms
    
    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }
 
    @Override
    public void run() 
    {
        String received;
        String toreturn;
        while (true) 
        {
            try {
 
                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n"+
                            "Type Exit to terminate connection.");
                 
                // receive the answer from client
                received = dis.readUTF();          
                
                if(received.equals("Exit"))
                { 
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                 
                
                
                Random rand = new Random();
                int randomNum = rand.nextInt((max_delay - min_delay) + 1) + min_delay;
                Thread.sleep(randomNum);
                // creating Date object
                Date date = new Date();
                System.out.println("Message received at " + fortime.format(date));
                
//                dos.writeUTF("Messaged received at: " + fortime.format(date));
                
                // write on output stream based on the
                // answer from the client
                switch (received) {
                 
                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;
                         
                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;
                         
                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
         
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}