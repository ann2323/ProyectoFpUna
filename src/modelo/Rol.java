package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * Rol generated by hbm2java
 */
public class Rol  implements java.io.Serializable {


     private int idRol;
     private String nombre;
     private String descripcion;

    public Rol() {
    }

	
    public Rol(int idRol) {
        this.idRol = idRol;
    }
    public Rol(int idRol, String nombre, String descripcion) {
       this.idRol = idRol;
       this.nombre = nombre;
       this.descripcion = descripcion;
    }
   
    public int getIdRol() {
        return this.idRol;
    }
    
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}


