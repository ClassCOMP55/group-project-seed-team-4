import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;
 
public class GameOverPane extends GraphicsPane {
 
	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;
 
	private static final Color BG          = new Color(  0,   1,   4);
	private static final Color NEON_RED    = new Color(255,  40,  60);
	private static final Color NEON_CYAN   = new Color(  0, 200, 230);
	private static final Color NEON_YELLOW = new Color(255, 220,   0);
	private static final Color DIM_CYAN    = new Color(  0, 120, 160);
	private static final Color PANEL_BG    = new Color(  0,   8,  18);
	private static final Color GRID_COLOR  = new Color(  8,  28,  45);
 
	private Font fTitle;
	private Font fSub;
	private Font fChip;
	private Font fStatL;
	private Font fStatV;
	private Font fHdr;
	private Font fBtn;
	private Font fTicker;
 
	private static final int PX = 110;
	private static final int PY = 100;
	private static final int PW = W - 220;
	private static final int PH = H - 200;
 
	private static final int INNER_TOP    = PY + 70;
	private static final int INNER_H      = (PY + PH) - INNER_TOP;
	private static final int BLOCK_H      = 443;
	private static final int CONTENT_Y    = INNER_TOP + (INNER_H - BLOCK_H) / 2;
 
	private int    finalScore   = 0;
	private int    finalLives   = 0;
	private String causeOfDeath = "FIREWALL BREACHED";
 
	private Rectangle retryRegion;
	private Rectangle menuRegion;
 
	public GameOverPane(MainApplication mainScreen) {
		super();
		this.mainScreen = mainScreen;
		fTitle  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  52f);
		fSub    = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 22f);
		fChip   = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 16f);
		fStatL  = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fStatV  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  20f);
		fHdr    = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  18f);
		fBtn    = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  24f);
		fTicker = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
	}
 
	public void setResults(int score, int lives, String cause) {
		this.finalScore   = score;
		this.finalLives   = lives;
		this.causeOfDeath = cause;
	}
 
	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawAlertPanel();
		drawTitle();
		drawStatsPanel();
		drawButtons();
		drawTickerBar();
	}
 
	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
		retryRegion = null;
		menuRegion  = null;
	}
 
	private void add(GObject o) { contents.add(o); mainScreen.add(o); }
 
	private void drawBackground() {
		int rw = (int) mainScreen.getWidth();
		int rh = (int) mainScreen.getHeight();
		GRect bg = new GRect(0, 0, rw, rh);
		bg.setFilled(true); bg.setFillColor(BG); bg.setColor(BG);
		add(bg);
	}
 
	private void drawGrid() {
		int s = 60;
		for (int x = 0; x <= W; x += s) { GLine l = new GLine(x,0,x,H); l.setColor(GRID_COLOR); add(l); }
		for (int y = 0; y <= H; y += s) { GLine l = new GLine(0,y,W,y); l.setColor(GRID_COLOR); add(l); }
	}
 
	private void drawCornerBrackets() {
		int cs = 36, m = 55;
		int[][] corners = {{m,m},{W-m-cs,m},{m,H-m-cs},{W-m-cs,H-m-cs}};
		for (int[] c : corners) {
			int cx = c[0], cy = c[1];
			GLine[] arms = {
				new GLine(cx,cy,cx+cs,cy), new GLine(cx,cy+cs,cx+cs,cy+cs),
				new GLine(cx,cy,cx,cy+cs), new GLine(cx+cs,cy,cx+cs,cy+cs)
			};
			for (GLine l : arms) { l.setColor(NEON_CYAN); add(l); }
		}
	}
 
	private void drawAlertPanel() {
		GRect panel = new GRect(PX, PY, PW, PH);
		panel.setFilled(true); panel.setFillColor(PANEL_BG); panel.setColor(NEON_RED);
		add(panel);
 
		GLine accent = new GLine(PX+30, PY+60, PX+PW-30, PY+60);
		accent.setColor(DIM_CYAN); add(accent);
 
		addChip(PX+32,       PY+42, "●  SYSTEM ALERT",  NEON_RED);
		addChip(PX+PW-280,   PY+42, "ERR CODE: 0xDEAD", NEON_YELLOW);
	}
 
	private void addChip(int x, int y, String text, Color col) {
		GLabel lbl = new GLabel(text, x, y);
		lbl.setFont(fChip); lbl.setColor(col);
		add(lbl);
	}
 
	private void drawTitle() {
		int titleY = CONTENT_Y + 55;
 
		GLabel cause = new GLabel(causeOfDeath.toUpperCase(), 0, titleY);
		cause.setFont(fTitle); cause.setColor(NEON_RED);
		cause.setLocation((W - cause.getWidth()) / 2.0, titleY);
		add(cause);
 
		GLine ul = new GLine(
			(W - cause.getWidth()) / 2.0, titleY + 8,
			(W + cause.getWidth()) / 2.0, titleY + 8);
		ul.setColor(NEON_RED); add(ul);
 
		GLabel sub = new GLabel("CONNECTION  TERMINATED", 0, titleY + 48);
		sub.setFont(fSub); sub.setColor(NEON_CYAN);
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 48);
		add(sub);
	}
 
	private void drawStatsPanel() {
		int bw = 640, bh = 220;
		int bx = (W - bw) / 2;
		int by = CONTENT_Y + 130;
 
		GRect box = new GRect(bx, by, bw, bh);
		box.setFilled(true); box.setFillColor(new Color(0,14,28)); box.setColor(DIM_CYAN);
		add(box);
 
		GLabel hdr = new GLabel("---  SESSION  REPORT  ---", 0, by+36);
		hdr.setFont(fHdr); hdr.setColor(NEON_CYAN);
		hdr.setLocation((W - hdr.getWidth()) / 2.0, by+36);
		add(hdr);
 
		GLine hdiv = new GLine(bx+20, by+46, bx+bw-20, by+46);
		hdiv.setColor(DIM_CYAN); add(hdiv);
 
		addStatRow(bx+28, by+86,  "FINAL  SCORE",     String.format("%06d", finalScore), NEON_CYAN);
		String livesStr = finalLives <= 0 ? "0   [ CRITICAL ]" : String.valueOf(finalLives);
		Color  livesCol = finalLives <= 0 ? NEON_RED : NEON_YELLOW;
		addStatRow(bx+28, by+134, "LIVES  REMAINING", livesStr, livesCol);
		addStatRow(bx+28, by+182, "STATUS",           "OFFLINE", NEON_RED);
	}
 
	private void addStatRow(int x, int y, String label, String value, Color valCol) {
		GLabel lbl = new GLabel(label + ":", x, y);
		lbl.setFont(fStatL); lbl.setColor(new Color(100, 170, 200));
		add(lbl);
 
		GLabel val = new GLabel(value, x+340, y);
		val.setFont(fStatV); val.setColor(valCol);
		add(val);
	}
 
	private void drawButtons() {
		int btnW = 260, btnH = 68, gap = 40;
		int startX = (W - (btnW * 2 + gap)) / 2;
		int by = CONTENT_Y + 375;
 
		retryRegion = makeButton(startX,          by, btnW, btnH, "[ RETRY ]", NEON_CYAN);
		menuRegion  = makeButton(startX+btnW+gap, by, btnW, btnH, "[ MENU  ]", NEON_YELLOW);
	}
 
	private Rectangle makeButton(int x, int y, int w, int h, String label, Color col) {
		GRect btn = new GRect(x, y, w, h);
		btn.setFilled(true);
		btn.setFillColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 35));
		btn.setColor(col);
		add(btn);
 
		GLabel lbl = new GLabel(label, 0, 0);
		lbl.setFont(fBtn); lbl.setColor(col);
		lbl.setLocation(x + (w - lbl.getWidth()) / 2.0, y + (h / 2.0) + 9);
		add(lbl);
 
		return new Rectangle(x, y, w, h);
	}
 
	private void drawTickerBar() {
		GLine div = new GLine(60, H-50, W-60, H-50);
		div.setColor(DIM_CYAN); add(div);
 
		GLabel ticker = new GLabel(
			">>  FIREWALL FRENZY  |  SYSTEM OFFLINE  |  THREAT LEVEL: CRITICAL  |  AWAITING REBOOT",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 160, 190));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		add(ticker);
	}
 
	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		if      (retryRegion != null && retryRegion.contains(mx, my)) mainScreen.switchToDifficultyScreen();
		else if (menuRegion  != null && menuRegion.contains(mx, my))  mainScreen.switchToWelcomeScreen();
	}
}