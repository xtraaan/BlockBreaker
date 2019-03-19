package CSCProject;

import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.io.IOException;

/**
 * This warrior class will be the character you control.
 * @author Eilynn
 */

public class Warrior extends Sprite 
{
    public Picture initialPic;
    
     public void init(SpriteComponent sc) throws IOException 
     {
        initialPic = new Picture("Walkdown.png");
        setPicture(initialPic);
        Dimension d = sc.getSize();
        setX(d.width/2);
        setY(d.height-63);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    public void setLeft(boolean b) 
    {
        if(b == true)
            setVelX(-3);
        else
            setVelX(0);
    }
    
    public void setRight(boolean b) 
    {
        if(b == true)
            setVelX(3);
        else
            setVelX(0);
    }
    
    SpriteComponent sc;

    @Override
    public void processEvent(SpriteCollisionEvent se) 
    {
        if (se.xlo) 
        {
            setX(sc.getSize().width-getWidth());
        }
        if (se.xhi) 
        {
            setX(0);
        }
        if (se.ylo) 
        {
            setY(sc.getSize().height-getHeight());
        }
        if (se.yhi) 
        {
            setY(0);
        }
    }
}