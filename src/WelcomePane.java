import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.*;
public class WelcomePane extends GraphicsPane{
	public WelcomePane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addPicture();
		addDescriptionButton();
		addButtons();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private GLabel playButton;
	private GLabel settingsButton;
	private GLabel shopButton;
	
	private void addButtons() {
		playButton = new GLabel("PLAY", 100, 200);
		settingsButton = new GLabel("SETTINGS", 100, 250);
		shopButton = new GLabel("SHOP", 100, 300);
		
		contents.add(playButton);
		contents.add(settingsButton);
		contents.add(shopButton);
		
		mainScreen.add(playButton);
		mainScreen.add(settingsButton);
		mainScreen.add(shopButton);
	}
	
	private void addPicture(){
		GImage startImage = new GImage("start.png", 200, 100);
		startImage.scale(0.5, 0.5);
		startImage.setLocation((mainScreen.getWidth() - startImage.getWidth())/ 2, 70);
		
		contents.add(startImage);
		mainScreen.add(startImage);
	}
	
	private void addDescriptionButton() {
		GImage moreButton = new GImage("more.jpeg", 200, 400);
		moreButton.scale(0.3, 0.3);
		moreButton.setLocation((mainScreen.getWidth() - moreButton.getWidth())/ 2, 400);
		
		contents.add(moreButton);
		mainScreen.add(moreButton);

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
		
		if(clicked == playButton) {
			mainScreen.switchToDifficultyScreen();
		}
		else if(clicked == settingsButton) {
			mainScreen.switchToSettingsScreen();
		}
		else if(clicked == shopButton) {
			mainScreen.switchToShopScreen();
		}
	}

}
