package Makhloul.ilyas.view.client;

import Makhloul.ilyas.EJBs.IClientSessionRemote;
import Makhloul.ilyas.EJBs.IclientAuthentificationRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class Authentification {

    public Authentification() {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Authentification Client");
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
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Ajouter un espace vide pour centrer le contenu verticalement
        mainPanel.add(Box.createVerticalGlue());

        // Ajouter un grand titre
        JLabel titleLabel = new JLabel("Bienvenue cher client !");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Grande police en gras
        titleLabel.setForeground(Color.white); // Couleur blanche
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        mainPanel.add(titleLabel);

        // Ajouter un espace entre le titre et le formulaire
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Ajouter un panneau centré contenant le formulaire
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false); // Rendre le panneau transparent
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Disposition verticale

        // Ajouter des champs de texte et des labels
        JLabel emailLabel = new JLabel("E-mail :");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        emailLabel.setForeground(Color.white);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement

        JTextField emailField = new JTextField(15); // Champ plus petit
        emailField.setMaximumSize(new Dimension(200, 30)); // Limiter la taille du champ
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.white);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement

        JPasswordField passwordField = new JPasswordField(15); // Champ plus petit
        passwordField.setMaximumSize(new Dimension(200, 30)); // Limiter la taille du champ
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement

        // Ajouter un bouton
        JButton connectButton = new JButton("Connexion");
        connectButton.setFont(new Font("Arial", Font.BOLD, 16));
        connectButton.setBackground(new Color(139, 69, 19)); // Couleur marron
        connectButton.setForeground(Color.white); // Texte en blanc
        connectButton.setFocusPainted(false); // Enlever la bordure de focus
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        connectButton.setMaximumSize(new Dimension(150, 40)); // Taille fixe du bouton

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bonjour");
                String email = emailField.getText();
                String password = passwordField.getText();

                if (email.isEmpty() || password.isEmpty() )
                    JOptionPane.showMessageDialog(mainPanel, "Veuillez remplir tous les champs!", "Erreur", JOptionPane.ERROR_MESSAGE);
                else {


                    try {


                        Hashtable<String, String> env = new Hashtable<>();
                        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                        env.put(Context.PROVIDER_URL, "http-remoting://localhost:8080"); // URL de votre WildFly
                        env.put(Context.SECURITY_PRINCIPAL, "root"); // Nom d'utilisateur si nécessaire
                        env.put(Context.SECURITY_CREDENTIALS, "Ilyas2024"); // Mot de passe si nécessaire



                        // Initialisation du contexte JNDI
                        InitialContext ctx = new InitialContext(env);
                        IclientAuthentificationRemote authService = (IclientAuthentificationRemote) ctx.lookup("java:global/gestionHotel_RMI_EJB/ClientAuthentificationBean!Makhloul.ilyas.IclientAuthentificationRemote");

                        IClientSessionRemote userSession = (IClientSessionRemote) ctx.lookup("java:global/gestionHotel_RMI_EJB/ClientSessionBean!Makhloul.ilyas.IClientSessionRemote");
                        int  clientId = authService.Sauthentifier(email , password);
                        if(clientId==0)  JOptionPane.showMessageDialog(mainPanel, "Erreur de connection!", "Erreur", JOptionPane.ERROR_MESSAGE);
                        else if(clientId==-1)   JOptionPane.showMessageDialog(mainPanel, "Email ou password incorrect!", "Erreur", JOptionPane.ERROR_MESSAGE);
                        else {
                            System.out.println("Bienvenur Monsieur "+clientId+" !");
                            userSession.seConnecter(clientId);
                        }


                    } catch (NamingException ex) {
                        throw new RuntimeException(ex);
                    }



                }
            }
        });

        // Ajouter les composants au panneau de formulaire
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement vertical
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espacement vertical
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement vertical
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Espacement vertical
        formPanel.add(connectButton);

        // Ajouter le panneau de formulaire au panneau principal
        mainPanel.add(formPanel);

        // Ajouter un espace vide supplémentaire pour centrer le contenu verticalement
        mainPanel.add(Box.createVerticalGlue());

        // Ajouter le panneau principal à la fenêtre
        frame.add(mainPanel);

        // Afficher la fenêtre
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Authentification();
    }
}
