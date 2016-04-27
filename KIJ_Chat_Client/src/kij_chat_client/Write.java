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
                System.out.println(input);
                if(!vals[0].toLowerCase().equals("hello")) {
                    input = RSAEncryption.encrypt(process(input), 
                            KeyHandler.getPublicKey("server"));
                }
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
    
    private String process(String input) throws NoSuchAlgorithmException, InterruptedException {
        String[] vals = input.split(" ");
        String message = input;
        
        // PM destUsername ERSA(public_key_destUsername, message)
        // ERSA(public_key_server, [message || hash])
        if(vals[0].toLowerCase().equals("pm")) {
            if(KeyHandler.getPublicKey(vals[1]) == null) {
                String msg = "RQ " + vals[1];
                String hash = Hashing.hashString(msg);
                
                out.println(RSAEncryption.encrypt(msg + ' ' + hash, KeyHandler.getPublicKey("server")));
                out.flush();
            }
            
            Thread.sleep(1000);
            
            message = vals[0] + " " + 
                    vals[1] + " " +
                    RSAEncryption.encrypt(vals[2], KeyHandler.getPublicKey(vals[1]));
        }

        String hash = Hashing.hashString(message);
        return message + ' ' + hash;
    }

}
