
package Servidor;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class thread extends Thread
{
     Socket scli=null;
     Socket scli2=null;
     DataInputStream entrada=null;
     DataOutputStream salida=null;
     DataOutputStream salida2=null;
     public static Vector<thread> clientesActivos=new Vector();	
     String nombreUsuario;
     Servidor serv;
     
     public thread(Socket scliente,Socket scliente2,Servidor serv)
     {
        scli=scliente;
        scli2=scliente2;
        this.serv=serv;
        nombreUsuario="";
        clientesActivos.add(this);        
        serv.log("cliente agregado: "+this);			
     }
     
     public String getnombreUsuario()
     {
       return nombreUsuario;
     }
     
     public void setnombreUsuario(String name)
     {
       nombreUsuario=name;
     }
     
     public void run()
     {
    	 int opcion=0;
         int numUsuarios=0;
         String cliente_inbox="";
         String msg="";
    	serv.log("Esperando Mensajes :");
    	
    	try
    	{
          entrada=new DataInputStream(scli.getInputStream());
          salida=new DataOutputStream(scli.getOutputStream());
          salida2=new DataOutputStream(scli2.getOutputStream());
          this.setnombreUsuario(entrada.readUTF());
          enviaUserActivos();
    	}
    	catch (IOException e) {  e.printStackTrace();     }
    	
        
                
    	while(true)
    	{
          try
          {
             opcion=entrada.readInt();
             switch(opcion)
             {
                case 1:// todos
                   msg=entrada.readUTF();
                   serv.log("mensaje recibido "+msg);
                   enviaMsg(msg);
                   break;
                case 2://conectados
                   numUsuarios=clientesActivos.size();
                   salida.writeInt(numUsuarios);
                   for(int i=0;i<numUsuarios;i++)
                      salida.writeUTF(clientesActivos.get(i).nombreUsuario);
                   break;
                case 3: // inbox
                   cliente_inbox=entrada.readUTF();//captura nombre de cliente_inbox
                   msg=entrada.readUTF();//mensage enviado
                   enviaMsg(cliente_inbox,msg);
                   break;
             }
          }
          catch (IOException e) {System.out.println("El cliente termino la conexion");break;}
    	}
    	serv.log("Se removio un usuario");
    	clientesActivos.removeElement(this);
    	try
    	{
          serv.log("Se desconecto un usuario");
          scli.close();
    	}	
        catch(Exception et)
        {serv.log("no se puede cerrar el socket");}   
     }
     
     public void enviaMsg(String msg2)
     {
        thread user=null;
        for(int i=0;i<clientesActivos.size();i++)
        {
           serv.log("MENSAJE DEVUELTO:"+msg2);
           try
            {
              user=clientesActivos.get(i);
              user.salida2.writeInt(1);//opcion de mensage 
              user.salida2.writeUTF(""+this.getnombreUsuario()+" >"+ msg2);              
            }catch (IOException e) {e.printStackTrace();}
        }
     }
     public void enviaUserActivos()
     {
        thread user=null;
        for(int i=0;i<clientesActivos.size();i++)
        {           
           try
            {
              user=clientesActivos.get(i);
              if(user==this)continue;//ya se lo envie
              user.salida2.writeInt(2);//opcion de agregar 
              user.salida2.writeUTF(this.getnombreUsuario());	
            }catch (IOException e) {e.printStackTrace();}
        }
     }

   private void enviaMsg(String cliente_inbox, String msg) 
   {
      thread user=null;
        for(int i=0;i<clientesActivos.size();i++)
        {           
           try
            {
              user=clientesActivos.get(i);
              if(user.nombreUsuario.equals(cliente_inbox))
              {
                 user.salida2.writeInt(3);//opcion de mensage cliente_inbox   
                 user.salida2.writeUTF(this.getnombreUsuario());
                 user.salida2.writeUTF(""+this.getnombreUsuario()+">"+msg);
              }
            }catch (IOException e) {e.printStackTrace();}
        }
   }
}