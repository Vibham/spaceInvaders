//New class ShipMissile which extends GameObj 

//Missiles will be a short "thick" line -- white for aliens, red for player (ship)
//MISSILE POSITION _ LOWER LEFT CORNER
import java.awt.*;

public class ShipMissile extends Missile {
    //not final or static because position is dependent on where the alien
    //that the missile comes from is
    private static final Color COLOR = Color.CYAN;
    private static final int VEL_Y = 10;
    
    public ShipMissile(int posX, int posY, int courtWidth, int courtHeight) {
        super(posX, posY, VEL_Y, courtWidth, courtHeight, false); 
    }
    
    @Override
    public void move() {
        if (this.willHitWall()) {
            this.setPy(this.getMaxY());
        }
        
        this.setPy(this.getPy() - this.getVy());
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(2)); 
        g1.setColor(COLOR);
        g1.drawLine(this.getPx(), this.getPy(), this.getPx(), this.getPy() - this.getHeight());
    }
    
    
}