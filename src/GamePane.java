import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import acm.graphics.*;

public class GamePane extends GraphicsPane {

    private static final int W = MainApplication.WINDOW_WIDTH;
    private static final int H = MainApplication.WINDOW_HEIGHT;

    private static final Color BG = new Color(0, 1, 4);
    private static final Color GRID_COLOR = new Color(8, 28, 45);
    private static final Color SCORE_COLOR = new Color(57, 255, 100);

    private Font fScore;

    private GLabel scoreLabel;
    private int score;

    public GamePane(MainApplication mainScreen) {
        super(mainScreen);
        fScore = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 28f);
        score = 0;
    }

    @Override
    public void showContent() {
        drawBackground();
        drawGrid();
        drawScore();
        // TODO: Add packets and other game elements here
    }

    @Override
    public void hideContent() {
        for (GObject o : contents) mainScreen.remove(o);
        contents.clear();
    }

    private void drawBackground() {
        GRect bg = new GRect(0, 0, W, H);
        bg.setFilled(true);
        bg.setFillColor(BG);
        bg.setColor(BG);
        add(bg);
    }

    private void drawGrid() {
        int s = 60;
        for (int x = 0; x <= W; x += s) {
            GLine l = new GLine(x, 0, x, H);
            l.setColor(GRID_COLOR);
            add(l);
        }
        for (int y = 0; y <= H; y += s) {
            GLine l = new GLine(0, y, W, y);
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

    public void updateScore(int delta) {
        score += delta;
        if (scoreLabel != null) {
            scoreLabel.setLabel("SCORE: " + score);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO: Handle packet clicks or other interactions
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO: Handle keyboard input
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    private void addContent(GObject o) {
        contents.add(o);
        mainScreen.add(o);
    }
}
