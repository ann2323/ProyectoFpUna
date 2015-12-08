/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.DetalleCuentaControlador;
import controlador.DetalleFacturaCompra;
import controlador.FacturaCabeceraCompraControlador;
import controlador.StockControlador;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pathy
 */
public class AnularNotaCreditoCompra extends javax.swing.JInternalFrame {

    /**
     * Creates new form AnularInternalFrame
     */
    public AnularNotaCreditoCompra() {
        initComponents();
        
    }
    
    //Date ahora = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    

    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    
    FacturaCabeceraCompraControlador compraControlador = new FacturaCabeceraCompraControlador();
    DetalleCuentaControlador detalleCuentaControlador = new DetalleCuentaControlador();
    DetalleFacturaCompra facturaDetalleCont = new DetalleFacturaCompra();
    StockControlador stockCont = new StockControlador();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    Date dato = null;
    int k;
    
   
     private void getCompra() {
        try {
            modeloBusqueda.setColumnCount(0);
            modeloBusqueda.setRowCount(0);
           
            try (ResultSet rs = compraControlador.datosBusquedaNotaCreditoCompra()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloBusqueda.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloBusqueda.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
     
          private void cargarDetalleFactura(int idCompra) {
        try {
            
            try (ResultSet rs = facturaDetalleCont.getDetalle(idCompra)) {
                 modeloDetalleBusqueda.setColumnCount(0);
                 modeloDetalleBusqueda.setRowCount(0);
                 ResultSetMetaData rsMd = rs.getMetaData();
                
               //int cantidadColumnas = rsMd.getColumnCount();
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                   modeloDetalleBusqueda.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloDetalleBusqueda.addRow(fila);
                }
                //Factura en suspension. una vez que devuelve las filas de la factura se agregan hasta completar las 12
                for (int i = 0; i < 11; i++) {
                        modeloDetalleBusqueda.addRow(new Object[]{"","","","","",""});
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

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtFechaCompra = new javax.swing.JTextField();
        txtPrefijo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lbEstado = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNroNotaCredito = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFacturaReferenciada = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        bAnular = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Anular Nota de Crédito de Compra");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anularNotaCreditoCompra.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(604, 402));
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

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel4.setText("Fecha de compra");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, 20));
        jPanel1.add(txtFechaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 160, -1));

        txtPrefijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrefijoKeyPressed(evt);
            }
        });
        jPanel1.add(txtPrefijo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 76, -1));

        jLabel8.setText("-");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 10, -1));

        lbEstado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbEstado.setText("Estado de la nota de crédito");
        jPanel1.add(lbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, 20));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 160, -1));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel3.setText("Factura Referenciada");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, 20));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel1.setText("Número de nota de crédito a anular");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 210, 20));
        jPanel1.add(txtNroNotaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 70, -1));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel6.setText("Monto total ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, 20));
        jPanel1.add(txtFacturaReferenciada, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 160, -1));
        jPanel1.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 160, -1));

        bAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anular.png"))); // NOI18N
        bAnular.setText(" ");
        bAnular.setToolTipText("Anular");
        bAnular.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bAnular.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bAnular.setDescription(" ");
        bAnular.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bAnular.setIconTextGap(2);
        bAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAnularActionPerformed(evt);
            }
        });
        jPanel1.add(bAnular, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 50, 50));

        bCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        bCancelar.setText(" ");
        bCancelar.setToolTipText("Cancelar");
        bCancelar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bCancelar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bCancelar.setDescription(" ");
        bCancelar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bCancelar.setIconTextGap(2);
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(bCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, 50, 50));

        jLabel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle Nota de Crédito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10), new java.awt.Color(0, 0, 0))); // NOI18N
        jLabel2.setEnabled(false);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 490, 240));

        jPanel2.setBackground(new java.awt.Color(51, 94, 137));

        jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("ANULAR NOTA DE CREDITO DE COMPRA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(76, 76, 76))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPrefijoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrefijoKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtPrefijo.getText())) {
                
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "nro_prefijo as \"Nro Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as \"Fecha\", trim(to_char(cast(precio_total as integer),'9G999G999')) as \"Total\", estado as \"Estado\", trim(to_char(cast(fact_referenciada as integer),'9G999G999')) as \"Factura Referenciada\"";
                bf.tabla = "Compra";
                bf.order = "compra_id";
                bf.filtroBusqueda = "estado != 'ANULADO' and es_factura = 'N'";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modeloBusqueda.getRowCount(); c++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        k = c;
                        txtPrefijo.setText(modeloBusqueda.getValueAt(k, 0).toString());
                        txtNroNotaCredito.setText(modeloBusqueda.getValueAt(k, 1).toString());
                        txtFechaCompra.setText(modeloBusqueda.getValueAt(k, 2).toString());
                        txtTotal.setText(modeloBusqueda.getValueAt(k, 3).toString());
                        txtEstado.setText(modeloBusqueda.getValueAt(k, 4).toString());
                        txtFacturaReferenciada.setText(modeloBusqueda.getValueAt(k, 5).toString());
                        txtPrefijo.setBackground(Color.white);
                    return;
                    }
                }  
            }
        }
    }//GEN-LAST:event_txtPrefijoKeyPressed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
      getCompra();
      txtFechaCompra.setEditable(false);
      txtNroNotaCredito.setEditable(false);
      txtTotal.setEditable(false);
      txtFacturaReferenciada.setEditable(false);
      txtPrefijo.requestFocusInWindow();
      txtEstado.setEditable(false);
      txtPrefijo.setBackground(Color.yellow);
     
    }//GEN-LAST:event_formInternalFrameOpened

    private void bAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAnularActionPerformed
        Integer IdCompra=0;
        try {
            IdCompra = compraControlador.devuelveId(Integer.parseInt(txtNroNotaCredito.getText().replace(".", "").trim()));
            cargarDetalleFactura(IdCompra);
        } catch (Exception ex) {
            Logger.getLogger(AnularFacturaCompraForm.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        anular();
    }//GEN-LAST:event_bAnularActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            limpiar();
            txtPrefijo.setBackground(Color.yellow);
        }
    }//GEN-LAST:event_bCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bAnular;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbEstado;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFacturaReferenciada;
    private javax.swing.JTextField txtFechaCompra;
    private javax.swing.JTextField txtNroNotaCredito;
    private javax.swing.JTextField txtPrefijo;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    private void anular() {
        
         if (txtNroNotaCredito.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo número de nota de crédito vacío, por favor ingrese el número de nota de crédito", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
         }
        if(showConfirmDialog (null, "Está seguro de anular la nota de crédito?", "Confirmar", YES_NO_OPTION) == YES_OPTION){ 
            int f=0; String dep="";
            try {                      
                 dep = compraControlador.getDeposito(Integer.parseInt(txtNroNotaCredito.getText().replace(".", "").trim()),(txtPrefijo.getText().replace(".", "").trim()));
             } catch (Exception ex) {
                 Logger.getLogger(AnularFacturaCompraForm.class.getName()).log(Level.SEVERE, null, ex);
             }
             while (!"".equals(modeloDetalleBusqueda.getValueAt(f, 0).toString())){     
                  try {
                      stockCont.update(modeloDetalleBusqueda.getValueAt(f, 0).toString(), dep, Integer.parseInt(modeloDetalleBusqueda.getValueAt(f, 3).toString()));
                  } catch (Exception ex) {
                      Logger.getLogger(AnularFacturaCompraForm.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  f++;
                  
              }
            try {
               compraControlador.updateEstadoAnuladoNotaCreditoC(Integer.parseInt(txtNroNotaCredito.getText().replace(".", "").trim()));
               
                
                showMessageDialog(null, "Nota de crédito de compra anulada correctamente");
            } catch (Exception ex) {
                Logger.getLogger(AnularNotaCreditoCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpiar();
            txtPrefijo.setBackground(Color.yellow);
        }   
    }

    private void limpiar() {
        txtPrefijo.setText("");
        txtFechaCompra.setText("");
        txtNroNotaCredito.setText("");
        txtTotal.setText("");
        txtEstado.setText("");
        txtFacturaReferenciada.setText("");
    }
}
