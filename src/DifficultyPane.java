import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;
 
public class DifficultyPane extends GraphicsPane {
 
	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;
 
	private static final Color BG          = new Color(  0,   1,   4);
	private static final Color NEON_CYAN   = new Color(  0, 200, 230);
	private static final Color NEON_GREEN  = new Color( 57, 255, 100);
	private static final Color NEON_YELLOW = new Color(255, 220,   0);
	private static final Color NEON_RED    = new Color(255,  40,  60);
	private static final Color DIM_CYAN    = new Color(  0, 120, 160);
	private static final Color GRID_COLOR  = new Color(  8,  28,  45);
 
	private Font fTitle;
	private Font fSub;
	private Font fName;
	private Font fDesc;
	private Font fCode;
	private Font fBack;
	private Font fTicker;
 
	private static final String[] NAMES  = { "NOOB",    "PRO",    "HACKER"  };
	private static final String[] DESCS  = {
		"BASIC THREATS  |  3 LIVES  |  SLOW PACKETS",
		"MIXED THREATS  |  3 LIVES  |  FAST PACKETS",
		"ALL THREATS    |  2 LIVES  |  MAX SPEED"
	};
	private static final Color[]  COLORS = { NEON_GREEN, NEON_YELLOW, NEON_RED };
	private static final String[] CODES  = { "[LVL_01]", "[LVL_02]", "[LVL_03]" };
 
	private static final int BLOCK_H = 656;
	private static final int TOP_Y   = (H - BLOCK_H) / 2;
 
	private Rectangle[] diffRegions = new Rectangle[3];
	private Rectangle   backRegion;
 
	public DifficultyPane(MainApplication mainScreen) {
		super(mainScreen);
		fTitle  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  52f);
		fSub    = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fName   = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  32f);
		fDesc   = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 18f);
		fCode   = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 16f);
		fBack   = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
		fTicker = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
	}
 
	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawHeader();
		drawDifficultyCards();
		drawBackButton();
		drawTickerBar();
	}
 
	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
	}
 
	private void addContent(GObject o) { contents.add(o); mainScreen.add(o); }
 
	private void drawBackground() {
		int rw = (int) mainScreen.getWidth();
		int rh = (int) mainScreen.getHeight();
		GRect bg = new GRect(0, 0, rw, rh);
		bg.setFilled(true); bg.setFillColor(BG); bg.setColor(BG);
		addContent(bg);
	}
 
	private void drawGrid() {
		int s = 60;
		for (int x = 0; x <= W; x += s) { GLine l = new GLine(x,0,x,H); l.setColor(GRID_COLOR); addContent(l); }
		for (int y = 0; y <= H; y += s) { GLine l = new GLine(0,y,W,y); l.setColor(GRID_COLOR); addContent(l); }
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
			for (GLine l : arms) { l.setColor(NEON_CYAN); addContent(l); }
		}
	}
 
	private void drawHeader() {
		int titleY = TOP_Y + 55;
 
		GLabel title = new GLabel("SELECT  DIFFICULTY", 0, titleY);
		title.setFont(fTitle); title.setColor(NEON_CYAN);
		title.setLocation((W - title.getWidth()) / 2.0, titleY);
		addContent(title);
 
		GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 8,
		                     (W + title.getWidth()) / 2.0, titleY + 8);
		ul.setColor(DIM_CYAN); addContent(ul);
 
		GLabel sub = new GLabel(">  CHOOSE YOUR THREAT CLASSIFICATION PROTOCOL", 0, titleY + 40);
		sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 40);
		addContent(sub);
	}
 
	private void drawDifficultyCards() {
		int cardW = 900, cardH = 130, gap = 28;
		int cardX = (W - cardW) / 2;
		int cardsY = TOP_Y + 120;
		for (int i = 0; i < 3; i++) {
			int cy = cardsY + i * (cardH + gap);
			diffRegions[i] = drawCard(cardX, cy, cardW, cardH,
				NAMES[i], DESCS[i], CODES[i], COLORS[i]);
		}
	}
 
	private Rectangle drawCard(int x, int y, int w, int h,
			String name, String desc, String code, Color col) {
		GRect card = new GRect(x, y, w, h);
		card.setFilled(true);
		card.setFillColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 28));
		card.setColor(col);
		addContent(card);
 
		GRect bar = new GRect(x, y, 5, h);
		bar.setFilled(true); bar.setFillColor(col); bar.setColor(col);
		addContent(bar);
 
		GLabel nameLbl = new GLabel("   " + name, x+24, y+52);
		nameLbl.setFont(fName); nameLbl.setColor(col);
		addContent(nameLbl);
 
		GLabel descLbl = new GLabel(desc, x+26, y+96);
		descLbl.setFont(fDesc); descLbl.setColor(new Color(160, 200, 215));
		addContent(descLbl);
 
		GLabel codeLbl = new GLabel(code, x+w-120, y+30);
		codeLbl.setFont(fCode);
		codeLbl.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 180));
		addContent(codeLbl);
 
		return new Rectangle(x, y, w, h);
	}
 
	private void drawBackButton() {
		int bw = 320, bh = 60;
		int bx = (W - bw) / 2;
		int by = TOP_Y + BLOCK_H - bh;
 
		GRect btn = new GRect(bx, by, bw, bh);
		btn.setFilled(true); btn.setFillColor(new Color(0,20,35)); btn.setColor(DIM_CYAN);
		addContent(btn);
 
		GLabel lbl = new GLabel("< BACK TO MENU", 0, 0);
		lbl.setFont(fBack); lbl.setColor(new Color(0, 170, 200));
		lbl.setLocation(bx + (bw - lbl.getWidth()) / 2.0, by + (bh / 2.0) + 8);
		addContent(lbl);
 
		backRegion = new Rectangle(bx, by, bw, bh);
	}
 
	private void drawTickerBar() {
		GLine div = new GLine(60, H-50, W-60, H-50);
		div.setColor(DIM_CYAN); addContent(div);
 
		GLabel ticker = new GLabel(
			">>  FIREWALL FRENZY  |  SELECT DIFFICULTY  |  NOOB  //  PRO  //  HACKER  |  CHOOSE WISELY, OPERATOR",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		addContent(ticker);
	}
 
	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		
		for (int i = 0; i < diffRegions.length; i++) {
			if (diffRegions[i] != null && diffRegions[i].contains(mx, my)) {
				mainScreen.switchToGameScreen();
				return;
			}
		}
		
		if (backRegion != null && backRegion.contains(mx, my)) {
			mainScreen.switchToWelcomeScreen();
		}
	}
}