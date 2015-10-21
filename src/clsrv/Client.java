package clsrv;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
public class Client {
    static public void main(String[] args) {
        try {
            
            // Read key
             System.out.println("\nCreating key for RC4 CPHR server side - Tutorial 3 _ outra vez \n");
             FileInputStream kfis = new FileInputStream("key");
             byte[] key = new byte[16]; 
             kfis.read(key); 
             kfis.close();

            // Connect to server
            Socket s = new Socket("127.0.0.1",4569);
            
            System.out.println("Connected to server...");
            
            BufferedReader stdIn=new BufferedReader (new InputStreamReader( s.getInputStream()));
            System.out.println("Ouvi o servidor");
            System.out.println("Response from server:");
            String userInput;
            userInput = stdIn.readLine();
                   
             System.out.println("Input:" + userInput); 
             
             if(userInput.equals("RC4"))
             {
                Cipher cipher = Cipher.getInstance(userInput);
                SecretKeySpec sk = new SecretKeySpec(key, userInput);

                //Inicia a cipher para encriptar

                cipher.init(Cipher.ENCRYPT_MODE, sk);

                Socket s_novo = new Socket("127.0.0.1",4567);      
                System.out.println("Connected to server...");
                 // Open file to upload
                FileInputStream fis = new FileInputStream(args[0]);
                System.out.println("Abre ficheiro:" + fis);
                // Get socket output stream
                OutputStream sos = s_novo.getOutputStream();


                // Upload file 100 bytes at a time
                byte[] buffer = new byte[100];
                int bytes_read = fis.read(buffer);

                while (bytes_read == 100) {
                    sos.write(cipher.update(buffer));
                    bytes_read = fis.read(buffer);
                }


                sos.write(cipher.doFinal(buffer,0,bytes_read));

                System.out.println("Disconnected from server.");

                // Close socket
                sos.close();

                // Close file
                fis.close(); 

                 
             } 
             else if(userInput.equals("AES"))
             {
                System.out.println("Nada desta bida");
                Cipher cipher = Cipher.getInstance(userInput);
                SecretKeySpec sk = new SecretKeySpec(key, userInput);
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); 
                byte[] iv = new byte[16];
                sr.nextBytes(iv);
                
                Socket s_novo = new Socket("127.0.0.1",4567);      
                System.out.println("Connected to server...");
                 // Open file to upload
                FileInputStream fis = new FileInputStream(args[0]);
                System.out.println("Abre ficheiro:" + fis);
                // Get socket output stream
                OutputStream sos = s_novo.getOutputStream();
                
                
                sos.write(iv);
                sos.flush();
                
                IvParameterSpec ivSpec=new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, sk, ivSpec );

               


                // Upload file 100 bytes at a time
                byte[] buffer = new byte[100];
                int bytes_read = fis.read(buffer);

                while (bytes_read == 100) {
                    sos.write(cipher.update(buffer));
                    bytes_read = fis.read(buffer);
                }


                sos.write(cipher.doFinal(buffer,0,bytes_read));

                System.out.println("Disconnected from server.");

                // Close socket
                sos.close();

                // Close file
                fis.close(); 
                 
                 
             }
                
          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
