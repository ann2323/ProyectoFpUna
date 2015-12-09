package modelo;
// Generated Dec 8, 2015 5:15:36 PM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Venta generated by hbm2java
 */
public class Venta  implements java.io.Serializable {


     private int ventaId;
     private Date fecha;
     private int precioTotal;
     private int cantidadTotal;
     private Integer descuento;
     private Double precioExenta;
     private String pagoContado;
     private String estado;
     private Integer factReferenciada;
     private String esFactura;
     private int clienteId;
     private Date vencimiento;
     private Integer pagoEn;
     private String codDeposito;
     private String nroPrefijo;
     private Integer nroFactura;
     private Integer iva10;
     private Integer iva5;
     private Integer proyectoId;
     private Integer saldo;

    public Venta() {
    }

	
    public Venta(int ventaId, Date fecha, int precioTotal, int cantidadTotal, String pagoContado, String estado, String esFactura, int clienteId, Date vencimiento) {
        this.ventaId = ventaId;
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.cantidadTotal = cantidadTotal;
        this.pagoContado = pagoContado;
        this.estado = estado;
        this.esFactura = esFactura;
        this.clienteId = clienteId;
        this.vencimiento = vencimiento;
    }
    public Venta(int ventaId, Date fecha, int precioTotal, int cantidadTotal, Integer descuento, Double precioExenta, String pagoContado, String estado, Integer factReferenciada, String esFactura, int clienteId, Date vencimiento, Integer pagoEn, String codDeposito, String nroPrefijo, Integer nroFactura, Integer iva10, Integer iva5, Integer proyectoId, Integer saldo) {
       this.ventaId = ventaId;
       this.fecha = fecha;
       this.precioTotal = precioTotal;
       this.cantidadTotal = cantidadTotal;
       this.descuento = descuento;
       this.precioExenta = precioExenta;
       this.pagoContado = pagoContado;
       this.estado = estado;
       this.factReferenciada = factReferenciada;
       this.esFactura = esFactura;
       this.clienteId = clienteId;
       this.vencimiento = vencimiento;
       this.pagoEn = pagoEn;
       this.codDeposito = codDeposito;
       this.nroPrefijo = nroPrefijo;
       this.nroFactura = nroFactura;
       this.iva10 = iva10;
       this.iva5 = iva5;
       this.proyectoId = proyectoId;
       this.saldo = saldo;
    }
   
    public int getVentaId() {
        return this.ventaId;
    }
    
    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public int getPrecioTotal() {
        return this.precioTotal;
    }
    
    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }
    public int getCantidadTotal() {
        return this.cantidadTotal;
    }
    
    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    public Integer getDescuento() {
        return this.descuento;
    }
    
    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }
    public Double getPrecioExenta() {
        return this.precioExenta;
    }
    
    public void setPrecioExenta(Double precioExenta) {
        this.precioExenta = precioExenta;
    }
    public String getPagoContado() {
        return this.pagoContado;
    }
    
    public void setPagoContado(String pagoContado) {
        this.pagoContado = pagoContado;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Integer getFactReferenciada() {
        return this.factReferenciada;
    }
    
    public void setFactReferenciada(Integer factReferenciada) {
        this.factReferenciada = factReferenciada;
    }
    public String getEsFactura() {
        return this.esFactura;
    }
    
    public void setEsFactura(String esFactura) {
        this.esFactura = esFactura;
    }
    public int getClienteId() {
        return this.clienteId;
    }
    
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    public Date getVencimiento() {
        return this.vencimiento;
    }
    
    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }
    public Integer getPagoEn() {
        return this.pagoEn;
    }
    
    public void setPagoEn(Integer pagoEn) {
        this.pagoEn = pagoEn;
    }
    public String getCodDeposito() {
        return this.codDeposito;
    }
    
    public void setCodDeposito(String codDeposito) {
        this.codDeposito = codDeposito;
    }
    public String getNroPrefijo() {
        return this.nroPrefijo;
    }
    
    public void setNroPrefijo(String nroPrefijo) {
        this.nroPrefijo = nroPrefijo;
    }
    public Integer getNroFactura() {
        return this.nroFactura;
    }
    
    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }
    public Integer getIva10() {
        return this.iva10;
    }
    
    public void setIva10(Integer iva10) {
        this.iva10 = iva10;
    }
    public Integer getIva5() {
        return this.iva5;
    }
    
    public void setIva5(Integer iva5) {
        this.iva5 = iva5;
    }
    public Integer getProyectoId() {
        return this.proyectoId;
    }
    
    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }
    public Integer getSaldo() {
        return this.saldo;
    }
    
    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }




}


