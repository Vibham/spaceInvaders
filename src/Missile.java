import java.awt.Graphics;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class Missile {
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
    private static final int WIDTH = 2;
    private static final int HEIGHT = 7;

    /* Velocity: number of pixels to move every time move() is called. */
    private static final int VEL_X = 0;
    private int vY;

    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;
    
    //True if the missile belongs to an alien
    private final boolean alienOrShip;
    /**
     * Constructor
     */
    public Missile(int px, int py, int velY, int courtWidth, int courtHeight, boolean aOrS) {
        this.px = px;
        this.py = py;
        
        vY = velY;

        // take the width and height into account when setting the bounds for the upper left corner
        // of the object.
        this.maxX = courtWidth - WIDTH;
        this.maxY = courtHeight - HEIGHT;
        
        alienOrShip = aOrS;
        
        
    }

    /*** GETTERS **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return VEL_X;
    }
    
    public int getVy() {
        return vY;
    }
    
    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }
    
    public int getMaxY() {
        return this.maxY;
    }
    
    public boolean getAlienOrShip() {
        return this.alienOrShip;
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
    

    /*** UPDATES AND OTHER METHODS ****************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area designated for the object.
     * (i.e. Object cannot go outside of the active area the user defines for it).
     */ 

    public boolean willHitWall() {
        if (alienOrShip) {
            return (this.py + vY >= maxY);
        } else {
            return (this.py - vY <= 0);
        }
    }

    public abstract void move();

    /**
     * Determine whether this game object is currently intersecting another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the bounding boxes overlap, then
     * an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(Missile that) {
        return (this.px + WIDTH >= that.px
            && this.py + HEIGHT >= that.py
            && that.px + WIDTH >= this.px 
            && that.py + HEIGHT >= this.py);
    }
  

    /**
     * Default draw method that provides how the object should be drawn in the GUI. This method does
     * not draw anything. Subclass should override this method based on how their object should
     * appear.
     * 
     * @param g The <code>Graphics</code> context used for drawing the object. Remember graphics
     * contexts that we used in OCaml, it gives the context in which the object should be drawn (a
     * canvas, a frame, etc.)
     */
    public abstract void draw(Graphics g);
}
