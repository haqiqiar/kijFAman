/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

/**
 * @author haqiqi
 *
 */
public class RSAEncryption {

    public static final String ALGORITHM = "RSA";

    public static final String PRIVATE_KEY_FILE = "keys/private.key";

    public static final String PUBLIC_KEY_FILE = "keys/public.key";

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static KeyPair generateKey() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            final KeyPair key = keyGen.generateKeyPair();
            
            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text : original plain text
     * @param key :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static String encrypt(String text, PublicKey key) {
//        byte[] cipherText = null;
        String ret = "";
        try {
            int block_size = 50;
            List<String> datas = new ArrayList<>();

            for (int start = 0; start < text.length(); start += block_size) {
                datas.add(text.substring(start, Math.min(text.length(), start + block_size)));
            }

            for(String data : datas) {
                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                // encrypt the plain text using the public key
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] cipherText = cipher.doFinal(data.getBytes());
                ret += Base64.getEncoder().encodeToString(cipherText);
            }
//            System.out.println("encrypt : " + cipherText.length + '\n' + new String(cipherText));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String ret = Base64.encode(cipherText);
//        System.out.println("encoded : " + ret.length() + '\n' + ret);
        return ret;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text :encrypted text
     * @param key :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(String text, PrivateKey key) {
        String ret = "";
        try {
            String[] datas = text.split("==");
            for(int i=0; i<datas.length; i++) {
                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                String data = datas[i];
                data += ("==");
                byte[] text_bytes = Base64.getDecoder().decode(data);

                // decrypt the text using the private key
                byte[] decryptedText = cipher.doFinal(text_bytes);
                ret += new String(decryptedText);
//                System.out.println("data res : " + data);
//                System.out.println("string res : " + new String(decryptedText));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        return new String(decryptedText);
        return ret;
    }

}
