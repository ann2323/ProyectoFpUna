/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.FacturaPendiente;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Pathy
 */
public class FacturaPendienteControlador {
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(factura_pendiente_id), 0) + 1 from FacturaPendiente").uniqueResult();
            
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código de factura pendiente: \n" + e.getMessage());
        }
        
    }

    public ResultSet getFacturasPendientes(int idCliente) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT nro_prefijo as \"Nro Prefijo\" , nro_factura  \"Nro Factura\" , fecha_vencimiento as \"Fecha vencimiento\", cuota as \"Cuota\", total \"Total\", estado \"Estado\" from factura_pendiente where cliente_id='"+idCliente+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Factura Pendiente: \n" + e.getMessage());
        }
    }
        
        public void insert(FacturaPendiente factPendiente) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.save(factPendiente);
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al guardar factura pendiente: \n" + e.getMessage());
            }
            baseDatos.close();
        } 
        
       public void borrarFacturasPendientes(Integer nroFactura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try{
            baseDatos.createQuery("Delete from FacturaPendiente where nro_factura = '" + nroFactura + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        }catch(HibernateException e){
            throw new Exception("Error al eliminar factura pendiente de compra: \n" + e.getMessage());
        }
      }
        
     public void updateFacturaPendiente(Integer pagado, Integer pendiente, Integer cambio, Integer reciboid, Integer factura, String plazo) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update FacturaPendiente set "
                   +" pagado = '" + pagado + "', pendiente = '" + pendiente + "'"
                    + ", cambio = '" +  cambio + "', recibo_id = '" +reciboid+  "'"
                    +", estado = 'PAGADO' where nro_factura='" +factura+"' and plazo = '" +plazo+"'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar factura pendiente: \n" + e.getMessage());
        }
        baseDatos.close();
    }
     
    public void updateFacturaPendienteContado(Integer pagado, Integer pendiente, Integer cambio, Integer reciboid, Integer factura) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update FacturaPendiente set "
                   +" pagado = '" + pagado + "', pendiente = '" + pendiente + "'"
                    + ", cambio = '" +  cambio + "', recibo_id = '" +reciboid+  "'"
                    +", estado = 'PAGADO' where nro_factura='" +factura+"'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar factura pendiente: \n" + e.getMessage());
        }
        baseDatos.close();
    }
             
      public Integer devuelveIdProv(Integer nroFactura, String plazo, Integer provId) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT factura_pendiente_id from factura_pendiente where nro_factura = '" + nroFactura + "' and plazo = '" + plazo + "' and proveedor_id = '" + provId + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de compra: \n" + e.getMessage());
        } 
     }
      
     public Integer devuelveIdCli(Integer nroFactura, String plazo, Integer cliId) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT factura_pendiente_id from factura_pendiente where nro_factura = '" + nroFactura + "' and plazo = '" + plazo + "' and cliente_id = '" + cliId + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de compra: \n" + e.getMessage());
        } 
     }
     
      public Integer devuelveIdProvContado(Integer nroFactura, Integer provId) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT factura_pendiente_id from factura_pendiente where nro_factura = '" + nroFactura + "' and proveedor_id = '" + provId + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver id factura pendiente: \n" + e.getMessage());
        } 
     }
      
     public long verificarEstadoFacturaPendientes(Integer nroFactura) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "select count(estado) from factura_pendiente where nro_factura = " + nroFactura + " and estado='PAGADO'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (long) rs.getObject(1);
            
        } catch(HibernateException e){
            throw new Exception("Error al verificar existencia en detalle de pago: \n" + e.getMessage());
        } 
        
     }
     
}
