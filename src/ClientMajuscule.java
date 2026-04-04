//package TP2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientMajuscule {
    private String nom;
    private String msg; // message que l'utilisateur va envoyer
    private String rep; // message que l'utilisateur va recevoir
    private Socket socket = null;
    

    public ClientMajuscule(String nom) throws IOException{
        this.nom = nom;
        this.socket = new Socket("localhost", 10000);
        System.out.println(InetAddress.getLocalHost());
        System.out.println(this.getNom() + " connecté");
        
    }

    public String getNom(){
        return this.nom;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public String readMessage() {
        //MonoServerMajuscule(PORT);
        String l = null;
        try{
        InputStream is = socket.getInputStream(); // création d'un flux d'entrée
        InputStreamReader read = new InputStreamReader(is); // conversion du contenu octets -> carac (char)
        BufferedReader bufferRead = new BufferedReader(read); // lit ligne par ligne le flux
        l = bufferRead.readLine();
        }
        catch(IOException e){
            System.out.println("Erreur de lecture");
        }
        return l;
	}

    public void sendMessage(String msg1) {
		try{
        OutputStream out = this.socket.getOutputStream(); // création flux sortie
        OutputStreamWriter osWriter = new OutputStreamWriter(out); // conversion carac -> octet
        PrintWriter writer = new PrintWriter(osWriter); 
        writer.println(msg1);
        writer.flush();
        }
        catch (IOException e){
            System.out.println("erreur ecriture");
        }
	}
    // Dans ClientMajuscule.java
    public void lecture() {
    // Thread de lecture (Anonyme)
        new Thread(() -> {
            while (true) {
                String reponse = readMessage();
                if (reponse != null) System.out.println("\n[Serveur] : " + reponse);
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

    public void linkServer(){
        this.lecture();
        this.envoi();
    }

    public static void main(String args[])throws UnknownHostException, IOException {
        //Socket s = new Socket("localhost",10000);
        System.out.println("Saisir votre nom : ");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        ClientMajuscule clt = new ClientMajuscule(nom);
        clt.linkServer();
        
}
}

