/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.ClienteControlador;
import controlador.FacturaCabeceraVentaControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pathy
 */
public class ConsultarNotaCreditoVentaForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form ConsultarFacturaVentaForm
     */
    public ConsultarNotaCreditoVentaForm() {
        initComponents();
        getCliente();
       
    }

    int codigoCliente = 0;
    DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
    ClienteControlador clienteControlador = new ClienteControlador();
    FacturaCabeceraVentaControlador ventaControlador = new FacturaCabeceraVentaControlador();
    DefaultTableModel modeloTablaFactura = new DefaultTableModel();
    
    
     private void getCliente() {
        try {
            try (ResultSet rs = clienteControlador.datosCombo()) {

                modelCombo.removeAllElements();

                while (rs.next()) {
                    modelCombo.addElement(rs.getObject("nombre").toString());
                }
                comboCliente.setModel(modelCombo);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
     private void getNotaCredito(int codigoCliente) {
        try {

            tbFact.setModel(modeloTablaFactura);

            modeloTablaFactura.setRowCount(0);
            modeloTablaFactura.setColumnCount(0);

            try (ResultSet rs = ventaControlador.busquedaNotaCredito(codigoCliente)) {
                ResultSetMetaData rsMd = rs.getMetaData();

                int cantidadColumnas = rsMd.getColumnCount();

                //System.out.println("Cantidad columnas "+cantidadColumnas);
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloTablaFactura.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    modeloTablaFactura.addRow(fila);
                }

                
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
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

        shapePainter1 = new org.jdesktop.swingx.painter.ShapePainter();
        windowsStatusBarUI1 = new org.jdesktop.swingx.plaf.windows.WindowsStatusBarUI();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboCliente = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbFact = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };

        setClosable(true);
        setIconifiable(true);
        setTitle("Consultar Nota Credito Venta");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscarNotaCreditoVenta.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(200, 33));
        setPreferredSize(new java.awt.Dimension(971, 486));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 94, 137));
        jPanel2.setPreferredSize(new java.awt.Dimension(801, 58));

        jLabel1.setBackground(new java.awt.Color(51, 94, 137));
        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CONSULTAR NOTA DE CRÉDITO DE VENTA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(155, 155, 155))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setText("Cliente");

        comboCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCliente.setSelectedIndex(-1);
        comboCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClienteActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBackground(new java.awt.Color(87, 123, 181));
        jPanel4.setPreferredSize(new java.awt.Dimension(457, 37));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Datos de la nota de crédito");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        tbFact.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tbFact.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nro Prefijo", "Nro. Nota de Crédito", "Factura Referenciada", "Fecha", "Total", "Estado", "Saldo"
            }
        ));
        tbFact.setEditingRow(0);
        jScrollPane1.setViewportView(tbFact);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 959, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(343, 343, 343)
                .addComponent(jLabel2)
                .addGap(42, 42, 42)
                .addComponent(comboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClienteActionPerformed
         try {
            codigoCliente = clienteControlador.getCodigo((String) comboCliente.getSelectedItem());
            getNotaCredito(codigoCliente);
            cargarTabla();
            //System.out.println(codigoCliente);
            
        } catch (Exception ex) {
            //Logger.getLogger(PruebaCombo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_comboClienteActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        comboCliente.setSelectedIndex(-1);
    }//GEN-LAST:event_formInternalFrameOpened

    private void cargarTabla() {
        
        DecimalFormat forma = new DecimalFormat("###,###.##");
        int i=0;
        
            tbFact.setValueAt(modeloTablaFactura.getValueAt(i, 0), i, 0);
            String nroNotaC = "";
            nroNotaC = forma.format(Integer.parseInt(modeloTablaFactura.getValueAt(i, 1).toString()));
            tbFact.setValueAt(nroNotaC, i, 1);
            String facturaRef = forma.format(Integer.parseInt(modeloTablaFactura.getValueAt(i, 2).toString()));
            tbFact.setValueAt(facturaRef, i, 2);
            tbFact.setValueAt(modeloTablaFactura.getValueAt(i, 3), i, 3);
            String total = "";
            total = forma.format(Integer.parseInt(modeloTablaFactura.getValueAt(i, 4).toString()));
            tbFact.setValueAt(total, i, 4);
            tbFact.setValueAt(modeloTablaFactura.getValueAt(i, 5), i, 5);
            String saldo = "";
            saldo = forma.format(Integer.parseInt(modeloTablaFactura.getValueAt(i, 6).toString()));
            tbFact.setValueAt(saldo, i, 6);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.painter.ShapePainter shapePainter1;
    private javax.swing.JTable tbFact;
    private org.jdesktop.swingx.plaf.windows.WindowsStatusBarUI windowsStatusBarUI1;
    // End of variables declaration//GEN-END:variables
}
