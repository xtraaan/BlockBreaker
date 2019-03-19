package CSCProject;

import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;
import java.applet.AudioClip;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class is for Level 3 of the game.
 * @author Eilynn
 */

public class Blocks3 extends Sprite 
{
    SpriteComponent sc;
    static int block3Count;
    
    Blocks3() 
    { 
        block3Count++;
    }
    
    @Override
    public void setActive(boolean b) 
    {
        if(isActive() == b)
            return;
        if(b)
            block3Count++;
        else
            block3Count--;
        super.setActive(b);
    }

    public void init(SpriteComponent sc) 
    {
        setPicture(Game.makeRectangle(Game.BLOCK3_COLOR, Game.SMALLEST));
        while (true) {
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
        setVelX(4 * Game.RAND.nextDouble() - 1);
        setVelY(4 * Game.RAND.nextDouble() - 1);
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
                if(block3Count == 0) 
                {
                    try 
                    {
                        JOptionPane.showMessageDialog(sc, "Final round complete. Defeat the big boss by hitting it 5 times and win!");
                        Boss boss = new Boss();
                        boss.init(sc);
                        AudioClip death = new ReusableClip("PlayerDeath.wav");
                        death.play();
                        BossLife heart = new BossLife();
                        heart.init(sc);
                        BossLife heart2 = new BossLife();
                        heart2.init2(sc);
                        BossLife heart3 = new BossLife();
                        heart3.init3(sc);
                        BossLife heart4 = new BossLife();
                        heart4.init4(sc);
                        BossLife heart5 = new BossLife();
                        heart5.init5(sc);
                        for(int i = 0; i < 10; i++) 
                        {
                            BossBackground scoin = new BossBackground();
                            scoin.init(sc);
                        }
                    } catch (IOException ex) 
                    {
                        Logger.getLogger(Blocks3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}