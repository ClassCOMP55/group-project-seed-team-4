import java.awt.event.MouseEvent;
import acm.graphics.*;
public class ShopPane extends GraphicsPane {

	public ShopPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	public void showContent() {
		GLabel title = new GLabel("SHOP", 100, 100);
		GLabel back = new GLabel("Back", 100, 300);
		
		contents.add(title);
		contents.add(back);
		
		mainScreen.add(title);
		mainScreen.add(back);
	}
	
	public void hideContent() {
		for(GObject obj : contents){
			mainScreen.remove(obj);
		}
		contents.clear();
	}
	
	public void mouseClicked(MouseEvent e) {
		if(mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(1)) {
			mainScreen.switchToWelcomeScreen();
		}
	}
}
