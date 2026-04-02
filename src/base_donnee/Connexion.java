package base_donnee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connexion{
    Connection connection = null;
    Statement st; // variable statement pour rs
    ResultSet rs; // variable requête
    

    public Connexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/messagerie_java",
            "db_admin",
            "db_admin");
            System.out.println("Connecté");
            } 
            catch (ClassNotFoundException e) {
            e.printStackTrace();
            } 
            catch (SQLException e) {
            e.printStackTrace();
            }
            //[…]
        
    }
    public void Close(){
        try {
            this.connection.close();
            } 
            catch (SQLException e) {
            e.printStackTrace();
            }
    }

    public ResultSet query_select(String req) throws SQLException{ // méthode pour récupérer les éléments des tables
        //String r;
        try{
            this.st = connection.createStatement(); // créer l'objet pour les requetes
            this.rs = st.executeQuery(req); //récupère le résultat de la requête 
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return this.rs;
    }

    public void query_maj(String req) throws SQLException{ // méthode pour mettre à jour les éléments des tables
        //String r;
        try{
            this.st = connection.createStatement(); // créer l'objet pour les requetes
            st.execute(req); //récupère le résultat de la requête 
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Connexion c = new Connexion();
        c.query_maj("INSERT INTO utilisateur VALUES (1, 'mathieu', 'uzumaki'); ");

    }
}
