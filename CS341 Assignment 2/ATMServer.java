// Matthew Clark
// CS341 - Computer Networks
// Assignment 2 - Application Layer Protocol

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ATMServer 
{
    // This is the port the ATM server listens on.
    private static final int PORT_NUMBER = 8080;

    // Main entry point for the application.
    public static void main(String args[]) 
    {
        try 
        {
            // Create account db.
            AccountDB db = new AccountDB();

            // Open socket.
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

            // Start listener thread.
            Thread listener = new Thread(new SocketListener(serverSocket, db));
            listener.start();

            // Message explaining how to connect.
            System.out.println("To connect to this server to test, try \"telnet 127.0.0.1 8080\"");

            // Wait until finished.
            System.out.println("Press enter to quit...");
            Console cons = System.console(); 
            String enterString = cons.readLine();

            // Kill listener thread.
            listener.interrupt();

            // Close the socket.
            serverSocket.close();
        } 
        catch (Exception e) 
        {
            System.err.println("ATMServer::main: " + e.toString());
        }
    }
}

class SocketListener implements Runnable 
{
    private ServerSocket serverSocket;

    private AccountDB db;

    public SocketListener(ServerSocket serverSocket, AccountDB db)   
    {
        this.serverSocket = serverSocket;
        this.db = db;
    }

    // This thread listens for connections, launches a separate socket connection.
    // Thread to interact with them.
    public void run() 
    {
        while(!this.serverSocket.isClosed())
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                Thread connection = new Thread(new SocketConnection(clientSocket, db));
                connection.start();
                Thread.yield();
            }
            catch(IOException e)
            {
                if (!this.serverSocket.isClosed())
                {
                    System.err.println("SocketListener::run: " + e.toString());
                }
            }
        }
    }
}

class SocketConnection implements Runnable 
{
    private Socket clientSocket;
    
    private AccountDB db;

    public SocketConnection(Socket clientSocket, AccountDB db)   
    {
        this.clientSocket = clientSocket;
        this.db = db;
    }

    // One of these threads is spawned and used to talk to each connection.
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
            System.err.println("SocketConnection::run: " + e.toString());
        }
    }

    // Runs ATM protocol for this server within this method.
    private void handleConnection(BufferedReader request, PrintWriter response)
    {
        try
        {       	        	
        		// Initiates connection with ATMClient.java.
	    		response.println("Welcome to Bitcoin!");
	    		String string = "";
	        	String[] split = string.split("@", 3);
	        	
	        	do
	        	{
	        		
	        	// Reads message from connection and splits it every time an @ symbol is encountered.
		    	string = this.readMessageFromConnection(request);
		    	split = string.split("@", 3);
		    	switch(split[0])
		    {
		        	
		    		// Performs login operation
		    		case "Login":
		    			System.out.println("Login if statement.");
		    			String username = split[1];
		    			int pin = Integer.parseInt(split[2]);
		    			String token = this.db.Login(username, pin);
		    			response.println(token);
		    			break;

			    	// Performs deposit operation
			    	case "Deposit":
					System.out.println("Deposit if statement.");
			    			token = split[1];
			    			Double amount = Double.parseDouble(split[2]);
			    			this.db.Deposit(token, amount);
			    			response.println(this.db.Balance(token));
			    			response.println(token);
			    			break;

			    	// Performs withdraw operation
			    	case "Withdraw":
			    		System.out.println("Withdraw if statement.");
			    		token = split[1];
			    		amount = Double.parseDouble(split[2]);
			    		this.db.Withdraw(token, amount);
			    		response.println(this.db.Balance(token));
			    		response.println(token);
			    		break;

			    	// Performs balance operation
			    	case "Balance":
			    		System.out.println("Balance if statement.");
			    		token = split[1];
			    		response.println(this.db.Balance(token));
			    		response.println(token);
			    		break;

			    	// Performs logout operation
			    	case "Logout":
			    		System.out.println("Logout if statement.");
			    		token = split[0];
			    		this.db.Logout(token);
			    		break;
			    		
			    	// Default switch case.
			    	default:
					break;
		    		}
	        	} 
	        	
	        	// Exits the program when Logout is entered.
	        	while(!split[0].equals("Logout"));
			
	        	// close the socket
	        	this.clientSocket.close();
        }
        
        // Catches exception.
        catch(IOException e)
        {
            System.err.println("SocketConnection::handleConnection: " + e.toString());
        }
    
    }
    
    // Function to read message from connection.
    private String readMessageFromConnection(BufferedReader reader)
    {
        // Read and print the reply.
        String message = "";
        try 
        {
            message = reader.readLine();
        }
        catch (IOException e) 
        {
            System.err.println("SocketConnection::readMessageFromConnection: " + e.toString());
        } 
        return message;
    }

}
