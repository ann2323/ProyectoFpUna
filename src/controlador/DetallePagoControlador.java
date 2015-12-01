/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import modelo.DetallePago;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;



/**
 *
 * @author Any
 */
public class DetallePagoControlador {
     public void insert(DetallePago pagos) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(pagos);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar detalle de pago: \n" + e.getMessage());
        }
        baseDatos.close();
    }
    public Integer nuevaCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(detalle_pago_id), 0) + 1 from DetallePago").uniqueResult();
            
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código detalle de pago: \n" + e.getMessage());
        }
        
   
    }
    
}
