
package vista;

import controlador.ClienteControlador;
import controlador.ComponentesControlador;
import controlador.DepositoControlador;
import controlador.DetalleCuentaControlador;
import controlador.DetalleFacturaVenta;
import controlador.FacturaCabeceraCompraControlador;
import controlador.FacturaCabeceraVentaControlador;
import controlador.PrefijoFacturaControlador;
import controlador.ProveedorControlador;
import controlador.ProyectoControlador;
import controlador.ReciboProveedorControlador;
import controlador.SaldoVentaControlador;
import controlador.StockControlador;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.Graphics; 
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
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
import java.util.Vector;
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
public class ReciboProveedorForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form facturaVenta
     * @throws java.lang.Exception
     */
    
    int k, k3;
    
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloBusquedaFacturas = new DefaultTableModel();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    
    ProveedorControlador provC = new ProveedorControlador();
    ReciboProveedorControlador reciboControlar = new ReciboProveedorControlador();
    
    FacturaCabeceraCompraControlador compraControlador = new FacturaCabeceraCompraControlador();
    public ReciboProveedorForm() throws Exception {
        initComponents();
        getProveedores();
        getFacturasProveedor();
    }
    
         private void getProveedores() {
        try {
            modeloBusqueda.setColumnCount(0);
            modeloBusqueda.setRowCount(0);
           
            try (ResultSet rs = provC.datosBusqueda2()) {
           
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
     
    
     private void getFacturasProveedor() {
        try {
            modeloBusquedaFacturas.setColumnCount(0);
            modeloBusquedaFacturas.setRowCount(0);
           
            try (ResultSet rs = compraControlador.datosTablaBusquedaFacturasPendientes()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloBusquedaFacturas.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloBusquedaFacturas.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
     
   
    
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        return formateador.format(ahora); 
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
    
    
     private void nuevo() throws Exception {
        limpiar();
        establecerBotones("Nuevo");
        txtFecha.setText(getFechaActual());
       
        
    }
      
     private void limpiarBusqueda(){
        tbVistaFacturasPendientes.removeAll();
        txtNroRecibo.setText("");
        txtFactura.setText("");
        txtProveedor1.setText("");
        txtProveedor.setText("");
        //nuevoDetalle();
     }
     
     
     private void limpiar() {
        //txtPrefijoVenta.setText("");
        tbVistaFacturasPendientes.removeAll();
        txtFactura.setText("");
        txtProveedor1.setText("");
        txtTotal.setText("");
             
    }
     
    private void cancelar(){
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            establecerBotones("Edicion");
            limpiar();
        }
    
    }
     
      private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bSuspender.setEnabled(true);
                bImprimir.setEnabled(true);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bSuspender.setEnabled(true);
                bImprimir.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bSuspender.setEnabled(false);
                bImprimir.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bSuspender.setEnabled(false);
                bImprimir.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
        if ("".equals(txtFecha.getText())) {
            showMessageDialog(null, "Debe ingresar un una fecha.", "Atención", INFORMATION_MESSAGE);
            txtFecha.requestFocusInWindow();
            return;
        } else if ("".equals(txtFecha.getText())) {
            showMessageDialog(null, "Debe ingresar la fecha de venta", "Atención", INFORMATION_MESSAGE);
            txtFecha.requestFocusInWindow();
            return;
        } else if ("".equals(txtFactura.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtFactura.requestFocusInWindow();
            return;
        }else if ("".equals(txtProveedor1.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtProveedor1.requestFocusInWindow();
            return;
        }
  
       }
           
       
    
     public void datosActuales() throws Exception{
       if (bNuevo.isEnabled() == true) {
            txtProveedor.setText(modeloBusqueda.getValueAt(k, 0).toString());
            txtProveedor1.setText(modeloBusqueda.getValueAt(k, 1).toString());
       }
       establecerBotones("Nuevo");
    }
     
      private void datosActuales2(){
        txtFactura.setText(modeloBusquedaFacturas.getValueAt(k3, 1).toString());
        cargarFacturasPendientes(Integer.parseInt(txtFactura.getText()));
        //datosActualesFacturaPendiente();
        establecerBotones("Nuevo");
    }
      
    private void cargarFacturasPendientes(int factura) {
        tbVistaFacturasPendientes.removeAll();
        try {
            
            try (ResultSet rs = reciboControlar.getFacturasPendientes(factura)) {
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

                 tbVistaFacturasPendientes.setModel(modeloDetalleBusqueda);
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
        tbVistaFacturasPendientes = new javax.swing.JTable();
        txtProveedor1 = new javax.swing.JTextField();
        lbCliente = new javax.swing.JLabel();
        txtNroRecibo = new javax.swing.JTextField();
        txtFactura = new javax.swing.JFormattedTextField();
        lbNroRecibo = new javax.swing.JLabel();
        lbFecha = new javax.swing.JLabel();
        btnDetallePago = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bSuspender = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bImprimir = new org.edisoncor.gui.button.ButtonTask();
        lbDatosGenerales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetallePagoVenta = new javax.swing.JTable();
        lbFecha1 = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JFormattedTextField();
        txtFecha = new datechooser.beans.DateChooserCombo();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Recibo de Pago Proveedor");
        setToolTipText("");
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setToolTipText("");
        jPanel1.setMinimumSize(new java.awt.Dimension(780, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(785, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 510, 85, -1));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total a pagar");
        jPanel1.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 510, 80, 20));

        tbVistaFacturasPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro. Prefijo", "Nro. Factura", "Fecha Vencimiento", "Plazo", "Monto Pendiente"
            }
        ));
        tbVistaFacturasPendientes.getTableHeader().setReorderingAllowed(false);
        tbVistaFacturasPendientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tbVistaFacturasPendientesFocusLost(evt);
            }
        });
        tbVistaFacturasPendientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbVistaFacturasPendientesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbVistaFacturasPendientes);
        if (tbVistaFacturasPendientes.getColumnModel().getColumnCount() > 0) {
            tbVistaFacturasPendientes.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 990, 220));
        jPanel1.add(txtProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 210, -1));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Proveedor");
        jPanel1.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 70, -1));

        txtNroRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroReciboActionPerformed(evt);
            }
        });
        jPanel1.add(txtNroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 80, -1));

        txtFactura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFacturaActionPerformed(evt);
            }
        });
        txtFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFacturaKeyPressed(evt);
            }
        });
        jPanel1.add(txtFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 80, -1));

        lbNroRecibo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbNroRecibo.setText("Nro. Recibo");
        jPanel1.add(lbNroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 76, -1));

        lbFecha.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbFecha.setText("Factura");
        lbFecha.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 50, 20));

        btnDetallePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1430364091_my-invoices.png"))); // NOI18N
        btnDetallePago.setText("Detalle de Pago");
        btnDetallePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetallePagoActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetallePago, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 150, 40));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel2.add(bNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 5, -1, -1));

        bSuspender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        bSuspender.setText("Guardar");
        bSuspender.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bSuspender.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        bSuspender.setDescription(" ");
        bSuspender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSuspenderActionPerformed(evt);
            }
        });
        jPanel2.add(bSuspender, new org.netbeans.lib.awtextra.AbsoluteConstraints(269, 5, -1, -1));

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
        jPanel2.add(bCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(516, 5, -1, -1));

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
        jPanel2.add(bImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(763, 5, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, -1));

        lbDatosGenerales.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 11)); // NOI18N
        lbDatosGenerales.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)), "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.add(lbDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1010, 30));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Aharoni", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE FACTURAS PENDIENTES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(361, 361, 361)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(393, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 990, 30));

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

        lbFecha1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbFecha1.setText("Fecha ");
        lbFecha1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbFecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, -1, 20));

        txtProveedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorKeyPressed(evt);
            }
        });
        jPanel1.add(txtProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 80, -1));

        txtFecha.setLocale(new java.util.Locale("es", "BO", ""));
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 49, 1020, 661));

        jPanel5.setBackground(new java.awt.Color(51, 94, 137));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(101, 25));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PAGO PROVEEDOR");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 17, -1, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1031, 49));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNroReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroReciboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroReciboActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbVistaFacturasPendientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesKeyPressed
       //establecerBotones("Ed");
      
    }//GEN-LAST:event_tbVistaFacturasPendientesKeyPressed

    private void bSuspenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSuspenderActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bSuspenderActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void tbVistaFacturasPendientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesFocusLost
         
       
    }//GEN-LAST:event_tbVistaFacturasPendientesFocusLost

    private void bImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bImprimirActionPerformed
     
    }//GEN-LAST:event_bImprimirActionPerformed

    private void txtFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtFactura.getText())) {
                Integer prov = 0;
                try {
                    prov = provC.devuelveId(txtProveedor.getText().replace(".","").trim());
                } catch (Exception ex) {
                    Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(vencimiento,'dd/mm/yyyy') as \"FechaVenc\", precio_total as \"Total\"";
                bf.tabla = "compra";
                bf.order = "nro_factura";
  
                bf.filtroBusqueda = "estado='PENDIENTE' or estado='CONFIRMADO' and es_factura='S' and proveedor_id= "+ prov+ "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);

                for(int c=0; c<modeloBusquedaFacturas.getRowCount(); c ++){
                    if (modeloBusquedaFacturas.getValueAt(c, 0).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
                        k3 = c;
                        try {
                            datosActuales2();
                        } catch (Exception ex) {
                            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }

            }
        }
    }//GEN-LAST:event_txtFacturaKeyPressed

    private void btnDetallePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetallePagoActionPerformed
         try {
            DetallePagoVentaForm detallePago = new DetallePagoVentaForm();
            MenuPrincipalForm.jDesktopPane1.add(detallePago);
            detallePago.toFront();
            detallePago.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDetallePagoActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        try {
            nuevo();
        } catch (Exception ex) {
            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bNuevoActionPerformed

    private void txtProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtProveedor.getText())) {

                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7) as \"CI\", nombre||' '||apellido as \"Proveedor\"";
                bf.tabla = "proveedor";
                bf.order = "proveedor_id";
                bf.filtroBusqueda = "estado!='INACTIVO'";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);

                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
                        k = c;
                        try {
                            datosActuales();
                        } catch (Exception ex) {
                            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }

            }
        }
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void txtFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaActionPerformed
       
    }//GEN-LAST:event_txtFacturaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bImprimir;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private org.edisoncor.gui.button.ButtonTask bSuspender;
    private javax.swing.JButton btnDetallePago;
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
    private javax.swing.JLabel lbFecha1;
    private javax.swing.JLabel lbNroRecibo;
    public static javax.swing.JTable tbDetallePagoVenta;
    private javax.swing.JTable tbVistaFacturasPendientes;
    private javax.swing.JFormattedTextField txtFactura;
    private datechooser.beans.DateChooserCombo txtFecha;
    private javax.swing.JTextField txtNroRecibo;
    private javax.swing.JFormattedTextField txtProveedor;
    private javax.swing.JTextField txtProveedor1;
    public static javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables

       
}
