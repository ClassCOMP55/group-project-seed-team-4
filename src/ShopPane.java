import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
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
	private Font fWallet;
	private Font fItemName;
	private Font fItemDesc;
	private Font fItemPrice;
	private Font fBack;
	private Font fTicker;

	private static final String[] ITEM_NAMES  = {
		"NETWORK RESET",
		"VIRUS SCANNER",
		"SYSTEM PURGE"
	};
	private static final String[] ITEM_DESCS  = {
		"Instantly clears an active DDoS attack when used",
		"Every enemy you destroy also destroys a random other enemy",
		"Nuclear option: instantly destroys every malicious packet on screen"
	};
	private static final String[] ITEM_PRICES = {
		"$500",
		"$1200",
		"$2000"
	};
	private static final Color[] ITEM_COLORS  = {
		NEON_GREEN,
		NEON_PURP,
		NEON_YELLOW
	};

	// ── Layout — computed from fixed pixel anchors, no BLOCK_H math ──────────
	// Title block:   80px tall  (title + underline + subtitle)
	// Wallet block:  70px tall  (large label)
	// Gap:           20px
	// 3 cards:       3 * 110 + 2 * 22 = 374px
	// Gap:           24px
	// Back button:   60px
	// Total:         80+70+20+374+24+60 = 628px
	// Centre: TOP_Y = (H - 628) / 2
	private static final int TITLE_H  = 80;
	private static final int WALLET_H = 70;
	private static final int CARD_H   = 110;
	private static final int CARD_GAP = 22;
	private static final int BACK_H   = 60;
	private static final int TOTAL_H  = TITLE_H + WALLET_H + 20 + (3 * CARD_H + 2 * CARD_GAP) + 24 + BACK_H;
	private static final int TOP_Y    = (H - TOTAL_H) / 2;

	private static final int TITLE_Y  = TOP_Y;
	private static final int WALLET_Y = TITLE_Y  + TITLE_H;
	private static final int CARDS_Y  = WALLET_Y + WALLET_H + 20;
	private static final int BACK_Y   = CARDS_Y  + 3 * CARD_H + 2 * CARD_GAP + 24;

	private Rectangle   backRegion;
	private CurrencyManager currencyManager;
	private GLabel      tokenLabel;
	private Rectangle[] itemRegions;
	private int[]       itemCosts;

	public ShopPane(MainApplication mainScreen, CurrencyManager currencyManager) {
		super(mainScreen);
		this.currencyManager = currencyManager;

		fTitle     = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  52f);
		fSub       = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
		fWallet    = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  30f);
		fItemName  = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  28f);
		fItemDesc  = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 18f);
		fItemPrice = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  20f);
		fBack      = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD,  22f);
		fTicker    = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 17f);

		itemRegions = new Rectangle[ITEM_NAMES.length];
		itemCosts   = new int[ITEM_PRICES.length];
		for (int i = 0; i < ITEM_PRICES.length; i++) {
			String s = ITEM_PRICES[i].replaceAll("[^0-9]", "");
			try { itemCosts[i] = Integer.parseInt(s); }
			catch (NumberFormatException ex) { itemCosts[i] = 0; }
		}
	}

	@Override
	public void showContent() {
		drawBackground();
		drawGrid();
		drawCornerBrackets();
		drawHeader();
		drawWallet();
		drawShopItems();
		drawBackButton();
		drawTickerBar();
	}

	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
		backRegion  = null;
		tokenLabel  = null;
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
		// Title sits at TITLE_Y + ~55px (label baseline)
		int titleY = TITLE_Y + 55;

		GLabel title = new GLabel("SHOP", 0, titleY);
		title.setFont(fTitle); title.setColor(NEON_CYAN);
		title.setLocation((W - title.getWidth()) / 2.0, titleY);
		addContent(title);

		GLine ul = new GLine((W - title.getWidth()) / 2.0, titleY + 8,
		                     (W + title.getWidth()) / 2.0, titleY + 8);
		ul.setColor(DIM_CYAN); addContent(ul);

		GLabel sub = new GLabel(">  SPEND YOUR POINTS  //  UPGRADES  &  POWER-UPS", 0, titleY + 36);
		sub.setFont(fSub); sub.setColor(new Color(0, 160, 190));
		sub.setLocation((W - sub.getWidth()) / 2.0, titleY + 36);
		addContent(sub);
	}

	private void drawWallet() {
		// Vertically centred within the WALLET_H block
		int walletBaseline = WALLET_Y + (WALLET_H / 2) + 10;

		tokenLabel = new GLabel("WALLET:  $" + currencyManager.getTokens(), 0, walletBaseline);
		tokenLabel.setFont(fWallet);
		tokenLabel.setColor(NEON_YELLOW);
		tokenLabel.setLocation((W - tokenLabel.getWidth()) / 2.0, walletBaseline);
		addContent(tokenLabel);

		GLine wul = new GLine((W - tokenLabel.getWidth()) / 2.0, walletBaseline + 6,
		                      (W + tokenLabel.getWidth()) / 2.0, walletBaseline + 6);
		wul.setColor(new Color(100, 80, 0));
		addContent(wul);
	}

	private int[] getCharges() {
		return new int[] {
			mainScreen.getNetworkResetCharges(),
			mainScreen.getVirusScannerCharges(),
			mainScreen.getSystemPurgeCharges()
		};
	}

	private void drawShopItems() {
		int cardW = 900;
		int cardX = (W - cardW) / 2;
		int[] charges = getCharges();

		for (int i = 0; i < ITEM_NAMES.length; i++) {
			int cy = CARDS_Y + i * (CARD_H + CARD_GAP);
			drawItemCard(cardX, cy, cardW, CARD_H,
				ITEM_NAMES[i], ITEM_DESCS[i], ITEM_PRICES[i], ITEM_COLORS[i], i, charges[i]);
		}
	}

	private void drawItemCard(int x, int y, int w, int h,
			String name, String desc, String price, Color col, int index, int charges) {
		GRect card = new GRect(x, y, w, h);
		card.setFilled(true); card.setFillColor(PANEL_BG); card.setColor(col);
		addContent(card);

		GRect bar = new GRect(x, y, 5, h);
		bar.setFilled(true); bar.setFillColor(col); bar.setColor(col);
		addContent(bar);

		GLabel nameLbl = new GLabel(name, x+24, y+40);
		nameLbl.setFont(fItemName); nameLbl.setColor(col);
		addContent(nameLbl);

		GLabel descLbl = new GLabel(desc, x+26, y+78);
		descLbl.setFont(fItemDesc); descLbl.setColor(new Color(160, 200, 215));
		addContent(descLbl);

		GLabel chargeLbl = new GLabel("x" + charges, 0, y+40);
		chargeLbl.setFont(fItemPrice);
		chargeLbl.setColor(charges > 0 ? col : new Color(80, 80, 80));
		chargeLbl.setLocation(x + 24 + nameLbl.getWidth() + 14, y+40);
		addContent(chargeLbl);

		GLabel priceLbl = new GLabel(price, 0, y+46);
		priceLbl.setFont(fItemPrice); priceLbl.setColor(NEON_YELLOW);
		priceLbl.setLocation(x + w - priceLbl.getWidth() - 24, y+46);
		addContent(priceLbl);

		GRect priceBox = new GRect(
			x + w - priceLbl.getWidth() - 36, y + 26,
			priceLbl.getWidth() + 24, 34);
		priceBox.setFilled(false); priceBox.setColor(new Color(100, 80, 0));
		addContent(priceBox);

		itemRegions[index] = new Rectangle(x, y, w, h);
	}

	private void drawBackButton() {
		int bw = 320;
		int bx = (W - bw) / 2;

		GRect btn = new GRect(bx, BACK_Y, bw, BACK_H);
		btn.setFilled(true); btn.setFillColor(new Color(0,20,35)); btn.setColor(DIM_CYAN);
		addContent(btn);

		GLabel lbl = new GLabel("< BACK TO MENU", 0, 0);
		lbl.setFont(fBack); lbl.setColor(new Color(0, 170, 200));
		lbl.setLocation(bx + (bw - lbl.getWidth()) / 2.0, BACK_Y + (BACK_H / 2.0) + 8);
		addContent(lbl);

		backRegion = new Rectangle(bx, BACK_Y, bw, BACK_H);
	}

	private void drawTickerBar() {
		GLine div = new GLine(60, H-50, W-60, H-50);
		div.setColor(DIM_CYAN); addContent(div);

		GLabel ticker = new GLabel(
			">>  FIREWALL FRENZY  |  SHOP  |  SPEND YOUR POINTS  |  UPGRADES  &  POWER-UPS  |  MORE COMING SOON",
			0, H-22);
		ticker.setFont(fTicker); ticker.setColor(new Color(0, 150, 180));
		ticker.setLocation((W - ticker.getWidth()) / 2.0, H-22);
		addContent(ticker);
	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		// Redraw after P so wallet balance updates live
		if (e.getKeyCode() == java.awt.event.KeyEvent.VK_P) {
			hideContent();
			showContent();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		if (backRegion != null && backRegion.contains(mx, my)) {
			mainScreen.switchToWelcomeScreen();
			return;
		}
		for (int i = 0; i < itemRegions.length; i++) {
			Rectangle r = itemRegions[i];
			if (r != null && r.contains(mx, my)) {
				handlePurchase(i);
				return;
			}
		}
	}

	private void handlePurchase(int index) {
		int cost = itemCosts[index];
		if (currencyManager.getTokens() < cost) {
			JOptionPane.showMessageDialog(null,
				"Not enough funds to purchase \"" + ITEM_NAMES[index] + "\".",
				"Insufficient Funds", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int choice = JOptionPane.showConfirmDialog(null,
			"Buy \"" + ITEM_NAMES[index] + "\" for $" + cost + "?",
			"Confirm Purchase", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			boolean ok = currencyManager.spendTokens(cost);
			if (ok) {
				if      (index == 0) mainScreen.grantNetworkResetPurchase();
				else if (index == 1) mainScreen.grantVirusScannerPurchase();
				else if (index == 2) mainScreen.grantSystemPurgePurchase();
				JOptionPane.showMessageDialog(null,
					"Purchased \"" + ITEM_NAMES[index] + "\".",
					"Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
				hideContent();
				showContent();
			} else {
				JOptionPane.showMessageDialog(null,
					"Not enough funds to complete transaction.",
					"Insufficient Funds", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}