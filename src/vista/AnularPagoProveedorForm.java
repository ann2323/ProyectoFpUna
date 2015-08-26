/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;


import controlador.CuentaCabeceraControlador;
import controlador.DetalleCuentaControlador;
import controlador.FacturaCabeceraCompraControlador;
import controlador.ProveedorControlador;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import modelo.CuentaCabecera;
import modelo.DetalleCuenta;
import modelo.Moneda;


/**
 *
 * @author anex
 */
public class AnularPagoProveedorForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form CuentasForm
     */
    public AnularPagoProveedorForm() {
        initComponents();
    }
     public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        return formateador.format(ahora);
        
    }
      int k, k2, clienteid;
      Integer  saldoCuota=0, monto=0, montoAbonado=0, fila=0;
      
      
     
     DetalleCuentaControlador cuentaDB = new DetalleCuentaControlador();
     FacturaCabeceraCompraControlador compraControlador = new  FacturaCabeceraCompraControlador();
     CuentaCabeceraControlador cuentaC = new CuentaCabeceraControlador();
     ProveedorControlador provC = new ProveedorControlador();
     
     
     
     DefaultTableModel modeloD = new DefaultTableModel();
     DefaultTableModel modeloD2 = new DefaultTableModel();
     DefaultTableModel modeloNroPago = new DefaultTableModel();
     DefaultTableModel modeloBusqueda = new DefaultTableModel();
     DefaultTableModel modeloFacturasPendientes = new DefaultTableModel();
     DefaultTableModel modeloCuotasPendientes = new DefaultTableModel();
     Moneda monedaModel = new Moneda();
     CuentaCabecera cuentaCabecera = new CuentaCabecera();
     DetalleCuenta cuentaDetalle = new DetalleCuenta();
     
        private void nuevo() {
        limpiar();
        txtFecha.setText(getFechaActual());
        txtFecha.setEnabled(false);
        nuevoDetalle();
        nuevoDetalle2();
        getProveedor();
        getNroPago();
    }
     
     private void limpiar() {
        txtProveedorNombre.setText("");
        txtProveedorCi.setText("");
        txtNroPago.setText("");
        saldoCuota=0;
        modeloBusqueda = new DefaultTableModel();
        nuevoDetalle();
        nuevoDetalle2();
    }
    
       private void cancelar(){
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            limpiar();
            getNroPago();
             if (modeloNroPago.getRowCount() == 0) {
                limpiar(); 
                return;
            }
            /*if (k >= 0){
                    limpiar(); 
                    datosActualesNroPago();         
                    return;
            }*/
        
        }
    
    }
    private void nuevoDetalle2() {
        try {
            try (ResultSet rs = cuentaDB.datosD()) {
                
                modeloD2.setRowCount(0);
                modeloD2.setColumnCount(0);
                
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloD2.addColumn(rsMd.getColumnLabel(i));
                }
                    for (int i = 0; i < 4; i++) {
                        modeloD2.addRow(new Object[]{"","","","",""});
                    }

                TBdetalleCuenta2.setModel(modeloD);
                TBdetalleCuenta2.setColumnSelectionAllowed(false);
                TBdetalleCuenta2.setRowSelectionAllowed(false);
                TBdetalleCuenta2.setCellSelectionEnabled(true);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
    private void nuevoDetalle() {
        try {
            try (ResultSet rs = cuentaDB.datosD()) {
                
                modeloD.setRowCount(0);
                modeloD.setColumnCount(0);
                
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloD.addColumn(rsMd.getColumnLabel(i));
                }
                    for (int i = 0; i < 4; i++) {
                        modeloD.addRow(new Object[]{"","","","",""});
                    }

                TBdetalleCuenta1.setModel(modeloD);
                TBdetalleCuenta1.setColumnSelectionAllowed(false);
                TBdetalleCuenta1.setRowSelectionAllowed(false);
                TBdetalleCuenta1.setCellSelectionEnabled(true);
             
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
 
        private void getNroPago() {
        try {
            modeloNroPago.setColumnCount(0);
            modeloNroPago.setRowCount(0);
                      
            try (ResultSet rs = cuentaC.getNroPago()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloNroPago.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloNroPago.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    private void getProveedor() {
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
   
   /*private boolean validarPagoCuotas(Integer cuotaPagada, Integer nroFacturaCompra) throws Exception{
       int pagoEn = compraControlador.getCuota(nroFacturaCompra);
       if (pagoEn==cuotaPagada && Integer.parseInt(TBdetalleCuenta.getValueAt(TBdetalleCuenta.getSelectedRow(), 5).toString())==0){
           compraControlador.updateEstado(nroFacturaCompra);
           cuentaCabecera.setEstado("PAGADO");
           return true;
       } else{        
       return false;
       }
   }*/
   private void anular(){
          
        if("".equals(txtNroPago.getText())){
            showMessageDialog(this, "Por favor seleccione un numero de pago", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{    try {
        
                int idProv = provC.devuelveId(txtProveedorCi.getText().replace(".", ""));
                
                
                cuentaDetalle.setCuotas(Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 1).toString()));
                
                cuentaDetalle.setNroFactura(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(),0).toString()));
                
                cuentaC.updateSaldoCuenta(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(),0).toString()),idProv,Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().replace(".", "").trim()));
                
                cuentaDB.updateEstado(cuentaDetalle);
                
                
               
                int idCuenta=cuentaC.devuelveId(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(),0).toString()));
                cuentaDetalle.setCuentaId(idCuenta);
                cuentaDetalle.setIdDetalleCuenta(cuentaDB.nuevaLinea());
                cuentaDetalle.setCuotas(Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 1).toString().trim()));         
                cuentaDetalle.setMontoAbonado(Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().replace(".", "").trim()));
                cuentaDetalle.setEstado("PENDIENTE");
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                Date date = formateador.parse(txtFecha.getText());
                cuentaDetalle.setFechaPago(date);
                
                cuentaDB.insert(cuentaDetalle);
               showMessageDialog(null, "Operación exitosa"); 
               limpiar();
               getProveedor();
               getNroPago();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }

    private void datosActuales(){
           
            txtProveedorCi.setText(modeloBusqueda.getValueAt(k, 0).toString());
            txtProveedorNombre.setText(modeloBusqueda.getValueAt(k, 1).toString());  
         
    }
    
   private void datosActualesNroPago(){
 
            TBdetalleCuenta2.setColumnSelectionInterval(0, 0);
            txtFecha.setText(modeloNroPago.getValueAt(k2, 0).toString());
            txtNroPago.setText(modeloNroPago.getValueAt(k2, 1).toString()); 
            txtProveedorCi.setText(modeloNroPago.getValueAt(k2, 2).toString());
            txtProveedorNombre.setText(modeloNroPago.getValueAt(k2, 3).toString());
     
            cargarFacturaActual(Integer.parseInt(modeloNroPago.getValueAt(k2, 6).toString()));     
            cargarCuotasFacturas(Integer.parseInt(modeloNroPago.getValueAt(k2, 6).toString()));
        
       //  establecerBotones("Nuevo");
    }
    private void cargarFacturaActual(Integer nro_factura){
          
            try {
            modeloFacturasPendientes.setColumnCount(0);
            modeloFacturasPendientes.setRowCount(0);
          
            TBdetalleCuenta2.setModel(modeloFacturasPendientes);
            
           
            try (ResultSet rs = cuentaC.getFacturaActual(nro_factura)) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
               int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                   modeloFacturasPendientes.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloFacturasPendientes.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
     private void cargarFacturasPendientes(Integer ci_id){
            try {
            modeloFacturasPendientes.setColumnCount(0);
            modeloFacturasPendientes.setRowCount(0);
            TBdetalleCuenta2.setModel(modeloFacturasPendientes);
           
            try (ResultSet rs = cuentaC.getFacturasPendientes(ci_id)) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                   modeloFacturasPendientes.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloFacturasPendientes.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
           
    }
 
    private void cargarCuotasFacturas(int nro_factura){
      
            try {
            modeloCuotasPendientes.setColumnCount(0);
            modeloCuotasPendientes.setRowCount(0);
          
           
            try (ResultSet rs = cuentaC.getCuotasPendientes(nro_factura)) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                   modeloCuotasPendientes.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloCuotasPendientes.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
          TBdetalleCuenta1.setModel(modeloCuotasPendientes);   
     
    }
 
    
  
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtProveedorCi = new javax.swing.JTextField();
        txtProveedorNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TBdetalleCuenta1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TBdetalleCuenta2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        jLabel6 = new javax.swing.JLabel();
        txtNroPago = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Anular Pago Proveedor");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/añadirPago.png"))); // NOI18N
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

        jLabel1.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel1.setText("Proveedor");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 196, 67, 22));

        txtProveedorCi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorCiKeyPressed(evt);
            }
        });
        getContentPane().add(txtProveedorCi, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 84, -1));

        txtProveedorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorNombreActionPerformed(evt);
            }
        });
        getContentPane().add(txtProveedorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, 145, -1));

        jLabel2.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel2.setText("Fecha");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 141, 40, 21));
        getContentPane().add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 84, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Cuenta ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        TBdetalleCuenta1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Fecha de pago", "Cuotas", "Monto Abonado", "Estado"
            }
        ));
        TBdetalleCuenta1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBdetalleCuenta1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(TBdetalleCuenta1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 349, -1, -1));
        jPanel3.getAccessibleContext().setAccessibleName("Datos Factura de Compra");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Factura Compra", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        TBdetalleCuenta2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nro Factura", "Fecha", "Monto Total"
            }
        ));
        TBdetalleCuenta2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBdetalleCuenta2KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(TBdetalleCuenta2);
        if (TBdetalleCuenta2.getColumnModel().getColumnCount() > 0) {
            TBdetalleCuenta2.getColumnModel().getColumn(0).setHeaderValue("Nro Factura");
            TBdetalleCuenta2.getColumnModel().getColumn(2).setResizable(false);
            TBdetalleCuenta2.getColumnModel().getColumn(2).setHeaderValue("Monto Total");
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 248, -1, -1));

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ANULACION PAGO PROVEEDORES");
        jLabel7.setOpaque(true);
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1240, 56));

        jPanel1.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 5));

        bCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        bCancelar.setText("Cancelar");
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
        jPanel1.add(bCancelar);

        bGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1434930410_note-delete.png"))); // NOI18N
        bGuardar.setText("Anular");
        bGuardar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bGuardar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bGuardar.setDescription(" ");
        bGuardar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bGuardar.setIconTextGap(2);
        bGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(bGuardar);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 1240, 55));

        jLabel6.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel6.setText("Nro. de Pago");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 169, 78, 20));

        txtNroPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroPagoKeyPressed(evt);
            }
        });
        getContentPane().add(txtNroPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 84, -1));

        jLabel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Cuenta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 410, 120));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtProveedorCiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorCiKeyPressed
       txtNroPago.setText("");
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtProveedorCi.getText())) {
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7) as \"CI\", nombre||' '||apellido as \"Proveedor\"";
                bf.tabla = "Proveedor";
                bf.order = "proveedor_id";
                bf.filtroBusqueda = "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        k = c;
                        datosActuales();
                        try {
                            cargarFacturasPendientes(provC.devuelveId(txtProveedorCi.getText().replace(".", "")));
                            cargarCuotasFacturas(0);
                            
                        } catch (Exception ex) {
                            Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    return;
               }
                }
               
                
            }
        }
          
    }//GEN-LAST:event_txtProveedorCiKeyPressed

    private void TBdetalleCuenta1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBdetalleCuenta1KeyPressed

  
    }//GEN-LAST:event_TBdetalleCuenta1KeyPressed

    private void TBdetalleCuenta2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBdetalleCuenta2KeyPressed
            if(evt.getKeyCode() == KeyEvent.VK_F11){
                try {
            cargarCuotasFacturas(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString()));
              } catch (Exception ex) {
            Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
              }
            int nro_pago=0;
                try {
                    nro_pago = cuentaC.devuelveId(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString()));
                } catch (Exception ex) {
                    Logger.getLogger(AnularPagoProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            txtNroPago.setText(Integer.toString(nro_pago));
            }
    }//GEN-LAST:event_TBdetalleCuenta2KeyPressed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
            cancelar();
       
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
            anular();
    }//GEN-LAST:event_bGuardarActionPerformed

    private void txtNroPagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroPagoKeyPressed
            if ("*".equals(txtNroPago.getText())) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                TBdetalleCuenta2.setRowSelectionInterval(0,0);
                BuscarForm bf3 = new BuscarForm( null, true);
                bf3.columnas = "ca.nro_pago";
                bf3.tabla = "cuenta_cabecera ca, proveedor pro, moneda mm";
                bf3.order = "";
                bf3.filtroBusqueda = "ca.proveedor_id=pro.proveedor_id and ca.moneda_id=mm.moneda_id";
                bf3.setLocationRelativeTo(this);
                bf3.setVisible(true);
                
                for(int c=0; c<modeloNroPago.getRowCount(); c ++){
                    if (modeloNroPago.getValueAt(c, 1).toString().equals(bf3.retorno)){
                        k2 = c;
                         datosActualesNroPago();
                    return;
                    }
                }
                
            }
            getNroPago();
            for(int c=0; c<modeloNroPago.getRowCount(); c ++){
                if (modeloNroPago.getValueAt(c, 1).toString().equals(txtNroPago.getText())){
                    k2 = c;                   
                    datosActualesNroPago();
                   return;
                }
            }
        }
    }//GEN-LAST:event_txtNroPagoKeyPressed

    private void txtProveedorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorNombreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TBdetalleCuenta1;
    private javax.swing.JTable TBdetalleCuenta2;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNroPago;
    private javax.swing.JTextField txtProveedorCi;
    private javax.swing.JTextField txtProveedorNombre;
    // End of variables declaration//GEN-END:variables

}  


