// Matthew Clark
// CS341 - Computer Networks
// Assignment 2 - Application Layer Protocol

import java.io.*;
import java.util.*;
import java.net.*;

class ATMClient 
{

    // main entry point for the application
    public static void main(String args[]) 
    {
    	
        try 
        {
        		// Create scanner.
			Scanner console = new Scanner (System.in);
			
        		// Open socket.
        		Socket httpSocket = new Socket("127.0.0.1", 8080);
        		
            // Open up streams to write to and read from.
            BufferedReader response = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
            PrintWriter request = new PrintWriter(httpSocket.getOutputStream(), true);
            
            // Read and print the reply.
            String responseStr = response.readLine();
            System.out.println(responseStr);

            // Declares operation and balance variables.
            int operation = 0;
            double balance = 0.0;
            
	    		// Requests users credentials.
	    		System.out.println("Please enter your Username:");
	    		String username = console.next();
	    		System.out.println("Please enter your Pin:");
	    		int pin = console.nextInt();
	    		request.println("Login" + "@" + username + "@" + pin);
	    		String token = response.readLine();
	    		
	    		do 
	    		{
	    			
		    		// Prompts user with operations to choose from.
	    			System.out.println("Please choose from the following operations:");
	    			System.out.println("Deposit: Enter 1, Withdraw: Enter 2, Balance: Enter 3, or Logout: Enter 4.");
		    		operation = console.nextInt();
		    		
		    		// Switch case operations Login, Deposit, Withdraw, Balance and Logout.
	    			switch(operation)
	    			{
	    			
			        // Performs deposit operation.
	    				case 1:
	    					System.out.println("How much would you like to deposit?");
			        		balance = 0.0;
	    					double amount = console.nextDouble();
			        		if(amount > 50000) 
			        		{
			        			System.out.println("The maximum amount to deposit is $50,0000.");
			        		}
			        		else
			        		{
			    	    			request.println("Deposit" + "@" + token + "@" + amount);
			    	    			balance = Double.parseDouble(response.readLine());
			    	    			System.out.println("Current account balance = " + balance + ".");
			    	    			token = response.readLine();
			        		}
			        		break;
			        		
			        // Performs withdraw operation.
	    				case 2:
	    					System.out.println("How much would you like to withdraw?");
			        		amount = console.nextDouble();
			        			if(amount <= balance)
			        			{
			        				if(amount > 500) {
			        					System.out.println("The maximum amount to withdraw is $500.");
			        				}
			        				else 
			        				{
				    	    				request.println("Withdraw" + "@" + token + "@" + amount);
					    	    			balance = Double.parseDouble(response.readLine());
					    	    			System.out.println("Current account balance = " + balance + ".");
					    	    			token = response.readLine();
			        				}
			        			}
			        			else
			        			{
			        				System.out.println("Insuficient funds.");
			        			}
			        		break;
			        		
			        // Performs balance operation.
	    				case 3:
	    	    				request.println("Balance" + "@" + token);
		    	    			balance = Double.parseDouble(response.readLine());
		    	    			System.out.println("Current account balance = " + balance + ".");
		    	    			token = response.readLine();
		    	    			break;
			        		
			        	// Performs logout operation.
	    				case 4:
	    					request.println("Logout" + "@" + token);
	    					System.out.println("Thank you for banking with Bitcoin! Goodbye " + username + "!");
			        		break;
			        	
			        	// Default switch case.
	    				default:
	    					System.out.println("Please enter a valid operation. Thank you.");
			        		break;
		        	}
	    		}
	    		
	    		// Exits the program when 4 is entered.
	    		while(operation != 4);
	    		
	        // Close scanner.
	        console.close();
	        
	        // Close socket.
	        httpSocket.close();
            
        } 
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know about host: hostname");
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
    }
}