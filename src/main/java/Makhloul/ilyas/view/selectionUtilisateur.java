package Makhloul.ilyas.view;

import javax.swing.*;
import java.awt.*;

public class selectionUtilisateur {

    public selectionUtilisateur() {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Choix Utilisateur");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // Taille ajustée pour une bonne visualisation
        frame.setLayout(new BorderLayout());

        // Ajouter un panneau principal avec un fond personnalisé
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Charger l'image de fond
                ImageIcon background = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/background.png"); // Remplacez par le chemin de votre image
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Utiliser BoxLayout pour centrer le contenu verticalement et horizontalement
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Disposition en colonne (verticale)

        // Ajouter un espace vide pour centrer le contenu verticalement
        mainPanel.add(Box.createVerticalGlue());  // Cette ligne permet de centrer le contenu verticalement

        // Ajouter la phrase "Vous êtes ?" au centre
        JLabel titleLabel = new JLabel("Vous êtes ?", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.white); // Couleur du texte
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        // Créer un panneau pour les boutons (transparent)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Rendre le panneau transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centrer les boutons avec espacement

        // Bouton "Client" avec icône
        JButton clientButton = new JButton("Client");
        ImageIcon clientIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/client.png");
        clientIcon = new ImageIcon(clientIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        clientButton.setIcon(clientIcon);
        clientButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clientButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clientButton.setFocusPainted(false);
        clientButton.setOpaque(false); // Rendre le bouton transparent
        clientButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        clientButton.setBorderPainted(false);
        clientButton.setForeground(Color.WHITE);// Enlever la bordure du bouton

        // Bouton "Employé" avec icône
        JButton employeeButton = new JButton("Employé");
        ImageIcon employeeIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/employee.png");
        employeeIcon = new ImageIcon(employeeIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        employeeButton.setIcon(employeeIcon);
        employeeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        employeeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        employeeButton.setFocusPainted(false);
        employeeButton.setOpaque(false); // Rendre le bouton transparent
        employeeButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        employeeButton.setBorderPainted(false);
        employeeButton.setForeground(Color.WHITE);// Enlever la bordure du bouton

        // Ajouter des actions aux boutons
        clientButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Vous êtes un client !"));
        employeeButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Vous êtes un employé !"));

        // Ajouter les boutons au panneau
        buttonPanel.add(clientButton);
        buttonPanel.add(employeeButton);

        // Ajouter les boutons au panneau principal
        mainPanel.add(buttonPanel);

        // Ajouter un espace vide supplémentaire pour que le contenu soit centré verticalement
        mainPanel.add(Box.createVerticalGlue());

        // Ajouter le panneau principal à la fenêtre
        frame.add(mainPanel);

        // Afficher la fenêtre
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new selectionUtilisateur();
    }
}
