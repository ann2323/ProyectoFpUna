/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import modelo.Compra;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author anex
 */
public class FacturaCabeceraCompraControlador {
     public void insert(Compra compraC) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(compraC);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar compra - cabecera: \n" + e.getMessage());
        }
    }
     
     public void updateEstado(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Compra set estado = 'CONFIRMADO' where nro_factura = '" +nro_factura+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar el estado de la factura de compra: \n" + e.getMessage());
        }
    }
     public void updateEstadoPendiente(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Compra set estado = 'PENDIENTE' where nro_factura = '" +nro_factura+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar el estado de la factura de compra: \n" + e.getMessage());
        }
    }
     public ResultSet datosTablaBusqueda2(int codigo) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(fecha,'dd/mmwhere est/yyyy') as \"Fecha\", pago_contado as \"Forma de pago\", cantidad_total as \"Cantidad Total\", precio_total as \"Total\", estado as \"Estado\" from Compra where es_factura = 'S' and proveedor_id = '" + codigo + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
        }
    
    public String Vencimiento(Date date, int cuotas) throws SQLException, Exception
    {
   Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        String res="";
        try {
        String query = "SELECT TO_CHAR(calcular_vencimiento(CAST('" +date+ "' as date), CAST('" +cuotas+ "' as integer)),'dd/mm/yyyy');";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
        res=rs.getString(1);
        }
           
        } catch(HibernateException e){
            throw new Exception("Error al consultar el vencimiento de pago: \n" + e.getMessage());
        }
         return res;
    }
    
    public void updateEstadoAnulado(int nroFactura) throws Exception {
          Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update Compra set estado = 'ANULADO' where nro_factura = '" +nroFactura+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular factura de compra: \n" + e.getMessage());
            }
    }
    
     public ResultSet getNroFactura() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo as \"Nro. Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro. Factura\", v.proveedor_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.compra_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0), to_char(v.fecha_recepcion,'dd/mm/yyyy'), v.fact_referenciada, v.timbrado from Compra v where v.estado = 'BORRADOR'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
     
     public ResultSet getNroFacturaPagadas() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.proveedor_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.compra_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0), to_char(v.fecha_recepcion,'dd/mm/yyyy'), v.fact_referenciada, v.timbrado from Compra v where v.estado = 'PENDIENTE' or v.estado = 'CONFIRMADO'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
    
    public ResultSet getNroFacturaPagadas2() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.proveedor_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.compra_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0), to_char(v.fecha_recepcion,'dd/mm/yyyy'), v.fact_referenciada, v.timbrado from Compra v where v.estado = 'PAGADO' or v.estado='PENDIENTE'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
        }
    }
    public ResultSet datosBusqueda() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as Fecha, pago_contado as \"Forma de pago\", trim(to_char(cast(precio_total as integer),'9G999G999')) as \"Total\", estado as \"Estado\" from Compra where estado != 'ANULADO' and estado != 'BORRADOR'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
    }
    
     public ResultSet datosComboSaldo(Integer provId) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo, nro_factura as \"NroFactura\" from Compra where es_factura='N' and proveedor_id= '" +provId+ "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
    }
     
      public ResultSet datosComboSaldoNotaCredito() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo, nro_factura as \"NroFactura\" from Compra where es_factura='N'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
    }
    
     public ResultSet datosTablaBusquedaFacturasPendientes(Integer idProv) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro Factura\", to_char(vencimiento,'dd/mm/yyyy') as \"FechaVenc\", trim(to_char(cast(precio_total as integer),'9G999G999')) as \"Total\" from Compra where es_factura = 'S' and (estado = 'PENDIENTE' or estado = 'CONFIRMADO') and proveedor_id='" + idProv + "' order by vencimiento desc";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
        }
    public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(compra_id), 0) + 1 from Compra").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código Cabecera: \n" + e.getMessage());
        }
    }
    public ResultSet datoCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        //SELECT codigo || ' - ' || nombre as dato, codigo from Deposito

        String query = "SELECT moneda_id, nombre from moneda";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla moneda \n" + e.getMessage());
        }
    }
    public String getDate(Integer nro_factura){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
            return (String) baseDatos.createQuery("select to_char(fecha,'dd/mm/yyyy') as fechaCompra from Compra where nro_factura = '" + nro_factura + "'").uniqueResult();
    }

    public Integer getMonto(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select precioTotal from Compra where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer precio total: \n" + e.getMessage());
        }
    }
    public Integer getMontoNotaCredito(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select precioTotal from Compra where nro_factura = '" + nro_factura + "' and es_factura='N'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer precio total: \n" + e.getMessage());
        }
    }
    
     public Integer getCuota(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select pagoEn from Compra where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al obtener cuotas: \n" + e.getMessage());
        }
    }
    
     public ResultSet datosTablaBusqueda(int codigo) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(fecha,'dd/mmwhere est/yyyy') as \"Fecha\", pago_contado as \"Forma de pago\", cantidad_total as \"Cantidad Total\", precio_total as \"Total\" from Compra where proveedor_id = '" + codigo + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
        }
     
     public ResultSet datosBusquedaNotaCreditoCompra() throws SQLException, Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", trim(to_char(cast(nro_factura as integer),'9G999G999')) as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as Fecha, trim(to_char(cast(precio_total as integer),'9G999G999')) as \"Total\", estado as \"Estado\", trim(to_char(cast(fact_referenciada as integer),'9G999G999')) as \"Factura Referenciada\" from Compra where es_factura = 'N' and estado != 'ANULADO' and estado != 'BORRADOR'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
    }
    
     public void updateEstadoAnuladoNotaCreditoC(int nroNotaCredito) throws Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update Compra set estado = 'ANULADO' where nro_factura = '" +nroNotaCredito+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular nota crédito de compra: \n" + e.getMessage());
            }
    }
     
     public ResultSet busquedaNotaCreditoCompra(int codigoProveedor) throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT c.nro_prefijo as \"Nro Prefijo\", c.nro_factura as \"Nro Nota de Crédito\", c.fact_referenciada as \"Factura Referenciada\", to_char(fecha,'dd/mm/yyyy') as \"Fecha\", m.nombre as \"Tipo de pago\", c.cantidad_total as \"Cantidad Total\", c.precio_total as \"Total\", c.estado as \"Estado\" from Compra c, Moneda m where m.moneda_id = c.moneda_id and es_factura = 'N' and proveedor_id = '" + codigoProveedor + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Compra: \n" + e.getMessage());
            }
    
    }
     
     public void cambiarAEnProceso(String pry){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            if (Integer.parseInt(baseDatos.createQuery("select count(*) from Proyectos where codigo = '" + pry + "'").uniqueResult().toString().trim()) > 0){
                if (Integer.parseInt(baseDatos.createQuery("select count(*) from Entrada where proyecto = '" + pry + "'").uniqueResult().toString().trim()) == 0){
                    if (Integer.parseInt(baseDatos.createQuery("select count(*) from Salida where proyecto = '" + pry + "'").uniqueResult().toString().trim()) == 0){
                        if (Integer.parseInt(baseDatos.createQuery("select count(*) from Compra where proyecto_id = (select id from Proyectos where codigo = '" + pry + "') ").uniqueResult().toString().trim()) == 0){
                                baseDatos.createQuery("update Proyectos set estado = 1 where codigo = '" + pry + "'").executeUpdate();
                        }
                    }
                }
            }
    }
     
     public Integer devuelveId(Integer nroFactura) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT compra_id from Compra where nro_factura = '" + nroFactura + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de compra: \n" + e.getMessage());
        } 
     }
     
     public void borrarCabecera(Integer nroFactura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try{
            baseDatos.createQuery("Delete from Compra where nro_factura = '" + nroFactura + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        }catch(HibernateException e){
            throw new Exception("Error al eliminar cabecera de compra: \n" + e.getMessage());
        }
    }
     
     public void updateEstadoPagado(int nroFactura) throws Exception {
          Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update Compra set estado = 'PAGADO' where nro_factura = '" +nroFactura+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular factura de compra: \n" + e.getMessage());
            }
    }
     
   public String esContado(Integer nroFactura) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT pago_contado from Compra where nro_factura = '" + nroFactura + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver pago contado: \n" + e.getMessage());
        } 
   }
   
   public String getDeposito(Integer nro_factura, String nro_prefijo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (String) baseDatos.createQuery("select codDeposito from Compra where nro_factura = '" + nro_factura + "' and nro_prefijo = '" + nro_prefijo + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer el codigo deposito: \n" + e.getMessage());
        }
    }
}

    

