

package Cliente;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Cliente
{
   public static String IP_SERVER = "192.168.1.68";
   Interface interfaceCliente;
   Socket socketNet = null;//para la socketNet
   Socket socketMensaje = null;//para recivir msg
   DataInputStream entrada = null;
   DataOutputStream salida = null;
   DataInputStream entrada2 = null;
   
   
   String nomCliente;
   /** Creates a new instance of Cliente */
   public Cliente(Interface interfaceCliente) throws IOException
   {      
	   
      this.interfaceCliente=interfaceCliente;
   }
   
   public void conexion() throws IOException 
   {
      try {
         socketNet = new Socket(Cliente.IP_SERVER, 8081);
         socketMensaje = new Socket(Cliente.IP_SERVER, 8082);
         entrada = new DataInputStream(socketNet.getInputStream());
         salida = new DataOutputStream(socketNet.getOutputStream());
         entrada2 = new DataInputStream(socketMensaje.getInputStream());
         nomCliente = JOptionPane.showInputDialog("Nombre de usuario :");
         interfaceCliente.setNombreUser(nomCliente);         
         salida.writeUTF(nomCliente);
      } catch (IOException e) {
         System.out.println("\tEl servidor no esta iniciado ");
         
      }
      new thread(entrada2, interfaceCliente).start();
   }
   public String getNombre()
   {
      return nomCliente;
   }
   public Vector<String> pedirUsuarios()
   {
      Vector<String> users = new Vector();
      try {         
         salida.writeInt(2);
         int numUsers=entrada.readInt();
         for(int i=0;i<numUsers;i++)
            users.add(entrada.readUTF());
      } catch (IOException ex) {
         Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
      }
      return users;
   }
   public void flujo(String mens) 
   {
      try {             
         System.out.println("el mensaje enviado desde el cliente es :"
             + mens);
         salida.writeInt(1);
         salida.writeUTF(mens);
      } catch (IOException e) {
         System.out.println("error...." + e);
      }
   }
   
   public void flujo(String amigo,String mens) 
   {
      try {             
         System.out.println("el mensaje enviado desde el cliente es :"
             + mens);
         salida.writeInt(3);//opcion de mensage a amigo
         salida.writeUTF(amigo);
         salida.writeUTF(mens);
      } catch (IOException e) {
         System.out.println("error...." + e);
      }
   }
   
  
}
