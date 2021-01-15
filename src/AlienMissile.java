//New class AlienMissile which extends Missile 

//Missiles will be a short "thick" line -- red for aliens, blue for player (ship)

import java.awt.*;

public class AlienMissile extends Missile {
    private static final Color COLOR = Color.RED;
    private static final int VEL_Y = 8;
    
    public AlienMissile(int posX, int posY, int courtWidth, int courtHeight) {
        super(posX, posY, VEL_Y, courtWidth, courtHeight, true); 

    }
    
    @Override
    public void move() {
        if (this.willHitWall()) {
            this.setPy(this.getMaxY());
        }
        
        this.setPy(this.getPy() + this.getVy());
    }
    
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(2)); 
        g1.setColor(COLOR);
        g1.drawLine(this.getPx(), this.getPy(), this.getPx(), this.getPy() + this.getHeight());
    }
    
    
}