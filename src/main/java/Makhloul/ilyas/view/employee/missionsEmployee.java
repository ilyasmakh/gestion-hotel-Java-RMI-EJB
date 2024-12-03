package Makhloul.ilyas.view.employee;

import javax.swing.*;
import java.awt.*;

public class missionsEmployee {

    public missionsEmployee () {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Espace employée");
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

        // Ajouter la phrase "Qu'est ce que vous voulez faire ?" au centre
        JLabel titleLabel = new JLabel("Qu'est ce que vous voulez faire ?", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.white); // Couleur du texte
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        // Créer un panneau pour les boutons (transparent)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Rendre le panneau transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centrer les boutons avec espacement

        // Bouton "Gérer les clients" avec icône
        JButton clientButton = new JButton("Gérer les clients");
        ImageIcon clientIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/client.png");
        clientIcon = new ImageIcon(clientIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        clientButton.setIcon(clientIcon);
        clientButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clientButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clientButton.setFocusPainted(false);
        clientButton.setOpaque(false); // Rendre le bouton transparent
        clientButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        clientButton.setBorderPainted(false); // Enlever la bordure du bouton
        clientButton.setForeground(Color.WHITE);

        // Bouton "Gérer les réservations" avec icône
        JButton employeeButton = new JButton("Gérer les réservations");
        ImageIcon employeeIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/employee.png");
        employeeIcon = new ImageIcon(employeeIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        employeeButton.setIcon(employeeIcon);
        employeeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        employeeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        employeeButton.setFocusPainted(false);
        employeeButton.setOpaque(false); // Rendre le bouton transparent
        employeeButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        employeeButton.setBorderPainted(false); // Enlever la bordure du bouton
        employeeButton.setForeground(Color.WHITE);

        // Bouton "Gérer les chambres" avec icône
        JButton roomButton = new JButton("Gérer les chambres");
        ImageIcon roomIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/chambre.PNG");
        roomIcon = new ImageIcon(roomIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        roomButton.setIcon(roomIcon);
        roomButton.setHorizontalTextPosition(SwingConstants.CENTER);
        roomButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        roomButton.setFocusPainted(false);
        roomButton.setOpaque(false); // Rendre le bouton transparent
        roomButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        roomButton.setBorderPainted(false); // Enlever la bordure du bouton
        roomButton.setForeground(Color.WHITE);

        // Nouveau bouton "Accéder aux rapports" avec icône
        JButton reportButton = new JButton("Accéder aux rapports");
        ImageIcon reportIcon = new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/rapport.png"); // Remplacez par votre icône de rapport
        reportIcon = new ImageIcon(reportIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Redimensionner l'icône
        reportButton.setIcon(reportIcon);
        reportButton.setHorizontalTextPosition(SwingConstants.CENTER);
        reportButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        reportButton.setFocusPainted(false);
        reportButton.setOpaque(false); // Rendre le bouton transparent
        reportButton.setContentAreaFilled(false); // Ne pas remplir l'aire du bouton
        reportButton.setBorderPainted(false); // Enlever la bordure du bouton
        reportButton.setForeground(Color.WHITE);

        // Ajouter des actions aux boutons
        clientButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Gestion des clients"));
        employeeButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Gestion des réservations"));
        roomButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Gestion des chambres"));
        reportButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Accès aux rapports"));

        // Ajouter les boutons au panneau
        buttonPanel.add(clientButton);
        buttonPanel.add(employeeButton);
        buttonPanel.add(roomButton);
        buttonPanel.add(reportButton); // Ajouter le nouveau bouton au panneau

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
        new missionsEmployee();
    }
}
