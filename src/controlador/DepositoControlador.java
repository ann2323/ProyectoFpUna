/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Deposito;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class DepositoControlador {
    
    public void insert(Deposito dep) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(dep);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar deposito: \n" + e.getMessage());
        }
    }
    
    public void update(Deposito dep) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(dep);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar deposito: \n" + e.getMessage());
        }
    }
    
    public void delete(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("delete from Deposito where codigo = '" + codigo + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar deposito: \n" + e.getMessage());
        }
    }
    
    public String getCodigo(String dato) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT codigo from Deposito where codigo || ' - ' || nombre = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Deposito: \n" + e.getMessage());
        }
    }
    
    public String getDato(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT codigo || ' - ' || nombre as valor from Deposito where codigo = '" + codigo + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Deposito: \n" + e.getMessage());
        }
    }
    
    
    
    public ResultSet datoCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();

        String query = "SELECT codigo || ' - ' || nombre as dato, codigo from Deposito";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Deposito: \n" + e.getMessage());
        }
    }
    
    public ResultSet datoComboFactura() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        //SELECT codigo || ' - ' || nombre as dato, codigo from Deposito

        String query = "SELECT codigo, nombre, descripcion from deposito";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Deposito: \n" + e.getMessage());
        }
    }

    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Código\", nombre as \"Nombre\", descripcion as \"Descripción\" from Deposito";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Deposito: \n" + e.getMessage());
        }
    }
    
    //metodo que devuelve el nombre de deposito al pasarle el codigo
    //Metodo utilizado en la busqueda de factura
    public String getDeposito(String codDeposito) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT nombre from Deposito where codigo = '" + codDeposito + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar deposito: \n" + e.getMessage());
        } 
    }
    
    
    //metodo para la consulta de stock
    public ResultSet getDepositos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Cod. Deposito\" from deposito";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Deposito: \n" + e.getMessage());
        }
    }

   
}
