package URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class URL_Sample {


    private String HTTPRequest(String Payload) {

        try {

            URL url = new URL(Payload);
            URLConnection con;
            HttpURLConnection http;

            con = url.openConnection();
            http = (HttpURLConnection) con;

            http.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "Application/json; utf-8");
            http.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            byte[] input = Payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String responseLine ;

            long date = (long) (con.getDate()*1.15741e-8*0.00273973);
            System.out.println(date);
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();


        } catch (IOException e) {

            return null;
        }
    }

    public static void main(String[] args) {

        try {

            URL url = new URL("http://www.ju.edu.jo/index.html");

            URLConnection connection = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) connection;

            System.out.println("Protocol: " + url.getProtocol());
            System.out.println("Host name: " + url.getHost());
            System.out.println("File name: " + url.getFile());
            System.out.println("Port number: " + url.getPort());
            System.out.println("Default Port : " + url.getDefaultPort());
            System.out.println("Query : " + url.getQuery());
            System.out.println("Path: " + url.getPath());
            System.out.println( http.getHeaderField(0));
            System.out.println( http.getResponseCode());

            URL_Sample sample = new URL_Sample();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
