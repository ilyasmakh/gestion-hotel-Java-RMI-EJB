package Makhloul.ilyas.entités;

import java.io.Serializable;

public class Chambre implements Serializable {
    int numChambre;
    String type ;
    double prix ;
    boolean disponibilite ;
    public Chambre( String type, double prix , boolean disponibilite ) {

        this.type = type;
        this.prix = prix;
        this.disponibilite = disponibilite;
    }



    public Chambre(int numChambre, String type, double prix,boolean disponibilite) {
        this.numChambre = numChambre;
        this.type = type;
        this.prix = prix;
        this.disponibilite = disponibilite;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setNumChambre(int numChambre) {
        this.numChambre = numChambre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getNumChambre() {
        return numChambre;
    }

    public String getType() {
        return type;
    }

    public double getPrix() {
        return prix;
    }
}
