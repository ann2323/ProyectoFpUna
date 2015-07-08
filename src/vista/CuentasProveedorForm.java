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
public class CuentasProveedorForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form CuentasForm
     */
    public CuentasProveedorForm() {
        initComponents();
        establecerBotones("Edicion");
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
        establecerBotones("Nuevo");
        txtFecha.setText(getFechaActual());
        txtFecha.setEnabled(false);
        txtTotalAbonado.setEnabled(false);
        nuevoDetalle();
        nuevoDetalle2();
        getMonedaVector();
        getProveedor();
        getNroPago();
    }
     
     private void limpiar() {
        txtProveedorNombre.setText("");
        txtProveedorCi.setText("");
        txtTotalAbonado.setText("");
        txtNroPago.setText("");
        saldoCuota=0;
        monto=0;
        montoAbonado=0;
        fila=0;
        modeloBusqueda = new DefaultTableModel();
        modeloD=new DefaultTableModel();
        nuevoDetalle();
        nuevoDetalle2();
    }
    private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(true);
            
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(true);
      
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(false);
            
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(false);
   
                break;
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
   private void borrar(){
          
        if("".equals(txtNroPago.getText())){
            showMessageDialog(this, "Por favor seleccione un numero de pago", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{    try {
                
               cuentaC.delete(Integer.parseInt(txtNroPago.getText()));
               showMessageDialog(null, "Operación exitosa"); 
               limpiar();
               getProveedor();
               getNroPago();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }
   private void cancelar(){
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            establecerBotones("Edicion");
            limpiar();
            getNroPago();
             if (modeloNroPago.getRowCount() == 0) {
                limpiar(); 
                establecerBotones("Edicion"); 
                modoBusqueda(false); 
                return;
            }
            if (k >= 0){
                    limpiar(); 
                    datosActualesNroPago(); 
                    establecerBotones("Edicion");
                    modoBusqueda(false);
                    return;
            }
        
        }
    
    }
    private void datosActuales(){
            if (bNuevo.isEnabled() == true) {
            txtProveedorCi.setText(modeloBusqueda.getValueAt(k, 0).toString());
            txtProveedorNombre.setText(modeloBusqueda.getValueAt(k, 1).toString());  
           
        }
         establecerBotones("Edicion");
    }
   private void datosActualesNroPago(){
           if (bNuevo.isEnabled() == true) {
            TBdetalleCuenta2.setColumnSelectionInterval(0, 0);
            txtFecha.setText(modeloNroPago.getValueAt(k2, 0).toString());
            txtNroPago.setText(modeloNroPago.getValueAt(k2, 1).toString()); 
            txtProveedorCi.setText(modeloNroPago.getValueAt(k2, 2).toString());
            txtProveedorNombre.setText(modeloNroPago.getValueAt(k2, 3).toString());
            JCproyecto.removeAllItems();
            JCmoneda.removeAllItems();;
            JCproyecto.addItem(modeloNroPago.getValueAt(k2, 4));
            JCmoneda.addItem(modeloNroPago.getValueAt(k2, 5));
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
        txtProveedorCi.setEnabled(false);
        txtProveedorNombre.setEnabled(false);
        txtTotalAbonado.setEnabled(false);
        JCmoneda.setEnabled(false);
        JCproyecto.setEnabled(false);
        TBdetalleCuenta1.setEnabled(false);
        TBdetalleCuenta2.setEnabled(false);
     
        } else {
        txtNroPago.setEditable(true);
        txtNroPago.setBackground(Color.white);
        txtNroPago.setEnabled(true);
        txtFecha.setEnabled(true);
        txtProveedorCi.setEnabled(true);
        txtProveedorNombre.setEnabled(true);
        txtTotalAbonado.setEnabled(true);
        JCmoneda.setEnabled(true);
        JCproyecto.setEnabled(true);
        TBdetalleCuenta1.setEnabled(true);
        TBdetalleCuenta2.setEnabled(true);     
        }
    }
    private void guardar() throws ParseException, Exception{
        if ("".equals(txtProveedorCi.getText())) {
            showMessageDialog(null, "Debe ingresar un cliente.", "Atención", INFORMATION_MESSAGE);
            txtProveedorCi.requestFocusInWindow();
            return;
          } else if (JCmoneda.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el medio de pago", "Atención", INFORMATION_MESSAGE);
            JCmoneda.requestFocusInWindow();
            return;
        } else if ("".equals(txtNroPago.getText())) {
            showMessageDialog(null, "Debe seleccionar un nro de pago", "Atención", INFORMATION_MESSAGE);
            JCmoneda.requestFocusInWindow();
            return;
        }
        else {
           if(showConfirmDialog (null, "Está seguro de guardar la operacion?", "Confirmar", YES_NO_OPTION) == YES_OPTION){      
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formateador.parse(txtFecha.getText());
            cuentaCabecera.setFecha(date);
            int idProv = provC.devuelveId(txtProveedorCi.getText().replace(".", ""));
            cuentaCabecera.setProveedorId(idProv);
            Moneda moneda = (Moneda) this.JCmoneda.getSelectedItem();
            cuentaCabecera.setMonedaId(moneda.getMonedaId());
            cuentaCabecera.setProyectoId(Integer.parseInt(JCproyecto.getSelectedItem().toString()));
            cuentaCabecera.setNroFactura(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim()));
            cuentaCabecera.setNroPago(Integer.parseInt(txtNroPago.getText().toString().trim()));
            cuentaCabecera.setTotalSaldo(Integer.parseInt(txtTotalAbonado.getText().replace(".", "").trim()));
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
                                cuentaDetalle.setMontoAbonado(Integer.parseInt(TBdetalleCuenta1.getValueAt(i, 2).toString().replace(".", "").trim()));
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
        txtProveedorCi = new javax.swing.JTextField();
        txtProveedorNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        JCmoneda = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtTotalAbonado = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TBdetalleCuenta1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TBdetalleCuenta2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        JCproyecto = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        jLabel6 = new javax.swing.JLabel();
        txtNroPago = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cuentas Proveedores");
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
        jLabel1.setText("Proveedor:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 195, 67, 22));

        txtProveedorCi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorCiKeyPressed(evt);
            }
        });
        getContentPane().add(txtProveedorCi, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 195, 84, -1));
        getContentPane().add(txtProveedorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 195, 145, -1));

        jLabel2.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel2.setText("Fecha");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 140, 40, 21));
        getContentPane().add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 139, 84, -1));

        jLabel3.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel3.setText("Moneda:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, 53, -1));

        JCmoneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(JCmoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 180, 102, -1));

        jLabel4.setText("Total Saldo:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 463, 67, 25));
        getContentPane().add(txtTotalAbonado, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 465, 127, -1));

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

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 345, -1, -1));
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

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 244, -1, -1));

        jLabel5.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel5.setText("Proyecto");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 53, 33));

        JCproyecto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        getContentPane().add(JCproyecto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 102, -1));

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CUENTAS PROVEEDORES");
        jLabel7.setOpaque(true);
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1240, 56));

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

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 1240, 55));

        jLabel6.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel6.setText("Nro. de Pago");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 168, 78, 20));

        txtNroPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroPagoKeyPressed(evt);
            }
        });
        getContentPane().add(txtNroPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 167, 84, -1));

        jLabel8.setToolTipText("");
        jLabel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Cuenta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 530, 110));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
        getMonedaVector();
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
                establecerBotones("Edicion");
                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
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

        int montoAux, abonado;
        try{
        abonado = Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().replace(".", "").trim());
        } catch (Exception ex){
         abonado = Integer.parseInt(TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 2).toString().trim()+"0");
        }
        try{
        montoAux=(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 2).toString().replace(".", "").trim()));
        }catch (Exception ex){
         montoAux=Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 2).toString().trim()+"0");
        }
        
        if (TBdetalleCuenta1.getSelectedColumn()==3 && TBdetalleCuenta1.getValueAt(TBdetalleCuenta1.getSelectedRow(), 3).equals("PENDIENTE")){
          
        TBdetalleCuenta1.setValueAt("PAGADO", TBdetalleCuenta1.getSelectedRow(), 3);
        txtTotalAbonado.setText(Integer.toString(Integer.parseInt(txtTotalAbonado.getText())-abonado));
        }
        if (evt.getKeyCode()==KeyEvent.VK_F5){
        TBdetalleCuenta1.setValueAt('0', TBdetalleCuenta1.getSelectedRow(), 2);
        TBdetalleCuenta1.setValueAt("PENDIENTE", TBdetalleCuenta1.getSelectedRow(), 3);
        txtTotalAbonado.setText(Integer.toString(Integer.parseInt(txtTotalAbonado.getText().replace(".", "").trim())+abonado));
        }
        if(Integer.parseInt(txtTotalAbonado.getText())==0){
        TBdetalleCuenta1.setValueAt("PAGADO", TBdetalleCuenta1.getSelectedRow(), 3);
        }
  
    }//GEN-LAST:event_TBdetalleCuenta1KeyPressed

    private void TBdetalleCuenta2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBdetalleCuenta2KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_F11){
            if(TBdetalleCuenta2.getSelectedColumn()>0){
            TBdetalleCuenta2.getFocusCycleRootAncestor();
            }
            if (TBdetalleCuenta2.getSelectedColumn()==0) {
            try {         
                    try {
                        int nroPago=cuentaC.getNroPagoDB(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim()));
                        txtNroPago.setText(Integer.toString(nroPago));
                    } catch (Exception ex) {
                        Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                 
            } catch (Exception ex) {
                Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        try {
            cargarCuotasFacturas(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString()));
        } catch (Exception ex) {
            Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            txtTotalAbonado.setText(cuentaC.getMonto(Integer.parseInt(TBdetalleCuenta2.getValueAt(TBdetalleCuenta2.getSelectedRow(), 0).toString().trim())).toString());
        } catch (Exception ex) {
            Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        }     
       getNroPago();
        }
    }//GEN-LAST:event_TBdetalleCuenta2KeyPressed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed

        cancelar();
       
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(CuentasProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                establecerBotones("Edicion");
                for(int c=0; c<modeloNroPago.getRowCount(); c ++){
                    if (modeloNroPago.getValueAt(c, 1).toString().equals(bf3.retorno)){
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox JCmoneda;
    private javax.swing.JComboBox JCproyecto;
    private javax.swing.JTable TBdetalleCuenta1;
    private javax.swing.JTable TBdetalleCuenta2;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNroPago;
    private javax.swing.JTextField txtProveedorCi;
    private javax.swing.JTextField txtProveedorNombre;
    private javax.swing.JFormattedTextField txtTotalAbonado;
    // End of variables declaration//GEN-END:variables

}  


