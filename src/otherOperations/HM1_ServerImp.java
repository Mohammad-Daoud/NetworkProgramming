package otherOperations;

import java.io.*;
import java.net.*;



// server implementation
public class HM1_ServerImp {

    static boolean isInt(String str) { // check if given operand is numeric and have integer property
        try {
            int num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    static boolean isOperation(String str) {// check if given operation is valid
        return (str.equals("*") || str.equals("/") || str.equals("+") || str.equals("-"));
    }

    static double result(int operand1, int operand2, String operation) { //  calculate the equation
        double res = 0;
        if (operation.equals("*"))
            res = operand1 * operand2;
        if (operation.equals("/"))
            res = ((double) operand1)/operand2;
        if (operation.equals("+"))
            res = operand1 + operand2;
        if (operation.equals("-"))
            res = operand1 - operand2;
        return  res;
    }

    public static void main(String[] args) throws Exception {

        /*
         * TODO: create a server socket todo bind and listen connection then the server accept connection and client socket to communication purpose (open listen).
         *
         * first create a ServerSocket object --(the API will do binding and listen for the handshake ).
         * the server will be always on to serve multiClients ,even if there any exception it will shown
         *                  to server side.
         * then create a Socket object to connect with client and assign the value .accept() to that object
         *                  to block the console until the client is connected.
         * then create an input stream to receive data from client.
         * then the server should ask the client of what operation he want to use (multiply, divide, subtract, addition )
         *
         * then create an output stream (byte stream 8-bit which is the best practice for send data )
         * for send the  to client.
         */

        InetAddress inetAddress = InetAddress.getLocalHost();// to show to the server that which machine in use
        InetSocketAddress address = new InetSocketAddress(3333); // we use the InetSocketAddress library for maintenance purpose
        ServerSocket serverSocket = new ServerSocket(address.getPort());

        System.out.println("Machine name : "+ inetAddress.getHostName());
        System.out.println("Machine IP : "+ inetAddress.getHostAddress());

        while (true) {
            Socket socket = serverSocket.accept();// 3 way handshake

            DataInputStream dataInput = new DataInputStream(socket.getInputStream());//to get data from the user (client )
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());// to send data to the client

            String clientString = ""; // from client
            String operand1, operand2, operation; // to read the operations form client


         /*
           TODO:to check if the connected succeeded
        */

            // here to show to the server that there is new client hes been connected
            System.out.println("\nNew client connected ... "
                    + "\nclient HOST ADDRESS ( IPv4 address ) : " +
                    socket.getInetAddress().getHostAddress() + '\n');

            while (!clientString.equals("end"))

                try {

                    dataOutput.writeUTF("ENTER THE FIRST NUMBER ( ONLY INTEGER ) ");
                    operand1 = (String) dataInput.readUTF().trim(); // to read the input from the client and the trim() fun is for eliminates leading and trailing spaces.

                    dataOutput.writeUTF("ENTER THE SECOND NUMBER ( ONLY INTEGER )  ");
                    operand2 = (String) dataInput.readUTF().trim(); // to read the input from the client and the trim() fun is for eliminates leading and trailing spaces.

                    dataOutput.writeUTF("CHOOSE THE OPERATION : (+, -, *, /) - JUST THE SYMBOL");
                    operation = (String) dataInput.readUTF().trim(); // to read the input from the client and the trim() fun is for eliminates leading and trailing spaces.

                    /*
                    we create a flags for more efficiency code
                     */
                    boolean operandFlag1 = isInt(operand1);
                    boolean operandFlag2 = isInt(operand2);
                    boolean operationFlag = isOperation(operation);

                    /*
                    these server outputs to know where the user has incorrect inputs. (like logs to catch user error )
                     */
                    System.out.println("operand 1 value : " + operand1 + "  and boolean value is : " + operandFlag1);
                    System.out.println("operand 1 value : " + operand2 + "  and boolean value is : " + operandFlag2);
                    System.out.println("operand 1 value : " + operation + "  and boolean value is : " + operationFlag);

                    // if the user has incorrect inputs
                    if (!operationFlag || !operandFlag1
                            || !operandFlag2) {

                        dataOutput.writeUTF("User-Input ERROR !!! \n" +
                                "RE-CONNECT TO THE SERVER PLEASE ! \n" +
                                "if you has still in connection press ENTER to disconnect .. ");
                        dataOutput.flush();
                        break;
                    }

                    /*
                    here if the user need more operation to be done with the server .
                     */
                    dataOutput.writeUTF(operand1 + " " + operation +
                            " " + operand2 + " = " +
                            result(Integer.parseInt(operand1), Integer.parseInt(operand2), operation) + "\n\n" +
                            "Do you want to continue Y/N ?");
                    dataOutput.flush();

                    clientString = (String) dataInput.readUTF();
                    if (clientString.equals("n") || clientString.equals("N")) {
                        System.out.println(socket.getInetAddress().getHostAddress()+" client has disconnected ");
                        break;
                    }
                /*
                for EXCEPTION HANDLING
                 */
                } catch (EOFException e) { // if End-Of-File Exception
                    System.err.println(socket.getInetAddress().getHostAddress()+" Client has disconnected ! EOFException\n");
                    break;
                } catch (SocketException e) { // if Socket Exception
                    System.err.println(socket.getInetAddress().getHostAddress()+" Client has disconnected ! SocketException\n");
                    break;
                } catch (ArithmeticException e) { // if Arithmetic Exception
                    dataOutput.writeUTF("ERROR : DIVISION BY ZERO !! \n " +
                            "RE-CONNECT TO THE SERVER \n " +
                            "if you has still in connection press ENTER to disconnect ..");
                    System.err.println(socket.getInetAddress().getHostAddress()+" Client has disconnected ! ArithmeticException\n");
                    break;
                }
            /*
            to close all connection with the current client and wait for another one
             */
            dataInput.close();
            dataOutput.close();
            socket.close();
        }
    }
}
