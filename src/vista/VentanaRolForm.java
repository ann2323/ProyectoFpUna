/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.RolControlador;
import controlador.RolVentanaControlador;
import controlador.VentanaControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Rol;
import modelo.Ventanas;
import modelo.RolVentanas;

/**
 *
 * @author anex
 */
public class VentanaRolForm extends javax.swing.JDialog {

    /**
     * Creates new form VentanaRolForm
     */
    public VentanaRolForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRolCombo();
        getVentanasCombo();
        getRolVentana();
        bBorrar.setSelected(false);
        bGuardar.setSelected(false);
    }
    
    public VentanaRolForm() {
        initComponents();
        getRolCombo();
        getVentanasCombo();
        getRolVentana();
        bBorrar.setSelected(false);
        bGuardar.setSelected(false);
    }
    
    RolVentanas rolVent = new RolVentanas();
    RolVentanaControlador rolVentBD = new RolVentanaControlador(); 
    Rol rol = new Rol();
    Ventanas vent = new Ventanas();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultComboBoxModel modelRol = new DefaultComboBoxModel();
    DefaultComboBoxModel modelVent = new DefaultComboBoxModel();
    RolControlador rolBD = new RolControlador();
    VentanaControlador ventBD = new VentanaControlador();

  
   
    
     private void getRolCombo() {
  
        try {
            try (ResultSet rs = rolBD.datosCombo()) {
                
                modelRol.removeAllElements();
                
                while (rs.next()) {
                    modelRol.addElement(rs.getObject("dato").toString());
                }
            jComboRol.setModel(modelRol);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
     private void getVentanasCombo() {
         try {
            try (ResultSet rs = ventBD.datosCombo()) {
                
                modelVent.removeAllElements();
                
                while (rs.next()) {
                    modelVent.addElement(rs.getObject("dato").toString());
                }
            jComboVentana.setModel(modelVent);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
        private void guardar() throws Exception{
        int rolInt=rolBD.devuelveId((String) jComboRol.getSelectedItem());
         rolVent.setRolId(rolInt);
         int ventInt=ventBD.devuelveId((String) jComboVentana.getSelectedItem());
         rolVent.setIdVentana(ventInt);
         rolVentBD.insert(rolVent);
         getRolVentana();
         bGuardar.setSelected(false);
        }
        private void borrar() throws Exception{
        int rolInt=rolBD.devuelveId(TBrolVentanas.getValueAt(TBrolVentanas.getSelectedRow(), 0).toString());
         rolVent.setRolId(rolInt);
         int ventInt=ventBD.devuelveId(TBrolVentanas.getValueAt(TBrolVentanas.getSelectedRow(), 1).toString());
         rolVent.setIdVentana(ventInt);
         rolVentBD.delete(rolInt, ventInt);
         getRolVentana();
         bBorrar.setSelected(false);
        }
        
        private void getRolVentana() {
          
        try {
             modelo.setColumnCount(0);
             modelo.setRowCount(0);
            TBrolVentanas.setModel(modelo);
           
            try (ResultSet rs = rolVentBD.datos()) {
           
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

        jComboRol = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboVentana = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        TBrolVentanas = new javax.swing.JTable();
        bGuardar = new javax.swing.JToggleButton();
        bBorrar = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboRol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboRol.setSelectedIndex(-1);
        jComboRol.setToolTipText("");
        jComboRol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboRolMouseClicked(evt);
            }
        });
        jComboRol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboRolItemStateChanged(evt);
            }
        });

        jLabel1.setText("Roles");

        jLabel2.setText("Ventanas");

        jComboVentana.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboVentana.setSelectedIndex(-1);
        jComboVentana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboVentanaMouseClicked(evt);
            }
        });
        jComboVentana.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboVentanaItemStateChanged(evt);
            }
        });

        TBrolVentanas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(TBrolVentanas);

        bGuardar.setText("Añadir");
        bGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarActionPerformed(evt);
            }
        });

        bBorrar.setText("Quitar");
        bBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBorrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboRol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboVentana, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                    .addComponent(bBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboVentana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(bGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bBorrar)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(VentanaRolForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGuardarActionPerformed

    private void jComboRolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboRolItemStateChanged
      
    }//GEN-LAST:event_jComboRolItemStateChanged

    private void jComboVentanaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboVentanaItemStateChanged
      
    }//GEN-LAST:event_jComboVentanaItemStateChanged

    private void jComboRolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboRolMouseClicked
       
    }//GEN-LAST:event_jComboRolMouseClicked

    private void jComboVentanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboVentanaMouseClicked

    }//GEN-LAST:event_jComboVentanaMouseClicked

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        try {
           borrar();
        } catch (Exception ex) {
            Logger.getLogger(VentanaRolForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bBorrarActionPerformed

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
            java.util.logging.Logger.getLogger(VentanaRolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaRolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaRolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaRolForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaRolForm dialog = new VentanaRolForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TBrolVentanas;
    private javax.swing.JToggleButton bBorrar;
    private javax.swing.JToggleButton bGuardar;
    private javax.swing.JComboBox jComboRol;
    private javax.swing.JComboBox jComboVentana;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
