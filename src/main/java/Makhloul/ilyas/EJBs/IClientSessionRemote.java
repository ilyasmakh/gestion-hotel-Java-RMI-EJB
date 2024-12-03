package Makhloul.ilyas.EJBs;


import jakarta.ejb.Remote;

@Remote
public interface IClientSessionRemote {

    public void seConnecter(int id );
    public void deconecter() ;
}
