package modelo;
// Generated 08-ago-2015 20:27:41 by Hibernate Tools 3.6.0



/**
 * DetallePago generated by hbm2java
 */
public class DetallePago  implements java.io.Serializable {


     private int detallePagoId;
     private Integer tipoPagoId;
     private Integer compraId;
     private Integer ventaId;
     private Integer monto;
     private Integer nroTarjeta;
     private Integer autorizacion;
     private Integer nroCheque;
     private Integer valorCredito;
     private Integer pendienteAAplicar;

    public DetallePago() {
    }

	
    public DetallePago(int detallePagoId) {
        this.detallePagoId = detallePagoId;
    }
    public DetallePago(int detallePagoId, Integer tipoPagoId, Integer compraId, Integer ventaId, Integer monto, Integer nroTarjeta, Integer autorizacion, Integer nroCheque, Integer valorCredito, Integer pendienteAAplicar) {
       this.detallePagoId = detallePagoId;
       this.tipoPagoId = tipoPagoId;
       this.compraId = compraId;
       this.ventaId = ventaId;
       this.monto = monto;
       this.nroTarjeta = nroTarjeta;
       this.autorizacion = autorizacion;
       this.nroCheque = nroCheque;
       this.valorCredito = valorCredito;
       this.pendienteAAplicar = pendienteAAplicar;
    }
   
    public int getDetallePagoId() {
        return this.detallePagoId;
    }
    
    public void setDetallePagoId(int detallePagoId) {
        this.detallePagoId = detallePagoId;
    }
    public Integer getTipoPagoId() {
        return this.tipoPagoId;
    }
    
    public void setTipoPagoId(Integer tipoPagoId) {
        this.tipoPagoId = tipoPagoId;
    }
    public Integer getCompraId() {
        return this.compraId;
    }
    
    public void setCompraId(Integer compraId) {
        this.compraId = compraId;
    }
    public Integer getVentaId() {
        return this.ventaId;
    }
    
    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }
    public Integer getMonto() {
        return this.monto;
    }
    
    public void setMonto(Integer monto) {
        this.monto = monto;
    }
    public Integer getNroTarjeta() {
        return this.nroTarjeta;
    }
    
    public void setNroTarjeta(Integer nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
    }
    public Integer getAutorizacion() {
        return this.autorizacion;
    }
    
    public void setAutorizacion(Integer autorizacion) {
        this.autorizacion = autorizacion;
    }
    public Integer getNroCheque() {
        return this.nroCheque;
    }
    
    public void setNroCheque(Integer nroCheque) {
        this.nroCheque = nroCheque;
    }
    public Integer getValorCredito() {
        return this.valorCredito;
    }
    
    public void setValorCredito(Integer valorCredito) {
        this.valorCredito = valorCredito;
    }
    public Integer getPendienteAAplicar() {
        return this.pendienteAAplicar;
    }
    
    public void setPendienteAAplicar(Integer pendienteAAplicar) {
        this.pendienteAAplicar = pendienteAAplicar;
    }




}


