

package Servidor;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

class Servidor //extends JFrame
{
   JTextArea txalog;
   public Servidor()
   {
 
      
   }
   public void log(String msg)
   {
      
	   System.out.println(msg + "\n");
   }
   public void iniciar()
   {
      ServerSocket socketServidorNet=null;
      ServerSocket socketServerMensajes=null;
      boolean listening=true;
      try{
    	  socketServidorNet=new ServerSocket(8081);
    	  socketServerMensajes=new ServerSocket(8082);
         log("Iniciando servidor.............:");
         while(listening)
         {
            Socket socketNet=null;
            Socket socketMensaje=null;
            try {
               log(".........esperando conexiones.......");
               socketNet=socketServidorNet.accept();
               socketMensaje=socketServerMensajes.accept();
            } catch (IOException e)
            {
               log("socket accept  " + socketServidorNet + ", " + e.getMessage());
               continue;
            }
            thread usuario_administrado=new thread(socketNet,socketMensaje,this);            
	    usuario_administrado.start();
         }
         
      }catch(IOException e){
         log("error :"+e);
      }
   }
   
   public static void main(String abc[]) throws IOException
   {                
     Servidor ser= new Servidor();
     ser.iniciar();
   }
}



