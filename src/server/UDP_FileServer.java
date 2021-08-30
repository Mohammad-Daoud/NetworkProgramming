package server;



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;


public class UDP_FileServer {


    static String Search(String path) throws Exception{

        // creating a object of Path class
        Path file = Paths.get(path);

        // creating a object of BasicFileAttributes
        BasicFileAttributes attr = Files.readAttributes(
                file, BasicFileAttributes.class);


        System.out.println("creationTime Of File Is  = "
                + attr.creationTime());
        System.out.println("lastAccessTime Of File Is  = "
                + attr.lastAccessTime());
        System.out.println("lastModifiedTime Of File Is = "
                + attr.lastModifiedTime());

        System.out.println("size Of File Is = "
                + attr.size());
        System.out.println("isRegularFile Of File Is = "
                + attr.isRegularFile());
        System.out.println("isDirectory Of File Is = "
                + attr.isDirectory());
        System.out.println("isOther Of File Is = "
                + attr.isOther());

        System.out.println("isSymbolicLink Of File Is  = "
                + attr.isSymbolicLink());

        return "hello";
    }

    public static void main(String[] args) throws Exception {

        DatagramSocket dgSocket = new DatagramSocket(5555);
        byte[] data = new byte[100];

        DatagramPacket datagramPacketReceive = new DatagramPacket(data, data.length);
        DatagramPacket datagramPacketSend = new DatagramPacket(data, data.length);

        dgSocket.receive(datagramPacketReceive);

        datagramPacketSend.setAddress(datagramPacketReceive.getAddress());
        datagramPacketSend.setPort(datagramPacketReceive.getPort());

        String str = new String(datagramPacketReceive.getData()).trim();


        while (!str.equalsIgnoreCase("end")) {

            System.out.println(datagramPacketReceive.getAddress() + "/" + datagramPacketReceive.getPort() + " says:" + str);
            str = "please enter the path ";
            datagramPacketSend.setData(str.getBytes());
            datagramPacketSend.setLength(str.length());
            dgSocket.send(datagramPacketSend);

            dgSocket.receive(datagramPacketReceive);
            str = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

            datagramPacketSend.setData(Search(str).getBytes());
            datagramPacketSend.setLength(Search(str).length());
            dgSocket.send(datagramPacketSend);

        }

        dgSocket.close();
    }
}
