package modelo;
// Generated Dec 21, 2015 6:00:36 PM by Hibernate Tools 3.6.0



/**
 * ResponsablesProyecto generated by hbm2java
 */
public class ResponsablesProyecto  implements java.io.Serializable {


     private int id;
     private Integer idProyecto;
     private Integer idUsuario;

    public ResponsablesProyecto() {
    }

	
    public ResponsablesProyecto(int id) {
        this.id = id;
    }
    public ResponsablesProyecto(int id, Integer idProyecto, Integer idUsuario) {
       this.id = id;
       this.idProyecto = idProyecto;
       this.idUsuario = idUsuario;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Integer getIdProyecto() {
        return this.idProyecto;
    }
    
    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }
    public Integer getIdUsuario() {
        return this.idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }




}


