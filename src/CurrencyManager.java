
public class CurrencyManager {
	private int secureTokens;
	private static final double[] CONVERSION_RATES = {0.02, 0.05, 0.08}; //NOOB, PRO, HACKER
	
	public CurrencyManager() {
		this.secureTokens = 0;
	}
	
	public void addTokensFromScore(int score, String difficulty) {
		double rate = CONVERSION_RATES[0];
		if (difficulty.equals("PRO")) rate = CONVERSION_RATES[1];
		else if (difficulty.equals("HACKER")) rate = CONVERSION_RATES[2];
		
		int tokensEarned = (int) (score * rate);
		secureTokens += tokensEarned;
	}
	
	public boolean spendTokens(int amount) {
		if (secureTokens >= amount) {
			secureTokens -= amount;
			return true;
		}
		return false;
	}
	
	public int getTokens() {return secureTokens;}
	public void setTokens(int tokens) {this.secureTokens = tokens;}
}
