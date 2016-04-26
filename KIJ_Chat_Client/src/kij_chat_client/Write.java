/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
                
                out.println(input);//SEND IT TO THE SERVER
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
    
    private String process(String input) {
        String[] vals = input.split(" ");
/*
        if(vals[0] == "pm"){
            byte[] encryptedWord = RSAEncryption.encrypt(vals[2], KeyHandler.getPublicKey(""));//Public key tujua
            String temp = new String(encryptedWord);
            input = vals[0] + vals[1] + temp;
        }
       byte[] encryptedMessage = RSAEncryption.encrypt(input, KeyHandler.getPublicKey("server"));
       String encrypted = new String(encryptedMessage);
       return encrypted;
*/
        String message = "";
        
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
        
        // CG username group_name
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("cg")) {
            
        }
        
        // GM src_username dest_groupname encrypted
        // ERSA(public_key_server, [Message||hash])
        if(vals[0].toLowerCase().equals("gm")) {
            
        }
        
        // BM srcUsername message
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("bm")) {
            
        }
        
        return message;
//>>>>>>> 42de7ca858c480c1fc5f0dcd625bf17d40c3ca3c
    }

}
