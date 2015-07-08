/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import modelo.Proyectos;
import modelo.ResponsablesProyecto;
import modelo.Temporal;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Grossling
 */
public class ProyectoControlador {
         public void insert(Proyectos pry) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(pry);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar proyecto: \n" + e.getMessage());
        }
    }
     
    public void insert(ResponsablesProyecto respP) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(respP);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar responsables - proyecto: \n" + e.getMessage());
        }
    }
    
    public void update(Proyectos pry) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(pry);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proyecto: \n" + e.getMessage());
        }
    }
    
     public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT id, codigo, solicitante, jefe, fecha_ini, fecha_fin, estado, presupuesto, descripcion, utilidad " +
                    "from Proyectos b";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Proyectos: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosAdjudicaciones(String cod_proyecto, String fecha) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query =  "SELECT " +
                        "b.codigo as \"Codigo\", " + 
                        "max(b.descripcion) as \"Descripcion\", " +
                        "ltrim(to_char(sum(b.cantidad), '9G999G999G999')) as \"Cantidad\", " +
                        "ltrim(to_char(sum(b.precio * b.cantidad), '9G999G999G999')) as \"Costo Total\" " +
                        "from salida a inner join salida_detalle b on a.codigo_interno = b.codigo_interno " +
                        "where a.proyecto = '" + cod_proyecto + "' ";
                        if (!fecha.toString().equals("")) {
                            query += "and a.fecha_ingreso = '" + fecha + "' ";
                        }
                        query += "group by b.codigo ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Salida - detalle: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosDevoluciones(String cod_proyecto, String fecha) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT " +
                        "b.codigo as \"Codigo\", " + 
                        "max(b.descripcion) as \"Descripcion\", " +
                        "ltrim(to_char(sum(b.cantidad), '9G999G999G999')) as \"Cantidad\", " +
                        "ltrim(to_char(sum(b.precio * b.cantidad), '9G999G999G999')) as \"Costo Total\" " +
                        "from entrada a inner join entrada_detalle b on a.codigo_interno = b.codigo_interno " +
                        "where a.proyecto = '" + cod_proyecto + "' ";
                        if (!fecha.toString().equals("")) {
                            query += "and a.fecha_ingreso = '" + fecha + "' ";
                        }
                        query += 
                        "group by b.codigo ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Entrada - detalle: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosGastos(String cod_proyecto, String fecha) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT " +
                        "b.codigo as \"Codigo\",  " +
                        "max(b.descripcion) as \"Descripcion\", " +
                        "ltrim(to_char(sum(b.cantidad), '9G999G999G999')) as \"Cantidad\", " +
                        "ltrim(to_char(sum(b.precio_unit * b.cantidad), '9G999G999G999')) as \"Costo Total\" " +
                        "from compra a inner join detalle_compra b on a.compra_id = b.compra_id where a.proyecto_id = (select x.id from proyectos x where x.codigo = '" + cod_proyecto + "' ) ";
                        if (!fecha.equals("")) {
                            query += "and a.fecha = '" + fecha + "' ";
                        }
                        query += "group by b.codigo ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compras - detalle: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosFacturaciones(String cod_proyecto, String fecha) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT " +
                        "to_char(a.fecha,'dd/MM/yyyy') as \"Fecha\",  " +
                        "a.nro_prefijo as \"Nro. Prefijo\",  " +
                        "a.nro_factura as \"Nro. Factura\",  " +
                        "ltrim(to_char(a.precio_total, '9G999G999G999')) as \"Precio Total\" " +
                        "from venta a inner join detalle_venta b on a.venta_id = b.venta_id where a.proyecto_id = (select x.id from proyectos x where x.codigo = '" + cod_proyecto + "' ) ";
                        if (!fecha.equals("")) {
                            query += "and a.fecha = '" + fecha + "' ";
                        }
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compras - detalle: \n" + e.getMessage());
        }
    }
    
    public ResultSet datosD() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT 0 as \"Responsables\"";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla ficticia: \n" + e.getMessage());
        }
    }
    
    public void guardarProyectoAFacturar(String proyecto, int precio) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        Temporal tmp = new Temporal();
        tmp.setPrecio(precio);
        tmp.setProyecto(proyecto);
        try {
            baseDatos.save(tmp);
            baseDatos.beginTransaction().commit();
        
        } catch (org.hibernate.exception.ConstraintViolationException cve) {
    
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al insertar", 
                     "Error: ", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
    }
    
    public String getProyectoAFacturar() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT proyecto from Temporal order by line desc limit 1";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
    
    public Integer getPrecioAFacturar() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT precio from Temporal order by line desc limit 1";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
    public Integer calcularPrecioAFacturar(String pry) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "select " +
                    "cast(((SELECT " +
                    "coalesce(cast(sum(b.precio * b.cantidad) as int),0) " +
                    "from salida a inner join salida_detalle b on a.codigo_interno = b.codigo_interno " +
                    "where a.proyecto = '" + pry + "') " +
                    "+ " +
                    "(SELECT " +
                    "coalesce(-cast(sum(b.precio * b.cantidad) as int),0) " +
                    "from entrada a inner join entrada_detalle b on a.codigo_interno = b.codigo_interno " +
                    "where a.proyecto = '" + pry + "') " +
                    "+ " +
                    "(SELECT " +
                    "coalesce(cast(sum(b.cantidad * b.precio_unit) as int),0) " +
                    "from compra a inner join detalle_compra b on b.compra_id = a.compra_id " +
                    "where a.proyecto_id = (select id from proyectos where codigo = '" + pry + "'))) " +
                    "* " +
                    "(SELECT coalesce(cast(utilidad as double precision)/100,1) from proyectos where codigo = '" + pry + "') as int) " +
                    "+ " +
                    "((SELECT " +
                    "coalesce(cast(sum(b.precio * b.cantidad) as int),0) " +
                    "from salida a inner join salida_detalle b on a.codigo_interno = b.codigo_interno " +
                    "where a.proyecto = '" + pry + "') " +
                    "+ " +
                    "(SELECT " +
                    "coalesce(-cast(sum(b.precio * b.cantidad) as int),0) " +
                    "from entrada a inner join entrada_detalle b on a.codigo_interno = b.codigo_interno " +
                    "where a.proyecto = '" + pry + "') " +
                    "+ " +
                    "(SELECT " +
                    "coalesce(cast(sum(b.cantidad * b.precio_unit) as int),0) " +
                    "from compra a inner join detalle_compra b on b.compra_id = a.compra_id " +
                    "where a.proyecto_id = (select id from proyectos where codigo = '" + pry + "'))) " +
                    "+ " +
                    "(SELECT " +
                    "coalesce(-cast(sum(b.cantidad * b.precio_unitario) as int),0) " +
                    "from venta a inner join detalle_venta b on b.venta_id = a.venta_id " +
                    "where a.proyecto_id = (select id from proyectos where codigo = '" + pry + "')) " +
                    "as valor ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (Integer) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
     public Integer getIdProyecto() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT coalesce(id,0) from proyectos where codigo = (select proyecto from Temporal order by line desc limit 1)";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar temporal: \n" + e.getMessage());
        }
    }
    
    public ResultSet datoCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        //SELECT codigo || ' - ' || nombre as dato, codigo from Deposito

        String query = "SELECT id, codigo from proyectos union all select null id,'' codigo";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Deposito: \n" + e.getMessage());
        }
    }
}
