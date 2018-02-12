import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file = new File("configuration.txt");
		 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			  while ((st = br.readLine()) != null) {
				  String[] arr = st.split("\t");
				  Node node = new Node(Integer.parseInt(arr[0]), arr[1], Integer.parseInt(arr[2]));	 
				  
			  }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		Server server = new Server(5000);
		
		Client client = new Client("127.0.0.1", 5000);
		
	}

}
