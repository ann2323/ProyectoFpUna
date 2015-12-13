
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
import java.sql.Timestamp;
import java.text.DateFormat;
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
import modelo.FacturaPendiente;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;


/**
 *
 * @author Pathy
 */
public class AnularPagoProveedorForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form facturaVenta
     * @throws java.lang.Exception
     */
    
    int k, k3;
    Integer total=0; String estado="";
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    DefaultTableModel modeloBusqueda = new DefaultTableModel();
    DefaultTableModel modeloBusquedaFacturas = new DefaultTableModel();
    DefaultTableModel modeloDetalleBusqueda = new DefaultTableModel();
    
    
    ProveedorControlador provC = new ProveedorControlador();
    ReciboProveedorControlador reciboControlador = new ReciboProveedorControlador();
    FacturaPendienteControlador facturaPendienteControl = new FacturaPendienteControlador();
    DetallePagoControlador detallePagoControl= new DetallePagoControlador();
    FacturaCabeceraCompraControlador compraC = new FacturaCabeceraCompraControlador();
    
    
    CabeceraRecibo reciboModelo = new CabeceraRecibo();
    DetallePago detallePagoModel = new DetallePago();
    FacturaPendiente factpendienteModel = new FacturaPendiente();
    

    public AnularPagoProveedorForm() throws Exception {
        initComponents();
        getProveedores();
        
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
     
    
     private void getReciboProveedor(Integer idProv) {
        try {
            modeloBusquedaFacturas.setColumnCount(0);
            modeloBusquedaFacturas.setRowCount(0);
           
            try (ResultSet rs = reciboControlador.recibosProveedor(idProv)) {
           
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
        txtProveedor.setText("");
        txtProveedor1.setText("");  
        txtNroRecib.setText("");
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
                bAnular.setEnabled(true);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bAnular.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bCancelar.setEnabled(false);
                bAnular.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bCancelar.setEnabled(true);
                bAnular.setEnabled(false);
                break;
        }
    }
  
        private void guardar() throws ParseException, Exception{
            if ("".equals(txtFecha.getText())) {
            showMessageDialog(null, "Debe ingresar una fecha de recibo.", "Atención", INFORMATION_MESSAGE);
            txtFecha.requestFocusInWindow();
            return;
        } else if ("".equals(txtProveedor.getText())) {
            showMessageDialog(null, "Debe ingresar la cedula del proveedor", "Atención", INFORMATION_MESSAGE);
            txtProveedor.requestFocusInWindow();
            return;
        } else if ("".equals(txtProveedor1.getText())) {
            showMessageDialog(null, "El nombre del proveedor no puede quedar vacio", "Atención", INFORMATION_MESSAGE);
            txtProveedor1.requestFocusInWindow();
            return;
        }else if ("".equals(txtNroRecib.getText())) {
            showMessageDialog(null, "El nro. del recibo no puede quedar vacio", "Atención", INFORMATION_MESSAGE);
            txtProveedor1.requestFocusInWindow();
            return;
        }else{ 
             if(showConfirmDialog (null, "Está seguro de guardar la factura?", "Confirmar", YES_NO_OPTION) == YES_OPTION){    
             int f=0, idRecibo=0;  
             Integer nroFactura = reciboControlador.getNroFactura(Integer.parseInt(txtNroRecib.getText().replace(".", "").trim()));
             String esContado = compraC.esContado(nroFactura);
             idRecibo = reciboControlador.devuelveId(Integer.parseInt(txtNroRecib.getText().replace(".", "").trim()));  
             String date =  (txtFecha.getText());                 
             reciboControlador.updateFechaAnulacion(idRecibo, date);
                while (f<tbVistaFacturasPendientes.getRowCount()){
                    if(tbVistaFacturasPendientes.isRowSelected(f)) {          
                        if (esContado.equals("CONTADO")){
                            facturaPendienteControl.updateEstadoAnuladoContado(idRecibo);
                        }else{
                            facturaPendienteControl.updateEstadoAnuladoPendiente(idRecibo,tbVistaFacturasPendientes.getValueAt(f, 3).toString());
                        }
                        factpendienteModel = new FacturaPendiente();
                        Integer idFact = facturaPendienteControl.nuevoCodigo();
                        factpendienteModel.setFacturaPendienteId(idFact);
                        factpendienteModel.setFechaVencimiento(tbVistaFacturasPendientes.getValueAt(f, 2).toString());
                        factpendienteModel.setMontoPendiente(Integer.parseInt(tbVistaFacturasPendientes.getValueAt(f, 5).toString()));
                        factpendienteModel.setNroFactura(Integer.parseInt(tbVistaFacturasPendientes.getValueAt(f, 1).toString()));
                        factpendienteModel.setNroPrefijo(tbVistaFacturasPendientes.getValueAt(f, 0).toString());
                        factpendienteModel.setPlazo(tbVistaFacturasPendientes.getValueAt(f, 3).toString());
                        factpendienteModel.setProveedorId(provC.devuelveId(txtProveedor.getText().replace(".", "").trim()));
                        factpendienteModel.setTotal(Integer.parseInt(tbVistaFacturasPendientes.getValueAt(f, 4).toString()));
                        facturaPendienteControl.insert(factpendienteModel);
                        facturaPendienteControl.updateEstadoPendiente(Integer.parseInt(tbVistaFacturasPendientes.getValueAt(f, 0).toString()),Integer.parseInt(tbVistaFacturasPendientes.getValueAt(f, 1).toString()));                       
                        
                    }
                    f++;
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
       getReciboProveedor(idProv);
    }
       
      private void datosActuales2(){
        txtNroRecib.setText(modeloBusquedaFacturas.getValueAt(k3, 0).toString());
        cargarCuotasPagadas(Integer.parseInt(modeloBusquedaFacturas.getValueAt(k3, 1).toString().replace(".","").trim()));
        establecerBotones("Nuevo");
    }

     
    private void cargarCuotasPagadas(int factura) {
        modeloDetalleBusqueda = new DefaultTableModel();
        try {
            
            try (ResultSet rs = facturaPendienteControl.getCuotasPagadasProv(factura)) {
                 modeloDetalleBusqueda.setColumnCount(0);
                 modeloDetalleBusqueda.setRowCount(0);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tbVistaFacturasPendientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        }   ;
        txtProveedor1 = new javax.swing.JTextField();
        lbCliente = new javax.swing.JLabel();
        txtNroRecib = new javax.swing.JTextField();
        lbNroRecibo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bAnular = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        lbDatosGenerales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetallePago = new javax.swing.JTable();
        lbFecha1 = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JFormattedTextField();
        txtFecha = new datechooser.beans.DateChooserCombo();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Anulacion Pago Proveedor");
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
        jPanel1.add(txtProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 220, -1));

        lbCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbCliente.setText("Proveedor");
        jPanel1.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 70, -1));

        txtNroRecib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroRecibActionPerformed(evt);
            }
        });
        txtNroRecib.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroRecibKeyPressed(evt);
            }
        });
        jPanel1.add(txtNroRecib, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 80, -1));

        lbNroRecibo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbNroRecibo.setText("Nro. Recibo");
        jPanel1.add(lbNroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 76, -1));

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

        bAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1434930410_note-delete.png"))); // NOI18N
        bAnular.setText("Anular");
        bAnular.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bAnular.setCategorySmallFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        bAnular.setDescription(" ");
        bAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAnularActionPerformed(evt);
            }
        });
        jPanel2.add(bAnular);

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
        jPanel1.add(lbDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1234, 130));

        jPanel3.setBackground(new java.awt.Color(51, 94, 137));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Aharoni", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("LISTA DE CUOTAS PAGADAS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(491, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(528, 528, 528))
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
        jPanel1.add(lbFecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, -1, 20));

        txtProveedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorKeyPressed(evt);
            }
        });
        jPanel1.add(txtProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 80, -1));

        txtFecha.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
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
    txtFecha.setFormat(2);
    txtFecha.setLocale(new java.util.Locale("es", "BO", ""));
    jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 160, -1));

    jPanel5.setBackground(new java.awt.Color(51, 94, 137));
    jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel5.setPreferredSize(new java.awt.Dimension(101, 25));

    jLabel5.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(255, 255, 255));
    jLabel5.setText("ANULACION PAGO PROVEEDOR");

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNroRecibActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroRecibActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroRecibActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        
    }//GEN-LAST:event_formInternalFrameOpened

    private void tbVistaFacturasPendientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesKeyPressed
            
    }//GEN-LAST:event_tbVistaFacturasPendientesKeyPressed

    private void bAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAnularActionPerformed
        try {  
            Integer cantSelected=0;
            int h=0;
            while (h<tbVistaFacturasPendientes.getRowCount()){
                if (tbVistaFacturasPendientes.isRowSelected(h)){
                   cantSelected++;
                }
              h++;
            }
            if (cantSelected==tbVistaFacturasPendientes.getRowCount()){
            guardar();
            }else{
              showMessageDialog(null,"Debe seleccionar todas las cuotas para la anulacion", "Atención", INFORMATION_MESSAGE);

            }
        } catch (Exception ex) {
            Logger.getLogger(AnularPagoProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bAnularActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void tbVistaFacturasPendientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesFocusLost
         
       
    }//GEN-LAST:event_tbVistaFacturasPendientesFocusLost

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        try {
            nuevo();
        } catch (Exception ex) {
            Logger.getLogger(AnularPagoProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
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
                            Logger.getLogger(AnularPagoProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }

            }
        }
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void tbVistaFacturasPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVistaFacturasPendientesMouseClicked

    }//GEN-LAST:event_tbVistaFacturasPendientesMouseClicked

    private void txtNroRecibKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroRecibKeyPressed
   if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            if ("*".equals(txtNroRecib.getText())) {
              if ("".equals(txtProveedor.getText())){ 
                  showMessageDialog(null, "Debe ingresar un proveedor.", "Atención", INFORMATION_MESSAGE);
                  
              }else{
                Integer prov = 0;
                try {
                    prov = provC.devuelveId(txtProveedor.getText().replace(".","").trim());
                } catch (Exception ex) {
                    Logger.getLogger(ReciboProveedorForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                BuscarForm bf = new BuscarForm(null, true);
                bf.columnas = "nro_recibo as \"Nro Prefijo\", trim(to_char(cast(factura_nro as integer),'9G999G999')) as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as \"FechaVenc\"";
                bf.tabla = "cabecera_recibo";
                bf.order = "fecha DESC";
                bf.filtroBusqueda = "proveedor_id= "+ prov+ " and fecha_anulacion is null";
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
        }
    }//GEN-LAST:event_txtNroRecibKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bAnular;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbDatosGenerales;
    private javax.swing.JLabel lbFecha1;
    private javax.swing.JLabel lbNroRecibo;
    public static javax.swing.JTable tbDetallePago;
    public static javax.swing.JTable tbVistaFacturasPendientes;
    private datechooser.beans.DateChooserCombo txtFecha;
    private javax.swing.JTextField txtNroRecib;
    public static javax.swing.JFormattedTextField txtProveedor;
    public static javax.swing.JTextField txtProveedor1;
    // End of variables declaration//GEN-END:variables

       
}
