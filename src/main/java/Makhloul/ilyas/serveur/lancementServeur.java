package Makhloul.ilyas.serveur;

import Makhloul.ilyas.entités.Client;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;

public class lancementServeur {

    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        LocateRegistry.createRegistry(1099);
        IServiceHotel  hotel= new ServiceHotel();
      Naming.bind("rmi://localhost:1099/gestionHotel",hotel);
        System.out.println("Serveur des gestion d'hotel est lancé ...");


    }
}
