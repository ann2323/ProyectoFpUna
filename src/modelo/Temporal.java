package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * Temporal generated by hbm2java
 */
public class Temporal  implements java.io.Serializable {


     private int line;
     private String proyecto;
     private Integer precio;

    public Temporal() {
    }

	
    public Temporal(int line) {
        this.line = line;
    }
    public Temporal(int line, String proyecto, Integer precio) {
       this.line = line;
       this.proyecto = proyecto;
       this.precio = precio;
    }
   
    public int getLine() {
        return this.line;
    }
    
    public void setLine(int line) {
        this.line = line;
    }
    public String getProyecto() {
        return this.proyecto;
    }
    
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }
    public Integer getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }




}


