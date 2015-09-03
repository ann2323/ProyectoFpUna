/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.CabeceraPagoCompraControlador;
import controlador.ComponentesControlador;
import controlador.DepositoControlador;
import controlador.DetalleFacturaCompra;
import controlador.DetallePagoControlador;
import controlador.FacturaCabeceraCompraControlador;
import controlador.ProveedorControlador;
import controlador.ProyectoControlador;
import controlador.SaldoCompraControlador;
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
import modelo.Compra;
import modelo.Deposito;
import modelo.DetalleCompra;
import modelo.Proyectos;
import modelo.SaldoCompra;
import modelo.Stock;

/**
 *
 * @author anex
 */
public class FacturaCompraForm extends javax.swing.JInternalFrame {

 public FacturaCompraForm() throws Exception {
        initComponents();
       
       
        JCpago.setSelectedIndex(-1);
        JCdeposito.setSelectedIndex(0);
        JCpagoEn.setSelectedIndex(-1);
        JCproyecto1.setSelectedIndex(-1); 
        tbDetallePagoCompra.setVisible(true);
         
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
    
 
    Stock stock = new Stock();
    
     Integer subTotal= 0, totaldesc=0;
     double iva10=0.0, iva5=0.0;
     Integer  cantProducto=0;
     int k, k2;
     DecimalFormat formateador = new DecimalFormat("###,###.##");
     
    StockControlador stockCont = new StockControlador();
    DepositoControlador depBD = new DepositoControlador();
    SaldoCompraControlador saldoC = new SaldoCompraControlador();
    DetalleFacturaCompra facturaDetalleCont = new DetalleFacturaCompra();
    DetallePagoControlador pagoDetalleControlador = new DetallePagoControlador();
    CabeceraPagoCompraControlador pagoCabeceraControlador = new CabeceraPagoCompraControlador();
    FacturaCabeceraCompraControlador compraControlador = new  FacturaCabeceraCompraControlador();
    ProveedorControlador provC = new ProveedorControlador();
    ComponentesControlador componentesControl = new ComponentesControlador();
    
    Deposito depModel = new  Deposito();
    SaldoCompra saldoModel = new SaldoCompra();
    DetalleCompra compraD = new DetalleCompra();
    Proyectos proyecto = new Proyectos();
    ProyectoControlador proControl = new ProyectoControlador ();
    Compra compraC = new Compra ();


   

  
    /*private void getDepositos() {
        try {
            try (ResultSet rs = depBD.datoCombo()) {
                
                modelCombo.removeAllElements();
                
                while (rs.next()) {
                    modelCombo.addElement(rs.getObject("dato").toString());
                }
            JCdeposito.setModel(modelCombo);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }*/
    private void getDepositosVector() {
        JCdeposito.removeAll();
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
        JCdeposito.setModel(md1);
    }
     private void nuevo() {
        
        limpiar();
        establecerBotones("Nuevo");
        JCpago.setSelectedItem(true);
        txtFechaRecepcion.setText(getFechaActual());
        txtFechaRecepcion.setEnabled(false);
        nuevoDetalle();
        getProveedores();
        getProyectoVector();
        getDepositosVector();
        getComponentes();
        
        
    }
      private void limpiar() {
        txtPrefijoCompra.setText("");
        JCdeposito.setSelectedIndex(0);
        JCpagoEn.setSelectedIndex(-1);
        JCpago.setSelectedIndex(0);
        tbDetalleCompra.removeAll();
        txtFacturaCompra.setText("");
        txtProveedor.setText("");
        txtProveedor1.setText("");
        JCpagoEn.setEditable(false);
        JCpago.setSelectedIndex(0);
        JCproyecto1.setSelectedIndex(0);
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
                jBGuardar1.setEnabled(true);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                jBCancelar.setEnabled(false);
                jBGuardar1.setEnabled(true);
               
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                jBCancelar.setEnabled(false);
                jBGuardar1.setEnabled(false);
               
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                jBCancelar.setEnabled(true);
                jBGuardar1.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
        
        if ("".equals(txtFechaCompra.getText())) {
            showMessageDialog(null, "Debe ingresar un una fecha.", "Atención", INFORMATION_MESSAGE);
            txtFechaCompra.requestFocusInWindow();
            return;
        } else if ("".equals(txtFechaRecepcion.getText())) {
            showMessageDialog(null, "Debe ingresar la fecha de recepcion", "Atención", INFORMATION_MESSAGE);
            txtFechaRecepcion.requestFocusInWindow();
            return;
        } else if ("".equals(txtProveedor.getText())) {
            showMessageDialog(null, "Debe ingresar el proveedor", "Atención", INFORMATION_MESSAGE);
            txtFechaRecepcion.requestFocusInWindow();
            return;
        }else if ("".equals(txtProveedor1.getText())) {
            showMessageDialog(null, "Debe ingresar el proveedor", "Atención", INFORMATION_MESSAGE);
            txtFechaRecepcion.requestFocusInWindow();
            return;
        }else if ("".equals(txtFacturaCompra.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de factura", "Atención", INFORMATION_MESSAGE);
            txtFechaRecepcion.requestFocusInWindow();
            return;
        }else if ("".equals(txtPrefijoCompra.getText())) {
            showMessageDialog(null, "Debe ingresar algun nro de prefijo", "Atención", INFORMATION_MESSAGE);
            txtFechaRecepcion.requestFocusInWindow();
            return;
        }else if (JCdeposito.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el deposito", "Atención", INFORMATION_MESSAGE);
            JCdeposito.requestFocusInWindow();
            return;
        } else if (JCpago.getSelectedIndex() == -1) {
            showMessageDialog(null, "Debe seleccionar el pago Contado/Credito", "Atención", INFORMATION_MESSAGE);
            JCdeposito.requestFocusInWindow();
            return;
        }
        else {
           if(showConfirmDialog (null, "Está seguro de guardar la factura?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
            int id= compraControlador.nuevoCodigo(); 
            compraC.setCompraId(id);
            compraC.setNroPrefijo(txtPrefijoCompra.getText());
            compraC.setNroFactura(Integer.parseInt(txtFacturaCompra.getText()));
            Date ahora = new Date();
            compraC.setFechaRecepcion(ahora);
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formato.parse(txtFechaCompra.getText());
            compraC.setFecha(date);
            compraC.setEsFactura('S');
            compraC.setEstado("PAGADO");
            int idProveedor = provC.devuelveId(txtProveedor.getText().replace(".", ""));
            compraC.setProveedorId(idProveedor);
            Deposito dep = (Deposito) this.JCdeposito.getSelectedItem();
            compraC.setCodDeposito(dep.getCodigo());
            compraC.setPagoContado(JCpago.getSelectedItem().toString());
            int idSaldo = saldoC.nuevoCodigo();
            saldoModel.setSaldoCompraId(idSaldo);
            saldoModel.setEstado("PENDIENTE");
            saldoModel.setPrefijo(Integer.parseInt(txtPrefijoCompra.getText()));
            saldoModel.setNumero((txtFacturaCompra.getText()));
            saldoModel.setEsFactura("S");
            saldoModel.setSaldo(Integer.parseInt(txtTotal.getText().replace(".", "")));
            if ("CREDITO".equals(JCpago.getSelectedItem().toString())){
            compraC.setPagoEn(Integer.parseInt(JCpagoEn.getSelectedItem().toString()));  
            String dateVenc = (compraControlador.Vencimiento(compraC.getFecha(), compraC.getPagoEn()));  
            compraC.setEstado("PENDIENTE");
            compraC.setVencimiento(formato.parse(dateVenc)); 
            }
            
            compraC.setVencimiento(date); 
            
            compraC.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText()));
        
            compraC.setDescuento(Integer.parseInt(txtDescuento.getText()));
          
            compraC.setPrecioTotal(Integer.parseInt(txtTotal.getText().replace(".", "")));
            
            Proyectos pro = (Proyectos) this.JCproyecto1.getSelectedItem();
            compraC.setProyectoId(pro.getId());
           
            if (bNuevo.isEnabled() == false) {
                    try {
                    int i = 0;
                    try {
                        int compra_id = compraC.getCompraId(); 
                        while (!"".equals(tbDetalleCompra.getValueAt(i, 0).toString())){
                            compraD.setCompraId(compra_id);
                            compraD.setDetalleCompraId(facturaDetalleCont.nuevaLinea());
                            compraD.setCodigo(tbDetalleCompra.getValueAt(i, 0).toString());
                            compraD.setDescripcion(tbDetalleCompra.getValueAt(i, 1).toString()); 
                          try {
                             compraD.setPrecioUnit(Integer.parseInt(tbDetalleCompra.getValueAt(i, 2).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                              System.out.println("not a number"); 
                            }
                          try {
                              compraD.setCantidad(Integer.parseInt(tbDetalleCompra.getValueAt(i, 3).toString().trim()));
                            } catch (NumberFormatException e) {
                               compraD.setCantidad(Integer.parseInt(tbDetalleCompra.getValueAt(i, 3).toString()+"0"));
                            }
                            try {
                                compraD.setExentas(Integer.parseInt(tbDetalleCompra.getValueAt(i, 4).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                                compraD.setExentas(Integer.parseInt(tbDetalleCompra.getValueAt(i, 4).toString()+"0"));
                            } 
                              try {
                                compraD.setSubTotal(Integer.parseInt(tbDetalleCompra.getValueAt(i, 5).toString().replace(".", "").trim()));
                            } catch (NumberFormatException e) {
                                compraD.setSubTotal(Integer.parseInt(tbDetalleCompra.getValueAt(i, 5).toString()+"0"));
                            }  
                           
                            if (stockCont.tieneCodStock(tbDetalleCompra.getValueAt(i, 0).toString(),dep.getCodigo()) == 0){
                                showMessageDialog(null, "No se encuentra en stock el codigo solicitado", "Atención", INFORMATION_MESSAGE);
                            } else {
                                try {
                                    stockCont.update(tbDetalleCompra.getValueAt(i, 0).toString(), dep.getCodigo(), Integer.parseInt(tbDetalleCompra.getValueAt(i, 3).toString().trim()));
                                 } catch (Exception ex) {
                                    stockCont.update(tbDetalleCompra.getValueAt(i, 0).toString(), dep.getCodigo(), Integer.parseInt(tbDetalleCompra.getValueAt(i, 3).toString() + "0"));
                                   }
                            }
                            facturaDetalleCont.insert(compraD);
                            i++;
                        }
                    
                    } catch (Exception ex) {
                     
                       showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                        //Guardar.requestFocusInWindow();
                        return;
                    }
                    compraControlador.cambiarAEnProceso(pro.getCodigo());
                    saldoC.insert(saldoModel);
                    compraControlador.insert(compraC);
                    nuevo();
                    //getEntradas();
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Atención", INFORMATION_MESSAGE);
                    //bGuardar.requestFocusInWindow();
                    return;
                }
            } 
        }
    txtFacturaCompra.setText("");
    txtFechaCompra.setText("");
     }
    }
    private void datosActualesComponentes(){
           if (bNuevo.isEnabled() == true) {
           tbDetalleCompra.setValueAt(modeloComponentes.getValueAt(k2, 0), tbDetalleCompra.getSelectedRow(), 0);
           tbDetalleCompra.setValueAt(modeloComponentes.getValueAt(k2, 1), tbDetalleCompra.getSelectedRow(), 1);
           tbDetalleCompra.setValueAt(modeloComponentes.getValueAt(k2, 2), tbDetalleCompra.getSelectedRow(), 2);
           tbDetalleCompra.setColumnSelectionInterval(2, 2);
    }}
 
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
       
                tbDetalleCompra.setModel(modeloD);
                tbDetalleCompra.setColumnSelectionAllowed(false);
                tbDetalleCompra.setRowSelectionAllowed(false);
                tbDetalleCompra.setCellSelectionEnabled(true);
    
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
           private void getProyectoVector() {
        JCproyecto1.removeAll();
        Vector<Proyectos> compVec = new Vector<Proyectos>();
        try {
           
            try (ResultSet rs = proControl.datoCombo()) {
               
                while(rs.next()){
                    proyecto=new Proyectos();
                    proyecto.setId(rs.getInt(1));
                    proyecto.setCodigo(rs.getString(2));           
                    compVec.add(proyecto);
                }
                rs.close();
                                    
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        DefaultComboBoxModel md1 = new DefaultComboBoxModel(compVec); 
        JCproyecto1.setModel(md1);
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
     private void datosActuales(){
             if (bNuevo.isEnabled() == true) {
            txtProveedor.setText(modeloBusqueda.getValueAt(k, 0).toString());
            txtProveedor1.setText(modeloBusqueda.getValueAt(k, 1).toString());  
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

        JpanelCompra = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        jBCancelar = new org.edisoncor.gui.button.ButtonTask();
        jBGuardar1 = new org.edisoncor.gui.button.ButtonTask();
        labelPrefijoCompra = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JTextField();
        txtPrefijoCompra = new javax.swing.JTextField();
        txtFacturaCompra = new javax.swing.JTextField();
        labelFechaRecepcion = new javax.swing.JLabel();
        txtProveedor1 = new javax.swing.JTextField();
        labelDias = new javax.swing.JLabel();
        JCpagoEn = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        labelPagoen = new javax.swing.JLabel();
        JCpago = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        JCdeposito = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        JCproyecto1 = new javax.swing.JComboBox();
        labelFechaCompra = new javax.swing.JLabel();
        labelFacturaCompra = new javax.swing.JLabel();
        labelProveedorNombre = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetalleCompra = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetallePagoCompra = new javax.swing.JTable();
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
        txtFechaCompra = new datechooser.beans.DateChooserCombo();
        txtFechaRecepcion = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Factura Compra");
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

        JpanelCompra.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(51, 94, 137));
        jLabel7.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("FACTURA DE COMPRA");
        jLabel7.setOpaque(true);
        JpanelCompra.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-9, 0, 950, 56));

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

        JpanelCompra.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 900, 55));

        labelPrefijoCompra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelPrefijoCompra.setText("Nro. Prefijo");
        JpanelCompra.add(labelPrefijoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 140, 76, -1));

        txtProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorActionPerformed(evt);
            }
        });
        txtProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorKeyPressed(evt);
            }
        });
        JpanelCompra.add(txtProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 200, 77, -1));

        txtPrefijoCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrefijoCompraActionPerformed(evt);
            }
        });
        JpanelCompra.add(txtPrefijoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 137, 57, -1));

        txtFacturaCompra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFacturaCompraFocusLost(evt);
            }
        });
        JpanelCompra.add(txtFacturaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 168, 77, -1));

        labelFechaRecepcion.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFechaRecepcion.setText("Fecha de Recepcion");
        labelFechaRecepcion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        JpanelCompra.add(labelFechaRecepcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 130, 40));
        JpanelCompra.add(txtProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 200, 260, -1));

        labelDias.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelDias.setText("Cuotas");
        JpanelCompra.add(labelDias, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, 42, -1));

        JCpagoEn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        JpanelCompra.add(JCpagoEn, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 200, 45, 20));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel2.setText("Pago");
        JpanelCompra.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, 35, -1));

        labelPagoen.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelPagoen.setText("Pago en:");
        JpanelCompra.add(labelPagoen, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 53, -1));

        JCpago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CONTADO", "CREDITO", " " }));
        JpanelCompra.add(JCpago, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 120, 20));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel3.setText("Proyecto");
        JpanelCompra.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 140, 52, -1));

        JCdeposito.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NODO1", "NODO13", "NODO2" }));
        JCdeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCdepositoActionPerformed(evt);
            }
        });
        JpanelCompra.add(JCdeposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, -1, 20));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel4.setText("Deposito ");
        JpanelCompra.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 60, 20));

        JCproyecto1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        JpanelCompra.add(JCproyecto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 140, 100, -1));

        labelFechaCompra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFechaCompra.setText("Fecha de Compra");
        labelFechaCompra.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        JpanelCompra.add(labelFechaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, -1, -1));

        labelFacturaCompra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelFacturaCompra.setText("Nro. Factura");
        JpanelCompra.add(labelFacturaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 171, 76, -1));

        labelProveedorNombre.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelProveedorNombre.setText("Proveedor");
        JpanelCompra.add(labelProveedorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 203, 76, -1));
        JpanelCompra.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, -1, -1));

        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10))); // NOI18N
        JpanelCompra.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 920, 120));

        tbDetalleCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbDetalleCompra.setEditingColumn(0);
        tbDetalleCompra.setFillsViewportHeight(true);
        tbDetalleCompra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tbDetalleCompraFocusLost(evt);
            }
        });
        tbDetalleCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDetalleCompraKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbDetalleCompra);

        JpanelCompra.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 920, 230));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tbDetallePagoCompra.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbDetallePagoCompra);

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("LISTA DE PRODUCTOS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(450, 450, 450)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(67, 67, 67)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        JpanelCompra.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 920, 30));
        JpanelCompra.add(labelCantidadTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 650, 200, -1));
        JpanelCompra.add(txtIva10, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 520, 87, -1));

        labelCantidadTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal.setText("Cantidad Total");
        JpanelCompra.add(labelCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 520, 90, -1));

        labelCantidadTotal3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal3.setText("Iva 10%");
        JpanelCompra.add(labelCantidadTotal3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 520, 47, -1));

        txtCantidadTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtCantidadTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadTotalActionPerformed(evt);
            }
        });
        JpanelCompra.add(txtCantidadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 520, 52, -1));

        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        JpanelCompra.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 520, 85, -1));

        labelSubTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelSubTotal.setText("Sub Total");
        JpanelCompra.add(labelSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 520, 60, 20));

        labelDescuento.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelDescuento.setText("Descuento");
        JpanelCompra.add(labelDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 550, 60, 20));

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
        JpanelCompra.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 550, 50, -1));

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel8.setText("%");
        JpanelCompra.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 550, 20, -1));

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        JpanelCompra.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 580, 85, -1));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total");
        JpanelCompra.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 580, 40, 20));
        JpanelCompra.add(txtIva5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, 87, -1));

        labelCantidadTotal4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelCantidadTotal4.setText("Iva 5%");
        JpanelCompra.add(labelCantidadTotal4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 520, 47, -1));

        txtFechaCompra.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    txtFechaCompra.setLocale(new java.util.Locale("es", "BO", ""));
    JpanelCompra.add(txtFechaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 140, -1));
    JpanelCompra.add(txtFechaRecepcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 140, -1));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(JpanelCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(JpanelCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDetalleCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetalleCompraKeyPressed
        establecerBotones("Nuevo");
    /* if (tbDetalleCompra.getSelectedColumn()==1){
     try {
         if (stockCont.tieneCodStock(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0).toString(),dep.getCodigo()) == 0){
             showMessageDialog(null, "No se encuentra en stock el codigo solicitado", "Atención", INFORMATION_MESSAGE);
              
         }
     } catch (Exception ex) {
         Logger.getLogger(FacturaCompraForm.class.getName()).log(Level.SEVERE, null, ex);
     }
     }*/
     
        
        if (!tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 2).equals("")&&!tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 3).equals("")){
        txtDescuento.setText("0");
        formateador = new DecimalFormat();
        
        Integer precio;
        String codigo = tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(),0).toString();
            
        precio = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 2).toString().replace(".", "").trim());
        
        Integer Cantidad;
        
        Cantidad = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 3).toString().trim());
        int total=(precio*Cantidad);
        String totalFormat=(formateador.format(total));

       if (tbDetalleCompra.getSelectedColumn()==5 && !(evt.getKeyCode() == KeyEvent.VK_DELETE)){
                          
               // tbDetalleCompra.setValueAt((total), tbDetalleCompra.getSelectedRow(), 4);
         cantProducto=cantProducto+Cantidad;
         txtCantidadTotal.setText(Integer.toString(cantProducto));      
         if(componentesControl.getTipoIva(codigo)==0){
             formateador = new DecimalFormat("###,###.##");
             tbDetalleCompra.setValueAt((totalFormat), tbDetalleCompra.getSelectedRow(), 5);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 5).toString().replace(".", "").trim());
             System.out.println(subTotal);
             String subTotalFormat=formateador.format(subTotal);
             System.out.println(subTotalFormat);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j = (Integer.parseInt(txtTotal.getText().toString().replace(".", ""))) - (Integer.parseInt((txtTotal.getText().toString().replace(".", "")))/1.1);
             Long l = Math.round(j);
             iva10=Integer.valueOf(l.intValue());
             String ivaFormat=formateador.format(iva10);
             txtIva10.setText(ivaFormat);
             
         }else if (componentesControl.getTipoIva(codigo)==1){
             formateador = new DecimalFormat("###,###.##");
             tbDetalleCompra.setValueAt((totalFormat), tbDetalleCompra.getSelectedRow(), 5);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 5).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             double j =(Integer.parseInt(txtTotal.getText().toString().replace(".", ""))) - (Integer.parseInt((txtTotal.getText().toString().replace(".", "")))/1.05);
             Long l = Math.round(j);
             iva5=Integer.valueOf(l.intValue());
             String ivaFormat=formateador.format(iva5);
             txtIva5.setText(ivaFormat);
         }else{
             formateador = new DecimalFormat("###,###.##");
             tbDetalleCompra.setValueAt((totalFormat), tbDetalleCompra.getSelectedRow(), 4);      
             subTotal=subTotal+ Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 4).toString().replace(".", "").trim());
             String subTotalFormat=formateador.format(subTotal);
             txtSubTotal.setText(subTotalFormat);
             txtTotal.setText(txtSubTotal.getText().trim());
             
            }
       }
    
       if(evt.getKeyCode() == KeyEvent.VK_F9){
           if(tbDetalleCompra.getSelectedRow() == -1){
                showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
            }else{
                if(showConfirmDialog (null, "¿Desea eliminar esta fila?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
                    System.out.println("Fila 1, columna 0 "+tbDetalleCompra.getValueAt(1, 2));
           
           Integer precio2;
                    
           precio2 = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 2).toString().replace(".", "").trim());
     
           Integer Cantidad2;
      
           Cantidad2 = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 3).toString().trim());
          

            int total2=(precio2*Cantidad2);
            cantProducto=cantProducto-Cantidad2;
             
                    subTotal=subTotal-total2;
                    if (componentesControl.getTipoIva(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0).toString())==0) {
                    DecimalFormat formato = new DecimalFormat("###,###.##");
                    Double iva102=total2-total2/1.1;
                    Long l = Math.round(iva102);
                    iva10=iva10 - Integer.valueOf(l.intValue());
                    String ivaFormat=formato.format(iva10);
                    txtIva10.setText(ivaFormat);
                    } else if  (componentesControl.getTipoIva(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0).toString())==1)  {
                    DecimalFormat formato = new DecimalFormat("###,###.##");
                    Double iva25=total2-total2/1.05;
                    Long l = Math.round(iva25);
                    iva5=iva5- Integer.valueOf(l.intValue());
                    String ivaFormat=formato.format(iva5);
                    txtIva5.setText(ivaFormat);
                    }
                    DecimalFormat formato = new DecimalFormat("###,###.##");
                    String subTotalFormat=formato.format(subTotal);
                    txtSubTotal.setText(subTotalFormat);
                    txtTotal.setText(txtSubTotal.getText().trim()); 
                    txtCantidadTotal.setText(Integer.toString(cantProducto)); 
                    modeloD.removeRow(tbDetalleCompra.getSelectedRow());                    
                    System.out.println("Fila 1, columna 0 "+tbDetalleCompra.getValueAt(0, 2));
                    modeloD.addRow(new Object[]{"","","","",""});
                    tbDetalleCompra.setModel(modeloD);
                    System.out.println("Cantidad de filas "+modeloD.getRowCount());
                    tbDetalleCompra.setColumnSelectionInterval(0, 0);
                }
           }}
       
         if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
           Integer precio2;

           precio2 = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 2).toString().replace(".", "").trim());
     
           Integer Cantidad2;
      
           Cantidad2 = Integer.parseInt(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 3).toString().trim());
           
       
            
            int total2=(precio2*Cantidad2);
             System.out.println(total2);
           
            cantProducto=cantProducto-Cantidad2;
            
            if (componentesControl.getTipoIva(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0).toString())==0) {
                 DecimalFormat FormatDeletIva10 = new DecimalFormat("###,###.##");
                 double iva102=total2-total2/1.1;
                 Long L = Math.round(iva102);
                 iva10=Integer.parseInt(txtIva10.getText().replace(".", "").trim())- Integer.valueOf(L.intValue());
                 String ivaFormat=FormatDeletIva10.format(iva10);
                 txtIva10.setText(ivaFormat);
            } else if  (componentesControl.getTipoIva(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0).toString())==1)  {
                 DecimalFormat FormatDeletIva5 = new DecimalFormat("###,###.##");
                 Double iva25=total2-total2/1.05;
                 Long L = Math.round(iva25);
                 iva5=Integer.parseInt(txtIva5.getText().replace(".", "").trim())- Integer.valueOf(L.intValue());
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
            tbDetalleCompra.setValueAt("", tbDetalleCompra.getSelectedRow(), 3);
            tbDetalleCompra.setValueAt("", tbDetalleCompra.getSelectedRow(), 5); 
            tbDetalleCompra.setValueAt("", tbDetalleCompra.getSelectedRow(), 4);
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
        if (tbDetalleCompra.getSelectedColumn()==0){
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
                          
                           establecerBotones("Edicion");
                           datosActualesComponentes();
                        return;
                        }
                    }

                
              
                for(int c=0; c<modeloComponentes.getRowCount(); c ++){
                    if (modeloComponentes.getValueAt(c, 0).equals(tbDetalleCompra.getValueAt(tbDetalleCompra.getSelectedRow(), 0))){
      
                        k2 = c;  
                          establecerBotones("Edicion");
                       datosActualesComponentes();
                       return;
                    }

                }
                
            }
        }
        
     
    }//GEN-LAST:event_tbDetalleCompraKeyPressed

    private void JCdepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCdepositoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdepositoActionPerformed

    private void txtPrefijoCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrefijoCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrefijoCompraActionPerformed

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
                        datosActuales();
                        return;
                    }
                }

            }
        }
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void jBGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardar1ActionPerformed
     try {
         guardar();
     } catch (Exception ex) {
         Logger.getLogger(FacturaCompraForm.class.getName()).log(Level.SEVERE, null, ex);
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

    private void tbDetalleCompraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbDetalleCompraFocusLost
 
    }//GEN-LAST:event_tbDetalleCompraFocusLost

    private void txtFacturaCompraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFacturaCompraFocusLost

    }//GEN-LAST:event_txtFacturaCompraFocusLost

    private void txtProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorActionPerformed
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox JCdeposito;
    private javax.swing.JComboBox JCpago;
    private javax.swing.JComboBox JCpagoEn;
    private javax.swing.JComboBox JCproyecto1;
    private javax.swing.JPanel JpanelCompra;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private org.edisoncor.gui.button.ButtonTask jBCancelar;
    private org.edisoncor.gui.button.ButtonTask jBGuardar1;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCantidadTotal;
    private javax.swing.JLabel labelCantidadTotal2;
    private javax.swing.JLabel labelCantidadTotal3;
    private javax.swing.JLabel labelCantidadTotal4;
    private javax.swing.JLabel labelDescuento;
    private javax.swing.JLabel labelDias;
    private javax.swing.JLabel labelFacturaCompra;
    private javax.swing.JLabel labelFechaCompra;
    private javax.swing.JLabel labelFechaRecepcion;
    private javax.swing.JLabel labelPagoen;
    private javax.swing.JLabel labelPrefijoCompra;
    private javax.swing.JLabel labelProveedorNombre;
    private javax.swing.JLabel labelSubTotal;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tbDetalleCompra;
    public static javax.swing.JTable tbDetallePagoCompra;
    private javax.swing.JFormattedTextField txtCantidadTotal;
    private javax.swing.JFormattedTextField txtDescuento;
    private javax.swing.JTextField txtFacturaCompra;
    private datechooser.beans.DateChooserCombo txtFechaCompra;
    private javax.swing.JTextField txtFechaRecepcion;
    private javax.swing.JFormattedTextField txtIva10;
    private javax.swing.JFormattedTextField txtIva5;
    private javax.swing.JTextField txtPrefijoCompra;
    private javax.swing.JTextField txtProveedor;
    private javax.swing.JTextField txtProveedor1;
    private javax.swing.JFormattedTextField txtSubTotal;
    public static javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
