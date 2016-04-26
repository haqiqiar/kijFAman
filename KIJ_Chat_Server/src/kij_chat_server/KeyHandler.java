/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 *
 * @author fendy
 */
public class KeyHandler {
    private static PublicKey public_key;
    private static PrivateKey private_key;
    private static String public_key_string;
    private static String private_key_string;
    
    public static void generateServerKey() {
        KeyPair kp = RSAEncryption.generateKey();
        
        KeyHandler.setPublic_key(kp.getPublic());
        KeyHandler.setPrivate_key(kp.getPrivate());
        
        KeyHandler.setPublic_key_string(Base64.getEncoder().
                encodeToString(kp.getPublic().getEncoded()));
        KeyHandler.setPrivate_key_string(Base64.getEncoder().
                encodeToString(kp.getPrivate().getEncoded()));
    }

    /**
     * @return the public_key
     */
    public static PublicKey getPublic_key() {
        return public_key;
    }

    /**
     * @param aPublic_key the public_key to set
     */
    public static void setPublic_key(PublicKey aPublic_key) {
        public_key = aPublic_key;
    }

    /**
     * @return the private_key
     */
    public static PrivateKey getPrivate_key() {
        return private_key;
    }

    /**
     * @param aPrivate_key the private_key to set
     */
    public static void setPrivate_key(PrivateKey aPrivate_key) {
        private_key = aPrivate_key;
    }

    /**
     * @return the public_key_string
     */
    public static String getPublic_key_string() {
        return public_key_string;
    }

    /**
     * @param aPublic_key_string the public_key_string to set
     */
    public static void setPublic_key_string(String aPublic_key_string) {
        public_key_string = aPublic_key_string;
    }

    /**
     * @return the private_key_string
     */
    public static String getPrivate_key_string() {
        return private_key_string;
    }

    /**
     * @param aPrivate_key_string the private_key_string to set
     */
    public static void setPrivate_key_string(String aPrivate_key_string) {
        private_key_string = aPrivate_key_string;
    }
}
