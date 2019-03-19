package CSCProject;

import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
import java.applet.AudioClip;
import javax.swing.JOptionPane;

/**
 * This class is for boss level of the game.
 * @author Eilynn
 */
 
public class Boss extends Sprite 
{
    SpriteComponent sc;
    static int bossCount = 4;
    
    Boss() 
    { 
        bossCount++;
    }
    
    @Override
    public void setActive(boolean b) 
    {
        if(isActive() == b)
            return;
        if(b)
            bossCount++;
        else
            bossCount--;
        super.setActive(b);
    }

    public void init(SpriteComponent sc) 
    {
        setPicture(new Picture("Alien.png"));
        setVelX(10 * Game.RAND.nextDouble() - 1);
        setVelY(10 * Game.RAND.nextDouble() - 1);
        sc.addSprite(this);
        this.sc = sc;
    }
    
    AudioClip shot = new ReusableClip("MageBurst.wav");

    @Override
    public void processEvent(SpriteCollisionEvent se) 
    {
        if (se.eventType == CollisionEventType.WALL) 
        {
            if (se.xlo) {
                setVelX(Math.abs(getVelX()));
            }
            if (se.xhi) {
                setVelX(-Math.abs(getVelX()));
            }
            if (se.ylo) {
                setVelY(Math.abs(getVelY()));
            }
        }
        else if (se.eventType == CollisionEventType.SPRITE) 
        {
            if (se.sprite2 instanceof Line) {
                setVelY(-Math.abs(getVelY()));
            }
        }

        if (se.eventType == CollisionEventType.SPRITE) 
        {
            if (se.sprite2 instanceof Bullet) 
            {
                shot.play();
                se.sprite2.setActive(false);
                bossCount--;
                if (bossCount == 4)
                {
                    BossLife heart5 = new BossLife();
                    heart5.remove(sc);
                }
                else if (bossCount == 3)
                {
                    BossLife heart4 = new BossLife();
                    heart4.remove(sc);
                }
                else if (bossCount == 2)
                {
                    BossLife heart5 = new BossLife();
                    heart5.remove(sc);
                }
                else if (bossCount == 1)
                {
                    BossLife heart = new BossLife();
                    heart.remove(sc);
                }
                if(bossCount == 0) 
                {
                    setActive(false);
                    
                    JOptionPane.showMessageDialog(sc, "Congratulations!"
                            + "\nYou won!");
                }
            }
        }
    }
}