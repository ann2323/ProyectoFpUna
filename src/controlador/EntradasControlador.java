/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Entrada;
import modelo.EntradaDetalle;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class EntradasControlador {
     public void insert(Entrada ent) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(ent);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar entrada - cabecera: \n" + e.getMessage());
        }
    }
     
    public void insert(EntradaDetalle entD) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(entD);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar entrada - detalle: \n" + e.getMessage());
        }
    }
     public void cambiarAEnProceso(String pry){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            if (Integer.parseInt(baseDatos.createQuery("select count(*) from Proyectos where codigo = '" + pry + "'").uniqueResult().toString().trim()) > 0){
                if (Integer.parseInt(baseDatos.createQuery("select count(*) from Entrada where proyecto = '" + pry + "'").uniqueResult().toString().trim()) == 0){
                    if (Integer.parseInt(baseDatos.createQuery("select count(*) from Salida where proyecto = '" + pry + "'").uniqueResult().toString().trim()) == 0){
                        if (Integer.parseInt(baseDatos.createQuery("select count(*) from Compra where proyecto_id = (select id from Proyectos where codigo = '" + pry + "') ").uniqueResult().toString().trim()) == 0){
                                baseDatos.createQuery("update Proyectos set estado = 1 where codigo = '" + pry + "'").executeUpdate();
                        }
                    }
                }
            }
    }
    public void update(Entrada ent) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(ent);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar entrada - cabecera: \n" + e.getMessage());
        }
    }
    
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(codigoInterno), 0) + 1 from Entrada").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código interno: \n" + e.getMessage());
        }
    }
    
    public Integer nuevaLinea() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(line), 0) + 1 from EntradaDetalle").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código interno: \n" + e.getMessage());
        }
    }
    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo_interno, numero, fecha_ingreso, comentarios, ajuste, deposito, proyecto " +
                    "from Entrada order by codigo_interno";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Entrada: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosDetalle(int cod) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Codigo\", descripcion as \"Descripcion\", ltrim(to_char(precio,'9G999G999G999')) as \"Precio\", ltrim(to_char(cantidad,'9G999G999G999')) as \"Cantidad\" " +
                    "from entrada_detalle where codigo_interno = " + cod;
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Entrada: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Codigo\", 0 as \"Descripcion\", 0 as \"Precio\", 0 as \"Cantidad\"";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Entrada: \n" + e.getMessage());
        }
    }
}
