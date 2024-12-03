package Makhloul.ilyas;

import Makhloul.ilyas.entités.Chambre;
import Makhloul.ilyas.entités.Client;
import Makhloul.ilyas.serveur.IServiceHotel;
import Makhloul.ilyas.serveur.ServiceHotel;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {


        IServiceHotel hotel = (IServiceHotel) Naming.lookup("rmi://localhost:1099/gestionHotel");



        // boolean b = hotel.ajouterClient(new Client(2,"ilyass", "makhloul", "makhloul.ilyas@usmba.ac.ma","123"));
        //boolean b = hotel.supprimerClient(1);
      //  boolean b = hotel.modifierClient(2,new Client("ilyas","makhloul","ilyas.makhloul0@gmail.com","123"));
     //   boolean b = hotel.ajouterChambre(new Chambre(1,"jspp",350));
        //b = hotel.modifierChambre(1 ,new Chambre("jspp",250));
    // boolean   b=  hotel.ajouterReservation(3,1,LocalDate.of(2024, 11, 28), LocalDate.of(2024, 12, 28));
     //   System.out.println("Succés "+b);

    }
}