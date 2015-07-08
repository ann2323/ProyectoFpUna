/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.CuentaCabecera;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */


public class CuentaCabeceraControlador {
     public void updateEstadoAnulado(int nroFactura) throws Exception {
          Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update CuentaCabecera set estado = 'ANULADO' where nro_factura = '" +nroFactura+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular cuenta cabecera: \n" + e.getMessage());
            }
    }
    
     public void insert(CuentaCabecera cmp) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(cmp);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar cuenta: \n" + e.getMessage());
        }
    }
    
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(cuentaId), 0) + 1 from CuentaCabecera").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código Cabecera: \n" + e.getMessage());
        }
    }
    public ResultSet getFacturasPendientes(Integer ci_id) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nro_factura as \"Facturas Pendientes\" , to_char(fecha,'dd/mm/yyyy') \"Fecha de Compra\" , replace(trim(to_char(precio_total,'9,999,999')),',','.') as \"Monto Total\" from Compra where proveedor_id='"+ci_id+"' and estado!='PAGADO'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
    
     public ResultSet getFacturasPendientesClientes(Integer ci_id) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nro_factura as \"Facturas Pendientes\" , to_char(fecha,'dd/mm/yyyy') \"Fecha de Venta\" , replace(trim(to_char(precio_total,'9,999,999')),',','.') as \"Monto Total\" from Venta where cliente_id='"+ci_id+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
    public ResultSet getFacturaActual(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nro_factura as \"Facturas Actual\" , to_char(fecha,'dd/mm/yyyy') \"Fecha de Compra\" , replace(trim(to_char(precio_total,'9,999,999')),',','.') as \"Monto Total\" from Compra where nro_factura='"+nro_factura+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
    
    
    public ResultSet getFacturaActualCliente(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT nro_factura as \"Facturas Actual\" , to_char(fecha,'dd/mm/yyyy') \"Fecha de Venta\" , replace(trim(to_char(precio_total,'9,999,999')),',','.') as \"Monto Total\" from Venta where nro_factura='"+nro_factura+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
    public ResultSet getNroPago() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT cc.fecha as \"Fecha\", cc.nro_pago as \"NroPago\", pro.ci as \"CI\", pro.nombre as \"Proveedor\", cc.proyecto_id as \"Proyecto\", mon.nombre as \"MedioPago\", cc.nro_factura as \"FacturaNro\", cc.total_saldo as \"Saldo\" from cuenta_cabecera cc, proveedor pro, moneda mon  where cc.proveedor_id=pro.proveedor_id and cc.moneda_id=mon.moneda_id and cc.estado!='ELIMINADO'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    
      public ResultSet getNroPagoClientes() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT to_char(fecha,'dd/mm/yyyy') as \"Fecha\", cc.nro_pago as \"NroPago\", pro.cedula as \"CI\", pro.nombre as \"Cliente\", cc.proyecto_id as \"Proyecto\", mon.nombre as \"MedioPago\", cc.nro_factura as \"FacturaNro\", cc.total_saldo as \"Saldo\" from cuenta_cabecera cc, cliente pro, moneda mon  where cc.cliente_id=pro.cliente_id and cc.moneda_id=mon.moneda_id and cc.estado!='ELIMINADO'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    public Integer getNroPagoDB(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select nroPago from CuentaCabecera where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer el nro de pago: \n" + e.getMessage());
        }
    }
    public ResultSet getCuotasPendientes(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT to_char(fecha_pago,'dd/mm/yyyy') as \"Fecha de Pago\" , cuotas \"Cuotas\" , case when monto_abonado is null then '0' else replace(trim(to_char(monto_abonado,'9,999,999')),',','.') end as \"Monto Abonado\", estado as \"Estado\"  from detalle_cuenta where nro_factura='"+nro_factura+"'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla cuenta: \n" + e.getMessage());
        }
    }
     public Integer getMonto(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select totalSaldo from CuentaCabecera where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer precio total: \n" + e.getMessage());
        }
    }
       public void update(CuentaCabecera cuenta) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
          try {
            baseDatos.createQuery("update CuentaCabecera set "
                   +" fecha = '" + cuenta.getFecha()+"'"
                    + ", moneda_id = '" +  cuenta.getMonedaId()+ "'"                   
             + ", nro_pago = " + cuenta.getNroPago()+ ", total_saldo = " + cuenta.getTotalSaldo()+ " where nro_factura = " + cuenta.getNroFactura() + "").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
     public void delete(Integer nro_pago) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update CuentaCabecera set estado = 'ELIMINADO' where nro_pago = '" +nro_pago+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar proveedor: \n" + e.getMessage());
        }
    }
     
     public void updateSaldo(Integer facturaCompra, Integer IdProveedor, Integer monto) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update CuentaCabecera set total_saldo = total_saldo - " + monto + " where nro_factura = '" + facturaCompra + "' and proveedor_id = '" + IdProveedor + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar Stock: \n" + e.getMessage());
        }
    }
    public void updateSaldoCuenta(Integer facturaCompra, Integer IdProveedor, Integer monto) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update CuentaCabecera set total_saldo = total_saldo + " + monto + " where nro_factura = '" + facturaCompra + "' and proveedor_id = '" + IdProveedor + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar Stock: \n" + e.getMessage());
        }
    }
    
    public int devuelveId(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("Select cuentaId from CuentaCabecera where nro_factura='"+nro_factura+"'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al modificar proveedor: \n" + e.getMessage());
        }
    }
    
     
    
     public void updateSaldoVenta(Integer facturaCompra, Integer IdProveedor, Integer monto) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update CuentaCabecera set total_saldo = total_saldo - " + monto + " where nro_factura = '" + facturaCompra + "' and proveedor_id = '" + IdProveedor + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar saldo cuenta cliente: \n" + e.getMessage());
        }
    }

    public ResultSet consultarCuentaCliente(int codigoCliente) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT total_saldo as \"Saldo Total de la Cuenta\", estado as \"Estado\" from cuenta_cabecera where cliente_id = '" + codigoCliente + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla cuenta: \n" + e.getMessage());
            }
    }

     public ResultSet consultarCuentaProveedor(int codigoProveedor) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT total_saldo as \"Saldo Total de la Cuenta\", estado as \"Estado\" from cuenta_cabecera where proveedor_id = '" + codigoProveedor + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla cuenta: \n" + e.getMessage());
            }
    }
}
