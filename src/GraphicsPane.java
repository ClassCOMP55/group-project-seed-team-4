import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class GraphicsPane {
	protected MainApplication mainScreen;
	protected ArrayList<GObject> contents;

	// Game objects
	private ArrayList<PacketSprite> packets;
	private GRect homeBase;
	private GLabel scoreLabel;
	private GLabel healthLabel;
	private GLabel statusLabel;

	// Game state
	private int score;
	private int health;
	private boolean paused;

	// Screen/game constants
	private static final int BASE_WIDTH = 140;
	private static final int BASE_HEIGHT = 30;
	private static final int PACKET_SIZE = 28;
	private static final double FALL_SPEED = 2.0;

	public GraphicsPane() {
		contents = new ArrayList<GObject>();
		packets = new ArrayList<PacketSprite>();
		score = 0;
		health = 5;
		paused = false;
	}

	public void setMainScreen(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}

	public void showContent() {
		if (mainScreen == null) {
			return;
		}

		contents.clear();
		packets.clear();
		score = 0;
		health = 5;
		paused = false;

		// HUD
		scoreLabel = new GLabel("Score: " + score, 20, 25);
		healthLabel = new GLabel("Health: " + health, 20, 45);
		statusLabel = new GLabel("Click bad packets. Let GOOD packets pass. Press P to pause.", 20, 65);

		addToPane(scoreLabel);
		addToPane(healthLabel);
		addToPane(statusLabel);

		// Home base near bottom center
		double baseX = (mainScreen.getWidth() - BASE_WIDTH) / 2.0;
		double baseY = mainScreen.getHeight() - 70;
		homeBase = new GRect(baseX, baseY, BASE_WIDTH, BASE_HEIGHT);
		addToPane(homeBase);

		// Spawn a few starter packets
		spawnPacket(60, 100, PacketType.PHISHING);
		spawnPacket(140, 40, PacketType.GOOD);
		spawnPacket(240, 160, PacketType.MALWARE);
		spawnPacket(340, 80, PacketType.DDOS);
		spawnPacket(440, 20, PacketType.SPOOF);
	}

	public void hideContent() {
		if (mainScreen == null) {
			return;
		}

		for (GObject obj : contents) {
			mainScreen.remove(obj);
		}
		contents.clear();
		packets.clear();
	}

	public void mousePressed(MouseEvent e) {
		if (paused) {
			return;
		}

		GObject clicked = mainScreen.getElementAt(e.getX(), e.getY());
		if (clicked == null) {
			return;
		}

		PacketSprite packet = findPacketByShape(clicked);
		if (packet == null) {
			return;
		}

		handlePacketClick(packet);
	}

	public void mouseReleased(MouseEvent e) {
		// Not needed right now, but kept for project structure
	}

	public void mouseClicked(MouseEvent e) {
		// Logic handled in mousePressed
	}

	public void mouseDragged(MouseEvent e) {
		// Not needed for this version
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_P) {
			paused = !paused;
			updateStatus();
		}

		// Optional: press SPACE to spawn a random packet for testing
		if (key == KeyEvent.VK_SPACE && !paused) {
			spawnRandomPacket();
		}
	}

	/**
	 * Call this repeatedly from your animation loop or timer.
	 */
	public void update() {
		if (paused || mainScreen == null) {
			return;
		}

		Iterator<PacketSprite> it = packets.iterator();
		while (it.hasNext()) {
			PacketSprite packet = it.next();
			packet.shape.move(0, FALL_SPEED);

			// Packet reached the home base area
			if (packet.shape.getY() + packet.shape.getHeight() >= homeBase.getY()) {
				if (packet.type == PacketType.GOOD) {
					score += 5;   // good traffic successfully reached destination
				} else {
					health -= 1;  // malicious packet hit the base
				}

				removeFromPane(packet.shape);
				it.remove();
				updateLabels();

				if (health <= 0) {
					paused = true;
					statusLabel.setLabel("Game Over. Press restart in your app to play again.");
				}
			}
		}
	}

	private void spawnRandomPacket() {
		PacketType[] allTypes = PacketType.values();
		int index = (int) (Math.random() * allTypes.length);
		PacketType randomType = allTypes[index];

		double x = 20 + Math.random() * (mainScreen.getWidth() - 60);
		spawnPacket(x, 90, randomType);
	}

	private void spawnPacket(double x, double y, PacketType type) {
		GOval packetShape = new GOval(x, y, PACKET_SIZE, PACKET_SIZE);

		// You can replace these labels/colors/images later
		PacketSprite packet = new PacketSprite(type, packetShape);
		packets.add(packet);
		addToPane(packetShape);
	}

	private void handlePacketClick(PacketSprite packet) {
		// GOOD packets should be allowed through, so clicking them is bad
		if (packet.type == PacketType.GOOD) {
			score -= 5;
		} else {
			score += 10;
		}

		removeFromPane(packet.shape);
		packets.remove(packet);
		updateLabels();
	}

	private PacketSprite findPacketByShape(GObject obj) {
		for (PacketSprite packet : packets) {
			if (packet.shape == obj) {
				return packet;
			}
		}
		return null;
	}

	private void updateLabels() {
		scoreLabel.setLabel("Score: " + score);
		healthLabel.setLabel("Health: " + health);
		updateStatus();
	}

	private void updateStatus() {
		if (paused && health > 0) {
			statusLabel.setLabel("Paused. Press P to resume.");
		} else if (health > 0) {
			statusLabel.setLabel("Click bad packets. Let GOOD packets pass. Press P to pause.");
		}
	}

	private void addToPane(GObject obj) {
		mainScreen.add(obj);
		contents.add(obj);
	}

	private void removeFromPane(GObject obj) {
		mainScreen.remove(obj);
		contents.remove(obj);
	}

	/**
	 * Simple helper class to connect a packet type with its shape.
	 */
	private static class PacketSprite {
		private PacketType type;
		private GOval shape;

		public PacketSprite(PacketType type, GOval shape) {
			this.type = type;
			this.shape = shape;
		}
	}
}