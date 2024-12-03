package Makhloul.ilyas.view.client;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.entités.Reservation;
import Makhloul.ilyas.serveur.IServiceHotel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class reserverChambres extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JFormattedTextField txtdateDebut, txtDateFin;

    public reserverChambres() throws MalformedURLException, NotBoundException, RemoteException {
        setTitle("Espace client");
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
        JLabel titleLabel = new JLabel("Bonjour ilyas makhloul ,choisir des chambres et faire des reservations!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ** Inputs **
        JPanel panelInputs = new JPanel();
        panelInputs.setOpaque(false);
        panelInputs.setLayout(new GridLayout(5, 2, 10, 10));

        // Champs formatés pour les dates
        DateFormatter dateFormatter = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));
        txtdateDebut = new JFormattedTextField(dateFormatter);
        txtDateFin = new JFormattedTextField(dateFormatter);

        panelInputs.add(new JLabel("Date début:")).setForeground(Color.WHITE);
        panelInputs.add(txtdateDebut);
        panelInputs.add(new JLabel("Date fin:")).setForeground(Color.WHITE);
        panelInputs.add(txtDateFin);

        mainPanel.add(panelInputs, BorderLayout.EAST);

        // ** Tableau transparent **
        String[] columnNames = {"Numéro", "Type", "Prix", "Réserver"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }
        };
        table = new JTable(tableModel);
        table.setOpaque(false);
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);


        table.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        table.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setOpaque(false);
        panelTable.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTable, BorderLayout.CENTER);

        // Charger les données des chambres
        afficherChambres();
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
                    createReserverButton(c.getNumChambre()),
            });
        }
        System.out.println("affichage des chambre....");
    }




    // Action pour le bouton Supprimer


    private JButton createReserverButton(int numero) {
        System.out.println("createReserverButton...." + numero);  // Vérifiez si cette ligne est bien exécutée
        JButton btnReserver = new JButton("Réserver");
        btnReserver.setOpaque(false);
        btnReserver.setContentAreaFilled(false);
        btnReserver.setForeground(Color.GREEN);
        btnReserver.setBorder(BorderFactory.createEmptyBorder());
        btnReserver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("Réservation de " + numero);  // Vérifiez si cela s'affiche
                    ReserverChambre(numero);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        return btnReserver;
    }

    private void ReserverChambre(int numero) throws MalformedURLException, NotBoundException, RemoteException {
        String dateDebut = txtdateDebut.getText();
        String dateFin = txtDateFin.getText();

        if (dateDebut.isEmpty() || dateFin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les dates pour réaliser la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Convertir les dates en LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate debut = LocalDate.parse(dateDebut, formatter);
            LocalDate fin = LocalDate.parse(dateFin, formatter);

            // Récupérer le service hôtel
            IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

            // Créer et enregistrer la réservation
            Reservation r = new Reservation(8122, numero, debut, fin);
            int re =hotel.ajouterReservation(r);
            if(re==3)  JOptionPane.showMessageDialog(this, "Réservation avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            if(re==1)  JOptionPane.showMessageDialog(this, "La chambre est déjà réservée pour cette période.", "Erreur", JOptionPane.ERROR_MESSAGE);

            afficherChambres();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la réservation de cette chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Réinitialiser les champs
        txtdateDebut.setText("");
        txtDateFin.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                reserverChambres app = new reserverChambres();
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
