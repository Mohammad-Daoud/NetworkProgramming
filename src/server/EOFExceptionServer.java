package server;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

//Server Class
public class EOFExceptionServer {
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


        InetSocketAddress socketAdd = new InetSocketAddress(3333);
        ServerSocket serverSocket = new ServerSocket(socketAdd.getPort());
        Socket socket = serverSocket.accept();
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());

        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

        InetAddress address = InetAddress.getLocalHost();

        String clientString=""; // from client
        String keyboardString=""; // from keyboard


        System.out.println("\n\nName of local host is : "+ address );

        System.out.println("\nNew client want to connect ... "
                +"\nclient HOST ADDRESS ( IPv4 address ) : "+ socket.getInetAddress()
                .getHostAddress()+'\n');

        System.out.println("Awaiting data from Client");

        /*
         * TODO: to the normal communication.
         *
         * now if the user send an "end" the disconnected in 3-way handshake termination process will just filed !!
         * and the server going to wait for data and all of he received is nothing (empty value) here will be (EOFException)
         * ,So there is 2 way to solve it  first to handle it with try-catch block and the second is to keep checking if
         * the client want to end the conversation.
         */

        /*
         * TODO: Handle the EODException with try-catch block.
         * In ServerRW and ClientRW classes I've handle the EOFException :
         *      A- in ServerRW.class :-
         *          1- Make the while loop always true.
         *          2- check if the client has end the conversation if(true)->break or keep looping.
         *          3- check if the server want to end the conversation (this for SocketException)
         *                  and send it to ClientRW class to check it .
         *      B- in ClientRW :-
         *          1- send the "end" statement to ServerRW class to break the loop
         *          2- check if the server has sent "end" to the client .
         *
         * In my opinion I preferred the first way which is try-catch cuz it's more efficient I think :) .
         *
         */


        String clientName  = socket.getInetAddress().getHostName(); // to show the name in response
        int clientPort = socket.getPort();// to show the port number in response
        while (!keyboardString.equals("end")) {

            try {
                clientString = (String) dataInput.readUTF();

            } catch (EOFException e) {

                /*
                 * if we don't need to serve multiple client (only one client and then close the connection )
                 * So just add "break;" after System.err.println("the Client has disconnected ...etc ");
                 *
                 */
                System.err.println("the Client has disconnected !\n\n" +
                        "\tpress ENTER to connect with new client  ");
                socket.close();

                serverSocket.accept();

                System.out.println("\nNew client want to connect ... "
                        +"\nclient HOST ADDRESS ( IPv4 address ) : "+ socket.getInetAddress()
                        .getHostAddress()+'\n');


            }
            System.out.println(clientName + clientPort + "sent : \t" + clientString);
            keyboardString = bReader.readLine(); // '\n'
            dataOutput.writeUTF(keyboardString);
            System.out.println("message sent successfully !");
            dataOutput.flush();
        }

        /*
        to close all program
         */
        System.err.println("\nTERMINATION SUCCEEDED !");
        dataInput.close();
        dataOutput.close();
        socket.close();
        serverSocket.close();
    }
}

