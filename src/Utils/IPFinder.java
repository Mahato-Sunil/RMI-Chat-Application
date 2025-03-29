package Utils;

import java.net.*;
import java.util.Enumeration;

public class IPFinder {
    public static String getLocalIPAddress() throws SocketException {
        // Get all network interfaces
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();

            // Get all IP addresses for the current network interface
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();

                // Check if the IP address is not a loopback address (localhost)
                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                    // Return the first non-loopback IP address (usually the local network IP)
                    return inetAddress.getHostAddress();

            }
        }
        return null; // Return null if no IP address found
    }

}
