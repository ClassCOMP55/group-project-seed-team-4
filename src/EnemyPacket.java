
public class EnemyPacket extends Packet{
	private String enemyType;
	private int damage;
	private boolean isSpawned;

	public EnemyPacket(int id, PacketType type, String sourceIp, int port, double threatScore) {
		super(id, type, sourceIp, port, threatScore);
        this.enemyType = enemyType;
        this.damage = damage;
        this.isSpawned = true;
	}
	
}
