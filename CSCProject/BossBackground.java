package CSCProject;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.io.IOException;
import java.util.Random;

/**
 * This class is just to add to the
 * background of the boss level.
 * @author Eilynn
 */

public class BossBackground extends Sprite
{
    public Picture basePic;
    SpriteComponent sc;
    Random rand = new Random();
    
    public void init(SpriteComponent sc) throws IOException
    {
        basePic = new Picture("scoin.png");
        setPicture(basePic);
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
        this.sc = sc;
        sc.addSprite(this);
    };
}