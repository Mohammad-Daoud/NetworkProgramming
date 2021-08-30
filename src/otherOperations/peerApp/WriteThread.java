package otherOperations.peerApp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// TODO: CREATE A WRITING THREAD
// here is the write thread
public class WriteThread extends Thread {

    Socket socket;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public WriteThread(Socket socket)  {
        this.socket = socket;
    }

    String message = "";// to handle the message

    @Override
    public void run() {
        while (true) {// start while loop for continuous conversation
            try {

                System.out.print("> ");//  for UI -- you will see when the project run :)
                message = br.readLine();// read the input

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());//to send the data
                out.writeUTF(message);// send it !
                if (message.equalsIgnoreCase("end")) {//to quit if the user want to end the conversation
                    System.out.println("you have end the conversation ! ");
                    break;
                }
                System.out.println(" sent ✓");// for UI
            } catch (Exception ignored) {
            }
        }
        try {// if the client end ,So we have to terminate by 3-way handshake termination
            socket.close();
        } catch (IOException ignored) {
        }
        System.err.println(" Disconnected !!");//for UI
        System.exit(-1);// to be sure that the system is closed

    }
}
