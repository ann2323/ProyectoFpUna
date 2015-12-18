/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Patricia Espinola
 */
public class permisos{

    public Long tiene(String ventana, Integer rol) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        baseDatos.flush();
       
        try {
            Integer x = (Integer) baseDatos.createQuery("select idVentana from Ventanas where nombre = '" + ventana + "'").uniqueResult();
           return (Long) baseDatos.createQuery("select count(*) as valor from RolVentanas where idVentana = " + x + " and rolId = " + rol).uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al consultar permiso: \n" + e.getMessage());
        }
    }

  
     public Integer codJefe() throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "Select id_rol from rol where lower(nombre) = 'jefe'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver id rol: \n" + e.getMessage());
        } 
    }
}
 
