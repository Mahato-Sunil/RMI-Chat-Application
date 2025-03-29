/*
entry point of the system
 */

import UserInterface.LoginPage;
import Utils.ChatServer;
import Utils.RemoteChatService;

import javax.naming.Name;
import javax.swing.*;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static Utils.IPFinder.getLocalIPAddress;

public class Main {
    public static void main(String[] args) {
        initRemoteChatServer();
        initSystemUI();
        initSystemUI();
    }

    private static void initRemoteChatServer() {
        try {
            //fetch the current ip of the system
//            String localIP = (getLocalIPAddress() != null) ? getLocalIPAddress() : null;
//            System.out.println("Server connected to " + localIP);
//            RemoteChatService rcs = new ChatServer();
//            Naming.rebind("rmi://" + localIP + ":1099/ChatService", rcs);

            RemoteChatService rcs = new ChatServer();
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("ChatService", rcs);
            System.out.println("Remote Chat Server Started");
        } catch (Exception e) {
            System.out.println("Remote Chat Server Failed");
            System.out.println(e.getMessage());
        }
    }

    private static void initSystemUI() {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
