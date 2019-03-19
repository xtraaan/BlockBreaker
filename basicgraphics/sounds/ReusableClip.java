package basicgraphics.sounds;


import basicgraphics.FileUtility;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.net.URL;
import java.net.ServerSocket;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.LinkedList;

/**
 * An interface to the java sound system
 * @author sbrandt
 */
public final class ReusableClip implements java.applet.AudioClip {
    public static class ClipInitializationException extends RuntimeException {
        ClipInitializationException(Exception ex) {
            super(ex);
        }
    }

    static LinkedList<ReusableClip> queue = new LinkedList<ReusableClip>();
    static synchronized void push(ReusableClip clip) {
        if(clip.bytes == null)
            return;
        if(sockLock == null)
            return;
        queue.addLast(clip);
        ReusableClip.class.notifyAll();
    }
    static synchronized ReusableClip pop() {
        while(queue.size()==0) {
            try {
                ReusableClip.class.wait();
            } catch(InterruptedException ie) {}
        }
        return queue.removeFirst();
    }

    /**
     * The Java Sound system doesn't work
     * if two sounds try to play at the same
     * time. Make sure that only one applet
     * has the sound.
     */
    static ServerSocket sockLock;
    static {
        try {
            sockLock = new ServerSocket(9585);
            new Thread() {
                public void run() {
                    while(true) {
                        ReusableClip clip = pop();
                        clip.play_();
                    }
                }
            }.start();
        } catch(java.io.IOException ioe) {
            System.out.println("Sound disabled");
        }
    }
    
    byte[] bytes;
    String sound;
    public ReusableClip(URL url,String sound) {
        try {
            init(new URL(url,sound));
        } catch(Exception ex) {
            throw new ClipInitializationException(ex);
        }
    }
    public ReusableClip(URL url) {
        init(url);
    }
    /**
     * This method will load sounds from
     * the same directory as the ReusableClip.java
     * source.
     */
    public ReusableClip(String sound) {
        if(sound == null) throw new NullPointerException();
        URL src = ReusableClip.class.getResource(sound);
        if(src == null) {
            src = FileUtility.findFile(sound);
        }
        if(src == null) 
            return;
        else
            init(src);
    }
    void init(URL url) {
        bytes = null;
        sound = url.getPath();
        int m = sound.lastIndexOf('/');
        if(m >= 0)
            sound = sound.substring(m+1);
        try {
            ByteArrayOutputStream baos =
                new ByteArrayOutputStream();
            InputStream in = url.openStream();
            byte[] buf = new byte[512];
            while(true) {
                int n = in.read(buf,0,buf.length);
                if(n <= 0)
                    break;
                baos.write(buf,0,n);
            }
            bytes = baos.toByteArray();
        } catch(Exception ex) {
            throw new ClipInitializationException(ex);
        }
    }
    public void play() {
        running = true;
        push(this);
    }
    synchronized void play_() {
        if(!running)
            return;
        try {
            ByteArrayInputStream bais =
                new ByteArrayInputStream(bytes);
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais =
                AudioSystem.getAudioInputStream(bais);
            while(true) {
                try {
                    clip.open(ais);
                    break;
                } catch(javax.sound.sampled.LineUnavailableException lux) {}
            }
            clip.start();
            long msecs = clip.getMicrosecondLength()/1000+1;
            for(int i=0;i<msecs;i++) {
                if(stopper) {
                    stopper = false;
                    break;
                }
                Thread.sleep(1);
            }
            clip.stop();
            clip.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public void loop() {
        running = true;
        while(running) {
            play();
        }
    }
    volatile boolean stopper = false;
    volatile boolean running;
    public void stop() {
        running = false;
        stopper = true;
    }

    public static void main(String[] args) throws Exception {
        java.io.File f = new java.io.File("beep.wav");
        ReusableClip clip = new ReusableClip(f.toURL());
        clip.play();
        System.out.println(clip.getClass().getResource("beep.wav"));
    }
}
