package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
class NBThreadServer extends Thread {
    Socket so;
    public NBThreadServer(Socket so) {
        this.so = so;
    }

    public void run () {

        try {

            System.out.println(so.getInetAddress().getHostName());
            System.out.println(so.getPort());
            System.out.println(so.getLocalPort());
            String client_name = so.getInetAddress().getHostName();
            int client_port = so.getPort();

            DataInputStream din = new DataInputStream(so.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dout = new DataOutputStream(so.getOutputStream());
            String str1; // from client
            String str2 = ""; // from keyboard

            while (!str2.equals("end")) {
                try {
                    str1 = (String) din.readUTF();// receive str1 from the client
                } catch (EOFException e) {
                    System.out.println("Connection closed by client");
                    break;
                }
                System.out.println(client_name + "_" + client_port + " sent: " + str1);// print str to screen
                System.out.println("Type your response: ");
                str2 = br.readLine(); // read one line from the user into str2 '\n'=CRLF
                dout.writeUTF(str2);// write str2 to the socket to be sent to the client
                dout.flush();
            }
            din.close();
            dout.close();
            so.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
public class NBServer {
    public static void main(String[] args) throws Exception {
        ServerSocket seso = new ServerSocket(3333);
        while (true) {
            System.out.println("Awaiting data from new client ..");
            Socket so = seso.accept();// 3 way handshack
            new NBThreadServer(so).start();
        }
    }
}