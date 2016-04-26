package kij_chat_server;

import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */


public class Client implements Runnable{

    private Socket socket;//SOCKET INSTANCE VARIABLE
    private String username;
    private PublicKey public_key;
    private boolean login = false;

    private ArrayList<Pair<Socket,Pair<String, PublicKey>>> _loginlist;
    private ArrayList<Pair<String,String>> _userlist;
    private ArrayList<Pair<String,String>> _grouplist;

    public Client(Socket s, ArrayList<Pair<Socket,Pair<String,PublicKey>>> _loginlist, ArrayList<Pair<String,String>> _userlist, ArrayList<Pair<String,String>> _grouplist)
    {
        socket = s;//INSTANTIATE THE SOCKET)
        this._loginlist = _loginlist;
        this._userlist = _userlist;
        this._grouplist = _grouplist;
    }

    @Override
    public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
    {
        try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
        {
            Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
            PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)

            while (true)//WHILE THE PROGRAM IS RUNNING
            {		
                if (in.hasNext())
                {
                    String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//					System.out.println("Client Said: " + input);//PRINT IT OUT TO THE SCREEN
//					out.println("You Said: " + input);//RESEND IT TO THE CLIENT
//					out.flush();//FLUSH THE STREAM

                    // param HELLO <public_key> <hash>
                    if (input.split(" ")[0].toLowerCase().equals("hello") == true) {
                        String[] vals = input.split(" ");

//                        System.out.println(vals[1]);
                        this.public_key = stringToPublic(vals[1]);

                        // send back server public_key
                        String message = "HELLO " + KeyHandler.getPublic_key_string();
                        String hash = Hashing.hashString(message);

                        out.println(message + " " + hash);
                        out.flush();
                        
                        return;
                    }
                    
//                    byte[] byte_to_decrypt = Base64.getDecoder().decode(input);
//                    input = RSAEncryption.decrypt(input, KeyHandler.getPrivate_key());
                    // param LOGIN <userName> <pass> 
                    if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                        String[] vals = input.split(" ");

                        if (this._userlist.contains(new Pair(vals[1], vals[2])) == true) {
                            if (this.login == false) {
                                this._loginlist.add(new Pair(this.socket, new Pair(vals[1], this.public_key)));
                                this.username = vals[1];
                                this.login = true;
                                System.out.println("Users count: " + this._loginlist.size());
                                out.println("SUCCESS login");
                                out.flush();
                            } else {
                                out.println("FAIL login");
                                out.flush();
                            }
                        } else {
                            out.println("FAIL login");
                            out.flush();
                        }
                        
                        return;
                    }

                    // param LOGOUT
                    if (input.split(" ")[0].toLowerCase().equals("logout") == true) {
                        String[] vals = input.split(" ");

                        if (this._loginlist.contains(new Pair(this.socket, this.username)) == true) {
                            this._loginlist.remove(new Pair(this.socket, this.username));
                            System.out.println(this._loginlist.size());
                            out.println("SUCCESS logout");
                            out.flush();
                            this.socket.close();
                            break;
                        } else {
                            out.println("FAIL logout");
                            out.flush();
                        }
                        
                        return;
                    }

                    // param PM <userName dst> <message>
                    if (input.split(" ")[0].toLowerCase().equals("pm") == true) {
                        String[] vals = input.split(" ");

                        boolean exist = false;

                        for(Pair<Socket, Pair<String, PublicKey>> cur : _loginlist) {
                            if (cur.getSecond().getFirst().equals(vals[1])) {
                                PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                String messageOut = "";
                                for (int j = 2; j<vals.length; j++) {
                                    messageOut += vals[j] + " ";
                                }
                                System.out.println(this.username + " to " + vals[1] + " : " + messageOut);
                                outDest.println(this.username + ": " + messageOut);
                                outDest.flush();
                                exist = true;
                            }
                        }

                        if (exist == false) {
                            System.out.println("pm to " + vals[1] + " by " + this.username + " failed.");
                            out.println("FAIL pm");
                            out.flush();
                        }
                        
                        return;
                    }

                    // param CG <groupName>
                    if (input.split(" ")[0].toLowerCase().equals("cg") == true) {
                        String[] vals = input.split(" ");

                        boolean exist = false;

                        for(Pair<String, String> selGroup : _grouplist) {
                            if (selGroup.getFirst().equals(vals[1])) {
                                exist = true;
                            }
                        }

                        if(exist == false) {
                            Group group = new Group();
                            int total = group.updateGroup(vals[1], this.username, _grouplist);
                            System.out.println("total group: " + total);
                            System.out.println("cg " + vals[1] + " by " + this.username + " successed.");
                            out.println("SUCCESS cg");
                            out.flush();
                        } else {
                            System.out.println("cg " + vals[1] + " by " + this.username + " failed.");
                            out.println("FAIL cg");
                            out.flush();
                        }
                        
                        return;
                    }

                    // param GM <groupName> <message>
                    if (input.split(" ")[0].toLowerCase().equals("gm") == true) {
                        String[] vals = input.split(" ");

                        boolean exist = false;

                        for(Pair<String, String> selGroup : _grouplist) {
                            if (selGroup.getSecond().equals(this.username)) {
                                exist = true;
                            }
                        }

                        if (exist == true) {
                            for(Pair<String, String> selGroup : _grouplist) {
                                if (selGroup.getFirst().equals(vals[1])) {
                                    for(Pair<Socket, Pair<String, PublicKey>> cur : _loginlist) {
                                        if (cur.getSecond().getFirst().equals(selGroup.getSecond()) && !cur.getFirst().equals(socket)) {
                                            PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                            String messageOut = "";
                                            for (int j = 2; j<vals.length; j++) {
                                                messageOut += vals[j] + " ";
                                            }
                                            System.out.println(this.username + " to " + vals[1] + " group: " + messageOut);
                                            outDest.println(this.username + " @ " + vals[1] + " group: " + messageOut);
                                            outDest.flush();
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("gm to " + vals[1] + " by " + this.username + " failed.");
                            out.println("FAIL gm");
                            out.flush();
                        }
                        
                        return;
                    }

                    // param BM <message>
                    if (input.split(" ")[0].toLowerCase().equals("bm") == true) {
                        String[] vals = input.split(" ");

                        for(Pair<Socket, Pair<String, PublicKey>> cur : _loginlist) {
                            if (!cur.getFirst().equals(socket)) {
                                PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                String messageOut = "";
                                for (int j = 1; j<vals.length; j++) {
                                    messageOut += vals[j] + " ";
                                }
                                System.out.println(this.username + " to alls: " + messageOut);
                                outDest.println(this.username + " <BROADCAST>: " + messageOut);
                                outDest.flush();
                            }
                        }
                        
                        return;
                    }
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
        }	
    }
    
    private PublicKey stringToPublic(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] public_key_bytes = Base64.getDecoder().decode(str);
        return KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(public_key_bytes));
    }
}


