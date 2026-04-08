import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;
import acm.graphics.*;
import java.awt.Color;

public class PacketSpawner {

    private GamePane gamePane;
    private Timer spawnTimer;
    private int spawnDelay;
    private double baseEnemySpeed;
    private int maxEnemies;
    private ArrayList<GObject> enemies = new ArrayList<>();
    private Map<GObject, PacketType> typeMap = new HashMap<>();
    private boolean phishingActive = false;
    private Timer phishingTimer;
    private int goodPacketClicks = 0;

    public PacketSpawner(GamePane gamePane, int spawnDelay, double baseEnemySpeed, int maxEnemies) {
        this.gamePane = gamePane;
        this.spawnDelay = spawnDelay;
        this.baseEnemySpeed = baseEnemySpeed;
        this.maxEnemies = maxEnemies;
        setupTimer();
    }

    private void setupTimer() {
      spawnTimer = new Timer(spawnDelay, e -> spawnEnemy());
    }

    public void start() {
        spawnTimer.start();
    }

    public void stop() {
        spawnTimer.stop();
    }
    //------HERE BOY------GOOD FIGHT---
    private PacketType randomPacketTypeByDifficulty() {
    	double r = Math.random();
    	if(r < 0.40) return PacketType.GOOD;
    	if(r < 0.60) return PacketType.PHISHING;
    	if(r < 0.75) return PacketType.MALWARE;
    	if(r < 0.90) return PacketType.DDOS;
    	return PacketType.SPOOF;
    }

    private void spawnEnemy() {
        if (enemies.size() >= maxEnemies) return;

        double x = Math.random() * (MainApplication.WINDOW_WIDTH - 40);
        double y = 0;

        PacketType type = randomPacketTypeByDifficulty();

        GRect enemy = new GRect(x, y, 40, 40);
        enemy.setFilled(true);

        // make phishing and spoof look similar to good (cyan-ish)
        if (type == PacketType.PHISHING || type == PacketType.SPOOF) {
            Color cyanish = Color.CYAN.darker();
            enemy.setColor(cyanish);
            enemy.setFillColor(cyanish);
        } else {
            enemy.setColor(type.getColor());
            enemy.setFillColor(type.getColor());
        }

        enemies.add(enemy);
        typeMap.put(enemy, type);
        gamePane.addEnemy(enemy);

        enemy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleClickOnPacket(enemy);
            }
        });
    }

//        GImage benemy = new GImage("C:/Users/adami/git/group-project-seed-team-4/src/Images/VirusPacket.png", x, y);
//        benemy.setSize(40, 40);
//
//        enemies.add(benemy);
//        gamePane.addEnemy(benemy);
        
       

    //-----HERE GOOD FIGHT BOY!-------
    private void handleClickOnPacket(GObject obj) {
    	PacketType type = typeMap.get(obj);
    	if(type == null) return;
    	
    	removeEnemy(obj);
    	
    	if(type == PacketType.GOOD) {
    		gamePane.updateScore(type.getPoints());
    		goodPacketClicks++;
    		if(goodPacketClicks == 10) {
    			gamePane.addLife(1);
    		}
    		else if (goodPacketClicks == 25) {
    			gamePane.grantTemporaryAbility(5); //gives an ability for 5s
    		}
    	}
    	else if (type == PacketType.PHISHING) {
            gamePane.updateScore(type.getPoints());
            triggerPhishingBurst();
        } else if (type == PacketType.MALWARE) {
            gamePane.updateScore(type.getPoints());
            // Example: malware deals damage if reaches bottom. On click it's destroyed as above.
        } else if (type == PacketType.DDOS) {
            gamePane.updateScore(type.getPoints());
            // DDOS clicked -> destroyed; if not clicked and reaches bottom -> disable skills (handled on reach-bottom)
        } else if (type == PacketType.SPOOF) {
            // clicking a Spoof steals life/ability
            if (!gamePane.stealAbilityOrLife()) {
                // fallback: steal life if ability wasn't available
                gamePane.loseLife();
            }
        }

    }
    
    private void triggerPhishingBurst() {
        if (phishingTimer != null && phishingTimer.isRunning()) {
            phishingTimer.stop();
        }
        phishingActive = true;

        // reduce spawnDelay to double spawn rate (i.e., spawnDelay/2)
        int oldDelay = spawnTimer.getDelay();
        spawnTimer.setDelay(Math.max(50, oldDelay / 2));

        phishingTimer = new Timer(15000, e -> {
            phishingActive = false;
            spawnTimer.setDelay(oldDelay);
            phishingTimer.stop();
        });
        phishingTimer.setRepeats(false);
        phishingTimer.start();
    }

    
    public void updateEnemies() {
        ArrayList<GObject> toRemove = new ArrayList<>();

        for (GObject enemy : new ArrayList<>(enemies)) {
            PacketType type = typeMap.get(enemy);
            double speedMult = (type != null) ? type.getBaseSpeedMult() : 1.0;
            double dy = baseEnemySpeed * speedMult;
            enemy.move(0, dy);

            if (enemy.getY() > MainApplication.WINDOW_HEIGHT) {
                // reached bottom: apply consequences based on type
                toRemove.add(enemy);
                gamePane.removeEnemy(enemy);
                typeMap.remove(enemy);
                enemies.remove(enemy);

                if (type == PacketType.DDOS) {
                    // DDOS reaching bottom disables skills for 20s
                    gamePane.disableSkillsTemporarily(20000);
                } else if (type == PacketType.SPOOF) {
                    // Spoof reaching bottom steals life or ability
                    if (!gamePane.stealAbilityOrLife()) gamePane.loseLife();
                } else {
                    // default: lose life equal to damage
                    gamePane.loseLife();
                }
            }
        }
        enemies.removeAll(toRemove);
    }

    
    public boolean isEnemy(GObject obj) {
        return enemies.contains(obj);
    }

    public void destroyEnemy(GObject obj) {
        if (enemies.contains(obj)) {
            removeEnemy(obj);
            PacketType type = typeMap.get(obj);
            if (type != null) gamePane.updateScore(type.getPoints());
        }
    }

    private void removeEnemy(GObject obj) {
        enemies.remove(obj);
        typeMap.remove(obj);
        gamePane.removeEnemy(obj);
    }
}
