
package controlador;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;


/**
 *
 * @author Patricia Espínola
 */

public class UsuarioControlador{
    
    public boolean login (Usuario usr) {
        
       
        try{
       Session baseDatos = HibernateUtil.getSessionFactory().openSession();
            Query query = baseDatos.createQuery("Select nombreusuario, contrasenha from Usuario WHERE nombreusuario = '" + usr.getNombreusuario() + "'"
                    + " and contrasenha = md5('" + usr.getContrasenha() + "')");
            
            System.out.println("query "+query);
        
            if(!query.list().isEmpty()){
                 //System.out.println("ENTRO EN TRUE");
                return true;
               
            }else{
                 //System.out.println("ENTRO EN FALSE");
                return false;    
            }
        }catch(Exception e){
           return false;
        }
    }
    
     public void insert(Usuario usuario) throws Exception {
      
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.save(usuario);
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al guardar usuario: \n" + e.getMessage());
        }
         
    }
     
     
      public void delete(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        //baseDatos.flush();
       
        try {
            baseDatos.createQuery("delete from Usuario where idusuario = '" + codigo + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
        } catch(HibernateException e){
            throw new Exception("Error al eliminar usuario: \n" + e.getMessage());
        }
    }
    
    public void update(Usuario usuario) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        baseDatos.beginTransaction();
        
        try {
            baseDatos.createQuery("update Usuario set "
                   +" nombre = '" + usuario.getNombre() + "', apellido = '" + usuario.getApellido() + "'"
                    + ", nombreusuario = '" +  usuario.getNombreusuario() + "', contrasenha = '" +  usuario.getContrasenha() + "', email = '" + usuario.getEmail()+ "'"
                    + ", id_rol = '" + usuario.getIdRol()+  "' where idusuario = '" + usuario.getIdusuario() + "'").executeUpdate();
            baseDatos.beginTransaction().commit();
            //baseDatos.flush();
            //baseDatos.close();
       } catch (org.hibernate.exception.ConstraintViolationException cve) {
             JOptionPane.showMessageDialog(null, "El valor ya existe. Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
             
        }catch(HibernateException e){
            e.getMessage();
        }
    }
    
     public ResultSet datos() throws Exception {
         
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT id_rol, nombre from rol";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs1 = ps.executeQuery();
        try {
            return rs1;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla rol: \n" + e.getMessage());
        }
    }
     
     public int getRolId(String nombreUsuario) throws Exception{
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         String cad = "SELECT id_rol from Usuario where nombreusuario = '" + nombreUsuario + "'";
         PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
         try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al obtener id rol: \n" + e.getMessage());
        }
     }
     
      public String getRolNombre(int idRol) throws Exception{
         Session baseDatos = HibernateUtil.getSessionFactory().openSession();
         String cad = "SELECT nombre from Rol where id_rol = '" + idRol + "'";
         PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
         try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al obtener nombre del rol: \n" + e.getMessage());
        }
     }
     
      public String getDato(String codigo) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT nombre from Rol where id_rol = '" + codigo + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Deposito: \n" + e.getMessage());
        }
    }
      
     public int getCodigo(String dato) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT id_rol from Rol where nombre = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar rol: \n" + e.getMessage());
        }
    }

     public ResultSet consultar() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "Select u.nombre AS \"Nombre\", u.apellido as \"Apellido\", u.nombreusuario as \"Usuario\", u.email as \"Correo Electrónico\", r.nombre as \"Rol\", u.idusuario, u.contrasenha from Usuario u left outer join Rol r on u.id_rol = r.id_rol order by u.nombre ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar la tabla Cliente: \n" + e.getMessage());
        }
    }
     
     public ResultSet datoCombo() throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String query = "Select u.nombre || ' ' || u.apellido as dato from Usuario u order by u.nombre ";
        PreparedStatement ps = baseDatos.connection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            return rs;
        } catch(HibernateException e){
            throw new Exception("Error al consultar Usuario: \n" + e.getMessage());
        }
    }
     
     public int getIdUsuario(String dato) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT u.idusuario from Usuario u where u.nombre || ' ' || u.apellido = '" + dato + "'";
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (int) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Usuario: \n" + e.getMessage());
        }
    }
     
     public String getDatoUsuario(Integer id) throws Exception {
        Session baseDatos = HibernateUtil.getSessionFactory().openSession();
        String cad = "SELECT u.nombre || ' ' || u.apellido from Usuario u where idusuario = " + id;
        PreparedStatement ps = baseDatos.connection().prepareStatement(cad);
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return (String) rs.getObject(1);
        } catch(HibernateException e){
            throw new Exception("Error al consultar Usuario: \n" + e.getMessage());
        }
    }  
}
   /* public static String md5(String input) {
        
        StringBuffer h = null;
        
        if(null == input) return null;
         
        try{   
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            byte[] b = md.digest(input.getBytes()); 
            h = new StringBuffer(b.length); 
            for (int i = 0; i <b.length; i++) { 
                int u = b[i]&255; 
               if (u<16){
                 h.append("0").append(Integer.toHexString(u)); 
               }else { 
                h.append(Integer.toHexString(u)); 
                } 
            } 
           
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        }
       return h.toString();
    }*/

