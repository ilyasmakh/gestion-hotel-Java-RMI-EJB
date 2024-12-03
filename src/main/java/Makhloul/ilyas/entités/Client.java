package Makhloul.ilyas.entités;

import java.io.Serializable;

public class Client implements Serializable {
    int id;
    String nom
            ;
    String prenom ;
    String email ;
    String password ;

    public Client(int id ,  String nom, String prenom, String email , String password) {
       this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }

    public Client( String nom, String prenom, String email , String password) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }
}
