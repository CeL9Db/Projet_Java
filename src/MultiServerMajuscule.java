import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServerMajuscule {
    private static final int PORT = 10000;	// socket port
	private ServerSocket server = null;
	private Socket socket = null;

	public MultiServerMajuscule(int num)throws IOException {
		try {
			this.server = new ServerSocket(num);
		} catch (IOException e) {
			System.err.println("Server: " + e);
			System.exit(1);
		}
		System.out.println("Serveur: a l'ecoute sur le port " + server.getLocalPort());
		this.ecoute();
	}


	/**
	 * receive from the socket a string message and send it in upper case to the same socket
	 */
	public void ecoute()throws IOException {
		while (true) { 
			this.socket = server.accept();
            ThreadMajuscule th = new ThreadMajuscule(this.socket);
            th.start();
			System.out.println(th.getId());
		
	}}

	public static void main(String args[])throws IOException {
		int num = 10000;
		new MultiServerMajuscule(num);
		System.out.println("Serveur en attente d'une reponse");
		
	}

}
