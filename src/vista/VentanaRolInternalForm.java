
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
import modelo.RolVentanas;
import modelo.Ventanas;


/**
 *
 * @author Ana
 */
public class VentanaRolInternalForm extends javax.swing.JInternalFrame {

    /**
     * Constructor que inicializa todos los componentes
     * 
     */
     
    public VentanaRolInternalForm() {
        initComponents();
        this.moveToFront();
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
                if(rolVentBD.existeRegistro(ventInt, rolInt) > 0){
                    showMessageDialog(this, "El rol "+ jComboRol.getSelectedItem()+ " ya tiene acceso a la ventana " +jComboVentana.getSelectedItem(), "Atención", JOptionPane.WARNING_MESSAGE);
                }else{
                    rolVentBD.insert(rolVent);
                    getRolVentana();
                    bGuardar.setSelected(false);
                }
         
               
            
        }
        
        private void borrar() throws Exception{
           if(TBrolVentanas.getSelectedRow() == -1){
                showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
           }
                int rolInt=rolBD.devuelveId(TBrolVentanas.getValueAt(TBrolVentanas.getSelectedRow(), 0).toString());
                rolVent.setRolId(rolInt);
                int ventInt=ventBD.devuelveId(TBrolVentanas.getValueAt(TBrolVentanas.getSelectedRow(), 1).toString());
                rolVent.setIdVentana(ventInt);
                System.out.println("ROL INT "+rolInt + "VENTA INT "+ ventInt);
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        bBorrar = new org.edisoncor.gui.button.ButtonTask();
        jPanel3 = new javax.swing.JPanel();
        txtIdUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboRol = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboVentana = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TBrolVentanas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Rol y ventana");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/permisos.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1258, 500));

        jPanel2.setBackground(new java.awt.Color(51, 94, 137));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(51, 94, 137));
        jLabel1.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ROLES Y VENTANAS");
        jPanel2.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 5));

        bGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        bGuardar.setText("Añadir");
        bGuardar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bGuardar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bGuardar.setDescription(" ");
        bGuardar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bGuardar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bGuardar.setIconTextGap(2);
        bGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(bGuardar);

        bBorrar.setForeground(new java.awt.Color(0, 51, 102));
        bBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        bBorrar.setText("Quitar");
        bBorrar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bBorrar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bBorrar.setDescription(" ");
        bBorrar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bBorrar.setIconTextGap(2);
        bBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBorrarActionPerformed(evt);
            }
        });
        jPanel1.add(bBorrar);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de roles y ventanas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtIdUsuario.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setFocusable(false);
        txtIdUsuario.setOpaque(false);
        txtIdUsuario.setRequestFocusEnabled(false);
        txtIdUsuario.setSelectionColor(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(txtIdUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 60, -1));

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel2.setText("Roles");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jComboRol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboRol.setSelectedIndex(-1);
        jComboRol.setToolTipText("");
        jPanel3.add(jComboRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 250, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel3.setText("Ventanas");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        jComboVentana.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboVentana.setSelectedIndex(-1);
        jPanel3.add(jComboVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 250, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setForeground(new java.awt.Color(153, 153, 153));

        jPanel5.setBackground(new java.awt.Color(87, 123, 181));

        jLabel8.setFont(new java.awt.Font("Aharoni", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("LISTA DE VENTANAS");

        TBrolVentanas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Roles", "Ventanas"
            }
        ));
        jScrollPane1.setViewportView(TBrolVentanas);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(182, 182, 182))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        try {
            borrar();
        } catch (Exception ex) {
            Logger.getLogger(VentanaRolInternalForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bBorrarActionPerformed

    
    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(VentanaRolInternalForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGuardarActionPerformed

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TBrolVentanas;
    private org.edisoncor.gui.button.ButtonTask bBorrar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private javax.swing.JComboBox jComboRol;
    private javax.swing.JComboBox jComboVentana;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtIdUsuario;
    // End of variables declaration//GEN-END:variables
}
