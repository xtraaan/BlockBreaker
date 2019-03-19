package CSCProject;

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
import java.io.IOException;

/**
 * This Line class is where your character will stand.
 * @author Eilynn
 */

public class Line extends Sprite 
{
    public Picture initialPicture;

    public static Picture makeRect(Color color,int w, int h) 
    {
        Image im = BasicFrame.createImage(w, h);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        return new Picture(im);
    }
    
    public void init(SpriteComponent sc) throws IOException 
    {
        setPicture(makeRect(Game.LINE_COLOR,800,65));
        Dimension d = sc.getSize();
        setX(0);
        setY(d.height/1.18);
        setVelX(0);
        setVelY(0);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    SpriteComponent sc;
    
    @Override
    public void preMove() {
    }

    @Override
    public void processEvent(SpriteCollisionEvent se) 
    {
        if (se.eventType == CollisionEventType.SPRITE) 
        {
            if (se.sprite2 instanceof Blocks) 
            {
                if (se.xlo)
                {
                    setVelX(Math.abs(getVelX()));
                }
                if (se.xhi)
                {
                    setVelX(-Math.abs(getVelX()));
                }
                if (se.ylo)
                {
                    setVelY(Math.abs(getVelY()));
                }
                if (se.yhi)
                {
                    setVelY(-Math.abs(getVelY()));
                }
            }
        }
    }
}