package otherOperations;

import java.io.*;
import java.net.*;



// client implementation
public class HM1_ClientImp {

    public static void main(String[] args) throws Exception {

        /*
         * TODO: create a socket and connecting it to server to communicate with (open client).         *
         * So first create a Socket object -- (the API will do the 3-way handshake with server) # make sure that the
         *                      port is the same as SERVER to make the communication well.
         * then create a buffer stream (character stream 16-bit stream for user input  ).
         * then create an output stream (byte stream 8-bit which is the best practice for send data ) to send the data to server.
         * then create an input stream to receive data from server .
         */

        InetAddress inetAddress = InetAddress.getLocalHost();// to show to the server that which machine in use
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",3333); // we use the InetSocketAddress library for maintenance purpose

        Socket socket = new Socket(address.getHostName(), address.getPort()); //localhost-> 127.0.0.1 and 3 way handshake
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));//to store data from the user (client ) in buffer
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream()); // to send data to the server
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());// to retrieve data from server
        String serverString; // form server
        String clientString; // form client

        //assign values if the client want to know which server he has connect with .
        String serverName= socket.getInetAddress().getHostName();
        int serverPort = socket.getPort();

        /*
          TODO:to check if the server want to connect with the client from the beginning.         */

        // notify the user that he is in connecting process

        System.out.println("connecting to server ...\n");
        serverString = dataInput.readUTF(); // to read the first thing that the server need to know for the operation

        System.out.println( serverName +" in port "+ serverPort +" CONNECTED !");

        // notify the user that he can cancel the connection any time he want
        System.out.println("***************************************************************\n" +
                "** YOU CAN DISCONNECT ANY TIME YOU WANT BY PRESSING \"end\"    **\n" +
                "***************************************************************\n");

        System.out.println(serverString);// showing it to the user


        while (!serverString.equals("end")) {

            /* TODO: for normal communication */

            clientString = bReader.readLine();// read from client
            try {
                dataOutput.writeUTF(clientString);//send it to server
                dataOutput.flush();// to make sure that the buffer is out of element ( means to clear the stream of any element )
                System.out.println("message sent successfully !"); // for notify the user that the message he wrote was sent successfully

                /*
                to handle the socket exception if the server has disconnected (just in case of )
                 */
            } catch (SocketException e) {
                System.err.println("disconnected !");
                break;
            }// end of try catch block


            //  to check if the client want to disconnect or end the conversation

            if (clientString.equals("end")) {
                System.out.println("You have end this conversation ! \n");
                break;
            }

            /*
                to handle the socket exception if the server has disconnected (just in case of )
                 */
            try {
                serverString = (String) dataInput.readUTF();
                System.out.println(serverString);
            } catch (EOFException | SocketException e) {
                System.err.println("disconnected !");
                break;
            }// end of try catch block

        }
        /*
        to close all program (disconnect from the server )
         */
        dataInput.close();
        dataOutput.close();
        socket.close();
    }

}
