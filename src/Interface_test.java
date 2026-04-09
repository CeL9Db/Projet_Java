import java.awt.*;
import javax.swing.*;
//import 

public class Interface_test extends JFrame {

    private JList<String> userList;
    private JTextArea chatArea;
    private JTextField messageField;
    private JLabel statusLabel;
    private JLabel currentUserLabel;
    private JLabel myNameLabel;

    public Interface_test() {


        
        setTitle("Messagerie");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel chatPanel = new JPanel(new BorderLayout());

        // Partie gauche : messages
        JPanel leftPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        messageField = new JTextField();
        JButton sendButton = new JButton("Envoyer");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        leftPanel.add(chatScroll, BorderLayout.CENTER);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        // Partie droite : liste utilisateurs
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] users = {"Alice", "Bob", "Charlie"};
        userList = new JList<>(users);
        JScrollPane userScroll = new JScrollPane(userList);

        rightPanel.add(new JLabel("Utilisateurs"), BorderLayout.NORTH);
        rightPanel.add(userScroll, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(550);

        chatPanel.add(splitPane, BorderLayout.CENTER);

        // Onglet paramètres
        JPanel settingsPanel = new JPanel();
        settingsPanel.add(new JLabel("Paramètres"));

        // Barre d'information en haut
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        statusLabel = new JLabel("Statut: Connecté");
        currentUserLabel = new JLabel("Discussion: Aucun");
        myNameLabel = new JLabel("Moi: User1");

        infoPanel.add(statusLabel);
        infoPanel.add(currentUserLabel);
        infoPanel.add(myNameLabel);

        add(infoPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Chat", chatPanel);
        tabbedPane.addTab("Paramètres", settingsPanel);

        add(tabbedPane);

        // Actions
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                currentUserLabel.setText("Discussion: " + selectedUser);
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append("Moi: " + message + "\n");
            messageField.setText("");
        }
    }
    private void receiveMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append("Moi: " + message + "\n");
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Interface_test().setVisible(true);
        });
    }
}
