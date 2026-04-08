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
//    private PacketType randomPacketTypeByDifficulty() {
//    	if(d < 0.40) return PacketType.GOOD;
//    	if(d < 0.60) return PacketType.PHISHING;
//    	if(d < 0.75) return PacketType.MALWARE;
//    	if(d < 0.90) return PacketType.DDOS;
//    	return PacketType.SPOOF;
//    }

    private void spawnEnemy() {
        if (enemies.size() >= maxEnemies) return;
        
        //apply phishing spawn-rate boost
        if(phishingActive && Math.random() < 0.5) {
        	//spawn an extra phishing more often
        }

        double x = Math.random() * (MainApplication.WINDOW_WIDTH - 40);
        double y = 0;

        GImage benemy = new GImage("C:/Users/adami/git/group-project-seed-team-4/src/Images/VirusPacket.png", x, y);
        benemy.setSize(40, 40);

        enemies.add(benemy);
        gamePane.addEnemy(benemy);
        
        //PacketType type = randomPacketTypeByDifficulty();
        
        GRect enemy = new GRect(x, y, 40, 40);
        enemy.setFilled(true);
        //-----LOOOK HERE-----
        //Trying to make phishing and spoof look similar to good
//        if(type == PacketType.PHISHING || type == PacketType.SPOOF) {
//        	enemy.setColor((Color.CYAN.darker());
//        	enemy.setFillColor(Color.CYAN.darker());
//        }
//        else {
//        	enemy.setColor(type.getColor());
//        	enemy.setFillColor(type.getColor());
//        }
        
        enemies.add(enemy);
        //typeMap.put(enemy,  type);
        gamePane.addEnemy(enemy);
        
        enemy.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		//handleClickOnPacket(enemy);
        	}
        });
    }

    //-----HERE GOOD FIGHT BOY!-------
//    private void handleClickOnPacket(GObject obj) {
//    	PacketType type = typeMap.get(obj);
//    	if(type == null) return;
//    	
//    	removeEnemy(obj);
//    	
//    	if(type == PacketType.GOOD) {
//    		gamePane.updateScore(type.getPoints());
//    		goodPacketClicks++;
//    		if(goodPacketClicks == 10) {
//    			gamePane.addLife(1);
//    		}
//    		else if (goodPacketClicks == 25) {
//    			gamePane.grantTemporaryAbity(5); //gives an ability for 5s
//    		}
//    	}
//    	else if (type == PacketType.PHISHING) {
//            gamePane.updateScore(type.getPoints());
//            triggerPhishingBurst();
//        } else if (type == PacketType.MALWARE) {
//            gamePane.updateScore(type.getPoints());
//            // Example: malware deals damage if reaches bottom. On click it's destroyed as above.
//        } else if (type == PacketType.DDOS) {
//            gamePane.updateScore(type.getPoints());
//            // DDOS clicked -> destroyed; if not clicked and reaches bottom -> disable skills (handled on reach-bottom)
//        } else if (type == PacketType.SPOOF) {
//            // clicking a Spoof steals life/ability
//            if (!gamePane.stealAbilityOrLife()) {
//                // fallback: steal life if ability wasn't available
//                gamePane.loseLife();
//            }
//        }
//
//    }
    
    public void updateEnemies() {
    	ArrayList<GObject> toRemove = new ArrayList<>();

    	for (GObject enemy : enemies) {
            enemy.move(0, baseEnemySpeed);

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
