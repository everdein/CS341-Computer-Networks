// Matthew Clark
// CS341 - Computer Networks
// Assignment 4 - UDP

import java.net.*;

class UDPServer
{
	public static void main(String args[]) throws Exception
    {
		DatagramSocket serverSocket = new DatagramSocket(8080);
		
		byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        
		System.out.println("Welcome to the UDPServer!");
		boolean x = true;
        while(x)
        {
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String message = new String(receivePacket.getData());
        	System.out.println("Client Message: " + message);
        	InetAddress IPAddress = receivePacket.getAddress();
        	int port = receivePacket.getPort();
        	sendData = message.getBytes();
        	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			x = false;
		}
		serverSocket.close();
    }
}