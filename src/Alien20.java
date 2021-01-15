import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Alien20 extends Alien {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    
    private static final int INIT_VEL_X = 3;
    
    private static final int POINTS = 20;
    
    private static final String IMG_FILE = "files/alien2Try3.png";

    private static BufferedImage img;
    
    public Alien20(int initX, int initY, int courtW, int courtH) {
        super(initX, initY, WIDTH, HEIGHT, INIT_VEL_X, courtW, courtH);
        
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