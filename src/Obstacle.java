import java.awt.*;

public class Obstacle {
    private final static int WIDTH = 2;
    private final static int HEIGHT = 5;
    
    private int px;
    private int py;
    private boolean hit;
    
    public Obstacle(int x, int y) {
        px = x;
        py = y;
        hit = false;
    }
    
    public int getPx() {
        return px;
    }
    
    public int getPy() {
        return py;
    }
    
    public boolean getIfHit() {
        return hit;
    }
    
    public boolean intersects(Missile m) {
        return (this.px + WIDTH >= m.getPx()
                && this.py + HEIGHT >= m.getPy()
                && m.getPx() + WIDTH >= this.px 
                && m.getPy() + HEIGHT >= this.py);
    }
    
    //What happens when an obstacle part is hit by a missile
    public boolean missileHit(Missile m) {
        if (!hit && intersects(m)) {
            hit = true;
            return true;
        }
        return false;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(px, py, WIDTH, HEIGHT);
    }
    
    
}