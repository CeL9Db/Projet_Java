import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class ThreadMajuscule extends Thread implements Serializable{
    //private ServerSocket server;
    private transient Socket socket_u = null;	
	ArrayList<ThreadMajuscule> serv;
	private transient ObjectOutputStream oos;
	private transient  ObjectInputStream ois;

    public ThreadMajuscule(Socket socket, ArrayList<ThreadMajuscule> a)throws IOException, UnknownHostException, SQLException{
        this.socket_u = socket; // socket du client
        this.serv = a;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
    }


    public void run() { // Main méthode pour accueillir un client via un thread
			//this.socket = server.accept();
			try{
			System.out.println("Serveur: connexion etablie avec le client " + this.socket_u.getInetAddress());
			String nom = (String) this.ois.readObject(); // réception du client
			this.setName(nom);
			System.out.println(nom);
			//this.serv.add(this);

			while(true) {
				String s = (String) this.ois.readObject(); //readMessage(); // Attend le message du Client A
				if(s == null) break;
				String[] message_d = s.split("/",2);
				String texte;
				if(message_d.length == 2){
					String user = message_d[0];
					texte = message_d[1];
					broadcastPrivate(texte, user);
				}
				else{
					texte= message_d[1];
					broadcastPublic(texte);
				}
				System.out.println("Relais du message de " + nom + " : " + s); // affichage console
				// Option A : Envoyer à tout le monde (Chat public)
				//broadcastPublic(s);
				
					//broadcastPublic(s);

		}
	}catch(ClassNotFoundException e){
		e.printStackTrace();
		//serv.removeClient(this);
	} catch (IOException ex) {
            System.getLogger(ThreadMajuscule.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
		for(ThreadMajuscule th : this.serv){
			if(th.getName().equals(nom)){ // thread name = client
				th.sendMessage(msg);
				return;
			}
		}
	}

	public void broadcastPublic(String msg){
			for(ThreadMajuscule th : this.serv){
				if(this != th){
					th.sendMessage(msg);
				}
			}
	}
	
}

