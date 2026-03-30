
public class Judge {
	public Result evaluateDrop(Packet packet, Zone zone, RuleSet ruleSet) {
		Decision decision = convertZoneToDecision(zone);
		Result result = new Result(false, decision, "");
		
		if (packet instanceof EnemyPacket) {
            EnemyPacket enemyPacket = (EnemyPacket) packet;
            result.isCorrect = (decision == Decision.BLOCK) && (enemyPacket.getType() != PacketType.GOOD);
            result.reason = enemyPacket.getEnemyType() + " should be blocked!";
           if (result.isCorrect) {
                enemyPacket.deactivate(); 
                result.decision = Decision.BLOCK; 
            } else {
                result.decision = Decision.ALLOW; 
            }
        } else {
            // Handle normal packets
            result.isCorrect = (decision == ruleSet.classify(packet));
            result.reason = "Packet handled according to rules.";
        }
		return result;
	}

	private Decision convertZoneToDecision(Zone zone) {
		// TODO Auto-generated method stub
		return null;
	}
}
