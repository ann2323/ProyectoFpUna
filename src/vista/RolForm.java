/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates * and open the template in the editor.
 */

package vista;
import controlador.RolControlador;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Rol;

/**
 *
 * @author anex
 */
public class RolForm extends javax.swing.JDialog {

    /**
     * Creates new form RolForm
     */
    public RolForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        establecerBotones("Vacio");
       
    }
    public RolForm() {
        
        initComponents();
        establecerBotones("Vacio");  
     
      
        
    }
    Rol rolModel = new Rol();
    RolControlador rolBD = new RolControlador();
    DefaultTableModel modelo = new DefaultTableModel();
    int k;
    private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                JBnuevo.setEnabled(false);
                JBborrar.setEnabled(false);
                JBcancelar.setEnabled(true);
                JBguardar.setEnabled(true);
                JBbuscar.setEnabled(false);
                break;
            case "Edicion":
                JBnuevo.setEnabled(true);
                JBborrar.setEnabled(true);
                JBcancelar.setEnabled(false);
                JBguardar.setEnabled(true);
                JBbuscar.setEnabled(true);
                break;
            case "Vacio":
                JBnuevo.setEnabled(true);
                JBborrar.setEnabled(false);
                JBcancelar.setEnabled(false);
                JBguardar.setEnabled(false);
                JBbuscar.setEnabled(false);
                break;
            case "Buscar":
                JBnuevo.setEnabled(false);
                JBborrar.setEnabled(false);
                JBcancelar.setEnabled(true);
                JBguardar.setEnabled(false);
                JBbuscar.setEnabled(true);
                break;
        }
    }
        private void nuevo() {
   
        limpiar();
        establecerBotones("Nuevo");
        
        try {
            txtnombreRol.requestFocusInWindow();     
        } catch (Exception ex) {
            showMessageDialog(null, ex.toString(), "Error", ERROR_MESSAGE);
        }
       
        }
        private void limpiar() {
        txtnombreRol.setText("");
        txtDescripcionRol.setText("");    
        }
        private void guardar(){
       
       if (showConfirmDialog(null, "Está seguro de guardar los datos?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
                 
             if (txtnombreRol.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo rol vacío, por favor ingrese el nombre del rol", "", JOptionPane.WARNING_MESSAGE);
                return;
             }
            
        rolModel.setNombre(txtnombreRol.getText());
        rolModel.setDescripcion(txtDescripcionRol.getText());  
      
          
           
         if (JBnuevo.isEnabled() == false) {//is Enable true - habilita boton 
                try {
                    
                    rolBD.insert(rolModel);
                    nuevo();
                    getRoles();
                 
                } catch (Exception ex) {
                    //showMessageDialog(null, "Debe ingresar la descripción.", "Atención", INFORMATION_MESSAGE); 
                    
                   showMessageDialog(null,ex.getMessage(), "Error", ERROR_MESSAGE);
                }
            } else {
                 try {                 
                    int idRol = rolBD.devuelveId(rolModel.getNombre());
                    rolBD.update(rolModel, idRol);               
                    showMessageDialog(null, "Actualizado correctamente");
                    limpiar(); 
                    getRoles();
                          
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
             
                
            }
         
         }
       }
        
   
        
       private void cancelar(){
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){
            getRoles();
            limpiar();

            establecerBotones("Edicion");
           if (modelo.getRowCount() == 0) {
                limpiar(); 
                establecerBotones("Vacio"); 
                modoBusqueda(false); 
                return;
            }
            if (k >= 0){
                    limpiar(); 
                    datosActuales(); 
                    establecerBotones("Edicion");
                    modoBusqueda(false);
                    return;
            }
         
            
        }
       }
       
       private void borrar(){
          
        if(TbRol.getSelectedRow() == -1){
            showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{    try {
                
             
               int idRol = rolBD.devuelveId(TbRol.getValueAt(TbRol.getSelectedRow(), 0).toString());
               rolBD.delete(idRol);
               showMessageDialog(null, "Operación exitosa"); 
               limpiar();
               getRoles();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }
      
      private void buscar(){
        limpiar();
        establecerBotones("Buscar");
        modoBusqueda(true);
      }
      private void modoBusqueda(boolean v){
        if (v == true) {
        txtnombreRol.setEditable(true);
        txtnombreRol.requestFocusInWindow();
        txtnombreRol.setBackground(Color.yellow);
        txtDescripcionRol.setEnabled(false);
        } else {
        txtnombreRol.setEditable(true);
        txtnombreRol.setBackground(Color.white);
        txtnombreRol.setEnabled(true);
        txtDescripcionRol.setEnabled(true);
        
        }
      }
      private void datosActuales(){
            txtnombreRol.setText(modelo.getValueAt(k, 0).toString());
            txtDescripcionRol.setText(modelo.getValueAt(k, 1).toString());
            
      }
      private void getRoles() {
          
        try {
             modelo.setColumnCount(0);
             modelo.setRowCount(0);
            TbRol.setModel(modelo);
           
            try (ResultSet rs = rolBD.datos()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modelo.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modelo.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
         
      }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtnombreRol = new javax.swing.JTextField();
        labelRol = new javax.swing.JLabel();
        labelDescripcion = new javax.swing.JLabel();
        txtDescripcionRol = new javax.swing.JTextField();
        JBnuevo = new javax.swing.JButton();
        JBguardar = new javax.swing.JButton();
        JBcancelar = new javax.swing.JButton();
        JBborrar = new javax.swing.JButton();
        JBbuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbRol = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administrar Roles");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtnombreRol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreRolKeyPressed(evt);
            }
        });

        labelRol.setText("Rol:");

        labelDescripcion.setText("Descripcion:");

        JBnuevo.setText("Nuevo");
        JBnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBnuevoActionPerformed(evt);
            }
        });

        JBguardar.setText("Guardar");
        JBguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBguardarActionPerformed(evt);
            }
        });

        JBcancelar.setText("Cancelar");
        JBcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBcancelarActionPerformed(evt);
            }
        });

        JBborrar.setText("Borrar");
        JBborrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBborrarActionPerformed(evt);
            }
        });

        JBbuscar.setText("Buscar");
        JBbuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBbuscarMouseClicked(evt);
            }
        });

        TbRol.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TbRol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TbRolMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TbRol);
        TbRol.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelDescripcion)
                            .addComponent(labelRol))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcionRol, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnombreRol, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JBcancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBborrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBguardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBnuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JBnuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBguardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBcancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBborrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBbuscar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnombreRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelRol))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcionRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDescripcion))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBnuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_JBnuevoActionPerformed

    private void JBguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBguardarActionPerformed
         guardar();
    }//GEN-LAST:event_JBguardarActionPerformed

    private void JBcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_JBcancelarActionPerformed

    private void JBborrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBborrarActionPerformed
        borrar();
    }//GEN-LAST:event_JBborrarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        nuevo();
    
    }//GEN-LAST:event_formWindowOpened

    private void JBbuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBbuscarMouseClicked
        if (evt.isMetaDown()){
            return;
        } else {
           buscar();
        }
    }//GEN-LAST:event_JBbuscarMouseClicked

    private void txtnombreRolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreRolKeyPressed
        if (txtDescripcionRol.isEnabled() == true){
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtnombreRol.getText())) {
                
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "nombre, descripcion";
                bf.tabla = "rol";
                bf.order = "nombre";
                bf.filtroBusqueda = "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modelo.getRowCount(); c ++){
                    if (modelo.getValueAt(c, 0).toString().equals(bf.retorno)){
                        modoBusqueda(false);
                        establecerBotones("Edicion");
                        k = c;
                        datosActuales();
                    return;
                    }
                }
                
            }
            
            for(int c=0; c<modelo.getRowCount(); c ++){
                if (modelo.getValueAt(c, 0).toString().equals(txtnombreRol.getText())){
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    k = c;
                    datosActuales();
                    return;
                }
            }
        }
    }//GEN-LAST:event_txtnombreRolKeyPressed

    private void TbRolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TbRolMouseClicked
        txtnombreRol.setText(TbRol.getValueAt(TbRol.getSelectedRow(), 0).toString());
        txtDescripcionRol.setText(TbRol.getValueAt(TbRol.getSelectedRow(), 1).toString());
    }//GEN-LAST:event_TbRolMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RolForm dialog = new RolForm(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBborrar;
    private javax.swing.JButton JBbuscar;
    private javax.swing.JButton JBcancelar;
    private javax.swing.JButton JBguardar;
    private javax.swing.JButton JBnuevo;
    private javax.swing.JTable TbRol;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelRol;
    private javax.swing.JTextField txtDescripcionRol;
    private javax.swing.JTextField txtnombreRol;
    // End of variables declaration//GEN-END:variables
}
