/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.Rol;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 */
public class RolControlador {
    
    public void insert(Rol rol) throws Exception {
      
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
           
        try {
            baseDatos.save(rol);
            baseDatos.beginTransaction().commit();
        }  catch (org.hibernate.exception.ConstraintViolationException cve) {
    
            JOptionPane.showMessageDialog(null, "El rol ya existe. Error al insertar", 
                     "Error: ", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
         
    }
        
    public int devuelveId(String nombre ) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("Select idRol from Rol where nombre = '" +nombre+ "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al modificar rol: \n" + e.getMessage());
        }
    }
     public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nombre as \"Rol\", descripcion as \"Descripcion\" from rol";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    public ResultSet datosCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nombre as dato, descripcion from rol";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    
   public void update(Rol rol, int i) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Rol set "
                   +" nombre = '" + rol.getNombre() + "', descripcion = '" + rol.getDescripcion()+ "'"
                    + " where id_rol = " + i + "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar rol: \n" + e.getMessage());
        }
    }
    
    public void delete(Integer i) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("DELETE from Rol where id_rol = " +i+ "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar rol: \n" + e.getMessage());
        }
    }
}
   
