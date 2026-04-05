import java.awt.*;
import javax.swing.*;


public class Interface_Messagerie extends JFrame{
    final static int Largeur = 1000;
	final static int Hauteur = 1000;


    public Interface_Messagerie(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Messagerie");
		setResizable(false);
		setSize(Largeur,Hauteur);
		setVisible(true);
        
    }
    public static void main(String[] args) {

        new Interface_Messagerie();
        
    }
}