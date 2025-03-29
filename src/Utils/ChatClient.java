/*
implements the clientinterface
responsible for interacting with the RemoteChatService remotely
 */


package Utils;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.IPFinder.getLocalIPAddress;

//public class ChatClient extends UnicastRemoteObject implements ClientInterface {
public class ChatClient extends UnicastRemoteObject implements ClientInterface {
    private List<String> activeUsers;
    private final String username;
    private final RemoteChatService rcs;
    private Map<String, String> newMessage;

    // return the instance of the remote chat service
    public RemoteChatService getRemoteService() {
        return rcs;
    }

    public String getUsername() {
        return this.username;  // Return the username
    }

    public ChatClient(String username) throws Exception {
        super();
        this.username = username;
        activeUsers = new ArrayList<>();
        newMessage = new HashMap<>();

//        String localIP = (getLocalIPAddress() != null) ? getLocalIPAddress() : null;
//        System.out.println("Client connected to " + localIP);
        Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
        rcs = (RemoteChatService) registry.lookup("ChatService");
//        rcs = (RemoteChatService) Naming.lookup("rmi://" + localIP + ":1099/ChatService");

        rcs.registerNewUser(username, this);
        System.out.println("Connected as: " + username);
    }

    public void logout() throws RemoteException {
        rcs.unregisterUser(username);
    }

    @Override
    public void receiveMessage(String sender, String receiver, String message) {
        System.out.println("Message Received:");
        System.out.println("From: " + sender + " -> " + receiver + ": " + message);
        newMessage.put(receiver, message);
    }

    @Override
    public void updateActiveUsers(List<String> users) throws RemoteException {
        activeUsers.clear();
        activeUsers.addAll(users);
        System.out.println("Active Users :" + users);
    }

    public String getMessage(String sender) {
        return newMessage.getOrDefault(sender, "No Message");
    }
}
