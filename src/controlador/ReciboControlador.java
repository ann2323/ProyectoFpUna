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
public class ReciboControlador {
    
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
        String query = "SELECT nro_prefijo as \"Nro Prefijo\" , nro_factura  \"Nro Factura\" , fecha_vencimiento as \"Fecha vencimiento\", plazo as \"Plazo\", total \"Total\", monto_pendiente \"Monto Pendiente\", estado \"Estado\" from factura_pendiente where nro_factura='"+nro_factura+"' and estado='PENDIENTE' or estado='CONFIRMADO'";
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
     
}
