package Makhloul.ilyas.EJBs;


import jakarta.ejb.Stateless;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Stateless
public class ClientAuthentificationBean implements IclientAuthentificationRemote {
    @Override
    public int Sauthentifier(String email, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Obtenez la connexion via DBConnection
            connection = DBConnection.getConnection();

            if (connection == null) {
                System.out.println("Impossible de se connecter à la base de données.");
                return 0;
            }

            // Requête SQL pour vérifier l'email et le mot de passe
            String sql = "SELECT id FROM clients WHERE email = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Exécutez la requête
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Si une correspondance est trouvée, retournez l'ID du client
                return resultSet.getInt("id");
            } else {
                // Si aucune correspondance n'est trouvée, retournez 0
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            // Fermez les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
