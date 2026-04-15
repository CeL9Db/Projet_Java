import base_donnee.Connexion;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

// extends ThreadMajuscule

public class MultiServerMajuscule implements Serializable{
	private ServerSocket server = null;
	private Socket socket = null;
	// Les groupes disponibles
	ArrayList<ThreadMajuscule> clients = new ArrayList<>();

	// Connexion à la base de données
	Connexion co;


	
	public MultiServerMajuscule(int num)throws IOException{
		try {
			this.server = new ServerSocket(num);
			co = new Connexion(); //on établit la connexion avec la base de données
		} catch (IOException e) {
			System.err.println("Server: " + e);
			System.exit(1);
		}
		System.out.println("Serveur: a l'ecoute sur le port " + server.getLocalPort());
		
	}

// methode qui établit la connexion avec le client
	public void ecoute()throws IOException, SQLException {
		while (true) { 
			this.socket = server.accept();
            ThreadMajuscule th = new ThreadMajuscule(this.socket, clients);
			this.clients.add(th);
            th.start();	
	}}

	public void addClient(ThreadMajuscule e) throws IOException{
		this.clients.add(e);
		
	}
	public void removeClient(ThreadMajuscule e){
		this.clients.remove(e);
	}

	public Groupe createGroupe(String nom, int nb){
		Groupe groupe = new Groupe(nom, nb);
		return groupe;
	} 

	public static void main(String args[])throws IOException, SQLException {
		int num = 10000;
		MultiServerMajuscule serv = new MultiServerMajuscule(num);
		serv.ecoute();
		
	}

}
