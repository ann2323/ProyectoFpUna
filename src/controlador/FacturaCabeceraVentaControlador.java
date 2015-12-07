package controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;
import modelo.Venta;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Pathy
 */
public class FacturaCabeceraVentaControlador {
     public void insert(Venta ventaC) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(ventaC);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar venta - cabecera: \n" + e.getMessage());
        }
    }
     
      /*public void update(Venta venta) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Venta set "
                   +" fecha = '" + venta.getFecha() + "', precio_total = '" + venta.getPrecioTotal()  + "', cantidad_total = '" + venta.getCantidadTotal() + "'"
                    + ", descuento = '" +  venta.getDescuento() + "', pago_contado = '" +  venta.getPagoContado() + "', estado = '" + venta.getEstado() +  "'"                   
            + ", es_factura = '" + venta.getEsFactura() + "', cliente_id = '" + venta.getClienteId() + "', vencimiento = '" + venta.getVencimiento() + "'"
                    + ", pago_en = '" + venta.getPagoEn() + "', moneda_id = '" + venta.getMonedaId() + "', cod_deposito = '" + venta.getCodDeposito() + "'"
                    + ", nro_prefijo = '" + venta.getNroPrefijo() + "', nro_factura = '" + venta.getNroFactura() + "', iva10 = '" + venta.getIva10() + "', iva5 = '" + venta.getIva5() + "' where nro_factura = '" + venta.getNroFactura() + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
            baseDatos.flush();
            baseDatos.close();
       } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al actualizar venta", "Error", JOptionPane.ERROR_MESSAGE);
             
        }catch(HibernateException e){
            e.getMessage();
        }
     }*/
     
     public ResultSet getNroFacturaPagadas() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.cliente_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.venta_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0), to_char(v.fecha,'dd/mm/yyyy'), v.fact_referenciada from venta v where v.estado = 'PAGADO'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
     
     
     public void update(Venta venta) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.update(venta);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al modificar venta - cabecera: \n" + e.getMessage());
        }
    }
    
     public ResultSet datosTablaBusquedaFacturasPendientes() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(vencimiento,'dd/mm/yyyy') as \"FechaVenc\", precio_total as \"Total\" from venta where es_factura = 'S' and (estado = 'PENDIENTE' or estado = 'PAGADO')";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
        }
     
      public String totalLetras(int precio_total) throws SQLException, Exception
    {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        String res="";
        try {
        String query = "SELECT (f_convnl(CAST("+precio_total+ " as numeric)));";
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
      
      //Para la consula de factura
      public ResultSet datosTablaBusqueda(int codigo) throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as \"Fecha\", pago_contado as \"Forma de pago\", to_char(vencimiento,'dd/mm/yyyy') as \"Fecha Vencimiento\", precio_total as \"Total\", estado as \"Estado\" from Venta where es_factura = 'S' and cliente_id = '" + codigo + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
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
            throw new Exception("Error en el método de vencimiento: \n" + e.getMessage());
        }
         return res;
    }
   public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (max(ventaId), 0) + 1 from Venta").uniqueResult();
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
            throw new Exception("Error al consultar la tabla moneda: \n" + e.getMessage());
        }
    }
    
    
    
    
    public String getDate(Integer nro_factura){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
            return (String) baseDatos.createQuery("select to_char(fecha,'dd/mm/yyyy') as fechaVenta from Venta where nro_factura = '" + nro_factura + "'").uniqueResult();
    }

    public Integer getMonto(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select precioTotal from Venta where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer precio total: \n" + e.getMessage());
        }
    }
     public Integer getCuota(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("select pagoEn from Venta where nro_factura = '" + nro_factura + "'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer cuota: \n" + e.getMessage());
        }
    }
    public void updateEstado(Integer nro_factura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Venta set estado = 'PAGADO' where nro_factura = '" +nro_factura+ "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al actualizar estado factura: \n" + e.getMessage());
        }
    }
    
     public ResultSet datosComboSaldo() throws SQLException, Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo, nro_factura as \"NroFactura\" from Venta where es_factura='S'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
    }
     
      public ResultSet datosComboSaldoNotaCredito() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo, nro_factura as \"NroFactura\" from Venta where es_factura='N'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
    }
    

        //verifica si hay algún registro en la tabla ventas para continuar
        //la secuencia en el nro de la factura o ingresar desde la tabla prefijo
        public Long verificarRegistro() throws SQLException, Exception{
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (nro_factura) as factura from Venta where es_factura = 'S'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al consultar tabla Venta: \n" + e.getMessage());
         }
        }
         
         //Máximo id de la tabla venta
         public int maximoIDVenta() throws SQLException, Exception{
            
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         String cad = "SELECT MAX(venta_id) from venta";
         PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
         try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
         } catch(HibernateException e){
            throw new Exception("Error al obtener maximo id: \n" + e.getMessage());
         }
        }
         
           public Integer ultimoNroFactura() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT MAX(nro_factura) from venta where es_factura = 'S'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return (Integer) rs.getObject(1);
            } catch(HibernateException e){
            throw new Exception("Error al obtener último número de factura: \n" + e.getMessage());
            } 
         }
         
           public Integer ultimoNroNotaC() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT MAX(nro_factura) from venta where es_factura = 'N'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return (Integer) rs.getObject(1);
            } catch(HibernateException e){
            throw new Exception("Error al obtener último número de factura: \n" + e.getMessage());
            } 
         }
           
         public Integer nroFactura() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT MAX(nro_factura)+1 from venta where es_factura = 'S'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return (Integer) rs.getObject(1);
            } catch(HibernateException e){
            throw new Exception("Error al obtener número de factura: \n" + e.getMessage());
            } 
         }
         
         public Integer nroNotaC() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT MAX(nro_factura)+1 from venta where es_factura = 'N'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return (Integer) rs.getObject(1);
            } catch(HibernateException e){
            throw new Exception("Error al obtener número de nota de crédito: \n" + e.getMessage());
            } 
         }
         
         
         
        public ResultSet datosBusqueda() throws Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as Fecha, pago_contado as \"Forma de pago\", precio_total as \"Total\", estado as \"Estado\" from Venta where es_factura = 'S' and estado != 'ANULADO'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
        }
        
        public void updateEstadoAnulado(Integer nro_factura) throws Exception {
        
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update Venta set estado = 'ANULADO' where nro_factura = '" +nro_factura+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular factura: \n" + e.getMessage());
            }
        }

    public ResultSet getNroFactura() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.cliente_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.moneda_id, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.venta_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0) from venta v where v.estado != 'PAGADO'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
    
    //para nota de credito
     public ResultSet getNroFactura1() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.cliente_id, to_char(v.fecha,'dd/mm/yyyy'), v.pago_contado, v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.venta_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), coalesce(v.pago_en, 0), v.fact_referenciada from venta v where v.estado != 'PAGADO'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
    

    public String getTipoPago(String idMoneda) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT nombre from Moneda where moneda_id = '" + idMoneda + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar cliente: \n" + e.getMessage());
        } 
    }
    
     public void updateEstadoAnuladoNotaCreditoV(int nroNotaCredito) throws Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            baseDatos.beginTransaction();
        
            try {
                baseDatos.createQuery("update Venta set estado = 'ANULADO' where nro_factura = '" +nroNotaCredito+ "'").executeUpdate();
                baseDatos.beginTransaction().commit();
            } catch(HibernateException e){
                throw new Exception("Error al anular nota crédito venta: \n" + e.getMessage());
            }
    }
     
     public ResultSet datosBusquedaNotaCreditoVenta() throws SQLException, Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT nro_prefijo as \"Nro Prefijo\", nro_factura as \"Nro Factura\", to_char(fecha,'dd/mm/yyyy') as Fecha, pago_contado as \"Forma de pago\", precio_total as \"Total\", estado as \"Estado\", fact_referenciada as \"Factura Referenciada\" from Venta where es_factura = 'N' and estado != 'ANULADO'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
    }
        //consulta nota de credito
     public ResultSet busquedaNotaCredito(int codigoCliente) throws SQLException, Exception {
            Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            String query = "SELECT v.nro_prefijo as \"Nro Prefijo\", v.nro_factura as \"Nro Nota de Crédito\", v.fact_referenciada as \"Factura Referenciada\", to_char(fecha,'dd/mm/yyyy') as \"Fecha\", v.precio_total as \"Total\", v.estado as \"Estado\" from Venta v where es_factura = 'N' and cliente_id = '" + codigoCliente + "'";
            PreparedStatement ps = baseDatos.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            try {
                //System.out.println("CORRECTA BUSQUEDA");
                return rs;
            } catch(HibernateException e){
                throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
            }
    }
     
      public ResultSet getNroNotaDeCredito() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_prefijo, v.nro_factura, v.cliente_id, to_char(v.fecha,'dd/mm/yyyy'), v.cod_deposito, v.cantidad_total, v.precio_total, v.descuento, v.venta_id, coalesce(v.iva10, 0), coalesce(v.iva5, 0), v.fact_referenciada from venta v where v.estado = 'BORRADOR'";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }

    public Long verificarRegistroNCredito() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (nro_factura) as factura from Venta where es_factura = 'N'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al consultar tabla Venta: \n" + e.getMessage());
         }
    } 
    public void borrarCabecera(Integer nroFactura) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try{
            baseDatos.createQuery("Delete from Venta where nro_factura = '" + nroFactura + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        }catch(HibernateException e){
            throw new Exception("Error al eliminar cabecera de venta: \n" + e.getMessage());
        }
    }
    
     public Integer devuelveId(Integer nroFactura) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT venta_id from Venta where nro_factura = '" + nroFactura + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de venta: \n" + e.getMessage());
        } 
    }
     
     public Integer devuelveClienteId(String nombreCliente) throws SQLException, Exception {
          
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT cliente_id from Cliente where nombre ||' '|| apellido = '" + nombreCliente + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al devolver nro de factura de venta: \n" + e.getMessage());
        } 
    }

    public Long verificarEstadoFactura(int nroFactura) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
     
          try{
            return (Long) baseDatos.createQuery("SELECT count (nro_factura) as factura from Venta where nro_factura = '" + nroFactura + "'").uniqueResult();
         } catch(HibernateException e){
            throw new Exception("Error al consultar tabla Venta: \n" + e.getMessage());
         }


   } 

    //devuelve nro de factura en la ventana detalle de pago de venta
     public ResultSet getNroFacturaDetallePagoVenta() throws SQLException, Exception {
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_factura, v.precio_total, c.nombre ||' '|| c.apellido as \"Nombre\", c.cliente_id from venta v, cliente c where c.cliente_id = v.cliente_id";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }

    public ResultSet getNroNotaCreditoPagoVenta() throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         
          String query = "Select v.nro_factura, v.precio_total from venta v";
         
         PreparedStatement ps = baseDatos.connection().prepareStatement(query);
         ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Venta: \n" + e.getMessage());
        }
    }
      
}
 

