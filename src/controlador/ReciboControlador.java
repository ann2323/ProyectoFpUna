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
 * @author Pathy
 */
public class ReciboControlador {
    
     public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(id_cabecera_recibo), 0) + 1 from CabeceraRecibo").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código de recibo: \n" + e.getMessage());
        }
    }
}
