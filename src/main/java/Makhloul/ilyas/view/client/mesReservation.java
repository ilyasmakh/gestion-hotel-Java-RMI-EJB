package Makhloul.ilyas.view.client;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.entités.Reservation;
import Makhloul.ilyas.serveur.IServiceHotel;
import javax.swing.text.DateFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class mesReservation extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    private JFormattedTextField txtdateDebut, txtDateFin;


    public mesReservation(int idClient) throws MalformedURLException, NotBoundException, RemoteException {
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
        JLabel titleLabel = new JLabel("Bonjour "+idClient+" ,Gérer vos réservations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ** Inputs **
        JPanel panelInputs = new JPanel();
        panelInputs.setOpaque(false);
        panelInputs.setLayout(new GridLayout(5, 2, 10, 10));

        DateFormatter dateFormatter = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));
        txtdateDebut = new JFormattedTextField(dateFormatter);
        txtDateFin = new JFormattedTextField(dateFormatter);




        panelInputs.add(new JLabel("Date début:")).setForeground(Color.WHITE);
        panelInputs.add(txtdateDebut);
        panelInputs.add(new JLabel("Date fin:")).setForeground(Color.WHITE);
        panelInputs.add(txtDateFin);

        panelInputs.add(new JLabel(""));


        mainPanel.add(panelInputs, BorderLayout.EAST);

        // ** Tableau transparent **
        String[] columnNames = {"ID", "Client ID", "Chambre ID", "Date de Début", "Date de Fin", "Statut","Modifier", "Supprimer"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6;
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
        afficherChambres(idClient);


    }

    private void afficherChambres(int idClient) throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        List<Reservation> reservations = hotel.Reservations(idClient);

        tableModel.setRowCount(0);
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getClient_id(),
                    r.getChambre_id(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getStatus(),
                    createModifierButton(r.getId(),idClient),
                    createSupprimerButton(r.getId(),idClient)
            });
        }
    }


    private JButton createModifierButton(int numero,int id) {
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setOpaque(false);
        btnModifier.setContentAreaFilled(false);
        btnModifier.setForeground(Color.GREEN);
        btnModifier.addActionListener(e -> {
            try {
                modifierReservation(numero,id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return btnModifier;
    }

    private JButton createSupprimerButton(int numero,int id ) {
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setOpaque(false);
        btnSupprimer.setContentAreaFilled(false);
        btnSupprimer.setForeground(Color.RED);
        btnSupprimer.addActionListener(e -> {
            try {
                supprimerReservation(numero,id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return btnSupprimer;
    }

    private void modifierReservation(int numero,int id) throws MalformedURLException, NotBoundException, RemoteException {
        String dateDebut = txtdateDebut.getText();
        String dateFin = txtDateFin.getText();

        if (dateDebut.isEmpty() || dateFin.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les dates pour modifier les dates de réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            // Récupérer l'ID du client à partir de la table
            System.out.println("Modification du réservation id : " + numero);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate debut = LocalDate.parse(dateDebut, formatter);
            LocalDate fin = LocalDate.parse(dateFin, formatter);
            // Récupérer le service hôtel
            IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");

            // Créer un objet Client avec les nouvelles données, y compris le mot de passe
          //  Chambre updatedChambre = new Chambre(type , prix ,disponible);
            Reservation existR = hotel.Reservation(numero);

                Reservation updatedReservation = new Reservation(existR.getClient_id(),existR.getChambre_id(),debut , fin,existR.getStatus());
            // Appeler la méthode modifierClient du service
          hotel.modifierReservation(numero,updatedReservation) ;

            // Rafraîchir la liste des clients après la modification
           afficherChambres(id);
            JOptionPane.showMessageDialog(this, "les date de réservation modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        txtdateDebut.setText("");
        txtDateFin.setText("");
    }

    private void supprimerReservation(int numero,int id) throws MalformedURLException, NotBoundException, RemoteException {
        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");
        boolean b = hotel.supprimerReservation(numero);
        if(b) JOptionPane.showMessageDialog(this, "Réservation  supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        if(!b)  JOptionPane.showMessageDialog(this, "Problem lors de suppression de ceete resevation !.", "Erreur", JOptionPane.ERROR);
        afficherChambres(id);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                mesReservation app = new mesReservation(8132);
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
