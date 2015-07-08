/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Componentes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class ComponentesControlador {
    
    public void insert(Componentes cmp) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(cmp);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar componente: \n" + e.getMessage());
        }
    }
    
    public void update(Componentes cmp) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(cmp);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar componente: \n" + e.getMessage());
        }
    }
    
    public void delete(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("delete from Componentes where codigo = '" + codigo + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar componente: \n" + e.getMessage());
        }
    }
    
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(codigoInterno), 0) + 1 from Componentes").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código interno: \n" + e.getMessage());
        }
    }
    
    public String getDescripcion(String codigo){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
            return (String) baseDatos.createQuery("select descripcion from Componentes where codigo = '" + codigo + "'").uniqueResult();
    }
    
    public Integer getCosto(String codigo){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
            return (Integer) baseDatos.createQuery("select costo from Componentes where codigo = '" + codigo + "'").uniqueResult();
    }
    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigo as \"Código\", unidad as \"Unidad de medida\", descripcion as \"Descripción\", estado as \"Estado\", " +
                    "precio as \"Precio\", codigo_interno, id_proveedor, costo, tipo_iva from Componentes order by codigo_interno";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosStock(String CodigoComponente) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "Select cod_deposito as \"Deposito\", c.descripcion as \"Descripcion\",ltrim(to_char(cantidad,'9G999G999G999')) as \"Cantidad\" From Stock a ";
                query += "inner join Componentes b on b.codigo = a.cod_componente " ;
                query += "inner join Deposito c on c.codigo = a.cod_deposito where cod_componente = '" +CodigoComponente+ "'" ;
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Stock: \n" + e.getMessage());
        }
    }
    
    public boolean tieneStock(String CodigoComponente) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        try {
            double resultado1 = (double) baseDatos.createQuery("select coalesce(sum(cantidad),0) as cantidad from Stock where cod_componente = '" +CodigoComponente+ "'").uniqueResult();
            if (resultado1 > 0.0) {
                return true;
            } else {
                return false;
            }
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Stock: \n" + e.getMessage());
        }
    }
    
    public ResultSet getComponentesDatos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT codigo \"Codigo\", descripcion as \"Descripcion\", precio as \"Precio\" from Componentes";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    
     public Integer getTipoIva(String codigo){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        return (Integer) baseDatos.createQuery("select tipoIva from Componentes where codigo = '" + codigo + "'").uniqueResult();
    }

     public String getExento(String codigo){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        if (codigo != "PRY"){ 
            int valor = (int) baseDatos.createQuery("select tipoIva from Componentes where codigo = '" + codigo + "'").uniqueResult();
            if (valor == 2) {
                return "S";
            } else {
                return "N";
            }
        } else{
            return "N";
        }
            
    }

}
