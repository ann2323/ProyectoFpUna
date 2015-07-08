/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Ventanas;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class VentanaControlador {
    public Integer nuevoCodigo() {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
            return (Integer) baseDatos.createQuery("select coalesce (max(id_ventana), 0) + 1 from Ventanas").uniqueResult();
   
    }
    
     public void insert(Ventanas ventana) throws Exception {
      
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
           
        try {
            baseDatos.save(ventana);
            baseDatos.beginTransaction().commit();
        }  catch (org.hibernate.exception.ConstraintViolationException cve) {
    
            JOptionPane.showMessageDialog(null, "El codigo de ventana existe. Error al insertar", 
                     "Error: ", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
         
    }
    public int devuelveId(String nombre ) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("Select idVentana from Ventanas where nombre = '" +nombre+ "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
     
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT id_ventana as \"Codigo\", nombre as \"Nombre\" from ventanas";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla ventanas: \n" + e.getMessage());
        }
    }
    public ResultSet datosCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nombre as dato, id_ventana from ventanas";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla ventanas: \n" + e.getMessage());
        }
    }
        public void update(Ventanas vent, int i) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
             baseDatos.createQuery("update Ventanas set "
                   +" nombre = '" + vent.getNombre() + "' where id_ventana = " +i+ "").executeUpdate();
            baseDatos.beginTransaction().commit();
         } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
         }
        
        }
}
