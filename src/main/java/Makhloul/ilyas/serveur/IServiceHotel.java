package Makhloul.ilyas.serveur;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.entités.Client;
import Makhloul.ilyas.entités.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface IServiceHotel extends Remote {

    public boolean ajouterClient(Client client) throws RemoteException;;
    public boolean supprimerClient(int id) throws RemoteException ;;
    public boolean modifierClient(int id, Client client) throws RemoteException ;;
    public List<Client> Clients() throws RemoteException ;;

    public boolean ajouterChambre(Chambre chambre) throws RemoteException ;;
    public boolean supprimerChambre(int numChambre) throws RemoteException ;;
    public boolean modifierChambre(int numChambre ,Chambre chambre) throws RemoteException ;;
    public List<Chambre> Chambres() throws RemoteException ;;

    public int ajouterReservation(Reservation r) throws RemoteException ;;
    public boolean confirmerReservation(int reservationId)throws RemoteException ;;
    public boolean  annulerReservation(int reservationId) throws RemoteException ;;
    public boolean  modifierReservation(int reservationId,Reservation r) throws RemoteException ;;
    public List<Reservation> Reservations() throws RemoteException ;;
    public List<Reservation> Reservations(int idClient) throws RemoteException ;;
    public boolean supprimerReservation(int id) throws RemoteException ;;

    public Reservation Reservation(int id) throws RemoteException ;;
   /*
    public double Facture(int ClientId)throws RemoteException ;*/
}
