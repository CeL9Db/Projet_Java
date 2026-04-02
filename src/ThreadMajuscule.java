import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ThreadMajuscule extends Thread{
    //private ServerSocket server;
    private Socket socket_u = null;	

    public ThreadMajuscule(Socket socket)throws IOException, UnknownHostException{
        this.socket_u = socket; // socket du client
        //this.socket = new Socket(PORT);

    }

    public void run() {
		try {
			//this.socket = server.accept();
			System.out.println("Serveur: connexion etablie avec le client " + this.socket_u.getInetAddress());
			String msg;
			do {
				msg = readMessage();
				if(msg != null){
					System.out.println("Message  : " + msg + "\n");
					sendMessage(msg.toUpperCase()); // renvoie le message en maj
				}
				
			} while (!msg.equalsIgnoreCase("exit"));
			System.out.println("connection rompue avec le client " + this.socket_u.getInetAddress());
			this.socket_u.close();
		} catch (IOException e) {
			System.out.println("Erreur client");
		}
	}

	/**
	 * read a message send from the socket
	 * @return the string message
	 */
	public String readMessage() {
        //MonoServerMajuscule(PORT);
        String li = null;
        try{
        InputStream is = this.socket_u.getInputStream(); // création d'un flux d'entrée
        InputStreamReader read = new InputStreamReader(is); // conversion du contenu octets -> carac (char)
        BufferedReader bufferRead = new BufferedReader(read); // lit ligne par ligne le flux
        li = bufferRead.readLine();
        }
        catch(IOException e){
            System.out.println("Erreur de lecture");
        }
        return li;
	}

	/**
	 * write a message to the socket
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try{
        OutputStream out = this.socket_u.getOutputStream(); // création flux sortie
        OutputStreamWriter osWriter = new OutputStreamWriter(out); // conversion carac -> octet
        PrintWriter writer = new PrintWriter(osWriter); 
        writer.println(msg);
        writer.flush();
        }
        catch (IOException e){
            System.out.println("erreur ecriture");
        }
	}
}
