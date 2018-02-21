// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
 
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
 
// Server class
public class ServerWithChat
{
 
    // Vector to store active clients
    static Vector<ClientHandlerChat> ar = new Vector<>();
     
    // counter for clients
    static int i = 0;
 
    public static void main(String[] args) throws IOException 
    {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);
         
        Socket s;
         
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            s = ss.accept();
 
            System.out.println("New client request received : " + s);
             
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             
            System.out.println("Creating a new handler for this client...");
 
            // Create a new handler object for handling this request.
            ClientHandlerChat mtch = new ClientHandlerChat(s,"Process " + i, dis, dos);
            
            dos.writeUTF("Initialize Process " + i);
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
 
            // add this client to active clients list
            ar.add(mtch);
 
            // start the thread.
            t.start();
 
            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
 
        }
    }
}
 
// ClientHandler class
class ClientHandlerChat implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandlerChat(Socket s, String name,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received;
       
        
        while (true) 
        {
            try
            {
                // receive the string
                received = dis.readUTF();
                                               
                System.out.println("Server receive message :" + received);
                 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                 
                // break the string into message and recipient part
//                StringTokenizer st = new StringTokenizer(received, "#");
//                String MsgToSend = st.nextToken();
//                String recipient = st.nextToken();
 
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                String[] strArr = received.split(";");
                String trueMessage = strArr[2];
                for (ClientHandlerChat mc : ServerWithChat.ar) 
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (mc.isloggedin==true) 
                    {
    
                        mc.dos.writeUTF(this.name+" : "+trueMessage);
//                        break;
                    }
                }
            } catch (IOException e) {
                 
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