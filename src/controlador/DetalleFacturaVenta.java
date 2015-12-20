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
import modelo.DetalleVenta;
import modelo.Venta;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class DetalleFacturaVenta {
     public void insert(DetalleVenta ventaD) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(ventaD);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar venta - detalle: \n" + e.getMessage());
        }
    }
     
     public void update(DetalleVenta ventaD) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update DetalleVenta set "
                   +" cantidad = '" + ventaD.getCantidad() + "', sub_total = '" + ventaD.getSubTotal()  + "', descripcion = '" + ventaD.getDescripcion() + "'"
                    + ", venta_id = '" +  ventaD.getVentaId() + "', codigo = '" +  ventaD.getCodigo() + "', precio_unitario = '" + ventaD.getPrecioUnitario() +  "'"                   
            + ", exentas = '" + ventaD.getExentas() + "' where venta_id = '" + ventaD.getVentaId() + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
            baseDatos.flush();
            baseDatos.close();
       } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al actualizar detalle venta", "Error", JOptionPane.ERROR_MESSAGE);
             
        }catch(HibernateException e){
            e.getMessage();
        }
     }
       public void borrarDetalle(Integer idventa) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try{
            baseDatos.createQuery("Delete from DetalleVenta where venta_id = '" + idventa + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        }catch(HibernateException e){
            throw new Exception("Error al eliminar detalle de venta: \n" + e.getMessage());
        }
    }
     
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Cod Articulo\", 0 as \"Descripcion\", 0 as \"Precio Unit\", 0 as \"Cantidad\", 0 as \"Exento\", 0 as \"Sub Total\"";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta-Detalle: \n" + e.getMessage());
        }
    }
     public Integer nuevaLinea() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(detalle_factura_id), 0) + 1 from DetalleVenta").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código detalle - venta: \n" + e.getMessage());
        }
    }

    public ResultSet getDetalle(int idventa) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Codigo\" ,descripcion  \"Descripcion\" , precio_unitario as \"Precio unitario\", cantidad as \"Cantidad\", exentas as \"Exentas\", coalesce(sub_total, 0) as \"Subtotal\" from detalle_venta where venta_id='"+idventa+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta \n" + e.getMessage());
        }
    }
     
   public ResultSet getDetalleFactura(int idventa) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Codigo\" ,descripcion  \"Descripcion\" , precio_unitario as \"Precio unitario\", cantidad as \"Cantidad\", exentas as \"Exentas\", coalesce(sub_total, 0) as \"Subtotal\" from detalle_venta where venta_id='"+idventa+"' and nota_credito is null";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
   
    public void updateFactura(int idVenta, String cmp) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update DetalleVenta set "
                   +" nota_credito = 'S' where venta_id = '" +idVenta+ "' and codigo = '" +cmp+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
  
}
