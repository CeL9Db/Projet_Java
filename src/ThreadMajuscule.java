import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ThreadMajuscule extends Thread implements Serializable{
    //private ServerSocket server;
    private transient Socket socket_u = null;	
	MultiServerMajuscule serv;
	private transient ObjectOutputStream oos;
	private transient  ObjectInputStream ois;

    public ThreadMajuscule(Socket socket, MultiServerMajuscule ms)throws IOException, UnknownHostException{
        this.socket_u = socket; // socket du client
        this.serv = ms;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
		//Thread 
    }


    public void run() { // Main méthode pour accueillir un client via un thread
		try {
			//this.socket = server.accept();
			System.out.println("Serveur: connexion etablie avec le client " + this.socket_u.getInetAddress());
			String nom = readMessage(); // réception du client
			this.setName(nom);
			if(nom != null){
				serv.addClient(this); // On l'ajoute à la liste DU SERVEUR
				System.out.println(nom + " est maintenant dans la liste.");
			}
			while(true) {
				Message message = new Message(null); // message qui contiendra les enregistrements des messages
				String s = readMessage(); // réception du client
				if(s == null) break; // on arrete la boucle si c'est nulle
				
				//if(this.client == null) break;
                //String msg1 = this.client.getMessage();
				if(s != null && !s.equalsIgnoreCase("exit")){
					message.setMessage(s);
					System.out.println("Message reçu de " + nom + " : " + s + " à " + message.getDate() + "h");
					System.out.println("Veuillez taper le nom du destinataire");
					Scanner sc = new Scanner(System.in);
					String name = sc.nextLine();
					this.broadcastPrivate(s, name);

				}
				else{
					break;
				}
			} 
			this.socket_u.close();
		} catch (IOException e) {
			this.serv.clients.remove(this);
			System.out.println("Erreur client");
		} catch (ClassNotFoundException c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
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