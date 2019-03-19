/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.examples;

import basicgraphics.BasicFrame;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class Foo implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Click!");
    }
}

/**
 *
 * @author sbrandt
 */
public class BasicGraphics {

    static class Ball extends Sprite {

    }

    static String[][] layout = {
        {"topl", "topm", "topr"},
        {"row2", "row2", "row2"},
        {"row3", "row3", "row3"},
        {"row4", "row4", "botr"}
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BasicFrame f = new BasicFrame("Fish");
        final JButton button1 = new JButton("Button 1");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(button1, "Button 1 was pressed!");
            }
        });
        button1.addActionListener(new Foo());
        f.add(layout, "topl", button1);
        f.add(layout, "topm", new JButton("Button 2"));
        f.add(layout, "topr", new JButton("Button 3"));
        f.add(layout, "row2", new JLabel("Row 2"));
        f.add(layout, "row4", new Picture("sarah.png").makeButton());
        f.add(layout, "botr", new JLabel("corner", JLabel.CENTER));

        Sprite bat = new Sprite() {
            @Override
            public void processEvent(SpriteCollisionEvent ce) {
                if (ce.eventType == CollisionEventType.WALL) {
                    if (ce.xlo) {
                        setVelX(Math.abs(getVelX()));
                    }
                    if (ce.xhi) {
                        setVelX(-Math.abs(getVelX()));
                    }
                    if (ce.ylo) {
                        setVelY(Math.abs(getVelY()));
                    }
                    if (ce.yhi) {
                        setVelY(-Math.abs(getVelY()));
                    }
                }
            }
        };

        bat.setPicture(new Picture("bat.png"));
        bat.setVelX(1);
        bat.setVelY(1);

        SpriteComponent sc = new SpriteComponent();
        Dimension d = new Dimension(800, 400);
        sc.setPreferredSize(d);

        final int nballs = 30;
        final int ballSize = 20;
        Image im1 = BasicFrame.createImage(ballSize, ballSize);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(Color.red);
        imgr.fillOval(0, 0, ballSize, ballSize);
        Image im2 = BasicFrame.createImage(ballSize, ballSize);
        imgr = im2.getGraphics();
        imgr.setColor(Color.green);
        imgr.fillOval(0, 0, ballSize, ballSize);
        final Image im3 = BasicFrame.createImage(ballSize, ballSize);
        imgr = im3.getGraphics();
        imgr.setColor(Color.blue);
        imgr.fillOval(0, 0, ballSize, ballSize);
        Picture ballPicture1 = new Picture(im1);
        ballPicture1.transparentWhite();
        Picture ballPicture2 = new Picture(im2);
        ballPicture2.transparentWhite();
        Random rand = new Random();
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("pressed: " + e.toString());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        f.addKeyListener(kl);
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        sc.addMouseListener(ml);
        for (int i = 0; i < nballs; i++) {
            Sprite sball = new Ball() {
                Double newVelX, newVelY;
                int deathCountDown;
                boolean dead = false;

                @Override
                public void processEvent(SpriteCollisionEvent ce) {
                    if (ce.eventType == CollisionEventType.WALL) {
                        if (ce.xlo) {
                            setVelX(Math.abs(getVelX()));
                        }
                        if (ce.xhi) {
                            setVelX(-Math.abs(getVelX()));
                        }
                        if (ce.ylo) {
                            setVelY(Math.abs(getVelY()));
                        }
                        if (ce.yhi) {
                            setVelY(-Math.abs(getVelY()));
                        }
                    } else if (ce.eventType == CollisionEventType.SPRITE) {
                        if (ce.sprite2 instanceof Ball) {
                            newVelX = ce.sprite2.getVelX();
                            newVelY = ce.sprite2.getVelY();
                        } else {
                            //setActive(false);
                            deathCountDown = 50;
                            dead = true;
                            setPicture(new Picture(im3));
                        }
                    }
                }

                @Override
                public void postMove() {
                    if(dead && deathCountDown > 0) {
                        deathCountDown--;
                        if(deathCountDown == 0)
                            setActive(false);
                    }
                    if (newVelX != null) {
                        setVelX(newVelX);
                        setVelY(newVelY);
                        newVelX = newVelY = null;
                    }
                }
            };
            if (i % 2 == 0) {
                sball.setPicture(ballPicture1);
            } else {
                sball.setPicture(ballPicture2);
            }
            sball.setVelX(2 * rand.nextDouble() - 1);
            sball.setVelY(2 * rand.nextDouble() - 1);
            sball.setX(rand.nextInt(d.width));
            sball.setY(rand.nextInt(d.height));
            sc.addSprite(sball);
        }

        sc.addSprite(bat);
        sc.start(0, 10);

        f.add(layout, "row3", sc);
        f.show();
    }

}
