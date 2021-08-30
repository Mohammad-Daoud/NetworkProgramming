package server;

import java.io.*;
import java.net.*;

public class myServer {
    public static void main(String[] args) {
        try {
            ServerSocket sSocket = new ServerSocket(1024);
            /*
             * 0-1023 -> well known port numbers
             * 1024-49151 -> registered ports
             * 49151-65535 -> free available port numbers
             * the IANA and ICAN the organized companies who decide which ip is used ,registered and free available .
             * the server can serve one client a time
             *
             */
               Socket s = sSocket.accept();
               DataInputStream dis = new DataInputStream(s.getInputStream());
               String str = (String) dis.readUTF();
               System.out.println("Client sent: " + str);
               s.close();
               sSocket.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}