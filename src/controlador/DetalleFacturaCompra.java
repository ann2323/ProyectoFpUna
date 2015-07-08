/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.DetalleCompra;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class DetalleFacturaCompra {
     public void insert(DetalleCompra compraD) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(compraD);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar compra - detalle: \n" + e.getMessage());
        }
    }
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Cod Articulo\", 0 as \"Descripcion\", 0 as \"Precio Unit\", 0 as \"Cantidad\", 0 as \"Excentas\", 0 as \"Sub Total\"";
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
            return (Integer) baseDatos.createQuery("select coalesce (max(detalleCompraId), 0) + 1 from DetalleCompra").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código detalle - compra: \n" + e.getMessage());
        }
    }
   
}
