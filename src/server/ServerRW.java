package server;


import java.io.*;
import java.net.*;

// server class for read and write data

public class ServerRW {
    public static void main(String[] args) throws Exception {
        /*
         *TODO: create a server socket todo bind and listen connection then the server accept connection and client socket to communication purpose (open listen).
         *
         *first create a ServerSocket object --(the API will do binding and listen for the handshake ).
         *then create a Socket object to connect with client and assign the value .accept() to that object
         *                  to block the console until the client is connected.
         *then create an input stream to receive data from client.
         *then create a buffer stream (character stream 16-bit stream for user input ).
         *then create an output stream (byte stream 8-bit which is the best practice for send data ) to send the data to client.
         */


        ServerSocket serverSocket = new ServerSocket(3333);
        Socket socket = serverSocket.accept();

        DataInputStream dataInput = new DataInputStream(socket.getInputStream());
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

        String clientString; // from client
        String keyboardString; // from keyboard


        /*
           TODO:to check if the server want to connect with the client from the beginning
        */

        System.out.println("\nNew client want to connect ... "
                +"\nclient HOST ADDRESS ( IPv4 address ) : "+ socket.getInetAddress()
                .getHostAddress()+'\n');

        System.out.println("Do you want to connect to client ? Y/N");// to ask the server he want to connect
        keyboardString = bReader.readLine(); // read the user input if Y or N

        if (keyboardString.equals("N")||keyboardString.equals("n")) {
            dataOutput.writeUTF("end"); // to make if statement for disconnection
            dataOutput.flush();// to make sure that the buffer is out of element ( means to clear the stream of any element )
            System.out.println("You have end this conversation ! \n\n ");
            /*
               to close all program
            */
            dataInput.close();
            dataOutput.close();
            socket.close();
            serverSocket.close();
            return;

        } else {
            /*
               TODO:to continue the if statement condition.
            */
            System.out.println("Wait for client message ... ");
            dataOutput.writeUTF("continue");
            dataOutput.flush();
        }
        while (true) {

            /*
            TODO: to check if the client want to disconnected or end the conversation.
             */
            clientString = (String) dataInput.readUTF();
            if (clientString.equals("end")) {
                System.out.println("Client has ended this conversation !!\n");
                break;
            }
            /*
            TODO: to the normal communication.
             */
            System.out.println("Client sent : " + clientString);
            keyboardString = bReader.readLine(); // '\n'
            dataOutput.writeUTF(keyboardString);
            dataOutput.flush();
            System.out.println("message sent successfully !");

            /*
            TODO: to check if the server want to disconnect or end the conversation.
             */
            if (keyboardString.equals("end")) {
                System.out.println("You have end this conversation ! \n\n ");
                break;
            }
        }
        /*
        to close all program
         */
        dataInput.close();
        dataOutput.close();
        socket.close();
        serverSocket.close();
    }
}

