package otherOperations.peerApp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//TODO: CREATE A READING THREAD
//here is the read thread
public class ReadThread extends Thread {

    Socket socket;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public ReadThread(Socket socket) {
        this.socket = socket ;
    }

    String responseName = "";// to show which address that message came from
    int responsePort = 0;// to show which port that sender is sending from
    String response = "";// to show the message

    @Override
    public void run() {
        while (true) {// start while loop for continuous conversation
            try {
                responseName =socket.getInetAddress().getHostName();
                responsePort = socket.getPort();
                DataInputStream in = new DataInputStream(socket.getInputStream());// get the data
                response = in.readUTF().trim();// assign it --- we use trim() for erasing the un-necessary spaces .
                if (response.equalsIgnoreCase("end")) {// to quit if the user want to end the conversation
                    System.out.println("the "+socket.getRemoteSocketAddress()+" has end the conversation");
                    break;
                }//end if

                System.out.println(responseName + " with port " + responsePort + " sent: " + response);// print the message
                System.out.print("> ");// for UI -- you will see when the project run :)

            } catch (Exception ignored) {// un-necessary for catch handling
            }
        }// end while loop
        try {// if the server or client end ,So we have to terminate by 3-way handshake termination
            socket.close();
        } catch (IOException ignored) {// un-necessary for catch handling
        }
        System.err.println(" Disconnected from :" + socket.getRemoteSocketAddress());//for UI
        System.exit(-1);// to be sure that the system is closed
    }
}
