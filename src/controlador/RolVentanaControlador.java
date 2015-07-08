/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.RolVentanas;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class RolVentanaControlador {
       public void insert(RolVentanas rolVent) throws Exception {
      
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
           
        try {
            baseDatos.save(rolVent);
            baseDatos.beginTransaction().commit();
        }  catch(HibernateException e){
            e.getMessage();
        }
       }
        
         public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT rol.nombre, ventana.nombre from Rol as rol, Ventanas as ventana, rol_ventanas where rol.id_rol=rol_ventanas.rol_id and ventana.id_ventana=rol_ventanas.id_ventana";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
        public void delete(Integer intRol, Integer intVent) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("DELETE from RolVentanas where id_rol = " +intRol+ " and id_ventana= " +intVent+ "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar proveedor: \n" + e.getMessage());
        }
    }
         
}
