import base_donnee.Connexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class Interface_Connexion extends JFrame{
    final static int Largeur = 450;
	final static int Hauteur = 350;

    public Interface_Connexion(){
        //création interface
        Panel pane = new Panel();
        pane.setLayout(new BorderLayout());

        
        //affichage texte en haut de la fenetre
        JPanel PanelNorth = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_north = new GridBagConstraints();
        gbc_north.insets = new Insets(10,10,0, 0);

        JLabel name_window = new JLabel("Connexion à la messagerie");
        name_window.setForeground(Color.blue);
        gbc_north.gridx = 1;
        gbc_north.gridy = 1;
        PanelNorth.add(name_window,gbc_north);

        JButton ColorButton = new JButton("✨");//Action du boutton a la fin de l'interface
        gbc_north.gridx = 2;
        gbc_north.gridy = 1;
        ColorButton.setPreferredSize(new Dimension(50,20));
        PanelNorth.add(ColorButton,gbc_north);  

        pane.add(PanelNorth,BorderLayout.NORTH);


        //affichage des informations de connexion au centre de la fenetre

        JPanel PanelCenter = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_center = new GridBagConstraints();
        gbc_center.insets = new Insets(0,5,20, 0);

        //set login
        JLabel loginLabel = new JLabel("Username : ");
        
        gbc_center.gridx = 0;
        gbc_center.gridy = 0;
        PanelCenter.add(loginLabel,gbc_center);

        JTextField loginJTF = new JTextField(15);
        gbc_center.gridx = 1;
        gbc_center.gridy = 0;
        PanelCenter.add(loginJTF,gbc_center);

        //Mot de passe
        JLabel pwdLabel = new JLabel("Mot de passe : ");
        gbc_center.gridx = 0;
        gbc_center.gridy = 1;
        PanelCenter.add(pwdLabel,gbc_center);

        JTextField pwdJTF = new JTextField(15);
        gbc_center.gridx = 1;
        gbc_center.gridy = 1;
        PanelCenter.add(pwdJTF,gbc_center);


        JLabel ErrorLabel = new JLabel(); //champ prévu en cas d'erreur lors de la connexion
        ErrorLabel.setVisible(false);
        ErrorLabel.setForeground(Color.red);
        gbc_center.gridx = 1;
        gbc_center.gridy = 2;
        PanelCenter.add(ErrorLabel,gbc_center);

        pane.add(PanelCenter,BorderLayout.CENTER);



        //Gestion Bouton pour se connecter ou s'enregister
        JPanel PanelSouth = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_south = new GridBagConstraints();

        JLabel ConnectLabel = new JLabel();
        ConnectLabel.setText("Vous n'êtes pas encore inscrit ?");
        gbc_south.gridx = 0;
        gbc_south.gridy = 0;
        PanelSouth.add(ConnectLabel,gbc_south);

        
        JButton BInscription = new JButton("S'inscrire");

        gbc_south.gridx = 0; 
        gbc_south.gridy = 1;
        BInscription.addActionListener(new ActionListener(){
            //ferme la fenetre et redirige vers Interface_Inscription
            public void actionPerformed(ActionEvent e){
                new Interface_Inscription();
                setVisible(false);
            }
        });

        PanelSouth.add(BInscription,gbc_south);

        JButton BConnect = new JButton("Se Connecter");  
        gbc_south.insets = new Insets(0,70,10, 10);
        gbc_south.gridx = 1;
        gbc_south.gridy = 1;
        BConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String username = loginJTF.getText();
                String mdp = pwdJTF.getText();
                String rq = "SELECT nom,mdp FROM utilisateur WHERE nom = '"+ username +"' AND mdp ='"+mdp+"'; ";
                String UpdateStatut = "UPDATE utilisateur SET statut = 1 WHERE nom ='"+username+"';";
                Connexion rqUsername = new Connexion();
                try {
                    rqUsername.query_select(rq);
                    if(rqUsername.rs.next()){
                        ClientMajuscule client = new ClientMajuscule(username);
                        new Interface_Messagerie(username, client);
                        client.lecture();
                        
                        rqUsername.query_maj(UpdateStatut); // mis à jour du statut
                        setVisible(false);
                    }else{
                        ErrorLabel.setText("Mauvais nom d'utilisateur ou mot de passe");
                        ErrorLabel.setVisible(true);
                    }
                } catch (SQLException | IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                } 
                }
        });
        //Penser a update le statut de l'utilisateur a la connexion
        PanelSouth.add(BConnect,gbc_south);




        pane.add(PanelSouth,BorderLayout.SOUTH);


        this.add(pane);

        ColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int r=Personnalisation.color();
                int b=Personnalisation.color();
                int g=Personnalisation.color();
                PanelNorth.setBackground(new Color(r,g,b));
                PanelCenter.setBackground(new Color(r,g,b));
                PanelSouth.setBackground(new Color(r,g,b));

            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("connexion");
		setResizable(true);
		setSize(Largeur,Hauteur);
		setVisible(true);
        
        
    }

    public static void main(String[] args) {

        new Interface_Connexion();
        
    }
}