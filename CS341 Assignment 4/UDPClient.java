// Matthew Clark
// CS341 - Computer Networks
// Assignment 4 - UDP

import java.io.*;
import java.net.*;

class UDPClient
{
   public static void main(String args[]) throws Exception
   {
      BufferedReader response = new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");
      
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      
      System.out.println("Welcome to the UDPClient!");
      System.out.println("Send a message to the UDPServer.");
      String message = response.readLine();
      sendData = message.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8080);
      clientSocket.send(sendPacket);
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);
      String modifiedSentence = new String(receivePacket.getData());
      System.out.println("Server Message: " + modifiedSentence);
      
      // Close socket. 
      clientSocket.close();
   }
}