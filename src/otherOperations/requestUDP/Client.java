package otherOperations.requestUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket client = new DatagramSocket();// create client udp socket
        InetAddress ip = InetAddress.getLocalHost();
        Scanner in = new Scanner(System.in);
        System.out.println("You are on device: " + ip);
        System.out.println("Enter the RequestType then the id\n"+
                "Example : \n" +
                "************\n" +
                "Name 0123456\n" +
                "************\n\n" +
                "PRESS ENTER TO CONTINUE . . .");

        byte data[];
        String toParse = in.nextLine();
        data = toParse.getBytes();
        DatagramPacket to_send = new DatagramPacket(data, data.length, ip, 5555);////for sending packets
        DatagramPacket to_receive ;//for receiving packets
        client.send(to_send);
        String id= "";
        String requestInfo ;
        while (true) {

            System.out.print("> ");
            toParse = in.nextLine();
            data = toParse.getBytes();
            to_send = new DatagramPacket(data, data.length, ip, 5555);
            client.send(to_send);
            System.out.println("Sent!");
            if(toParse.equalsIgnoreCase("end")) break;

            data = new byte[4546];
            to_receive = new DatagramPacket(data, data.length);
            client.receive(to_receive);
            toParse = new String(to_receive.getData(), StandardCharsets.UTF_8).trim();

            // TODO: Check if there is such id, if not print the warning message from the server
            try {
                id = toParse.substring(toParse.indexOf(':') + 1, toParse.indexOf(','));
            }catch (NumberFormatException e) {
                System.out.println(to_receive.getSocketAddress() + " --> "+ id);
                continue;

            }catch (StringIndexOutOfBoundsException e){
                System.out.println(toParse);
                continue;
            }
            /*
                taking requested information and id by '|' as a substrings from server message (toParse)
                string form:
                    requestedInformation:RequestedInfo, StudentNumber:id
                after split:
                    requestedInformation:|RequestedInfo|, StudentNumber:|id|
                    |RequestedInfo| |id|
            */
            requestInfo = toParse.substring(toParse.lastIndexOf(':') + 1, toParse.length());

            //TODO: print output in requested form studentNumber/requestedInformation
            System.out.println(to_receive.getSocketAddress() + " --> "+ id + "/ " + requestInfo);
        }
        client.close();
    }
}