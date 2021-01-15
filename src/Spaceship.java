//Spaceship class that extends Shooter
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO; 

public class Spaceship extends Shooter {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 30;
    
    private static final int INIT_X = 349;
    private static final int INIT_Y = 450;
    
    private static final int INIT_VEL_X = 0; 
    
    private static int lives;
    
    private static int courtWidth;
    private static int courtHeight;
    
    private static final String IMG_FILE = "files/spaceship.png";
    private static BufferedImage img;

    public Spaceship(int cWidth, int cHeight) {
        super(INIT_X, INIT_Y, WIDTH, HEIGHT, INIT_VEL_X, cWidth, cHeight);
        lives = 3;
        courtWidth = cWidth;
        courtHeight = cHeight;
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public int getLives() {
        return lives;
    }
    
    @Override
    public Missile shoot() {
        Missile shipShoot = new ShipMissile(
                this.getPx() + 35, this.getPy(), courtWidth, courtHeight);
        return shipShoot;
    }
    
    @Override
    public boolean missileHit(Missile m) {
        if (m.getAlienOrShip() && this.intersects(m) && lives > 0) {
            lives = Math.max(0, lives - 1);
            return true;
        }
        return false;
    }

    //start of here, how to add indiv. files (pngs) to eclipse
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
    
}