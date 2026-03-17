
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
	
	public boolean matches(Packet p) {
		if (packetType != null && p.getType() != packetType) {
			return false;
		}
		
		if (p.getPort() != portRange) {
			return false;
		}
		
		if (p.getThreatScore() < threatThreshold) {
			return false;
		}
		
		return true;
	}
}
