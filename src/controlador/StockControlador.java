/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Stock;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class StockControlador {
    
    public void insert(Stock stock) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(stock);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar componente: \n" + e.getMessage());
        }
    }
    
    public void delete(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("delete from Stock where cod_componente = '" + codigo + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar Stock: \n" + e.getMessage());
        }
    }
    
    public void update(String codigo, String deposito, Integer cantidad) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Stock set cantidad = cantidad + " + cantidad + " where cod_componente = '" + codigo + "' and cod_deposito = '" + deposito + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar Stock: \n" + e.getMessage());
        }
    }
    
    public void update2(String codigo, String deposito, Integer cantidad) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Stock set cantidad = cantidad - " + cantidad + " where cod_componente = '" + codigo + "' and cod_deposito = '" + deposito + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar Stock: \n" + e.getMessage());
        }
    }
    
    public Long tieneCodStock(String codigo, String deposito) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            return (Long) baseDatos.createQuery("select count(cod_componente) from Stock where cod_componente = '" + codigo + "' and cod_deposito = '" + deposito + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al calcular Stock: \n" + e.getMessage());
        }
    }
    
    public Integer tieneStock(String codigo, String deposito) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce(cantidad,0) from Stock where cod_componente = '" + codigo + "' and cod_deposito = '" + deposito + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al calcular Stock: \n" + e.getMessage());
        }
    }
    
     public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce(max(line),0) + 1 from Stock").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al calcular Nuevo codigo para stock: \n" + e.getMessage());
        }
    }
     
     public ResultSet getStock(String codDeposito) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "Select s.cod_componente as \"Código Componente\", c.descripcion as \"Descripción\", s.cantidad as \"Cantidad\" from Stock s, Componentes c, Deposito d where s.cod_componente = c.codigo and s.cod_deposito = '" + codDeposito + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar stock: \n" + e.getMessage());
        } 
    }
}
