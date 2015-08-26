/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;


import modelo.SaldoCompra;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class SaldoCompraControlador {
     public void insert(SaldoCompra saldoCompra) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(saldoCompra);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar saldo - cabecera: \n" + e.getMessage());
        }
    }
     
     public void updateEstado(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Compra set estado = 'PAGADO' where nro_factura = '" +nro_factura+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar el estado de la factura de compra: \n" + e.getMessage());
        }
    }


    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(saldoCompraId), 0) + 1 from SaldoCompra").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código Cabecera de saldo: \n" + e.getMessage());
        }
    }

    public Integer getSaldo(String nro_prefijo, Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select saldo from SaldoCompra where numero = '" + nro_factura + "' and prefijo = '" + nro_prefijo + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer saldo total: \n" + e.getMessage());
        }
    }
    

    
        public String getEstado(String nro_prefijo, Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (String) baseDatos.createQuery("select estado from SaldoCompra where numero = '" + nro_factura + "' and prefijo = '" + nro_prefijo + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error el estado del saldo: \n" + e.getMessage());
        }
    }


}

    

