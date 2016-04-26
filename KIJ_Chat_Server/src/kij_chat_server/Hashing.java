/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author fendy
 */
public class Hashing {
    
    public static String hashString(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(msg.getBytes());
        byte hash[]= md.digest();
        String hash_string = String.format("%032x", new java.math.BigInteger(1, hash));
        
        return hash_string;
    }
}
