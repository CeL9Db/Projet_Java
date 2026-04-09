import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;


public class ThreadMajuscule extends Thread implements Serializable{
    //private ServerSocket server;
    private transient Socket socket_u = null;	
	MultiServerMajuscule serv;
	private transient ObjectOutputStream oos;
	private transient  ObjectInputStream ois;
	Interface_Messagerie mess;

    public ThreadMajuscule(Socket socket, MultiServerMajuscule ms)throws IOException, UnknownHostException, SQLException{
        this.socket_u = socket; // socket du client
        this.serv = ms;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
		//this.oos.writeObject(this);
		//mess = new Interface_Messagerie(null);
		//Thread 
    }


    public void run() { // Main méthode pour accueillir un client via un thread
			//this.socket = server.accept();
			try{
			System.out.println("Serveur: connexion etablie avec le client " + this.socket_u.getInetAddress());
			String nom = readMessage(); // réception du client
			this.setName(nom);

			while(true) {
				String s = readMessage(); // Attend le message du Client A
				if(s == null) break;
				if(!s.equalsIgnoreCase("exit")){
				System.out.println("Relais du message de " + nom + " : " + s);
				// Option A : Envoyer à tout le monde (Chat public)
				broadcastPublic(s);}
				
				// Option B : Si tu veux du privé, il faut que le client envoie 
				// un format spécial (ex: "destinataire:message") pour que tu puisses 
				// extraire le nom sans utiliser Scanner au clavier sur le serveur.

		}
	}catch(ClassNotFoundException e){
		e.printStackTrace();
	}
			
	}

	private Scanner nextLine() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'nextLine'");
	}


	/**
	 * read a message send from the socket
	 * @return the string message
	 * @throws ClassNotFoundException 
	 */
	public String readMessage() throws ClassNotFoundException {
        //MonoServerMajuscule(PORT);
        //String l = null;
		//ClientMajuscule c = null;
        try{
        return (String) ois.readObject(); // Méthode bloquante qui attend un objet
        //l = this.client.getMessage();
        }
        catch(IOException e){
            System.out.println("Erreur de lecture");
        }
        return null;
	}

	
	public void sendMessage(String msg) {
		try {

            oos.writeObject(msg);
            oos.flush();
			oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

		public void broadcastPrivate(String msg, String nom){
		for(ThreadMajuscule th : serv.clients){
			if(th.getName().equals(nom)){ // thread name = client
				//System.out.println("thread :" + th.getName() + "/" + th.getId() + " envoie un message...");
				th.sendMessage(msg);
			}
		}
	}

	public void broadcastPublic(String msg){
		for(ThreadMajuscule th : serv.clients){
			if(this != th){
				//System.out.println("thread :" + th.getName() + "/" + th.getId() + " envoie un message...");
				th.sendMessage(msg);
			}
		}
	}

}