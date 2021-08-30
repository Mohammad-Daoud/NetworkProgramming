package client;

import java.io.*;
import java.net.*;

public class myClient {
    public static void main(String[] args) {
        try {

            Socket cSocket = new Socket("localhost", 3333);
            DataOutputStream dOut = new DataOutputStream(cSocket.getOutputStream());
            dOut.writeUTF("1");
            dOut.flush();
            dOut.close();
            cSocket.close();
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
