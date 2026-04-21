import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;
import javax.swing.*;

public class DescriptionPane extends GraphicsPane{
	
	private static final int W = MainApplication.WINDOW_WIDTH;
	private static final int H = MainApplication.WINDOW_HEIGHT;
	
	private static final Color BG = new Color(0, 1, 4);
	private static final Color ACCENT = new Color(0, 200, 230);
	private static final Color PANEL_BG = new Color(0, 8, 18);
	private static final Color TEXT_COL = new Color(200, 240, 255);
	
	private Font fTitle = MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 52f);
	private Font fBody = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 20f);
	private Font fNote = MainApplication.FONT_ITHACA.deriveFont(Font.PLAIN, 16f);
	private Rectangle continueRegion;
	private Rectangle backRegion;
	
	private MainApplication mainScreen;

	public DescriptionPane(MainApplication mainScreen) {
		super(mainScreen);
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		drawBackground();
		drawHeader();
//		drawPacketLegend();
//		drawFooterButtons();
	}
	
	@Override
	public void hideContent() {
		for (GObject o : contents) mainScreen.remove(o);
		contents.clear();
		continueRegion = null;
		backRegion = null;
	}
	
	private void addContent(GObject o) { contents.add(o); mainScreen.add(o); }
	
	private void drawBackground() {
		GRect bg = new GRect(0, 0, W, H);
		bg.setFilled(true); bg.setFillColor(BG); bg.setColor(BG);
		addContent(bg);
	}
	
	private void drawHeader() {
		GLabel title = new GLabel("HOW TO PLAY", 40, 80);
        title.setFont(fTitle); title.setColor(ACCENT);
        addContent(title);

        GLabel sub = new GLabel("Click hostile packets, avoid friendly ones. Know the threats.", 40, 120);
        sub.setFont(fBody); sub.setColor(TEXT_COL);
        addContent(sub);
	}
}