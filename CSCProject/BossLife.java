package CSCProject;

import static CSCProject.Boss.bossCount;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.io.IOException;

/**
 * This class lets you know much life the boss has left.
 * @author Eilynn
 */

public class BossLife extends Sprite
{
    public Picture one;
    public Picture two;
    public Picture three;
    public Picture four;
    public Picture five;
    SpriteComponent sc;
    
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
    
    public void init(SpriteComponent sc) throws IOException
    {
        one = new Picture("heart.png");
        setPicture(one);
        setX(1);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    public void init2(SpriteComponent sc) throws IOException
    {
        two = new Picture("heart.png");
        setPicture(two);
        setX(51);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    public void init3(SpriteComponent sc) throws IOException
    {
        three = new Picture("heart.png");
        setPicture(three);
        setX(101);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    public void init4(SpriteComponent sc) throws IOException
    {
        four = new Picture("heart.png");
        setPicture(four);
        setX(151);
        this.sc = sc;
        sc.addSprite(this);
    }
    
    public void init5(SpriteComponent sc) throws IOException
    {
        five = new Picture("heart.png");
        setPicture(five);
        setX(201);
        this.sc = sc;
        sc.addSprite(this);
    }

    void remove(SpriteComponent sc) {
        setActive(false);    
    }
}