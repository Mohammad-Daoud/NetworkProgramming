
package otherOperations.UDP_PeerApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TODO: CREATE A WRITING THREAD
//here is the write thread

public class UDP_WriteThread extends Thread {

    DatagramSocket socket;//to pass the socket in the constructor

    byte[] data = new byte[9595];// container for sending data
    DatagramPacket datagramPacketSend;// the packet that need to sent

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    String response;

    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    LocalDateTime now = LocalDateTime.now();
    UDP_WriteThread(DatagramSocket socket, InetAddress ip, int port) {
        this.socket = socket;
        datagramPacketSend = new DatagramPacket(data, data.length, ip, port);
    }

    @Override
    public void run() {
        try {
            InetAddress myAddress = InetAddress.getLocalHost();//to show the active workin'On machine
            System.out.println("You are on :" + myAddress);// to show myAddress
            System.out.println("CONNECTING . . . ");// for UI

            // for UI
            System.out.println("\n**************************************************\n" +
                               "be first who send or wait for receiving messages ..  \n" +
                               "**************************************************");

            System.out.println("******************  "+formatter.format(now)+"  ******************");
        } catch (Exception ignored) {
        }

        while (true) {
            try {
                data = new byte[9595];

                System.out.print("> ");
                response = input.readLine();

                datagramPacketSend.setData(response.getBytes());
                datagramPacketSend.setLength(response.length());
                socket.send(datagramPacketSend);

                System.out.println("    sent ✓");

                if (response.equalsIgnoreCase("end")) {
                    System.out.println("You have end this conversation !");
                    break;
                }

            } catch (Exception ignored) {
            }
        }
        try {
            socket.close();
        } catch (Exception ignored) {
        }

        System.err.println(" Disconnected !!");//for UI
        System.exit(-1);// to be sure that the system is closed

    }
}