package CSCProject;

import static CSCProject.Game.BLOCKS;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;
import java.applet.AudioClip;
import javax.swing.JOptionPane;

/**
 * This class is for Level 2 of the game.
 * @author Eilynn
 */

public class Blocks2 extends Sprite 
{
    SpriteComponent sc;
    static int block2Count;
    
    Blocks2() 
    { 
        block2Count++;
    }
    
    @Override
    public void setActive(boolean b) 
    {
        if(isActive() == b)
            return;
        if(b)
            block2Count++;
        else
            block2Count--;
        super.setActive(b);
    }

    public void init(SpriteComponent sc) 
    {
        setPicture(Game.makeRectangle(Game.BLOCK2_COLOR, Game.SMALLER));
        while (true) 
        {
            setX(Game.RAND.nextInt(Game.BOARD_SIZE.width)-Game.BULLET);
            setY(Game.RAND.nextInt(Game.BOARD_SIZE.height)-Game.BOTTOM);
            if (Math.abs(getX() - Game.BOARD_SIZE.width / 2) < 2 * Game.BIG
                    && Math.abs(getY() - Game.BOARD_SIZE.height / 2) < 2 * Game.BIG) 
            {
            } else 
            {
                break;
            }
        }
        setVelX(3 * Game.RAND.nextDouble() - 1);
        setVelY(3 * Game.RAND.nextDouble() - 1);
        sc.addSprite(this);
        this.sc = sc;
    }

    
    AudioClip shot = new ReusableClip("MageBurst.wav");
    
    @Override
    public void processEvent(SpriteCollisionEvent se) 
    {
        if (se.eventType == CollisionEventType.WALL) 
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
        }
        else if (se.eventType == CollisionEventType.SPRITE) 
        {
            if (se.sprite2 instanceof Line) 
            {
                setVelY(-Math.abs(getVelY()));
            }
        }

        if (se.eventType == CollisionEventType.SPRITE) 
        {
            if (se.sprite2 instanceof Bullet) 
            {
                shot.play();
                setActive(false);
                se.sprite2.setActive(false);
                if(block2Count == 0) 
                {
                    JOptionPane.showMessageDialog(sc, "Round 2 Complete."
                            + "\nGet ready for Round 3: The Final Round!");
                    for(int i = 0; i < BLOCKS; i++) 
                    {
                        Blocks3 b3 = new Blocks3();
                        b3.init(sc);
                    }
                }
            }
        }
    }
}