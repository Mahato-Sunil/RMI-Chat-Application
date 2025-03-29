package Utils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer extends UnicastRemoteObject implements RemoteChatService {
    private final List<String> activeUsers;
    private final Map<String, ClientInterface> clientCallbacks;

    public ChatServer() throws RemoteException {
        super();
        activeUsers = new ArrayList<>();
        clientCallbacks = new HashMap<>();
    }

    @Override
    public synchronized void registerNewUser(String username, ClientInterface callback) throws RemoteException {
        if (!activeUsers.contains(username)) {
            activeUsers.add(username);
            clientCallbacks.put(username, callback);
            System.out.println(username + " has joined the chat.");
            notifyAllClients();
        }
    }

    @Override
    public synchronized List<String> getActiveUsers() throws RemoteException {
        return new ArrayList<>(activeUsers);
    }

    @Override
    public synchronized void unregisterUser(String username) throws RemoteException {
        if (activeUsers.remove(username)) {
            clientCallbacks.remove(username);
            System.out.println(username + " has left the chat.");
            notifyAllClients();
        }
    }

    private void notifyAllClients() throws RemoteException {
        List<String> users = new ArrayList<>(activeUsers);
        for (ClientInterface client : clientCallbacks.values()) {
            client.updateActiveUsers(users);  // Send updated user list to all clients
        }
    }
    @Override
    public synchronized void sendMessage(String sender, String receiver, String message) throws RemoteException {
        System.out.println(sender + " -> " + receiver + ": " + message);

        ClientInterface receiverClient = clientCallbacks.get(receiver);
        if (receiverClient != null) {
            receiverClient.receiveMessage(sender, receiver, message);  // Deliver the message
        } else {
            System.out.println("User " + receiver + " not found.");
        }
    }
}
