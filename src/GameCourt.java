/**
3 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Spaceship spaceship;
    private Alien[][] aliens;
    private Alien alienUfo;
    private int points;
    private List<Missile> missiles;
    private Obstacle[][] obstacle1;
    private Obstacle[][] obstacle2;
    private Obstacle[][] obstacle3;
    private Obstacle[][] obstacle4;
    
    private boolean playing = false;
    private JLabel status;
    
    private String username;
//    private JOptionPane usernameIn = JOptionPane("Please enter a username!");
//    
    private final static String IMG_NAME = "files/spaceBackground1.jpeg"; 
    private Image bgImage; 
    
    private static final int ALIEN_ARRAY_HEIGHT = 3;
    private static final int ALIEN_ARRAY_WIDTH = 8;
    private static final int ALIEN_ARR_INIT_X = 157;
    private static final int ALIEN_ARR_INIT_Y = 110;
    private static final int ROWS_OF_ALIEN10 = 1;
    private static final int ROWS_OF_ALIEN20 = 2;
    
    private static final int OBSTACLE_HEIGHT = 2;
    private static final int OBSTACLE_WIDTH = 40;
    private static final int OBSTACLE1_INIT_X = 86;
    private static final int OBSTACLE1_INIT_Y = 365;
    
    private static final int COURT_WIDTH = 750;
    private static final int COURT_HEIGHT = 500;
    private static final int SPACESHIP_VEL = 4;
    
    private static final int INTERVAL = 35;
    private static final int ALIEN_SHOOTING_INTERVAL = 700;
    private static final int SHIP_SHOOTING_INTERVAL = 1050;
    private boolean isShipShooting;
    private long lastShipShotTime;
    
    private static final String FILE_PATH = "files/highScores.txt";
    private static final String UPDATING_FILE_PATH = "files/updating.txt";
    
    
    public GameCourt(JLabel status) {
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
                
            }
        });
        timer.start();
        timer.setInitialDelay(0);
        
        setFocusable(true);
        
        //Timer for when aliens shoot
        Timer timerAlienShoot = new Timer(ALIEN_SHOOTING_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shootTick();
            }
        });
        
        timerAlienShoot.start();
        timerAlienShoot.setInitialDelay(0);
              
        //controls - left arrow key/A, right arrow key/D, spacebar (to shoot)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                    spaceship.setVx(-SPACESHIP_VEL);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                    spaceship.setVx(SPACESHIP_VEL);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isShipShooting = true;
                } 
            }
            
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT
                        || e.getKeyCode() == KeyEvent.VK_A ||  e.getKeyCode() == KeyEvent.VK_D) {
                    spaceship.setVx(0);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isShipShooting = false;
                }
                
            }
        });
        
        
        this.status = status;
        
    }
     
    
    public void reset() {
        // Reading background image
        try {
            bgImage = ImageIO.read(new File(IMG_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        // Initializing game objects
        spaceship = new Spaceship(COURT_WIDTH, COURT_HEIGHT);
        alienUfo = new Alien100(COURT_WIDTH, COURT_HEIGHT);
        
        // Setting points to 0
        points = 0;
        
        // Setting up restricting how quickly the ship can shoot
        isShipShooting = false;
        lastShipShotTime = System.nanoTime();
        
        // Initialize alien array 
        int alienStartX = ALIEN_ARR_INIT_X;
        int alienStartY = ALIEN_ARR_INIT_Y;
        
        aliens = new Alien[ALIEN_ARRAY_HEIGHT][ALIEN_ARRAY_WIDTH];
        
        for (int i = 0; i < ROWS_OF_ALIEN20; i++) {
            for (int j = 0; j < ALIEN_ARRAY_WIDTH; j++) {
                aliens[i][j] = new Alien20(
                        alienStartX, alienStartY, COURT_WIDTH, COURT_HEIGHT);
                alienStartX += 55;
            }
            alienStartX = ALIEN_ARR_INIT_X;
            alienStartY += 55;
        }
        
        for (int i = ROWS_OF_ALIEN20; i < ROWS_OF_ALIEN10 + ROWS_OF_ALIEN20; i++) {
            for (int j = 0; j < ALIEN_ARRAY_WIDTH; j++) {
                aliens[i][j] = new Alien10(
                        alienStartX, alienStartY, COURT_WIDTH, COURT_HEIGHT);
                alienStartX += 55;
            }
            alienStartX = ALIEN_ARR_INIT_X;
            alienStartY += 40;
        }
        
        // Initializing obstacles
        int obStartX = OBSTACLE1_INIT_X;
        int obStartY = OBSTACLE1_INIT_Y;
        
        obstacle1 = new Obstacle[OBSTACLE_HEIGHT][OBSTACLE_WIDTH];
        obstacle2 = new Obstacle[OBSTACLE_HEIGHT][OBSTACLE_WIDTH];
        obstacle3 = new Obstacle[OBSTACLE_HEIGHT][OBSTACLE_WIDTH];
        obstacle4 = new Obstacle[OBSTACLE_HEIGHT][OBSTACLE_WIDTH];
        
        for (int i = 0; i < OBSTACLE_HEIGHT; i++) {
            for (int j = 0; j < OBSTACLE_WIDTH; j++) {
                obstacle1[i][j] = new Obstacle(obStartX, obStartY);
                obstacle2[i][j] = new Obstacle(obStartX + 166, obStartY);
                obstacle3[i][j] = new Obstacle(obStartX + 332, obStartY);
                obstacle4[i][j] = new Obstacle(obStartX + 498, obStartY);
                obStartX += 2;
            }
            obStartX = OBSTACLE1_INIT_X;
            obStartY += 5;
        }
        
        // Creating missiles list
        missiles = new LinkedList<Missile>();
        
        // Setting status text
        status.setText("Lives: " + spaceship.getLives() + " Points: " + points);
        
        // Shows instructions when you first open the game 
        showInstructions();

        // Getting username + validating that it has up to 12 alphanumeric characters with no spaces
        boolean validInput = false;
        while (!validInput) {
            username = JOptionPane.showInputDialog(this, "Please enter a username to play!");
            if (username != null) {
                username = username.trim();
                if (!username.matches(".*[\\W&&[^']].*") && 
                        username.split(" ").length == 1 && username.length() <= 12
                        && username.length() > 0) {
                    validInput = true;
                    playing = true;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a username with up to 12 alphanumberic characters "
                            + "and no spaces");
                }
            } else {
                validInput = true;
                playing = false;
            } 
            
        }

        requestFocusInWindow();
    }
    
    
    void tick() {
        if (playing) {
            spaceship.move();
            alienUfo.move();
            
            // Checking if the ship can shoot - spacebar must be pressed + enough time passed from 
            //last shot
            if (isShipShooting && 
                    (System.nanoTime() - lastShipShotTime) >= (SHIP_SHOOTING_INTERVAL * 1000000)) {
                missiles.add(spaceship.shoot());
                lastShipShotTime = System.nanoTime();
            }
            
            // If alienUfo hits a wall, it should move in the other directions
            if (alienUfo.hitWall()) {
                alienUfo.setVx(-alienUfo.getVx());
            }
            
            // Moving alien array
            for (Alien[] row : aliens) {
                for (Alien a : row) {
                    a.move();
                }
            }
            
            // If aliens in the left-most column or right-most column hit a wall, array should move
            //opposite direction
            if (aliens[0][0].hitWall() || aliens[0][ALIEN_ARRAY_WIDTH - 1].hitWall()) { 
                for (Alien[] row : aliens) {
                    for (Alien a : row) {
                        a.setVx(-a.getVx()); 
                    }
                }
            }

            
            // Tracks what missiles should be removed from missile list and no longer drawn
            LinkedList<Missile> missilesToRemove = new LinkedList<Missile>();
            
            for (Missile m : missiles) {
                m.move();
                
                // If missile hits the top or bottom wall, it should be removed
                if (m.willHitWall()) {
                    missilesToRemove.add(m);
                }
                
                // If any of the missiles hit alienUfo it should disappear and the correct number 
                // of points should be added
                if (alienUfo.missileHit(m)) {
                    points += alienUfo.getPoints();
                    missilesToRemove.add(m);
                }
                
                // If any of missiles hit the spaceship, it should be removed (-1 life taken care 
                // of in the method itself
                if (spaceship.missileHit(m)) {
                    missilesToRemove.add(m);
                }
                
                // If any alien gets hit, that missile should be removed and the correct number of 
                // points should be added
                for (Alien[] row : aliens) {
                    for (Alien a : row) {
                        if (a.missileHit(m)) {
                            missilesToRemove.add(m);
                            points += a.getPoints();
                        }
                    }
                }
                
                // If any of the obstacles are hit, that missile should be removed
                for (Obstacle[] row : obstacle1) {
                    for (Obstacle o : row) {
                        if (o.missileHit(m)) {
                            missilesToRemove.add(m);
                        } 
                    }
                }
                
                for (Obstacle[] row : obstacle2) {
                    for (Obstacle o : row) {
                        if (o.missileHit(m)) {
                            missilesToRemove.add(m);
                        } 
                    }
                }
                
                for (Obstacle[] row : obstacle3) {
                    for (Obstacle o : row) {
                        if (o.missileHit(m)) {
                            missilesToRemove.add(m);
                        } 
                    }
                }
                
                for (Obstacle[] row : obstacle4) {
                    for (Obstacle o : row) {
                        if (o.missileHit(m)) {
                            missilesToRemove.add(m);
                        } 
                    }
                }
                
                // Checks if a missile hit another missile
                for (Missile m1 : missiles) {
                    if (m.intersects(m1) && m != m1) {
                        missilesToRemove.add(m);
                    }
                }
            }
            
            // Actually removing missiles
            for (Missile m : missilesToRemove) {
                missiles.remove(m);
            }
            
            // Writing username + score to file and then reading top 3 high scores
            if (!alienUfo.getAlive() && allAliensDead()) {
                points += spaceship.getLives() * 100;
                playing = false;
                status.setText("You won! Lives: " + spaceship.getLives() + " Points: " + points);
                addUsernameAndPoints();
                JOptionPane.showMessageDialog(this, "You won! Lives: "  + spaceship.getLives() + 
                        " Points: " + points + "\nHigh Scores \n" + getUsersAndHighScores());
            } else if (spaceship.getLives() == 0) {
                status.setText("You lost! Lives: 0 Points: " + points);
                playing = false;
                addUsernameAndPoints();
                JOptionPane.showMessageDialog(this, "You lost! Points: " + points + 
                        "\nHigh Scores \n" + getUsersAndHighScores());
            } else {
                status.setText("Player: " + username + " Lives: " + spaceship.getLives() + 
                        " Points: " + points);
            }
            
            repaint();
        }
    }
    
    // Alien shooting
    void shootTick() {
        if (playing) {
            // alienUfo is constantly shooting
            if (alienUfo.getAlive()) {
                missiles.add(alienUfo.shoot());
            }
            
            // Aliens in array should keep attacking until all of them are dead 
            if (!allAliensDead()) {
                missiles.add(alienAttack());                 
            }
 
        }
                
    }
    
    // randomly chooses which alien should attack until one that is alive is chosen
    private Missile alienAttack() {
        int x = 0;
        int y = 0;
        do {
            x = (int) (Math.random() * ALIEN_ARRAY_HEIGHT);
            y = (int) (Math.random() * ALIEN_ARRAY_WIDTH); 
        } while (!aliens[x][y].getAlive());
        
        return (aliens[x][y].shoot());
               
    }
    
    // Checks if all aliens in array are dead
    private boolean allAliensDead() {
        for (Alien[] row : aliens) {
            for (Alien a : row) {
                if (a.getAlive()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Read and parse highScores.txt file until you find where the score from current game fits in 
    // descending order while copying all of this to the updating.txt file, adding current game 
    // score. Then, finish copying all of highScores and then copying all of updating.txt back to 
    // highScores.txt
    
    private void addUsernameAndPoints() {
        boolean added = false;
        String s = username + " " + points;
        File file = Paths.get(FILE_PATH).toFile();
        File fileUpdating = Paths.get(UPDATING_FILE_PATH).toFile();
        BufferedWriter bw = null;
        BufferedReader br = null;
        BufferedWriter bwUpdating = null;
        BufferedReader brUpdated = null;
        try {
            br = new BufferedReader(new FileReader(file));
            bwUpdating = new BufferedWriter(new FileWriter(fileUpdating, false));
            String str = br.readLine();

            
            while (str != null) {
                if (!str.equals("")) {
                    if (points <= Integer.parseInt(str.split(" ")[1]) || added) {
                        bwUpdating.write(str, 0, str.length());
                        bwUpdating.newLine();
                        str = br.readLine();
                    } else if (!added) {
                        bwUpdating.write(s, 0, s.length()); 
                        bwUpdating.newLine();
                        added = true;
                    }
                } 
            }
            
            if (!added) {
                bwUpdating.write(s, 0, s.length());
                added = true;
            }
            
            bwUpdating.flush();
            bwUpdating.close();
        } catch (IOException e) {
            System.out.println("Encountered IOException");
        }
        
        try {
            brUpdated = new BufferedReader(new FileReader(fileUpdating));
            bw = new BufferedWriter(new FileWriter(file, false));
            String line = brUpdated.readLine();
            while (line != null) {
                bw.write(line, 0, line.length());
                bw.newLine();
                line = brUpdated.readLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error updating scores");
        }
        
    }
    
    // Reading top 3 lines to get top 3 high scores
    private String getUsersAndHighScores() {
        BufferedReader br = null;
        List<String> highScores = new LinkedList<String>();
        int count = 0;
        
        
        try {
            br = new BufferedReader(new FileReader(FILE_PATH));
            String s = br.readLine();
            while (s != null && count < 3) {
                highScores.add(s);
                s = br.readLine();
                count++;
            }
        } catch (IOException e) {
            //can add option pane
            System.out.println("Encountered IOException");
        }
        
        String highScoresOut = "";
        for (String str: highScores) {
            highScoresOut = highScoresOut + str + "\n";
        }
        
        return highScoresOut;
    }
    
    // Showing instructions - happens at beginning of game and when "Instructions" button is pressed
    // If playing, game becomes paused until player presses ok on popup
    public void showInstructions() {
        if (playing) {
            playing = false;
            int result = JOptionPane.showConfirmDialog(this, "Welcome to Space Invaders!\n"
                    + "The slightly glitchy (but still fun) game where you can shoot aliens with "
                    + "your"
                    + "\n" 
                    + "very own spaceship.To move the spaceship left or right, use the left or "
                    + "right"
                    + "\n"
                    + "arrow keys. To shoot, press the spacebar. You can use the white rectangles "
                    + "as"
                    + "\n"
                    + "cover while the aliens are shooting. The first two rows of aliens are worth "
                    + "20" 
                    + "\n"
                    + "pts each and the aliens in the last row are worth 10 pts. The mothership is"
                    + "\n"
                    + "worth 100 points. The aliens will be shooting at you though and each time "
                    + "you"
                    + "\n"
                    + "get shot, you lose a life. You only have 3 so good luck and don't die!",
                    "Instructions", 
                    JOptionPane.DEFAULT_OPTION);
            if (result == 0) {
                playing = true;
            }  
        } else {
            JOptionPane.showConfirmDialog(this, "Welcome to Space Invaders!\n"
                    + "The slightly glitchy (but still fun) game where you can shoot aliens with "
                    + "your"
                    + "\n" 
                    + "very own spaceship.To move the spaceship left or right, use the left or "
                    + "right"
                    + "\n"
                    + "arrow keys. To shoot, press the spacebar. You can use the white rectangles "
                    + "as"
                    + "\n"
                    + "cover while the aliens are shooting. The first two rows of aliens are worth "
                    + "20" 
                    + "\n"
                    + "pts each and the aliens in the last row are worth 10 pts. The mothership is"
                    + "\n"
                    + "worth 100 points. The aliens will be shooting at you though and each time "
                    + "you"
                    + "\n"
                    + "get shot, you lose a life. You only have 3 so good luck and don't die!",
                    "Instructions", 
                    JOptionPane.DEFAULT_OPTION);
        }
        
        requestFocusInWindow();
    }
    
    //Clear all scores saved
    public void clearScores() {
        try {
            PrintWriter writer = new PrintWriter(FILE_PATH);
            writer.print("");
            writer.close();
            if(playing) {
                playing = false;
                int result = JOptionPane.showConfirmDialog(this, "Scores Cleared!", "Clear Scores",
                        JOptionPane.DEFAULT_OPTION);
                if (result == 0) {
                    playing = true;
                }
            } else {
                JOptionPane.showConfirmDialog(this, "Scores Cleared!", "Clear Scores", 
                        JOptionPane.DEFAULT_OPTION);
            }
        } catch (IOException e) {
            System.out.println("Encountered IOException");
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw background image
        g.drawImage(bgImage, 0, 0, 750, 500, Color.BLACK, null);
        
        // Draw spaceship
        spaceship.draw(g);
        
        // Draw aliens only if they are alive
        if (alienUfo.getAlive()) {
            alienUfo.draw(g);
        }
        
        // Drawing missiles (already have removed missiles that hit aliens, the spaceship, the 
        // obstacles, or another missile)
        for (Missile m : missiles) {
            m.draw(g);
        }
        
        // Drawing aliens if they are alive
        for (Alien[] row : aliens) {
            for (Alien a : row) {
                if (a.getAlive()) {
                    a.draw(g);
                }
            }
        }
        
        // Drawing obstacles as long as they have not been hit
        for (Obstacle[] row : obstacle1) {
            for (Obstacle o : row) {
                if (!o.getIfHit()) {
                    o.draw(g);
                }
            }
        }
        
        for (Obstacle[] row : obstacle2) {
            for (Obstacle o : row) {
                if (!o.getIfHit()) {
                    o.draw(g);
                }  
            }
        }
        
        for (Obstacle[] row : obstacle3) {
            for (Obstacle o : row) {
                if (!o.getIfHit()) {
                    o.draw(g);
                }
            }
        }
        
        for (Obstacle[] row : obstacle4) {
            for (Obstacle o : row) {
                if (!o.getIfHit()) {
                    o.draw(g);
                }
            }
        }
        
        
    }
 

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
}
