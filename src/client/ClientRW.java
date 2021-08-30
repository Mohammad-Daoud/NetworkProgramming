package client;

import java.io.*;
import java.net.*;

//client Class for read and write data
public class ClientRW {

    public static void main(String[] args) throws Exception {

        /*
         * TODO: create a socket and connecting it to server to communicate with (open client).         *
         * So first create a Socket object -- (the API will do the 3-way handshake with server) # make sure that the
         *                      port is the same as SERVER to make the communication well.
         * then create a buffer stream (character stream 16-bit stream for user input  ).
         * then create an output stream (byte stream 8-bit which is the best practice for send data ) to send the data to server.
         * then create an input stream to receive data from server .
         *
         */
        Socket socket = new Socket("localhost", 3333); //localhost-> 127.0.0.1
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());
        String serverString; // form server
        String clientString; // form client

        /*
          TODO:to check if the server want to connect with the client from the beginning.         */
        System.out.println("connecting to server ...");
      /*  serverString = dataInput.readUTF();
        if (serverString.equals("end")) {
            System.out.println("Server has end this conversation ! \n");
            dataInput.close();
            dataOutput.close();
            socket.close();
            return;
        } else {*/
            System.out.println(" connected ! ");
            System.out.println("Please type your message: ");
        //}
        while (true) {
            /*
            TODO: for normal communication             */
            clientString = bReader.readLine();// read from client
            dataOutput.writeUTF(clientString);//send it to server
            dataOutput.flush();// to make sure that the buffer is out of element ( means to clear the stream of any element )
            System.out.println("message sent successfully !"); // for notify the user that the message he wrote was sent successfully
            /*
            TODO: to check if the client want to disconnect or end the conversation             */
            if (clientString.equals("end")) {
                System.out.println("You have end this conversation ! \n");
                break;
            }
            /*
            TODO: to check if the server want to disconnected or end the conversation             */
            serverString = dataInput.readUTF();
            if (serverString.equals("end")) {
                System.out.println("Server has end this conversation ! \n");
                break;
            }
            System.out.println("Server response is : " + serverString);
        }
        /*
        to close all program
         */
        dataInput.close();
        dataOutput.close();
        socket.close();
    }
}