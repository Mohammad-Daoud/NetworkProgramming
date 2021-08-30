package otherOperations.peerApp;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//this is the server side of peer application
public class ServerPeer extends Thread {

    /**
     * In this program we've create a simple client-server application but the difference is that
     * the client can send multiple messages and the server will receive it.(send and receive in parallel)
     *
     * To do that we create the server socket object to bind and accept the connection
     * then create a values for bufferReader and an Address
     * but before anything we have to do a constructor for the connection and communication purpose.
     * then crate a main thread to connect with client and -
     * After that we create 2 Thread objects -> 1st : the reader thread that will handle the data that have been sending
     * from the server .
     * -> 2nd : the writer thread which will carry the data that we need to sent
     * and sending it to server
     * Now after we created these two threads all we have to do is RUN THE PROJECT by join the main thread with others .
     */

    //TODO : THE PRE-CONNECTION SET-UP
    private final ServerSocket serverSocket;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));// create the reader
    static Socket server; // to pass the port in the constructor

    public ServerPeer(int port) throws IOException {// constructor
        serverSocket = new ServerSocket(port);

    }
    //TODO : TO MAKE THE CONNECTION SUCCEED ✔
    @Override
    public void run() {
        try {
            InetAddress myAddress = InetAddress.getLocalHost();//to show the active workin'On machine
            System.out.println("You are on :" + myAddress);// to show myAddress
            System.out.println("CONNECTING . . . ");// for UI
            server = serverSocket.accept(); // 3-way handshake

            // for UI
            System.out.println("\nConnected with " + server.getRemoteSocketAddress());
            System.out.println("\n**************************************************\n" +
                    "be first who send or wait for receiving messages ..  \n" +
                    "**************************************************");
        } catch (Exception ignored) {// un-necessary for catch handling
        }
    }

    public static void main(String[] args) {// main start . . .
        try {
            Thread connector = new ServerPeer(5555);// create a thread object and assign port on ServerPeer class
            connector.start();// start the thread
            try {
                connector.join();// to join the read and write threads
            } catch (Exception ignored) {// un-necessary for catch handling
            }
            Thread readerThread =new ReadThread(server);
            Thread writerThread =new WriteThread(server);
            //TODO : MAKE ALL THINGS WORK
            readerThread.start();// start the reading thread
            writerThread.start();// start the writing thread

        } catch (Exception e) {
            //TODO HANDLE ANY EXCEPTION
            System.err.println("❌ ERROR ❌ :" + e); // for catching the error (EXCEPTION HANDLING )
            System.err.println("termination in process !!");
            System.exit(-1);
        }
    }
}




