package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * DetalleCompra generated by hbm2java
 */
public class DetalleCompra  implements java.io.Serializable {


     private int detalleCompraId;
     private int cantidad;
     private int subTotal;
     private String descripcion;
     private int compraId;
     private int componenteId;
     private String codigo;
     private Integer precioUnit;
     private Integer exentas;

    public DetalleCompra() {
    }

	
    public DetalleCompra(int detalleCompraId, int cantidad, int subTotal, int compraId, int componenteId, String codigo) {
        this.detalleCompraId = detalleCompraId;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.compraId = compraId;
        this.componenteId = componenteId;
        this.codigo = codigo;
    }
    public DetalleCompra(int detalleCompraId, int cantidad, int subTotal, String descripcion, int compraId, int componenteId, String codigo, Integer precioUnit, Integer exentas) {
       this.detalleCompraId = detalleCompraId;
       this.cantidad = cantidad;
       this.subTotal = subTotal;
       this.descripcion = descripcion;
       this.compraId = compraId;
       this.componenteId = componenteId;
       this.codigo = codigo;
       this.precioUnit = precioUnit;
       this.exentas = exentas;
    }
   
    public int getDetalleCompraId() {
        return this.detalleCompraId;
    }
    
    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
    }
    public int getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getSubTotal() {
        return this.subTotal;
    }
    
    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getCompraId() {
        return this.compraId;
    }
    
    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }
    public int getComponenteId() {
        return this.componenteId;
    }
    
    public void setComponenteId(int componenteId) {
        this.componenteId = componenteId;
    }
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Integer getPrecioUnit() {
        return this.precioUnit;
    }
    
    public void setPrecioUnit(Integer precioUnit) {
        this.precioUnit = precioUnit;
    }
    public Integer getExentas() {
        return this.exentas;
    }
    
    public void setExentas(Integer exentas) {
        this.exentas = exentas;
    }




}


