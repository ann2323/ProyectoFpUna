package modelo;
// Generated Jun 27, 2015 5:39:32 PM by Hibernate Tools 3.6.0



/**
 * PrefijoFactura generated by hbm2java
 */
public class PrefijoFactura  implements java.io.Serializable {


     private int idprefijo;
     private String prefijo;
     private String tipoDocumento;

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
     private Integer principiofactura;
     private Integer finfactura;

    public PrefijoFactura() {
    }

	
    public PrefijoFactura(int idprefijo) {
        this.idprefijo = idprefijo;
    }
    public PrefijoFactura(int idprefijo, String prefijo, Integer principiofactura, Integer finfactura) {
       this.idprefijo = idprefijo;
       this.prefijo = prefijo;
       this.principiofactura = principiofactura;
       this.finfactura = finfactura;
    }
   
    public int getIdprefijo() {
        return this.idprefijo;
    }
    
    public void setIdprefijo(int idprefijo) {
        this.idprefijo = idprefijo;
    }
    public String getPrefijo() {
        return this.prefijo;
    }
    
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }
    public Integer getPrincipiofactura() {
        return this.principiofactura;
    }
    
    public void setPrincipiofactura(Integer principiofactura) {
        this.principiofactura = principiofactura;
    }
    public Integer getFinfactura() {
        return this.finfactura;
    }
    
    public void setFinfactura(Integer finfactura) {
        this.finfactura = finfactura;
    }




}


