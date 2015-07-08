package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * PagoFactura generated by hbm2java
 */
public class PagoFactura  implements java.io.Serializable {


     private int pagoDocumentoId;
     private String estado;
     private Integer facturaVentaId;
     private Integer facturaCompraId;
     private double monto;
     private int cuentaId;

    public PagoFactura() {
    }

	
    public PagoFactura(int pagoDocumentoId, String estado, double monto, int cuentaId) {
        this.pagoDocumentoId = pagoDocumentoId;
        this.estado = estado;
        this.monto = monto;
        this.cuentaId = cuentaId;
    }
    public PagoFactura(int pagoDocumentoId, String estado, Integer facturaVentaId, Integer facturaCompraId, double monto, int cuentaId) {
       this.pagoDocumentoId = pagoDocumentoId;
       this.estado = estado;
       this.facturaVentaId = facturaVentaId;
       this.facturaCompraId = facturaCompraId;
       this.monto = monto;
       this.cuentaId = cuentaId;
    }
   
    public int getPagoDocumentoId() {
        return this.pagoDocumentoId;
    }
    
    public void setPagoDocumentoId(int pagoDocumentoId) {
        this.pagoDocumentoId = pagoDocumentoId;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Integer getFacturaVentaId() {
        return this.facturaVentaId;
    }
    
    public void setFacturaVentaId(Integer facturaVentaId) {
        this.facturaVentaId = facturaVentaId;
    }
    public Integer getFacturaCompraId() {
        return this.facturaCompraId;
    }
    
    public void setFacturaCompraId(Integer facturaCompraId) {
        this.facturaCompraId = facturaCompraId;
    }
    public double getMonto() {
        return this.monto;
    }
    
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public int getCuentaId() {
        return this.cuentaId;
    }
    
    public void setCuentaId(int cuentaId) {
        this.cuentaId = cuentaId;
    }




}


