package otherOperations.UDP_PeerApp;

import java.net.DatagramSocket;
import java.net.InetAddress;

//client side
public class UDP_ClientPeer {

    public static void main(String[] args) {// main start . . .

        try {
            InetAddress ip = InetAddress.getLocalHost();
            DatagramSocket socket = new DatagramSocket(6666);

            //TODO : MAKE ALL THINGS WORK
            new UDP_ReadThread(socket).start();// start the reading thread
            new UDP_WriteThread(socket, ip, 5555).start();// start the writing thread

        } catch (Exception e) {
            //TODO HANDLE ANY EXCEPTION
            System.err.println("❌ ERROR ❌ :" + e.getMessage()); // for catching the error (EXCEPTION HANDLING )
            System.err.println("termination in process !!");
            System.exit(-1);
        }
    }
}
