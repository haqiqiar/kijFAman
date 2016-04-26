/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
    private Scanner chat;
    private PrintWriter out;
    boolean keepGoing = true;
    ArrayList<String> log;

    public Write(Scanner chat, PrintWriter out, ArrayList<String> log)
    {
        this.chat = chat;
        this.out = out;
        this.log = log;
    }

    @Override
    public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
    {
        try
        {
            while (keepGoing)//WHILE THE PROGRAM IS RUNNING
            {						
                String input = chat.nextLine();//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                // ini harus diolah dulu
                out.println(RSAEncryption.encrypt(input, KeyHandler.getPublicKey("server")));//SEND IT TO THE SERVER
                out.flush();//FLUSH THE STREAM

                if (input.contains("logout")) {
                    if (log.contains("true"))
                        keepGoing = false;
                }
            }
        }
        catch (Exception e)
        {
                e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
        } 
    }
    
    private String process(String input) throws NoSuchAlgorithmException {
        String[] vals = input.split(" ");
        String message = input;
        
        // LOGIN username password
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("login")) {
            
        }
        
        // LOGOUT username
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("logout")) {
            
        }
        
        // PM destUsername ERSA(public_key_destUsername, message)
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("pm")) {
            
        }
        
        // CG group_name
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("cg")) {
//            String hash = Hashing.hashString(input);
//            message += hash;
        }
        
        // GM src_username dest_groupname
        // ERSA(public_key_server, [Message||hash])
        if(vals[0].toLowerCase().equals("gm")) {
            
        }
        
        // BM srcUsername message
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("bm")) {
            
        }
        
        String hash = Hashing.hashString(message);
        return message;
    }

}
