import java.awt.Graphics;

//New abstact class for Alien objects that will be in 2D array


public abstract class Alien extends Shooter {
    private static int courtWidth;
    private static int courtHeight;
   
    private boolean alive;


    public Alien(int xI, int yI, int width, int height, int initialVX, int cWidth, int cHeight) {
        super(xI, yI, width, height, initialVX, cWidth, cHeight);
        courtWidth = cWidth;
        courtHeight = cHeight;
        alive = true;
        
    }
    
    public boolean getAlive() {
        return alive;
    }
    
    //implements abstract method, returns an AlienMissile
    @Override
    public Missile shoot() {
        Missile alienShoot = new AlienMissile(
                this.getPx() + this.getWidth() / 2, this.getPy() + this.getHeight(), 
                courtWidth, courtHeight);
        return alienShoot;
    }
    
    //what happens when an alien is hit by a ship missile
    @Override
    public boolean missileHit(Missile m) {
        if (!m.getAlienOrShip() && this.intersects(m) && this.alive) {
            alive = false;
            return true;
        }
        return false;
    }
    
    //returns if it hit one of left or right walls
    public boolean hitWall() {
        if (this.getPx() <= 0 || this.getPx() >= this.getMaxX()) {
            return true;
        } 
        return false;
    }
    
    //abstract because each alien type has a different number of points
    public abstract int getPoints();
    
    //will implement draw in individual alien classes
    public abstract void draw(Graphics g);
    
}