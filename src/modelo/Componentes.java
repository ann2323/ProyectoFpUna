package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * Componentes generated by hbm2java
 */
public class Componentes  implements java.io.Serializable {


     private int codigoInterno;
     private String descripcion;
     private String codigo;
     private Integer estado;
     private Integer idProveedor;
     private Integer unidad;
     private Integer precio;
     private Integer costo;
     private Integer tipoIva;

    public Componentes() {
    }

	
    public Componentes(int codigoInterno, String codigo) {
        this.codigoInterno = codigoInterno;
        this.codigo = codigo;
    }
    public Componentes(int codigoInterno, String descripcion, String codigo, Integer estado, Integer idProveedor, Integer unidad, Integer precio, Integer costo, Integer tipoIva) {
       this.codigoInterno = codigoInterno;
       this.descripcion = descripcion;
       this.codigo = codigo;
       this.estado = estado;
       this.idProveedor = idProveedor;
       this.unidad = unidad;
       this.precio = precio;
       this.costo = costo;
       this.tipoIva = tipoIva;
    }
   
    public int getCodigoInterno() {
        return this.codigoInterno;
    }
    
    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Integer getEstado() {
        return this.estado;
    }
    
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    public Integer getIdProveedor() {
        return this.idProveedor;
    }
    
    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }
    public Integer getUnidad() {
        return this.unidad;
    }
    
    public void setUnidad(Integer unidad) {
        this.unidad = unidad;
    }
    public Integer getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
    public Integer getCosto() {
        return this.costo;
    }
    
    public void setCosto(Integer costo) {
        this.costo = costo;
    }
    public Integer getTipoIva() {
        return this.tipoIva;
    }
    
    public void setTipoIva(Integer tipoIva) {
        this.tipoIva = tipoIva;
    }




}


