//package TP2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.SwingUtilities;

// extends Interface_test
public class ClientMajuscule implements Serializable{
    private String nom;
    private String msg; // message que l'utilisateur va envoyer
    private String rep; // message que l'utilisateur va recevoir
    private transient Socket socket = null;
    private transient ObjectOutputStream oos;
    private transient ObjectInputStream ois;
    //Interface_Messagerie mess;
    //ThreadMajuscule ths; // thread du server pour connecter le client et server
    

    public ClientMajuscule(String nom) throws IOException, SQLException{
        this.nom = nom;
        //this.ths = th;
        this.socket = new Socket("localhost", 10000);
        //System.out.println(InetAddress.getLocalHost());
        System.out.println(this.getNom() + " connecté");
        this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        this.ois = new ObjectInputStream(this.socket.getInputStream());
        this.oos.writeObject(this.nom);
        oos.flush();
        //mess = new Interface_Messagerie(nom, this);
        
    }

    public String getNom(){
        return this.nom;
    }
    public void setNom(String nom){
        this.nom = nom;
    }

    public String getMessage(){
        return this.msg;
    }

    
    public String readMessage() throws ClassNotFoundException {
        
        try{
        return (String) ois.readObject(); // Méthode bloquante qui attend un objet
        }
        catch(IOException | ClassNotFoundException e){
            return null;
        }
        //return null;
	}

    public void sendMessage(String ms) {
		try {
            //this.msg = ms;
            oos.writeObject(ms);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    // Dans ClientMajuscule.java
    public void lecture() throws ClassNotFoundException{
    // Thread de lecture (Anonyme)
        //String reponse;
        new Thread(() -> {
                try {
                    while (true) {
                    this.rep = (String) this.ois.readObject();
                    //if (this.rep != null) {
                    SwingUtilities.invokeLater(() -> {
                        //if(this.rep != null)
                            //this.rep.addMessage(this.rep, false); // false = message reçu des autres
                        try {
                            this.rep = (String) this.ois.readObject();
                        } catch (ClassNotFoundException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                            
                    });
            //}
                    //System.out.println("\n[Serveur] : " + this.rep);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            
        }).start(); 
}
    public void envoi(){
        Scanner sc = new Scanner(System.in);
        try {
            do{
                System.out.print("Veuillez saisir votre message : ");
                this.msg = sc.nextLine();
                sendMessage(this.msg);
                System.out.println("Message envoye");
            }
            
            while(!this.msg.equalsIgnoreCase("exit"));
        } catch (Exception e) {
            System.err.println("Erreur envoie du message : "+ e);
        }
        
    }

    public void close_s() throws IOException{
        System.out.println("Fermeture de la socket du client : " + this.socket.getInetAddress() + "...");
        try {
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void linkServer() throws ClassNotFoundException{
        this.lecture();
        //this.envoi();
    }

    public static void main(String args[])throws UnknownHostException, IOException, ClassNotFoundException, SQLException {
        //Socket s = new Socket("localhost",10000);
        System.out.println("Saisir votre nom : ");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        ClientMajuscule clt = new ClientMajuscule(nom);
        clt.linkServer();
        
}
}

