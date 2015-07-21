

package vista;


import controlador.UsuarioControlador;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import modelo.Usuario;

/**
 *
 * @author Pathy
 */
public class UsuarioForm extends javax.swing.JInternalFrame {

    /**
     * Constructor que inicializa todos los componentes
     * 
     */
     
    public UsuarioForm() {
        initComponents();
        this.moveToFront();
        jLabel1.setLayout(null);  
        deshabilitarCampos();
        ocultarColumna();
        txtIdUsuario.setVisible(false);
        
    }

    boolean modificar = false;
    String contrasenhaSinEncriptar = "";
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
    Usuario usuario = new Usuario();
    UsuarioControlador usuarioBD = new UsuarioControlador();

    
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bNuevo = new org.edisoncor.gui.button.ButtonTask();
        bCancelar = new org.edisoncor.gui.button.ButtonTask();
        bGuardar = new org.edisoncor.gui.button.ButtonTask();
        bBorrar = new org.edisoncor.gui.button.ButtonTask();
        bBuscar = new org.edisoncor.gui.button.ButtonTask();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtContrasenha = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jComboBoxRol = new javax.swing.JComboBox();
        lbRol = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Usuario");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1258, 500));
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

        jPanel2.setBackground(new java.awt.Color(51, 94, 137));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(51, 94, 137));
        jLabel1.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("USUARIOS");
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Usuario", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Aharoni", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel9.setText("Nombre");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 20));

        txtNombre.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        jPanel3.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 240, -1));

        jLabel10.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel10.setText("Apellido");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 20));

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });
        jPanel3.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 240, -1));

        jLabel11.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel11.setText("Usuario");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyTyped(evt);
            }
        });
        jPanel3.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 240, -1));

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel6.setText("Contraseña");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 60, 20));

        txtContrasenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContrasenhaKeyTyped(evt);
            }
        });
        jPanel3.add(txtContrasenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 240, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        jLabel3.setText("Email");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, 20));

        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmailKeyTyped(evt);
            }
        });
        jPanel3.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 240, -1));

        jPanel3.add(jComboBoxRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 120, -1));

        lbRol.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 11)); // NOI18N
        lbRol.setText("Rol");
        jPanel3.add(lbRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 20, 20));

        txtIdUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtIdUsuario.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setFocusable(false);
        txtIdUsuario.setOpaque(false);
        txtIdUsuario.setRequestFocusEnabled(false);
        txtIdUsuario.setSelectionColor(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(txtIdUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 60, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setForeground(new java.awt.Color(153, 153, 153));

        jPanel5.setBackground(new java.awt.Color(87, 123, 181));

        jLabel8.setFont(new java.awt.Font("Aharoni", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("LISTA DE USUARIOS");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(363, 363, 363)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        tbUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Apellido", "Usuario", "Correo electrónico", "Rol", "Id", "Contraseña"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsuario);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento del boton borrar
     * @param evt evento al hacer clic sobre el botón eliminar
     */
    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        borrar();
    }//GEN-LAST:event_bBorrarActionPerformed

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_bGuardarActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        buscar();
        
        
    }//GEN-LAST:event_bBuscarActionPerformed

     /**
     * Método que guarda todos los datos introducidos por el usuario. 
     * Se valida que no haya campos vacíos
     */
    private void guardar() {
        if (showConfirmDialog(null, "Está seguro de guardar los datos?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
            ocultarColumna();

            if (txtIdUsuario.getText().isEmpty() == false) {
                usuario.setIdusuario(Integer.parseInt(txtIdUsuario.getText()));
                System.out.println("ENTRO");
            }
            usuario.setNombre(txtNombre.getText());
            usuario.setApellido(txtApellido.getText());
            usuario.setNombreusuario(txtUsuario.getText());
            contrasenhaSinEncriptar = txtContrasenha.getText();
            System.out.println(contrasenhaSinEncriptar);
            usuario.setContrasenha(md5(txtContrasenha.getText()));
            usuario.setEmail(txtEmail.getText());

            try {
                usuario.setIdRol(usuarioBD.getCodigo((String) jComboBoxRol.getSelectedItem()));

            } catch (Exception ex) {
                Logger.getLogger(UsuarioForm.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (txtNombre.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo nombre vacío, por favor ingrese su nombre ", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtApellido.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo apellido vacío, por favor ingrese su apellido ", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtContrasenha.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo contraseña vacío, por favor ingrese su contraseña ", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtEmail.getText().trim().isEmpty() == true) {
                showMessageDialog(this, "Campo correo electrónico vacío, por favor ingrese su correo ", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (jComboBoxRol.getSelectedIndex() == -1) {
                showMessageDialog(null, "Debe seleccionar el rol", "Atención", INFORMATION_MESSAGE);
                return;
            }
            System.out.println(modificar);
            if (bNuevo.isEnabled() == false) {
                try {

                    usuarioBD.insert(usuario);
                    nuevo();
                    //showMessageDialog(null, "Guardado correctamente");
                    getUsuario();

                } catch (Exception ex) {
                    System.out.println("Error al guardar");
                    showMessageDialog(null, ex.getMessage(), "", ERROR_MESSAGE);
                }
            } else {
                try {
                    if(modificar == false){
                        usuario.setContrasenha(contrasenhaSinEncriptar);
                    }
                    usuarioBD.update(usuario);
                    showMessageDialog(null, "Actualizado correctamente");
                    limpiar();
                    getUsuario();
                } catch (Exception ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
            }
            modificar = false;
        }
    }
    
    /**
     * Método que limpia todos los campos para ingresar datos nuevos 
     */
    private void nuevo() { 
        limpiar();
        habilitarCampos();
        establecerBotones("Nuevo");
    }
    
    /**
     * Método que recupera todos los clientes en la tabla
     */
    private void getUsuario() {
        try {

            tbUsuario.setModel(modelo);

            modelo.setRowCount(0);
            modelo.setColumnCount(0);

            try (ResultSet rs = usuarioBD.consultar()) {
                ResultSetMetaData rsMd = rs.getMetaData();

                int cantidadColumnas = rsMd.getColumnCount();

                //System.out.println("Cantidad columnas "+cantidadColumnas);
                for (int i = 1; i <= cantidadColumnas; i++) {
                    modelo.addColumn(rsMd.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cantidadColumnas];
                    for (int i = 0; i < cantidadColumnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    modelo.addRow(fila);
                }

                ocultarColumna();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }

    }

    /**
     * Método que vacía todos los campos
     */
    private void limpiar() {
        txtApellido.setText("");
        txtNombre.setText("");
        txtContrasenha.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        jComboBoxRol.setSelectedIndex(-1);
        txtIdUsuario.setText("");

        if (tbUsuario.getRowCount() == 0) {
            establecerBotones("Vacio");
        }
    }
    
    /**
     * Método que establece el estado de un cliente de activo a inactivo o viceversa
     */
    private void borrar() {
        if (tbUsuario.getSelectedRow() == -1) {
            showMessageDialog(this, "Por favor seleccione una fila", "Atención", JOptionPane.WARNING_MESSAGE);
        }
        if (showConfirmDialog(null, "Está seguro de eliminar los datos?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
            try {
                usuarioBD.delete(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 5).toString());
                showMessageDialog(null, "Se eliminó correctamente");
                getUsuario();
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método que cancela la operación de registro de clientes nuevos
     */
    private void cancelar() {
        
         if (showConfirmDialog(null, "Está seguro de cancelar la operación?", "Confirmar", YES_NO_OPTION) == YES_OPTION) {
            //deshabilitarCampos();
            //txtUsuario.setBackground(Color.white);
            modoBusqueda(false);
            ocultarColumna();
            limpiar();
            getUsuario();
            //modoBusqueda(false);
            establecerBotones("Edicion");
        }
    }
    
     private void buscar() {
        limpiar();
        establecerBotones("Buscar");
        modoBusqueda(true);
    
    }

      private void ocultarColumna(){
  
        //oculto columna id
        tbUsuario.getColumnModel().getColumn(5).setMaxWidth(0);
        tbUsuario.getColumnModel().getColumn(5).setMinWidth(0);
        tbUsuario.getColumnModel().getColumn(5).setPreferredWidth(0);

        //oculto columna contrasenha
        tbUsuario.getColumnModel().getColumn(6).setMaxWidth(0);
        tbUsuario.getColumnModel().getColumn(6).setMinWidth(0);
        tbUsuario.getColumnModel().getColumn(6).setPreferredWidth(0);
    
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
                bBuscar.setEnabled(false);
                break;      
        }
    }
    
    private void txtEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyTyped

        if (txtEmail.getText().length() > 29) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtEmailKeyTyped

    private void txtContrasenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContrasenhaKeyTyped
        
        
        if(bNuevo.isEnabled() == true){
            modificar = true;
            System.out.println("MODIFICO");
        }
        if (txtContrasenha.getText().length() > 31) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtContrasenhaKeyTyped

    private void txtUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyTyped

        if (txtUsuario.getText().length() > 14) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtUsuarioKeyTyped

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if (txtNombre.isEnabled() == true) {
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("*".equals(txtUsuario.getText())) {

                BuscarForm bf = new BuscarForm(null, true);
                bf.columnas = "idusuario, nombre";
                bf.tabla = "Usuario";
                bf.order = "idusuario";
                bf.filtroBusqueda = "";
                bf.setLocationRelativeTo(this);
                bf.setVisible(true);

                for (int c = 0; c < modelo.getRowCount(); c++) {
                    if (modelo.getValueAt(c, 5).toString().equals(bf.retorno)) {
                        modoBusqueda(false);
                        establecerBotones("Edicion");

                        txtNombre.setText((String) modelo.getValueAt(c, 0));
                        txtApellido.setText((String) modelo.getValueAt(c, 1));
                        txtUsuario.setText((String) modelo.getValueAt(c, 2));
                        txtEmail.setText((String) modelo.getValueAt(c, 3));
                        txtIdUsuario.setText(modelo.getValueAt(c, 5).toString());
                        txtContrasenha.setText((String) modelo.getValueAt(c, 6));

                        try {
                            jComboBoxRol.setSelectedItem(modelo.getValueAt(c, 4));
                        } catch (Exception ex) {
                            showMessageDialog(null, "Combo rol no obtuvo datos", "Error", ERROR_MESSAGE);
                        }
                        return;
                    }
                }
            }
            for (int c = 0; c < modelo.getRowCount(); c++) {
                if (modelo.getValueAt(c, 2).toString().equals(txtUsuario.getText())) {
                    modoBusqueda(false);
                    establecerBotones("Edicion");
                    txtNombre.setText((String) modelo.getValueAt(c, 0));
                    txtApellido.setText((String) modelo.getValueAt(c, 1));
                    txtUsuario.setText((String) modelo.getValueAt(c, 2));
                    txtEmail.setText((String) modelo.getValueAt(c, 3));
                    txtIdUsuario.setText(modelo.getValueAt(c, 5).toString());
                    txtContrasenha.setText((String) modelo.getValueAt(c, 6));

                    try {
                        jComboBoxRol.setSelectedItem(modelo.getValueAt(c, 4));

                    } catch (Exception ex) {
                        showMessageDialog(null, "Combo rol no obtuvo datos", "Error", ERROR_MESSAGE);
                    }

                    return;
                }
            }
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        char c = evt.getKeyChar();

        if (Character.isDigit(c)) {
            getToolkit().beep();
            evt.consume();
        }

        if (txtApellido.getText().length() > 19) {
            evt.consume();
        }
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        char c = evt.getKeyChar();

        if (Character.isDigit(c)) {
            getToolkit().beep();
            evt.consume();
        }

        if (txtNombre.getText().length() > 19) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void tbUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuarioMouseClicked
        
        if(txtNombre.isEnabled() == false){
            evt.consume();
        }else{
            try {
                txtIdUsuario.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 5).toString());
                txtNombre.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 0).toString());
                txtApellido.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 1).toString());
                txtUsuario.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 2).toString());
                txtContrasenha.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 6).toString());
                txtEmail.setText(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 3).toString());
                jComboBoxRol.setSelectedItem(tbUsuario.getValueAt(tbUsuario.getSelectedRow(), 4).toString());
            } catch (Exception ex) {
                Logger.getLogger(UsuarioForm.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }//GEN-LAST:event_tbUsuarioMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        nuevo();
        getRol();
        txtNombre.requestFocus();
    }//GEN-LAST:event_formInternalFrameOpened

   private void modoBusqueda(boolean v) {

        if (v == true) {
            txtUsuario.requestFocusInWindow();
            txtUsuario.setBackground(Color.yellow);
            txtApellido.setEnabled(false);
            txtContrasenha.setEnabled(false);
            txtEmail.setEnabled(false);
            txtNombre.setEnabled(false);
            txtUsuario.setEnabled(true);
            jComboBoxRol.setEnabled(false);
            tbUsuario.setEnabled(false);
            
        } else {
            txtUsuario.setBackground(Color.white);
            txtApellido.setEnabled(true);
            txtContrasenha.setEnabled(true);
            txtEmail.setEnabled(true);
            txtNombre.setEnabled(true);
            txtUsuario.setEnabled(true);
            jComboBoxRol.setEnabled(true);
            tbUsuario.setEnabled(true);
        }
    }

    public static String md5(String input) {

        StringBuffer h = null;

        if (null == input) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(input.getBytes());
            h = new StringBuffer(b.length);
            for (int i = 0; i < b.length; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    h.append("0").append(Integer.toHexString(u));
                } else {
                    h.append(Integer.toHexString(u));
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        }
        return h.toString();
    }

  
   private void habilitarCampos() {
        txtApellido.setEnabled(true);
        txtContrasenha.setEnabled(true);
        txtEmail.setEnabled(true);
        txtNombre.setEnabled(true);
        txtUsuario.setEnabled(true);
        jComboBoxRol.setEnabled(true);
    }

    private void deshabilitarCampos() {
        txtApellido.setEnabled(false);
        txtContrasenha.setEnabled(false);
        txtEmail.setEnabled(false);
        txtNombre.setEnabled(false);
        txtUsuario.setEnabled(false);
        jComboBoxRol.setEnabled(false);
    }

      private void getRol() {
        try {
            try (ResultSet rs = usuarioBD.datos()) {

                modelCombo.removeAllElements();

                while (rs.next()) {
                    modelCombo.addElement(rs.getObject("nombre").toString());
                }
                jComboBoxRol.setModel(modelCombo);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.edisoncor.gui.button.ButtonTask bBorrar;
    private org.edisoncor.gui.button.ButtonTask bBuscar;
    private org.edisoncor.gui.button.ButtonTask bCancelar;
    private org.edisoncor.gui.button.ButtonTask bGuardar;
    private org.edisoncor.gui.button.ButtonTask bNuevo;
    private javax.swing.ButtonGroup btnGrupoCedula;
    private javax.swing.ButtonGroup btnGrupoEmpresa;
    private javax.swing.JComboBox jComboBoxRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbRol;
    private javax.swing.JTable tbUsuario;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JPasswordField txtContrasenha;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
