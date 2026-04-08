import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acm.graphics.*;

public class GamePane extends GraphicsPane {

    private static final Color BG = new Color(0, 1, 4);
    private static final Color GRID_COLOR = new Color(8, 28, 45);
    private static final Color SCORE_COLOR = new Color(57, 255, 100);
    private static final Color LIVES_COLOR = new Color(255, 80, 80);

    private Font fScore;

    private GLabel scoreLabel;
    private GLabel livesLabel;

    private int score;
    private double enemySpeed;
    private int spawnDelay;
    private int maxEnemies;
    private int lives;

    private PacketSpawner spawner;
    private Timer gameLoop;
    
    private int goodPacketClicks = 0;
    private int abilitiesCount = 0;
    private boolean skillsEnabled = true;
    private Timer skillDisableTimer;
    private Timer abilityExpireTimer;

    public GamePane(MainApplication mainScreen) {
        super(mainScreen);
        fScore = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 28f);
        score = 0;
    }

    public void startNewGame() {
        score = 0;
        goodPacketClicks = 0;
        abilitiesCount = 0;
        skillsEnabled = true;
        if (skillDisableTimer != null && skillDisableTimer.isRunning()) skillDisableTimer.stop();
        if (abilityExpireTimer != null && abilityExpireTimer.isRunning()) abilityExpireTimer.stop();

        String difficulty = mainScreen.getDifficulty();

        if (difficulty.equals("NOOB")) {
            lives = 5;
            enemySpeed = 1.0;
            spawnDelay = 2000;
            maxEnemies = 3;
        } else if (difficulty.equals("PRO")) {
            lives = 3;
            enemySpeed = 2.0;
            spawnDelay = 1200;
            maxEnemies = 5;
        } else {
            lives = 2;
            enemySpeed = 3.5;
            spawnDelay = 700;
            maxEnemies = 8;
        }

        if (spawner != null) {
            spawner.stop();
        }

        if (gameLoop != null) {
            gameLoop.stop();
        }

        spawner = new PacketSpawner(this, spawnDelay, enemySpeed, maxEnemies);

        gameLoop = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (spawner != null) {
                    spawner.updateEnemies();
                }
            }
        });
    }

    public void addEnemy(GObject enemy) {
        addContent(enemy);
    }

    public void removeEnemy(GObject enemy) {
        mainScreen.remove(enemy);
        contents.remove(enemy);
    }

    @Override
    public void showContent() {
        drawBackground();
        drawGrid();
        drawScore();
        drawLives();

        if (spawner != null) {
            spawner.start();
        }

        if (gameLoop != null) {
            gameLoop.start();
        }
    }

    @Override
    public void hideContent() {
        if (spawner != null) {
            spawner.stop();
        }

        if (gameLoop != null) {
            gameLoop.stop();
        }

        super.hideContent();
    }
    
    private void addContent(GObject o) {
        contents.add(o);
        mainScreen.add(o);
    }

    private void drawBackground() {
        GRect bg = new GRect(0, 0, MainApplication.WINDOW_WIDTH, MainApplication.WINDOW_HEIGHT);
        bg.setFilled(true);
        bg.setFillColor(BG);
        bg.setColor(BG);
        addContent(bg);
    }

    private void drawGrid() {
        int s = 60;

        for (int x = 0; x <= MainApplication.WINDOW_WIDTH; x += s) {
            GLine l = new GLine(x, 0, x, MainApplication.WINDOW_HEIGHT);
            l.setColor(GRID_COLOR);
            addContent(l);
        }

        for (int y = 0; y <= MainApplication.WINDOW_HEIGHT; y += s) {
            GLine l = new GLine(0, y, MainApplication.WINDOW_WIDTH, y);
            l.setColor(GRID_COLOR);
            addContent(l);
        }
    }

    private void drawScore() {
        scoreLabel = new GLabel("SCORE: " + score, 20, 40);
        scoreLabel.setFont(fScore);
        scoreLabel.setColor(SCORE_COLOR);
        addContent(scoreLabel);
    }

    private void drawLives() {
        livesLabel = new GLabel("LIVES: " + lives, 20, 80);
        livesLabel.setFont(fScore);
        livesLabel.setColor(LIVES_COLOR);
        addContent(livesLabel);
    }

    public void updateScore(int delta) {
        score += delta;

        if (scoreLabel != null) {
            scoreLabel.setLabel("SCORE: " + score);
        }
    }

    public void loseLife() {
        lives--;

        if (livesLabel != null) {
            livesLabel.setLabel("LIVES: " + lives);
        }

        if (lives <= 0) {
            if (spawner != null) {
                spawner.stop();
            }

            if (gameLoop != null) {
                gameLoop.stop();
            }

            mainScreen.switchToGameOverScreen(score, lives, "FIREWALL BREACHED");
        }
    }
    
    public void addLife(int n) {
    	lives += n;
    	if (livesLabel != null) {
    		livesLabel.setLabel("LIVES: " + lives);
    	}
    }
    
    public void grantTemporaryAbility(int seconds) {
    	abilitiesCount++;
    	if (abilityExpireTimer != null && abilityExpireTimer.isRunning()) {
    		abilityExpireTimer.stop();
    	}
    	abilityExpireTimer = new Timer(seconds * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (abilitiesCount > 0) abilitiesCount--;
                abilityExpireTimer.stop();
            }
        });
        abilityExpireTimer.setRepeats(false);
        abilityExpireTimer.start();
    }
    
    public void disableSkillsTemporarily(int milliseconds) {
    	skillsEnabled = false;
    	if (skillDisableTimer != null && skillDisableTimer.isRunning()) {
    		skillDisableTimer.stop();
    	}
    	skillDisableTimer = new Timer(milliseconds, new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			skillsEnabled = true;
    			skillDisableTimer.stop();
    		}
    	});
    	skillDisableTimer.setRepeats(false);
    	skillDisableTimer.start();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

        if (clicked != null && spawner != null && spawner.isEnemy(clicked)) {
            spawner.destroyEnemy(clicked);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Add keyboard controls later
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
