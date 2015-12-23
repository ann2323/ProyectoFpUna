
package vista;

import controlador.DetallePagoControlador;
import controlador.FacturaCabeceraCompraControlador;
import controlador.FacturaPendienteControlador;
import controlador.ProveedorControlador;
import controlador.ReciboProveedorControlador;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;
import modelo.CabeceraRecibo;
import modelo.DetallePago;
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
    Integer total=0;
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloBusquedaFacturas = new DefaultTableModel();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    
    ProveedorControlador provC = new ProveedorControlador();
    ReciboProveedorControlador reciboControlador = new ReciboProveedorControlador();
    FacturaPendienteControlador facturaPendienteControl = new FacturaPendienteControlador();
    DetallePagoControlador detallePagoControl= new DetallePagoControlador();
    
    
    CabeceraRecibo reciboModelo = new CabeceraRecibo();
    DetallePago detallePagoModel = new DetallePago();
    
    FacturaCabeceraCompraControlador compraControlador = new FacturaCabeceraCompraControlador();
    public ReciboProveedorForm() throws Exception {
        initComponents();
        getProveedores();
        txtTotal.setEditable(false);
        txtpendiente.setVisible(false);
        txtcambio.setVisible(false);
        txtpagado.setVisible(false);
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
     
    
     private void getFacturasProveedor(Integer idProv) {
        try {
            modeloBusquedaFacturas.setColumnCount(0);
            modeloBusquedaFacturas.setRowCount(0);
           
            try (ResultSet rs = compraControlador.datosTablaBusquedaFacturasPendientes(idProv)) {
           
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

     
     private void limpiar() {
        //txtPrefijoVenta.setText("");
        modeloDetalleBusqueda= new DefaultTableModel();
        modeloDetalleBusqueda.setRowCount(0);
        tbVistaFacturasPendientes.setModel(modeloDetalleBusqueda);
        tbDetallePago.removeAll();
        txtFactura.setText("");
        txtProveedor.setText("");
        txtProveedor1.setText("");
        txtTotal.setText(""); 
        txtNroRecibo.setText("");
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
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bSuspender.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bSuspender.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bSuspender.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
        if ("".equals(txtNroRecibo.getText())) {
            showMessageDialog(null, "Debe ingresar un numero de recibo.", "Atención", INFORMATION_MESSAGE);
            txtNroRecibo.requestFocusInWindow();
            return;
        } else if ("".equals(txtProveedor.getText())) {
            showMessageDialog(null, "Debe ingresar un proveedor", "Atención", INFORMATION_MESSAGE);
            txtProveedor.requestFocusInWindow();
            return;
        } else if ("".equals(txtFactura.getText())) {
            showMessageDialog(null, "Debe seleccionar una factura pendiente", "Atención", INFORMATION_MESSAGE);
            txtFactura.requestFocusInWindow();
            return;
        }else{ 
            if(showConfirmDialog (null, "Está seguro de guardar el recibo?", "Confirmar", YES_NO_OPTION) == YES_OPTION){
            reciboModelo.setNroRecibo(Integer.parseInt(txtNroRecibo.getText()));
            reciboModelo.setFacturaNro(Integer.parseInt(txtFactura.getText().replace(".","").trim()));
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formato.parse(txtFecha.getText());
            reciboModelo.setFecha(date);
            reciboModelo.setTotalAPagar(Integer.parseInt(txtTotal.getText().replace(".", "").trim()));
            Integer provId = provC.devuelveId(txtProveedor.getText().replace(".","").trim());
            reciboModelo.setProveedorId(provId);
            Integer reciboId = reciboControlador.nuevoCodigo();
            reciboModelo.setReciboId(reciboId);
                int f=0;
                while (f<tbVistaFacturasPendientes.getRowCount()){
                    if(tbVistaFacturasPendientes.isRowSelected(f)) {
                        int f1=0, idFacturaPendiente=0; 
                        System.out.println("filas de fact pendiente "+f +"cantidad total "+tbVistaFacturasPendientes.getRowCount());
                        if (compraControlador.esContado(Integer.parseInt(txtFactura.getText().replace(".", ""))).equals("CONTADO")){
                            facturaPendienteControl.updateFacturaPendienteContado(Integer.parseInt(txtpagado.getText().replace(".", "").trim()), Integer.parseInt(txtpendiente.getText().replace(".", "").trim()), Integer.parseInt(txtcambio.getText().replace(".", "").trim()), reciboId, Integer.parseInt(txtFactura.getText().replace(".", "").trim()));
                            Integer idFactura = facturaPendienteControl.devuelveIdProvContado(Integer.parseInt(txtFactura.getText().replace(".", "").trim()),provId);
                            idFacturaPendiente=idFactura;
                        }else{
                            facturaPendienteControl.updateFacturaPendiente(Integer.parseInt(txtpagado.getText().replace(".", "").trim()), Integer.parseInt(txtpendiente.getText().replace(".", "").trim()), Integer.parseInt(txtcambio.getText().replace(".", "").trim()), reciboId,Integer.parseInt(txtFactura.getText().replace(".", "").trim()),tbVistaFacturasPendientes.getValueAt(f, 3).toString());
                            Integer idFactura = facturaPendienteControl.devuelveIdProv(Integer.parseInt(txtFactura.getText().replace(".", "").trim()), tbVistaFacturasPendientes.getValueAt(f, 3).toString(), provId);
                            idFacturaPendiente=idFactura;
                        }
                        
                        while(f1<tbDetallePago.getRowCount()){
                            System.out.println("filas de detalle pago "+f1 +"cantidad total "+tbDetallePago.getRowCount());
                            int detalleId=detallePagoControl.nuevaCodigo();
                            detallePagoModel.setDetallePagoId(detalleId);
                            detallePagoModel.setFacturaPendienteId(idFacturaPendiente);
                            detallePagoModel.setNombrePago(tbDetallePago.getValueAt(f1, 0).toString());
                            if (tbDetallePago.getValueAt(f1, 1).toString().isEmpty()){
                            detallePagoModel.setNroTarjetaCheque(null);
                            }else{
                            detallePagoModel.setNroTarjetaCheque(Integer.parseInt(tbDetallePago.getValueAt(f1, 1).toString()));
                            }
                            detallePagoModel.setMontoAbonado(Integer.parseInt(tbDetallePago.getValueAt(f1, 2).toString().replace(".", "").trim()));
                            if (tbDetallePago.getValueAt(f1, 3).toString().isEmpty()){
                            detallePagoModel.setMontoCredito(null);
                            }else{
                            detallePagoModel.setMontoCredito(Integer.parseInt(tbDetallePago.getValueAt(f1, 3).toString().replace(".", "").trim()));
                            }
                            if (tbDetallePago.getValueAt(f1, 4).toString().isEmpty()){
                            detallePagoModel.setPendienteAAplicar(null);
                            }else{
                            detallePagoModel.setPendienteAAplicar(Integer.parseInt(tbDetallePago.getValueAt(f1, 4).toString().replace(".", "").trim()));
                            }
                            if (tbDetallePago.getValueAt(f1, 4).toString().isEmpty()){
                            detallePagoModel.setNroNotaCredito(null);
                            }else{
                            detallePagoModel.setNroNotaCredito(Integer.parseInt(tbDetallePago.getValueAt(f1, 5).toString().trim()));
                            }
                            
                            if (bNuevo.isEnabled() == false){ 
                            detallePagoControl.insert(detallePagoModel);
                            f1++;
                            }
                        }
                        
                    }
                    f++;
                }
               if (bNuevo.isEnabled() == false){ 
                 reciboControlador.insert(reciboModelo);
               }  
               nuevo();
                
          }
  
       }
    }     
       
    
     public void datosActuales() throws Exception{
       if (bNuevo.isEnabled() == true) {
                   

           txtProveedor.setText(modeloBusqueda.getValueAt(k,0).toString());
                       
            txtProveedor1.setText(modeloBusqueda.getValueAt(k, 1).toString());
       }
       establecerBotones("Nuevo");
       Integer idProv = provC.devuelveId(txtProveedor.getText().replace(".","").trim());
       getFacturasProveedor(idProv);
    }
     
      private void datosActuales2(){
        txtFactura.setText(modeloBusquedaFacturas.getValueAt(k3, 1).toString());
        cargarFacturasPendientes(Integer.parseInt(txtFactura.getText().replace(".", "").trim()));
        datosActualesFacturaPendiente();
        establecerBotones("Nuevo");
    }
      
     private void datosActualesFacturaPendiente(){
       int f=0, c=0, total_apagar=0;
       int filas = modeloDetalleBusqueda.getRowCount();
       tbVistaFacturasPendientes.setModel(modeloDetalleBusqueda); 
     
       while(f<filas) //recorro las filas
       {
           
            tbVistaFacturasPendientes.setValueAt(modeloDetalleBusqueda.getValueAt(f,0),f,0);
            tbVistaFacturasPendientes.setValueAt(modeloDetalleBusqueda.getValueAt(f, 1),f,1);
            tbVistaFacturasPendientes.setValueAt(modeloDetalleBusqueda.getValueAt(f, 2),f, 2);
            tbVistaFacturasPendientes.setValueAt(modeloDetalleBusqueda.getValueAt(f, 3),f, 3);
            formateador = new DecimalFormat("###,###.##");
            String monto_total=formateador.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(f, 4).toString()));
            tbVistaFacturasPendientes.setValueAt(monto_total, f, 4);
            formateador = new DecimalFormat("###,###.##");
            String pendiente=formateador.format(Integer.parseInt(modeloDetalleBusqueda.getValueAt(f, 5).toString()));
            total_apagar=total_apagar+Integer.parseInt(modeloDetalleBusqueda.getValueAt(f, 5).toString());
            tbVistaFacturasPendientes.setValueAt(pendiente, f, 5);
            tbVistaFacturasPendientes.setValueAt(modeloDetalleBusqueda.getValueAt(f, 6), f, 6);              
           f++;
       }
       formateador = new DecimalFormat("###,###.##");
       String tot_apagar=formateador.format(total_apagar);
       txtTotal.setText(tot_apagar);
   }
     
    private void cargarFacturasPendientes(int factura) {
        tbVistaFacturasPendientes.removeAll();
        try {
            
            try (ResultSet rs = reciboControlador.getFacturasPendientes(factura)) {
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

                // tbVistaFacturasPendientes.setModel(modeloDetalleBusqueda);
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
        tbVistaFacturasPendientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        }   ;
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
        lbDatosGenerales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetallePago = new javax.swing.JTable();
        lbFecha1 = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JFormattedTextField();
        txtFecha = new datechooser.beans.DateChooserCombo();
        txtpendiente = new javax.swing.JFormattedTextField();
        txtpagado = new javax.swing.JFormattedTextField();
        txtcambio = new javax.swing.JFormattedTextField();
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

        jPanel1.setToolTipText("");
        jPanel1.setMinimumSize(new java.awt.Dimension(780, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(785, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 488, 85, -1));

        labelTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        labelTotal.setText("Total a pagar");
        jPanel1.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 488, 80, 20));

        tbVistaFacturasPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro. Prefijo", "Nro. Factura", "Fecha Vencimiento", "Plazo", "Total", "Monto Pendiente", "Estado"
            }
        ));
        tbVistaFacturasPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbVistaFacturasPendientesMouseClicked(evt);
            }
        });
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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 252, 1234, 220));
        jPanel1.add(txtProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 210, -1));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Proveedor");
        jPanel1.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 70, -1));

        txtNroRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroReciboActionPerformed(evt);
            }
        });
        txtNroRecibo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNroReciboKeyTyped(evt);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFacturaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFacturaKeyTyped(evt);
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
        jPanel2.add(bSuspender);

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

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1234, 80));

        lbDatosGenerales.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 11)); // NOI18N
        lbDatosGenerales.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)), "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 10), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.add(lbDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 82, 1234, 130));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Aharoni", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE CUOTAS PENDIENTES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(390, 390, 390))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 222, 1234, -1));

        tbDetallePago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbDetallePago);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 309, 0, 0));

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

        txtFecha.setFormat(2);
        txtFecha.setLocale(new java.util.Locale("es", "BO", ""));
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        txtpendiente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtpendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 488, 85, -1));

        txtpagado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtpagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 488, 85, -1));

        txtcambio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel1.add(txtcambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 523, 85, -1));

        jPanel5.setBackground(new java.awt.Color(51, 94, 137));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(101, 25));

        jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PAGO PROVEEDOR");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(400, 400, 400))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNroReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroReciboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroReciboActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbVistaFacturasPendientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesKeyPressed
            
    }//GEN-LAST:event_tbVistaFacturasPendientesKeyPressed

    private void bSuspenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSuspenderActionPerformed
      try {
            if (txtpagado.getText().equals("")){
              showMessageDialog(null, "Debe realizar el detalle de pago para generar el recibo", "Atención", INFORMATION_MESSAGE);
            }else{
               Integer nroFactura = Integer.parseInt(txtFactura.getText().replace(".", "").trim());
               if (compraControlador.esContado(Integer.parseInt(txtFactura.getText().replace(".", "").trim())).equals("CREDITO")){
                System.out.println("Entro en credito");
                guardar();
                Long cuotas = (long) compraControlador.getCuota(nroFactura);
                System.out.println("cuotas compra"+cuotas);
                Long cuotasCant = facturaPendienteControl.verificarEstadoFacturaPendientes(nroFactura);
                System.out.println("cuotas fact pendiente"+cuotasCant);
                    if (cuotas==cuotasCant){
                        System.out.println("cuotas pagadas total");
                        compraControlador.updateEstadoPagado(nroFactura);
                    } 
                }else{
                System.out.println("Entro en contado");
                guardar();
                compraControlador.updateEstadoPagado(nroFactura);
               
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }//GEN-LAST:event_bSuspenderActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void tbVistaFacturasPendientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesFocusLost
         
       
    }//GEN-LAST:event_tbVistaFacturasPendientesFocusLost

    private void txtFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyPressed
      if(txtNroRecibo.getText().equals("")) {
        showMessageDialog(null, "Debe primero el proveedor", "Atención", INFORMATION_MESSAGE);
      }else{
      
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtFactura.getText())) {
                Integer prov = 0;
                try {
                    prov = provC.devuelveId(txtProveedor.getText().replace(".","").trim());
                } catch (Exception ex) {
                    Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                BuscarForm bf = new BuscarForm(null, true);
                bf.columnas = "nro_prefijo as \"Nro Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro Factura\", to_char(vencimiento,'dd/mm/yyyy') as \"FechaVenc\", trim(to_char(cast(precio_total as integer),'9G999G999')) as \"Total\"";
                bf.tabla = "Compra";
                bf.order = "vencimiento DESC";
                bf.filtroBusqueda = "(estado='PENDIENTE' or estado='CONFIRMADO') and es_factura='S' and proveedor_id= "+ prov+ "";
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
            
            for(int c=0; c<modeloBusquedaFacturas.getRowCount(); c ++){
                    if (modeloBusquedaFacturas.getValueAt(c, 0).toString().equals(txtFactura.getText())){
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
           
        if (tbVistaFacturasPendientes.isRowSelected(0)||tbVistaFacturasPendientes.isRowSelected(1)||tbVistaFacturasPendientes.isRowSelected(2)){
        try {
            DetallePagoProveedorForm detallePago = new DetallePagoProveedorForm();
            MenuPrincipalForm.jDesktopPane1.add(detallePago);
            detallePago.toFront();
            detallePago.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
       }else{
            showMessageDialog(null, "Debe seleccionar al menos un factura pendiente para el pago", "Atención", INFORMATION_MESSAGE);
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
                bf.columnas = "trim(to_char(cast(ci as integer),'9G999G999')) as \"CI\", nombre||' '||apellido as \"Proveedor\"";
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
            
             for(int c=0; c<modeloBusqueda.getRowCount(); c ++){
                    if (modeloBusqueda.getValueAt(c, 0).toString().equals(txtProveedor.getText())){
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
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void txtFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaActionPerformed
       
    }//GEN-LAST:event_txtFacturaActionPerformed

    private void tbVistaFacturasPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesMouseClicked

    }//GEN-LAST:event_tbVistaFacturasPendientesMouseClicked

    private void txtFacturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaKeyReleased

    private void txtNroReciboKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroReciboKeyTyped
         char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
    }//GEN-LAST:event_txtNroReciboKeyTyped

    private void txtFacturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyTyped
         char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
    }//GEN-LAST:event_txtFacturaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bCancelar;
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
    public static javax.swing.JTable tbDetallePago;
    public static javax.swing.JTable tbVistaFacturasPendientes;
    private javax.swing.JFormattedTextField txtFactura;
    private datechooser.beans.DateChooserCombo txtFecha;
    private javax.swing.JTextField txtNroRecibo;
    public static javax.swing.JFormattedTextField txtProveedor;
    public static javax.swing.JTextField txtProveedor1;
    public static javax.swing.JFormattedTextField txtTotal;
    public static javax.swing.JFormattedTextField txtcambio;
    public static javax.swing.JFormattedTextField txtpagado;
    public static javax.swing.JFormattedTextField txtpendiente;
    // End of variables declaration//GEN-END:variables

       
}
