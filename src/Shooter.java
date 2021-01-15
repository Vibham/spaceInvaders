//AliensAndShips class noted in game proposal (named it Shooters because Aliens and Spaceships are )
//only objects that can shoot a missile)
import java.awt.*;

public abstract class Shooter {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object. This position should
     * always be within bounds.
     *  0 <= px <= maxX 
     *  0 <= py <= maxY 
     */
    private int px; 
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private int velX;
    private static final int VEL_Y = 0;

    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;
    
    public Shooter(int px, int py, int w, int h, int vx, int courtWidth, int courtHeight) {
        this.px = px;
        this.py = py;
        width = w;
        height = h;
        velX = vx;
        maxX = courtWidth - width;
        maxY = courtHeight - height;
    }
    
    /*** GETTERS **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return this.velX;
    }
    
    public int getVy() {
        return VEL_Y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getMaxY() {
        return this.maxY;
    }
    
    public int getMaxX() {
        return this.maxX;
    }
    
    
    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        if (px >= maxX) {
            this.px = maxX;
        } else if (px <= 0) {
            this.px = 0;
        } else {
            this.px = px;
        }
    }

    public void setPy(int py) {
        if (py >= maxY) {
            this.py = maxY;
        } else if (py <= 0) {
            this.py = 0;
        } else {
            this.py = py;
        }
    }
    
    public void setVx(int vx) {
        velX = vx;
    }
    
    /*** UPDATES AND OTHER METHODS ****************************************************************/
 
    //method to shoot that will implemented in Alien and Spaceship
    public abstract Missile shoot();
    
    public void move() {
        if (this.px + velX >= maxX) {
            this.px = maxX;
        } else if (this.px + velX <= 0) {
            this.px = 0;
        } else {
            this.px += velX;
        }
    }
    
    //aliens and spaceship will only ever intersect a missile because of the 
    //way the game is set up so only have to have this case
    public boolean intersects(Missile that) {
        return (this.px + this.width >= that.getPx()
            && this.py + this.height >= that.getPy()
            && that.getPx() + that.getWidth() >= this.px 
            && that.getPy() + that.getHeight() >= this.py);
    }
    
    //method that reflects what happens when an alien or spaceship that gets hit by a missile
    public abstract boolean missileHit(Missile missile);
    
    public abstract void draw(Graphics g);


}