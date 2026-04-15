
public class CurrencyManager {
	private int secureTokens;
	private static final double[] CONVERSION_RATES = {0.02, 0.05, 0.08}; //NOOB, PRO, HACKER
	private static final String SAVE_FILE = "secure_tokens.dat";
	
	public CurrencyManager() {
		this.secureTokens = 0;
		//load();
	}
	
	public void addTokensFromScore(int score, String difficulty) {
        double rate = CONVERSION_RATES[0];
        if ("PRO".equals(difficulty)) rate = CONVERSION_RATES[1];
        else if ("HACKER".equals(difficulty)) rate = CONVERSION_RATES[2];

        int tokensEarned = (int) (score * rate);
        secureTokens += tokensEarned;
        //save();
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
