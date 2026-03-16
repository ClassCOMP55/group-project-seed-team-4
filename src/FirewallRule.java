
public class FirewallRule {
	private PacketType packetType;
	private int portRange;
	private double threatThreshold;
	private Decision decision;
	
	public FirewallRule(PacketType packetType, int portRange, double threatThreshold, Decision decision) {
		this.packetType = packetType;
		this.portRange = portRange;
		this.threatThreshold = threatThreshold;
		this.decision = decision;
	}
}
