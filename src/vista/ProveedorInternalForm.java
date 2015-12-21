/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import controlador.ProveedorControlador;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import modelo.Proveedor;





/**
 *
 * @author Pathy
 */
public class ProveedorInternalForm extends javax.swing.JInternalFrame {
    


     public ProveedorInternalForm(java.awt.Dialog parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        establecerBotones("Nuevo");
        this.moveToFront();
        this.setSize(900, 600);
        nuevo();
        JRci.setSelected(false);
        labelGuion.setVisible(false);
        txtdv.setVisible(false);
        JRparticular.setSelected(false);
        	
        
    }
     
  

    public ProveedorInternalForm() {
        
        initComponents();
        establecerBotones("Nuevo");
        nuevo();
        JRci.setSelected(false);
        labelGuion.setVisible(false);
        txtdv.setVisible(false);
        JRparticular.setSelected(false);
        //getProveedores();
        
        
    }
    
    Proveedor pro = new Proveedor();
    
    ProveedorControlador proBD = new ProveedorControlador();
    DefaultTableModel modelo = new DefaultTableModel();
    int k;

    
  
   
    
        private void nuevo() {
        limpiar();
        CBestado.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"ACTIVO", "INACTIVO"}));
        establecerBotones("Nuevo");
        txtCodigo.setEditable(true);
        CBestado.setSelectedIndex(0);
        CBestado.setEnabled(false);
        txtdv.setEditable(false);
        
     
        
        try {
            txtCodigo.requestFocusInWindow();     
        } catch (Exception ex) {
            showMessageDialog(null, ex.toString(), "Error", ERROR_MESSAGE);
        }
       
        }
        private void limpiar() {
        txtCodigo.setText("");
        txtci.setText("");
        txtdv.setText("");
        txtNombre.setText("");      
        txtApellido.setText("");
        txtDireccion.setText("");
        txtTelefono.setText(""); 
      
        
        if(tbProveedor.getRowCount() == 0){
            establecerBotones("Vacio");
        }
        }
        
       private void guardar() throws Exception{
       if (showConfirmDialog(null, "Está seguro de guardar los datos?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
            pro.setCodProveedor(txtCodigo.getText());
            pro.setNombre(txtNombre.getText());
            pro.setApellido(txtApellido.getText()); 
            pro.setDireccion(txtDireccion.getText()); 
            pro.setCi(txtci.getText());
            pro.setDv(txtdv.getText());
            pro.setTelefono(txtTelefono.getText());
            pro.setEstado(CBestado.getSelectedItem().toString());
            int idProv = proBD.nuevoCodigo();
            pro.setProveedorId(idProv);
            
             if (txtci.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo cédula/ruc vacío, por favor ingrese su cédula o RUC ", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JRruc.isSelected() == true && txtdv.getText().trim().isEmpty()) {
                showMessageDialog(this, "Campo RUC incompleto, por favor ingrese correctamente su RUC", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
         
            if (txtNombre.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo nombre vacío, por favor ingrese correctamente su nombre", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JRparticular.isSelected() == true && txtApellido.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo apellido vacío, por favor ingrese correctamente su apellido", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtDireccion.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo dirección vacío, por favor ingrese correctamente su dirección", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtTelefono.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo teléfono, por favor ingrese correctamente su teléfono", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

          
           
           
         if (bNuevo.isEnabled() == false) { //is Enable true - habilita boton 
                try {
                    proBD.insert(pro);
                    nuevo();
                    getProveedores();
                } catch (Exception ex) {
                    //showMessageDialog(null, "Debe ingresar la descripción.", "Atención", INFORMATION_MESSAGE); 
                    
                   showMessageDialog(null,ex.getMessage(), "Error", ERROR_MESSAGE);
                }
            } else {
                 try {
                    proBD.update(pro);
                    showMessageDialog(null, "Actualizado correctamente");
                    limpiar();              
                    getProveedores();
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
             
                
            }
         
         }
       }
     private void cancelar(){
           
        if(showConfirmDialog (null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION){
            txtCodigo.setEditable(false);
            CBestado.setEnabled(true);
            txtdv.setEditable(false);
            getProveedores();         
            establecerBotones("Edicion");
            limpiar();
            if (modelo.getRowCount() == 0) {
                limpiar(); 
                establecerBotones("Vacio"); 
                modoBusqueda(false); 
                return;
            }
            if (k >= 0){
                    limpiar(); 
                    datosActuales(); 
                    establecerBotones("Edicion");
                    modoBusqueda(false);
                    return;
            }
        }
    }
    private void borrar(){
          
        if(tbProveedor.getSelectedRow() == -1){
            showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
        }else{    try {
                
               proBD.delete(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 0).toString());
               showMessageDialog(null, "Operación exitosa"); 
               limpiar();
               getProveedores();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }
       
        
    private void getProveedores() {
        try {
            modelo.setColumnCount(0);
            modelo.setRowCount(0);
            tbProveedor.setModel(modelo);
           
            try (ResultSet rs = proBD.datos()) {
           
                ResultSetMetaData rsMd = rs.getMetaData();
                
                int cantidadColumnas = rsMd.getColumnCount();
                
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modelo.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i]=rs.getObject(i+1);
                    }
                    modelo.addRow(fila);
                }
            } catch (Exception ex) {
                showMessageDialog(null,  ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
   
    
    
    
        
        
        
        
        
        private void establecerBotones(String modo) {
        switch (modo) {
            case "Nuevo":
                bNuevo.setEnabled(false);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(true);
                bBuscar.setEnabled(false);
                break;
            case "Edicion":
                bNuevo.setEnabled(true);
                bBorrar.setEnabled(true);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(true);
                bBuscar.setEnabled(true);
                break;
            case "Vacio":
                bNuevo.setEnabled(true);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(false);
                bGuardar.setEnabled(false);
                bBuscar.setEnabled(false);
                break;
            case "Buscar":
                bNuevo.setEnabled(false);
                bBorrar.setEnabled(false);
                bCancelar.setEnabled(true);
                bGuardar.setEnabled(false);
                bBuscar.setEnabled(true);
                break;
        }
    }
  private String Pa_Calcular_Dv_11_A(String p_numero, int p_basemax) {
  int v_total, v_resto, k, v_numero_aux, v_digit;
  String v_numero_al = "";
  String v_digit2;
      
  for (int i = 0; i < p_numero.length(); i++) {
    char c = p_numero.charAt(i);
    if(Character.isDigit(c)) {
      v_numero_al += c;
    } else {
      v_numero_al += (int) c;
    }
  }
      
  k = 2;
  v_total = 0;
      
  for(int i = v_numero_al.length() - 1; i >= 0; i--) {
    k = k > p_basemax ? 2 : k;
    v_numero_aux = v_numero_al.charAt(i) - 48;
    v_total += v_numero_aux * k++;
  }
      
  v_resto = v_total % 11;
  v_digit = v_resto > 1 ? 11 - v_resto : 0;
  
      
  return Integer.toString(v_digit);
}
private void buscar(){
        limpiar();
        establecerBotones("Buscar");
        modoBusqueda(true);
}
private void modoBusqueda(boolean v){
        if (v == true) {
        txtCodigo.setEditable(true);
        txtCodigo.requestFocusInWindow();
        txtCodigo.setBackground(Color.yellow);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtci.setEnabled(false);
        txtdv.setEnabled(false);
        JRci.setEnabled(false);
        JRruc.setEnabled(false);
        JRempresa.setEnabled(false);
        JRparticular.setEnabled(false);
     
        } else {
        txtCodigo.setEditable(true);
        txtCodigo.setBackground(Color.white);
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtci.setEnabled(true);
        txtdv.setEnabled(true);
        JRci.setEnabled(true);
        JRruc.setEnabled(true);
        JRempresa.setEnabled(true);
        JRparticular.setEnabled(true);
        
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

        btnGrupoCedula = new javax.swing.ButtonGroup();
        btnGrupoEmpresa = new javax.swing.ButtonGroup();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        menuBar2 = new java.awt.MenuBar();
        menu3 = new java.awt.Menu();
        menu4 = new java.awt.Menu();
        menuBar3 = new java.awt.MenuBar();
        menu5 = new java.awt.Menu();
        menu6 = new java.awt.Menu();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        bBorrar = new org.edisoncor.gui.button.ButtonTask();
        bBuscar = new org.edisoncor.gui.button.ButtonTask();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        JRempresa = new javax.swing.JRadioButton();
        JRparticular = new javax.swing.JRadioButton();
        lbApellido = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        JRruc = new javax.swing.JRadioButton();
        txtci = new javax.swing.JTextField();
        JRci = new javax.swing.JRadioButton();
        txtNombre = new javax.swing.JTextField();
        labelGuion = new javax.swing.JLabel();
        CBestado = new javax.swing.JComboBox();
        txtApellido = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtdv = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProveedor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        menu3.setLabel("File");
        menuBar2.add(menu3);

        menu4.setLabel("Edit");
        menuBar2.add(menu4);

        menu5.setLabel("File");
        menuBar3.add(menu5);

        menu6.setLabel("Edit");
        menuBar3.add(menu6);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Proveedor");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/proveedores.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1258, 500));

        jPanel2.setBackground(new java.awt.Color(51, 94, 137));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(51, 94, 137));
        jLabel1.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PROVEEDORES");
        jPanel2.add(jLabel1, java.awt.BorderLayout.CENTER);

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
        bBorrar.setText("Inactivar");
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

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        bBuscar.setText("Buscar");
        bBuscar.setCategoryFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        bBuscar.setCategorySmallFont(new java.awt.Font("Aharoni", 0, 5)); // NOI18N
        bBuscar.setDescription(" ");
        bBuscar.setFont(new java.awt.Font("Algerian", 0, 5)); // NOI18N
        bBuscar.setIconTextGap(2);
        bBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(bBuscar);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Proveedor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel2.setText("Nombre:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 55, 20));

        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });
        jPanel3.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 240, -1));

        txtTelefono.setToolTipText("Formato (09xx) 323-323");
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });
        jPanel3.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 240, -1));

        btnGrupoEmpresa.add(JRempresa);
        JRempresa.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        JRempresa.setText("Empresa");
        JRempresa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JRempresaMouseClicked(evt);
            }
        });
        jPanel3.add(JRempresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        btnGrupoEmpresa.add(JRparticular);
        JRparticular.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        JRparticular.setText("Particular");
        JRparticular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JRparticularMouseClicked(evt);
            }
        });
        jPanel3.add(JRparticular, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        lbApellido.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        lbApellido.setText("Apellido:");
        jPanel3.add(lbApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, 20));

        jLabel5.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel5.setText("Direccion:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 60, 20));

        jLabel6.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel6.setText("Telefono/Celular:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, 20));

        jLabel7.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel7.setText("Estado:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 40, 20));

        btnGrupoCedula.add(JRruc);
        JRruc.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        JRruc.setText("RUC");
        JRruc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JRrucMouseClicked(evt);
            }
        });
        jPanel3.add(JRruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        txtci.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtciFocusLost(evt);
            }
        });
        txtci.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtciKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtciKeyTyped(evt);
            }
        });
        jPanel3.add(txtci, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 119, -1));

        btnGrupoCedula.add(JRci);
        JRci.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        JRci.setText("Cedula");
        JRci.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JRciMouseClicked(evt);
            }
        });
        jPanel3.add(JRci, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 73, -1));

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        jPanel3.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 240, -1));

        labelGuion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelGuion.setText("-");
        jPanel3.add(labelGuion, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 10, -1));

        CBestado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Inactivo" }));
        CBestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBestadoActionPerformed(evt);
            }
        });
        jPanel3.add(CBestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 80, -1));

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });
        jPanel3.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 240, -1));

        jLabel3.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        jLabel3.setText("Codigo");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 40, 20));

        txtCodigo.setFont(new java.awt.Font("Aharoni", 0, 11)); // NOI18N
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });
        jPanel3.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 120, 30));
        jPanel3.add(txtdv, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 36, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setForeground(new java.awt.Color(153, 153, 153));

        jPanel5.setBackground(new java.awt.Color(87, 123, 181));

        jLabel8.setFont(new java.awt.Font("Aharoni", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("LISTA DE PROVEEDORES");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(319, 319, 319)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tbProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "RUC/CI", "Nombre", "Apellido", "Dirección", "Teléfono", "Estado"
            }
        ));
        tbProveedor.setToolTipText("");
        tbProveedor.setName(""); // NOI18N
        tbProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProveedorMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbProveedor);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
            borrar();
            limpiar();
            getProveedores();
    }//GEN-LAST:event_bBorrarActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
       nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
         try {
             guardar();
         } catch (Exception ex) {
             Logger.getLogger(ProveedorInternalForm.class.getName()).log(Level.SEVERE, null, ex);
         }
    }//GEN-LAST:event_bGuardarActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed

        cancelar();
        
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_bBuscarActionPerformed

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
    
    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
         char c = evt.getKeyChar();
         if(Character.isLetter(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
        if(txtTelefono.getText().length()>14){
             evt.consume();  
        }

    
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void JRempresaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JRempresaMouseClicked
             txtApellido.setVisible(false);
             lbApellido.setVisible(false);
             txtNombre.requestFocusInWindow();
    }//GEN-LAST:event_JRempresaMouseClicked

    private void JRparticularMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JRparticularMouseClicked
         if (evt.isMetaDown()){
            return;
        } else {
             
             txtApellido.setVisible(true);
             lbApellido.setVisible(true);
            
          
             txtNombre.requestFocusInWindow();
            //txtdv.setText((Pa_Calcular_Dv_11_A(txtci.getText(), 11)));    
        }
    }//GEN-LAST:event_JRparticularMouseClicked

    private void JRrucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JRrucMouseClicked
        if (evt.isMetaDown()){
            return;
        } else {
     
            txtdv.setVisible(true);
            labelGuion.setVisible(true);
            txtci.requestFocusInWindow();
            //txtdv.setText((Pa_Calcular_Dv_11_A(txtci.getText(), 11)));    
        }
    }//GEN-LAST:event_JRrucMouseClicked

    private void txtciFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtciFocusLost
       //txtdv.setVisible(true);
       //labelGuion.setVisible(true);
        if (JRruc.isSelected()){
        txtdv.setText((Pa_Calcular_Dv_11_A(txtci.getText(), 11))); }
       else{
        txtdv.setText("");
       } 

    }//GEN-LAST:event_txtciFocusLost

    private void txtciKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciKeyPressed
       
    }//GEN-LAST:event_txtciKeyPressed

    private void txtciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciKeyTyped
        if(txtci.getText().length()>7){
             evt.consume();  
        }
   
    }//GEN-LAST:event_txtciKeyTyped

    private void JRciMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JRciMouseClicked
         txtdv.setVisible(false);
         labelGuion.setVisible(false);
         txtci.requestFocusInWindow();


    }//GEN-LAST:event_JRciMouseClicked

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
          String nomb = txtNombre.getText().toLowerCase();
        int aux = 0;
        String nombre = "";

        for (int i = 0; i < nomb.length(); i++) {

            char letra = nomb.charAt(i);

            if (i == 0) {
                letra = Character.toUpperCase(letra);
            } else if (i > 0 && letra != ' ') {
                letra = Character.toLowerCase(letra);
            }

            if (letra == ' ' || letra == '.') {
                aux = i + 1;
            }

            if (aux == i) {
                letra = Character.toUpperCase(letra);
            }

            nombre = nombre + letra;
        }

        txtNombre.setText(nombre);
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
       char c = evt.getKeyChar();
         if(Character.isDigit(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
        if(txtNombre.getText().length()>14){
             evt.consume();  
        }

    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
         String ape = txtApellido.getText().toLowerCase();
        int aux = 0;
        String apellido = "";

        for (int i = 0; i < ape.length(); i++) {

            char letra = ape.charAt(i);

            if (i == 0) {
                letra = Character.toUpperCase(letra);
            } else if (i > 0 && letra != ' ') {
                letra = Character.toLowerCase(letra);
            }

            if (letra == ' ' || letra == '.') {
                aux = i + 1;
            }

            if (aux == i) {
                letra = Character.toUpperCase(letra);
            }

            apellido = apellido + letra;
        }

        txtApellido.setText(apellido);
    }//GEN-LAST:event_txtApellidoKeyReleased

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
          char c = evt.getKeyChar();
         if(Character.isDigit(c))
         {
             getToolkit().beep();
             evt.consume();
         }   
        if(txtApellido.getText().length()>14){
             evt.consume();  
        }


    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
         if(txtCodigo.getText().length()>9){
             evt.consume();  
        }
         if (txtNombre.isEnabled() == true){
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtCodigo.getText())) {
                
                BuscarForm bf = new BuscarForm( null, true);
                bf.columnas = "cod_proveedor as \"Cod. Proveedor\" ,nombre as \"Nombre\", apellido as  \"Apellido\",trim(to_char(cast(ci as integer),'9G999G999')) as  \"CI\"";
                bf.tabla = "Proveedor";
                bf.order = "proveedor_id";
                bf.filtroBusqueda = "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);
                
                for(int c=0; c<modelo.getRowCount(); c ++){
                    if (modelo.getValueAt(c, 0).toString().equals(bf.retorno)){
                        modoBusqueda(false);
                        establecerBotones("Edicion");
                        k = c;
                         datosActuales();
                    return;
                    }
                }
                
            }
            
            for(int c=0; c<modelo.getRowCount(); c ++){
                if (modelo.getValueAt(c, 0).toString().equals(txtCodigo.getText())){
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    k = c;
                    datosActuales();
                    return;
                }
            }
        }

    }//GEN-LAST:event_txtCodigoKeyPressed

    
    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void tbProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProveedorMouseClicked
          String ced = tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 1).toString();
       
       
        
        //busca la posicion en la que se encuentra el guion
        
        
        // variable dv que almacena el dígito verificador en caso de que sea un ruc
        char dv = ced.charAt(ced.length()-1);
        
        if (evt.isMetaDown()){
            return;
        } else {
            if (bNuevo.isEnabled() == true) {
                
                //Averiguo si es un RUC 
                if(ced.indexOf('-') != -1){
                    int posGuion = ced.indexOf('-');
                    txtdv.setVisible(true);
                    labelGuion.setVisible(true);
                    txtdv.setText(Character.toString(dv));
                    JRruc.setSelected(true);
                    txtci.setText(ced.substring(0, posGuion));
                     
                }else{
                    txtdv.setText("");
                    txtdv.setVisible(false);
                    labelGuion.setVisible(false);
                    JRci.setSelected(true);
                    txtci.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 1).toString());   
                }
               
                // si apellido es null entonces es una empresa
                if(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 3).toString().equals("")){
                    lbApellido.setVisible(false);
                    JRempresa.setSelected(true);
                    txtApellido.setText("");
                    txtApellido.setVisible(false);
                }else{
                    JRparticular.setSelected(true);
                    lbApellido.setVisible(true);
                    txtApellido.setVisible(true);
                    txtApellido.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 3).toString());
                }
                txtCodigo.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 0).toString());
                txtNombre.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 2).toString());
                txtDireccion.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 4).toString());
                txtTelefono.setText(tbProveedor.getValueAt(tbProveedor.getSelectedRow(), 5).toString());
                
            }
        }
    }//GEN-LAST:event_tbProveedorMouseClicked

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void CBestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBestadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBestadoActionPerformed

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        String nomb = txtDireccion.getText().toLowerCase();
        int aux = 0;
        String direccion = "";

        for (int i = 0; i < nomb.length(); i++) {

            char letra = nomb.charAt(i);

            if (i == 0) {
                letra = Character.toUpperCase(letra);
            } else if (i > 0 && letra != ' ') {
                letra = Character.toLowerCase(letra);
            }

            if (letra == ' ' || letra == '.') {
                aux = i + 1;
            }

            if (aux == i) {
                letra = Character.toUpperCase(letra);
            }

            direccion = direccion + letra;
        }

        txtDireccion.setText(direccion);
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void datosActuales(){
            String ced = modelo.getValueAt(k, 1).toString();
            char dv = ced.charAt(ced.length()-1);
             if (bNuevo.isEnabled() == true) {
                
                //Averiguo si es un RUC 
                if(ced.indexOf('-') != -1){
                    int posGuion = ced.indexOf('-');
                    txtdv.setVisible(true);
                    labelGuion.setVisible(true);
                    txtdv.setText(Character.toString(dv));
                    JRruc.setSelected(true);
                    txtci.setText(ced.substring(0, posGuion));
                     
                }else{
                    txtdv.setText("");
                    txtdv.setVisible(false);
                    labelGuion.setVisible(false);
                    JRci.setSelected(true);
                    txtci.setText(modelo.getValueAt(k, 1).toString());   
                }
               
                // si apellido es null entonces es una empresa
                if(modelo.getValueAt(k, 3).toString().equals("")){
                    lbApellido.setVisible(false);
                    JRempresa.setSelected(true);
                    txtApellido.setText("");
                    txtApellido.setVisible(false);
                }else{
                    JRparticular.setSelected(true);
                    lbApellido.setVisible(true);
                    txtApellido.setVisible(true);
                    txtApellido.setText(modelo.getValueAt(k, 3).toString());
                }

            txtCodigo.setText(modelo.getValueAt(k, 0).toString());
            txtNombre.setText(modelo.getValueAt(k, 2).toString());
            txtDireccion.setText(modelo.getValueAt(k, 4).toString());
            txtTelefono.setText(modelo.getValueAt(k, 5).toString());
              
        }
    
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox CBestado;
    private javax.swing.JRadioButton JRci;
    private javax.swing.JRadioButton JRempresa;
    private javax.swing.JRadioButton JRparticular;
    private javax.swing.JRadioButton JRruc;
    private org.edisoncor.gui.button.ButtonTask bBorrar;
    private org.edisoncor.gui.button.ButtonTask bBuscar;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.ButtonGroup btnGrupoCedula;
    private javax.swing.ButtonGroup btnGrupoEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelGuion;
    private javax.swing.JLabel lbApellido;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.Menu menu3;
    private java.awt.Menu menu4;
    private java.awt.Menu menu5;
    private java.awt.Menu menu6;
    private java.awt.MenuBar menuBar1;
    private java.awt.MenuBar menuBar2;
    private java.awt.MenuBar menuBar3;
    private javax.swing.JTable tbProveedor;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtci;
    private javax.swing.JTextField txtdv;
    // End of variables declaration//GEN-END:variables
}
