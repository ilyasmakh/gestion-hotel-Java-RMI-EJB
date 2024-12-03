package Makhloul.ilyas.view.employee;

import Makhloul.ilyas.entités.Reservation;
import Makhloul.ilyas.serveur.IServiceHotel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

class gestionReservations extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public gestionReservations() throws MalformedURLException, NotBoundException, RemoteException {
        setTitle("Gestion des Réservations");
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
        JLabel titleLabel = new JLabel("Gestion des Réservations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ** Tableau transparent **
        String[] columnNames = {"ID", "Client ID", "Chambre ID", "Date de Début", "Date de Fin", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the "Statut" column is editable
            }
        };
        table = new JTable(tableModel);
        table.setOpaque(false);
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setOpaque(false);
        panelTable.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTable, BorderLayout.CENTER);

        // Charger les données des réservations
        afficherReservations();
    }

    private void afficherReservations() throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        List<Reservation> reservations = hotel.Reservations();

        tableModel.setRowCount(0);
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getClient_id(),
                    r.getChambre_id(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getStatus(),
            });
        }

        // Assigner un ComboBoxEditor à la colonne "Statut"

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Confirmée", "Annulée"});
        // Ajouter un ItemListener pour afficher le changement de statut
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Récupérer la ligne correspondante à l'élément sélectionné
                int row = table.getEditingRow(); // La ligne en cours d'édition
                if (row != -1) {
                    // Récupérer l'ID de la réservation depuis la ligne sélectionnée
                    int reservationId =(int) tableModel.getValueAt(row, 0); // L'ID est dans la première colonne
                    String newStatus = (String) e.getItem();
                   if(newStatus.equals("Annulée")) {
                       try {
                           hotel.annulerReservation(reservationId);
                       } catch (RemoteException ex) {
                           throw new RuntimeException(ex);
                       }
                   }
                   else if(newStatus.equals("Confirmée")) {
                       try {
                           hotel.confirmerReservation(reservationId);
                       } catch (RemoteException ex) {
                           throw new RuntimeException(ex);
                       }
                   }
                    // Afficher l'ID de la réservation et le nouveau statut dans la console
                    System.out.println("Le statut de la réservation (ID: " + reservationId + ") a été changé en : " + newStatus);
                }
            }
        });
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox));

        table.getColumnModel().getColumn(5).setCellRenderer(new ComboBoxRenderer());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new gestionReservations().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
    public ComboBoxRenderer() {
        super(new String[]{"Confirmée", "Annulée"});
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelectedItem(value);
        return this;
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
