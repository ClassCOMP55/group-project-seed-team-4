import java.awt.event.MouseEvent;
import acm.graphics.*;

public class SettingsPane extends GraphicsPane{
	
	public SettingsPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	public void showContent() {
		GLabel title = new GLabel("Settings", 100, 100);
		GLabel back = new GLabel("Back", 100, 300);
		
		contents.add(title);
		contents.add(back);
		
		mainScreen.add(title);
		mainScreen.add(back);
	}
}
