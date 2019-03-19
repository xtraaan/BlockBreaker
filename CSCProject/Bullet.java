package CSCProject;

import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import java.awt.event.KeyEvent;

/**
 * This is the bullet class used to break blocks.
 * @author Eilynn
 */

class Bullet extends Sprite 
{
    
    public static int bullets = 300;
    
    Bullet(){
    }
    
    public int getBullets() 
    {
        return bullets;
    }
    
    
    public void init(SpriteComponent sc,Sprite sp,int direction) 
    {
        setPicture(Game.makeBall(Game.BULLET_COLOR, Game.BULLET));
        setCenterX(sp.centerX());
        setCenterY(sp.centerY());
        if(direction == KeyEvent.VK_SPACE)
        {
            bullets--;
            setVelY(-2.0);
            sc.addSprite(this);
        }
    }
    
    @Override
    public void processEvent(SpriteCollisionEvent se) 
    {
        if(se.sprite2 instanceof Warrior) 
        {
        }
        else if (se.sprite2 instanceof Line) 
        {
        }
        else 
        {
            setActive(false);
        }
    }

    void init(SpriteComponent sc, Warrior sp, int x, int y) 
    {
        setPicture(Game.makeRectangle(Game.BULLET_COLOR, Game.BULLET));
        setX(sp.getX()+(Game.BIG-Game.BULLET)/2);
        setY(sp.getY()+(Game.BIG-Game.BULLET)/2);
        double delx = x-sp.getX()-sp.getWidth()/2;
        double dely = y-sp.getY()-sp.getHeight()/2;
        double dist = Math.sqrt(delx*delx+dely*dely);
        setVelX(2*delx/dist);
        setVelY(2*dely/dist);
        sc.addSprite(this);
    }
}