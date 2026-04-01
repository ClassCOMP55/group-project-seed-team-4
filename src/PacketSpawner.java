import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import acm.graphics.*;

public class PacketSpawner {

    private GamePane gamePane;

    private Timer spawnTimer;

    private int spawnDelay;
    private double enemySpeed;
    private int maxEnemies;

    private ArrayList<GRect> enemies;

    public PacketSpawner(GamePane gamePane, int spawnDelay, double enemySpeed, int maxEnemies) {
        this.gamePane = gamePane;
        this.spawnDelay = spawnDelay;
        this.enemySpeed = enemySpeed;
        this.maxEnemies = maxEnemies;

        enemies = new ArrayList<>();

        setupTimer();
    }

    private void setupTimer() {
        spawnTimer = new Timer(spawnDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spawnEnemy();
            }
        });
    }

    public void start() {
        spawnTimer.start();
    }

    public void stop() {
        spawnTimer.stop();
    }

    private void spawnEnemy() {
        if (enemies.size() >= maxEnemies) return;

        // Create a simple square packet
        double x = Math.random() * (MainApplication.WINDOW_WIDTH - 40);
        double y = 0;

        GRect enemy = new GRect(x, y, 40, 40);
        enemy.setFilled(true);
        enemy.setFillColor(java.awt.Color.RED);

        enemies.add(enemy);
        gamePane.addEnemy(enemy);
    }

    public void updateEnemies() {
        ArrayList<GRect> toRemove = new ArrayList<>();

        for (GRect enemy : enemies) {
            enemy.move(0, enemySpeed);

            // If it reaches bottom → remove + lose life
            if (enemy.getY() > MainApplication.WINDOW_HEIGHT) {
                toRemove.add(enemy);
                gamePane.removeEnemy(enemy);
                gamePane.loseLife();
            }
        }

        enemies.removeAll(toRemove);
    }
}