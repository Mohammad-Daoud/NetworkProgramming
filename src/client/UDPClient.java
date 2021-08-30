package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UDPClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DatagramSocket dgSocket = new DatagramSocket();
        String ipInput ;
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("you are on device : "+ ip);


        byte [] receive = new byte[65535];
        System.out.println("enter the server ip address :");
        ipInput = sc.nextLine().trim();
        ip = InetAddress.getByName(ipInput);

        // to know the period of time

        long startTime ,endTime , period ;

        System.out.println("enter port number :");
        int port;
        port= sc.nextInt();

        DatagramPacket dgpSend = new DatagramPacket(receive, receive.length, ip, port);
        DatagramPacket dgReceive = new DatagramPacket(receive, receive.length);

        System.out.println("Do you wanna send message Y/N?");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        if (str.equals("N") ||str.equals("n")){
            dgSocket.close();
            return;
        }

        while (!str.equalsIgnoreCase("end")) {

            System.out.println("write your message : ");
            str = scanner.nextLine().trim();

            dgpSend.setData(str.getBytes());
            dgpSend.setLength(str.length());
            startTime =System.nanoTime();

            dgSocket.send(dgpSend);

            System.out.println("Sent Successfully !");
            System.out.println("the massage has sent since "+ startTime/1000000+"ms");

            if (str.equalsIgnoreCase("end")) {
                System.out.println("you have end this conversation ");
                break;
            }

            // for receiving

            dgSocket.receive(dgReceive);
            TimeUnit.SECONDS.sleep(5);
            endTime=System.nanoTime();
            str = new String(dgReceive.getData(), StandardCharsets.UTF_8);

            if (str.equalsIgnoreCase("end"))
                break;

            period =endTime-startTime;
            //String message = new String(receive, StandardCharsets.UTF_8);

            System.out.println("Server sent : " + str);
            System.out.println("the message has received since "+ endTime/1000000+"ms");

            System.out.println("the period is "+ period/1000000 +"ms");

        }
        dgSocket.close();
    }
}

