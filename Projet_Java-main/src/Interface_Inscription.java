import base_donnee.Connexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;


public class Interface_Inscription extends JFrame{
    final static int Largeur = 450;
	final static int Hauteur = 350;
    ResultSet req;


    public Interface_Inscription(){
        // init connexion
        Connexion rqUsername = new Connexion();
       //création interface
        Panel pane = new Panel();
        pane.setLayout(new BorderLayout());

        
        //affichage texte en haut de la fenetre
        JPanel PanelNorth = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_north = new GridBagConstraints();
        gbc_north.insets = new Insets(10,10,0, 0);

        JLabel name_window = new JLabel("Inscription à la messagerie");
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


        //Confirmer Mot de passe
        JLabel pwdCLabel = new JLabel("Confirmer mot de passe : ");
        gbc_center.gridx = 0;
        gbc_center.gridy = 2;
        PanelCenter.add(pwdCLabel,gbc_center);

        JTextField pwdCJTF = new JTextField(15);
        gbc_center.gridx = 1;
        gbc_center.gridy = 2;
        PanelCenter.add(pwdCJTF,gbc_center);

        JLabel ErrorLabel = new JLabel();//Lors d'erreur d'inscription
        ErrorLabel.setVisible(false);
        ErrorLabel.setForeground(Color.red);
        gbc_center.gridx = 1;
        gbc_center.gridy = 3;
        PanelCenter.add(ErrorLabel,gbc_center);

        pane.add(PanelCenter,BorderLayout.CENTER);



        //Gestion Bouton pour se connecter ou s'enregister
        JPanel PanelSouth = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_south = new GridBagConstraints();

        
        JLabel ConnectLabel = new JLabel();
        ConnectLabel.setText("Vous êtes déjà inscrit ?");
        gbc_south.gridx = 0;
        gbc_south.gridy = 0;
        PanelSouth.add(ConnectLabel,gbc_south);

        JButton BConnect = new JButton("Se Connecter");  
        gbc_south.gridx = 0;
        gbc_south.gridy = 1;
        BConnect.addActionListener(new ActionListener(){
            //ferme la fenetre et redirige vers Interface_connection
            public void actionPerformed(ActionEvent e){
                new Interface_Connexion();
                setVisible(false);
            }
        });
        PanelSouth.add(BConnect,gbc_south);

        JButton BInscription = new JButton("S'inscrire");
        gbc_south.insets = new Insets(0,70,10, 10);
        gbc_south.gridx = 1; 
        gbc_south.gridy = 1;
        /*Verif username existe pas bdd
        */
        BInscription.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                int k = 1;
                String NewUsername = loginJTF.getText();
                String mdp1 = pwdJTF.getText();
                String mdp2 = pwdCJTF.getText();
                System.out.println(mdp1 +" "+ mdp2);
                if(mdp1.equals(mdp2)){
                    String rq = "SELECT nom FROM utilisateur WHERE nom ='"+ NewUsername + "';"; 
                    System.out.println(rq);
                    
                    try {
                        rqUsername.query_select(rq);
                        if(!rqUsername.getSelect().next()){
                                String rq_maj = "INSERT INTO utilisateur VALUES("+k+", '"+NewUsername+"', '"+mdp1+"', 1);"; // le 1 veut dire connecté
                                System.out.println(rq_maj);
                                rqUsername.query_maj(rq_maj);
                                new Interface_Connexion();
                                setVisible(false);
                            }else{
                                ErrorLabel.setText("Le nom d'utilisateur existe déjà");
                                ErrorLabel.setVisible(true);
                        }
                        k++;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    ErrorLabel.setText("Le mot de passe confirmé n'est pas le bon");
                    ErrorLabel.setVisible(true);
                }
            }
        });
        PanelSouth.add(BInscription,gbc_south);



        pane.add(PanelSouth,BorderLayout.SOUTH);

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

        this.add(pane);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Inscription");
		setResizable(true);
		setSize(Largeur,Hauteur);
		setVisible(true);
        
    }
    public static void main(String[] args) {
        new Interface_Inscription();
        
    }
}