//package TP2;

//package TP2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Mono-threaded socket server converting string messages in upper case.
 * 
 * @author Sameh Kchaou
 *
 */
public final class MonoServerMajuscule {

	private static final int PORT = 10000;	// socket port
	private ServerSocket server = null;
	private Socket socket = null;

	public MonoServerMajuscule(int num) {
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
	public void ecoute() {
		try {
			this.socket = server.accept();
			System.out.println("Serveur: connexion etablie avec le client " + socket.getInetAddress());
			String msg;
			do {
				msg = readMessage();
				if(msg != null){
					System.out.println("Message  : " + msg + "\n");
					sendMessage(msg.toUpperCase()); // renvoie le message en maj
				}
				
			} while (!msg.equalsIgnoreCase("exit"));
			System.out.println("connection rompue avec le client " + socket.getInetAddress());
			socket.close();
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
        InputStream is = socket.getInputStream(); // création d'un flux d'entrée
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
        OutputStream out = socket.getOutputStream(); // création flux sortie
        OutputStreamWriter osWriter = new OutputStreamWriter(out); // conversion carac -> octet
        PrintWriter writer = new PrintWriter(osWriter); 
        writer.println(msg);
        writer.flush();
        }
        catch (IOException e){
            System.out.println("erreur ecriture");
        }
	}

	public static void main(String args[]) {
		int num = 10000;
		new MonoServerMajuscule(num);
		System.out.println("Serveur en attente d'une reponse");
		
	}

}