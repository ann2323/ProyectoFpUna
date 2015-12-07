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
import modelo.Proveedor;
import modelo.Temporal;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 */
public class ProveedorControlador {
    
    public void insert(Proveedor prov) throws Exception {
      
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(prov);
            baseDatos.beginTransaction().commit();
        
        } catch (org.hibernate.exception.ConstraintViolationException cve) {
    
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al insertar", 
                     "Error: ", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
         baseDatos.close();
    }
    
   
     
    /*public void update(Proveedor prov) throws Exception{
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           
          
          baseDatos.update(prov);
        
         
          baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }*/
    
    public void update(Proveedor prov) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Proveedor set "
                   +" nombre = '" + prov.getNombre() + "', apellido = '" + prov.getApellido() + "'"
                    + ", ci = '" +  prov.getCi() + "', dv = '" +  prov.getDv()+  "', direccion = '" +  prov.getDireccion()+ "', telefono = '" + prov.getTelefono() +  "'"                   
             + ", estado = '" + prov.getEstado() + "' where cod_proveedor = '" + prov.getCodProveedor() + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
        baseDatos.close();
    }
    
     public ResultSet datosCombo() throws Exception {
         
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT proveedor_id, nombre ||' '|| apellido as \"Nombre\" from proveedor";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs1 = ps.executeQuery();
        try {
            return rs1;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla proveedor: \n" + e.getMessage());
        }
    }
     
    public void delete(String codigo_prov) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Proveedor set estado = 'INACTIVO' where cod_proveedor = '" + codigo_prov+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar proveedor: \n" + e.getMessage());
        }
        baseDatos.close();
    }
   
    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT cod_proveedor as \"Código\",  case when dv='' then substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7) else substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7)||'-'||dv end as \"RUC/CI\", nombre as \"Nombre\", apellido as \"Apellido\", direccion as \"Direccion\", telefono as \"Telefono\", estado as \"Estado\" from Proveedor order by cod_proveedor ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al obtener proveedor: \n" + e.getMessage());
        }
    }
    
    public Integer devuelveId(String ci) throws Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT proveedor_id from proveedor where ci = '" +ci+ "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver el id proveedor: \n" + e.getMessage());
        } 
     }
    
    public int devuelveId(String ci, String nombre) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("Select proveedorId from Proveedor where nombre = '" +nombre+ "' or ci='"+ci+"'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al devolver id del proveedor: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosBusqueda() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT ci as \"RUC/CI\", nombre||' '||apellido as \"Nombre\" from Proveedor";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al devolver datos del Proveedor: \n" + e.getMessage());
        }
    }
     public ResultSet datosBusqueda2() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT substring(ci from 1 for 1)||'.'||substring(ci from 2 for 3)||'.'||substring(ci from 5 for 7) as \"RUC/CI\", nombre||' '||apellido as \"Nombre\" from Proveedor";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al devolver datos del Proveedor: \n" + e.getMessage());
        }
    }
    public String getDato(Integer codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT cod_proveedor || ' - ' || nombre as valor from Proveedor where proveedor_id = " + codigo + "";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver datos del Proveedor: \n" + e.getMessage());
        }
    }
    
    public Integer getCodigo(String dato) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT proveedor_id from Proveedor where cod_proveedor || ' - ' || nombre = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Proveedor: \n" + e.getMessage());
        }
    }
    
    public void guardarProyectoAFacturar(String proyecto, int precio) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        Temporal tmp = new Temporal();
        tmp.setPrecio(precio);
        tmp.setProyecto(proyecto);
        try {
            baseDatos.save(tmp);
            baseDatos.beginTransaction().commit();
        
        } catch (org.hibernate.exception.ConstraintViolationException cve) {
    
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al insertar", 
                     "Error: ", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
        baseDatos.close();
    }
    
    public String getProyectoAFacturar() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT top 1 proyecto from Temporal order by line desc";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
    public Integer getPrecioAFacturar() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT top 1 precio from Temporal order by line desc";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
     public ResultSet datoCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();

        String query = "SELECT cod_proveedor || ' - ' || nombre as dato from Proveedor";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Proveedor: \n" + e.getMessage());
        }
    }
     
      public String getCedula(String dato) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT ci from Proveedor where proveedor_id = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Proveedor: \n" + e.getMessage());
        }
    }
      
     public String getNombreProveedor(String idProveedor) throws SQLException, Exception  {
       Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT nombre ||' '|| apellido as \"Nombre\" from Proveedor where proveedor_id = '" + idProveedor + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Proveedor: \n" + e.getMessage());
        } 
    }
}
   
