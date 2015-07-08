/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.VentanaControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Ventanas;


/**
 *
 * @author anex
 */
public class VentanasForm extends javax.swing.JDialog {

    /**
     * Creates new form VentanasForm
     */
    public VentanasForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
       establecerBotones("Nuevo"); 
        if(TbVentanas.getRowCount() == 0){
            txtCodigo.setValue(k);
        }else{
            txtCodigo.setValue(c);
        }
       
       txtCodigo.setEnabled(false);
    }
     public VentanasForm() {
        
        initComponents();
        establecerBotones("Vacio");  
    }
    Ventanas ventModel = new Ventanas();
    VentanaControlador ventanaBD = new VentanaControlador();
    DefaultTableModel modelo = new DefaultTableModel();
    int k= ventanaBD.nuevoCodigo();
    int c=k;

    
        private void nuevo() {
        limpiar();
        establecerBotones("Nuevo");
      
        txtCodigo.setValue(k);
        
       
        
        try {
            txtCodigo.requestFocusInWindow();     
        } catch (Exception ex) {
            showMessageDialog(null, ex.toString(), "Error", ERROR_MESSAGE);
        }
        }
        
        private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");    
        }
        private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                JBnuevo.setEnabled(false);
                JBcancelar.setEnabled(true);
                JBguardar.setEnabled(true);
                break;
            case "Edicion":
                JBnuevo.setEnabled(true);
                JBcancelar.setEnabled(false);
                JBguardar.setEnabled(true);
                break;
            case "Vacio":
                JBnuevo.setEnabled(true);
                JBcancelar.setEnabled(false);
                JBguardar.setEnabled(false);
                break;
            case "Buscar":
                JBnuevo.setEnabled(false);
                JBcancelar.setEnabled(true);
                JBguardar.setEnabled(false);
                break;
        }
    }
        private void guardar(){
       txtCodigo.setEnabled(false);
       if (showConfirmDialog(null, "Está seguro de guardar los datos?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
             
             
         
             ventModel.setNombre(txtNombre.getText());
             ventModel.setIdVentana(k);
         if (JBnuevo.isEnabled() == false) {//is Enable true - habilita boton 
                try {
                    k++;
                    ventanaBD.insert(ventModel);
                    nuevo();
                    getVentanas();
                 
                } catch (Exception ex) {
                    //showMessageDialog(null, "Debe ingresar la descripción.", "Atención", INFORMATION_MESSAGE); 
                    
                   showMessageDialog(null,ex.getMessage(), "Error", ERROR_MESSAGE);
                }
            } else {
               
                 try {
                    int i = (Integer)TbVentanas.getValueAt(TbVentanas.getSelectedRow(), 0);
                    ventanaBD.update(ventModel,i);               
                    showMessageDialog(null, "Actualizado correctamente");
                    limpiar(); 
                    getVentanas();
                   // txtCodigo.setValue(ventanaBD.devuelveId(ventModel.getNombre()));
                          
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
             
                
            }
         
         }
       }
            
      
     private void cancelar(){
      txtCodigo.setEditable(false);
      if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){
            getVentanas();
            limpiar();
            establecerBotones("Edicion");
        }
     }      
      private void getVentanas() {
          
        try {
             modelo.setColumnCount(0);
             modelo.setRowCount(0);
             TbVentanas.setModel(modelo);
           
            try (ResultSet rs = ventanaBD.datos()) {
           
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

        txtCodigo = new javax.swing.JFormattedTextField();
        labelCodigo = new javax.swing.JLabel();
        labelNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbVentanas = new javax.swing.JTable();
        JBnuevo = new javax.swing.JButton();
        JBguardar = new javax.swing.JButton();
        JBcancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de ventanas del Sistema");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        labelCodigo.setText("Codigo:");

        labelNombre.setText("Nombre:");

        TbVentanas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TbVentanas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TbVentanasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TbVentanas);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JBcancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBguardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBnuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCodigo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombre)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JBnuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBguardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBcancelar)
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void JBnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBnuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_JBnuevoActionPerformed

    private void JBguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBguardarActionPerformed
        guardar();
    }//GEN-LAST:event_JBguardarActionPerformed

    private void JBcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_JBcancelarActionPerformed

    private void TbVentanasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TbVentanasMouseClicked
        txtCodigo.setValue(TbVentanas.getValueAt(TbVentanas.getSelectedRow(), 0));
        txtNombre.setText(TbVentanas.getValueAt(TbVentanas.getSelectedRow(), 1).toString());
    }//GEN-LAST:event_TbVentanasMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        nuevo();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(VentanasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanasForm dialog = new VentanasForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton JBcancelar;
    private javax.swing.JButton JBguardar;
    private javax.swing.JButton JBnuevo;
    private javax.swing.JTable TbVentanas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JFormattedTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
