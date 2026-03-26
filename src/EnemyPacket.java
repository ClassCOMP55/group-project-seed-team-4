
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
	
	public String getEnemyType() {
		return enemyType;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public boolean getisSpawned() {
		return isSpawned;
	}
	
	public void deactivate() {
		isSpawned = false;
	}
}
