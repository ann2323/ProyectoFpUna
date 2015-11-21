

package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Cliente;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Patricia Espinola
 */
public class ClienteControlador {
//esta es una modificacion
    public void insert(Cliente cmp)    {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
         try {
            baseDatos.save(cmp);
            baseDatos.beginTransaction().commit();
            baseDatos.flush();
            baseDatos.close();
         } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al insertar", "Error", JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            e.getMessage();
        }
    }
    
  public void update(Cliente cliente) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Cliente set "
                   +" nombre = '" + cliente.getNombre() + "', codigocliente = '" + cliente.getCodigocliente()  + "', apellido = '" + cliente.getApellido() + "'"
                    + ", cedula = '" +  cliente.getCedula() + "', dv = '" +  cliente.getDv() + "', telefono = '" + cliente.getTelefono() +  "'"                   
            + ", direccion = '" + cliente.getDireccion() + "', estado = '" + cliente.getEstado() + "', limitecredito = '" + cliente.getLimitecredito() + "', tipocliente = '" + cliente.getTipocliente() + "' where codigocliente = '" + cliente.getCodigocliente() + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
            baseDatos.flush();
            baseDatos.close();
       } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
             
        }catch(HibernateException e){
            e.getMessage();
        }
    }
    public void delete(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        baseDatos.flush();
       
        try {
            baseDatos.createQuery("update Cliente set estado = 'Inactivo' where codigocliente = '" + codigo + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar cliente: \n" + e.getMessage());
        }
    }
     public ResultSet datosBusqueda2() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT substring(cedula from 1 for 1)||'.'||substring(cedula from 2 for 3)||'.'||substring(cedula from 5 for 7) as \"RUC/CI\", nombre||' '||apellido as \"Nombre\" from Cliente";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Componentes: \n" + e.getMessage());
        }
    }
    
    /**public Integer nuevoCodigo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Integer) baseDatos.createQuery("select coalesce (CAST(max(codigo) AS integer), 0) + 1 from Cliente").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al generar nuevo código: \n" + e.getMessage());
        }
    }*/

 public ResultSet datosBusqueda() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT cedula as \"RUC/CI\", nombre||' '||apellido as \"Nombre\" from Cliente";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Cliente: \n" + e.getMessage());
        }
    }
 
    public ResultSet datosCliente() throws Exception  {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        
        
        String query = "SELECT cliente_id, cedula as \"RUC/CI\", nombre||' '||apellido as \"Nombre\" from Cliente";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Cliente: \n" + e.getMessage());
        }
    }
 
    

     public int devuelveId(String ci) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
           return (Integer) baseDatos.createQuery("Select clienteId from Cliente where cedula='"+ci+"'").uniqueResult();
        } catch(HibernateException e){
            throw new Exception("Error al traer id cliente: \n" + e.getMessage());
        }
    }  
    
    public ResultSet datos() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT codigocliente as \"Código\", case when dv='' then cedula else cedula ||'-'|| dv end as \"RUC O CI Nº\", nombre as \"Nombre\", apellido as \"Apellido\", direccion as \"Dirección\", telefono as \"Teléfono\", estado as \"Estado\", limitecredito \"Límite crédito\", cliente_id, tipocliente \"Tipo cliente\" from Cliente order by codigocliente";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Cliente: \n" + e.getMessage());
        }
    }

     public String getExento(String cedula){
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            return (String) baseDatos.createQuery("select tipocliente from Cliente where cedula = '" + cedula + "'").uniqueResult();
    }
     
     public ResultSet datosCombo() throws Exception {
         
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT cliente_id, nombre ||' '|| apellido as \"Nombre\" from cliente";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs1 = ps.executeQuery();
        try {
            return rs1;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla cliente: \n" + e.getMessage());
        }
    }
     
     public int getCodigo(String dato) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT cliente_id from Cliente where nombre||' '||apellido = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar cliente: \n" + e.getMessage());
        }
    }

     //metodo al que se le pasa el id de cliente y devuelve la cedula
     //Metodo para la busqueda en factura venta
    public String getCedula(String dato) throws SQLException, Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT cedula from Cliente where cliente_id = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar cliente: \n" + e.getMessage());
        }
    }

    public String getNombreCliente(String idCliente) throws SQLException, Exception  {
       Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT nombre ||' '|| apellido as \"Nombre\" from Cliente where cliente_id = '" + idCliente + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar cliente: \n" + e.getMessage());
        } 
    }
     
     

   
}
 
