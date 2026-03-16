
public class Packet {
	private int id;
	private PacketType type;
	private String sourceIp;
	private int port;
	private double threatScore;
	
	public Packet(int id, PacketType type, String sourceIp, int port, double threatScore) {
		this.id = id;
		this.type = type;
		this.sourceIp = sourceIp;
		this.port = port;
		this.threatScore = threatScore;
	}
	
	public int getId() {
		return id;
	}
}
