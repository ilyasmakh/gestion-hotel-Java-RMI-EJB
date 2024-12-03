package Makhloul.ilyas.EJBs;


import jakarta.ejb.Remote;

@Remote
public interface IclientAuthentificationRemote {

    public int Sauthentifier(String username, String password);
}
