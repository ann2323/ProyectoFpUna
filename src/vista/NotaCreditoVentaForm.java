
package vista;

import controlador.ClienteControlador;
import controlador.ComponentesControlador;
import controlador.CuentaCabeceraControlador;
import controlador.DepositoControlador;
import controlador.DetalleCuentaControlador;
import controlador.DetalleFacturaVenta;
import controlador.FacturaCabeceraVentaControlador;
import controlador.PrefijoFacturaControlador;
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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
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

import javax.swing.table.DefaultTableModel;
import modelo.CuentaCabecera;
import modelo.Deposito;
import modelo.DetalleCuenta;
import modelo.DetalleVenta;
import modelo.Moneda;
import modelo.PrefijoFactura;
import modelo.Stock;
import modelo.Venta;
import static vista.FacturaVentaForm.getFechaActual;


/**
 *
 * @author Pathy
 */
public class NotaCreditoVentaForm extends javax.swing.JInternalFrame implements Printable{

    /**
     * Creates new form NotaCreditoVenta
     * @throws java.lang.Exception
     */
    public NotaCreditoVentaForm() throws Exception {
        initComponents();
        getClientes();
        getDepositosVector();
        getComponentes();
        comboPago.setSelectedIndex(-1);
        comboDeposito.setSelectedIndex(0);
        //comboCuota.setSelectedIndex(-1);  
        
    }
    
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        return formateador.format(ahora); 
    }
    
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloD = new DefaultTableModel();
    DefaultTableModel modeloComponente = new DefaultTableModel();
    DefaultTableModel modeloNroFactura;
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    
    Stock stock = new Stock();
    PrefijoFactura prefijo = new PrefijoFactura();
    private boolean esPrimero = true;
    Formatter formato = new Formatter();
    
     int contadorLote = 0;
     Integer subTotal= 0, totaldesc=0;
     Integer  cantProducto=0;
     int k, k2;
     double iva10=0.0, iva5=0.0; //variables que suman el iva al traer los componentes

    StockControlador stockCont = new StockControlador();
    PrefijoFacturaControlador prefijoControladorCredito= new PrefijoFacturaControlador();
    DepositoControlador depBD = new DepositoControlador();
    DetalleFacturaVenta facturaDetalleCont = new DetalleFacturaVenta();
    FacturaCabeceraVentaControlador ventaControlador = new  FacturaCabeceraVentaControlador();
    ClienteControlador cliC = new ClienteControlador();
    CuentaCabeceraControlador cuentaC = new CuentaCabeceraControlador();
    CuentaCabecera cuentaCabecera = new CuentaCabecera();
    DetalleCuenta cuentaDetalle = new DetalleCuenta();
    ComponentesControlador cmpCont = new ComponentesControlador();
    
    
    Deposito depModel = new  Deposito();
    Moneda monedaModel = new Moneda();
    DetalleVenta ventaD = new DetalleVenta();
    Venta ventaC = new Venta ();

    private void getMonedaVector() {
        comboTipoPago.removeAll();
        Vector<Moneda> ventaVec = new Vector<Moneda>();
        try {
          
            try (ResultSet rs = ventaControlador.datoCombo()) {
                while(rs.next()){
                    monedaModel=new Moneda();
                    monedaModel.setMonedaId(rs.getInt(1));
                    monedaModel.setNombre(rs.getString(2));           
                    ventaVec.add(monedaModel);
                }
                rs.close();
                                    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        DefaultComboBoxModel md1 = new DefaultComboBoxModel(ventaVec); 
        comboTipoPago.setModel(md1);
    }
    
    
    private void getDepositosVector() {
        comboDeposito.removeAll();
        Vector<Deposito> depVec = new Vector<Deposito>();
        try {
          
            try (ResultSet rs = depBD.datoComboFactura()) {
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
        comboDeposito.setModel(md1);
    }
     private void nuevo() {
        limpiar();
        establecerBotones("Nuevo");
        getNroFactura();
        getDepositosVector();
        txtFechaVenta.setText(getFechaActual());
      
        try {
            System.out.println(ventaControlador.verificarRegistroNCredito());
            if (ventaControlador.verificarRegistroNCredito()==0){
                try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", prefijoControladorCredito.primernroNotaC()).toString());
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //si hay registros pero el último nro de factura de la tabla venta es menor 
                // al inicio factura de la tabla prefijo se sigue la secuencia del nro factura
                //de la tabla venta
            }else if(ventaControlador.verificarRegistroNCredito()>0 && prefijoControladorCredito.primernroNotaC()< ventaControlador.ultimoNroNotaC()) {
                try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", ventaControlador.nroNotaC()).toString());
                    
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }  
                
                //si hay registros pero el último nro de factura de la tabla venta es mayor 
                // al inicio factura de la tabla prefijo significa que se agregó un nuevo inicio de factura
                //en la tabla prefijo y se debe reiniciar la secuencia
            }else if(ventaControlador.verificarRegistroNCredito()>0 && prefijoControladorCredito.primernroNotaC()> ventaControlador.ultimoNroNotaC()){
                 try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", prefijoControladorCredito.primernroNotaC()).toString());
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }
          } catch (Exception ex) {
            Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
      
     private void limpiarBusqueda(){
        tbDetalleVenta.removeAll();
        txtNroNotaCredito.setText("");
        txtPrefijoVenta.setText("");
        txtCliente.setText("");
        txtCliente1.setText("");
        txtFechaVenta.setText("");
        //omboCuota.removeAll();
        comboDeposito.removeAll();  
        txtCantidadTotal.setText("");
        txtDescuento.setText("");
        txtCantidadTotal.setText("");
        txtSubTotal.setText("");
        txtTotal.setText("");
        //nuevoDetalle();
     }
     
     
     private void limpiar() {
        //txtPrefijoVenta.setText("");
        comboDeposito.setSelectedIndex(0);
        //comboCuota.setSelectedIndex(-1);
        comboPago.setSelectedIndex(0);
        tbDetalleVenta.removeAll();
        txtNroNotaCredito.setText("");
        txtCliente.setText("");
        txtCliente1.setText("");
        txtFacturaReferenciada.setText("");
        //comboCuota.setEditable(false);
        comboPago.setSelectedIndex(0);
        txtCantidadTotal.setText("");
        txtDescuento.setText("");
        txtIva10.setText("");
        txtIva5.setText("");
        txtCantidadTotal.setText("");
        txtSubTotal.setText("");
        txtTotal.setText("");
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
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(true);
                bBuscar.setEnabled(false);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(true);
                bBuscar.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(false);
                 bBuscar.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(false);
                bBuscar.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
        if ("".equals(txtFechaVenta.getText())) {
            showMessageDialog(null, "Debe ingresar un una fecha.", "Atención", INFORMATION_MESSAGE);
            txtFechaVenta.requestFocusInWindow();
            return;
        } else if ("".equals(txtFechaVenta.getText())) {
            showMessageDialog(null, "Debe ingresar la fecha de recepcion", "Atención", INFORMATION_MESSAGE);
            txtFechaVenta.requestFocusInWindow();
            return;
        } else if ("".equals(txtCliente.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtCliente.requestFocusInWindow();
            return;
        }else if ("".equals(txtCliente1.getText())) {
            showMessageDialog(null, "Debe ingresar el cliente", "Atención", INFORMATION_MESSAGE);
            txtCliente1.requestFocusInWindow();
            return;
        }else if ("".equals(txtNroNotaCredito.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de nota de crédito", "Atención", INFORMATION_MESSAGE);
            txtNroNotaCredito.requestFocusInWindow();
            return;
        }else if ("".equals(txtPrefijoVenta.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de prefijo", "Atención", INFORMATION_MESSAGE);
            txtPrefijoVenta.requestFocusInWindow();
            return;
        }else if (comboDeposito.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el deposito", "Atención", INFORMATION_MESSAGE);
            comboDeposito.requestFocusInWindow();
            return;
        } else if (comboPago.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el pago Contado/Credito", "Atención", INFORMATION_MESSAGE);
            comboDeposito.requestFocusInWindow();
            return;
        }
        else {
           if(showConfirmDialog (null, "Está seguro de guardar la nota de crédito?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            int id= ventaControlador.nuevoCodigo(); 
            System.out.println("EL ID ES"+id);
            ventaC.setVentaId(id);
            ventaC.setNroPrefijo(txtPrefijoVenta.getText());
            ventaC.setNroFactura(Integer.parseInt(txtNroNotaCredito.getText()));
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formateador.parse(txtFechaVenta.getText());
            ventaC.setFecha(date);
            ventaC.setEsFactura('N');
            ventaC.setEstado("BORRADOR");
            int idCliente = cliC.devuelveId(txtCliente.getText());
            ventaC.setClienteId(idCliente);
            Moneda moneda = (Moneda) this.comboTipoPago.getSelectedItem();
            ventaC.setMonedaId(moneda.getMonedaId());
            Deposito dep = (Deposito) this.comboDeposito.getSelectedItem();
            ventaC.setCodDeposito(dep.getCodigo());
            ventaC.setPagoContado(comboPago.getSelectedItem().toString());
            ventaC.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText()));
            Date ahora = new Date();
            ventaC.setVencimiento(ahora);
           
           cuentaC.updateSaldoVenta(Integer.parseInt(txtFacturaReferenciada.getText()), idCliente, Integer.parseInt(txtTotal.getText().replace(".", "")));
            if(!txtDescuento.getText().equals("")){
                ventaC.setDescuento(Integer.parseInt(txtDescuento.getText())); 
            }
                   
            ventaC.setPrecioTotal(Integer.parseInt(txtTotal.getText().replace(".", "")));
            
           
            //if (bNuevo.isEnabled() == false) {
                    try {
                    int i = 0;
                    try {
                        int venta_id = ventaC.getVentaId();                       
                        while (!"".equals(tbDetalleVenta.getValueAt(i, 0).toString())){
                            ventaD.setVentaId(venta_id);
                            ventaD.setDetalleFacturaId(facturaDetalleCont.nuevaLinea());
                            ventaD.setCodigo(tbDetalleVenta.getValueAt(i, 0).toString());
                            ventaD.setDescripcion(tbDetalleVenta.getValueAt(i, 1).toString()); 
                          try {
                             ventaD.setPrecioUnitario(Integer.parseInt(tbDetalleVenta.getValueAt(i, 2).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                              System.out.println("Problema");
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
              
                            if (stockCont.tieneCodStock(tbDetalleVenta.getValueAt(i, 0).toString(),dep.getCodigo()) == 0){
                                
                                stock.setCantidad(Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString().trim()));
                                stock.setCodComponente(tbDetalleVenta.getValueAt(i, 0).toString());
                                stock.setCodDeposito(dep.getCodigo());
                                stock.setLine(stockCont.nuevoCodigo());
                                
                                stockCont.insert(stock);
                            } else {
                                if(stockCont.tieneStock(tbDetalleVenta.getValueAt(i, 0).toString(), dep.getCodigo()) < Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString()) ){
                                    showMessageDialog(null, "No hay stock", "Atención", INFORMATION_MESSAGE);
                                    return;        
                                }
                                stockCont.update2(tbDetalleVenta.getValueAt(i, 0).toString(), dep.getCodigo(), Integer.parseInt(tbDetalleVenta.getValueAt(i, 3).toString().trim()));
            
                            }
                            
                            if (bNuevo.isEnabled() == false){ 
                                System.out.println("Entro en el insert de detalle");
                                facturaDetalleCont.insert(ventaD);
                                i++;
                            }else{
                                try {
                                    System.out.println("Entro en el update de detalle");
                                    facturaDetalleCont.update(ventaD);
                                    //showMessageDialog(null, "Detalle actualizado correctamente");
                                    limpiar();
                                }catch(Exception ex){
                                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);   
                                }        
                            }
                        }
                    } catch (Exception ex) {
                       showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                        //Guardar.requestFocusInWindow();
                        return;
                    }
                   
                   if (bNuevo.isEnabled() == false){ 
                       System.out.println("Entro en el insert de cabecera");
                        ventaControlador.insert(ventaC);
                        txtNroNotaCredito.setText("");
                        nuevo();
                   }else{
                        try {
                            System.out.println("Entro en el update de cabecera");
                            ventaControlador.update(ventaC);
                             limpiar();
                            nuevo();
                            //showMessageDialog(null, "Venta actualizada correctamente");
                           
                        }catch(Exception ex){
                            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);   
                        }        
                    }
                    
                    //getEntradas();
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                    //bGuardar.requestFocusInWindow();
                    return;
                }
        }
           
     }
    }
    
    private void nuevoDetalle() {
       tbDetalleVenta.removeAll();
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
            modoBusqueda(false);
        }
        txtFechaVenta.setText(getFechaActual());
            try {
            System.out.println(ventaControlador.verificarRegistroNCredito());
            if (ventaControlador.verificarRegistroNCredito()==0){
                try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", prefijoControladorCredito.primernroNotaC()).toString());
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //si hay registros pero el último nro de factura de la tabla venta es menor 
                // al inicio factura de la tabla prefijo se sigue la secuencia del nro factura
                //de la tabla venta
            }else if(ventaControlador.verificarRegistroNCredito()>0 && prefijoControladorCredito.primernroNotaC()< ventaControlador.ultimoNroNotaC()) {
                try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", ventaControlador.nroNotaC()).toString());
                    
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }  
                
                //si hay registros pero el último nro de factura de la tabla venta es mayor 
                // al inicio factura de la tabla prefijo significa que se agregó un nuevo inicio de factura
                //en la tabla prefijo y se debe reiniciar la secuencia
            }else if(ventaControlador.verificarRegistroNCredito()>0 && prefijoControladorCredito.primernroNotaC()> ventaControlador.ultimoNroNotaC()){
                 try {
                    formato = new Formatter();
                    txtNroNotaCredito.setText(formato.format("%07d", prefijoControladorCredito.primernroNotaC()).toString());
                } catch (Exception ex) {
                    Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }
          } catch (Exception ex) {
            Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    
         
    }
    private void getComponentes() {
         modeloComponente = new DefaultTableModel();
        try {
            modeloComponente.setColumnCount(0);
            modeloComponente.setRowCount(0);
                      
            try (ResultSet rs = cmpCont.getComponentesDatos()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modeloComponente.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modeloComponente.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
    private void getClientes() {
        try {
            modeloBusqueda.setColumnCount(0);
            modeloBusqueda.setRowCount(0);
           
            try (ResultSet rs = cliC.datosBusqueda()) {
           
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
                txtCliente.setText(modeloBusqueda.getValueAt(k, 0).toString());
                txtCliente1.setText(modeloBusqueda.getValueAt(k, 1).toString());  
        }
       establecerBotones("Nuevo");
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
        comboTipoPago = new javax.swing.JComboBox();
        labelMoneda = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        txtIva10 = new javax.swing.JFormattedTextField();
        txtTotal = new javax.swing.JFormattedTextField();
        txtDescuento = new javax.swing.JFormattedTextField();
        txtCantidadTotal = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        comboDeposito = new javax.swing.JComboBox();
        labelCantidadTotal2 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        labelDescuento = new javax.swing.JLabel();
        labelSubTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetalleVenta = new javax.swing.JTable();
        txtCliente1 = new javax.swing.JTextField();
        lbCliente = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        txtPrefijoVenta = new javax.swing.JTextField();
        txtNroNotaCredito = new javax.swing.JTextField();
        txtFacturaReferenciada = new javax.swing.JTextField();
        labelPrefijoCompra = new javax.swing.JLabel();
        labelFacturaCompra = new javax.swing.JLabel();
        txtFechaVenta = new javax.swing.JTextField();
        labelFechaVenta = new javax.swing.JLabel();
        comboPago = new javax.swing.JComboBox();
        labelPago = new javax.swing.JLabel();
        labelCantidadTotal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bBuscar = new org.edisoncor.gui.button.ButtonTask();
        lbDatosGenerales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbIva5 = new javax.swing.JLabel();
        txtIva5 = new javax.swing.JTextField();
        lbCliente1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Factura Venta");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/factura_venta.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1200, 680));
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

        comboTipoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Efectivo", "Tarjeta" }));
        jPanel1.add(comboTipoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 180, 100, -1));

        labelMoneda.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelMoneda.setText("Tipo de Pago");
        jPanel1.add(labelMoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 80, 20));

        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jPanel1.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 500, 85, -1));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel1.setText("%");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 530, -1, -1));
        jPanel1.add(txtIva10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 500, 60, -1));

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 560, 85, -1));

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
        });
        jPanel1.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 530, 50, -1));

        txtCantidadTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtCantidadTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadTotalActionPerformed(evt);
            }
        });
        jPanel1.add(txtCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 500, 52, -1));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel4.setText("Depósito ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 120, 60, 20));

        comboDeposito.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NODO1", "NODO13", "NODO2" }));
        comboDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDepositoActionPerformed(evt);
            }
        });
        jPanel1.add(comboDeposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 120, -1, -1));

        labelCantidadTotal2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal2.setText("Iva 10%");
        jPanel1.add(labelCantidadTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 500, 47, 20));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total");
        jPanel1.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 560, 40, 20));

        labelDescuento.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelDescuento.setText("Descuento");
        jPanel1.add(labelDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 530, 60, 20));

        labelSubTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelSubTotal.setText("Sub Total");
        jPanel1.add(labelSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 500, 60, 20));

        tbDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código artículo", "Descripción", "Precio Unitario", "Cantidad", "Exento", "Subtotal"
            }
        ));
        tbDetalleVenta.getTableHeader().setReorderingAllowed(false);
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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 990, 220));
        jPanel1.add(txtCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 190, -1));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Factura referenciada ");
        jPanel1.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, 130, 20));

        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClienteKeyPressed(evt);
            }
        });
        jPanel1.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 80, -1));

        txtPrefijoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrefijoVentaActionPerformed(evt);
            }
        });
        jPanel1.add(txtPrefijoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 80, -1));

        txtNroNotaCredito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroNotaCreditoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNroNotaCreditoKeyReleased(evt);
            }
        });
        jPanel1.add(txtNroNotaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 80, -1));

        txtFacturaReferenciada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFacturaReferenciadaActionPerformed(evt);
            }
        });
        jPanel1.add(txtFacturaReferenciada, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 120, 100, -1));

        labelPrefijoCompra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelPrefijoCompra.setText("Nro. Prefijo");
        jPanel1.add(labelPrefijoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 76, -1));

        labelFacturaCompra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFacturaCompra.setText("Nro. Nota de Crédito ");
        jPanel1.add(labelFacturaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 120, 20));
        jPanel1.add(txtFechaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 93, -1));

        labelFechaVenta.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFechaVenta.setText("Fecha de Venta");
        labelFechaVenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(labelFechaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, -1, 20));

        comboPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CONTADO", "CREDITO" }));
        comboPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboPagoItemStateChanged(evt);
            }
        });
        comboPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPagoActionPerformed(evt);
            }
        });
        jPanel1.add(comboPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 100, -1));

        labelPago.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelPago.setText("Forma de Pago");
        jPanel1.add(labelPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 120, 20));

        labelCantidadTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal.setText("Cantidad Total");
        jPanel1.add(labelCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 90, 20));

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

        bGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        bGuardar.setText("Guardar");
        bGuardar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bGuardar.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        bGuardar.setDescription(" ");
        bGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarActionPerformed(evt);
            }
        });
        jPanel2.add(bGuardar);

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
        jPanel2.add(bBuscar);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, -1));

        lbDatosGenerales.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 11)); // NOI18N
        lbDatosGenerales.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)), "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.add(lbDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 990, 150));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Aharoni", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE PRODUCTOS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(447, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(375, 375, 375))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 990, 30));

        lbIva5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbIva5.setText("Iva 5%");
        jPanel1.add(lbIva5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 500, -1, 20));
        jPanel1.add(txtIva5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 500, 60, -1));

        lbCliente1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente1.setText("Cliente");
        jPanel1.add(lbCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 40, -1));

        jPanel5.setBackground(new java.awt.Color(51, 94, 137));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(101, 25));

        jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("NOTA DE CREDITO DE VENTA");

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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
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

    private void txtPrefijoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrefijoVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrefijoVentaActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
        getDepositosVector();
        getMonedaVector();
        getComponentes();
        txtPrefijoVenta.setEditable(false);
        //txtFacturaVenta.setEditable(false);
        //comboCuota.setEnabled(false);
        
        
       try {
            txtPrefijoVenta.setText(prefijoControladorCredito.prefijoNotaCredito());
            } catch (Exception ex) {
            Logger.getLogger(FacturaVentaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbDetalleVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetalleVentaKeyPressed
       //establecerBotones("Ed");
       
        if (tbDetalleVenta.getSelectedColumn()==0){
                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
                        BuscarForm bf = new BuscarForm(null, true);
                        bf.columnas = "codigo, descripcion";
                        bf.tabla = "componentes";
                        bf.order = "";
                        bf.filtroBusqueda = "";
                        bf.setLocationRelativeTo(this);
                        bf.setVisible(true);
                        for(int c=0; c<modeloComponente.getRowCount(); c++){
                        if (modeloComponente.getValueAt(c, 0).toString().equals(bf.retorno)){
                            k2 = c;
                           //establecerBotones("Edicion");
                           datosActualesComponente();
                        return;
                        }
                    }

                
              
                for(int c=0; c<modeloComponente.getRowCount(); c ++){
                    if (modeloComponente.getValueAt(c, 0).equals(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0))){
                        k2 = c;  
                        //establecerBotones("Edicion");
                       datosActualesComponente();
                       return;
                    }

                }
                
            }
        }
       
       
        if (!tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).equals("")&&!tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).equals("")){
        txtDescuento.setText("0");
        formateador = new DecimalFormat();
        int precio;
        //Guarda el codigo de componente para saber el tipo de iva que tiene
        String codigo = tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(),0).toString();
        precio = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
        int cantidad;
        cantidad = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim());
        int total=(precio*cantidad);
        String totalFormat=(formateador.format(total));

       if (tbDetalleVenta.getSelectedColumn()==5 && !(evt.getKeyCode() == KeyEvent.VK_DELETE)){
         cantProducto=cantProducto+cantidad;
         txtCantidadTotal.setText(Integer.toString(cantProducto));
         
           String tipoDeCliente = cliC.getExento(txtCliente.getText());
         //validacion de producto y cliente exento
           System.out.println("TIPO CLIENTE"+cliC.getExento(txtCliente.getText()));
         
         if(txtCliente.getText().equals("")){
             showMessageDialog(null, "Ingrese el número de cédula del cliente", "Atención", INFORMATION_MESSAGE);
             return;
         }
         
         //Aca pregunta si el tipoIva es 0 entonces tiene iva 10
         if(cmpCont.getTipoIva(codigo)==0 && tipoDeCliente.equals("Normal")){
             System.out.println("Entro en el normal");
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 5);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim());
             System.out.println(subTotal);
             String subTotalFormat=formateador.format(subTotal);
             System.out.println(subTotalFormat);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j =((Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "")))*10)/100;
             iva10= iva10+Math.round(j*10/10);
             String ivaFormat=formateador.format(iva10);
             txtIva10.setText(ivaFormat);
             ventaC.setIva10(Integer.parseInt(txtIva10.getText().toString().replace(".", "").trim()));
         
             //iva 10 cliente exento
         }else if(cmpCont.getTipoIva(codigo)==0 && tipoDeCliente.equals("Exento")){      
             double iva_10 = 0.0;
             formateador = new DecimalFormat("###,###.##");
             total=(precio*cantidad);
             double j = (double) total*10/100; //calculo el iva 10, si el total es 4000 j es 400
             iva_10= iva_10+Math.round(j*10/10);
             total = (int) (total - iva_10);
             totalFormat=(formateador.format(total));
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 4);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 4).toString().replace(".", "").trim());
             System.out.println(subTotal);
             String subTotalFormat=formateador.format(subTotal);
             System.out.println(subTotalFormat);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
              
             //si el tipo iva es 1 iva 5
         }else if (cmpCont.getTipoIva(codigo)==1 && tipoDeCliente.equals("Normal")){
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 5);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j =((Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 5).toString().replace(".", ""))) *5)/100;
             iva5=iva5+Math.round(j*10/10);
             String ivaFormat=formateador.format(iva5);
             txtIva5.setText(ivaFormat);
             ventaC.setIva5(Integer.parseInt(txtIva5.getText().toString().replace(".", "").trim()));
             
             //iva 5% cliente exento
         }else if(cmpCont.getTipoIva(codigo)==1 && tipoDeCliente.equals("Exento")){
             double iva_5 = 0.0;
             formateador = new DecimalFormat("###,###.##");
             total=(precio*cantidad);
             double j = (double) total*5/100; //calculo el iva 5, si el total es 4000 j es 
             iva_5= iva_5+Math.round(j*10/10);
             total = (int) (total - iva_5);
             totalFormat=(formateador.format(total));
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 4);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 4).toString().replace(".", "").trim());
             System.out.println(subTotal);
             String subTotalFormat=formateador.format(subTotal);
             System.out.println(subTotalFormat);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
    
                //si el producto es exento o cliente exento  
         }else{
             formateador = new DecimalFormat("###,###.##");
             tbDetalleVenta.setValueAt((totalFormat), tbDetalleVenta.getSelectedRow(), 4);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 4).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
            }
       }
    
       //metodo que elimina una fila
       if(evt.getKeyCode() == KeyEvent.VK_F9){
           if(tbDetalleVenta.getSelectedRow() == -1){
                showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
            }else{
                if(showConfirmDialog (null, "¿Desea eliminar esta fila?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
                    //System.out.println("Fila 1, columna 0 "+tbDetalleVenta.getValueAt(1, 2));
           
              
           //al borrar se debe restar el precio y la cantidad anterior
           int precio2 = 0;       
           precio2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
           int cantidad2 = 0;
           cantidad2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim());
           int total2 = 0;
           int iva102 = 0;
           int iva25 = 0;
           total2=(precio2*cantidad2);
           cantProducto=cantProducto-cantidad2;
           subTotal=subTotal-total2;
           
           
            if (cmpCont.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==0) {
               DecimalFormat formato = new DecimalFormat("###,###.##");
               iva102=(total2*10)/100;
               iva10=iva10-iva102;
               String ivaFormat=formato.format(iva10);
               txtIva10.setText(ivaFormat);
             } else if  (cmpCont.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==1)  {
                DecimalFormat formato = new DecimalFormat("###,###.##");
                iva25=(total2*5)/100;
                iva5=iva5-iva25;
                String ivaFormat=formato.format(iva5);
                txtIva5.setText(ivaFormat);
              }
            
               DecimalFormat formato = new DecimalFormat("###,###.##");
               String subTotalFormat=formato.format(subTotal);
               txtSubTotal.setText(subTotalFormat);
               txtTotal.setText(txtSubTotal.getText().trim()); 
               txtCantidadTotal.setText(Integer.toString(cantProducto)); 
               DefaultTableModel modeloActual = (DefaultTableModel) tbDetalleVenta.getModel();
               modeloActual.removeRow(tbDetalleVenta.getSelectedRow());
               //modeloD.removeRow(tbDetalleVenta.getSelectedRow());                    
               System.out.println("Fila 1, columna 0 "+tbDetalleVenta.getValueAt(0, 2));
               modeloActual.addRow(new Object[]{"","","","",""});
               //modeloD.addRow(new Object[]{"","","","",""});
               //tbDetalleVenta.setModel(modeloD);
               tbDetalleVenta.setModel(modeloActual);
               System.out.println("Cantidad de filas "+modeloD.getRowCount());
               tbDetalleVenta.setColumnSelectionInterval(0, 0);
               }
           }
       }
       
       //elimina el dato de una celda
         if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
           int precio2 = 0;
           precio2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 2).toString().replace(".", "").trim());
           int cantidad2 = 0;
           cantidad2 = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString().trim());
           int total2 = 0;
           total2=(precio2*cantidad2);
           System.out.println(total2);
           cantProducto=cantProducto-cantidad2;
            
            if (cmpCont.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==0) {
                 DecimalFormat FormatDeletIva10 = new DecimalFormat("###,###.##");
                 int iva102 = 0;
                  iva102=(total2*10)/100;
                 iva10=Integer.parseInt(txtIva10.getText().replace(".", "").trim())-iva102;
                 String ivaFormat=FormatDeletIva10.format(iva10);
                 txtIva10.setText(ivaFormat);
            } else if  (cmpCont.getTipoIva(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString())==1)  {
                 DecimalFormat FormatDeletIva5 = new DecimalFormat("###,###.##");
                 int iva25 = 0;
                 iva25=(total2*5)/100;
                 iva5=Integer.parseInt(txtIva5.getText().replace(".", "").trim())-iva25;
                 String ivaFormat=FormatDeletIva5.format(iva5);
                 txtIva5.setText(ivaFormat);
            }
            DecimalFormat FormatSubTotal = new DecimalFormat("###,###.##");
            System.out.println(subTotal);
            subTotal=subTotal-total2;
            System.out.println(subTotal);
            System.out.println(total2);
            String subTotalFormat=FormatSubTotal.format(subTotal);
            txtSubTotal.setText(subTotalFormat);
            txtTotal.setText(txtSubTotal.getText().trim()); 
            txtCantidadTotal.setText(Integer.toString(cantProducto));      
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 3);
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 5); 
            tbDetalleVenta.setValueAt("", tbDetalleVenta.getSelectedRow(), 4);
             System.out.println("SUBTOTAL "+subTotal);
             System.out.println("cantidad "+cantProducto);
             System.out.println("cantidad2 "+cantidad2);
             System.out.println("cantidad "+cantidad);
         }
        }
       
        /*Verifico si hay stock. Cuando esta en la columna exento recien se verifica
        //la columna anterior o sea la cantidad*/
       if(tbDetalleVenta.getSelectedColumn()==4) {
        Deposito dep = (Deposito) this.comboDeposito.getSelectedItem();
         int stockActual = 0;
         int cantidadIntroducida = 0;
       
        try {
           stockActual= stockCont.tieneStock(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString(), dep.getCodigo());
           cantidadIntroducida = Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString());
            System.out.println("STOCK ACTUAL "+stockActual + "\nCantidad introducida "+cantidadIntroducida);
            if(stockActual < cantidadIntroducida){
                showMessageDialog(null, "No hay stock", "Atención", INFORMATION_MESSAGE);
            }
       } catch (Exception ex) {
            Logger.getLogger(NotaCreditoVentaForm.class.getName()).log(Level.SEVERE, null, ex);
         }    
       }    
           
            /*if(stockCont.tieneStock(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 0).toString(), dep.getCodigo()) < Integer.parseInt(tbDetalleVenta.getValueAt(tbDetalleVenta.getSelectedRow(), 3).toString()) ){
                showMessageDialog(null, "No hay stock", "Atención", INFORMATION_MESSAGE);
                 tbDetalleVenta.setColumnSelectionInterval(3, 3);
                return;
            } */
         
     
    }//GEN-LAST:event_tbDetalleVentaKeyPressed

    private void txtDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyPressed

    }//GEN-LAST:event_txtDescuentoKeyPressed

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

    private void txtCantidadTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadTotalActionPerformed

    private void comboDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDepositoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboDepositoActionPerformed

    private void comboPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPagoActionPerformed

    private void comboPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboPagoItemStateChanged
        /*if(comboPago.getSelectedIndex() == 1){
            comboCuota.setEnabled(true);
        }else{
            comboCuota.setEnabled(false);
        }*/
    }//GEN-LAST:event_comboPagoItemStateChanged

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
       nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        try {
            guardar();
        } catch (Exception ex) {
            Logger.getLogger(NotaCreditoVentaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGuardarActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        cancelar();
        subTotal = 0;
        iva10 = 0.0;
        iva5 = 0.0;  
        cantProducto = 0;
        totaldesc = 0;
         /*if(comboPago.getSelectedIndex() == 1){
            comboCuota.setEnabled(true);
        }else{
            //comboCuota.setEnabled(false);
        }*/
        
    }//GEN-LAST:event_bCancelarActionPerformed

    private void tbDetalleVentaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbDetalleVentaFocusLost
         
       
    }//GEN-LAST:event_tbDetalleVentaFocusLost

    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        tbDetalleVenta.removeAll();
        limpiarBusqueda();
        establecerBotones("Buscar");
        modoBusqueda(true);
        nuevoDetalle();
    }//GEN-LAST:event_bBuscarActionPerformed

    private void txtNroNotaCreditoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoKeyPressed
         if (txtCliente.isEnabled() == true){
             System.out.println("Es true");
            return;
        }  
         else{
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtNroNotaCredito.getText())) {
                //TBdetalleCuenta2.setRowSelectionInterval(0,0);
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "v.nro_factura";
                bf.tabla = "venta v";
                bf.order = "v.nro_factura";
                bf.filtroBusqueda = "tipo_documento = 'N'";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                

                for(int c=0; c<modeloNroFactura.getRowCount(); c ++){
                    System.out.println("modelo row count "+modeloNroFactura.getRowCount());
                    if (modeloNroFactura.getValueAt(c, 1).toString().equals(bf.retorno)){
                        System.out.println("MODELO "+modeloNroFactura.getValueAt(c, 1).toString());
                        modoBusqueda(false);
                        establecerBotones("Edicion");
                        k2 = c;
                        datosActualesNroFactura();
                       
                    return;
                    }
                }
                
            }
            getNroFactura();
            
            System.out.println(modeloNroFactura.getRowCount());
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

    private void txtClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtCliente.getText())) {

                BuscarForm bf = new BuscarForm(null, true);
                bf.columnas = "cedula as \"CI\", nombre||' '||apellido as \"Cliente\"";
                bf.tabla = "Cliente";
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
    }//GEN-LAST:event_txtClienteKeyPressed

    private void txtNroNotaCreditoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroNotaCreditoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroNotaCreditoKeyReleased

    private void txtFacturaReferenciadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaReferenciadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaReferenciadaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bBuscar;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.JComboBox comboDeposito;
    private javax.swing.JComboBox comboPago;
    private javax.swing.JComboBox comboTipoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCantidadTotal;
    private javax.swing.JLabel labelCantidadTotal2;
    private javax.swing.JLabel labelDescuento;
    private javax.swing.JLabel labelFacturaCompra;
    private javax.swing.JLabel labelFechaVenta;
    private javax.swing.JLabel labelMoneda;
    private javax.swing.JLabel labelPago;
    private javax.swing.JLabel labelPrefijoCompra;
    private javax.swing.JLabel labelSubTotal;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbCliente1;
    private javax.swing.JLabel lbDatosGenerales;
    private javax.swing.JLabel lbIva5;
    private javax.swing.JTable tbDetalleVenta;
    private javax.swing.JFormattedTextField txtCantidadTotal;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCliente1;
    private javax.swing.JFormattedTextField txtDescuento;
    private javax.swing.JTextField txtFacturaReferenciada;
    private javax.swing.JTextField txtFechaVenta;
    private javax.swing.JFormattedTextField txtIva10;
    private javax.swing.JTextField txtIva5;
    private javax.swing.JTextField txtNroNotaCredito;
    private javax.swing.JTextField txtPrefijoVenta;
    private javax.swing.JFormattedTextField txtSubTotal;
    private javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    private void datosActualesComponente() {
      
       
           tbDetalleVenta.setValueAt(modeloComponente.getValueAt(k2, 0), tbDetalleVenta.getSelectedRow(), 0);
           tbDetalleVenta.setValueAt(modeloComponente.getValueAt(k2, 1), tbDetalleVenta.getSelectedRow(), 1);
           tbDetalleVenta.setValueAt(modeloComponente.getValueAt(k2, 2), tbDetalleVenta.getSelectedRow(), 2);
           
           tbDetalleVenta.setColumnSelectionInterval(2, 2);
       
    }   

    private void modoBusqueda(boolean v) {
        if (v == true) {
            txtNroNotaCredito.setEnabled(true);
            txtNroNotaCredito.requestFocusInWindow();
            txtNroNotaCredito.setBackground(Color.yellow);
            txtPrefijoVenta.setEnabled(false);
            txtCantidadTotal.setEnabled(false);
            txtCliente.setEnabled(false);
            txtTotal.setEnabled(false);
            txtCliente1.setEnabled(false);
            txtDescuento.setEnabled(false);
            txtFechaVenta.setEnabled(false);
            txtFacturaReferenciada.setEnabled(false);
            txtIva10.setEnabled(false);
            txtIva5.setEnabled(false);
            txtSubTotal.setEnabled(false);
            //comboCuota.setEnabled(false);
            comboPago.setEnabled(false);
            comboTipoPago.setEnabled(false);
            comboDeposito.setEnabled(false);
            tbDetalleVenta.setEnabled(true);
        
        } else {
            txtNroNotaCredito.setEnabled(true);
            txtNroNotaCredito.setBackground(Color.white);
            txtPrefijoVenta.setEnabled(true);
            txtPrefijoVenta.setEditable(false);
            txtFacturaReferenciada.setEnabled(true);
            txtCantidadTotal.setEnabled(true);
            txtCantidadTotal.setEditable(false);
            txtCliente.setEnabled(true);
            txtTotal.setEnabled(true);
            txtTotal.setEditable(false);
            txtCliente1.setEnabled(true);
            txtDescuento.setEnabled(true);
            txtDescuento.setEditable(true);
            txtFechaVenta.setEnabled(true);
            txtFechaVenta.setEditable(false);
            txtIva10.setEnabled(true);
            txtIva5.setEnabled(true);
            txtSubTotal.setEnabled(true);
            txtSubTotal.setEditable(false);
            //comboCuota.setEnabled(true);
            comboPago.setEnabled(true);
            comboTipoPago.setEnabled(true);
            comboDeposito.setEnabled(true);
            tbDetalleVenta.setEnabled(true);
        
        }
    }

    private void getNroFactura() {
        
        modeloNroFactura=new DefaultTableModel();
        try {
            modeloNroFactura.setColumnCount(0);
            modeloNroFactura.setRowCount(0);
                      
            try (ResultSet rs = ventaControlador.getNroNotaDeCredito()) {
           
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

    private void datosActualesNroFactura() {
              
            txtPrefijoVenta.setText(modeloNroFactura.getValueAt(k2, 0).toString());
            txtNroNotaCredito.setText(modeloNroFactura.getValueAt(k2, 1).toString());
            try {
                txtCliente.setText(cliC.getCedula(modeloNroFactura.getValueAt(k2, 2).toString()));
                txtCliente1.setText(cliC.getNombreCliente(modeloNroFactura.getValueAt(k2, 2).toString()));
                comboTipoPago.setSelectedItem(ventaControlador.getTipoPago(modeloNroFactura.getValueAt(k2, 5).toString()));
                comboDeposito.setSelectedItem(depBD.getDeposito(modeloNroFactura.getValueAt(k2, 6).toString()));
            
            } catch (Exception ex) {
                Logger.getLogger(NotaCreditoVentaForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 13).toString()) == 0){
                comboCuota.setEnabled(false);
            }else{
                comboCuota.setSelectedItem(modeloNroFactura.getValueAt(k2, 13).toString());
            }*/
            
            txtFechaVenta.setText(modeloNroFactura.getValueAt(k2, 3).toString());
            comboPago.setSelectedItem(modeloNroFactura.getValueAt(k2, 4).toString());
            txtCantidadTotal.setText(modeloNroFactura.getValueAt(k2, 7).toString());
            cantProducto = Integer.parseInt(modeloNroFactura.getValueAt(k2, 7).toString());
            txtTotal.setText(modeloNroFactura.getValueAt(k2, 8).toString());
            txtDescuento.setText(modeloNroFactura.getValueAt(k2, 9).toString());
            txtFacturaReferenciada.setText(modeloNroFactura.getValueAt(k2, 13).toString());
            int total = 0;
            total = Integer.parseInt(modeloNroFactura.getValueAt(k2, 8).toString());
            int descuento = 0;
            descuento= Integer.parseInt(modeloNroFactura.getValueAt(k2, 9).toString());
            int subtotal = 0;
            subtotal = total - descuento;
            //seteo el subTotal para que acumule en la búsqueda
            subTotal = subtotal;
            txtSubTotal.setText(String.valueOf(subtotal));
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 11).toString()) == 0){
                txtIva10.setText("");
                iva10 = 0.0;
            }else{
                txtIva10.setText(modeloNroFactura.getValueAt(k2, 11).toString());
                iva10 = Double.valueOf(txtIva10.getText());
            }
            if(Integer.parseInt(modeloNroFactura.getValueAt(k2, 12).toString())== 0){
                txtIva5.setText("");
                iva5 = 0.0;
            }else{
                txtIva5.setText(modeloNroFactura.getValueAt(k2, 12).toString());
                iva5 = Double.valueOf(txtIva5.getText());
            }    
           
            cargarDetalleFactura(Integer.parseInt(modeloNroFactura.getValueAt(k2, 10).toString())); 
    }

    private void cargarDetalleFactura(int idventa) {
        try {
            //tbDetalleVenta.setModel(modeloDetalleBusqueda);
            modeloDetalleBusqueda.setColumnCount(0);
            modeloDetalleBusqueda.setRowCount(0);
     
            try (ResultSet rs = facturaDetalleCont.getDetalle(idventa)) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
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
                
                   for (int i = 0; i < 11; i++) {
                        modeloDetalleBusqueda.addRow(new Object[]{"","","","","",""});
                    }
                   
                     tbDetalleVenta.setModel(modeloDetalleBusqueda);
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException  {
        if (pageIndex == 0)
        {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            this.printAll(graphics);
            return PAGE_EXISTS;
        }
        else
            return NO_SUCH_PAGE;        
    }  
        
        
    
}
