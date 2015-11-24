/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package vista;
import controlador.ClienteControlador;
import java.awt.Color;
import controlador.ComponentesControlador;
import controlador.DepositoControlador;
import controlador.DetalleFacturaVenta;
import controlador.FacturaCabeceraVentaControlador;
import controlador.SaldoVentaControlador;
import controlador.StockControlador;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Deposito;
import modelo.DetalleVenta;
import modelo.SaldoVenta;
import modelo.Stock;
import modelo.Venta;

/**
 *
 * @author anex
 */
public class NotaCreditoVentaForm extends javax.swing.JInternalFrame {

 public NotaCreditoVentaForm() throws Exception {
        initComponents();
       
      
        JCdeposito.setSelectedIndex(0);
        txtTotal.setEditable(false);
        txtSubTotal.setEditable(false);
        txtCantidadTotal.setEditable(false);
        txtIva10.setEditable(false);
        txtIva5.setEditable(false);
         
    }
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
        
    }
    DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
    DefaultTableModel modeloComponentes = new DefaultTableModel();
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloD = new DefaultTableModel();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    DefaultTableModel modeloNroFactura;
    DefaultTableModel modeloNroFacturaPagada;
    
    Stock stock = new Stock();
    
     Integer subTotal= 0, totaldesc=0;
     double iva10=0.0, iva5=0.0;
     Integer  cantProducto=0;
     int k, k2, factRef;
     DecimalFormat formateador = new DecimalFormat("###,###.##");
     
    StockControlador stockCont = new StockControlador();
    DepositoControlador depBD = new DepositoControlador();
    DetalleFacturaVenta facturaDetalleCont = new DetalleFacturaVenta();
    FacturaCabeceraVentaControlador ventaControlador = new  FacturaCabeceraVentaControlador();
    ClienteControlador clienteC = new ClienteControlador();
    ComponentesControlador componentesControl = new ComponentesControlador();
    SaldoVentaControlador saldoC = new SaldoVentaControlador();
    
    Deposito depModel = new  Deposito();
    DetalleVenta ventaD = new DetalleVenta();
    Venta venta = new Venta();
    SaldoVenta saldoModel = new SaldoVenta();
   

    private void getDepositosVector() {
        JCdeposito.removeAll();
        Vector<Deposito> depVec = new Vector<Deposito>();
        try {
          
            try (ResultSet rs = depBD.datoCombo()) {
                while(rs.next()){
                    depModel=new Deposito();
                    depModel.setCodigo(rs.getString(1));
                    depModel.setNombre(rs.getString(2));
                    depModel.setDescripcion(rs.getString(3));
                    depVec.add(depModel);
                }
                rs.close();
                                    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        DefaultComboBoxModel md1 = new DefaultComboBoxModel(depVec); 
        JCdeposito.setModel(md1);
    }
     private void nuevo() {
        limpiar();
        establecerBotones("Nuevo");
        nuevoDetalle();
        getCliente();
        getDepositosVector();
        getComponentes();
        getNroFactura();
        getNroFacturaPagadas();         
    }
      private void limpiar() {
        txtPrefijoVenta.setText("");
        JCdeposito.setSelectedIndex(0);
        tbDetalleVenta.removeAll();
        txtNroNotaCredito.setText("");
        txtFacturaReferenciada.setText("");
        txtCedula.setText("");
        txtNombreCliente.setText("");
        txtCantidadTotal.setText("");
        txtDescuento.setText("");
        txtSubTotal.setText("");
        txtTotal.setText("");
        txtIva10.setText("");
        txtIva5.setText("");
        subTotal= 0;
        totaldesc=0;
        iva10=0.0;
        iva5=0.0;
        cantProducto=0;
        nuevoDetalle();     
    }
      private void establecerBotones(String modo) {
         
          switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                jBCancelar.setEnabled(true);
                jBsuspender.setEnabled(true);
                bBuscar.setEnabled(false);
                jBGuardar1.setEnabled(true);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                jBCancelar.setEnabled(false);
                jBsuspender.setEnabled(true);
                bBuscar.setEnabled(true);
                jBGuardar1.setEnabled(true);

                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                jBCancelar.setEnabled(false);
                jBsuspender.setEnabled(false);
                bBuscar.setEnabled(false);
                jBGuardar1.setEnabled(false);        
                
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                jBCancelar.setEnabled(true);
                jBsuspender.setEnabled(false);
                bBuscar.setEnabled(false);
                jBGuardar1.setEnabled(false);
                break;
        }
    }
     
    private void guardar() throws ParseException, Exception{
       suspender();
    }
    
    private void getNroFactura() {
        
        modeloNroFactura=new DefaultTableModel();
        try {
            modeloNroFactura.setColumnCount(0);
            modeloNroFactura.setRowCount(0);
                      
            try (ResultSet rs = ventaControlador.getNroFactura1()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloNroFactura.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloNroFactura.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
        private void getNroFacturaPagadas() {
        
        modeloNroFacturaPagada=new DefaultTableModel();
        try {
            modeloNroFacturaPagada.setColumnCount(0);
            modeloNroFacturaPagada.setRowCount(0);
                      
            try (ResultSet rs = ventaControlador.getNroFacturaPagadas()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloNroFacturaPagada.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloNroFacturaPagada.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
  
        private void suspender() throws ParseException, Exception{
        
        if ("".equals(txtFechaVenta.getText())) {
            showMessageDialog(null, "Debe ingresar una fecha.", "Atención", INFORMATION_MESSAGE);
            txtFechaVenta.requestFocusInWindow();
            return;
        } else if ("".equals(txtFacturaReferenciada.getText())) {
            showMessageDialog(null, "Debe ingresar el nro de factura de venta", "Atención", INFORMATION_MESSAGE);
            txtFacturaReferenciada.requestFocusInWindow();
            return;
        } else if ("".equals(txtCedula.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtCedula.requestFocusInWindow();
            return;
        }else if ("".equals(txtNombreCliente.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtNombreCliente.requestFocusInWindow();
            return;
        }else if ("".equals(txtNroNotaCredito.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de factura", "Atención", INFORMATION_MESSAGE);
            txtNroNotaCredito.requestFocusInWindow();
            return;
        }else if ("".equals(txtPrefijoVenta.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de prefijo", "Atención", INFORMATION_MESSAGE);
            txtPrefijoVenta.requestFocusInWindow();
            return;
        }else if (JCdeposito.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el deposito", "Atención", INFORMATION_MESSAGE);
            JCdeposito.requestFocusInWindow();
            return;
        } 
        else {
           if(showConfirmDialog (null, "Está seguro de guardar la factura?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            int id= ventaControlador.nuevoCodigo(); 
            venta.setVentaId(id);
            if(!txtIva10.getText().equals("")){
               venta.setIva10(Integer.parseInt(txtIva10.getText().trim().replace(".", "")));
            }
            
             if(!txtIva5.getText().equals("")){
               venta.setIva5(Integer.parseInt(txtIva5.getText().trim().replace(".","")));
            }
            venta.setNroPrefijo(txtPrefijoVenta.getText());
            venta.setNroFactura(Integer.parseInt(txtNroNotaCredito.getText()));
            Date ahora = new Date();
            venta.setFecha(ahora);
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formateador.parse(txtFechaVenta.getText());
            venta.setFecha(date);
            venta.setEsFactura("N");
            venta.setPagoContado("");
            venta.setFactReferenciada(Integer.parseInt(txtFacturaReferenciada.getText().trim()));
            venta.setEstado("BORRADOR");
            int idCliente = clienteC.devuelveId(txtCedula.getText().replace(".", ""));
            venta.setClienteId(idCliente);
            Deposito dep = (Deposito) this.JCdeposito.getSelectedItem();
            venta.setCodDeposito(dep.getCodigo());
            
            venta.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText().replace(".", "").trim()));
        
            venta.setDescuento(Integer.parseInt(txtDescuento.getText()));
          
            venta.setPrecioTotal(Integer.parseInt(txtTotal.getText().replace(".", "")));
            
          
            venta.setVencimiento(ahora);
            
            //id de la facturaRefenciada
            if (venta.getEsFactura().equals("N")){
               factRef=ventaControlador.devuelveId(Integer.parseInt(txtFacturaReferenciada.getText()));
            }
            
            int idSaldo = saldoC.nuevoCodigo();
            saldoModel.setSaldoVentaId(idSaldo);
            saldoModel.setEstado("PENDIENTE");
            saldoModel.setPrefijo(Integer.parseInt(txtPrefijoVenta.getText()));
            saldoModel.setNumero(Integer.parseInt((txtNroNotaCredito.getText())));
            saldoModel.setEsFactura("N");
            saldoModel.setSaldo(Integer.parseInt(txtTotal.getText().replace(".", "")));
          
                    try {
                    int i = 0;
                    int borrado  = 0;
                    try {
                        int venta_id = ventaControlador.nuevoCodigo();                        
                        while (!"".equals(tbDetalleVenta.getValueAt(i, 0).toString())){
                            ventaD.setVentaId(venta_id);
                            ventaD.setDetalleFacturaId(facturaDetalleCont.nuevaLinea());
                            ventaD.setCodigo(tbDetalleVenta.getValueAt(i, 0).toString());
                            ventaD.setDescripcion(tbDetalleVenta.getValueAt(i, 1).toString()); 
                          try {
                             ventaD.setPrecioUnitario(Integer.parseInt(tbDetalleVenta.getValueAt(i, 2).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                              System.out.println("not a number"); 
                            }
                          try {
                              ventaD.setCantidad(Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString().trim()));
                            } catch (NumberFormatException e) {
                               ventaD.setCantidad(Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString()+"0"));
                            }
                            try {
                                ventaD.setExentas(Integer.parseInt(tbDetalleVenta.getValueAt(i, 4).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                                ventaD.setExentas(Integer.parseInt(tbDetalleVenta.getValueAt(i, 4).toString()+"0"));
                            } 
                              try {
                                ventaD.setSubTotal(Integer.parseInt(tbDetalleVenta.getValueAt(i, 5).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                                ventaD.setSubTotal(Integer.parseInt(tbDetalleVenta.getValueAt(i, 5).toString()+"0"));
                            }  
   
                            if(stockCont.tieneStock(tbDetalleVenta.getValueAt(i, 0).toString(), dep.getCodigo()) < Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString()) ){
                                    showMessageDialog(null, "No hay stock", "Atención", INFORMATION_MESSAGE);
                                    return;        
                            }
                
                          
                            if (bNuevo.isEnabled() == false){ 
                                System.out.println("Entro en el insert de detalle");
                                facturaDetalleCont.insert(ventaD);
                                facturaDetalleCont.updateFactura(factRef, tbDetalleVenta.getValueAt(i, 0).toString());
                                 try {
                                    stockCont.update2(tbDetalleVenta.getValueAt(i, 0).toString(), dep.getCodigo(), Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString().trim().replace(".", "")));
                                } catch (Exception ex) {
                                    stockCont.update2(tbDetalleVenta.getValueAt(i, 0).toString(), dep.getCodigo(), Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString().replace(".", "").trim() + "0"));
                                }
                                i++;
                                //nuevo();
                                //si no inserta, actualiza (factura en suspension)
                            }else{
                                try { 
                                    //borra el detalle para actualizar en caso de que ingrese más componentes
                                    if (borrado == 0){
                                        facturaDetalleCont.borrarDetalle(ventaControlador.devuelveId(venta.getNroFactura()));
                                    }
                                        borrado = 1;
                                          facturaDetalleCont.insert(ventaD);
                                          i++;
                                     }catch(Exception ex){
                                              ex.printStackTrace();
                                              showMessageDialog(null, ex, "Error al actualizar detalle venta", ERROR_MESSAGE);   
                                     }     
                            }
                        }
                    } catch (Exception ex) {
                     
                       showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                        //Guardar.requestFocusInWindow();
                        return;
                    }
                    
                     if (bNuevo.isEnabled() == false){ 
                        venta.setEstado("CONFIRMADO");
                        ventaControlador.insert(venta);
                        txtNroNotaCredito.setText("");
                        nuevo();
                   }else{
                        try {  
                            ventaControlador.borrarCabecera(venta.getNroFactura());
                            ventaControlador.insert(venta);
                            saldoC.insert(saldoModel);
                            limpiar();
                            nuevo();
                            //showMessageDialog(null, "Venta actualizada correctamente");
                           
                        }catch(Exception ex){
                            showMessageDialog(null, ex, "Error al actualizar factura venta", ERROR_MESSAGE);   
                        }        
                    }
                    
                  
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                    //bGuardar.requestFocusInWindow();
                    return;
                }
            
        }
     }
    }
    private void datosActualesComponentes(){    
           tbDetalleVenta.setValueAt(modeloComponentes.getValueAt(k2, 0), tbDetalleVenta.getSelectedRow(), 0);
           tbDetalleVenta.setValueAt(modeloComponentes.getValueAt(k2, 1), tbDetalleVenta.getSelectedRow(), 1);
           formateador = new DecimalFormat("###,###.##");
           String precio=formateador.format((Integer)modeloComponentes.getValueAt(k2, 2));
           tbDetalleVenta.setValueAt(precio, tbDetalleVenta.getSelectedRow(), 2);
           tbDetalleVenta.setColumnSelectionInterval(2, 2);
    }
 
    private void nuevoDetalle() {
        try {
            try (ResultSet rs = facturaDetalleCont.datosD()) {
                
                modeloD.setRowCount(0);
                modeloD.setColumnCount(0);
                
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloD.addColumn(rsMd.getColumnLabel(i));
                }
                    for (int i = 0; i < 12; i++) {
                        modeloD.addRow(new Object[]{"","","","","",""});
                    }
       
                tbDetalleVenta.setModel(modeloD);
                tbDetalleVenta.setColumnSelectionAllowed(false);
                tbDetalleVenta.setRowSelectionAllowed(false);
                tbDetalleVenta.setCellSelectionEnabled(true);
    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    private void cancelar(){
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            establecerBotones("Edicion");
            limpiar();
        }
    
    }
    private void getComponentes() {
         modeloComponentes=new DefaultTableModel();
        try {
            modeloComponentes.setColumnCount(0);
            modeloComponentes.setRowCount(0);
                      
            try (ResultSet rs = componentesControl.getComponentesDatos()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloComponentes.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloComponentes.addRow(fila);
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
           
            try (ResultSet rs = clienteC.datosBusqueda2()) {
           
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
     private void datosActuales(){
             if (bNuevo.isEnabled() == true) {
            txtCedula.setText(modeloBusqueda.getValueAt(k, 0).toString());
            txtNombreCliente.setText(modeloBusqueda.getValueAt(k, 1).toString());  
        }
         establecerBotones("Nuevo");
     }
     
     private void datosActualesNroFactura() {
            DecimalFormat forma = new DecimalFormat("###,###.##");   
            txtPrefijoVenta.setText(modeloNroFactura.getValueAt(k2, 0).toString());
            txtNroNotaCredito.setText(modeloNroFactura.getValueAt(k2, 1).toString());
            try {
                         
                String cedula=forma.format(Integer.parseInt(clienteC.getCedula(modeloNroFactura.getValueAt(k2, 2).toString())));
                txtCedula.setText(cedula);
                txtNombreCliente.setText(clienteC.getNombreCliente(modeloNroFactura.getValueAt(k2, 2).toString()));
                JCdeposito.setSelectedItem(modeloNroFactura.getValueAt(k2, 6).toString());
            
            } catch (Exception ex) {
                Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
            }
  
            txtFechaVenta.setText(modeloNroFactura.getValueAt(k2, 3).toString());
            
            forma = new DecimalFormat("###,###.##");
            String cantidad=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 7).toString().trim().replace(".", "")));
            txtCantidadTotal.setText(cantidad);
            cantProducto = Integer.parseInt(modeloNroFactura.getValueAt(k2, 7).toString().trim().replace(".", ""));
            String totalFormat=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 8).toString().trim().replace(".", "")));
            txtTotal.setText(totalFormat);
            txtDescuento.setText(modeloNroFactura.getValueAt(k2, 9).toString());
            int total = 0;
            total = Integer.parseInt(modeloNroFactura.getValueAt(k2, 8).toString());
            int descuento = 0;
            descuento= Integer.parseInt(modeloNroFactura.getValueAt(k2, 9).toString());
            int subtotal = 0;
            subtotal = total - descuento;
            //seteo el subTotal para que acumule en la búsqueda
            subTotal = subtotal;
            forma = new DecimalFormat("###,###.##");
            String subTotalFormat=forma.format(subtotal);
            txtSubTotal.setText(String.valueOf(subTotalFormat));
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 11).toString()) == 0){
                txtIva10.setText("");
                iva10 = 0.0;
            }else{
                iva10=0.0;
                forma = new DecimalFormat("###,###.##");
                String iva10Format=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 11).toString()));
                txtIva10.setText(iva10Format);
                iva10 = Integer.parseInt(txtIva10.getText().trim().replace(".",""));
            }
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 12).toString())== 0){
                iva5=0.0;
                txtIva5.setText("");
                iva5 = 0.0;
            }else{
                iva5=0.0;
                forma = new DecimalFormat("###,###.##");
                String iva5Format=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 12).toString()));
                txtIva5.setText(iva5Format);
                iva5 = Integer.parseInt(txtIva5.getText().trim().replace(".", ""));
            }    
           
            cargarDetalleFactura(Integer.parseInt(modeloNroFactura.getValueAt(k2, 10).toString())); 
            datosActualesDetalleFactura();
            
    }
     
         private void datosActualesNroFacturaFactura() {
            DecimalFormat forma = new DecimalFormat("###,###.##");   
            txtFacturaReferenciada.setText(modeloNroFactura.getValueAt(k2, 1).toString());
            try {
                         
                String cedula=forma.format(Integer.parseInt(clienteC.getCedula(modeloNroFactura.getValueAt(k2, 2).toString())));
                txtCedula.setText(cedula);
                txtNombreCliente.setText(clienteC.getNombreCliente(modeloNroFactura.getValueAt(k2, 2).toString()));
                JCdeposito.setSelectedItem(modeloNroFactura.getValueAt(k2, 5).toString());
            
            } catch (Exception ex) {
                Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
            }
  

            forma = new DecimalFormat("###,###.##");
            String cantidad=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 6).toString().trim().replace(".", "")));
            txtCantidadTotal.setText(cantidad);
            cantProducto = Integer.parseInt(modeloNroFactura.getValueAt(k2, 6).toString().trim().replace(".", ""));
            String totalFormat=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 7).toString().trim().replace(".", "")));
            txtTotal.setText(totalFormat);
            txtDescuento.setText(modeloNroFactura.getValueAt(k2, 8).toString());
            int total = 0;
            total = Integer.parseInt(modeloNroFactura.getValueAt(k2, 7).toString());
            int descuento = 0;
            descuento= Integer.parseInt(modeloNroFactura.getValueAt(k2, 8).toString());
            int subtotal = 0;
            subtotal = total - descuento;
            //seteo el subTotal para que acumule en la búsqueda
            subTotal = subtotal;
            forma = new DecimalFormat("###,###.##");
            String subTotalFormat=forma.format(subtotal);
            txtSubTotal.setText(String.valueOf(subTotalFormat));
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 10).toString()) == 0){
                txtIva10.setText("");
                iva10 = 0.0;
            }else{
                iva10=0.0;
                forma = new DecimalFormat("###,###.##");
                String iva10Format=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 10).toString()));
                txtIva10.setText(iva10Format);
                iva10 = Integer.parseInt(txtIva10.getText().trim().replace(".",""));
            }
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 11).toString())== 0){
                iva5=0.0;
                txtIva5.setText("");
                iva5 = 0.0;
            }else{
                iva5=0.0;
                forma = new DecimalFormat("###,###.##");
                String iva5Format=forma.format(Integer.parseInt(modeloNroFactura.getValueAt(k2, 11).toString()));
                txtIva5.setText(iva5Format);
                iva5 = Integer.parseInt(txtIva5.getText().trim().replace(".", ""));
            }    
           
            cargarDetalleFacturaFactura(Integer.parseInt(modeloNroFactura.getValueAt(k2, 9).toString())); 
            datosActualesDetalleFactura();
            
    }
        
      private void datosActualesDetalleFactura(){
           DecimalFormat forma = new DecimalFormat("###,###.##");  
          int i=0;
           while (!"".equals(modeloDetalleBusqueda.getValueAt(i, 0).toString())){
            
            tbDetalleVenta.setValueAt(modeloDetalleBusqueda.getValueAt(i, 0), i, 0);
            tbDetalleVenta.setValueAt(modeloDetalleBusqueda.getValueAt(i, 1), i, 1);
            forma = new DecimalFormat("###,###.##");
            String precioUnit=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 2).toString()));
            tbDetalleVenta.setValueAt(precioUnit, i, 2);
            forma = new DecimalFormat("###,###.##");
            String cantidadFormat=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 3).toString()));
            tbDetalleVenta.setValueAt(cantidadFormat, i, 3);
            forma = new DecimalFormat("###,###.##");
            String exentasFormat=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 4).toString()));
            tbDetalleVenta.setValueAt(exentasFormat, i, 4);
            forma = new DecimalFormat("###,###.##");
            String subTotalFormat=forma.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(i, 5).toString()));
            tbDetalleVenta.setValueAt(subTotalFormat, i, 5);
            i++;
           }
     }
        
       private void cargarDetalleFactura(int idVenta) {
        tbDetalleVenta.removeAll();
        modeloDetalleBusqueda=new DefaultTableModel();
        try {
            
            try (ResultSet rs = facturaDetalleCont.getDetalle(idVenta)) {
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
       
   private void cargarDetalleFacturaFactura(int idVenta) {
        tbDetalleVenta.removeAll();
        modeloDetalleBusqueda=new DefaultTableModel();
        try {
            
            try (ResultSet rs = facturaDetalleCont.getDetalleFactura(idVenta)) {
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
    
     
    private void limpiarBusqueda(){
        tbDetalleVenta.removeAll();
        txtNroNotaCredito.setText("");
        txtPrefijoVenta.setText("");
        txtCedula.setText("");
        txtNombreCliente.setText("");
        txtFechaVenta.setText("");
        JCdeposito.removeAll();  
        txtCantidadTotal.setText("");
        txtDescuento.setText("");
        txtCantidadTotal.setText("");
        txtSubTotal.setText("");
        txtTotal.setText("");
        //nuevoDetalle();
     }
    
           private void modoBusqueda(boolean v) {
        if (v == true) {
            txtNroNotaCredito.setEnabled(true);
            txtNroNotaCredito.requestFocusInWindow();
            txtNroNotaCredito.setBackground(Color.yellow);
            txtFacturaReferenciada.setEnabled(false);
            txtPrefijoVenta.setEnabled(false);
            txtCantidadTotal.setEnabled(false);
            txtCedula.setEnabled(false);
            txtTotal.setEnabled(false);
            txtNombreCliente.setEnabled(false);
            txtDescuento.setEnabled(false);
            txtFechaVenta.setEnabled(false);
            txtIva10.setEnabled(false);
            txtIva5.setEnabled(false);
            txtSubTotal.setEnabled(false);
            JCdeposito.setEnabled(false);
            tbDetalleVenta.setEnabled(true);
        
        } else {
            txtNroNotaCredito.setEnabled(true);
            txtNroNotaCredito.setBackground(Color.white);
            txtFacturaReferenciada.setEnabled(true);
            txtPrefijoVenta.setEnabled(true);
            txtPrefijoVenta.setEditable(true);
            txtCantidadTotal.setEnabled(true);
            txtCantidadTotal.setEditable(false);
            txtCedula.setEnabled(true);
            txtTotal.setEnabled(true);
            txtTotal.setEditable(false);
            txtNombreCliente.setEnabled(true);
            txtDescuento.setEnabled(true);
            txtDescuento.setEditable(true);
            txtFechaVenta.setEnabled(true);
            txtIva10.setEnabled(true);
            txtIva5.setEnabled(true);
            txtSubTotal.setEnabled(true);
            txtSubTotal.setEditable(false);
            JCdeposito.setEnabled(true);
            tbDetalleVenta.setEnabled(true);
        
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

        JpanelVenta = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        jBCancelar = new org.edisoncor.gui.button.ButtonTask();
        jBsuspender = new org.edisoncor.gui.button.ButtonTask();
        bBuscar = new org.edisoncor.gui.button.ButtonTask();
        jBGuardar1 = new org.edisoncor.gui.button.ButtonTask();
        lbPrefijo = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtPrefijoVenta = new javax.swing.JTextField();
        txtNroNotaCredito = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        JCdeposito = new javax.swing.JComboBox();
        lbDeposito = new javax.swing.JLabel();
        txtFacturaReferenciada = new javax.swing.JTextField();
        lbFacturaReferenciada = new javax.swing.JLabel();
        lbNroNotaCredito = new javax.swing.JLabel();
        lbCliente = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetalleVenta = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        labelCantidadTotal2 = new javax.swing.JLabel();
        txtIva10 = new javax.swing.JFormattedTextField();
        labelCantidadTotal = new javax.swing.JLabel();
        labelCantidadTotal3 = new javax.swing.JLabel();
        txtCantidadTotal = new javax.swing.JFormattedTextField();
        txtSubTotal = new javax.swing.JFormattedTextField();
        labelSubTotal = new javax.swing.JLabel();
        labelDescuento = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JFormattedTextField();
        labelTotal = new javax.swing.JLabel();
        txtIva5 = new javax.swing.JFormattedTextField();
        labelCantidadTotal4 = new javax.swing.JLabel();
        lbFechaOperacion = new javax.swing.JLabel();
        txtFechaVenta = new datechooser.beans.DateChooserCombo();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Nota Credito Venta");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1434930447_data-edit.png"))); // NOI18N
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

        JpanelVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("NOTA DE CREDITO DE VENTA");
        jLabel7.setOpaque(true);
        JpanelVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-9, 0, 1280, 56));

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

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
        jBCancelar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jBCancelar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        jBCancelar.setDescription(" ");
        jBCancelar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        jBCancelar.setIconTextGap(2);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(jBCancelar);

        jBsuspender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Pause.png"))); // NOI18N
        jBsuspender.setText("Suspender");
        jBsuspender.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jBsuspender.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        jBsuspender.setDescription(" ");
        jBsuspender.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        jBsuspender.setIconTextGap(2);
        jBsuspender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBsuspenderActionPerformed(evt);
            }
        });
        jPanel1.add(jBsuspender);

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        bBuscar.setText("Buscar");
        bBuscar.setActionCommand(" ");
        bBuscar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bBuscar.setDefaultCapable(false);
        bBuscar.setDescription(" ");
        bBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(bBuscar);

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

        JpanelVenta.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 1250, 55));

        lbPrefijo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbPrefijo.setText("Nro. Prefijo");
        JpanelVenta.add(lbPrefijo, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 140, 76, -1));

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaKeyPressed(evt);
            }
        });
        JpanelVenta.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 77, -1));

        txtPrefijoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrefijoVentaActionPerformed(evt);
            }
        });
        txtPrefijoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrefijoVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrefijoVentaKeyTyped(evt);
            }
        });
        JpanelVenta.add(txtPrefijoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 57, -1));

        txtNroNotaCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroNotaCreditoActionPerformed(evt);
            }
        });
        txtNroNotaCredito.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNroNotaCreditoFocusLost(evt);
            }
        });
        txtNroNotaCredito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroNotaCreditoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNroNotaCreditoKeyTyped(evt);
            }
        });
        JpanelVenta.add(txtNroNotaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 77, -1));
        JpanelVenta.add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 200, 250, -1));

        JCdeposito.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NODO1", "NODO13", "NODO2" }));
        JCdeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCdepositoActionPerformed(evt);
            }
        });
        JpanelVenta.add(JCdeposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 190, -1, -1));

        lbDeposito.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbDeposito.setText("Deposito ");
        JpanelVenta.add(lbDeposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, 71, -1));

        txtFacturaReferenciada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFacturaReferenciadaActionPerformed(evt);
            }
        });
        txtFacturaReferenciada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFacturaReferenciadaKeyPressed(evt);
            }
        });
        JpanelVenta.add(txtFacturaReferenciada, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 140, 93, -1));

        lbFacturaReferenciada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbFacturaReferenciada.setText("Factura Referenciada");
        lbFacturaReferenciada.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        JpanelVenta.add(lbFacturaReferenciada, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, -1, -1));

        lbNroNotaCredito.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbNroNotaCredito.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbNroNotaCredito.setText("Nro. Nota de Credito");
        lbNroNotaCredito.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        JpanelVenta.add(lbNroNotaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 171, 120, 20));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Cliente");
        JpanelVenta.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 203, 76, -1));
        JpanelVenta.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, -1, -1));

        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10))); // NOI18N
        JpanelVenta.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1240, 120));

        tbDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbDetalleVenta.setEditingColumn(0);
        tbDetalleVenta.setFillsViewportHeight(true);
        tbDetalleVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDetalleVentaMouseClicked(evt);
            }
        });
        tbDetalleVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tbDetalleVentaFocusLost(evt);
            }
        });
        tbDetalleVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDetalleVentaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbDetalleVenta);

        JpanelVenta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 1240, 230));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("LISTA DE PRODUCTOS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(540, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(530, 530, 530))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        JpanelVenta.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 1240, 30));
        JpanelVenta.add(labelCantidadTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 650, 200, -1));
        JpanelVenta.add(txtIva10, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 520, 87, -1));

        labelCantidadTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal.setText("Cantidad Total");
        JpanelVenta.add(labelCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 520, 90, 20));

        labelCantidadTotal3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal3.setText("Iva 10%");
        JpanelVenta.add(labelCantidadTotal3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 520, 47, -1));

        txtCantidadTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtCantidadTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadTotalActionPerformed(evt);
            }
        });
        JpanelVenta.add(txtCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 520, 52, -1));

        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        JpanelVenta.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 520, 85, -1));

        labelSubTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelSubTotal.setText("Sub Total");
        JpanelVenta.add(labelSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 520, 60, 20));

        labelDescuento.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelDescuento.setText("Descuento");
        JpanelVenta.add(labelDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 550, 60, 20));

        txtDescuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDescuentoFocusLost(evt);
            }
        });
        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyTyped(evt);
            }
        });
        JpanelVenta.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 550, 50, -1));

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel8.setText("%");
        JpanelVenta.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 550, 10, -1));

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        JpanelVenta.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 580, 85, -1));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total");
        JpanelVenta.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 580, 40, 20));
        JpanelVenta.add(txtIva5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 520, 87, -1));

        labelCantidadTotal4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal4.setText("Iva 5%");
        JpanelVenta.add(labelCantidadTotal4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 520, 47, -1));

        lbFechaOperacion.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbFechaOperacion.setText("Fecha Operacion");
        lbFechaOperacion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        JpanelVenta.add(lbFechaOperacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, -1, -1));

        txtFechaVenta.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    txtFechaVenta.setLocale(new java.util.Locale("es", "BO", ""));
    JpanelVenta.add(txtFechaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 120, -1));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(JpanelVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 1272, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(JpanelVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDetalleVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetalleVentaKeyPressed
        
        if (!tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).equals("")&&!tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).equals("")){
        txtDescuento.setText("0");
        formateador = new DecimalFormat();
        
        Integer precio;
        String codigo = tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(),0).toString();

            
        precio = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
        
        Integer Cantidad;
        
        Cantidad = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim().replace(".",""));
		formateador = new DecimalFormat("###,###.##");
        String cantidadDet=formateador.format(Cantidad);
        tbDetalleVenta.setValueAt((cantidadDet), tbDetalleVenta.getSelectedRow(), 3); 
        int total=(precio*Cantidad);
        String totalFormat=(formateador.format(total));

       if (tbDetalleVenta.getSelectedColumn()==5 && !(evt.getKeyCode() == KeyEvent.VK_DELETE)){
                          
         cantProducto=cantProducto+Cantidad;
		 formateador = new DecimalFormat("###,###.##");
         String cantidad=formateador.format(cantProducto);
         txtCantidadTotal.setText(cantidad);      
         if(componentesControl.getTipoIva(codigo)==0){
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 5);      

             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim());      
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim())-   Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim())/1.1; 
             iva10=iva10+j;
             String ivaFormat=formateador.format(Math.round(iva10));
             txtIva10.setText(ivaFormat);
             
         }else if (componentesControl.getTipoIva(codigo)==1){
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 5);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim())-   Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim())/1.05; 
             iva5=iva5+j;
             String ivaFormat=formateador.format(Math.round(iva5));
             txtIva5.setText(ivaFormat);
         }else{
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 4);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 4).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             
            }
       }
    
       if(evt.getKeyCode() == KeyEvent.VK_F9){
           if(tbDetalleVenta.getSelectedRow() == -1){
                showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
            }else{
                if(showConfirmDialog (null, "¿Desea eliminar esta fila?", "Confirmar", YES_NO_OPTION) == YES_OPTION){                   
       
           Integer precio2;
                    
           precio2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
     
           Integer Cantidad2;
      
           Cantidad2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim().replace(".", ""));
          

            int total2=(precio2*Cantidad2);
            cantProducto=cantProducto-Cantidad2;
             
                    subTotal=subTotal-total2;
                    if (componentesControl.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==0) {
                    DecimalFormat formato = new DecimalFormat("###,###.##");
					Double iva102=total2-total2/1.1;
                    
                    iva10=iva10-iva102;
                    String ivaFormat=formato.format(Math.round(iva10));
                    txtIva10.setText(ivaFormat);
                    } else if  (componentesControl.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==1)  {
                    DecimalFormat formato = new DecimalFormat("###,###.##");
					Double iva25=total2-total2/1.05;
            
                    iva5=iva5-iva25;
                    String ivaFormat=formato.format(Math.round(iva5));
                    txtIva5.setText(ivaFormat);
                    }
                    DecimalFormat formato = new DecimalFormat("###,###.##");
                    String subTotalFormat=formato.format(subTotal);
                    txtSubTotal.setText(subTotalFormat);
                    txtTotal.setText(txtSubTotal.getText().trim()); 
				    DecimalFormat formatoCant = new DecimalFormat("###,###.##");
                    String cantidad=formatoCant.format(cantProducto);

                    txtCantidadTotal.setText(cantidad); 
                    modeloD.removeRow(tbDetalleVenta.getSelectedRow());                   
                    modeloD.addRow(new Object[]{"","","","",""});
                    tbDetalleVenta.setModel(modeloD);        
                    tbDetalleVenta.setColumnSelectionInterval(0, 0);
                }
           }}
       
         if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
           Integer precio2;

           precio2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
     
           Integer Cantidad2;
      
           Cantidad2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim());
                    
            int total2=(precio2*Cantidad2);
           
            cantProducto=cantProducto-Cantidad2;
            
            if (componentesControl.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==0) {
                 DecimalFormat FormatDeletIva10 = new DecimalFormat("###,###.##");

                 double iva102=total2-total2/1.1;
                 iva10=iva10- iva102;
                 String ivaFormat=FormatDeletIva10.format(Math.round(iva10));
                 txtIva10.setText(ivaFormat);
            } else if  (componentesControl.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==1)  {
                 DecimalFormat FormatDeletIva5 = new DecimalFormat("###,###.##");
				 Double iva25=total2-total2/1.05;
                 iva5=iva5- iva25;

                 String ivaFormat=FormatDeletIva5.format(Math.round(iva5));
                 txtIva5.setText(ivaFormat);
            }
            DecimalFormat FormatSubTotal = new DecimalFormat("###,###.##");
            subTotal=subTotal-total2;
            String subTotalFormat=FormatSubTotal.format(subTotal);
            txtSubTotal.setText(subTotalFormat);
            txtTotal.setText(txtSubTotal.getText().trim()); 
			DecimalFormat formatoCant = new DecimalFormat("###,###.##");
            String cantidad=formatoCant.format(cantProducto);
            txtCantidadTotal.setText(cantidad);      
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 3);
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 5); 
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 4);
         }
          /* }else{
            try {
                subTotal=subTotal+ Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 5).toString().trim());
            }catch (Exception ex) {
                subTotal=subTotal+ Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 5).toString()+"0");
            }
            txtSubTotal.setText(Integer.toString(subTotal).trim());
            txtTotal.setText(txtSubTotal.getText().trim());
           
           }*/
        }
        if (tbDetalleVenta.getSelectedColumn()==0){
                if(txtCedula.getText().equals("")){
                showMessageDialog(this, "Por favor ingrese un proveedor", "Atención", JOptionPane.WARNING_MESSAGE);
                txtCedula.requestFocus();
            }else{
                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
                        BuscarForm bf = new BuscarForm(null, true);
                        bf.columnas = "codigo, descripcion";
                        bf.tabla = "componentes";
                        bf.order = "";
                        bf.filtroBusqueda = "";
                        bf.setLocationRelativeTo(this);
                        bf.setVisible(true);
                        for(int c=0; c<modeloComponentes.getRowCount(); c ++){
                        if (modeloComponentes.getValueAt(c, 0).toString().equals(bf.retorno)){
                            k2 = c;
                          
                           //establecerBotones("Edicion");
                           datosActualesComponentes();
                        return;
                        }
                    }

                
              
                for(int c=0; c<modeloComponentes.getRowCount(); c ++){
                    if (modeloComponentes.getValueAt(c, 0).equals(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0))){
      
                        k2 = c;  
                         // establecerBotones("Edicion");
                       datosActualesComponentes();
                       return;
                    }

                }
                
            }

        }
    }
    }//GEN-LAST:event_tbDetalleVentaKeyPressed

    private void JCdepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCdepositoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdepositoActionPerformed

    private void txtPrefijoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrefijoVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrefijoVentaActionPerformed

    private void txtCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtCedula.getText())) {

                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7) as \"CI\", nombre||' '||apellido as \"Cliente\"";
                bf.tabla = "cliente";
                bf.order = "cliente_id";
                bf.filtroBusqueda = "estado!='INACTIVO'";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);

                for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(bf.retorno)){
                        establecerBotones("Edicion");
                        k = c;
                        datosActuales();
                        return;
                    }
                }

            }
        }
    }//GEN-LAST:event_txtCedulaKeyPressed

    private void jBGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardar1ActionPerformed
     try {
         guardar();
     } catch (Exception ex) {
         Logger.getLogger(NotaCreditoVentaForm.class.getName()).log(Level.SEVERE, null, ex);
     }
       
    }//GEN-LAST:event_jBGuardar1ActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void txtCantidadTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadTotalActionPerformed

    private void txtDescuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescuentoFocusLost
         if ("0".equals(txtDescuento.getText())){
            txtTotal.setText(txtSubTotal.getText());
        }else{
            int desc=Integer.parseInt(txtDescuento.getText().replace(".", ""));
            totaldesc = (Integer.parseInt(txtSubTotal.getText().replace(".", ""))-((Integer.parseInt(txtSubTotal.getText().replace(".", ""))*desc)/100));
            String totalDesc=formateador.format(totaldesc);
            txtTotal.setText(totalDesc);
            totaldesc=0;
        }
    }//GEN-LAST:event_txtDescuentoFocusLost

    private void txtDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyPressed

    }//GEN-LAST:event_txtDescuentoKeyPressed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbDetalleVentaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbDetalleVentaFocusLost
 
    }//GEN-LAST:event_tbDetalleVentaFocusLost

    private void txtNroNotaCreditoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoFocusLost

    }//GEN-LAST:event_txtNroNotaCreditoFocusLost

    private void txtFacturaReferenciadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaReferenciadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaReferenciadaActionPerformed

    private void jBsuspenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBsuspenderActionPerformed
        try {
            suspender();
        } catch (Exception ex) {
            Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBsuspenderActionPerformed

    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        tbDetalleVenta.removeAll();
        limpiarBusqueda();
        establecerBotones("Buscar");
        modoBusqueda(true);
        nuevoDetalle();
    }//GEN-LAST:event_bBuscarActionPerformed

    private void txtPrefijoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrefijoVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrefijoVentaKeyPressed

    private void txtPrefijoVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrefijoVentaKeyTyped
        char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
    }//GEN-LAST:event_txtPrefijoVentaKeyTyped

    private void txtNroNotaCreditoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoKeyTyped
       char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
    }//GEN-LAST:event_txtNroNotaCreditoKeyTyped

    private void txtNroNotaCreditoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoKeyPressed
        if (txtCedula.isEnabled() == true){
              return ;
        }  
         else{
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtNroNotaCredito.getText())) {
                //TBdetalleCuenta2.setRowSelectionInterval(0,0);
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "v.nro_factura";
                bf.tabla = "venta v";
                bf.order = "v.nro_factura";
                bf.filtroBusqueda = "es_factura = 'N' and estado = 'BORRADOR'"; //factura en suspension. Solo los que esten en estado Borrador
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                

                for(int c=0; c<modeloNroFactura.getRowCount(); c ++){
                    if (modeloNroFactura.getValueAt(c, 1).toString().equals(bf.retorno)){
                        modoBusqueda(false);
                        establecerBotones("Edicion");
                        k2 = c;
                        datosActualesNroFactura();
                       
                    return;
                    }
                }
                
            }
            getNroFactura();
            
            for(int c=0; c<modeloNroFactura.getRowCount(); c ++){
                if (modeloNroFactura.getValueAt(c, 1).toString().equals(txtNroNotaCredito.getText())){
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    k2 = c;                   
                    datosActualesNroFactura();
                   return;
                }
            }
        }
      }
    }//GEN-LAST:event_txtNroNotaCreditoKeyPressed

    private void tbDetalleVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDetalleVentaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbDetalleVentaMouseClicked

    private void txtDescuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyTyped
         char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
    }//GEN-LAST:event_txtDescuentoKeyTyped

    private void txtNroNotaCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroNotaCreditoActionPerformed

    private void txtFacturaReferenciadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaReferenciadaKeyPressed
     
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtFacturaReferenciada.getText())) {
                //TBdetalleCuenta2.setRowSelectionInterval(0,0);
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "v.nro_factura";
                bf.tabla = "venta v";
                bf.order = "v.nro_factura";
                bf.filtroBusqueda = "es_factura = 'S' and estado = 'PAGADO'"; //factura en suspension. Solo los que esten en estado Borrador
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                

                for(int c=0; c<modeloNroFactura.getRowCount(); c ++){
                    if (modeloNroFactura.getValueAt(c, 1).toString().equals(bf.retorno)){
                        modoBusqueda(false);
                        establecerBotones("Nuevo");
                        k2 = c;
                        datosActualesNroFacturaFactura();
                       
                    return;
                    }
                }
                
            }
            getNroFacturaPagadas();
       
            for(int c=0; c<modeloNroFactura.getRowCount(); c ++){
                if (modeloNroFactura.getValueAt(c, 1).toString().equals(txtNroNotaCredito.getText())){
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    k2 = c;                   
                    datosActualesNroFacturaFactura();
                   return;
                }
            }
        }
    }//GEN-LAST:event_txtFacturaReferenciadaKeyPressed
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox JCdeposito;
    private javax.swing.JPanel JpanelVenta;
    private org.edisoncor.gui.button.ButtonTask bBuscar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private org.edisoncor.gui.button.ButtonTask jBCancelar;
    private org.edisoncor.gui.button.ButtonTask jBGuardar1;
    private org.edisoncor.gui.button.ButtonTask jBsuspender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCantidadTotal;
    private javax.swing.JLabel labelCantidadTotal2;
    private javax.swing.JLabel labelCantidadTotal3;
    private javax.swing.JLabel labelCantidadTotal4;
    private javax.swing.JLabel labelDescuento;
    private javax.swing.JLabel labelSubTotal;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbDeposito;
    private javax.swing.JLabel lbFacturaReferenciada;
    private javax.swing.JLabel lbFechaOperacion;
    private javax.swing.JLabel lbNroNotaCredito;
    private javax.swing.JLabel lbPrefijo;
    private javax.swing.JTable tbDetalleVenta;
    private javax.swing.JFormattedTextField txtCantidadTotal;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JFormattedTextField txtDescuento;
    private javax.swing.JTextField txtFacturaReferenciada;
    private datechooser.beans.DateChooserCombo txtFechaVenta;
    private javax.swing.JFormattedTextField txtIva10;
    private javax.swing.JFormattedTextField txtIva5;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNroNotaCredito;
    private javax.swing.JTextField txtPrefijoVenta;
    private javax.swing.JFormattedTextField txtSubTotal;
    private javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
