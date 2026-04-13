public enum PacketType {

    // Friendly
    // isBad, damage, speedMult, points, normalSprite, destroyedSprite, ransomPenalty
    GOOD        (false, 0, 1.0,  10, "GoodPacket.png",       "GoodPacketSplit.png",    0),
    DATA_BURST  (false, 0, 1.2,  15, "DataBurst.png",        "DataBurstSplit.png",     0),

    // Malicious
    VIRUS       (true,  1, 1.0,  20, "VirusPacket.png",      "VirusSplit.png",         0),
    TROJAN      (true,  1, 1.6,  25, "TrojanPacket.png",     "TrojanSplit.png",        0),
    DDOS        (true,  0, 1.3,  30, "DDoSPacket.png",       "DDoSSplit.png",          0),
    PHISHING    (true,  1, 1.0,  20, "PhishingPacket.png",   "PhishingSplit.png",      0),
    RANSOMWARE  (true,  0, 1.1,  25, "RansomwarePacket.png","RansomwareSplit.png",  200);

    private final boolean isBad;
    private final int     damage;
    private final double  speedMult;
    private final int     points;
    private final String  normalSprite;
    private final String  destroyedSprite;
    private final int     ransomPenalty;

    PacketType(boolean isBad, int damage, double speedMult, int points,
               String normalSprite, String destroyedSprite, int ransomPenalty) {
        this.isBad          = isBad;
        this.damage         = damage;
        this.speedMult      = speedMult;
        this.points         = points;
        this.normalSprite   = normalSprite;
        this.destroyedSprite = destroyedSprite;
        this.ransomPenalty  = ransomPenalty;
    }

    public boolean isBad()            { return isBad;           }
    public int     getDamage()         { return damage;          }
    public double  getBaseSpeedMult()  { return speedMult;       }
    public int     getPoints()         { return points;          }
    public String  getNormalSprite()   { return normalSprite;    }
    public String  getDestroyedSprite(){ return destroyedSprite; }
    public int     getRansomPenalty()  { return ransomPenalty;   }
}