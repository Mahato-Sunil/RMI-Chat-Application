/*
 Interface defining the methods that the server will implement for remote clients.
 */

package Utils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteChatService extends Remote {
    void registerNewUser(String username, ClientInterface callback) throws RemoteException;
    List<String> getActiveUsers() throws RemoteException;
    void unregisterUser(String username) throws RemoteException;
    void sendMessage(String sender, String receiver, String message) throws RemoteException;  // Added sendMessage
}
