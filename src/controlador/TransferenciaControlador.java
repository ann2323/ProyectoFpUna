/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Transferencia;
import modelo.TransferenciaDetalle;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class TransferenciaControlador {
     public void insert(Transferencia trans) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(trans);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar transferencia - cabecera: \n" + e.getMessage());
        }
    }
     
    public void insert(TransferenciaDetalle transD) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(transD);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar transferencia - detalle: \n" + e.getMessage());
        }
    }
    
    public void update(Transferencia trans) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(trans);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar transferencia - cabecera: \n" + e.getMessage());
        }
    }
    
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(codigoInterno), 0) + 1 from Transferencia").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código interno: \n" + e.getMessage());
        }
    }
    
    public Integer nuevaLinea() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(line), 0) + 1 from TransferenciaDetalle").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código de linea: \n" + e.getMessage());
        }
    }
    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo_interno, numero, fecha_transferencia, comentarios, origen, destino " +
                    "from Transferencia order by codigo_interno";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Transferencia: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosDetalle(int cod) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Codigo\", descripcion as \"Descripcion\", LTRIM(to_char(cantidad, '9G999G999G999')) as \"Cantidad\" " +
                    "from transferencia_detalle where codigo_interno = " + cod;
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Transferencia detalle: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Codigo\", 0 as \"Descripcion\", 0 as \"Cantidad\" ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Entrada: \n" + e.getMessage());
        }
    }
}
