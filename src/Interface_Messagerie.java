import base_donnee.Connexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Interface_Messagerie extends JFrame{
    final static int Largeur = 1000;
	final static int Hauteur = 600;
    JPanel chatPanel;
    int i = 1;
    String user;
    private String receiver;
    ClientMajuscule cl;
    Connexion c;
    JPanel zoneMSG;
    JPanel JPGauche;

    public Interface_Messagerie(String username, ClientMajuscule cl) throws SQLException{
        c = new Connexion();
        this.cl = cl;
        this.user = username; //defini user pour le récuperer dans le close de l'interface
        setTitle("Messagerie");
        setSize(Largeur, Hauteur);
        addWindowListener(new ClosingAdapter());
        setLocationRelativeTo(null);
        setVisible(true);

        //Fenetre global
        JPanel chatPanel = new JPanel(new BorderLayout());

        // Partie gauche : messages
        JPGauche = new JPanel(new BorderLayout());
        zoneMSG = new JPanel();
        BoxLayout box_chat = new BoxLayout(zoneMSG, BoxLayout.PAGE_AXIS);
        zoneMSG.setLayout(box_chat);
        JPGauche.add(zoneMSG);

        JScrollPane chatScroll = new JScrollPane(zoneMSG);
        
        JTextField text = new JTextField();
        JButton envoyer = new JButton("Envoyer");
        Date date = new Date();
        
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
        Connexion c2 = new Connexion();
        List<String> ListUser = new ArrayList<>();
        String StatutUser;
        Boolean rStatutUser;
        while(c.rs.next()){ //Rentre les utilisateurs de la rqt SQL pour les afficher
            String JLUserActuel = c.rs.getString("nom");
            StatutUser = "SELECT statut FROM utilisateur WHERE nom = '"+JLUserActuel+"';";
            c2.query_select(StatutUser);
            c2.rs.next();
            rStatutUser = c2.rs.getBoolean("statut");
            if(rStatutUser){
                ListUser.add(JLUserActuel+": connecté");
            }else {
                ListUser.add(JLUserActuel+" : déconnecté");
            }
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
        DefaultListModel<String> DListGrp = new DefaultListModel<>();
        while(c.rs.next()){
            DListGrp.addElement(c.rs.getString("nom_grp"));
        }
        JList<String> JListGrp = new JList<>(DListGrp);
        JScrollPane JSPGrp = new JScrollPane(JListGrp);
        JPgroupes.add(JSPGrp,BorderLayout.CENTER);
        JPSelect.add(JPgroupes, "Groupes");

        JPanel PDSud = new JPanel(new BorderLayout());
        JButton BajouteGrp = new JButton("Nouveau Groupe");
        PDSud.add(BajouteGrp, BorderLayout.SOUTH);

        JPanel PDSNord = new JPanel(new GridLayout(1,2));
        JLabel JLgrp = new JLabel("Entrez le nom du groupe");
        JTextField JTFaddGrp = new JTextField();
        PDSNord.add(JLgrp);
        PDSNord.add(JTFaddGrp);
        PDSud.add(PDSNord,BorderLayout.NORTH);
        
        JList<String> JLUser4Grp = new JList<>(ListUser.toArray(new String[0]));
        JScrollPane JSPUser4Grp = new JScrollPane(JLUser4Grp);
        JLUser4Grp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        PDSud.add(JSPUser4Grp,BorderLayout.CENTER);
        
        JPgroupes.add(PDSud,BorderLayout.SOUTH);
        
        
        //Fonction qui ajoute un nouveau groupe a la base de donnée et a la liste des groupes
        BajouteGrp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String grpName = JTFaddGrp.getText();
                List<String> ListSelec = JLUser4Grp.getSelectedValuesList();
                String rqCreaGrp= "INSERT INTO groupe(nom_grp,nb_membre) VALUES ('"+ grpName +"',"+ListSelec.size()+");";
                
                try {
                    if(ListSelec.contains(username)){
                        c.query_maj(rqCreaGrp);
                        String idNewGrp = "SELECT id_grp FROM groupe WHERE nom_grp = '"+ grpName +"';";
                        c.query_select(idNewGrp);
                        c.rs.next();
                        int idgrp = c.rs.getInt("id_grp");
                        String UserList;
                        for(String s : ListSelec){
                            UserList = "SELECT id_user FROM utilisateur WHERE nom = '"+s+"';";
                            c.query_select(UserList);
                            c.rs.next();
                            int iduser = c.rs.getInt("id_user");
                            String GroupeUpdate = "INSERT INTO utilisateur_groupe(id_grp,id_user) VALUES ("+idgrp+","+iduser+");";
                            c.query_maj(GroupeUpdate);        
                        }
                        DListGrp.addElement(grpName);
                    }else {
                        JOptionPane.showConfirmDialog(null,"Vous ne pouvez pas créer un groupe ou vous n'êtes pas","Erreur Création Groupe", JOptionPane.OK_OPTION);
                    }
                } catch (SQLException e1) {
                    
                    e1.printStackTrace();
                }
                
            }

        });

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
        JPanel infoPanel = new JPanel(new GridLayout(1, 4));

        JLabel LabelStatut = new JLabel("Vous êtes connecté");
        infoPanel.add(LabelStatut);

        JLabel DiscussActuel = new JLabel("Discussion avec Personne");
        infoPanel.add(DiscussActuel);

        JLabel LabelNomUser = new JLabel("Vous êtes : "+ username);
        infoPanel.add(LabelNomUser);



        JListUser.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                DiscussActuel.setText(JListUser.getSelectedValue());
                if(JListUser.getSelectedValue().equals(JListUser.getSelectedValue())){
                    receiver = JListUser.getSelectedValue();
                }
            }
        });
        
        envoyer.addActionListener(new ActionListener() { // methode qui envoie un message et créé des bulles
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = text.getText();
                String s = receiver;
                if(!message.isEmpty() && receiver != null){
                    //if(message.contains(receiver)){
                    String[] nom_u = s.split(":",2); // on split laurin : déconnecté/salut laurin -> laurin en 0 et déconnecter/salut laurin en 1
                    //String[] texte = message.split("/",2); 
                    String nom2 = nom_u[0];
                    if(nom2.contains(" ")){
                        nom2 = nom2.split(" ")[0];
                    }
                    else{
                        nom2 = nom_u[0];
                    }
                    String phrase_finale = nom2 +"/"+message;
                    //addMessage(message, true); // on l'affiche de notre côté
                    addMessage(message, true);
                    
                    cl.sendMessage(phrase_finale);



                    // Partie où l'on enregistre le message des users dans la data base
                    int id = 0;
                    try {
                        ResultSet id_user = c.query_select("Select id_user from utilisateur WHERE nom = '"+username+"';");
                        if(id_user.next())
                            id = id_user.getInt("id_user");
                        c.query_maj("INSERT INTO message(id_sender,messagetxt,dateMess) VALUES ("+id+", '" +message +"','"+date+"');");
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    text.setText("");
                }
                 else
                    //JOptionPane.showMessageDialog(null, "Veuillez choisir un destinataire dans la liste !");
                    cl.sendMessage(message);
            
            }
        
        });


        JListGrp.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                DiscussActuel.setText(JListGrp.getSelectedValue());
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
                PDSNord.setBackground(new Color(r,g,b));
                JLMenu.setBackground(new Color(r,g,b));
                infoPanel.setBackground(new Color(r,g,b));
            }
        });
        parametre.add(Decoration);
        setting.add(parametre);

        
        JPanelNord.add(setting);
        JPanelNord.add(infoPanel);
        add(JPanelNord,BorderLayout.NORTH);
        add(chatPanel,BorderLayout.CENTER);
    }
    public String getUser(){
        return user;
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
        zoneMSG.add(line);
    
        // Espace entre les messages (le fameux gap)
        zoneMSG.add(Box.createRigidArea(new Dimension(0, 10))); 
    
        zoneMSG.revalidate();
    }

    public void listen_reception_mess(){
    new Thread(() -> {
        try {
            while (true) {
                // On lit dans le thread secondaire (NE BLOQUE PAS L'UI)
                final String m = this.cl.readMessage(); 
                
                if (m != null) {
                    // On met à jour l'UI sur le thread graphique
                    SwingUtilities.invokeLater(() -> {
                        addMessage(m, false);
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
    }

    public class ClosingAdapter extends WindowAdapter{
        
        public void windowClosing(WindowEvent e){
            String username = getUser();
            JOptionPane.showConfirmDialog(e.getWindow(),"Vous allez quitter", null, JOptionPane.OK_OPTION);
                try {
                    c.query_maj("UPDATE utilisateur SET statut = 0 WHERE nom = '"+ username +"';");
                    System.exit(0);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
        }
    }
    public static void main(String[] args) {
    }


}