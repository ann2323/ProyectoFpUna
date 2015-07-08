/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.DetalleCuenta;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class DetalleCuentaControlador {
      public void insert(DetalleCuenta cmp) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(cmp);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar cuenta-detalle: \n" + e.getMessage());
        }
    }
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Nro Factura\", 0 as \"Fecha\", 0 as \"Monto\", 0 as \"Cuota\",0 as \"Monto Abonado\", 0 as \"Estado\"";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra-Detalle: \n" + e.getMessage());
        }
    }
    public Integer nuevaLinea() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(idDetalleCuenta), 0) + 1 from DetalleCuenta").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código detalle - cuenta: \n" + e.getMessage());
        }
    }
     public void update(DetalleCuenta cuenta) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update DetalleCuenta set "
                   +" monto_abonado = " + cuenta.getMontoAbonado()+""
                    + ", cuenta_id = " +  cuenta.getCuentaId()+ ", fecha_pago = '" +  cuenta.getFechaPago()+  "'"                   
             + ", estado = '" + cuenta.getEstado() + "' where nro_factura = " + cuenta.getNroFactura() + " and cuotas=  " + cuenta.getCuotas() + "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
   
    public void updateEstado(DetalleCuenta det) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update DetalleCuenta set "                  
             + "estado = 'ANULADO' where nro_factura = " + det.getNroFactura() + " and cuotas=  " + det.getCuotas() + "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
    
    public void updateEstadoAnulado(int nroFactura) throws Exception {
          Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update DetalleCuenta set estado = 'ANULADO' where nro_factura = '" +nroFactura+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular cuenta detalle: \n" + e.getMessage());
            }
    }
}
