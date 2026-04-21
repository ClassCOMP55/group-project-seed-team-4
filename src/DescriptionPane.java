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
	
}