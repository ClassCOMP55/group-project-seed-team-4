
public class PacketInfo {
	public String type;
	public String sourceIp;
	public String port;
	public String hint;
	
	public PacketInfo(String type, String sourceIp, String port, String hint) {
		this.type = type;
		this.sourceIp = sourceIp;
		this.port = port;
		this.hint = hint;
	}
}
