package modelo;
// Generated 08-ago-2015 20:27:41 by Hibernate Tools 3.6.0



/**
 * Stock generated by hbm2java
 */
public class Stock  implements java.io.Serializable {


     private int line;
     private String codComponente;
     private String codDeposito;
     private Integer cantidad;

    public Stock() {
    }

	
    public Stock(int line, String codComponente, String codDeposito) {
        this.line = line;
        this.codComponente = codComponente;
        this.codDeposito = codDeposito;
    }
    public Stock(int line, String codComponente, String codDeposito, Integer cantidad) {
       this.line = line;
       this.codComponente = codComponente;
       this.codDeposito = codDeposito;
       this.cantidad = cantidad;
    }
   
    public int getLine() {
        return this.line;
    }
    
    public void setLine(int line) {
        this.line = line;
    }
    public String getCodComponente() {
        return this.codComponente;
    }
    
    public void setCodComponente(String codComponente) {
        this.codComponente = codComponente;
    }
    public String getCodDeposito() {
        return this.codDeposito;
    }
    
    public void setCodDeposito(String codDeposito) {
        this.codDeposito = codDeposito;
    }
    public Integer getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }




}


