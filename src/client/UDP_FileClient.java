package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDP_FileClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DatagramSocket dgSocket = new DatagramSocket();

        InetAddress ip = InetAddress.getLocalHost();

        byte [] receive = new byte[65535];

        DatagramPacket dgpSend = new DatagramPacket(receive, receive.length, ip, 5555);
        DatagramPacket dgReceive = new DatagramPacket(receive, receive.length);

        System.out.println("Do you wanna send message Y/N?");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        if (str.equals("N") ||str.equals("n")){
            dgSocket.close();
            return;
        }
        System.out.println("Connected !! " );
        while (!str.equalsIgnoreCase("end")) {

            if (str.equalsIgnoreCase("yes")) {
                dgpSend.setData(str.getBytes());
                dgpSend.setLength(str.length());
                dgSocket.send(dgpSend);
            }

            if (str.equalsIgnoreCase("end")) {
                System.out.println("you have end this conversation ");
                break;
            }

            // for receiving

            dgSocket.receive(dgReceive);
            str = new String(dgReceive.getData(),0,dgReceive.getLength());
            System.out.println("Server sent : " + str);

            str = scanner.nextLine().trim();
            dgpSend.setData(str.getBytes());
            dgpSend.setLength(str.length());
            dgSocket.send(dgpSend);

            if (str.equalsIgnoreCase("end"))
                break;

            dgSocket.receive(dgReceive);
            str = new String(dgReceive.getData(),0,dgReceive.getLength());

            if (str.equalsIgnoreCase("end"))
                break;

            System.out.println("Server sent : " + str);

        }
        dgSocket.close();
    }
}
