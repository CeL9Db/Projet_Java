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
    private String msg;
    private Socket socket = null;
    

    public ClientMajuscule() throws IOException{
        this.socket = new Socket("localhost", 10000);
        System.out.println(InetAddress.getLocalHost());
        this.envoi();
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
        OutputStream out = socket.getOutputStream(); // création flux sortie
        OutputStreamWriter osWriter = new OutputStreamWriter(out); // conversion carac -> octet
        PrintWriter writer = new PrintWriter(osWriter); 
        writer.println(msg1);
        writer.flush();
        }
        catch (IOException e){
            System.out.println("erreur ecriture");
        }
	}

    public void envoi(){
        Scanner sc = new Scanner(System.in);
        try {
            do{
            System.out.print("Veuillez saisir votre message : ");
            this.msg = sc.nextLine();
            sendMessage(this.msg);
            System.out.println("Message envoye"); 
            String rep = readMessage();
            if(rep != null)
                System.out.println("Retour : " + rep + "\n");
            }
            while(!msg.equalsIgnoreCase("exit"));
        } catch (Exception e) {
            System.err.println("Erreur envoie du message : "+ e);
        }
        
    }

    public static void main(String args[])throws UnknownHostException, IOException {
        ClientMajuscule clt = new ClientMajuscule();
}
}

