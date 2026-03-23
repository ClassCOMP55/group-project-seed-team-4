import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;

public class WelcomePane extends GraphicsPane {

	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;

	private static final Color BG         = new Color(  0,   1,   4);
	private static final Color NEON_CYAN  = new Color(  0, 200, 230);
	private static final Color NEON_GREEN = new Color( 57, 255, 100);
	private static final Color NEON_PURP  = new Color(180, 100, 255);
	private static final Color DIM_CYAN   = new Color(  0, 120, 160);
	private static final Color GRID_COLOR = new Color(  8,  28,  45);

	private Font fLogoHacked; 
	private Font fSub;
	private Font fBoot;
	private Font fBtn;
	private Font fTicker;

	private static final int BLOCK_H = 644;
	private static final int TOP_Y   = (H - BLOCK_H) / 2;

	private Rectangle startRegion;
	private Rectangle settingsRegion;
	private Rectangle shopRegion;

	public WelcomePane(MainApplication mainScreen) {
		super();
		this.mainScreen = mainScreen;
		// Derive sized fonts from the loaded base fonts
		fLogoHacked = MainApplication.FONT_HACKED.deriveFont(Font.BOLD,  90f);
		fSub        = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fBoot       = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 18f);
		fBtn        = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  26f);
		fTicker     = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
	}

	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawLogotype();
		drawBootLines();
		drawMenuButtons();
		drawTickerBar();
	}

	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
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

	private void drawLogotype() {
		int titleY = TOP_Y + 120;

		GLabel title = new GLabel("FIREWALL FRENZY", 0, titleY);
		title.setFont(fLogoHacked); title.setColor(NEON_CYAN);
		title.setLocation((W - title.getWidth()) / 2.0, titleY);
		add(title);

		GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 10,
		                     (W + title.getWidth()) / 2.0, titleY + 10);
		ul.setColor(DIM_CYAN); add(ul);

		GLabel sub = new GLabel("PACKET  DEFENSE  SIMULATOR  //  v2.0", 0, titleY + 40);
		sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 40);
		add(sub);
	}

	private void drawBootLines() {
		int bootY = TOP_Y + 260;
		String[] lines = {
			">  INITIALIZING FIREWALL ENGINE ...    [ OK ]",
			">  LOADING PACKET DATABASE     ...    [ OK ]",
			">  AWAITING OPERATOR INPUT",
		};
		Color[] colors = {
			new Color(0, 180, 160),
			new Color(0, 180, 160),
			new Color(255, 220, 0),
		};
		for (int i = 0; i < lines.length; i++) {
			GLabel lbl = new GLabel(lines[i], 0, bootY + i * 30);
			lbl.setFont(fBoot); lbl.setColor(colors[i]);
			lbl.setLocation((W - lbl.getWidth()) / 2.0, bootY + i * 30);
			add(lbl);
		}
	}

	private void drawMenuButtons() {
		int btnStartY = TOP_Y + 370;
		String[] labels = { "START", "SETTINGS", "SHOP" };
		Color[]  colors = { NEON_CYAN, NEON_GREEN, NEON_PURP };
		int btnW = 480, btnH = 70, gap = 22;
		int startX = (W - btnW) / 2;
		for (int i = 0; i < 3; i++) {
			int by = btnStartY + i * (btnH + gap);
			Rectangle r = makeButton(startX, by, btnW, btnH, labels[i], colors[i]);
			if      (i == 0) startRegion    = r;
			else if (i == 1) settingsRegion = r;
			else             shopRegion     = r;
		}
	}

	private Rectangle makeButton(int x, int y, int w, int h, String label, Color col) {
		GRect btn = new GRect(x, y, w, h);
		btn.setFilled(true);
		btn.setFillColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 30));
		btn.setColor(col);
		add(btn);

		GLine bar = new GLine(x+6, y+10, x+6, y+h-10);
		bar.setColor(col); add(bar);

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
			">>  FIREWALL FRENZY  |  DEFEND YOUR NETWORK  |  CLASSIFY PACKETS  |  ALLOW  //  BLOCK  //  QUARANTINE",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		add(ticker);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		if      (startRegion    != null && startRegion.contains(mx, my))    mainScreen.switchToDifficultyScreen();
		else if (settingsRegion != null && settingsRegion.contains(mx, my)) mainScreen.switchToSettingsScreen();
		else if (shopRegion     != null && shopRegion.contains(mx, my))     mainScreen.switchToShopScreen();
	}
}