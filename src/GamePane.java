import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import acm.graphics.*;

public class GamePane extends GraphicsPane {

    private static final Color BG          = new Color(  0,   1,   4);
    private static final Color GRID_COLOR  = new Color(  8,  28,  45);
    private static final Color SCORE_COLOR = new Color( 57, 255, 100);
    private static final Color LIVES_COLOR = new Color(255,  80,  80);

    private Font fScore;
    private Font fDDoS;

    private GLabel scoreLabel;
    private GLabel livesLabel;

    private GRect  ddosOverlay;
    private GLabel ddosLabel;
    private boolean ddosActive = false;
    private Timer   ddosTimer;

    private Timer   shakeTimer;
    private int     shakeTick;
    private int     shakeDuration;    
    private double  shakeMagnitude; 
    private double  shakeOffsetX = 0;
    private double  shakeOffsetY = 0;
    private final java.util.Random rng = new java.util.Random();

    private GRect  flashRect;
    private Timer  flashTimer;
    private int    flashTick;
    private int    flashDuration;
    private Color  flashColor;

    private int    score;
    private double enemySpeed;
    private int    spawnDelay;
    private int    maxEnemies;
    private int    lives;

    private PacketSpawner spawner;
    private Timer         gameLoop;

    public GamePane(MainApplication mainScreen) {
        super(mainScreen);
        fScore = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 48f);
        fDDoS  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 72f);
    }
    

    public void startNewGame() {
        hardStop();
        resetShake();

        score      = 0;
        ddosActive = false;
        ddosOverlay = null;
        ddosLabel   = null;

        String diff = mainScreen.getDifficulty();
        if (diff.equals("NOOB")) {
            lives = 5; enemySpeed = 1.5; spawnDelay = 2000; maxEnemies = 4;
        } else if (diff.equals("PRO")) {
            lives = 3; enemySpeed = 4.0; spawnDelay = 700;  maxEnemies = 9;
        } else { 
            lives = 2; enemySpeed = 5.5; spawnDelay = 400; maxEnemies = 12;
        }

        spawner  = new PacketSpawner(this, spawnDelay, enemySpeed, maxEnemies);
        gameLoop = new Timer(16, e -> { if (spawner != null) spawner.updateEnemies(); });
    }

    @Override
    public void showContent() {
        mainScreen.setCanvasBackground(new Color(180, 180, 180));
        drawBackground();
        drawGrid();
        drawHUD();
        if (spawner  != null) spawner.start();
        if (gameLoop != null) gameLoop.start();
    }

    @Override
    public void hideContent() {
        mainScreen.setCanvasBackground(Color.WHITE);
        hardStop();
        resetShake();
        super.hideContent();
    }

    private void hardStop() {
        if (gameLoop  != null) gameLoop.stop();
        if (spawner   != null) spawner.stop();
        if (ddosTimer != null) { ddosTimer.stop(); ddosTimer = null; }
        stopShake();
        if (flashTimer != null){ flashTimer.stop(); flashTimer = null; }
    }

    private void drawBackground() {
        int rw = (int) mainScreen.getWidth();
        int rh = (int) mainScreen.getHeight();
        GRect bg = new GRect(0, 0, rw, rh);
        bg.setFilled(true); bg.setFillColor(BG); bg.setColor(BG);
        add(bg);
    }

    private void drawGrid() {
        int s  = 60;
        int rw = (int) mainScreen.getWidth();
        int rh = (int) mainScreen.getHeight();
        for (int x = 0; x <= rw; x += s) { GLine l = new GLine(x,0,x,rh); l.setColor(GRID_COLOR); add(l); }
        for (int y = 0; y <= rh; y += s) { GLine l = new GLine(0,y,rw,y); l.setColor(GRID_COLOR); add(l); }
    }

    private void drawHUD() {
        scoreLabel = new GLabel("SCORE: " + score, 20, 60);
        scoreLabel.setFont(fScore); scoreLabel.setColor(SCORE_COLOR);
        add(scoreLabel);

        livesLabel = new GLabel("LIVES: " + lives, 20, 115);
        livesLabel.setFont(fScore); livesLabel.setColor(LIVES_COLOR);
        add(livesLabel);
    }

    public void addEnemy(GObject enemy)    { add(enemy);    }
    public void removeEnemy(GObject enemy) { remove(enemy); }

    public void addTemporary(GObject obj) {
        mainScreen.add(obj);
        contents.add(obj);
    }

    public void removeTemporary(GObject obj) {
        mainScreen.remove(obj);
        contents.remove(obj);
    }

    public void updateScore(int delta) {
        score += delta;
        if (scoreLabel != null) scoreLabel.setLabel("SCORE: " + score);
    }

    public void deductPoints(int amount) {
        score = Math.max(0, score - amount);
        if (scoreLabel != null) scoreLabel.setLabel("SCORE: " + score);
    }

    public void loseLife() {
        if (lives <= 0) return;
        lives--;
        if (livesLabel != null) livesLabel.setLabel("LIVES: " + lives);
        if (lives <= 0) endGame();
    }

    public void addLife(int n) {
        lives += n;
        if (livesLabel != null) livesLabel.setLabel("LIVES: " + lives);
    }

    private void endGame() {
        hardStop();
        resetShake();
        
        CurrencyManager cm = mainScreen.getCurrencyManager();
        if (cm != null) {
        	cm.addTokensFromScore(score, mainScreen.getDifficulty());
        }
        
        super.hideContent();
        mainScreen.switchToGameOverScreen(score, lives, "FIREWALL BREACHED");
    }

    public void shake(double magnitude, int durationMs) {
        stopShake();

        shakeMagnitude = magnitude;
        shakeDuration  = Math.max(1, durationMs / 16);
        shakeTick      = 0;

        shakeTimer = new Timer(16, e -> {
            shakeTick++;

            if (shakeTick >= shakeDuration) {
                stopShake();
                return;
            }

            double prevX = shakeOffsetX;
            double prevY = shakeOffsetY;

            double progress = (double) shakeTick / shakeDuration;
            double decay    = Math.pow(1.0 - progress, 1.8);
            double mag      = shakeMagnitude * decay;
            double angle    = rng.nextDouble() * 2 * Math.PI;

            shakeOffsetX = Math.cos(angle) * mag;
            shakeOffsetY = Math.sin(angle) * mag;

            double dx = shakeOffsetX - prevX;
            double dy = shakeOffsetY - prevY;
            for (GObject obj : new ArrayList<>(contents)) {
                obj.move(dx, dy);
            }
        });
        shakeTimer.start();
    }

    private void stopShake() {
        if (shakeTimer != null) {
            shakeTimer.stop();
            shakeTimer = null;
        }

        if (shakeOffsetX != 0 || shakeOffsetY != 0) {
            for (GObject obj : new ArrayList<>(contents)) {
                obj.move(-shakeOffsetX, -shakeOffsetY);
            }
            shakeOffsetX  = 0;
            shakeOffsetY  = 0;
        }
        shakeMagnitude = 0;
    }

    private void resetShake() {
        stopShake();
    }

    public void flash(Color color, int durationMs) {
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.stop();
            if (flashRect != null) {
                mainScreen.remove(flashRect);
                contents.remove(flashRect);
                flashRect = null;
            }
        }

        int rw = (int) mainScreen.getWidth();
        int rh = (int) mainScreen.getHeight();

        flashRect = new GRect(0, 0, rw, rh);
        flashRect.setFilled(true);
        flashRect.setColor(color);
        flashRect.setFillColor(color);
        mainScreen.add(flashRect);
        contents.add(flashRect);

        flashColor    = color;
        flashDuration = durationMs / 16;
        flashTick     = 0;

        flashTimer = new Timer(16, e -> {
            flashTick++;
            double progress = (double) flashTick / flashDuration;

            if (progress >= 1.0) {
                flashTimer.stop();
                flashTimer = null;
                mainScreen.remove(flashRect);
                contents.remove(flashRect);
                flashRect = null;
                return;
            }

            int alpha = (int)(flashColor.getAlpha() * (1.0 - progress));
            Color faded = new Color(
                flashColor.getRed(),
                flashColor.getGreen(),
                flashColor.getBlue(),
                alpha
            );
            flashRect.setFillColor(faded);
            flashRect.setColor(faded);
        });
        flashTimer.start();
    }

    public void playSfx(String filename) {
        mainScreen.playSfx(filename);
    }

    public void onPacketDestroyed() {
        shake(12, 220);
        playSfx("destroy.wav");
    }

    public void onFriendlyDestroyed() {
        shake(12, 220);
        flash(new Color(255, 30, 30, 150), 400);
        playSfx("friendly_destroyed.wav");
    }

    public void onFriendlyPassedThrough() {
        shake(5, 150);
        flash(new Color(57, 255, 100, 20), 400); // green
    }

    public void onMaliciousBreached() {
        shake(28, 350);
        flash(new Color(255, 30, 30, 180), 500);  // red
    }

    public void triggerDDoS() {
        if (ddosActive) return;
        ddosActive = true;

        onMaliciousBreached();

        int rw = (int) mainScreen.getWidth();
        int rh = (int) mainScreen.getHeight();

        ddosOverlay = new GRect(0, 0, rw, rh);
        ddosOverlay.setFilled(true);
        ddosOverlay.setFillColor(new Color(180, 0, 0, 160));
        ddosOverlay.setColor(new Color(180, 0, 0, 0));
        mainScreen.add(ddosOverlay);
        contents.add(ddosOverlay);

        ddosLabel = new GLabel("YOU HAVE BEEN DDOS'D", 0, 0);
        ddosLabel.setFont(fDDoS);
        ddosLabel.setColor(Color.WHITE);
        ddosLabel.setLocation((rw - ddosLabel.getWidth()) / 2.0, rh / 2.0);
        mainScreen.add(ddosLabel);
        contents.add(ddosLabel);

        ddosTimer = new Timer(3000, e -> {
            ddosActive = false;
            if (ddosOverlay != null) { mainScreen.remove(ddosOverlay); contents.remove(ddosOverlay); ddosOverlay = null; }
            if (ddosLabel   != null) { mainScreen.remove(ddosLabel);   contents.remove(ddosLabel);   ddosLabel   = null; }
            ddosTimer.stop();
        });
        ddosTimer.setRepeats(false);
        ddosTimer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (ddosActive) return;
        int mx = e.getX(), my = e.getY();
        
        GObject clicked = mainScreen.getElementAtLocation(mx, my);
        if (clicked == flashRect || clicked == ddosOverlay || clicked == ddosLabel) {
            clicked = null;
        }
       
        if (clicked == null) {
            if (spawner != null) spawner.destroyEnemyAt(mx, my);
            return;
        }
        if (spawner != null && spawner.isEnemy(clicked)) {
            spawner.destroyEnemy(clicked);
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e)  {}
    @Override public void mouseDragged(MouseEvent e)  {}
    @Override public void mouseMoved(MouseEvent e)    {}
    @Override public void keyPressed(KeyEvent e)      {}
    @Override public void keyReleased(KeyEvent e)     {}
    @Override public void keyTyped(KeyEvent e)        {}
}