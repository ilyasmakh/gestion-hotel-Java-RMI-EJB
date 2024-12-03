package Makhloul.ilyas.view.employee;
import Makhloul.ilyas.entités.Client;
import Makhloul.ilyas.serveur.IServiceHotel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class gestionClients extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrenom, txtEmail, txtPassword;
    private JButton btnAjouter;
    // Rendu des cellules de la table en blanc


    public gestionClients() throws MalformedURLException, NotBoundException, RemoteException {

        setTitle("Gestion des Clients");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // ** Charger l'image de fond **
        String backgroundPath = "C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/background2.png";
        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon(backgroundPath).getImage());
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Panel de titre
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Gestion des Clients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.white); // Change la couleur du texte du titre
        titlePanel.add(titleLabel);

        // ** Panel pour les inputs et le bouton Ajouter **
        JPanel panelInputs = new JPanel();
        panelInputs.setOpaque(false); // Rendre le panel transparent
        panelInputs.setLayout(new GridLayout(6, 2, 10, 10)); // Ajouter une ligne pour le titre

        // Initialisation des champs d'entrée
        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();  // Pour plus de sécurité, utiliser JPasswordField pour le mot de passe

        // Bouton "Ajouter" avec une icône et un fond transparent
        btnAjouter = new JButton("");
       btnAjouter.setIcon(new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/ajouter.png")); // Remplacez par le chemin de votre icône
        btnAjouter.setOpaque(false);
        btnAjouter.setContentAreaFilled(false);
        btnAjouter.setBorder(BorderFactory.createEmptyBorder());
        btnAjouter.setHorizontalAlignment(SwingConstants.LEFT);

        // Ajouter les labels et champs de texte
        JLabel labelNom = new JLabel("Nom:");
        labelNom.setForeground(Color.WHITE);
        panelInputs.add(labelNom);
        panelInputs.add(txtNom);
        JLabel labelPrenom = new JLabel("Prenom:");
        labelPrenom.setForeground(Color.WHITE);
        panelInputs.add(labelPrenom);
        panelInputs.add(txtPrenom);
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setForeground(Color.WHITE);
        panelInputs.add(labelEmail);

        panelInputs.add(txtEmail);
        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setForeground(Color.WHITE);
        panelInputs.add(labelPassword);
        panelInputs.add(txtPassword);

        // Ajouter un espace vide
        panelInputs.add(new JLabel());
        panelInputs.add(btnAjouter);
        // Ajouter le titre au panneau principal dans la zone NORTH
        mainPanel.add(titlePanel, BorderLayout.NORTH);
// Ajouter les champs d'entrée dans la zone EAST
        mainPanel.add(panelInputs, BorderLayout.EAST);

        // ** Création du tableau transparent **
        String[] columnNames = {"Client ID", "Nom", "Prénom", "Email", "Password", "Modifier", "Supprimer"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 5; // Seules les colonnes "Modifier" et "Supprimer" sont éditables
            }
        };
        table = new JTable(tableModel);
        // Rendu des cellules de la table en blanc
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
           // Fond blanc
                c.setForeground(Color.white);  // Texte en noir
                return c;
            }
        });

        table.setOpaque(false); // Rendre le tableau transparent
        table.setFillsViewportHeight(true);

        // Rendre la vue du tableau transparente
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);

        // Ajouter des boutons Modifier et Supprimer avec des icônes
        table.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
        table.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());

        // Ajouter un TableCellEditor pour les boutons
        table.getColumn("Modifier").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Supprimer").setCellEditor(new ButtonEditor(new JCheckBox()));

        // JScrollPane transparent
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Ajouter le tableau à gauche (60% de la largeur)
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setOpaque(false); // Transparent
        panelTable.setPreferredSize(new Dimension(600, getHeight())); // 60% de largeur

        panelTable.add(scrollPane, BorderLayout.CENTER);

        // Ajouter le panel du tableau au centre
        mainPanel.add(panelTable, BorderLayout.CENTER);
        afficherClients();
        // ** Listener pour le bouton Ajouter **
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ajouterClient();
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void afficherClients() throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

        // Récupérer la liste des clients après la suppression
        List<Client> cls = hotel.Clients();

        // Vider le tableau avant de le remplir avec de nouvelles données
        tableModel.setRowCount(0);

        // Ajouter chaque client au tableau
        for (int i = 0; i < cls.size(); i++) {
            Client c = cls.get(i);

            // Ajouter une nouvelle ligne avec les données du client
            Object[] newRow = {
                    c.getId(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getEmail(),
                    c.getPassword(),
                    createModifierButton(i),
                    createSupprimerButton(i) // Associer le bon index
            };

            tableModel.addRow(newRow);

        }
    }

    private void ajouterClient() throws MalformedURLException, NotBoundException, RemoteException {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        hotel.ajouterClient(new Client(nom,prenom , email , password));
        afficherClients();
        // Réinitialiser les champs
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    // Créer le bouton "Modifier" avec une icône
    private JButton createModifierButton(int row) {
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setOpaque(false);
        btnModifier.setContentAreaFilled(false);
        btnModifier.setBorder(BorderFactory.createEmptyBorder());
        btnModifier.setForeground(Color.green);
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modifierClient(row  );
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return btnModifier;
    }

    private JButton createSupprimerButton(final int rowIndex) {
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setOpaque(false);
        btnSupprimer.setContentAreaFilled(false);
        btnSupprimer.setForeground(Color.red);
        btnSupprimer.setBorder(BorderFactory.createEmptyBorder());
        // Action pour le bouton Supprimer
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clientId = (int) tableModel.getValueAt(rowIndex, 0);  // Récupère l'ID de la première colonne
                System.out.println("Suppression du client avec l'ID : " + clientId); // Log pour vérifier l'ID
                try {
                    // Supprimer le client
                    supprimerClient(clientId);
                    System.out.println("Client supprimé avec succès");
                    tableModel.removeRow(rowIndex); // Supprime la ligne à l'index spécifié

                    // Mettre à jour le tableau
                    afficherClients(); // Rafraîchir l'affichage après la suppression
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (NotBoundException ex) {
                    ex.printStackTrace();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return btnSupprimer;
    }

    // Méthode pour modifier un client
    private void modifierClient(int row) throws RemoteException, MalformedURLException, NotBoundException {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Récupérer l'ID du client à partir de la table
            int clientId = (int) tableModel.getValueAt(row, 0);  // ID du client à partir de la première colonne
            System.out.println("Modification du client avec l'ID : " + clientId);

            // Récupérer le service hôtel
            IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

            // Créer un objet Client avec les nouvelles données, y compris le mot de passe
            Client updatedClient = new Client(clientId, nom, prenom, email, password);

            // Appeler la méthode modifierClient du service
            hotel.modifierClient(clientId, updatedClient);

            // Rafraîchir la liste des clients après la modification
            afficherClients();
            JOptionPane.showMessageDialog(this, "Client modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Réinitialiser les champs
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void supprimerClient(int clientId) throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

        boolean b = hotel.supprimerClient(clientId);
        if(b)  JOptionPane.showMessageDialog(this, "Client supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        if(b==false) JOptionPane.showMessageDialog(this, "ce client a des reservation - suppression impossible!");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gestionClients app = null;
            try {
                app = new gestionClients();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            app.setVisible(true);
        });
    }

    // ** Classe pour dessiner un JPanel avec une image de fond **
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ** Rendu des boutons dans les cellules de tableau **
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value;
            }
            return this;
        }
    }

    // ** Éditeur de boutons pour l'action des boutons Modifier et Supprimer **
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
            }
            return button;
        }
    }
}
