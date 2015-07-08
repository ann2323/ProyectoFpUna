/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Pathy
 */
public class Prueba {
    
    public static void main(String[] args) {
         Timestamp stamp = new Timestamp(System.currentTimeMillis());
        timestampToDate(stamp);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formateador.format(stamp);
        System.out.println(fecha);
    }
   private static Date timestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
    
}
