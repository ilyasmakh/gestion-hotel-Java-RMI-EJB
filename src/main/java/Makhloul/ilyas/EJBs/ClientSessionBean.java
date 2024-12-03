package Makhloul.ilyas.EJBs;


import jakarta.ejb.Stateful;

@Stateful
public class ClientSessionBean implements IClientSessionRemote {

    int clientId ;


    @Override
    public void seConnecter(int id ) {
        this.clientId = id;
    }


    @Override
    public void deconecter() {
        this.clientId = -1;
    }
}
