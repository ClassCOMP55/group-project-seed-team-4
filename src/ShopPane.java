import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;

public class ShopPane extends GraphicsPane {

	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;

	private static final Color BG          = new Color(  0,   1,   4);
	private static final Color NEON_CYAN   = new Color(  0, 200, 230);
	private static final Color NEON_GREEN  = new Color( 57, 255, 100);
	private static final Color NEON_YELLOW = new Color(255, 220,   0);
	private static final Color NEON_PURP   = new Color(180, 100, 255);
	private static final Color DIM_CYAN    = new Color(  0, 120, 160);
	private static final Color GRID_COLOR  = new Color(  8,  28,  45);
	private static final Color PANEL_BG    = new Color(  0,   8,  18);

	private Font fTitle;
	private Font fSub;
	private Font fItemName;
	private Font fItemDesc;
	private Font fItemPrice;
	private Font fBack;
	private Font fTicker;

	// Placeholder shop items, no functionality yet
	private static final String[] ITEM_NAMES  = {
		"EXTRA LIFE",
		"PECULIAR AUDIENCE",
		"EYE OF RA"
	};
	private static final String[] ITEM_DESCS  = {
		"Adds one additional life to your session",
		"Every time you hit an enemy, another random enemy is also hit",
		"Exposes disguised enemies for the duratrion"
	};
	private static final String[] ITEM_PRICES = {
		"800 PTS",
		"1200 PTS",
		"1500 PTS"
	};
	private static final Color[] ITEM_COLORS  = {
		NEON_GREEN,
		NEON_PURP,
		NEON_YELLOW
	};
	private static final int BLOCK_H = 584;
	private static final int TOP_Y   = (H - BLOCK_H) / 2;

	private Rectangle backRegion;

	public ShopPane(MainApplication mainScreen, CurrencyManager currencyManager) {
		super(mainScreen);
		fTitle     = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  52f);
		fSub       = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fItemName  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  28f);
		fItemDesc  = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 18f);
		fItemPrice = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  20f);
		fBack      = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
		fTicker    = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);
	}

	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawHeader();
		drawShopItems();
		drawBackButton();
		drawTickerBar();
	}

	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
		backRegion = null;
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

		GLabel title = new GLabel("SHOP", 0, titleY);
		title.setFont(fTitle); title.setColor(NEON_CYAN);
		title.setLocation((W - title.getWidth()) / 2.0, titleY);
		addContent(title);

		GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 8,
		                     (W + title.getWidth()) / 2.0, titleY + 8);
		ul.setColor(DIM_CYAN); add(ul);

		GLabel sub = new GLabel(">  SPEND YOUR POINTS  //  UPGRADES  &  POWER-UPS", 0, titleY + 40);
		sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 40);
		addContent(sub);
	}

	private void drawShopItems() {
		int cardW = 900, cardH = 110, gap = 22;
		int cardX = (W - cardW) / 2;
		int startY = TOP_Y + 120;

		for (int i = 0; i < ITEM_NAMES.length; i++) {
			int cy = startY + i * (cardH + gap);
			drawItemCard(cardX, cy, cardW, cardH,
				ITEM_NAMES[i], ITEM_DESCS[i], ITEM_PRICES[i], ITEM_COLORS[i]);
		}
	}

	private void drawItemCard(int x, int y, int w, int h,
			String name, String desc, String price, Color col) {
		// Card background
		GRect card = new GRect(x, y, w, h);
		card.setFilled(true);
		card.setFillColor(PANEL_BG);
		card.setColor(col);
		addContent(card);

		// Left accent bar
		GRect bar = new GRect(x, y, 5, h);
		bar.setFilled(true); bar.setFillColor(col); bar.setColor(col);
		addContent(bar);

		// Item name
		GLabel nameLbl = new GLabel(name, x+24, y+40);
		nameLbl.setFont(fItemName); nameLbl.setColor(col);
		addContent(nameLbl);

		// Description
		GLabel descLbl = new GLabel(desc, x+26, y+78);
		descLbl.setFont(fItemDesc); descLbl.setColor(new Color(160, 200, 215));
		addContent(descLbl);

		// Price tag (right side)
		GLabel priceLbl = new GLabel(price, 0, y+46);
		priceLbl.setFont(fItemPrice); priceLbl.setColor(NEON_YELLOW);
		priceLbl.setLocation(x + w - priceLbl.getWidth() - 24, y+46);
		addContent(priceLbl);

		// Price box outline
		GRect priceBox = new GRect(
			x + w - priceLbl.getWidth() - 36, y + 26,
			priceLbl.getWidth() + 24, 34);
		priceBox.setFilled(false); priceBox.setColor(new Color(100, 80, 0));
		addContent(priceBox);
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
		div.setColor(DIM_CYAN); add(div);

		GLabel ticker = new GLabel(
			">>  FIREWALL FRENZY  |  SHOP  |  SPEND YOUR POINTS  |  UPGRADES  &  POWER-UPS  |  MORE COMING SOON",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		addContent(ticker);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		if (backRegion != null && backRegion.contains(mx, my)) {
			mainScreen.switchToWelcomeScreen();
		}
	}
}
