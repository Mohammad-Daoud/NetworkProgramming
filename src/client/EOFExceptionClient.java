package client;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

//Client class
public class EOFExceptionClient {
    public static void main(String[] args) throws Exception {

        /*
         * TODO: create a socket and connecting it to server to communicate with (open client).
         *
         * So first create a Socket object -- (the API will do the 3-way handshake with server) # make sure that the
         *                      port is the same as SERVER to make the communication well.
         * then create a buffer stream (character stream 16-bit stream for user input  ).
         * then create an output stream (byte stream 8-bit which is the best practice for send data ) to send the data to server.
         * then create an input stream to receive data from server .
         *
         */
        InetAddress clientAdd = InetAddress.getLocalHost();
        InetSocketAddress socketAdd = new InetSocketAddress("127.0.0.1",3333); // InetSocketAddress is the best practice for maintenance


        Socket socket = new Socket(socketAdd.getHostName(), socketAdd.getPort()); //localhost-> 127.0.0.1 ,see tell ya !

        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

        /* for receiving data*/
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());



        String serverString; // form server
        String clientString; // form client
        /*
         * here we will use the InetAddress in java.net library
         */

        System.out.println(socket.getInetAddress().getHostName());
        System.out.println(socket.getPort());
        System.out.println(socket.getLocalPort());

        //end of examples

        String serverName = socket.getInetAddress().getHostName(); // to show the name in response
        int serverPort = socket.getPort();// to show the port number in response

        System.out.println("Please type your message:\n");


        clientString = bReader.readLine();// read from client


        while (!clientString.equals("end")) {

            /*
            TODO: for normal communication
             */

            dataOutput.writeUTF(clientString);//send it to server
            dataOutput.flush();// to make sure that the buffer is out of element ( means to clear the stream of any element )
            System.out.println("message sent successfully !"); // for notify the user that the message he wrote was sent successfully

            /*
             * if the server closed the connection we will have SocketException instead of EOFException
             * TODO: Handle the SocketException
             */
            try {

                serverString=(String)dataInput.readUTF();

            }catch (SocketException e ){
                System.err.println("the SocketException has DETECTED !!!\n\n" +
                        "\tTERMINATING PROGRAM ...");
                break;
            }

            //you should write the response from the name of the server and the port number

            System.out.println(serverName + serverPort + " Response is : \t" + serverString);
            /*
            * here will be the EOFException !!
            * if the client send "end" they'll break the loop and stop the process
            * while the server still waiting for client message to break or continue  while-loop .
            */

            clientString = bReader.readLine();
        }

        /*
        to close all program
         */
        System.err.println("\nTERMINATION SUCCEEDED !");
        dataInput.close();
        dataOutput.close();
        socket.close();
    }

}
