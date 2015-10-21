package clsrv;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.spec.IvParameterSpec;

public class Server {
    static public void main(String[] args) {
        try {           
            // Read key
             System.out.println("\nCreating key for RC4 CPHR server side - Tutorial 3_outra vez\n");
             FileInputStream kfis = new FileInputStream("key");
             byte[] key = new byte[16]; 
             kfis.read(key); 
             kfis.close();
            
             Scanner in = new Scanner(System.in);
             System.out.println("Escolha o tipo de especificação: 1- RC4 , 2 - os outros");
             int a = in.nextInt();
             System.out.println("e é isto: "+a);
             
             if(a==1)
             {
                SecretKeySpec sk = new SecretKeySpec(key, "RC4");

                //Inicia a cipher para encriptar
                Cipher cipher = Cipher.getInstance("RC4");
                cipher.init(Cipher.DECRYPT_MODE, sk);


               // Create server socket
               ServerSocket ss = new ServerSocket(4569);
               
               Socket s = ss.accept();
               
               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                   writer.write("RC4");
                   writer.flush();
                   writer.close();
                   System.out.println("Falei com o cliente");
                   
               s.close();
               
               ServerSocket ss_novo = new ServerSocket(4567);

               // Start upload counter
               int counter = 0;

               System.out.println("Server started ...");

               while(true) {
                   // Wait for client            
                   Socket s_novo = ss_novo.accept(); 
                   // Increment counter
             
                   System.out.println("Accepted connection "+counter+".");
                   counter++;

                   // Open file to write to
                   FileOutputStream fos = new FileOutputStream(args[0]+"/"+counter);
                   /*7System.out.println("é isto que sai do FileOutputStream");
                   System.out.println("fos" + fos);
                   System.out.println("args" + args[0]);
                   System.out.println("counter" + counter);*/
                   
                   // Get socket input stream
                   
                   InputStream sis = s_novo.getInputStream();
                   System.out.println("é isto que sai do InputStream");
                   System.out.println(sis);
                 
                   // Get file 50 bytes at a time
                   byte[] buffer = new byte[50];

                   //encriptando
                 ; //there's no more data

                   int bytes_read = sis.read(buffer);
                 
                   
                   System.out.println("primeiro byte lido");
                   System.out.println(bytes_read);
                   
                   while (bytes_read > 0) {
                      fos.write(cipher.update(buffer,0,bytes_read));
                      bytes_read = sis.read(buffer);
                   }

                   // Close socket
                   s_novo.close();
                   System.out.println("Closed connection.");

                   // Close file
                   cipher.doFinal();
                   fos.close();
               }
             }
             else
             {
                 System.out.println("Não tá a dar");
                 SecretKeySpec sk = new SecretKeySpec(key, "AES");

                //Inicia a cipher para encriptar
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
               //cipher.init(Cipher.DECRYPT_MODE, sk);


               // Create server socket
               ServerSocket ss = new ServerSocket(4569);
               Socket s = ss.accept();
               
               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                   writer.write("AES");
                   writer.flush();
                   writer.close();
                   System.out.println("Falei com o cliente");  
               s.close();
               
               
               
              
               SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); 
               byte[] iv = new byte[16];
               sr.nextBytes(iv);

              

               System.out.println("Server started ...");

                 //SecretKeySpec sk = new SecretKeySpec(key, "AES");
                 IvParameterSpec ivSpec=new IvParameterSpec(iv);
                 //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                 cipher.init(Cipher.DECRYPT_MODE,sk,ivSpec);
 
                 
                 //ServerSocket ss = new ServerSocket(4567);
            
            // Start upload counter
                int counter = 0;
                ServerSocket ss_novo = new ServerSocket(4567);

                System.out.println("Server started ... agora com AES");

                while(true) {
                    // Wait for client            
                    Socket s_novo= ss_novo.accept();
                    // Increment counter
                    counter++;
                    System.out.println("Accepted connection "+counter+"com AES.");
                    // Open file to write to
                    FileOutputStream fos = new FileOutputStream(args[0]+"/"+counter);
                    
                    // Get socket input stream
                    InputStream sis = s_novo.getInputStream();
                    // Get file 50 bytes at a time
                    byte[] buffer = new byte[50];

                    //encriptando
                  ; //there's no more data

                    int bytes_read = sis.read(buffer);
                    while (bytes_read > 0) {
                       fos.write(cipher.update(buffer,0,bytes_read));
                       bytes_read = sis.read(buffer);
                    }
                      // Close socket
                    s_novo.close();
                    System.out.println("Closed connection.");

                    // Close file
                    cipher.doFinal();
                    fos.close();
                    
                    System.out.println("cheguei ao fim");

                 }
             }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
}
