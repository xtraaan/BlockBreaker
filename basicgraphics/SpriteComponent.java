/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;

/**
 *
 * @author sbrandt
 */
public class SpriteComponent extends JComponent {

    Timer t = null;
    private final List<Sprite> sprites = new ArrayList<>();

    public SpriteComponent() {
        setPreferredSize(new Dimension(100, 100));
    }

    public void addSprite(Sprite sp) {
        if(sp.getPicture()==null)
            throw new NullPointerException("Sprite has a null picture");
        sprites.add(sp);
    }

    public void removeSprite(Sprite sp) {
        sprites.remove(sp);
    }

    public void start(int delay, int period) {
        if (t != null) {
            throw new GuiException("SpriteComponent already started");
        }
        t = new Timer();
        TimerTask tt = new TimerTask() {
            int toc = 0;
            @Override
            public void run() {
                TaskRunner.runLater(SpriteComponent.this, new Runnable() {
                    @Override
                    public void run() {
                        moveSprites();
                    }
                });
            }
        };
        t.schedule(tt, delay, period);
    }

    public void paintBackground(Graphics g) {
        Dimension d = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);
    }

    public final void paintSprites(Graphics g) {
        Collections.sort(sprites, DRAWING_PRIORITY);
        for (Sprite sprite : new ArrayList<>(sprites)) {
            int xv = (int) (sprite.getX());
            int yv = (int) (sprite.getY());
            g.drawImage(sprite.getPicture().getImage(), xv, yv, null);
        }
    }

    Image image;
    Dimension sz;

    @Override
    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        if (image == null || sz.width != d.width || sz.height != d.height) {
            image = createImage(d.width, d.height);
            sz = d;
        }
        Graphics gi = image.getGraphics();
        paintBackground(gi);
        paintSprites(gi);
        g.drawImage(image, 0, 0, this);
    }

    /**
     * Simple version of the collision detector. It is O(n^2) where n is the
     * number of sprites. Not ideal if there are lots of sprites.
     */
    public void detectCollisions1(List<Sprite> spriteLoop) {
        ageCollisions();
        int n = spriteLoop.size();
        for (int i = 0; i < n; i++) {
            Sprite sp1 = spriteLoop.get(i);
            for (int j = i + 1; j < n; j++) {
                Sprite sp2 = spriteLoop.get(j);
                if (Sprite.overlap(sp1, sp2)) {
                    if (!addCollision(sp1,sp2)) {
                        sp1.processEvent(new SpriteCollisionEvent(sp2));
                        sp2.processEvent(new SpriteCollisionEvent(sp1));
                    }
                }
            }
        }
        clearCollisions();
    }
    
    Map<Sprite,Map<Sprite,CollisionState>> collisions = new HashMap<>();
    
    boolean addCollision(Sprite sp1,Sprite sp2) {
        int k1 = System.identityHashCode(sp1);
        int k2 = System.identityHashCode(sp2);
        if(k2 < k1) {
            Sprite tmp = sp1;
            sp1 = sp2;
            sp2 = tmp;
        }
        Map<Sprite,CollisionState> subMap = collisions.get(sp1);
        if(subMap == null) {
            subMap = new HashMap<>();
            collisions.put(sp1,subMap);
        }
        if(subMap.containsKey(sp2)) {
            return false;
        } else {
            subMap.put(sp2,CollisionState.NEW);
            return true;
        }
    }

    public static Comparator<Sprite> COMPX = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite a, Sprite b) {
            double d = a.getX() - b.getX();
            if (d < 0) {
                return -1;
            }
            if (d > 0) {
                return 1;
            }
            return 0;
        }
    };

    public static Comparator<Sprite> DRAWING_PRIORITY = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite a, Sprite b) {
            double d = a.getDrawingPriority() - b.getDrawingPriority();
            if (d < 0) {
                return -1;
            }
            if (d > 0) {
                return 1;
            }
            return 0;
        }
    };

    /**
     * This should be a faster version of detecting collisions. It's still
     * O(n^2), unfortunately.
     */
    public void detectCollisions(List<Sprite> spriteLoop) {
        int n = spriteLoop.size();
        Collections.sort(spriteLoop, COMPX);
        for (int i = 0; i < n; i++) {
            Sprite sp1 = spriteLoop.get(i);
            for (int ii = i + 1; ii < n; ii++) {
                Sprite sp2 = spriteLoop.get(ii);
                if (Sprite.overlapx(sp1, sp2)) {
                    if (Sprite.overlapy(sp1, sp2)) {
                        // At this point we know the boxes overlap. Now to
                        // detect if drawing the images interferes.
                        if (overlapImage(sp1, sp2)) {
                            if (!addCollision(sp1, sp2)) {
                                sp1.processEvent(new SpriteCollisionEvent(sp2));
                                sp2.processEvent(new SpriteCollisionEvent(sp1));
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
        clearCollisions();
    }
    
    /**
     * Override to cause things to happen before a move.
     */
    public void preMove() {}
    
    /**
     * Override to cause things to happen after a move,
     * but before a repaint.
     */
    public void postMove() {}

    public void moveSprites() {
        preMove();
        Dimension d = getSize();
        if (d.width == 0 || d.height == 0) {
            return;
        }
        for (Iterator<Sprite> iter = sprites.iterator(); iter.hasNext();) {
            Sprite sp = iter.next();
            if (!sp.isActive()) {
                iter.remove();
            }
        }
        List<Sprite> spriteLoop = new ArrayList<>(sprites.size());
        spriteLoop.addAll(sprites);
        for (Sprite sp : spriteLoop) {
            sp.preMove();
            sp.move(d);
        }
        detectCollisions(spriteLoop);
        for (Sprite sp : spriteLoop) {
            sp.postMove();
        }
        postMove();
        repaint();
    }

    /**
     * Usually you subclass a MouseAdapter to handle mouse events. It should
     * make things simpler for you if you give your MouseAdapters to the
     * component rather than the BasicFrame.
     *
     * @param kl
     */
    @Override
    public void addMouseListener(MouseListener ml) {
        super.addMouseListener(new MouseWrapper(ml));
    }

    private void ageCollisions() {
        for(Map<Sprite,CollisionState> me : collisions.values()) {
            for(Map.Entry<Sprite, CollisionState> ms : me.entrySet()) {
                ms.setValue(CollisionState.OLD);
            }
        }
    }

    private void clearCollisions() {
        for(Map<Sprite,CollisionState> me : collisions.values()) {
            Iterator<Map.Entry<Sprite, CollisionState>> ms = me.entrySet().iterator();
            while(ms.hasNext()) {
                Map.Entry<Sprite, CollisionState> entry = ms.next();
                if(entry.getValue() == CollisionState.OLD) {
                    ms.remove();
                }
            }
        }
    }
    
    int range(double d) {
        int n = (int)d;
        if(n < 0) return n;
        if(n > getWidth()) return getWidth();
        return n;
    }
    
    boolean overlapImage(Sprite sp1,Sprite sp2) {
        int xl1 = range(sp1.getX());
        int xh1 = xl1+sp1.getPicture().getWidth();
        
        int yl1 = (int)sp1.getY();
        int yh1 = yl1+sp1.getPicture().getHeight();
        
        int xl2 = (int)sp2.getX();
        int xh2 = (int)(sp2.getX()+sp2.getWidth());
        
        int yl2 = (int)sp2.getY();
        int yh2 = (int)(sp2.getY()+sp2.getHeight());
        
        int xl = xl1 > xl2 ? xl1 : xl2;
        int xh = xh1 < xh2 ? xh1 : xh2;
        
        if(xh < xl) return false;
        
        int yl = yl1 > yl2 ? yl1 : yl2;
        int yh = yh1 < yh2 ? yh1 : yh2;
        
        if(yh < yl) return false;
        
        Picture p1 = sp1.getPicture();
        Picture p2 = sp2.getPicture();
        for (int j = yl; j < yh; j++) {
            for (int i = xl; i < xh; i++) {
                if (p1.mask(i - xl1, j - yl1) && p2.mask(i - xl2, j - yl2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
