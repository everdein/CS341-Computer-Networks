// Matthew Clark
// CS341 - Computer Networks
// Assignment 5 - Web Server

import java.io.*;
import java.net.*;

class WebServer 
{
    // this is the port the web server listens on
    private static final int PORT_NUMBER = 8080;

    // main entry point for the application
    public static void main(String args[]) 
    {
        try 
        {
            // open socket
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

            // start listener thread
            Thread listener = new Thread(new SocketListener(serverSocket));
            listener.start();

            // message explaining how to connect
            System.out.println("To connect to this server via a web browser, try \"http://127.0.0.1:8080/{url to retrieve}\"");

            // wait until finished
            System.out.println("Press enter to shutdown the web server...");
            Console cons = System.console(); 
            String enterString = cons.readLine();

            // kill listener thread
            listener.interrupt();

            // close the socket
            serverSocket.close();
        } 
        catch (Exception e) 
        {
            System.err.println("WebServer::main - " + e.toString());
        }
    }
}

class SocketListener implements Runnable 
{
    private ServerSocket serverSocket;

    public SocketListener(ServerSocket serverSocket)   
    {
        this.serverSocket = serverSocket;
    }

    // this thread listens for connections, launches a seperate socket connection
    //  thread to interact with them
    public void run() 
    {
        while(!this.serverSocket.isClosed())
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                Thread connection = new Thread(new SocketConnection(clientSocket));
                connection.start();
                Thread.yield();
            }
            catch(IOException e)
            {
                if (!this.serverSocket.isClosed())
                {
                    System.err.println("SocketListener::run - " + e.toString());
                }
            }
        }
    }
}

class SocketConnection implements Runnable 
{
    private final String HTTP_LINE_BREAK = "\r\n";

    private Socket clientSocket;

    public SocketConnection(Socket clientSocket)   
    {
        this.clientSocket = clientSocket;
    }

    // one of these threads is spawned and used to talk to each connection
    public void run() 
    {       
        try
        {
            BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter response = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.handleConnection(request, response);
        }
        catch(IOException e)
        {
            System.err.println("SocketConnection::run - " + e.toString());
        }
    }

    // TODO: implement your ATM protocol for this server within this method
    private void handleConnection(BufferedReader request, PrintWriter response)
    {
        try
        {
            /*
             *
             * TODO: implement your web server here
             * 
             * Tasks:
             * ------
             * 1.) Figure out how to parse the HTTP request header
             * 2.) Figure out how to open files
             * 3.) Figure out how to create an HTTP response header
             * 4.) Figure out how to return resources (i.e. files)
             * 
             */
            // EXAMPLE: code prints the web request
            String message = this.readHTTPHeader(request);
            System.out.println("Message:\r\n" + message);

            // returns text hello world
            response.println("Hello World!");
            
            // close the socket, no keep alives
            this.clientSocket.close();
        }
        catch(IOException e)
        {
            System.err.println("SocketConnection::handleConnection: " + e.toString());
        }
    }

    private String readHTTPHeader(BufferedReader reader)
    {
        String message = "";
        String line = "";
        while ((line != null) && (!line.equals(this.HTTP_LINE_BREAK)))
        {   
            line = this.readHTTPHeaderLine(reader);
            message += line;
        }
        return message;
    }

    private String readHTTPHeaderLine(BufferedReader reader)
    {
        String line = "";
        try 
        {
            line = reader.readLine() + this.HTTP_LINE_BREAK;
        }
        catch (IOException e) 
        {
            System.err.println("SocketConnection::readHTTPHeaderLine: " + e.toString());
            line = "";
        } 
        return line;
    }
}
