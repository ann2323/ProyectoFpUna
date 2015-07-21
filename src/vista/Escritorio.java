/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

import javax.swing.border.Border;

/**
 *
 * @author Pathy
 */
public class Escritorio implements Border{
    
    public BufferedImage back;
    
    public Escritorio(){
        try{
            URL imagePath = new URL(getClass().getResource("../imagenes/intersat4.png").toString());
            back = ImageIO.read(imagePath);
        }catch(Exception ex){
            
        }
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        //g.drawImage(back, (x+(width - back.getWidth())/2), (y+(height -  back.getWidth())/2), null);
        g.drawImage(back, x, y, Color.cyan, c);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
      return false;
    }
    
} 