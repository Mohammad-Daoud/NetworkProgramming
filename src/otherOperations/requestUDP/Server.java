package otherOperations.requestUDP;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

class Student {


    private String studentName;
    private double GPA;
    private int age;


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /*
       TODO: function to return requested data from server
    */
    public String getRequest(String value) {
        if (value.equalsIgnoreCase("gpa"))
            return String.valueOf(getGPA());
        else if (value.equalsIgnoreCase("age"))
            return String.valueOf(getAge());
        return String.valueOf(getStudentName());
    }
}

public class Server {
    //TODO: function to check if requested information available

    static boolean checkInfo(String value) {
        return value.equalsIgnoreCase("age") ||
                value.equalsIgnoreCase("gpa") ||
                value.equalsIgnoreCase("name");
    }

    // main method
    public static void main(String[] args) throws Exception {
        // student samples
        // student 1
        Student mohammadDaoud = new Student();
        mohammadDaoud.setAge(23);
        mohammadDaoud.setGPA(2.5);
        mohammadDaoud.setStudentName("Mohammad Abdallah Raja Daoud");

        // student 2
        Student hamzaMonther = new Student();
        hamzaMonther.setAge(23);
        hamzaMonther.setGPA(3.9);
        hamzaMonther.setStudentName("Hamza Monther Mostafa Amirah");

        // student 3
        Student SaifSaboba = new Student();
        SaifSaboba.setAge(23);
        SaifSaboba.setGPA(1.5);
        SaifSaboba.setStudentName("Saif Raed Mostafa Saboba");
        /*
         We use HashMap to identify each student by its ID (student number)
         -because its unique- as a key, and the value of this key is object of type student
         which contain student information.
         */
        HashMap<String, Student> dataPojo = new HashMap<String, Student>();
        dataPojo.put("0173632", mohammadDaoud);
        dataPojo.put("0156969", SaifSaboba);
        dataPojo.put("0189136", hamzaMonther);

        DatagramSocket server = new DatagramSocket(5555);
        byte data[] = new byte[4564];
        DatagramPacket Packet_received = new DatagramPacket(data, data.length), // for receiving packets
                Packet_send = new DatagramPacket(data, data.length);//for sending packets
        server.receive(Packet_received);
        Packet_send.setAddress(Packet_received.getAddress());
        Packet_send.setPort(Packet_received.getPort());

        String message;
        String requestInfo;
        String id;
        StringTokenizer value;
        String sendQuery;
        while (true) {
            data = new byte[4564];
            Packet_received = new DatagramPacket(data, data.length);
            server.receive(Packet_received);
            message = new String(Packet_received.getData()).trim();
            System.out.println(Packet_received.getAddress() + "//" + Packet_received.getPort() + " --> " + message);
            value = new StringTokenizer(message);
            if (message.equalsIgnoreCase("end")) break;
            // TODO: we need to check if input contain two parts, info and id
            try {
                requestInfo = value.nextToken();
                id = value.nextToken();
            } catch (NoSuchElementException e) {
                Packet_send.setData(("\n****************\n" +
                        "* WRONG INPUT !*\n" +
                        "****************").getBytes());
                server.send(Packet_send);
                continue;
            }
            //TODO: then check if id exist
            if (!dataPojo.containsKey(id)) {
                Packet_send.setData("There no such id in this server!, re-enter".getBytes());
                server.send(Packet_send);
                continue;
            }
            //TODO:then check information if it's available
            if (!checkInfo(requestInfo)) {
                Packet_send.setData("Wrong request, no such information!, re-enter".getBytes());
                server.send(Packet_send);
                continue;
            }
            // TODO: otherwise return requestedInformation:RequestedInfo, StudentNumber:id to client
            requestInfo = "requestedInformation:" + dataPojo.get(id).getRequest(requestInfo);
            id = "studentNumber:" + id;
            sendQuery = id + ", " + requestInfo;
            Packet_send.setData(sendQuery.getBytes());
            server.send(Packet_send);
        }
        System.out.println("Client has end this conversation !");
        server.close();
    }
}