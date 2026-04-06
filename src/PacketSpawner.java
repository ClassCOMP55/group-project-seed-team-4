import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import acm.graphics.*;
import java.awt.Color;

public class PacketSpawner {

    private GamePane gamePane;

    private Timer spawnTimer;

    private int spawnDelay;
    private double enemySpeed;
    private int maxEnemies;

    private ArrayList<GObject> enemies;

    public PacketSpawner(GamePane gamePane, int spawnDelay, double enemySpeed, int maxEnemies) {
        this.gamePane = gamePane;
        this.spawnDelay = spawnDelay;
        this.enemySpeed = enemySpeed;
        this.maxEnemies = maxEnemies;

        enemies = new ArrayList<GObject>();

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

        double x = Math.random() * (MainApplication.WINDOW_WIDTH - 40);
        double y = 0;

        GImage enemy = new GImage("C:/Users/adami/git/group-project-seed-team-4/src/Images/VirusPacket.png", x, y);
        enemy.setSize(40, 40);

        enemies.add(enemy);
        gamePane.addEnemy(enemy);
    }

    public void updateEnemies() {
    	ArrayList<GObject> toRemove = new ArrayList<>();

    	for (GObject enemy : enemies) {
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
    public boolean isEnemy(GObject obj) {
        return enemies.contains(obj);
    }

    public void destroyEnemy(GObject obj) {
        if (obj instanceof GRect && enemies.contains(obj)) {
            GRect enemy = (GRect) obj;
            enemies.remove(enemy);
            gamePane.removeEnemy(enemy);
            gamePane.updateScore(100);
        }
    }
}
