package modelo;
// Generated 15-jul-2015 17:18:17 by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Proyectos generated by hbm2java
 */
public class Proyectos  implements java.io.Serializable {


     private int id;
     private String codigo;
     private Integer jefe;
     private Date fechaIni;
     private Date fechaFin;
     private String descripcion;
     private Integer solicitante;
     private Integer estado;
     private Integer utilidad;
     private Integer presupuesto;

    public Proyectos() {
    }

	
    public Proyectos(int id) {
        this.id = id;
    }
    public Proyectos(int id, String codigo, Integer jefe, Date fechaIni, Date fechaFin, String descripcion, Integer solicitante, Integer estado, Integer utilidad, Integer presupuesto) {
       this.id = id;
       this.codigo = codigo;
       this.jefe = jefe;
       this.fechaIni = fechaIni;
       this.fechaFin = fechaFin;
       this.descripcion = descripcion;
       this.solicitante = solicitante;
       this.estado = estado;
       this.utilidad = utilidad;
       this.presupuesto = presupuesto;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Integer getJefe() {
        return this.jefe;
    }
    
    public void setJefe(Integer jefe) {
        this.jefe = jefe;
    }
    public Date getFechaIni() {
        return this.fechaIni;
    }
    
    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }
    public Date getFechaFin() {
        return this.fechaFin;
    }
    
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getSolicitante() {
        return this.solicitante;
    }
    
    public void setSolicitante(Integer solicitante) {
        this.solicitante = solicitante;
    }
    public Integer getEstado() {
        return this.estado;
    }
    
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    public Integer getUtilidad() {
        return this.utilidad;
    }
    
    public void setUtilidad(Integer utilidad) {
        this.utilidad = utilidad;
    }
    public Integer getPresupuesto() {
        return this.presupuesto;
    }
    
    public void setPresupuesto(Integer presupuesto) {
        this.presupuesto = presupuesto;
    }




}


