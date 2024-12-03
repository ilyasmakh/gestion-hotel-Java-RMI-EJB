package Makhloul.ilyas.entités;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

public class Reservation implements Serializable {
    int id ;
    int client_id ;
    int chambre_id ;
    LocalDate dateDebut ;
    LocalDate dateFin ;
    String status ;

    public int getClient_id() {
        return client_id;
    }

    public int getChambre_id() {
        return chambre_id;
    }

    public String getStatus() {
        return status;
    }

    public Reservation( int client_id, int chambre_id, LocalDate dateDebut, LocalDate dateFin, String status) {

        this.client_id = client_id;
        this.dateDebut = dateDebut;
        this.chambre_id = chambre_id;
        this.dateFin = dateFin;
        this.status = status;
    }
    public Reservation( int client_id, int chambre_id, LocalDate dateDebut, LocalDate dateFin) {

        this.client_id = client_id;
        this.dateDebut = dateDebut;
        this.chambre_id = chambre_id;
        this.dateFin = dateFin;

    }

    public Reservation(int id, int client_id, int chambre_id, LocalDate dateDebut, LocalDate dateFin, String status) {
        this.id = id;
        this.client_id = client_id;
        this.dateDebut = dateDebut;
        this.chambre_id = chambre_id;
        this.dateFin = dateFin;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }
}
