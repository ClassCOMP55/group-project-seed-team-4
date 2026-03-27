
public class EnemyPacket extends Packet {

    private PacketType enemyType;
    private int damage;
    private boolean isSpawned;

    public EnemyPacket(int id, PacketType type, String sourceIp, int port, double threatScore) {
        super(id, type, sourceIp, port, threatScore);

        this.enemyType = type;              // ✅ now matches type
        this.damage = type.getDamage();     // ✅ from enum
        this.isSpawned = true;
    }

    public PacketType getEnemyType() {
        return enemyType;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isSpawned() {
        return isSpawned;
    }
}