package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class UDP_EchoServer {
    public static void main(String[] args) throws Exception {

        DatagramSocket dgSocket = new DatagramSocket(5555);
        byte[] data = new byte[100];
        DatagramPacket datagramPacketReceive = new DatagramPacket(data, data.length);
        DatagramPacket datagramPacketSend = new DatagramPacket(data, data.length);
        dgSocket.receive(datagramPacketReceive);
        datagramPacketSend.setAddress(datagramPacketReceive.getAddress());
        datagramPacketSend.setPort(datagramPacketReceive.getPort());
        String str = new String(datagramPacketReceive.getData()).trim();


        while (!str.equalsIgnoreCase("end")) {
            System.out.println(datagramPacketReceive.getAddress() + "/" + datagramPacketReceive.getPort() + " says:" + str);
            datagramPacketSend.setData(data);
            dgSocket.send(datagramPacketSend);
            System.out.println("Re-sent Successfully !");
            if (str.equalsIgnoreCase("end")) {
                break;
            }
            dgSocket.receive(datagramPacketReceive);
            str = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

        }
        dgSocket.close();
    }
}
