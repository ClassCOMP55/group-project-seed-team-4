public enum PacketType {
    GOOD(false, 0, 1, 0),       // safe packet
    PHISHING(true, 1, 2, 10),
    MALWARE(true, 2, 3, 15),
    DDOS(true, 3, 4, 20),
    SPOOF(true, 1, 2, 12);

    private boolean isBad;
    private int damage;     // damage to base
    private int speed;      // how fast it moves
    private int points;     // points when destroyed

    PacketType(boolean isBad, int damage, int speed, int points) {
        this.isBad = isBad;
        this.damage = damage;
        this.speed = speed;
        this.points = points;
    }

    // getters
    public boolean isBad() {
        return isBad;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPoints() {
        return points;
    }
}
