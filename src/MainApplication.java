import acm.graphics.GObject;
import acm.program.*;
 
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
 
public class MainApplication extends GraphicsProgram {
 
	public static int WINDOW_WIDTH  = 1920;
	public static int WINDOW_HEIGHT = 1080;
 
	public static Font FONT_HACKED; 
	public static Font FONT_ITHACA;
 
	private WelcomePane     welcomePane;
	private DescriptionPane descriptionPane;
	private SettingsPane    settingsPane;
	private ShopPane        shopPane;
	private DifficultyPane  difficultyPane;
	private GamePane        gamePane;
	private GameOverPane    gameOverPane;
	private GraphicsPane    currentScreen;
	private String selectedDifficulty = "NOOB";

	public void setDifficulty(String difficulty) {
	    selectedDifficulty = difficulty;
	}

	public MainApplication() {
		super();
	}
 
	@Override
	public void init() {
		System.setProperty("sun.java2d.uiScale", "1");
		System.setProperty("sun.java2d.uiScale.enabled", "false");
 
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		WINDOW_WIDTH  = screen.width;
		WINDOW_HEIGHT = screen.height;
 
		loadFonts();
 
		addKeyListeners();
		addMouseListeners();
	}
 
	private void loadFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			Font hacked = Font.createFont(Font.TRUETYPE_FONT, new File("Hacked-KerX.ttf"));
			ge.registerFont(hacked);
			FONT_HACKED = hacked;
		} catch (Exception e) {
			System.err.println("Could not load Hacked-KerX.ttf: " + e.getMessage());
			FONT_HACKED = new Font("Dialog", Font.BOLD, 12); // fallback
		}
		try {
			Font ithaca = Font.createFont(Font.TRUETYPE_FONT, new File("Ithaca-LVB75.ttf"));
			ge.registerFont(ithaca);
			FONT_ITHACA = ithaca;
		} catch (Exception e) {
			System.err.println("Could not load Ithaca-LVB75.ttf: " + e.getMessage());
			FONT_ITHACA = new Font("Dialog", Font.PLAIN, 12); // fallback
		}
	}
 
	@Override
	public void run() {
		for (Frame f : Frame.getFrames()) {
			if (f.isVisible()) {
				f.dispose();
				f.setUndecorated(true);
				f.setExtendedState(Frame.MAXIMIZED_BOTH);
				f.setVisible(true);
				break;
			}
		}
 
		requestFocus();
 
		welcomePane     = new WelcomePane(this);
		descriptionPane = new DescriptionPane(this);
		settingsPane    = new SettingsPane(this);
		shopPane        = new ShopPane(this);
		difficultyPane  = new DifficultyPane(this);
		gamePane        = new GamePane(this);
		gameOverPane    = new GameOverPane(this);
 
		switchToScreen(welcomePane);
	}
 
	public void switchToWelcomeScreen()     { switchToScreen(welcomePane);     }
	public void switchToDescriptionScreen() { switchToScreen(descriptionPane); }
	public void switchToSettingsScreen()    { switchToScreen(settingsPane);    }
	public void switchToShopScreen()        { switchToScreen(shopPane);        }
	public void switchToDifficultyScreen()  { switchToScreen(difficultyPane);  }
	public void switchToGameScreen() {
	    gamePane.startNewGame();
	    switchToScreen(gamePane);
	}
	
	public void switchToGameOverScreen(int score, int lives, String cause) {
		gameOverPane.setResults(score, lives, cause);
		switchToScreen(gameOverPane);
	}
 
	public void switchToGameOverScreen() {
		switchToScreen(gameOverPane);
	}
 
	protected void switchToScreen(GraphicsPane newScreen) {
		if (currentScreen != null) currentScreen.hideContent();
		newScreen.showContent();
		currentScreen = newScreen;
	}
	
 
	public GObject getElementAtLocation(double x, double y) {
		return getElementAt(x, y);
	}
 
	public static void main(String[] args) {
		new MainApplication().start();
	}

	public String getDifficulty() {
	    return selectedDifficulty;
	}
 
	@Override public void mousePressed(MouseEvent e)  { if (currentScreen != null) currentScreen.mousePressed(e);  }
	@Override public void mouseReleased(MouseEvent e) { if (currentScreen != null) currentScreen.mouseReleased(e); }
	@Override public void mouseClicked(MouseEvent e)  { if (currentScreen != null) currentScreen.mouseClicked(e);  }
	@Override public void mouseDragged(MouseEvent e)  { if (currentScreen != null) currentScreen.mouseDragged(e);  }
	@Override public void mouseMoved(MouseEvent e)    { if (currentScreen != null) currentScreen.mouseMoved(e);    }
 
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_G) {
			gameOverPane.setResults(4200, 0, "FIREWALL BREACHED");
			switchToGameOverScreen();
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if (currentScreen != null) currentScreen.keyPressed(e);
	}
 
	@Override public void keyReleased(KeyEvent e) { if (currentScreen != null) currentScreen.keyReleased(e); }
	@Override public void keyTyped(KeyEvent e)    { if (currentScreen != null) currentScreen.keyTyped(e);    }
}
 