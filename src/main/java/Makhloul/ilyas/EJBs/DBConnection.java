package Makhloul.ilyas.EJBs;

import Makhloul.ilyas.entités.Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Informations de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_hotel"; // Remplacez par votre URL de base de données
    private static final String USER = "root"; // Remplacez par votre nom d'utilisateur MySQL
    private static final String PASSWORD = ""; // Remplacez par votre mot de passe MySQL

    // Méthode pour obtenir la connexion à la base de données
    public static Connection getConnection() {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver"); // Assurez-vous que le driver est dans votre classpath
            System.out.println("salut");
            // Retourner la connexion
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("erreur2");
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        // Connection conn = DBConnection.getConnection();


    }
}
