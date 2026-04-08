import java.awt.Color;

public enum PacketType {
    GOOD(false, 0, 1.0, 0, Color.CYAN),       // safe packet
    PHISHING(true, 1, 1.0, 10, Color.MAGENTA),
    MALWARE(true, 2, 1.0, 15, Color.ORANGE),
    DDOS(true, 3, 1.5, 20, Color.RED),
    SPOOF(true, 1, 2, 12, Color.PINK);

    private boolean isBad;
    private int damage;   
    private double baseSpeedMult;// damage to base
    private int points;
    private Color color;// points when destroyed

    PacketType(boolean isBad, int damage, double baseSpeedMult, int points, Color color) {
        this.isBad = isBad;
        this.damage = damage;
        this.baseSpeedMult = baseSpeedMult;
        this.points = points;
        this.color = color;
    }

    // getters
    public boolean isBad() {
        return isBad;
    }

    public int getDamage() {
        return damage;
    }

    public double getBaseSpeedMult() {
        return baseSpeedMult;
    }

    public int getPoints() {
        return points;
    }
    
    public Color getColor() {
    	return color;
    }
}
