// Matthew Clark
// CS341 - Computer Networks
// Assignment 2 - Application Layer Protocol

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

class AccountDB
{

    private volatile Map<String, Integer> usernamePin;

    private volatile Map<String, Double> usernameBalances;

    private volatile Map<String, String> tokenToUsername;

    // Main entry point for the application, this tests this class.
    public static void main(String args[]) 
    {
        AccountDB db = new AccountDB();

        // Test the class end to end for success.
        String token = db.Login("taylor", 4321);
        System.out.println(token);
        System.out.println(db.Balance(token));
        System.out.println(db.Withdraw(token, 100));
        System.out.println(db.Balance(token));
        db.Deposit(token, 100);
        System.out.println(db.Balance(token));
        db.Logout(token);

        // Verify that the token is now bad.
        System.out.println(db.Balance(token));

        // Verify that the login / pin combo works with a bad pin.
        token = db.Login("taylor", 4324);
        System.out.println("Should be empty string: " + token);

        // Verify cannot withdraw more than we have.
        token = db.Login("will", 1234);
        System.out.println(token);
        System.out.println(db.Balance(token));
        System.out.println(db.Withdraw(token, 10000));
    }

    // This is where accounts/pins/balances are initialized.
    public AccountDB()
    {
        // Create usernames and pins.
        this.usernamePin = new HashMap<String, Integer>();
        this.usernamePin.put("will", 1234);
        this.usernamePin.put("taylor", 4321);
        this.usernamePin.put("samus", 9999);

        // Create balances.
        this.usernameBalances = new HashMap<String, Double>();
        this.usernameBalances.put("will", 5040.12);
        this.usernameBalances.put("taylor", 19000713.19);
        this.usernameBalances.put("samus", 8123.41);

        this.tokenToUsername = new HashMap<String, String>();
    }

    // Login - generates and returns a token, if bad login returns "".
    public String Login(String username, int pin)
    {
        String retVal = "";
        if (this.usernamePin.containsKey(username))
        {
            int actualPin = usernamePin.get(username);
            if (pin == actualPin)
            {
                retVal = UUID.randomUUID().toString();
                this.tokenToUsername.put(retVal, username);
            }
        }
        return retVal;
    }

    // Logout (if token valid).
    public void Logout(String token)
    {
        if (this.tokenToUsername.containsKey(token))
        {
            this.tokenToUsername.remove(token);
        }
    }

    // Deposit money into an account (if token valid).
    public void Deposit(String token, double amount)
    {
        if (this.tokenToUsername.containsKey(token))
        {
            String username = this.tokenToUsername.get(token);
            Double balance = this.usernameBalances.get(username);
            this.usernameBalances.put(username, balance + amount);
        }
    }

    // Withdraw money from an account (if token valid & amount <= balance).
    public boolean Withdraw(String token, double amount)
    {
        boolean retVal = false;
        if (this.tokenToUsername.containsKey(token))
        {
            String username = this.tokenToUsername.get(token);
            if (this.usernameBalances.get(username) >= amount)
            {
                Double balance = this.usernameBalances.get(username);
                this.usernameBalances.put(username, balance - amount);
                retVal = true;
            }
        }
        return retVal;
    }

    // Returns the balance from an account (if token valid, if invalid token.
    // Returns -1.
    public double Balance(String token)
    {
        double retVal = -1;
        if (this.tokenToUsername.containsKey(token))
        {
            String username = this.tokenToUsername.get(token);
            retVal = this.usernameBalances.get(username);
        }
        return retVal;
    }
}