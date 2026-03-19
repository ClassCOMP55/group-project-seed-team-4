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
		
		contents.add(easy);
		contents.add(medium);
		contents.add(hard);
		contents.add(back);
		
		mainScreen.add(easy);
		mainScreen.add(medium);
		mainScreen.add(hard);;
		mainScreen.add(back);
	}
	
	public void hideContent() {
		for(GObject obj : contents) {
			mainScreen.remove(obj);
		}
		contents.clear();
	}
	
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
		
		if(clicked == contents.get(0) || clicked == contents.get(1) || clicked == contents.get(2)) {
			mainScreen.switchToGameScreen();
		}
		else if(clicked == contents.get(3)) {
			mainScreen.switchToWelcomeScreen();
		}
	}
}
