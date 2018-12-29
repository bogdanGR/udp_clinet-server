/**
 *
 * @author Bogdan Vaskan cs161181
 */
import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

public class UDPServer {
   public static void main(String args[]) throws Exception {
     String myPath = "C:\\Users\\fant1\\Desktop\\clientserver\\hosts.txt";
     FileReader reader  = new FileReader(myPath);
     BufferedReader buffReader = new BufferedReader(reader);

      FileWriter writer = new FileWriter(myPath, true);
      BufferedWriter buffWriter  = new BufferedWriter(writer);
      PrintWriter out = new PrintWriter(buffWriter);

      String line;
      InetAddress address;
      Hashtable dnsNames = new Hashtable();
      int portNumber = 8888;

      if(args.length == 0) {
         System.out.println("Usage: java UDPServer <port number>");
         System.out.println("The default port is 8888\n");
      }
      else {
          portNumber = Integer.parseInt(args[0]);
      }

      System.out.println("The UDP Server  is running....");
      DatagramSocket serverSocket = new DatagramSocket(portNumber);

      byte[] receiveData = new byte[1024];
      byte[] sendData = new byte[1024];

      while(true) {
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          serverSocket.receive(receivePacket);
          String sentence = new String( receivePacket.getData(), 0, receivePacket.getLength());
          System.out.println("Your message is: " + sentence);
          int i = 0;
          //reading data from the file when server starts
          while ((line = buffReader.readLine()) != null) { //while we have lines
                 System.out.println(line); //show line in console for debugging
                 String[] tokens = line.split(" "); //split file to tokens
                 dnsNames.put(tokens[0], tokens[1]);
                 System.out.println(dnsNames);
                 i++;
              }
              buffReader.close();
              InetAddress IPAddress = receivePacket.getAddress();

              int port = receivePacket.getPort();
          //if theare isn't hostname then
          //Add to the Hashtable and file hosts.txt
          if(!(dnsNames.containsKey(sentence))) {
            System.out.println("There is not hostname"); // FOR DEBUGGING ONLY!
            address = InetAddress.getByName(sentence);
            String[] ipTokens = address.toString().split("/"); //split file to tokens
            dnsNames.put(ipTokens[0], ipTokens[1]);
            String putLine = ipTokens[0] + " " + ipTokens[1] + "\n";
            out.write(putLine);
            String strServerResponce = ipTokens[1].toString();

            sendData = strServerResponce.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);

          } else {
             System.out.println("There is hostname"); // FOR DEBUGGING ONLY!
             //send IP address of hostname
             String strServerResponce = dnsNames.get(sentence).toString();
             sendData = strServerResponce.getBytes();
             DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
             serverSocket.send(sendPacket);
          }
          out.close();
        }
      }
}
