package otherOperations.UDP_PeerApp;

import java.net.DatagramSocket;
import java.net.InetAddress;

//server side
public class UDP_ServerPeer {

    public static void main(String[] args) {// main start . . .
        try {
            DatagramSocket serverSocket = new DatagramSocket(5556);
            InetAddress ip = InetAddress.getLocalHost();

            //TODO : MAKE ALL THINGS WORK
            new UDP_ReadThread(serverSocket).start();// start the reading thread
            new UDP_WriteThread(serverSocket, ip, 6666).start();// start the writing thread

        } catch (Exception e) {
            //TODO HANDLE ANY EXCEPTION
            System.err.println("❌ ERROR ❌ :" + e.getMessage()); // for catching the error (EXCEPTION HANDLING )
            System.err.println("termination in process !!");
            System.exit(-1);
        }
    }
}
