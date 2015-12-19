/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.CabeceraRecibo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Pathy
 */
public class ReciboProveedorControlador {
    
     public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(recibo_id), 0) + 1 from CabeceraRecibo").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código de recibo: \n" + e.getMessage());
        }
    }
     
     public ResultSet getFacturasPendientes(int nro_factura) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT nro_prefijo as \"Nro Prefijo\" , nro_factura  \"Nro Factura\" , fecha_vencimiento as \"Fecha vencimiento\", plazo as \"Plazo\", total \"Total\", monto_pendiente \"Monto Pendiente\", estado \"Estado\" from factura_pendiente where nro_factura='"+nro_factura+"' and estado='PENDIENTE' order by plazo";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Factura Pendiente: \n" + e.getMessage());
        }
    }
     
   public void insert(CabeceraRecibo recibo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(recibo);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar recibo: \n" + e.getMessage());
        }
        baseDatos.close();
    }
   
     public ResultSet recibosProveedor(Integer idProv) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_recibo as \"Nro Prefijo\", trim(to_char(cast(factura_nro as integer),'9G999G999')) as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as \"FechaVenc\" from cabecera_recibo where proveedor_id="+idProv +" order by fecha desc";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
        }
     
       public Integer getNroFactura(Integer nroRecibo) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT factura_nro from cabecera_recibo where nro_recibo = '" + nroRecibo + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de compra: \n" + e.getMessage());
        } 
     }
       
     public Integer devuelveId(Integer nroRecibo) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT recibo_id from cabecera_recibo where nro_recibo = '" + nroRecibo + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de recibo: \n" + e.getMessage());
        } 
     }
     
    public void updateFechaAnulacion(int idRecibo, String fechaAnulacion) throws Exception {
          Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update CabeceraRecibo set fecha_anulacion = '"+ fechaAnulacion +"'  where recibo_id = '" +idRecibo+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular factura de compra: \n" + e.getMessage());
            }
    }
}
