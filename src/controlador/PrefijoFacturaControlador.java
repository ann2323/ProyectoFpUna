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
import modelo.PrefijoFactura;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Pathy
 */
public class PrefijoFacturaControlador {
       public void insert(PrefijoFactura prefijo)    {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
         try {
            baseDatos.save(prefijo);
            baseDatos.beginTransaction().commit();
            baseDatos.flush();
            baseDatos.close();
         } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al insertar", "Error", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
        
    }
       public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(idprefijo), 0) + 1 from PrefijoFactura").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código prefijo: \n" + e.getMessage());
        }
    }
       
       
     public Integer primernrofactura() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT MAX(principiofactura) from prefijo_factura where tipo_documento = 'F'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener maximo id: \n" + e.getMessage());
        }
    }
 public int maximoIDPrefijo() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT MAX(idprefijo) from prefijo_factura where tipo_documento = 'F'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener maximo id: \n" + e.getMessage());
        }
    }
    
     public int maximoIDPrefijoNC() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT MAX(idprefijo) from prefijo_factura where tipo_documento = 'N'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener maximo id: \n" + e.getMessage());
        }
    }
 
      //Este método retorna el último prefijo ingresado en la tabla
    public String prefijoFactura() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT prefijo from prefijo_factura where tipo_documento = 'F' and idprefijo = '" + maximoIDPrefijo() + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener el prefijo de factura: \n" + e.getMessage());
        }
    }
    
     public String prefijoNotaCredito() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT prefijo from prefijo_factura where tipo_documento = 'N' and idprefijo = '" + maximoIDPrefijoNC() + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener el prefijo de factura: \n" + e.getMessage());
        }
    }
    
    
    //devuelve el fin de factura de la tabla prefijo
     public Integer finfactura() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT MAX(finfactura) from prefijo_factura where tipo_documento = 'F'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener el número final de factura: \n" + e.getMessage());
        }
    }
 public Integer finNotaC() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT MAX(finfactura) from prefijo_factura where tipo_documento = 'N'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al obtener el número final de nota de crédito: \n" + e.getMessage());
        }
    }

    public Long existeNroFinalFactura(int finalFactura) throws Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (finFactura) from PrefijoFactura where tipo_documento = 'F' and finfactura = '" + finalFactura+ "'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al obtener número final de factura: \n" + e.getMessage());
         }
      }
      
    
     public Long existeNroInicialFactura(int inicioFactura) throws SQLException, Exception{
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (principioFactura) from PrefijoFactura where tipo_documento = 'F' and principiofactura = '" + inicioFactura+ "'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al obtener número inicial de factura: \n" + e.getMessage());
         }
      }
     
      public Long existeNroFinalNotaCredito(int finalCredito) throws Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (finFactura) from PrefijoFactura where tipo_documento = 'N' and finfactura = '" + finalCredito+ "'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al obtener número final de nota de crédito: \n" + e.getMessage());
         }
      }
      
       public Long existeNroInicialNotaCredito(int principioCredito) throws Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (principioFactura) from PrefijoFactura where tipo_documento = 'N' and principiofactura = '" + principioCredito+ "'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al obtener número final de nota de crédito: \n" + e.getMessage());
         }
      }
     

    public int primernroNotaC() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT MAX(principiofactura) from prefijo_factura where tipo_documento = 'N'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch (HibernateException e) {
            throw new Exception("Error al consultar tabla Prefijo: \n" + e.getMessage());
        }
    }
}

