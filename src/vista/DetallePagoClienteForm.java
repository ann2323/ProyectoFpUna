/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;
import controlador.DetallePagoControlador;
import controlador.FacturaCabeceraVentaControlador;
import controlador.ClienteControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Venta;


/**
 *26/08/2015
 * @author anex
 */
public class DetallePagoClienteForm extends javax.swing.JInternalFrame {
  
    DefaultTableModel modeloPago = new DefaultTableModel();
    FacturaCabeceraVentaControlador ventaControlador = new FacturaCabeceraVentaControlador();
    DetallePagoControlador detalleControl = new DetallePagoControlador();
    ClienteControlador cliC = new ClienteControlador();
    Venta facturaVenta = new Venta();
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    int total=0, i=0, pendiente=0, cambio=0,pendiente2=0, pendienteAplicar=0;
   
    
 public DetallePagoClienteForm() throws Exception {
        initComponents();
        getFacturaVenta();
        
        txtPagado.setEnabled(false);
        txtPendiente.setEnabled(false);
        txtCambio.setEnabled(false);
        jPanelCheque.setVisible(false);
        jPanelNotaCredito.setVisible(false);
        jPanelTarjeta.setVisible(false);
        txtValorDelCredito.setEditable(false);
        txtPendienteAplicar.setEditable(false);
        
        modeloPago.addColumn("Forma de Pago");
        modeloPago.addColumn("Tarjeta/Cheque nro.");
        modeloPago.addColumn("Monto Abonado");
        modeloPago.addColumn("Monto Credito");
        modeloPago.addColumn("Pendiente a aplicar");
        modeloPago.addColumn("Nro. Nota Credito");
        modeloPago.isCellEditable(0,0);
        tbDetallePago.setModel(modeloPago);
        
        int k=0;
        while (k<ReciboClienteForm.tbVistaFacturasPendientes.getRowCount()){
                    if(ReciboClienteForm.tbVistaFacturasPendientes.isRowSelected(k)) {
                        pendiente2 = pendiente2 + Integer.parseInt(ReciboClienteForm.tbVistaFacturasPendientes.getValueAt(i, 5).toString().replace(".","").trim());
                    }
               k++;     
        }
        
        
        formateador = new DecimalFormat("###,###.##");
        txtPendiente.setText(formateador.format(pendiente2));
        
        txtPagado.setText("0");
        txtCambio.setText("0");
        
    }
 
  private void getFacturaVenta() throws Exception {
        JCfactura.removeAll();
        Vector<Venta> comVec = new Vector<Venta>();
        String cliCi = ReciboClienteForm.txtCliente.getText().replace(".", "").trim();
        Integer clienteId = cliC.devuelveId(cliCi);
        try {
          
            try (ResultSet rs = ventaControlador.datosComboSaldo(clienteId)) {
                while(rs.next()){
                    facturaVenta=new Venta();
                    facturaVenta.setNroPrefijo(rs.getString(1));
                    facturaVenta.setNroFactura(Integer.parseInt(rs.getString(2)));
                    comVec.add(facturaVenta);
                }
                rs.close();
                                    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        DefaultComboBoxModel md1 = new DefaultComboBoxModel(comVec); 
        JCfactura.setModel(md1);
    }
  
    private void limpiar() {
        //txtPrefijoVenta.setText("");
        modeloPago= new DefaultTableModel();
        tbDetallePago.removeAll();
        txtPagado.setText("");
        txtPendiente.setText("");
        txtPendienteAplicar.setText("");
        txtValor.setText(""); 
        txtValorDelCredito.setText(""); 
        txtCambio.setText(""); 
       
    }
   
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelVenta = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        jBeliminar = new org.edisoncor.gui.button.ButtonTask();
        jBGuardar1 = new org.edisoncor.gui.button.ButtonTask();
        JCtipoPago = new javax.swing.JComboBox();
        labelMoneda = new javax.swing.JLabel();
        labelFechaVenta = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetallePago = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        }
        ;
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        labelCantidadTotal2 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jPanelTarjeta = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNroTarjeta = new javax.swing.JTextField();
        jPanelCheque = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtNroCheque = new javax.swing.JTextField();
        jPanelNotaCredito = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtPendienteAplicar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtValorDelCredito = new javax.swing.JTextField();
        JCfactura = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        txtPagado = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtPendiente = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Detalle de Pago");
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

        jPanelVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("DETALLE DE PAGO");
        jLabel7.setOpaque(true);
        jPanelVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-9, 0, 930, 56));

        jPanel1.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 5));

        bNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/añadirPago.png"))); // NOI18N
        bNuevo.setText("Añadir");
        bNuevo.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bNuevo.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bNuevo.setDescription(" ");
        bNuevo.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bNuevo.setIconTextGap(2);
        bNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNuevoActionPerformed(evt);
            }
        });
        jPanel1.add(bNuevo);
        bNuevo.getAccessibleContext().setAccessibleName("Añadir ");

        jBeliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        jBeliminar.setText("Eliminar Registro");
        jBeliminar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jBeliminar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        jBeliminar.setDescription(" ");
        jBeliminar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        jBeliminar.setIconTextGap(2);
        jBeliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBeliminarActionPerformed(evt);
            }
        });
        jPanel1.add(jBeliminar);
        jBeliminar.getAccessibleContext().setAccessibleDescription("");

        jBGuardar1.setForeground(new java.awt.Color(0, 51, 102));
        jBGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        jBGuardar1.setText("Guardar");
        jBGuardar1.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jBGuardar1.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        jBGuardar1.setDescription(" ");
        jBGuardar1.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        jBGuardar1.setIconTextGap(2);
        jBGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGuardar1ActionPerformed(evt);
            }
        });
        jPanel1.add(jBGuardar1);

        jPanelVenta.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 900, 55));

        JCtipoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Guaranies", "Tarjeta Debito", "Tarjeta Credito", "Cheque", "Nota de Credito" }));
        JCtipoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCtipoPagoActionPerformed(evt);
            }
        });
        jPanelVenta.add(JCtipoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 140, 20));

        labelMoneda.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelMoneda.setText("Tipo de Pago");
        jPanelVenta.add(labelMoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 80, -1));

        labelFechaVenta.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFechaVenta.setText("Monto:");
        labelFechaVenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanelVenta.add(labelFechaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));
        jPanelVenta.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, -1, -1));

        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10))); // NOI18N
        jPanelVenta.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 890, 120));

        tbDetallePago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Forma Pago", "Tarjeta Nro. / Cheque Nro.", "Monto Abonado", "MontoCredito", "Pendiente a Aplicar", "Nro Nota Credito"
            }
        ));
        tbDetallePago.setEditingColumn(0);
        tbDetallePago.setFillsViewportHeight(true);
        tbDetallePago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tbDetallePagoFocusLost(evt);
            }
        });
        tbDetallePago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDetallePagoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbDetallePago);

        jPanelVenta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 890, 230));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Detalle de Pago");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(400, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(371, 371, 371))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanelVenta.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 890, 30));
        jPanelVenta.add(labelCantidadTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 650, 200, -1));
        jPanelVenta.add(txtValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 93, -1));

        jPanelTarjeta.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N
        jPanelTarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel2.setText("Tarjeta:");
        jPanelTarjeta.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 19, -1, -1));

        txtNroTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroTarjetaActionPerformed(evt);
            }
        });
        jPanelTarjeta.add(txtNroTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 16, 140, -1));

        jPanelVenta.add(jPanelTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 138, 410, 100));

        jPanelCheque.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel4.setText("Cheque:");

        javax.swing.GroupLayout jPanelChequeLayout = new javax.swing.GroupLayout(jPanelCheque);
        jPanelCheque.setLayout(jPanelChequeLayout);
        jPanelChequeLayout.setHorizontalGroup(
            jPanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChequeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNroCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(210, Short.MAX_VALUE))
        );
        jPanelChequeLayout.setVerticalGroup(
            jPanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChequeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNroCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanelVenta.add(jPanelCheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 410, 100));

        jPanelNotaCredito.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel10.setText("Monto del Credito:");

        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel11.setText("Pendiente a aplicar:");

        JCfactura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        JCfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCfacturaActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel12.setText("Nro. Nota Credito");

        javax.swing.GroupLayout jPanelNotaCreditoLayout = new javax.swing.GroupLayout(jPanelNotaCredito);
        jPanelNotaCredito.setLayout(jPanelNotaCreditoLayout);
        jPanelNotaCreditoLayout.setHorizontalGroup(
            jPanelNotaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNotaCreditoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNotaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNotaCreditoLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPendienteAplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelNotaCreditoLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtValorDelCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JCfactura, 0, 170, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelNotaCreditoLayout.setVerticalGroup(
            jPanelNotaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNotaCreditoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNotaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtValorDelCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCfactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelNotaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtPendienteAplicar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelVenta.add(jPanelNotaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 560, -1));

        jLabel13.setText("Cambio");
        jPanelVenta.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 530, 60, -1));

        jLabel14.setText("Pagado");
        jPanelVenta.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 530, -1, -1));
        jPanelVenta.add(txtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 560, 90, -1));
        jPanelVenta.add(txtPagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 560, 90, -1));

        jLabel15.setText("Pendiente");
        jPanelVenta.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 530, -1, -1));
        jPanelVenta.add(txtPendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 560, 90, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDetallePagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetallePagoKeyPressed

     
    }//GEN-LAST:event_tbDetallePagoKeyPressed

    private void jBGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardar1ActionPerformed
       if (Integer.parseInt(txtPendiente.getText().replace(".", "").trim())!=0){
          showMessageDialog(this, "Debe ingresar la totalidad del pago pendiente", "Atención", JOptionPane.WARNING_MESSAGE);
       }else{ if(showConfirmDialog (null, "Está seguro de confirmar los cambios?", "Confirmar", YES_NO_OPTION) == YES_OPTION){
        ReciboClienteForm.tbDetallePago.setModel(modeloPago);
        ReciboClienteForm.txtpagado.setText(txtPagado.getText());
        ReciboClienteForm.txtpendiente.setText(txtPendiente.getText());
        ReciboClienteForm.txtcambio.setText(txtCambio.getText());
        limpiar();
        this.dispose();
        }
       }
    }//GEN-LAST:event_jBGuardar1ActionPerformed

    private void jBeliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBeliminarActionPerformed

       if(tbDetallePago.getSelectedRow() == -1){
            showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{
            DecimalFormat formateadorDelet = new DecimalFormat("###,###.##");
            total=total-Integer.parseInt(tbDetallePago.getValueAt(tbDetallePago.getSelectedRow(), 2).toString().trim().replace(".", ""));
            String TotalFormat=formateadorDelet.format(total);
            txtPagado.setText(TotalFormat);
             
            if (Integer.parseInt(txtPagado.getText().trim().replace(".", ""))<pendiente2 && pendiente!=0){
            pendiente=pendiente+Integer.parseInt(tbDetallePago.getValueAt(tbDetallePago.getSelectedRow(), 2).toString().trim().replace(".", ""));
            String PendienteFormat=formateadorDelet.format(pendiente);
            txtPendiente.setText(PendienteFormat);
            }else if (Integer.parseInt(txtPagado.getText().trim().replace(".", ""))==pendiente2){
            pendiente=0;
            String PendienteFormat=formateadorDelet.format(pendiente);
            txtPendiente.setText(PendienteFormat);
            }else if (Integer.parseInt(txtPagado.getText().trim().replace(".", ""))<pendiente2 && pendiente==0){
            pendiente=pendiente2-Integer.parseInt(txtPagado.getText().trim().replace(".", ""));
            String PendienteFormat=formateadorDelet.format(pendiente);
            txtPendiente.setText(PendienteFormat);
            }
           
           
            
            if (pendiente2<=Integer.parseInt(txtPagado.getText().replace(".", "").trim())){
            cambio=Integer.parseInt(txtPagado.getText().replace(".", "").trim())-pendiente2;
            String CambioFormat=formateadorDelet.format(cambio);
            txtCambio.setText(CambioFormat);
            }else{
            cambio=0; pendiente=0;
            String CambioFormat=formateadorDelet.format(cambio);
            txtCambio.setText(CambioFormat);
            }
            
            if (Integer.parseInt(txtPagado.getText().replace(".", "").trim())== 0 && Integer.parseInt(txtCambio.getText().replace(".", "").trim())==0){
            total=0; cambio=0;
            pendiente=pendiente2;
            String PendienteFormat=formateadorDelet.format(pendiente);
            txtPendiente.setText(PendienteFormat);
            }
            

            i--;
            modeloPago.removeRow(tbDetallePago.getSelectedRow());                    
            tbDetallePago.setModel(modeloPago);
       }
           
    }//GEN-LAST:event_jBeliminarActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
       if (!"".equals(txtValor.getText())){
        if (JCtipoPago.getSelectedItem().toString().equals("Tarjeta Debito")  || JCtipoPago.getSelectedItem().toString().equals("Tarjeta Credito")){
            formateador = new DecimalFormat("###,###.##");
            
            String datos [] = new String [6];
            datos[0]=JCtipoPago.getSelectedItem().toString();
            datos[1]=txtNroTarjeta.getText();
            datos[2]=formateador.format(Integer.parseInt(txtValor.getText()));
            datos[3]="";
            datos[4]="";
            datos[5]="";
            modeloPago.addRow(datos);  
        }
        if (JCtipoPago.getSelectedItem().toString().equals("Guaranies")){
            String datos [] = new String [6];
            datos[0]=JCtipoPago.getSelectedItem().toString();
            datos[1]="";
            datos[2]=formateador.format(Integer.parseInt(txtValor.getText()));
            datos[3]="";
            datos[4]="";
            datos[5]="";
            modeloPago.addRow(datos);   
        }
        if (JCtipoPago.getSelectedItem().toString().equals("Cheque")){
            String datos [] = new String [6];
            datos[0]=JCtipoPago.getSelectedItem().toString();
            datos[1]=txtNroCheque.getText();;
            datos[2]=formateador.format(Integer.parseInt(txtValor.getText()));
            datos[3]="";
            datos[4]="";
            datos[5]="";
            modeloPago.addRow(datos);   
        }
        if (JCtipoPago.getSelectedItem().toString().equals("Nota de Credito")){
            if (0==Integer.parseInt(txtPendienteAplicar.getText().replace(".", "").trim())){
                showMessageDialog(this, "El valor del credito ya se ha utilizado en su totalidad", "Atención", JOptionPane.WARNING_MESSAGE);
            }else{
            String datos [] = new String [6];
            datos[0]=JCtipoPago.getSelectedItem().toString();
            datos[1]="";
            datos[2]=formateador.format(Integer.parseInt(txtValor.getText()));
            datos[3]=txtValorDelCredito.getText();
            pendienteAplicar = Integer.parseInt(txtPendienteAplicar.getText().replace(".", "").trim()) - Integer.parseInt(txtValor.getText());
            formateador = new DecimalFormat("###,###.##");
            txtPendienteAplicar.setText(formateador.format(pendienteAplicar));
            datos[4]=txtPendienteAplicar.getText();
            String nroPrefijoNotaCredito = JCfactura.getSelectedItem().toString();
            String notaCredito = nroPrefijoNotaCredito.substring(4);
            datos[5]=notaCredito;
            modeloPago.addRow(datos);  
            }
        }
        
     
        tbDetallePago.setModel(modeloPago);
        txtValor.setText("");
        txtNroTarjeta.setText("");
        
        
        total=total+Integer.parseInt(tbDetallePago.getValueAt(i, 2).toString().replace(".", "").trim());
        i++;
        String TotalFormat=formateador.format(total);
        txtPagado.setText(TotalFormat);
        
        pendiente=Integer.parseInt(txtPendiente.getText().replace(".", "").trim());
        
        if (Integer.parseInt(txtPagado.getText().replace(".", "").trim())==0){
        pendiente=pendiente2;
        }
        if (total< pendiente2){
        pendiente=pendiente2- total;
        String PendienteFormat=formateador.format(pendiente);
        txtPendiente.setText(PendienteFormat);
        }else{
        cambio=total-pendiente2;
        String cambioFormat=formateador.format(cambio);
        txtCambio.setText(cambioFormat);
        
        pendiente=0;
        String PendienteFormat=formateador.format(pendiente);
        txtPendiente.setText(PendienteFormat);

        }
                    
       }else{
        showMessageDialog(this, "Debe ingresar un valor para añadir el pago", "Atención", JOptionPane.WARNING_MESSAGE);
       }
        
    }//GEN-LAST:event_bNuevoActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void tbDetallePagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbDetallePagoFocusLost
 
    }//GEN-LAST:event_tbDetallePagoFocusLost

    private void JCtipoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCtipoPagoActionPerformed

        if (JCtipoPago.getSelectedItem().toString().equals("Tarjeta Debito") || JCtipoPago.getSelectedItem().toString().equals("Tarjeta Credito")){
             jPanelCheque.setVisible(false);
             jPanelNotaCredito.setVisible(false);
             jPanelTarjeta.setVisible(true);
        }else if (JCtipoPago.getSelectedItem().toString().equals("Cheque")){ 
            jPanelTarjeta.setVisible(false);
            jPanelNotaCredito.setVisible(false);
            jPanelCheque.setVisible(true);
        }else if (JCtipoPago.getSelectedItem().toString().equals("Nota de Credito")){ 
            jPanelTarjeta.setVisible(false);
            jPanelNotaCredito.setVisible(true);
            jPanelCheque.setVisible(false);
        }else if (JCtipoPago.getSelectedItem().toString().equals("Guaranies")){ 
            jPanelTarjeta.setVisible(false);
            jPanelNotaCredito.setVisible(false);
            jPanelCheque.setVisible(false);
        }
    }//GEN-LAST:event_JCtipoPagoActionPerformed

    private void txtNroTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroTarjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroTarjetaActionPerformed

    private void JCfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCfacturaActionPerformed
    String nroPrefijoNotaCredito = JCfactura.getSelectedItem().toString();
    String notaCredito = nroPrefijoNotaCredito.substring(4);
        try {
            if(detalleControl.verificarEstadoFactura(Integer.parseInt(notaCredito.trim()))==0) {
                try {
                    Integer montoCredito  = ventaControlador.getMonto(Integer.parseInt(notaCredito));
                    formateador = new DecimalFormat("###,###.##");
                    txtValorDelCredito.setText(formateador.format(montoCredito));
                    formateador = new DecimalFormat("###,###.##");
                    txtPendienteAplicar.setText(formateador.format(montoCredito));
                } catch (Exception ex) {
                    Logger.getLogger(DetallePagoClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else{
                    Integer montoCredito  = ventaControlador.getMonto(Integer.parseInt(notaCredito));
                    formateador = new DecimalFormat("###,###.##");
                    txtValorDelCredito.setText(formateador.format(montoCredito));
                    Integer montoActualAplicar = detalleControl.getPendienteAplicar(Integer.parseInt(notaCredito.trim()));
                    formateador = new DecimalFormat("###,###.##");
                    txtPendienteAplicar.setText(formateador.format(montoActualAplicar));          
            }
        } catch (Exception ex) {
            Logger.getLogger(DetallePagoClienteForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_JCfacturaActionPerformed
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox JCfactura;
    private javax.swing.JComboBox JCtipoPago;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private org.edisoncor.gui.button.ButtonTask jBGuardar1;
    private org.edisoncor.gui.button.ButtonTask jBeliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelCheque;
    private javax.swing.JPanel jPanelNotaCredito;
    private javax.swing.JPanel jPanelTarjeta;
    private javax.swing.JPanel jPanelVenta;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCantidadTotal2;
    private javax.swing.JLabel labelFechaVenta;
    private javax.swing.JLabel labelMoneda;
    private javax.swing.JTable tbDetallePago;
    public static javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtNroCheque;
    private javax.swing.JTextField txtNroTarjeta;
    public static javax.swing.JTextField txtPagado;
    public static javax.swing.JTextField txtPendiente;
    private javax.swing.JTextField txtPendienteAplicar;
    private javax.swing.JTextField txtValor;
    private javax.swing.JTextField txtValorDelCredito;
    // End of variables declaration//GEN-END:variables
}
