import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;

public class SettingsPane extends GraphicsPane {

	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;

	private static final Color BG         = new Color(  0,   1,   4);
	private static final Color NEON_CYAN  = new Color(  0, 200, 230);
	private static final Color NEON_GREEN = new Color( 57, 255, 100);
	private static final Color DIM_CYAN   = new Color(  0, 120, 160);
	private static final Color GRID_COLOR = new Color(  8,  28,  45);
	private static final Color PANEL_BG   = new Color(  0,   8,  18);

	private Font fTitle;
	private Font fSub;
	private Font fItem;
	private Font fBack;
	private Font fTicker;

	// Placeholder settings, no functionality yet
	private static final String[] SETTING_LABELS = {
		"SOUND EFFECTS",
		"MUSIC VOLUME",
		"SHOW HINTS",
	};
	private static final String[] SETTING_VALUES = {
		"ON",
		"80%",
		"ON",
	};

	private static final int BLOCK_H = 616;
	private static final int TOP_Y   = (H - BLOCK_H) / 2;

	private Rectangle backRegion;

	public SettingsPane(MainApplication mainScreen) {
		super();
		this.mainScreen = mainScreen;
		fTitle  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  52f);
		fSub    = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fItem   = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 22f);
		fBack   = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
		fTicker = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
	}

	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawHeader();
		drawSettingsPanel();
		drawBackButton();
		drawTickerBar();
	}

	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
		backRegion = null;
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

	private void drawHeader() {
		int titleY = TOP_Y + 55;

		GLabel title = new GLabel("SETTINGS", 0, titleY);
		title.setFont(fTitle); title.setColor(NEON_CYAN);
		title.setLocation((W - title.getWidth()) / 2.0, titleY);
		add(title);

		GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 8,
		                     (W + title.getWidth()) / 2.0, titleY + 8);
		ul.setColor(DIM_CYAN); add(ul);

		GLabel sub = new GLabel(">  SYSTEM CONFIGURATION PANEL", 0, titleY + 40);
		sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 40);
		add(sub);
	}

	private void drawSettingsPanel() {
		int panelW = 900, rowH = 70, gap = 14;
		int panelH = SETTING_LABELS.length * rowH + (SETTING_LABELS.length - 1) * gap + 40;
		int panelX = (W - panelW) / 2;
		int panelY = TOP_Y + 120;

		GRect panel = new GRect(panelX, panelY, panelW, panelH);
		panel.setFilled(true); panel.setFillColor(PANEL_BG); panel.setColor(DIM_CYAN);
		add(panel);

		for (int i = 0; i < SETTING_LABELS.length; i++) {
			int ry = panelY + 20 + i * (rowH + gap);

			if (i > 0) {
				GLine div = new GLine(panelX + 20, ry - gap/2, panelX + panelW - 20, ry - gap/2);
				div.setColor(new Color(0, 60, 80)); add(div);
			}

			GRect bar = new GRect(panelX, ry, 4, rowH);
			bar.setFilled(true); bar.setFillColor(NEON_CYAN); bar.setColor(NEON_CYAN);
			add(bar);

			GLabel lbl = new GLabel(SETTING_LABELS[i], panelX + 24, ry + rowH/2 + 8);
			lbl.setFont(fItem); lbl.setColor(new Color(160, 200, 215));
			add(lbl);

			GLabel val = new GLabel(SETTING_VALUES[i], 0, ry + rowH/2 + 8);
			val.setFont(fItem); val.setColor(NEON_GREEN);
			val.setLocation(panelX + panelW - val.getWidth() - 24, ry + rowH/2 + 8);
			add(val);
		}
	}

	private void drawBackButton() {
		int bw = 320, bh = 60;
		int bx = (W - bw) / 2;
		int by = TOP_Y + BLOCK_H - bh;

		GRect btn = new GRect(bx, by, bw, bh);
		btn.setFilled(true); btn.setFillColor(new Color(0,20,35)); btn.setColor(DIM_CYAN);
		add(btn);

		GLabel lbl = new GLabel("< BACK TO MENU", 0, 0);
		lbl.setFont(fBack); lbl.setColor(new Color(0, 170, 200));
		lbl.setLocation(bx + (bw - lbl.getWidth()) / 2.0, by + (bh / 2.0) + 8);
		add(lbl);

		backRegion = new Rectangle(bx, by, bw, bh);
	}

	private void drawTickerBar() {
		GLine div = new GLine(60, H-50, W-60, H-50);
		div.setColor(DIM_CYAN); add(div);

		GLabel ticker = new GLabel(
			">>  FIREWALL FRENZY  |  SYSTEM CONFIGURATION  |  CHANGES APPLY ON NEXT SESSION",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		add(ticker);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		if (backRegion != null && backRegion.contains(mx, my)) {
			mainScreen.switchToWelcomeScreen();
		}
	}
}