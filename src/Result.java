
public class Result {
	public boolean isCorrect;
	public Decision decision;
	public String reason;
	
	public Result(boolean isCorrect, Decision decision, String reason) {
		this.isCorrect = isCorrect;
		this.decision = decision;
		this.reason = reason;
	}
}
