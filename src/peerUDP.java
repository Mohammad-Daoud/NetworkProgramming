import java.io.IOException;
import java.net.*;
import java.util.Scanner;
// another sample of peer App in udp
class receiverUDP extends Thread {
    DatagramSocket dgSocket;

    public receiverUDP(DatagramSocket dgs) {
        dgSocket = dgs;
    }

    public void run() {
        try {
            byte[] data = new byte[100];
            DatagramPacket dgp = new DatagramPacket(data, 100);
            try {
                dgSocket.receive(dgp);
            } catch (SocketException e) {
                System.err.println("the socket has been closed !!");
                System.exit(1);
            }
            String str = new String(dgp.getData()).trim();
            while (!str.equalsIgnoreCase("end")) {
                System.out.println(dgp.getAddress() + "/" + dgp.getPort() + " says:" + str);
                try {
                    dgSocket.receive(dgp);
                } catch (SocketException e) {
                    System.err.println("the socket has been closed !!");
                    System.exit(1);
                }
                str = new String(dgp.getData(), 0, dgp.getLength());
            }
            dgSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class peerUDP {
    public static void main(String[] args) {
        int serverPort = 5555;
        int peerPort = 4444;
        try {
            DatagramSocket dgSocket = new DatagramSocket(serverPort);

            new receiverUDP(dgSocket).start();
            byte[] data = new byte[100];
            DatagramPacket dgp = new DatagramPacket(data, 100, InetAddress.getLoopbackAddress(), peerPort);
            Scanner input = new Scanner(System.in);
            String str = "";
            while (!str.equalsIgnoreCase("end")) {
                System.out.println("Type your message ...");
                str = input.nextLine();
                dgp.setData(str.getBytes());
                dgp.setLength(str.length());
                dgSocket.send(dgp);
            }
            dgSocket.close();
        } catch (IOException e){
            System.err.println("communication is over :" + e.getMessage());
            System.exit(1);
        }
    }

}