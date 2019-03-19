package CSCProject;

import static CSCProject.Bullet.bullets;
import basicgraphics.BasicFrame;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_SPACE;
import java.io.IOException;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * CSC 1351 Final Project
 * This is the main class of the block breaker game that uses basic graphics.
 * @author Eilynn
 */

public class Game 
{
    BasicFrame bf = new BasicFrame("Block Breaker");
    
    final public static Random RAND = new Random();
    final public static int BOSS = 45;
    final public static int BIG = 20;
    final public static int SMALLER = 15;
    final public static int SMALLEST = 10;
    final public static int BULLET = 7;
    final public static int BLOCKS = 10;
    final public static int BOTTOM = 65;
    final public static Dimension BOARD_SIZE = new Dimension(800,400);
    final public static Color BULLET_COLOR = Color.black;
    final public static Color BOSS_COLOR = Color.pink;
    final public static Color BLOCK_COLOR = Color.red;
    final public static Color BLOCK2_COLOR = Color.orange;
    final public static Color BLOCK3_COLOR = Color.green;
    final public static Color LINE_COLOR = Color.darkGray;
    
    static Picture makeBall(Color color,int size) 
    {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillOval(0, 0, size, size);
        return new Picture(im);
    }
    
    public static Picture makeRectangle(Color color,int size) 
    {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);
        return new Picture(im);
    }
    
    public static void main(String[] args) throws IOException 
    {
        Game g = new Game();
        g.run();
    }
    
    public void run() throws IOException 
    {
        final SpriteComponent sc = new SpriteComponent() 
        {
            @Override
            public void paintBackground(Graphics g) 
            {
                Dimension d = getSize();
                d.width =800;
                d.height =400;
                g.setColor(Color.cyan);
                g.fillRect(0, 0, d.width, d.height);
                final int NUM_CLOUDS = 50;
                Random rand = new Random();
                rand.setSeed(0);
                g.setColor(Color.white);
                for(int i=0;i<NUM_CLOUDS;i++) 
                {
                    int diameter = rand.nextInt(75)+1;
                    int xpos = rand.nextInt(d.width);
                    int ypos = rand.nextInt(d.height/5);
                    g.fillOval(xpos, ypos, diameter, diameter);
                }
            } 
        };     
        JOptionPane.showMessageDialog(sc,"BLOCK BREAKER");
        AudioClip music = new ReusableClip("Intro.wav");
        music.play();
        sc.setPreferredSize(new Dimension(800,400));
        bf.add("Warrior",sc,0,0,0,0);
        bf.show();

        final Blocks in = new Blocks();
        for(int i = 0; i < BLOCKS; i++) 
        {
            Blocks b = new Blocks();
            b.init(sc);
        }
        
        final Line l = new Line();
        final Warrior r = new Warrior();
        final double INCR = .2;
        bf.addKeyListener(new KeyAdapter() 
        {   
            @Override
            public void keyPressed(KeyEvent ke) 
            {
                if(ke.getKeyCode() == KeyEvent.VK_RIGHT) 
                {
                    r.setRight(true);
                }
                else if(ke.getKeyCode() == KeyEvent.VK_LEFT) 
                {
                    r.setLeft(true);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent ke) 
            {
                if(ke.getKeyCode() == KeyEvent.VK_RIGHT) 
                {
                    r.setRight(false);
                }
                else if(ke.getKeyCode() == KeyEvent.VK_LEFT) 
                {
                    r.setLeft(false);
                }
            }
        });
        
        KeyAdapter key = new KeyAdapter() 
        {
            boolean pressed = false;
            @Override
            public void keyPressed(KeyEvent ke) 
            {
                Bullet a = new Bullet();
                a.init(sc,r,ke.getKeyCode());
                if (ke.getKeyCode() == VK_SPACE) 
                {
                    pressed = true;
                }
                
                if (bullets == 250) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 250 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 200) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 200 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 150) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 150 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 100) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 100 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 50) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 50 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 10) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 10 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 5) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 5 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == 1) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "You have 1 bullets left."
                            + "\nPress space bar to continue.");
                }
                else if (bullets == -1) 
                {
                    JOptionPane.showMessageDialog(bf.getContentPane(), "No more ammo. You lose!");
                    System.exit(0);
                }
            }
        };
        bf.addKeyListener(key);
        bf.addMenuAction("About", "About", new Runnable() 
        {
            @Override
            public void run() 
            {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Enjoy this block breaker game created using basic graphics!");
            }
        });
        
        bf.addMenuAction("Instructions", "How To Play", new Runnable() 
        {
            @Override
            public void run() 
            {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Move to the left and right using left and right arrow keys."
                        + "\nHit space bar to shoot bullets at blocks until all of them are gone." 
                        + "\nKeep in mind, you only have 300 bullets to use.");
            }
        });
        
        bf.addMenuAction("Quit", "Quit", new Runnable() 
        { 
            @Override
            public void run() 
            {
                System.exit(0);
            }
        });
        
        bf.show();
        
        in.init(sc);
        l.init(sc);
        r.init(sc);
        sc.start(0,10);
    }
}