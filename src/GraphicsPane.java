import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GObject;

public class GraphicsPane {

	protected MainApplication mainScreen;
	protected ArrayList<GObject> contents;

	public GraphicsPane(MainApplication app) {
		mainScreen = app;
		contents = new ArrayList<GObject>();
	}

	/**
	 * Called when this pane becomes the active screen.
	 * Child classes should override this and add their graphics here.
	 */
	public void showContent() {
	}

	/**
	 * Removes all graphics that were added by this pane.
	 */
	public void hideContent() {
		for (GObject obj : contents) {
			mainScreen.remove(obj);
		}
		contents.clear();
	}

	/**
	 * Helper method so child panes can add objects and automatically track them.
	 */
	public void add(GObject obj) {
		mainScreen.add(obj);
		contents.add(obj);
	}

	/**
	 * Helper method so child panes can remove objects and automatically untrack them.
	 */
	public void remove(GObject obj) {
		mainScreen.remove(obj);
		contents.remove(obj);
	}

	/**
	 * Optional helper to clear everything from this pane.
	 */
	public void clearPane() {
		hideContent();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	
}
