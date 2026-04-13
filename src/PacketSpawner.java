import java.util.*;
import javax.swing.Timer;
import java.awt.*;
import acm.graphics.*;

public class PacketSpawner {

    private static final int PACKET_SIZE = 64;

    private GamePane gamePane;
    private Timer    spawnTimer;
    private double   baseEnemySpeed;

    private final ArrayList<GObject>       enemies = new ArrayList<>();
    private final Map<GObject, PacketType> typeMap = new HashMap<>();
    private final Map<GObject, GObject>    phishReal   = new HashMap<>();
    private final Map<GObject, Timer>      phishTimers = new HashMap<>();

    public PacketSpawner(GamePane gamePane, int spawnDelay,
                         double baseEnemySpeed, int maxEnemies) {
        this.gamePane       = gamePane;
        this.baseEnemySpeed = baseEnemySpeed;

        spawnTimer = new Timer(spawnDelay, e -> {
            try {
                spawnEnemy();
            } catch (Exception ex) {
                System.err.println("PacketSpawner.spawnEnemy error: " + ex.getMessage());
            }
        });
        spawnTimer.setRepeats(true);
    }

    public void start() { spawnTimer.start(); }

    public void stop() {
        spawnTimer.stop();
        for (Timer t : new ArrayList<>(phishTimers.values())) t.stop();
        phishTimers.clear();
        phishReal.clear();
        enemies.clear();
        typeMap.clear();
    }

    private PacketType randomType() {
        double r = Math.random();
        if (r < 0.20) return PacketType.GOOD;
        if (r < 0.35) return PacketType.DATA_BURST;
        if (r < 0.50) return PacketType.VIRUS;
        if (r < 0.63) return PacketType.TROJAN;
        if (r < 0.74) return PacketType.DDOS;
        if (r < 0.87) return PacketType.PHISHING;
        return PacketType.RANSOMWARE;
    }

    private void spawnEnemy() {
        double x = Math.random() * (MainApplication.WINDOW_WIDTH - PACKET_SIZE);

        PacketType type = randomType();

        if (type == PacketType.PHISHING) {
            spawnPhishing(x);
        } else {
            GObject obj = makeSprite(type.getNormalSprite(), x, -PACKET_SIZE);
            enemies.add(obj);
            typeMap.put(obj, type);
            gamePane.addEnemy(obj);
        }
    }

    private void spawnPhishing(double x) {
        String disguiseSprite = Math.random() < 0.5
            ? PacketType.GOOD.getNormalSprite()
            : PacketType.DATA_BURST.getNormalSprite();

        GObject disguise = makeSprite(disguiseSprite, x, -PACKET_SIZE);
        GObject real     = makeSprite(PacketType.PHISHING.getNormalSprite(), x, -PACKET_SIZE);
        setVisible(real, false);

        enemies.add(disguise);
        typeMap.put(disguise, PacketType.PHISHING);
        phishReal.put(disguise, real);

        gamePane.addEnemy(disguise);
        gamePane.addEnemy(real);

        Timer flash = new Timer(900, null);
        flash.addActionListener(e -> {
            try {
                if (!enemies.contains(disguise)) { flash.stop(); return; }
                setVisible(disguise, false);
                setVisible(real, true);
                Timer revert = new Timer(300, ev -> {
                    try {
                        if (!enemies.contains(disguise)) return;
                        setVisible(real, false);
                        setVisible(disguise, true);
                    } catch (Exception ex) {
                        System.err.println("Phishing revert error: " + ex.getMessage());
                    }
                });
                revert.setRepeats(false);
                revert.start();
            } catch (Exception ex) {
                System.err.println("Phishing flash error: " + ex.getMessage());
            }
        });
        flash.setRepeats(true);
        flash.start();
        phishTimers.put(disguise, flash);
    }

    public void updateEnemies() {
        try {
            for (GObject obj : new ArrayList<>(enemies)) {
                PacketType type = typeMap.get(obj);
                if (type == null) continue;

                double speed = baseEnemySpeed * type.getBaseSpeedMult();
                obj.move(0, speed);

                GObject real = phishReal.get(obj);
                if (real != null) real.move(0, speed);

                if (obj.getY() > MainApplication.WINDOW_HEIGHT) {
                    onReachedBottom(obj, type);
                }
            }
        } catch (Exception ex) {
            System.err.println("updateEnemies error: " + ex.getMessage());
        }
    }

    private void onReachedBottom(GObject obj, PacketType type) {
        removePacket(obj);

        if (!type.isBad()) {
            gamePane.updateScore(type.getPoints());
            gamePane.onFriendlyPassedThrough();
        } else {
            switch (type) {
                case VIRUS:
                case TROJAN:
                case PHISHING:
                    gamePane.loseLife();
                    break;
                case DDOS:
                    gamePane.triggerDDoS();
                    break;
                case RANSOMWARE:
                    gamePane.deductPoints(type.getRansomPenalty());
                    break;
                default:
                    gamePane.loseLife();
                    break;
            }

            if (type != PacketType.DDOS) {
                gamePane.onMaliciousBreached();
            }
        }
    }

    public boolean isEnemy(GObject obj) {
        if (enemies.contains(obj)) return true;
        return phishReal.containsValue(obj);
    }

    public void destroyEnemyAt(int mx, int my) {
        for (GObject obj : new ArrayList<>(enemies)) {
            double ex = obj.getX(), ey = obj.getY();
            if (mx >= ex && mx <= ex + PACKET_SIZE && my >= ey && my <= ey + PACKET_SIZE) {
                destroyEnemy(obj);
                return;
            }
        }

        for (GObject real : new ArrayList<>(phishReal.values())) {
            double ex = real.getX(), ey = real.getY();
            if (mx >= ex && mx <= ex + PACKET_SIZE && my >= ey && my <= ey + PACKET_SIZE) {
                destroyEnemy(real);
                return;
            }
        }
    }

    public void destroyEnemy(GObject obj) {
        try {
            GObject key = resolveKey(obj);
            if (key == null || !enemies.contains(key)) return;

            PacketType type = typeMap.get(key);
            double x = key.getX();
            double y = key.getY();

            removePacket(key);

            if (type == null) return;

            if (!type.isBad()) {
                gamePane.loseLife();
                gamePane.onFriendlyDestroyed();
            } else {
                gamePane.updateScore(type.getPoints());
                gamePane.onPacketDestroyed();
            }

            showDestroyed(type.getDestroyedSprite(), x, y, type);
        } catch (Exception ex) {
            System.err.println("destroyEnemy error: " + ex.getMessage());
        }
    }

    private GObject resolveKey(GObject obj) {
        if (enemies.contains(obj)) return obj;
        for (Map.Entry<GObject, GObject> e : phishReal.entrySet()) {
            if (e.getValue() == obj) return e.getKey();
        }
        return null;
    }

    private void removePacket(GObject key) {
        Timer t = phishTimers.remove(key);
        if (t != null) t.stop();

        GObject real = phishReal.remove(key);
        if (real != null) {
            setVisible(real, true);
            gamePane.removeEnemy(real);
        }

        enemies.remove(key);
        typeMap.remove(key);
        gamePane.removeEnemy(key);
    }

    private void showDestroyed(String sprite, double x, double y, PacketType type) {
        try {
            GObject destroyed = makeSprite(sprite, x, y);
            gamePane.addTemporary(destroyed);
            spawnParticles(x + PACKET_SIZE / 2.0, y + PACKET_SIZE / 2.0, particleColor(type));

            Timer cleanup = new Timer(400, e -> gamePane.removeTemporary(destroyed));
            cleanup.setRepeats(false);
            cleanup.start();
        } catch (Exception ex) {
            System.err.println("showDestroyed error: " + ex.getMessage());
        }
    }

    private Color particleColor(PacketType type) {
        if (type == null) return new Color(0, 200, 230);
        switch (type) {
            case GOOD:       return new Color( 80, 180, 255);
            case DATA_BURST: return new Color(255, 220,   0);
            case VIRUS:
            case TROJAN:
            case DDOS:       return new Color(255,  50,  50); 
            case PHISHING:
            case RANSOMWARE: return new Color(180, 100, 255);
            default:         return new Color(0, 200, 230);
        }
    }

    private void spawnParticles(double cx, double cy, Color baseColor) {
        Random rng = new Random();
        int count = 20;

        double[] px    = new double[count]; 
        double[] py    = new double[count]; 
        double[] vx    = new double[count]; 
        double[] vy    = new double[count]; 
        double[] size  = new double[count]; 
        double[] decay = new double[count]; 

        ArrayList<GOval> ovals = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double angle = rng.nextDouble() * 2 * Math.PI;
            double speed = 3.0 + rng.nextDouble() * 9.0;
            vx[i] = Math.cos(angle) * speed;
            vy[i] = Math.sin(angle) * speed - 1.5;

            double s = 4.0 + rng.nextDouble() * 10.0;
            size[i] = s;

            decay[i] = 0.3 + rng.nextDouble() * 0.5;

            px[i] = cx - s / 2;
            py[i] = cy - s / 2;

            Color c = rng.nextDouble() < 0.35
                ? blend(baseColor, Color.WHITE, 0.6f)
                : baseColor;

            GOval oval = new GOval(px[i], py[i], size[i], size[i]);
            oval.setFilled(true);
            oval.setFillColor(c);
            oval.setColor(c);
            gamePane.addTemporary(oval);
            ovals.add(oval);
        }

        int[] tick = {0};
        int maxTicks = 28; 
        double gravity = 0.4;

        Timer anim = new Timer(20, null);
        anim.addActionListener(e -> {
            tick[0]++;
            boolean anyAlive = false;

            for (int i = 0; i < count; i++) {
                if (size[i] <= 0) continue;
                anyAlive = true;

                vy[i] += gravity;

                px[i] += vx[i];
                py[i] += vy[i];

                vx[i] *= 0.92;
                vy[i] *= 0.92;

                size[i] = Math.max(0, size[i] - decay[i]);

                GOval oval = ovals.get(i);
                oval.setLocation(px[i], py[i]);
                oval.setSize(size[i], size[i]);
            }

            if (!anyAlive || tick[0] >= maxTicks) {
                anim.stop();
                for (GOval oval : ovals) gamePane.removeTemporary(oval);
            }
        });
        anim.start();
    }

    private Color blend(Color a, Color b, float t) {
        return new Color(
            (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t),
            (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t)
        );
    }

    private GObject makeSprite(String filename, double x, double y) {
        try {
            GImage img = new GImage(filename, x, y);
            img.setSize(PACKET_SIZE, PACKET_SIZE);
            return img;
        } catch (Exception ex) {
            System.err.println("Missing sprite: " + filename + " — using fallback rect");
            GRect rect = new GRect(x, y, PACKET_SIZE, PACKET_SIZE);
            rect.setFilled(true);
            rect.setFillColor(Color.MAGENTA);
            rect.setColor(Color.WHITE);
            return rect;
        }
    }

    private void setVisible(GObject obj, boolean visible) {
        if (obj instanceof GImage) ((GImage) obj).setVisible(visible);
        else obj.setColor(visible ? Color.WHITE : new Color(0,0,0,0));
    }
}