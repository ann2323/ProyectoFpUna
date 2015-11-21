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
        } 
}
