import java.awt.event.MouseEvent;
import acm.graphics.*;

public class DifficultyPane extends GraphicsPane{
	
	public DifficultyPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}

	public void showContent() {
		GLabel easy = new GLabel("Noob", 100, 200);
		GLabel medium = new GLabel("Pro", 100, 250);
		GLabel hard = new GLabel("Hacker", 100, 300);
		GLabel back = new GLabel("Back", 100, 400);
	}
}
