package kij_chat_client;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;
/**
Aes encryption
*/
public class AESEncryption
{
    
    private static SecretKeySpec secretKey ;
    private static byte[] key ;
    
    private static String decryptedString;
    private static String encryptedString;
    
    public static void setKey(String myKey){
        
   
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            System.out.println(key.length);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            System.out.println(key.length);
            System.out.println(new String(key,"UTF-8"));
            secretKey = new SecretKeySpec(key, "AES");
            
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
              
    
    }
    
    public static String getDecryptedString() {
        return decryptedString;
    }
    public static void setDecryptedString(String decryptedString) {
        AESEncryption.decryptedString = decryptedString;
    }
    public static String getEncryptedString() {
        return encryptedString;
    }
    public static void setEncryptedString(String encryptedString) {
        AESEncryption.encryptedString = encryptedString;
    }
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
         
            setEncryptedString(Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
        
        }
        catch (Exception e)
        {
           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt.getBytes()))));
           
            
        }
        catch (Exception e)
        {
         
            System.out.println("Error while decrypting: "+e.toString());
        }
        return null;
    }
    
    public static String generateKey() throws NoSuchAlgorithmException{
        KeyGenerator key = KeyGenerator.getInstance("AES");
        key.init(256); 
        SecretKey secretKey = key.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }
    
    public static void main(String args[]) throws NoSuchAlgorithmException
    {
                final String strToEncrypt = "My text to encrypt";
                
                String key = AESEncryption.generateKey();
                //final String strPssword = "encryptor key";
                AESEncryption.setKey(key);
               
                AESEncryption.encrypt(strToEncrypt.trim());
                
                System.out.println("String to Encrypt: " + strToEncrypt); 
                System.out.println("Encrypted: " + AESEncryption.getEncryptedString());
                System.out.println(key);
           
                final String strToDecrypt =  AESEncryption.getEncryptedString();
                AESEncryption.decrypt(strToDecrypt.trim());
               
                System.out.println("String To Decrypt : " + strToDecrypt);
                System.out.println("Decrypted : " + AESEncryption.getDecryptedString());
        
    }
     
}
  