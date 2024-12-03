package Makhloul.ilyas.view.employee;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.serveur.IServiceHotel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class gestionChambres extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField  txtType, txtPrix;
    private JCheckBox checkDisponibilite;
    private JButton btnAjouter;

    public gestionChambres() throws MalformedURLException, NotBoundException, RemoteException {
        setTitle("Gestion des Chambres");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // ** Image de fond **
        String backgroundPath = "C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/background2.png";
        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon(backgroundPath).getImage());
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // ** Titre **
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Gestion des Chambres");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ** Inputs **
        JPanel panelInputs = new JPanel();
        panelInputs.setOpaque(false);
        panelInputs.setLayout(new GridLayout(5, 2, 10, 10));


        txtType = new JTextField();
        txtPrix = new JTextField();
        checkDisponibilite = new JCheckBox("Disponible");
        checkDisponibilite.setOpaque(false);
        checkDisponibilite.setForeground(Color.WHITE);

        btnAjouter = new JButton("");
        btnAjouter.setIcon(new ImageIcon("C:/Users/pc/IdeaProjects/gestionHotel_RMI_EJB/src/main/java/Makhloul/ilyas/resource/ajouter.png"));
        btnAjouter.setOpaque(false);
        btnAjouter.setContentAreaFilled(false);
        btnAjouter.setBorder(BorderFactory.createEmptyBorder());


        panelInputs.add(new JLabel("Type:")).setForeground(Color.WHITE);
        panelInputs.add(txtType);
        panelInputs.add(new JLabel("Prix:")).setForeground(Color.WHITE);
        panelInputs.add(txtPrix);
        panelInputs.add(new JLabel("Disponibilité:")).setForeground(Color.WHITE);
        panelInputs.add(checkDisponibilite);
        panelInputs.add(new JLabel(""));
        panelInputs.add(btnAjouter);

        mainPanel.add(panelInputs, BorderLayout.EAST);

        // ** Tableau transparent **
        String[] columnNames = {"Numéro", "Type", "Prix", "Disponibilité", "Modifier", "Supprimer"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4;
            }
        };
        table = new JTable(tableModel);
        table.setOpaque(false);
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);

        table.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
        table.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
        table.getColumn("Modifier").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Supprimer").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setOpaque(false);
        panelTable.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTable, BorderLayout.CENTER);

        // Charger les données des chambres
        afficherChambres();

        // Ajouter une chambre
        btnAjouter.addActionListener(e -> {
            try {
                ajouterChambre();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void afficherChambres() throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        List<Chambre> chambres = hotel.Chambres();

        tableModel.setRowCount(0);
        for (Chambre c : chambres) {
            tableModel.addRow(new Object[]{
                    c.getNumChambre(),
                    c.getType(),
                    c.getPrix(),
                    c.isDisponibilite() ? "Oui" : "Non",
                    createModifierButton(c.getNumChambre()),
                    createSupprimerButton(c.getNumChambre())
            });
        }
    }

    private void ajouterChambre() throws MalformedURLException, NotBoundException, RemoteException {

        String type = txtType.getText();
        double prix = Double.parseDouble(txtPrix.getText());
        boolean disponible = checkDisponibilite.isSelected();

        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        hotel.ajouterChambre(new Chambre(type, prix, disponible));
        afficherChambres();


        txtType.setText("");
        txtPrix.setText("");
        checkDisponibilite.setSelected(false);
    }

    private JButton createModifierButton(int numero) {
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setOpaque(false);
        btnModifier.setContentAreaFilled(false);
        btnModifier.setForeground(Color.GREEN);
        btnModifier.addActionListener(e -> {
            try {
                modifierChambre(numero);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return btnModifier;
    }

    private JButton createSupprimerButton(int numero) {
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setOpaque(false);
        btnSupprimer.setContentAreaFilled(false);
        btnSupprimer.setForeground(Color.RED);
        btnSupprimer.addActionListener(e -> {
            try {
                supprimerChambre(numero);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return btnSupprimer;
    }

    private void modifierChambre(int numero) throws MalformedURLException, NotBoundException, RemoteException {
        String type = txtType.getText();
        double prix = 0 ;
       if(!txtPrix.getText().isEmpty() ) prix = Double.parseDouble(txtPrix.getText());
        boolean disponible = checkDisponibilite.isSelected();

        if (type.isEmpty() || txtPrix.getText().isEmpty() ) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Récupérer l'ID du client à partir de la table
           System.out.println("Modification du chmabre num : " + numero);

            // Récupérer le service hôtel
            IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

            // Créer un objet Client avec les nouvelles données, y compris le mot de passe
            Chambre updatedChambre = new Chambre(type , prix ,disponible);

            // Appeler la méthode modifierClient du service
            hotel.modifierChambre(numero, updatedChambre);

            // Rafraîchir la liste des clients après la modification
            afficherChambres();
            JOptionPane.showMessageDialog(this, "Chambre modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Réinitialiser les champs
        txtType.setText("");
        txtPrix.setText("");
        checkDisponibilite.setSelected(false);
    }

    private void supprimerChambre(int numero) throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        boolean b = hotel.supprimerChambre(numero);
        if(b) JOptionPane.showMessageDialog(this, "chmabre supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        if(!b)  JOptionPane.showMessageDialog(this, "cette chambre est reservé - suppression impossible!.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        afficherChambres();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                gestionChambres app = new gestionChambres();
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

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

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value;
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(false);
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
