/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.DetallePago;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;



/**
 *
 * @author Any
 */
public class DetallePagoControlador {
     public void insert(DetallePago pagos) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(pagos);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar detalle de pago: \n" + e.getMessage());
        }
        baseDatos.close();
    }
    public Integer nuevaCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(detalle_pago_id), 0) + 1 from DetallePago").uniqueResult();
            
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código detalle de pago: \n" + e.getMessage());
        }        
    }

      public long verificarEstadoFactura(Integer nroFactura) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "select count(nro_nota_credito) from detalle_pago where nro_nota_credito = " + nroFactura + "";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (long) rs.getObject(1);
            
        } catch(HibernateException e){
            throw new Exception("Error al verificar existencia en detalle de pago: \n" + e.getMessage());
        } 
        
     }
  
    public Integer getPendienteAplicar(int nroFactura) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();   
        String cad = "SELECT distinct coalesce(min(pendiente_a_aplicar),0) from detalle_pago where nro_nota_credito = '" + nroFactura + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver monto pendiente: \n" + e.getMessage());
        } 
   } 
}
