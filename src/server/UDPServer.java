package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class UDPServer {
        public static void main(String[] args) throws Exception {
            DatagramSocket dgSocket = new DatagramSocket(5555);
            byte[] data = new byte[100];
            DatagramPacket datagramPacketReceive = new DatagramPacket(data, data.length);
            DatagramPacket datagramPacketSend = new DatagramPacket(data, data.length);
            dgSocket.receive(datagramPacketReceive);
            datagramPacketSend.setAddress(datagramPacketReceive.getAddress());
            datagramPacketSend.setPort(datagramPacketReceive.getPort());
            String str = new String(datagramPacketReceive.getData()).trim();
            Scanner input = new Scanner(System.in);

            while (!str.equalsIgnoreCase("end")) {
                System.out.println(datagramPacketReceive.getAddress() + "/" + datagramPacketReceive.getPort() + " says:" + str);
                System.out.println("Type your response... ");
                str = input.nextLine();
                datagramPacketSend.setData(str.getBytes());
                datagramPacketSend.setLength(str.length());
                dgSocket.send(datagramPacketSend);
                System.out.println("Sent Successfully !");
                if (str.equalsIgnoreCase("end")) {
                    break;
                }
                dgSocket.receive(datagramPacketReceive);
                str = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

            }
            dgSocket.close();
        }
    }

