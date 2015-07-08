/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;


import controlador.ClienteControlador;
import controlador.CuentaCabeceraControlador;
import controlador.DetalleCuentaControlador;
import controlador.FacturaCabeceraCompraControlador;
import controlador.ProveedorControlador;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.CuentaCabecera;
import modelo.DetalleCuenta;
import modelo.Moneda;


/**
 *
 * @author anex
 */
public class CuentasClientesForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form CuentasForm
     */
    public CuentasClientesForm() {
        initComponents();
        establecerBotones("Nuevo");
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
     ClienteControlador clienteControlador = new ClienteControlador();
     
     
     
     DefaultTableModel modeloD = new DefaultTableModel();
     DefaultTableModel modeloNroPago = new DefaultTableModel();
     DefaultTableModel modeloBusqueda = new DefaultTableModel();
     DefaultTableModel modeloFacturasPendientes = new DefaultTableModel();
     DefaultTableModel modeloCuotasPendientes = new DefaultTableModel();
     Moneda monedaModel = new Moneda();
     CuentaCabecera cuentaCabecera = new CuentaCabecera();
     DetalleCuenta cuentaDetalle = new DetalleCuenta();
     
        private void nuevo() {
        limpiar();
        establecerBotones("Nuevo");
        txtFecha.setText(getFechaActual());
        txtFecha.setEnabled(false);
        nuevoDetalle();
        getMonedaVector();
        getCliente();
        getNroPago();
    }
     
     private void limpiar() {
        txtClienteNombre.setText("");
        txtClienteCi.setText("");
        txtTotalAbonado.setText("");
        txtNroPago.setText("");
        nuevoDetalle();     
    }
    private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(true);
                //bBuscar.setEnabled(false);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bBorrar.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(true);
                //bBuscar.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(false);
                //bBuscar.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(false);
                //bBuscar.setEnabled(true);
                break;
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
                TBdetalleCuenta2.setModel(modeloD);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    private void getMonedaVector() {
        JCmoneda.removeAll();
        Vector<Moneda> compVec = new Vector<Moneda>();
        try {
          
            try (ResultSet rs = compraControlador.datoCombo()) {
                while(rs.next()){
                    monedaModel=new Moneda();
                    monedaModel.setMonedaId(rs.getInt(1));
                    monedaModel.setNombre(rs.getString(2));           
                    compVec.add(monedaModel);
                }
                rs.close();
                                    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        DefaultComboBoxModel md1 = new DefaultComboBoxModel(compVec); 
        JCmoneda.setModel(md1);
    }
        private void getNroPago() {
        modeloNroPago=new DefaultTableModel();
        try {
            modeloNroPago.setColumnCount(0);
            modeloNroPago.setRowCount(0);
                      
            try (ResultSet rs = cuentaC.getNroPagoClientes()) {
           
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
    private void getCliente() {
        try {
            modeloBusqueda.setColumnCount(0);
            modeloBusqueda.setRowCount(0);
           
            try (ResultSet rs = clienteControlador.datosBusqueda()) {
           
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
   private void borrar(){
          
        if(txtNroPago.getText() == ""){
            showMessageDialog(this, "Por favor seleccione un numero de pago", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{    try {
                
               cuentaC.delete(Integer.parseInt(txtNroPago.getText()));
               showMessageDialog(null, "Operación exitosa"); 
               limpiar();
               getCliente();
               getNroPago();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }
   private void cancelar() throws ParseException{
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            establecerBotones("Edicion");
            limpiar();
            txtFecha.setText(getFechaActual());
            txtFecha.setEnabled(false);
            getNroPago();
            getMonedaVector();
             if (modeloNroPago.getRowCount() == 0) {
                limpiar(); 
                establecerBotones("Edicion"); 
                modoBusqueda(false); 
                return;
            }
            if (k >= 0){
                    limpiar(); 
                    //datosActualesNroPago(); 
                    establecerBotones("Edicion");
                    modoBusqueda(false);
                    return;
            }
        
        }
    
    }
    private void datosActuales(){
             if (bNuevo.isEnabled() == true) {
                txtClienteCi.setText(modeloBusqueda.getValueAt(k, 0).toString());
                txtClienteNombre.setText(modeloBusqueda.getValueAt(k, 1).toString());  
            }
         establecerBotones("Edicion");
    }
   private void datosActualesNroPago(){
           if (bNuevo.isEnabled() == true) {
            txtFecha.setText(modeloNroPago.getValueAt(k2, 0).toString());
            txtNroPago.setText(modeloNroPago.getValueAt(k2, 1).toString()); 
            txtClienteCi.setText(modeloNroPago.getValueAt(k2, 2).toString());
            txtClienteNombre.setText(modeloNroPago.getValueAt(k2, 3).toString());
            //JCproyecto.removeAllItems();
            //JCmoneda.removeAllItems();
            //JCproyecto.addItem(modeloNroPago.getValueAt(k2, 4));
            JCmoneda.setSelectedItem(modeloNroPago.getValueAt(k2, 5));
            cargarFacturaActual(Integer.parseInt(modeloNroPago.getValueAt(k2, 6).toString()));     
            cargarCuotasFacturas(Integer.parseInt(modeloNroPago.getValueAt(k2, 6).toString()));
            txtTotalAbonado.setText(modeloNroPago.getValueAt(k2, 7).toString());
        }
       //  establecerBotones("Nuevo");
    }
    private void cargarFacturaActual(Integer nro_factura){
          
            try {
            modeloFacturasPendientes.setColumnCount(0);
            modeloFacturasPendientes.setRowCount(0);
          
            TBdetalleCuenta2.setModel(modeloFacturasPendientes);
            
           
            try (ResultSet rs = cuentaC.getFacturaActualCliente(nro_factura)) {
           
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
           
            try (ResultSet rs = cuentaC.getFacturasPendientesClientes(ci_id)) {
           
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
                    TBdetalleCuenta1.setColumnSelectionAllowed(false);
                    TBdetalleCuenta1.setRowSelectionAllowed(false);
                    TBdetalleCuenta1.setCellSelectionEnabled(true);
                   
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
      TBdetalleCuenta1.setModel(modeloCuotasPendientes);
      
     
    }
    private void buscar(){
        limpiar();
        establecerBotones("Buscar");
        modoBusqueda(true);
    }
    private void modoBusqueda(boolean v){
        if (v == true) {
        txtNroPago.setEditable(true);
        txtNroPago.requestFocusInWindow();
        txtNroPago.setBackground(Color.yellow);
        txtFecha.setEnabled(false);
        txtClienteCi.setEnabled(false);
        txtClienteNombre.setEnabled(false);
        txtTotalAbonado.setEnabled(false);
        JCmoneda.setEnabled(false);
        //JCproyecto.setEnabled(false);
        TBdetalleCuenta1.setEnabled(false);
        TBdetalleCuenta2.setEnabled(false);
     
        } else {
        txtNroPago.setEditable(true);
        txtNroPago.setBackground(Color.white);
        txtNroPago.setEnabled(true);
        txtFecha.setEnabled(true);
        txtClienteCi.setEnabled(true);
        txtClienteNombre.setEnabled(true);
        txtTotalAbonado.setEnabled(true);
        JCmoneda.setEnabled(true);
        //JCproyecto.setEnabled(true);
        TBdetalleCuenta1.setEnabled(true);
        TBdetalleCuenta2.setEnabled(true);     
        }
    }
    private void guardar() throws ParseException, Exception{
        if ("".equals(txtClienteCi.getText())) {
            showMessageDialog(null, "Debe ingresar un cliente.", "Atención", INFORMATION_MESSAGE);
            txtClienteCi.requestFocusInWindow();
            return;
          } else if ("".equals(txtNroPago.getText())) {
            showMessageDialog(null, "Debe seleccionar un nro de pago", "Atención", INFORMATION_MESSAGE);
            return;
        }
        else {
           if(showConfirmDialog (null, "Está seguro de guardar la operacion?", "Confirmar", YES_NO_OPTION) == YES_OPTION){      
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formateador.parse(txtFecha.getText());
            cuentaCabecera.setFecha(date);
            int idCliente = clienteControlador.devuelveId(txtClienteCi.getText());
            cuentaCabecera.setClienteId(idCliente);
            Moneda moneda = (Moneda) this.JCmoneda.getSelectedItem();
            cuentaCabecera.setMonedaId(moneda.getMonedaId());
            //cuentaCabecera.setProyectoId(Integer.parseInt(JCproyecto.getSelectedItem().toString()));
            cuentaCabecera.setNroFactura(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim()));
            cuentaCabecera.setNroPago(Integer.parseInt(txtNroPago.getText().toString().trim()));
            cuentaCabecera.setTotalSaldo(Integer.parseInt(txtTotalAbonado.getText()));
            if (bNuevo.isEnabled() == true) {
                    try {
                    int i = 0;
                    try {                             
                        while (i<TBdetalleCuenta1.getRowCount()){
                            cuentaDetalle.setNroFactura(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim()));
                            Date date_pago = formateador.parse(TBdetalleCuenta1.getValueAt(i, 0).toString());
                            cuentaDetalle.setFechaPago(date_pago);
                            cuentaDetalle.setCuotas(Integer.parseInt(TBdetalleCuenta1.getValueAt(i, 1).toString().trim()));
                            try {
                                cuentaDetalle.setMontoAbonado(Integer.parseInt(TBdetalleCuenta1.getValueAt(i, 2).toString().trim()));
                            } catch (NumberFormatException e) {
                                cuentaDetalle.setMontoAbonado(Integer.parseInt(TBdetalleCuenta1.getValueAt(i, 2).toString()+"0"));
                            } 
                            cuentaDetalle.setEstado(TBdetalleCuenta1.getValueAt(i, 3).toString());
                       
                            cuentaDB.update(cuentaDetalle);
                            i++;
                        }
                    } catch (Exception ex) {
                     
                       showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                        //Guardar.requestFocusInWindow();
                        return;
                    }
                    if (Integer.parseInt(txtTotalAbonado.getText())==0) {        
                        cuentaCabecera.setEstado("PAGADO");
                    }
                    cuentaC.update(cuentaCabecera);
                    nuevo();
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                    return;
                }
            } 
        }
     }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtClienteCi = new javax.swing.JTextField();
        txtClienteNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTotalAbonado = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TBdetalleCuenta1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TBdetalleCuenta2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        bBorrar = new org.edisoncor.gui.button.ButtonTask();
        jLabel6 = new javax.swing.JLabel();
        txtNroPago = new javax.swing.JTextField();
        JCmoneda = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Cuentas de Clientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cuentaCliente.png"))); // NOI18N
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

        jLabel1.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel1.setText("Cliente");

        txtClienteCi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteCiActionPerformed(evt);
            }
        });
        txtClienteCi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClienteCiKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel2.setText("Fecha");

        jLabel4.setText("Total Saldo:");

        txtTotalAbonado.setEnabled(false);

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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Factura Venta", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

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

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CUENTAS CLIENTES");
        jLabel7.setOpaque(true);

        jPanel1.setFont(new java.awt.Font("Aharoni", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 5));

        bNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        bNuevo.setText("Nuevo");
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

        bGuardar.setForeground(new java.awt.Color(0, 51, 102));
        bGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        bGuardar.setText("Guardar");
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

        bBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        bBorrar.setText("Eliminar");
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

        jLabel6.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel6.setText("Nro. de Pago");

        txtNroPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroPagoKeyPressed(evt);
            }
        });

        JCmoneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Efectivo", "Tarjeta", " " }));

        jLabel3.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel3.setText("Moneda");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1240, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtClienteCi, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNroPago, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(JCmoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(274, 274, 274)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalAbonado, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNroPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtClienteCi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCmoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalAbonado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        jPanel3.getAccessibleContext().setAccessibleName("Datos Factura de Compra");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
        getMonedaVector();
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtClienteCiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteCiKeyPressed
        txtNroPago.setText("");
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtClienteCi.getText())) {
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "cedula as \"CI\", nombre||' '||apellido as \"Cliente\"";
                bf.tabla = "Cliente";
                bf.order = "cliente_id";
                bf.filtroBusqueda = "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
                        k = c;
                        datosActuales();
                        try {
                            cargarFacturasPendientes(clienteControlador.devuelveId(txtClienteCi.getText()));
                            cargarCuotasFacturas(0);
                            
                        } catch (Exception ex) {
                            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    return;
               }
                }
               
                
            }
        }
          
    }//GEN-LAST:event_txtClienteCiKeyPressed

    private void TBdetalleCuenta1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBdetalleCuenta1KeyPressed

        int montoAux, abonado;
        try{
        abonado = Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().trim());
        } catch (Exception ex){
         abonado = Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().trim()+"0");
        }
        try{
        montoAux=(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 2).toString().trim()));
        }catch (Exception ex){
         montoAux=Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 2).toString().trim()+"0");
        }
        System.out.println("Monto aux"+montoAux);
        if (TBdetalleCuenta1.getSelectedColumn()==3 && TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 3).equals("PENDIENTE")){
          
        TBdetalleCuenta1.setValueAt("PAGADO", TBdetalleCuenta1.getSelectedRow(), 3);
        txtTotalAbonado.setText(Integer.toString(Integer.parseInt(txtTotalAbonado.getText())-abonado));
        }
        if (evt.getKeyCode()==KeyEvent.VK_F5){
        TBdetalleCuenta1.setValueAt('0', TBdetalleCuenta1.getSelectedRow(), 2);
        TBdetalleCuenta1.setValueAt("PENDIENTE", TBdetalleCuenta1.getSelectedRow(), 3);
        txtTotalAbonado.setText(Integer.toString(Integer.parseInt(txtTotalAbonado.getText())+abonado));
        }
        if(Integer.parseInt(txtTotalAbonado.getText())==0){
        TBdetalleCuenta1.setValueAt("PAGADO", TBdetalleCuenta1.getSelectedRow(), 3);
        }
  
    }//GEN-LAST:event_TBdetalleCuenta1KeyPressed

    private void TBdetalleCuenta2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBdetalleCuenta2KeyPressed
        try {
            txtNroPago.setText(cuentaC.getNroPagoDB(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim())).toString());
        } catch (Exception ex) {
            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cargarCuotasFacturas(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim()));
        } catch (Exception ex) {
            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            txtTotalAbonado.setText(cuentaC.getMonto(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim())).toString());
        } catch (Exception ex) {
            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       getNroPago();
    }//GEN-LAST:event_TBdetalleCuenta2KeyPressed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        try {
            cancelar();
        } catch (ParseException ex) {
            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(CuentasClientesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGuardarActionPerformed

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        borrar();
        limpiar();
        txtFecha.setText(getFechaActual());
        txtFecha.setEnabled(false);
        getCliente();
        getNroPago();
        getMonedaVector();
    }//GEN-LAST:event_bBorrarActionPerformed

    private void txtNroPagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroPagoKeyPressed
             if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtNroPago.getText())) {
                TBdetalleCuenta2.setRowSelectionInterval(0,0);
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "cc.nro_pago";
                bf.tabla = "cuenta_cabecera cc, cliente pro, moneda mon";
                bf.order = "cc.nro_pago";
                bf.filtroBusqueda = "cc.cliente_id=pro.cliente_id and cc.moneda_id=mon.moneda_id";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modeloNroPago.getRowCount(); c ++){
                    if (modeloNroPago.getValueAt(c, 1).toString().equals(bf.retorno)){
                        modoBusqueda(false);
                        establecerBotones("Edicion");
                        k2 = c;
                         datosActualesNroPago();
                    return;
                    }
                }
                
            }
            getNroPago();
            for(int c=0; c<modeloNroPago.getRowCount(); c ++){
                if (modeloNroPago.getValueAt(c, 1).toString().equals(txtNroPago.getText())){
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    k2 = c;                   
                    datosActualesNroPago();
                   return;
                }
            }
        }
    }//GEN-LAST:event_txtNroPagoKeyPressed

    private void txtClienteCiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteCiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteCiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox JCmoneda;
    private javax.swing.JTable TBdetalleCuenta1;
    private javax.swing.JTable TBdetalleCuenta2;
    private org.edisoncor.gui.button.ButtonTask bBorrar;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtClienteCi;
    private javax.swing.JTextField txtClienteNombre;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNroPago;
    private javax.swing.JFormattedTextField txtTotalAbonado;
    // End of variables declaration//GEN-END:variables

}  


