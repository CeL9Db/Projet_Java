import base_donnee.Connexion;
import java.util.ArrayList;

public class Groupe extends Connexion{
    private int id;
    private String nom;
    private int nombre;
    ArrayList<ClientMajuscule> list;


    public Groupe(int id, String nom, int nombre){
        this.list = new ArrayList<ClientMajuscule>();
        this.id = id;
        this.nom = nom;
        this.nombre = nombre;
    }

    public void addUser(ClientMajuscule clt){
        this.list.add(clt);
    }

    public void deleteUser(String nom){
        for (ClientMajuscule clt : this.list) {
            if(clt.getNom().equals(nom))
                this.list.remove(clt);
        }
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombre() {
        return this.nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
    
}
