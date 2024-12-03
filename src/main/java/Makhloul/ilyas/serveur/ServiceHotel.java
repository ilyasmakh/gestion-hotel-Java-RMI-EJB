package Makhloul.ilyas.serveur;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.entités.Client;
import Makhloul.ilyas.entités.Reservation;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceHotel extends UnicastRemoteObject implements IServiceHotel, Serializable {
    public  ServiceHotel() throws RemoteException {
    }


    @Override
    public boolean ajouterClient(Client client) throws RemoteException {
        String query = "INSERT INTO clients (nom, prenom, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPassword());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Client ajouté avec succès");
                return true;
            } else {
                System.out.println("Aucune ligne insérée");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception");
            return false;
        }
    }


    @Override
    public boolean supprimerClient(int id) throws RemoteException {
        // Vérifier si le client a des réservations avant de procéder à la suppression
        if (aDesReservations(id)) {
            System.out.println("Le client avec l'ID " + id + " a des réservations. La suppression est impossible.");
            return false;  // Retourner false si le client a des réservations
        }

        // Si le client n'a pas de réservations, procéder à la suppression
        String query = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Si des lignes sont affectées, la suppression a réussi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour vérifier si un client a des réservations
    private boolean aDesReservations(int clientId) throws RemoteException {
        String query = "SELECT COUNT(*) FROM reservations WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;  // Si le compte est supérieur à 0, cela signifie que le client a des réservations
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Aucun enregistrement trouvé pour ce client
    }


    @Override
    public boolean modifierClient(int id ,Client client) throws RemoteException {
        String query = "UPDATE clients SET nom = ?, prenom = ?, email = ?, password=? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPassword());
            stmt.setInt(5, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Client> Clients()  throws RemoteException{
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("password"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public boolean ajouterChambre(Chambre chambre) throws RemoteException {
        String query = "INSERT INTO chambres (num_chambre, type_chambre, prix,disponibilite) VALUES (?, ?, ?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, chambre.getNumChambre());
            stmt.setString(2, chambre.getType());
            stmt.setDouble(3, chambre.getPrix());
            stmt.setBoolean(4, chambre.isDisponibilite());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimerChambre(int numChambre) throws RemoteException {
        String checkReservationQuery = "SELECT COUNT(*) FROM reservations WHERE chambre_id = ?";
        String deleteChambreQuery = "DELETE FROM chambres WHERE num_chambre = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Vérifier si la chambre est réservée
            try (PreparedStatement checkStmt = conn.prepareStatement(checkReservationQuery)) {
                checkStmt.setInt(1, numChambre);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // La chambre est réservée
                    System.out.println("La chambre numéro " + numChambre + " est réservée et ne peut pas être supprimée.");
                    return false; // Indiquer que la suppression n'a pas été effectuée
                }
            }

            // Supprimer la chambre si elle n'est pas réservée
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteChambreQuery)) {
                deleteStmt.setInt(1, numChambre);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean modifierChambre(int numChambre , Chambre chambre)  throws RemoteException {
        String query = "UPDATE chambres SET type_chambre = ?, prix = ?,disponibilite=? WHERE num_chambre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, chambre.getType());
            stmt.setDouble(2, chambre.getPrix());
            stmt.setBoolean(3, chambre.isDisponibilite());
            stmt.setInt(4, numChambre);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Chambre> Chambres() throws RemoteException {
        List<Chambre> chambres = new ArrayList<>();
        String query = "SELECT * FROM chambres";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Chambre chambre = new Chambre(rs.getInt("num_chambre"), rs.getString("type_chambre"), rs.getDouble("prix"),rs.getBoolean("disponibilite"));
                chambres.add(chambre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chambres;
    }

    @Override
    public int ajouterReservation(Reservation r) throws RemoteException {
        System.out.println("Ajout de reservation...");
        String checkClientQuery = "SELECT COUNT(*) FROM clients WHERE id = ?";
        String checkChambreQuery = "SELECT COUNT(*) FROM reservations " +
                "WHERE chambre_id = ? AND (" +
                "(date_debut <= ? AND date_fin >= ?) OR " +
                "(date_debut <= ? AND date_fin >= ?) OR " +
                "(date_debut >= ? AND date_fin <= ?) OR " +
                "(date_debut <= ? AND date_fin >= ?))";
        String insertReservationQuery = "INSERT INTO reservations (client_id, chambre_id, date_debut, date_fin) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Démarrer une transaction

            // Étape 1 : Vérifier si le client existe
            System.out.println("Vérification de l'existence du client...");
            try (PreparedStatement checkClientStmt = conn.prepareStatement(checkClientQuery)) {
                checkClientStmt.setInt(1, r.getClient_id());
                ResultSet rsClient = checkClientStmt.executeQuery();
                if (rsClient.next() && rsClient.getInt(1) == 0) {
                    System.out.println("Le client avec l'ID " + r.getClient_id() + " n'existe pas.");
                    conn.rollback();
                    return 0;
                }
            }

            // Étape 2 : Vérifier s'il y a un chevauchement pour la chambre demandée
            System.out.println("Vérification de la disponibilité de la chambre...");
            try (PreparedStatement checkChambreStmt = conn.prepareStatement(checkChambreQuery)) {
                checkChambreStmt.setInt(1, r.getChambre_id());
                checkChambreStmt.setDate(2, Date.valueOf(r.getDateDebut())); // date_debut <= r.dateDebut
                checkChambreStmt.setDate(3, Date.valueOf(r.getDateDebut())); // date_fin >= r.dateDebut
                checkChambreStmt.setDate(4, Date.valueOf(r.getDateFin()));   // date_debut <= r.dateFin
                checkChambreStmt.setDate(5, Date.valueOf(r.getDateFin()));   // date_fin >= r.dateFin
                checkChambreStmt.setDate(6, Date.valueOf(r.getDateDebut())); // date_debut >= r.dateDebut
                checkChambreStmt.setDate(7, Date.valueOf(r.getDateFin()));   // date_fin <= r.dateFin
                checkChambreStmt.setDate(8, Date.valueOf(r.getDateDebut())); // date_debut <= r.dateDebut
                checkChambreStmt.setDate(9, Date.valueOf(r.getDateFin()));   // date_fin >= r.dateFin

                ResultSet rsChambre = checkChambreStmt.executeQuery();
                if (rsChambre.next() && rsChambre.getInt(1) > 0) {
                    System.out.println("La chambre est déjà réservée pour cette période.");
                    conn.rollback();
                    return 1;
                }
            }

            // Étape 3 : Ajouter la réservation
            System.out.println("Ajout de la réservation...");
            try (PreparedStatement insertStmt = conn.prepareStatement(insertReservationQuery)) {
                insertStmt.setInt(1, r.getClient_id());
                insertStmt.setInt(2, r.getChambre_id());
                insertStmt.setDate(3, Date.valueOf(r.getDateDebut()));
                insertStmt.setDate(4, Date.valueOf(r.getDateFin()));

                if (insertStmt.executeUpdate() <= 0) {
                    System.out.println("Échec de l'insertion dans la table des réservations.");
                    conn.rollback();
                    return 2;
                }
            }

            conn.commit(); // Valider la transaction
            System.out.println("Réservation ajoutée avec succès !");
            return 3;

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
            e.printStackTrace();
            return 4;
        }
    }

    @Override
    public boolean confirmerReservation(int r)  throws RemoteException{
        String query = "UPDATE reservations SET statut = 'confirmée' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, r);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean annulerReservation(int r)  throws RemoteException{
        String query = "UPDATE reservations SET statut = 'annulée' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, r);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifierReservation(int reservationId , Reservation r)  throws RemoteException{
        String query = "UPDATE reservations SET date_debut = ?, date_fin = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1,Date.valueOf(   r.getDateDebut()));
            stmt.setDate(2,Date.valueOf( r.getDateFin()));
            stmt.setInt(3, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reservation> Reservations()  throws RemoteException{
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reservation reservation = new Reservation(rs.getInt("id"), rs.getInt("client_id"), rs.getInt("chambre_id"),
                        rs.getDate("date_debut").toLocalDate(), rs.getDate("date_fin").toLocalDate(), rs.getString("statut"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public List<Reservation> Reservations(int ClientId) throws RemoteException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Remplacer le ? par le client_id passé en paramètre
            stmt.setInt(1, ClientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("chambre_id"),
                            rs.getDate("date_debut").toLocalDate(),
                            rs.getDate("date_fin").toLocalDate(),
                            rs.getString("statut")
                    );
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public boolean supprimerReservation(int id) throws RemoteException {


        // Si le client n'a pas de réservations, procéder à la suppression
        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Si des lignes sont affectées, la suppression a réussi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reservation Reservation(int id) throws RemoteException {
        Reservation reservation = null;
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Remplacer le ? par l'ID de la réservation
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si une réservation est trouvée, la créer et la retourner
                    reservation = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("chambre_id"),
                            rs.getDate("date_debut").toLocalDate(),
                            rs.getDate("date_fin").toLocalDate(),
                            rs.getString("statut")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

/*
    @Override
    public double Facture(int ClientId)throws RemoteException {
        List<Reservation> rs =  Reservations(ClientId);
        return 0 ;
    }*/

}
