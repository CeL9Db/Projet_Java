import base_donnee.Connexion;
import java.util.ArrayList;

public class Groupe extends Connexion{
    private String nom;
    private int nombre;
    ArrayList<String> list;


    public Groupe(String nom, int nombre){
        this.nom = nom;
        this.nombre = nombre;
        this.list = new ArrayList<>(this.nombre);
    }

    public void addUser(String nomC){
        this.list.add(nomC);
    }

    public void deleteUser(String nom){
        for (String clt : this.list) {
            if(clt.equals(nom))
                this.list.remove(clt);
        }
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
