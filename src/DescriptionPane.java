import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;

public class DescriptionPane extends GraphicsPane {

    private static final Color BG         = new Color(  0,   1,   4);
    private static final Color NEON_CYAN  = new Color(  0, 200, 230);
    private static final Color NEON_GREEN = new Color( 57, 255, 100);
    private static final Color NEON_RED   = new Color(255,  50,  50);
    private static final Color NEON_YELL  = new Color(255, 220,   0);
    private static final Color NEON_PURP  = new Color(180, 100, 255);
    private static final Color DIM_CYAN   = new Color(  0, 120, 160);
    private static final Color GRID_COLOR = new Color(  8,  28,  45);
    private static final Color PANEL_BG   = new Color(  0,   8,  18);

    private Font fTitle;
    private Font fSub;
    private Font fCardName;
    private Font fCardDesc;
    private Font fTag;
    private Font fBtn;
    private Font fTicker;

    private Rectangle continueRegion;
    private Rectangle backRegion;

    private static final String[][] ENTRIES = {
        { "GOOD PACKET",   "GoodPacket.png",      "ALLOW",
          "Safe data. Do NOT destroy. Let it pass through to gain $10. Destroying one costs you a life." },
        { "DATA BURST",    "DataBurst.png",        "ALLOW",
          "High-volume safe traffic. Do NOT destroy. Passes through for $15. Slightly faster than a Good Packet." },
        { "VIRUS",         "VirusPacket.png",      "DESTROY",
          "Classic malware. Click to neutralize for $20. If it reaches the base, you lose 1 life." },
        { "TROJAN",        "TrojanPacket.png",     "DESTROY",
          "Faster than a Virus. Click to destroy for $25, or lose 1 life on breach." },
        { "DDoS",          "DDoSPacket.png",       "DESTROY",
          "If it enters the system, you will be unable to do anything for 3 seconds. Destroy for $30." },
        { "PHISHING",      "PhishingPacket.png",   "DESTROY",
          "Disguises itself as a friendly packet, briefly revealing its true form. Stay alert. Worth $20 when destroyed." },
        { "RANSOMWARE",    "RansomwarePacket.png", "DESTROY",
          "Upon breaching the system, it deducts $200 from your score instead of a life. Destroy it early to prevent the penalty." },
    };

    private static final Color[] ENTRY_COLORS = {
        NEON_CYAN,  NEON_YELL, NEON_RED, NEON_RED, NEON_RED, NEON_PURP, NEON_PURP,
    };

    public DescriptionPane(MainApplication mainScreen) {
        super(mainScreen);
        fTitle    = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  48f);
        fSub      = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 19f);
        fCardName = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
        fCardDesc = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 16f);
        fTag      = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  14f);
        fBtn      = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
        fTicker   = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
    }

    @Override
    public void showContent() {
        int W = (int) mainScreen.getWidth();
        int H = (int) mainScreen.getHeight();

        int CARD_W   = (int)(W * 0.70);
        int CARD_H   = 72;
        int CARD_GAP = 14;
        int CARD_X   = (W - CARD_W) / 2;
        int N        = ENTRIES.length;

        int HEADER_H = 100;
        int BTN_H    = 62;
        int TICKER_H = 55;
        int CARD_GAP_BTN = 18;
        int HEADER_GAP   = 18;

        int blockH        = CARD_H * N + CARD_GAP * (N - 1);
        int totalContentH = HEADER_H + HEADER_GAP + blockH + CARD_GAP_BTN + BTN_H;
        int topY          = (H - TICKER_H - totalContentH) / 2;

        int headerY = topY;
        int cardsY  = headerY + HEADER_H + HEADER_GAP;
        int btnY    = cardsY  + blockH   + CARD_GAP_BTN;

        drawBackground(W, H);
        drawGrid(W, H);
        drawCornerBrackets(W, H);
        drawHeader(W, headerY);
        drawCards(W, CARD_X, CARD_W, CARD_H, CARD_GAP, cardsY);
        drawButtons(W, btnY, BTN_H);
        drawTickerBar(W, H);
    }

    @Override
    public void hideContent() {
        for (GObject o : contents) mainScreen.remove(o);
        contents.clear();
        continueRegion = null;
        backRegion     = null;
    }

    private void addContent(GObject o) { contents.add(o); mainScreen.add(o); }

    private void drawBackground(int W, int H) {
        GRect bg = new GRect(0, 0, W, H);
        bg.setFilled(true); bg.setFillColor(BG); bg.setColor(BG);
        addContent(bg);
    }

    private void drawGrid(int W, int H) {
        int s = 60;
        for (int x = 0; x <= W; x += s) { GLine l = new GLine(x,0,x,H); l.setColor(GRID_COLOR); addContent(l); }
        for (int y = 0; y <= H; y += s) { GLine l = new GLine(0,y,W,y); l.setColor(GRID_COLOR); addContent(l); }
    }

    private void drawCornerBrackets(int W, int H) {
        int cs = 36, m = 55;
        int[][] corners = {{m,m},{W-m-cs,m},{m,H-m-cs},{W-m-cs,H-m-cs}};
        for (int[] c : corners) {
            int cx = c[0], cy = c[1];
            GLine[] arms = {
                new GLine(cx,cy,cx+cs,cy), new GLine(cx,cy+cs,cx+cs,cy+cs),
                new GLine(cx,cy,cx,cy+cs), new GLine(cx+cs,cy,cx+cs,cy+cs)
            };
            for (GLine l : arms) { l.setColor(NEON_CYAN); addContent(l); }
        }
    }

    private void drawHeader(int W, int topY) {
        int titleY = topY + 52;
        GLabel title = new GLabel("THREAT INTELLIGENCE BRIEFING", 0, titleY);
        title.setFont(fTitle); title.setColor(NEON_CYAN);
        title.setLocation((W - title.getWidth()) / 2.0, titleY);
        addContent(title);

        GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 8,
                             (W + title.getWidth()) / 2.0, titleY + 8);
        ul.setColor(DIM_CYAN); addContent(ul);

        GLabel sub = new GLabel(">  CLASSIFY PACKETS CORRECTLY  //  ALLOW FRIENDLIES  //  NEUTRALIZE THREATS", 0, titleY + 42);
        sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
        sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 42);
        addContent(sub);
    }

    private void drawCards(int W, int cardX, int cardW, int cardH, int cardGap, int cardsY) {
        for (int i = 0; i < ENTRIES.length; i++) {
            int cy = cardsY + i * (cardH + cardGap);
            drawCard(cardX, cy, cardW, cardH, ENTRIES[i], ENTRY_COLORS[i]);
        }
    }

    private void drawCard(int x, int y, int cardW, int cardH, String[] entry, Color col) {
        String name = entry[0], sprite = entry[1], action = entry[2], desc = entry[3];

        GRect card = new GRect(x, y, cardW, cardH);
        card.setFilled(true); card.setFillColor(PANEL_BG); card.setColor(col);
        addContent(card);

        GRect bar = new GRect(x, y, 5, cardH);
        bar.setFilled(true); bar.setFillColor(col); bar.setColor(col);
        addContent(bar);

        int imgSize = 68;
        int imgX = x + 18;
        int imgY = y + (cardH - imgSize) / 2;
        try {
            GImage img = new GImage(sprite, imgX, imgY);
            img.setSize(imgSize, imgSize);
            addContent(img);
        } catch (Exception ex) {
            GRect fb = new GRect(imgX, imgY, imgSize, imgSize);
            fb.setFilled(true); fb.setFillColor(new Color(30,30,30)); fb.setColor(col);
            addContent(fb);
        }

        int textX = imgX + imgSize + 20;
        int midY  = y + cardH / 2;

        GLabel nameLbl = new GLabel(name, textX, midY - 6);
        nameLbl.setFont(fCardName); nameLbl.setColor(col);
        addContent(nameLbl);

        GLabel descLbl = new GLabel(desc, textX, midY + 18);
        descLbl.setFont(fCardDesc); descLbl.setColor(new Color(160, 200, 215));
        addContent(descLbl);

        boolean allow  = action.equals("ALLOW");
        Color tagCol   = allow ? NEON_GREEN : NEON_RED;
        Color tagBg    = allow ? new Color(0, 40, 15) : new Color(40, 0, 0);

        GLabel tagLbl = new GLabel("  " + action + "  ", 0, midY - 6);
        tagLbl.setFont(fTag); tagLbl.setColor(tagCol);
        double tagX = x + cardW - tagLbl.getWidth() - 18;
        tagLbl.setLocation(tagX, midY - 6);

        GRect tagBox = new GRect(tagX - 2, midY - 20, tagLbl.getWidth() + 4, 22);
        tagBox.setFilled(true); tagBox.setFillColor(tagBg); tagBox.setColor(tagCol);
        addContent(tagBox);
        addContent(tagLbl);
    }

    private void drawButtons(int W, int btnY, int btnH) {
        int btnW  = 380;
        int gap   = 20;
        int total = btnW * 2 + gap;
        int bx    = (W - total) / 2;

        GRect backBtn = new GRect(bx, btnY, btnW, btnH);
        backBtn.setFilled(true); backBtn.setFillColor(new Color(0,20,35)); backBtn.setColor(DIM_CYAN);
        addContent(backBtn);
        GLabel backLbl = new GLabel("< BACK TO MENU", 0, 0);
        backLbl.setFont(fBtn); backLbl.setColor(new Color(0, 170, 200));
        backLbl.setLocation(bx + (btnW - backLbl.getWidth()) / 2.0, btnY + btnH / 2.0 + 9);
        addContent(backLbl);
        backRegion = new Rectangle(bx, btnY, btnW, btnH);

        int cx = bx + btnW + gap;
        GRect contBtn = new GRect(cx, btnY, btnW, btnH);
        contBtn.setFilled(true); contBtn.setFillColor(new Color(0,25,15)); contBtn.setColor(NEON_GREEN);
        addContent(contBtn);
        GLabel contLbl = new GLabel("CONTINUE  >", 0, 0);
        contLbl.setFont(fBtn); contLbl.setColor(NEON_GREEN);
        contLbl.setLocation(cx + (btnW - contLbl.getWidth()) / 2.0, btnY + btnH / 2.0 + 9);
        addContent(contLbl);
        continueRegion = new Rectangle(cx, btnY, btnW, btnH);
    }

    private void drawTickerBar(int W, int H) {
        GLine div = new GLine(60, H-50, W-60, H-50);
        div.setColor(DIM_CYAN); addContent(div);

        GLabel ticker = new GLabel(
            ">>  FIREWALL FRENZY  |  THREAT INTELLIGENCE  |  KNOW YOUR ENEMY  |  DEFEND THE NETWORK",
            0, H-22);
        ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
        ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
        addContent(ticker);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        if (continueRegion != null && continueRegion.contains(mx, my)) {
            mainScreen.switchToDifficultyScreen();
            return;
        }
        if (backRegion != null && backRegion.contains(mx, my)) {
            mainScreen.switchToWelcomeScreen();
        }
    }
}