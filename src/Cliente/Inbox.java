

package Cliente;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
public class Inbox extends JFrame implements ActionListener
{
   JTextArea mensaje_;
   JTextField txtMensaje;
   JButton btnEnviar;
   
   Cliente cliente;
   String cliente_inbox;
   
   public Inbox(Cliente cliente)
   {
      
      this.cliente=cliente;
      txtMensaje = new JTextField(30);
      btnEnviar = new JButton("Enviar");
      mensaje_ = new JTextArea(); 
      mensaje_.setEditable(false);
      txtMensaje.requestFocus();
      txtMensaje.addActionListener(this);
      btnEnviar.addActionListener(this);
      
      JPanel panel_mensaje = new JPanel();
             panel_mensaje.setLayout(new BorderLayout());
             
             panel_mensaje.add(txtMensaje, BorderLayout.CENTER);
             panel_mensaje.add(btnEnviar, BorderLayout.EAST);
      
      setLayout(new BorderLayout());
      add(new JScrollPane(mensaje_),BorderLayout.CENTER);
      add(panel_mensaje,BorderLayout.SOUTH);
       
      cliente_inbox="";
      
      this.addWindowListener(new WindowListener()
      {         
         public void windowClosing(WindowEvent e) {
            cerrarVentana();
         }
         public void windowClosed(WindowEvent e) {}         
         public void windowOpened(WindowEvent e) {}
         public void windowIconified(WindowEvent e) {}
         public void windowDeiconified(WindowEvent e) {}
         public void windowActivated(WindowEvent e) {}
         public void windowDeactivated(WindowEvent e) {}
        
      });
      
      setTitle("Inbox");
      setSize(1100,300);
      setLocation(570,90);
      
   }
   public void setcliente_inbox(String cliente_inbox)
   {      
      this.cliente_inbox=cliente_inbox;
      this.setTitle(cliente_inbox);      
   }
    private void cerrarVentana() 
    {       
      this.setVisible(false);      
    }
    public void mostrarMsg(String msg)
     {
        this.mensaje_.append(msg+"\n");
     }
    
   @Override
   public void actionPerformed(ActionEvent e) 
   {
      String mensaje = txtMensaje.getText();              
      mostrarMsg(cliente.getNombre()+">"+mensaje);
      cliente.flujo(cliente_inbox,mensaje);
      txtMensaje.setText("");
   }
}
