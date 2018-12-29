/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bogdan Vaskan cs161181
 */

import java.io.*;
import java.net.*;

public class UDPClient {
   public static void main(String args[]) throws Exception {
        String hostName = "";
        int portNumber = 8888;

        if (args.length == 1) {
           hostName = args[0];
        }
        else {
            System.out.println("Usage: java EchoClient <host name> <port number>");
            System.out.println("The default values for host and port are 127.0.0.1 and 8888\n");
        }

      DatagramSocket clientSocket = new DatagramSocket();
      clientSocket.setSoTimeout(1000);
      InetAddress IPAddress = InetAddress.getLocalHost();

      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];

      // change string to bytes to send data
      sendData = hostName.getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
      clientSocket.send(sendPacket);
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

      try {
        clientSocket.receive(receivePacket);
        String srvResponce = new String(receivePacket.getData());
        System.out.println("The server response :" + srvResponce);
      }
      catch (SocketTimeoutException e) {
           System.out.println("Timeout reached!!! " + e);
         }
        clientSocket.close();
    }
}
