/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import static com.sun.imageio.plugins.common.LZWStringTable.hash;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import static java.util.Objects.hash;
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
    
    Hashing hash = new Hashing();
    
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
                // ini harus diolah duluf
                String[] vals = input.split(" ");
                if(!vals[0].toLowerCase().equals("hello"))
                    input = RSAEncryption.encrypt(process(input), 
                            KeyHandler.getPublicKey("server"));//SEND IT TO THE SERVER
                out.println(input);
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
       // String message = "";

        String message = input;
        
        // LOGIN username password
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("login")) {
            //hashing password
            Hashing hashing = new Hashing();
            String hashResult = Hashing.hashString(vals[2]);
            
            
            
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
        

        //return message;
//>>>>>>> 42de7ca858c480c1fc5f0dcd625bf17d40c3ca3c

        String hash = Hashing.hashString(message);
        return message + ' ' + hash;

    }

}
