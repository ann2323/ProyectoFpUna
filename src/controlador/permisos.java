/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

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
}
 
