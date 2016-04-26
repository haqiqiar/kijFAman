/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
	
	public Read(Scanner in, ArrayList<String> log)
	{
            this.in = in;
            this.log = log;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
            try
            {
                while (keepGoing)//WHILE THE PROGRAM IS RUNNING
                {						
                    if(this.in.hasNext()) {
                            //IF THE SERVER SENT US SOMETHING
                        input = this.in.nextLine();
                        if (!input.split(" ")[0].toLowerCase().equals("hello")) {
                            System.out.println(input);//PRINT IT OUT
                        } else {
                            // HELLO <public_key>
                            String[] vals = input.split(" ");
                            KeyHandler.putPublicKey("server", stringToPublic(vals[1]));
                            
                            // System.out.println(KeyHandler.getPublicKey("server"));
                        }
                        if (input.split(" ")[0].toLowerCase().equals("success")) {
                            if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                keepGoing = false;
                            } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                                log.clear();
                                log.add("true");
                            }
                        }

                    }

                }
            }
            catch (Exception e)
            {
                    e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
            } 
	}

    private PublicKey stringToPublic(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] public_key_bytes = Base64.getDecoder().decode(str);
        return KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(public_key_bytes));
    }
}
