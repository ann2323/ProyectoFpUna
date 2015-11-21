
package vista;

import controlador.ClienteControlador;
import controlador.FacturaPendienteControlador;
import controlador.ReciboControlador;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;


/**
 *
 * @author Pathy
 */
public class ReciboForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form Recibo
     * @throws java.lang.Exception
     */
    
    
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    
    Formatter formato = new Formatter();
    int k2;

    FacturaPendienteControlador facturaPendienteControlador = new FacturaPendienteControlador();
    ClienteControlador cliC = new ClienteControlador();
    ReciboControlador reciboC = new ReciboControlador();
    
    
    public ReciboForm() throws Exception {
        initComponents();
        getClientes();
       
            
    }
    
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        return formateador.format(ahora); 
    }
        
    public String totalLetras;
    
    private void getClientes() {
        try {
            
            modeloBusqueda.setColumnCount(0);
            modeloBusqueda.setRowCount(0);
           
            try (ResultSet rs = cliC.datosCliente()) {
           
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
    
    public String totalLetras(int precio_total) throws SQLException, Exception
    {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        String res="";
        try {
        String query = "SELECT (f_convnl(CAST("+precio_total+ " as numeric)));";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
       res=rs.getString(1);
        }
           
        } catch(HibernateException e){
            throw new Exception("Error al consultar el vencimiento de pago: \n" + e.getMessage());
        }
         return res;
    }
    
    
     private void nuevo() {
        limpiar();
        establecerBotones("Nuevo");
        txtFecha.setText(getFechaActual());      
    }
      
    
     
     private void limpiar() {
        //txtPrefijoVenta.setText("");
        tbDetalleRecibo.removeAll();
        txtCliente.setText("");
        txtCliente1.setText("");
        txtTotal.setText("");
   
    }
     
      private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                btnGuardar.setEnabled(true);
                bImprimir.setEnabled(true);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                btnGuardar.setEnabled(true);
                bImprimir.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                btnGuardar.setEnabled(false);
                bImprimir.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
        if ("".equals(txtFecha.getText())) {
            showMessageDialog(null, "Debe ingresar un una fecha.", "Atención", INFORMATION_MESSAGE);
            txtFecha.requestFocusInWindow();
            return;
        } else if ("".equals(txtNroRecibo.getText())) {
            showMessageDialog(null, "Debe ingresar el número del recibo", "Atención", INFORMATION_MESSAGE);
            txtFecha.requestFocusInWindow();
            return;
        } else if ("".equals(txtCliente.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtCliente.requestFocusInWindow();
            return;
        }else if ("".equals(txtCliente1.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtCliente1.requestFocusInWindow();
            return;
        }
        else {
           if(showConfirmDialog (null, "Está seguro de guardar el recibo?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            int id= reciboC.nuevoCodigo(); 
           }
        }
       }
            
      //retorna el numero de cedula y nombre de cliente
     private void datosActuales(){
            DecimalFormat forma = new DecimalFormat("###,###.##"); 
             try {
                String cedula=forma.format(Integer.parseInt((modeloBusqueda.getValueAt(k2, 1).toString())));
                txtCliente.setText(cedula);
                txtCliente1.setText(modeloBusqueda.getValueAt(k2, 2).toString());
             } catch (Exception ex) {
                Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
             } 
       
        cargarFacturasPendientes(Integer.parseInt(modeloBusqueda.getValueAt(k2, 0).toString()));
        datosActualesFacturaPendiente();
        establecerBotones("Nuevo");
    }
    
     //carga las facturas pendientes de un cliente determinado
     private void datosActualesFacturaPendiente(){
           DecimalFormat forma = new DecimalFormat("###,###.##");  
           int i=0;
           while (!"".equals(modeloDetalleBusqueda.getValueAt(i, 0).toString())){
            tbDetalleRecibo.setValueAt(modeloDetalleBusqueda.getValueAt(i, 0), i, 0);
            String numeroFactura=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 1).toString()));
            tbDetalleRecibo.setValueAt(numeroFactura, i, 1);
            tbDetalleRecibo.setValueAt(modeloDetalleBusqueda.getValueAt(i, 2), i, 2);
            tbDetalleRecibo.setValueAt(modeloDetalleBusqueda.getValueAt(i, 3), i, 3);
            String total=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 4).toString()));
            tbDetalleRecibo.setValueAt(total, i, 4);
            tbDetalleRecibo.setValueAt(modeloDetalleBusqueda.getValueAt(i, 5), i, 5);
            i++;
           }
     }

     //retorna los datos de la tabla factura_pendiente para cargar las facturas pendientes de un cliente especifico
    private void cargarFacturasPendientes(int idCliente) {
        tbDetalleRecibo.removeAll();
        try {
            
            try (ResultSet rs =facturaPendienteControlador.getFacturasPendientes(idCliente)) {
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
                   
                 tbDetalleRecibo.setModel(modeloDetalleBusqueda);
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
        txtTotal = new javax.swing.JFormattedTextField();
        labelTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetalleRecibo = new javax.swing.JTable();
        txtCliente1 = new javax.swing.JTextField();
        lbCliente = new javax.swing.JLabel();
        txtNroRecibo = new javax.swing.JTextField();
        txtCliente = new javax.swing.JFormattedTextField();
        lbNroRecibo = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        lbFecha = new javax.swing.JLabel();
        btnDetallePago = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        btnGuardar = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bImprimir = new org.edisoncor.gui.button.ButtonTask();
        lbDatosGenerales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetallePagoVenta = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Recibo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/factura_venta.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1275, 680));
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

        jPanel1.setToolTipText("");
        jPanel1.setMinimumSize(new java.awt.Dimension(780, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(785, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 510, 85, -1));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total a pagar");
        jPanel1.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 510, 80, 20));

        tbDetalleRecibo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro. Prefijo", "Nro. Factura", "Fecha Vencimiento", "Cuota", "Total", "Estado"
            }
        ));
        tbDetalleRecibo.getTableHeader().setReorderingAllowed(false);
        tbDetalleRecibo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDetalleReciboKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbDetalleRecibo);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 990, 220));
        jPanel1.add(txtCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 190, -1));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Cliente");
        jPanel1.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 40, -1));
        jPanel1.add(txtNroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 80, -1));

        txtCliente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClienteKeyPressed(evt);
            }
        });
        jPanel1.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 80, -1));

        lbNroRecibo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbNroRecibo.setText("Nro. Recibo");
        jPanel1.add(lbNroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 76, -1));
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 120, 140, -1));

        lbFecha.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbFecha.setText("Fecha ");
        lbFecha.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, -1, 20));

        btnDetallePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1430364091_my-invoices.png"))); // NOI18N
        btnDetallePago.setText("Detalle de Pago");
        btnDetallePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetallePagoActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetallePago, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, 160, 50));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        bNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        bNuevo.setText("Nuevo");
        bNuevo.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bNuevo.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        bNuevo.setDescription(" ");
        bNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNuevoActionPerformed(evt);
            }
        });
        jPanel2.add(bNuevo);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnGuardar.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        btnGuardar.setDescription(" ");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardar);

        bCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        bCancelar.setText("Cancelar");
        bCancelar.setToolTipText("");
        bCancelar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bCancelar.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        bCancelar.setDescription(" ");
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });
        jPanel2.add(bCancelar);

        bImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/imprimir.png"))); // NOI18N
        bImprimir.setText("Generar Recibo");
        bImprimir.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 16)); // NOI18N
        bImprimir.setCategorySmallFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        bImprimir.setDescription(" ");
        bImprimir.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bImprimirActionPerformed(evt);
            }
        });
        jPanel2.add(bImprimir);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, -1));

        lbDatosGenerales.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 11)); // NOI18N
        lbDatosGenerales.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)), "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.add(lbDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 990, 140));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Aharoni", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE FACTURAS PENDIENTES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(447, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(307, 307, 307))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 990, 30));

        tbDetallePagoVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbDetallePagoVenta);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 327, 120, 100));

        jPanel5.setBackground(new java.awt.Color(51, 94, 137));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(101, 25));

        jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("RECIBO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(487, 487, 487))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1166, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        getClientes();
        nuevo();
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbDetalleReciboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetalleReciboKeyPressed
      
      
    }//GEN-LAST:event_tbDetalleReciboKeyPressed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(ReciboForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            establecerBotones("Edicion");
            limpiar();
        }
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bImprimirActionPerformed
      
      if(showConfirmDialog (null, "Está seguro de imprimir la factura?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
       
      }  
    }//GEN-LAST:event_bImprimirActionPerformed

    private void txtClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtCliente.getText())) {

                BuscarForm bf = new BuscarForm(null, true);
                bf.columnas = "cedula as \"CI\", nombre||' '||apellido as \"Cliente\"";
                bf.tabla = "Cliente";
                bf.order = "cliente_id";
                bf.filtroBusqueda = "estado != 'Inactivo'";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);

                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 1).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
                        k2 = c;
                        datosActuales();
                        return;
                    }
                }
            }
        }
    }//GEN-LAST:event_txtClienteKeyPressed

    private void btnDetallePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetallePagoActionPerformed
         try {
            DetallePagoVentaForm detallePago = new DetallePagoVentaForm();
            MenuPrincipalForm.jDesktopPane1.add(detallePago);
            detallePago.toFront();
            detallePago.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(ReciboForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDetallePagoActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bImprimir;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.JButton btnDetallePago;
    private org.edisoncor.gui.button.ButtonTask btnGuardar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbDatosGenerales;
    private javax.swing.JLabel lbFecha;
    private javax.swing.JLabel lbNroRecibo;
    public static javax.swing.JTable tbDetallePagoVenta;
    private javax.swing.JTable tbDetalleRecibo;
    private javax.swing.JFormattedTextField txtCliente;
    private javax.swing.JTextField txtCliente1;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNroRecibo;
    public static javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables

   
        
    
}
