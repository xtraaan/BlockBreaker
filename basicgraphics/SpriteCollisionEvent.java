/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

/**
 *
 * @author sbrandt
 */
public class SpriteCollisionEvent {
    public final Sprite sprite2;
    public final boolean xlo, xhi, ylo, yhi;
    public final CollisionEventType eventType;
    public SpriteCollisionEvent(boolean xlo,boolean xhi,boolean ylo,boolean yhi, CollisionEventType ct) { 
        sprite2 = null; 
        this.xlo = xlo;
        this.xhi = xhi;
        this.ylo = ylo;
        this.yhi = yhi;
        this.eventType = ct;
        if(ct == null) throw new NullPointerException();
    }
    public SpriteCollisionEvent(Sprite s2) {
        sprite2 = s2; 
        xlo = false;
        xhi = false;
        ylo = false;
        yhi = false;
        eventType = CollisionEventType.SPRITE;
    }
    private final static String PRE = "CollisionEvent[";
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PRE);
        append(sb,"xlo",xlo);
        append(sb,"xhi",xhi);
        append(sb,"ylo",ylo);
        append(sb,"yhi",yhi);
        append(sb,eventType.toString(),true);
        sb.append(']');
        return sb.toString();
    }
    private void append(StringBuilder sb,String s,boolean b) {
        if(b) {
            if(sb.length()>PRE.length())
                sb.append(',');
            sb.append(s);
        }
    }
}
