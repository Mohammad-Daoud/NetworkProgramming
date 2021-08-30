package otherOperations.peerApp;

import java.io.*;
import java.net.InetAddress;

import java.net.Socket;
// this is the client side of peer application
public class ClientPeer extends Thread {
    /**
     * In this program we've create a simple client-server application but the difference is that
     *          the client can send multiple messages and the server will receive it.(send and receive in parallel)
     *
     * To do that we create a values for bufferReader and an Address.
     * After that we create 2 Thread objects -> 1st : the reader thread that will handle the data that have been sending
     *                                                from the server .
     *                                       -> 2nd : the writer thread which will carry the data that we need to sent
     *                                                and sending it to server
     * Now after we created these two threads all we have to do is RUN THE PROJECT .
     *
     */
    public static void main(String[] args) {
        try {
            // TODO: CREATE THE PRE-CONNECTION SET-UP
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));// create the reader
            InetAddress myAddress = InetAddress.getLocalHost(); // to show the active workin'On machine

            Socket client = new Socket("127.0.0.1", 5555);// declare a socket for client

            System.out.println("You are on : " + myAddress);// to show my address
            // the next statements for UI
            System.out.println("CONNECTING . . . ");
            System.out.println("\nConnected with " + client.getRemoteSocketAddress());
            System.out.println("\n**************************************************\n" +
                    "be first who send or wait for receiving messages .. \n" +
                    "**************************************************");

            Thread readerThread =new ReadThread(client);
            Thread writerThread =new WriteThread(client);

            //TODO : MAKE ALL THINGS WORK
            readerThread.start();// start the reading thread
            readerThread.join(1000);
            writerThread.start();// start the writing thread
            writerThread.join(1000);
        } catch (Exception e) {
            //TODO HANDLE ANY EXCEPTION
            e.printStackTrace();
            System.err.println("❌ ERROR ❌ :" + e); // for catching the error (EXCEPTION HANDLING )
            System.err.println("termination in process !!");
            System.exit(-1);
        }
    }
}

