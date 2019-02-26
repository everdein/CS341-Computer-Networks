// Matthew Clark
// CS341 - Computer Networks
// Assignment 1 - Socket

import java.io.*;
import java.net.*;

class SocketGet 
{
    // main entry point for the application
    public static void main(String args[]) 
    {
        try 
        {
        	// URL
        	URL httpUrl = new URL("http://www.williammortl.com/index.html");

            // Open Socket
            Socket httpSocket = new Socket(httpUrl.getHost(), httpUrl.getDefaultPort());

            // Opens write and read streams.
            PrintWriter request = new PrintWriter(httpSocket.getOutputStream(), true);
            BufferedReader response = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));

            // HTTP "GET" request.
            String httpHeader = "GET " + httpUrl + " HTTP/1.1\r\nAccept: */*\r\nAccept-Language: en-us\r\nHost: localhost\r\nConnection: close\r\n\r\n";
            System.out.println(httpHeader);
            // send the HTTP request
            request.println(httpHeader);

            // Read the reply and print.
            String responseStr = response.readLine();
            while ((responseStr != null) && (responseStr != ""))
            {   
                System.out.println(responseStr);
                responseStr = response.readLine();
            } 

            // Close socket
            httpSocket.close();
        } 
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know the hostname");
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't get I/O for the connection");
        }
    }
}
