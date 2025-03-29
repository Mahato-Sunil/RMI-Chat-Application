package Utils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote { // Extending Remote to mark it as a remote interface
    void receiveMessage(String sender, String receiver, String message) throws RemoteException;  // Add throws RemoteException
    void updateActiveUsers(List<String> users) throws RemoteException;  // This method should also throw RemoteException
}
