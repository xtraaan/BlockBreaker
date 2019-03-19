/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.images;

import basicgraphics.BasicFrame;
import basicgraphics.FileUtility;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * This class encapsulates the creation and
 * use of images.
 * @author sbrandt
 */
public class Picture extends JComponent {
    
    private BufferedImage image;
    private int width, height;
    boolean[][] mask;
    
    /**
     * Set all white pixels to transparent
     */
    public void transparentWhite() {
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i,j);
                if((color & 0x00FFFFFF) == 0x00FFFFFF) {
                    image.setRGB(i, j, 0x00000000);
                }
            }
        }
    }
    
    /**
     * Get the raw image stored by this class.
     * @return 
     */
    public Image getImage() { return image; }
    
    /** You should store your images
     * in the same directory as the source for
     * this class (i.e. the same directory as
     * Picture.java). That will enable you to
     * load them either from the working directory
     * in Netbeans, or in the jar file you
     * distribute.
     * @param name
     */
    public Picture(String name) {
        URL src = null;
        try {
            src = new URL(name);
        } catch(MalformedURLException me) {
            ;
        }
        if(src == null)
            src = getClass().getResource(name);
        if(src == null) {
            src = FileUtility.findFile(name);
            if(src == null) {
                System.out.println("Could not load image: "+name);
                throw new RuntimeIOException("No such image: "+name);
            }
        }
        try {
            image = ImageIO.read(src);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(getPreferredSize());
        createMask();
    }
    
    void createMask() {
        mask = new boolean[width][height];
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i, j);
                mask[i][j] = (color & 0xFF000000) != 0;
            }
        }
    }
    
    /**
     * You can also create a picture from an image
     * directly (using basicgraphics.BasicFrame.createImage())
     * and drawing on it.
     * @param im 
     */
    public Picture(Image im) {
        this.image = (BufferedImage) im;
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(getPreferredSize());
        createMask();
    }
    
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    
    /**
     * Create a new copy of the picture
     * object that's rotated by the specified
     * angle (measured in radians).
     * @param angle
     * @return 
     */
    public Picture rotate(double angle) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        double tx = w/2;
        double ty = h/2;
        g2.translate(tx,ty);
        g2.rotate(angle);
        g2.translate(-tx,-ty);
        g2.drawImage(image, 0, 0, this);
        return new Picture(bi);
    }
    
    public Picture resize(double factor) {
        int w = (int) (image.getWidth()*factor);
        int h = (int) (image.getHeight()*factor);
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(factor, factor);
        g2.drawImage(image, xform, this);
        return new Picture(bi);
    }
    
    public Picture flipLeftRight() {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(-1, 1);
        g2.drawImage(image, xform, this);
        return new Picture(bi);
    }
    
    /**
     * Create a button that uses the same
     * image as the one stored in this Picture.
     * @return 
     */
    public JButton makeButton() {
        return new JButton(new ImageIcon(image));
    }
    
    public boolean mask(int i,int j) {
//        if(i < 0 || i >= mask.length) return false;
//        if(j < 0 || j >= mask[i].length) return false;
        return mask[i][j];
    }
}
