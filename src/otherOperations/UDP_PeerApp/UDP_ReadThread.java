package otherOperations.UDP_PeerApp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;


//TODO: CREATE A READING THREAD
//here is the readThread
public class UDP_ReadThread extends Thread {

    DatagramSocket socket;
    DatagramPacket datagramPacketReceive;
    byte[] data = new byte[9595];
    String response;

    UDP_ReadThread(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        while (true) {
            try {

                data = new byte[9595];
                datagramPacketReceive = new DatagramPacket(data, data.length);
                socket.receive(datagramPacketReceive);
                response = new String(datagramPacketReceive.getData(), StandardCharsets.UTF_8).trim();// to store the response byte to the user

                if (response.equalsIgnoreCase("end")) { // to check ether if client or server wanna end !
                    System.err.println(datagramPacketReceive.getSocketAddress() + " has end the conversation !");
                    break;
                }
                System.out.println(datagramPacketReceive.getSocketAddress()+ " says : " + response );// show the response to user
                System.out.println();
                System.out.print("> ");// for UI

            } catch (Exception ignored) {
            }
        }
        try {
            socket.close();
        } catch (Exception ignored) { }
        System.err.println(" Disconnected !!");//for UI
        System.exit(-1);// to be sure that the system is closed
    }
}
