import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Alien100 extends Alien {
    private static final int INIT_PX = 350;
    private static final int INIT_PY = 75;
    
    private static final int WIDTH = 60;
    private static final int HEIGHT = 25;
    
    private static final int INIT_VEL_X = 6;
    
    private static final int POINTS = 100;
    
    private static final String IMG_FILE = "files/alienUFO1.png";
    private static BufferedImage img;
    
    public Alien100(int courtW, int courtH) {
        super(INIT_PX, INIT_PY, WIDTH, HEIGHT, INIT_VEL_X, courtW, courtH);
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    @Override 
    public int getPoints() {
        return POINTS;
    }
    
    @Override 
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        
    }
}