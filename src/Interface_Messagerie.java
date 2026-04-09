import base_donnee.Connexion;
import javafx.scene.layout.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Interface_Messagerie extends JFrame{
    final static int Largeur = 1000;
	final static int Hauteur = 600;
    JPanel chatPanel;
    int i = 1;
  
    
    Connexion c;

    public Interface_Messagerie(String username, ClientMajuscule cl) throws SQLException{
        c = new Connexion();
        setTitle("Messagerie");
        setSize(Largeur, Hauteur);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel chatPanel = new JPanel(new BorderLayout());

        // Partie gauche : messages
        JPanel JPGauche = new JPanel(new BorderLayout());
        JTextArea zoneMSG = new JTextArea();
        zoneMSG.setEditable(false);

        JScrollPane chatScroll = new JScrollPane(zoneMSG);
        
        JTextField text = new JTextField();
        JButton envoyer = new JButton("Envoyer");
        envoyer.addActionListener(new ActionListener() { // methode qui envoie un message et créé des bulles
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = text.getText(); 
                if(!message.isEmpty()){
                    addMessage(message, true); // on l'affiche de notre côté
                    cl.sendMessage(message); // envoie au server
                    try {
                        c.query_maj("INSERT INTO message VALUES (null, null, null,'" +message +"', null");
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    text.setText("");
                }
            }
        
        });
        
        JPanel envoieP = new JPanel(new BorderLayout());
        envoieP.add(text, BorderLayout.CENTER);
        envoieP.add(envoyer, BorderLayout.EAST);

        JPGauche.add(chatScroll, BorderLayout.CENTER);
        JPGauche.add(envoieP, BorderLayout.SOUTH);


        // Partie a droite
        JPanel JPDroit = new JPanel(new BorderLayout());
        CardLayout MenuCartes = new CardLayout();
        JPanel JPSelect = new JPanel(MenuCartes);
        

        //Menu de séléction conversation Utilisateur ou groupe grâce a des boutons 
        JButton JButilisateurs = new JButton("Utilisateurs");
        JButton JBgroupes = new JButton("Groupes");
        
        JPanel JLMenu = new JPanel();
        JLMenu.add(JButilisateurs);
        JLMenu.add(JBgroupes);

        


        //Partie conv utilisateur
        JPanel JPUtilisateur = new JPanel(new BorderLayout()); //création d'un panel pour stocké la liste des utilisateurs

        String rqListUser = "SELECT nom FROM utilisateur;";
        c.query_select(rqListUser);
        
        List<String> ListUser = new ArrayList<>();

        while(c.rs.next()){ //Rentre les utilisateurs de la rqt SQL pour les afficher
            ListUser.add(c.rs.getString("nom"));
        }

        JList<String> JListUser = new JList<>(ListUser.toArray(new String[0]));
        JScrollPane JSPUser = new JScrollPane(JListUser);


        JPUtilisateur.add(JSPUser,BorderLayout.CENTER);
        JPSelect.add(JPUtilisateur,"Utilisateurs");
        JButilisateurs.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuCartes.show(JPSelect,"Utilisateurs");
            }
        });



        //Partie conv groupes
        JPanel JPgroupes = new JPanel(new BorderLayout());
        //rqt qui prend les grps auquel l'utilisateur de l'interface appartient
        String rqListGrp = "SELECT nom_grp FROM groupe g JOIN utilisateur_groupe ug ON g.id_grp = ug.id_grp WHERE id_user = (SELECT id_user FROM utilisateur WHERE nom = '" + username + "');";
        c.query_select(rqListGrp);
        List<String> ListGrp = new ArrayList<>();
        while(c.rs.next()){
            ListGrp.add(c.rs.getString("nom_grp"));
        }
        JList<String> JListGrp = new JList<>(ListGrp.toArray(new String[0]));
        JScrollPane JSPGrp = new JScrollPane(JListGrp);
        JPgroupes.add(JSPGrp,BorderLayout.CENTER);
        JPSelect.add(JPgroupes, "Groupes");
        JBgroupes.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                MenuCartes.show(JPSelect,"Groupes");
            }
        });

        JPDroit.add(JLMenu,BorderLayout.NORTH);
        JPDroit.add(JPSelect,BorderLayout.CENTER);
        chatPanel.add(JPDroit);

        //Séparation des 2 panel pour avoir 2 colonnes
        JSplitPane separation = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, JPGauche, JPDroit);
        separation.setResizeWeight( 0.9 );

        chatPanel.add(separation, BorderLayout.CENTER);



        //Barre d'information utilisateur 
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));

        JLabel LabelStatut = new JLabel("Vous êtes connecté");
        infoPanel.add(LabelStatut);

        JLabel DiscussActuel = new JLabel("Discussion avec Personne");
        infoPanel.add(DiscussActuel);

        JLabel LabelNomUser = new JLabel("Vous êtes : "+ username);
        infoPanel.add(LabelNomUser);



        JListUser.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                DiscussActuel.setText(JListUser.getSelectedValue());
                zoneMSG.setText("");
            }
        });

        JListGrp.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                DiscussActuel.setText(JListGrp.getSelectedValue());
                zoneMSG.setText("");
            }
        });




        //Menu parametre
        JMenuBar setting = new JMenuBar();
        JMenu parametre = new JMenu("Paramètre");
        JMenuItem log_out = new JMenuItem("Déconnexion"); 
        log_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                try {
                    c.query_select("SELECT statut FROM utilisateur WHERE nom ='" + username +"';");
                    if(c.rs.next() && c.rs.getInt("statut")== 1){
                        c.query_maj("UPDATE utilisateur SET statut = 0 WHERE nom = '"+ username +"';");
                        log_out.setText("Connection");
                        LabelStatut.setText("Vous êtes deconnecté");
                        DiscussActuel.setText("Discussion avec Personne");
                        LabelNomUser.setText("Vous êtes personne");
                        zoneMSG.setText("");
                    }else {
                        new Interface_Connexion();
                        setVisible(false);

                    }

                } catch (SQLException e1) {
                    
                    e1.printStackTrace();
                }
            }
        });
        parametre.add(log_out);
        

        JPanel JPanelNord = new JPanel(new FlowLayout());

        JMenuItem Decoration = new JMenuItem("Décoration");
        Decoration.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int r=Personnalisation.color();
                int b=Personnalisation.color();
                int g=Personnalisation.color();
                JPanelNord.setBackground(new Color(r,g,b));
                envoieP.setBackground(new Color(r,g,b));
                JLMenu.setBackground(new Color(r,g,b));
            }
        });
        parametre.add(Decoration);
        setting.add(parametre);

        
        JPanelNord.add(setting);
        JPanelNord.add(infoPanel);
        add(JPanelNord,BorderLayout.NORTH);
        add(chatPanel,BorderLayout.CENTER);
    }

    public void addMessage(String text, boolean isMe) {
            // Panel de ligne pour l'alignement (Gauche ou Droite)
            JPanel line = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT));
            line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Empêche de prendre trop de hauteur
    
            // La bulle de message
            JLabel bubble = new JLabel(text);
            bubble.setOpaque(true);
            bubble.setBackground(isMe ? Color.CYAN : Color.RED);
            bubble.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Le "padding" interne
    
            line.add(bubble);
            chatPanel.add(line);
    
            // Espace entre les messages (le fameux gap)
            chatPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
    
            chatPanel.revalidate();
        }


    public static void main(String[] args) {
    }


}