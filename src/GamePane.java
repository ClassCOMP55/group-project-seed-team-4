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

    public GamePane(MainApplication mainScreen) {
        super(mainScreen);
        fScore = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 28f);
        score = 0;
    }

    public void startNewGame() {
        score = 0;

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

    public void addEnemy(GRect enemy) {
        add(enemy);
    }

    public void removeEnemy(GRect enemy) {
        remove(enemy);
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

    private void drawBackground() {
        GRect bg = new GRect(0, 0, MainApplication.WINDOW_WIDTH, MainApplication.WINDOW_HEIGHT);
        bg.setFilled(true);
        bg.setFillColor(BG);
        bg.setColor(BG);
        add(bg);
    }

    private void drawGrid() {
        int s = 60;

        for (int x = 0; x <= MainApplication.WINDOW_WIDTH; x += s) {
            GLine l = new GLine(x, 0, x, MainApplication.WINDOW_HEIGHT);
            l.setColor(GRID_COLOR);
            add(l);
        }

        for (int y = 0; y <= MainApplication.WINDOW_HEIGHT; y += s) {
            GLine l = new GLine(0, y, MainApplication.WINDOW_WIDTH, y);
            l.setColor(GRID_COLOR);
            add(l);
        }
    }

    private void drawScore() {
        scoreLabel = new GLabel("SCORE: " + score, 20, 40);
        scoreLabel.setFont(fScore);
        scoreLabel.setColor(SCORE_COLOR);
        add(scoreLabel);
    }

    private void drawLives() {
        livesLabel = new GLabel("LIVES: " + lives, 20, 80);
        livesLabel.setFont(fScore);
        livesLabel.setColor(LIVES_COLOR);
        add(livesLabel);
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

    @Override
    public void mousePressed(MouseEvent e) {
        // Add click handling later
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