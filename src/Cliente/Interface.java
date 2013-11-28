

package Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.JOptionPane.*;


public class Interface extends JFrame implements ActionListener {
     private String mensaje_;
     
      
     

     
     //controles
     private JTextArea historial;
     private JTextField txtfmensaje;
     private JButton btnenviar;
     private JLabel lblUsuarios;
     private JList lstActivos;
     private JButton btnInbox;
     	
     
      //cambiar
     // private VentanaAyuda va;
      
      //paneles
      //private JOptionPane AcercaDe;
     
      //variables de instancia
      
      private Cliente cliente;
      Inbox privado;
      
      // listas
      
      Vector<String> nicks;
     
     /** Creates a new instance of Cliente */
     public Interface() throws IOException {
             
                          
             
            
             
             txtfmensaje = new JTextField(30);
             btnenviar = new JButton("Enviar");
             
             lstActivos=new JList();             
             btnInbox=new JButton("Inbox");
             
             lblUsuarios = new JLabel("Usuario <<  >>");
             
             
             historial = new JTextArea();
             
             
             
            
             
             
             lblUsuarios.setHorizontalAlignment(JLabel.CENTER);
             
             historial.setColumns(50);
             btnenviar.addActionListener(this);
             
             
             
           
             
             
             txtfmensaje.addActionListener(this);
             btnInbox.addActionListener(this);
            
             
             
             historial.setEditable(false);            
             historial.setForeground(Color.BLUE);
             		

             JPanel panel_enviar = new JPanel();
             panel_enviar.setLayout(new BorderLayout());
                
                panel_enviar.add(txtfmensaje, BorderLayout.CENTER);
                panel_enviar.add(btnenviar, BorderLayout.EAST);
             JPanel panel_mensajes = new JPanel();
             panel_mensajes.setLayout(new BorderLayout());
                panel_mensajes.add(lblUsuarios, BorderLayout.NORTH);
                panel_mensajes.add(new JScrollPane(historial), BorderLayout.CENTER);
                panel_mensajes.add(panel_enviar,BorderLayout.SOUTH);
             JPanel panel_usuarios=new JPanel();
             panel_usuarios.setLayout(new BorderLayout());
               panel_usuarios.add(new JScrollPane(this.lstActivos),BorderLayout.CENTER);
               panel_usuarios.add(this.btnInbox,BorderLayout.NORTH);
             JSplitPane sldCentral=new JSplitPane();  
             sldCentral.setDividerLocation(800);
             
               sldCentral.setLeftComponent(panel_mensajes);
               sldCentral.setRightComponent(panel_usuarios);
             
             
             setLayout(new BorderLayout());
             add(sldCentral, BorderLayout.CENTER);   
            
             
             txtfmensaje.requestFocus();//pedir el focus	
             
             cliente=new Cliente(this);
             cliente.conexion();     
             nicks=new Vector();
             ponerActivos(cliente.pedirUsuarios());
             
             privado=new Inbox(cliente);
             
             setTitle("chat-");
             setExtendedState(JFrame.MAXIMIZED_BOTH);
             setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
             setVisible(true);
     }
     
     public void setNombreUser(String user)
     {
        lblUsuarios.setText("Usuario " + user);
     }
     public void mostrarMsg(String msg)
     {
        this.historial.append(msg+"\n");
     }
     public void ponerActivos(Vector datos)
     {
        nicks=datos;
        ponerDatosList(this.lstActivos,nicks);
     }
     public void agregarUser(String user)
     {
        nicks.add(user);
        ponerDatosList(this.lstActivos,nicks);
     }
     public void retirraUser(String user)
     {        
        nicks.remove(user);
        ponerDatosList(this.lstActivos,nicks);
     }
    private void ponerDatosList(JList list,final Vector datos)
    {
        list.setModel(new AbstractListModel() {            
            @Override
            public int getSize() { return datos.size(); }
            @Override
            public Object getElementAt(int i) { return datos.get(i); }
        });
    }
    @Override
     public void actionPerformed(ActionEvent evt) {
         
       String comand=(String)evt.getActionCommand();
       
        if(evt.getSource()==this.btnenviar || evt.getSource()==this.txtfmensaje)
        {
           String mensaje = txtfmensaje.getText();        
           cliente.flujo(mensaje);
           txtfmensaje.setText("");
        }
        else if(evt.getSource()==this.btnInbox)
        {
           int pos=this.lstActivos.getSelectedIndex();
           if(pos>=0)              
           {
              privado.setcliente_inbox(nicks.get(pos));           
              privado.setVisible(true);
           }
        }
     }
     
     public void mensageAmigo(String amigo,String msg)
     {
        privado.setcliente_inbox(amigo);           
        privado.mostrarMsg(msg);        
        privado.setVisible(true);
     }

     public static void main(String args[]) throws IOException {
             
             Interface cliente = new Interface();
     }
}
