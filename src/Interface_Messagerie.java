import base_donnee.Connexion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;


public class Interface_Messagerie extends JFrame{
    final static int Largeur = 600;
	final static int Hauteur = 600;
    private String user;
    protected  String mdp;
    JPanel chatPanel;
    Connexion c;
    int i = 1;

    public Interface_Messagerie(String username, String mdp) throws SQLException{
        c = new Connexion();
        this.user = username;
        this.mdp = mdp;
        //BorderLayout b1 = new BorderLayout();
        JPanel p1 = new JPanel(new BorderLayout(20,20)); // panel interface complète
        chatPanel = new JPanel(); // panel interface des messages
        BoxLayout box_chat = new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS);
        chatPanel.setLayout(box_chat);
        //chatPanel.setBackground(Color.gray); // changement de couleur
        JPanel p3 = new JPanel(new GridLayout(1, 3, 50, 50));
        //p3
        JMenuBar menu = new JMenuBar();

        // bouton pour ajouter des groupes et utilisateurs
        //JButton add_user = new JButton("Ajouter Utilisateur");
        JLabel statut = new JLabel();
        ResultSet s = c.query_select("Select statut from utilisateur where nom ='" + this.user +"';");
        int connected = 0;
        if(s.next()){
            connected = s.getInt("statut");
        }
        if(connected == 1)
            statut.setText(this.user + " : connecté");

        
        
        JButton add_groupe = new JButton("Ajout Groupe");
        
        add_groupe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                
                JDialog addG = new JDialog(getOwner());
                JTextField t = new JTextField("entrez le nom du groupe");
                addG.add(t);
                this.add(addG);
                if(t.getText() != null){
                    addGroupe(i, t.getText());
                    JMenuItem g =  new JMenuItem(t.getText());
                    add_groupe.add(g);
                    i += 1;
            }
            }

            private void add(JDialog addG) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        // Size pour les boutons d'ajout
        add_groupe.setSize(30, 30);
        //add_user.setSize(30, 30);
        // elements du menu
        JMenu groupe = new JMenu("Groupes");
        JMenu utilisateur = new JMenu("Utilisateurs");
        JMenu setting = new JMenu("Paramètre");

        // Items des menus
        JMenuItem log_out = new JMenuItem("Déconnexion"); 
        log_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //user = null;
                System.exit(0);
                System.out.println("Déconnexion réussi");
                try {
                    c.query_maj("UPDATE utilisateur SET statut = 0");
                    statut.setText(user + " : déconnecté");
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });
        setting.add(log_out);

        // ajout des éléments
        menu.add(groupe);
        menu.add(utilisateur);
        menu.add(setting);
        menu.add(statut);
        menu.add(add_groupe);


        JLabel message = new JLabel("message :");
        JTextField text = new JTextField();
        JButton envoyer = new JButton("envoyer");
        envoyer.addActionListener(new ActionListener() { // methode qui envoie un message et créé des bulles
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = text.getText(); 
                addMessage(message, true);
                text.setText(null);
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        
        });
        p3.add(message);
        p3.add(text);
        p3.add(envoyer);
        

        // interface message (p2)
        
        p1.add(menu, BorderLayout.PAGE_START );
        p1.add(chatPanel, BorderLayout.CENTER);
        p1.add(p3, BorderLayout.PAGE_END);
        this.add(p1);
        

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Messagerie");
		setResizable(false);
		setSize(Largeur,Hauteur);
		setVisible(true);
        
    }
    // 1. Le conteneur principal qui contient tous les messages
//JPanel chatPanel = new JPanel();
//chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));

// 2. Méthode pour ajouter une bulle
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

    public void addGroupe(int id, String n){
        Groupe g = new Groupe(id, mdp, 10);
    }
    public static void main(String[] args) throws SQLException {

        new Interface_Messagerie(null, null);
        
    }
}