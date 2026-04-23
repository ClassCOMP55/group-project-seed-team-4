import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


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
        drawPacketLegend();
        drawFooterButtons();
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

    private void drawPacketLegend() {
        int startX = 60;
        int startY = 160;
        int rowH = 120;
        int imgSize = 96;
        int labelX = startX + imgSize + 28;

        PacketType[] list = {
            PacketType.GOOD,
            PacketType.DATA_BURST,
            PacketType.VIRUS,
            PacketType.TROJAN,
            PacketType.DDOS,
            PacketType.PHISHING,
            PacketType.RANSOMWARE
        };

        for (int i = 0; i < list.length; i++) {
            PacketType p = list[i];
            int y = startY + i * rowH;

            String file = filenameForPacket(p);
            java.net.URL url = getClass().getResource("/images/" + file);

            GObject img;
            if (url != null) {
                try {
                    BufferedImage bi = ImageIO.read(url);
                    if (bi != null) {
                        GImage g = new GImage(bi, startX, y);
                        g.setSize(imgSize, imgSize);
                        img = g;
                    } else {
                        throw new Exception("ImageIO returned null for " + file);
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to load image: /images/" + file + " -> " + ex.getMessage());
                    GRect r = new GRect(startX, y, imgSize, imgSize);
                    r.setFilled(true);
                    r.setFillColor(Color.DARK_GRAY);
                    r.setColor(Color.WHITE);
                    img = r;
                }
            } else {
                System.err.println("Image resource not found on classpath: /images/" + file);
                GRect r = new GRect(startX, y, imgSize, imgSize);
                r.setFilled(true);
                r.setFillColor(Color.DARK_GRAY);
                r.setColor(Color.WHITE);
                img = r;
            }
            addContent(img);

            GLabel title = new GLabel(p.name().replace('_', ' '), labelX, y + 28);
            title.setFont(fBody.deriveFont(Font.BOLD, 20f));
            title.setColor(ACCENT);
            addContent(title);

            String desc = packetDescription(p);
            GLabel descLbl = new GLabel(wrapText(desc, 60), labelX, y + 56);
            descLbl.setFont(fNote);
            descLbl.setColor(TEXT_COL);
            addContent(descLbl);
        }
    }

    private String filenameForPacket(PacketType p) {
        switch (p) {
            case GOOD: return "GoodPacket.png";
            case PHISHING: return "PhishingPacket.png";
            case RANSOMWARE: return "RansomwarePacket.png";
            case TROJAN: return "TrojanPacket.png";
            case VIRUS: return "VirusPacket.png";
            case DATA_BURST: return "DataBurst.png";
            case DDOS: return "DDoSPacket.png";
            default: return "GoodPacket.png";
        }
    }

    private String packetDescription(PacketType p) {
        if (!p.isBad()) {
            if (p == PacketType.GOOD) return "Good packet — safe. DO NOT click. Clicking costs a life and penalties.";
            if (p == PacketType.DATA_BURST) return "Good burst — safe, grants slightly more points if left alone.";
            return "Safe packet.";
        } else {
            switch (p) {
                case VIRUS:
                    return "Virus — malicious. Click to destroy. If it reaches the base you lose 1 life.";
                case TROJAN:
                    return "Trojan — faster malicious. Click to destroy or you'll lose a life on breach.";
                case DDOS:
                    return "DDOS — fast. If it reaches the base it disables your abilities for 20s. Click to destroy.";
                case PHISHING:
                    return "Phishing — disguises itself as a good packet. Clicking will reveal its harmful effect.";
                case RANSOMWARE:
                    return "Ransomware — on breach it deducts points (ransom). Click to destroy to prevent penalty.";
                default:
                    return "Malicious packet — destroy it.";
            }
        }
    }

    private String wrapText(String s, int maxCharsPerLine) {
        String[] words = s.split(" ");
        StringBuilder out = new StringBuilder();
        int lineLen = 0;
        for (String w : words) {
            if (lineLen + w.length() + 1 > maxCharsPerLine) {
                out.append("\n");
                lineLen = 0;
            } else if (out.length() > 0) {
                out.append(" ");
                lineLen++;
            }
            out.append(w);
            lineLen += w.length();
        }
        return out.toString();
    }

    private void drawFooterButtons() {
        int btnW = 360, btnH = 78;
        int gap = 18;
        int bx = W - btnW - 60;
        int by = H - btnH - 120; // continue y

        // Continue button (lower)
        GRect cont = new GRect(bx, by, btnW, btnH);
        cont.setFilled(true); cont.setFillColor(PANEL_BG); cont.setColor(ACCENT);
        addContent(cont);

        GLabel contLbl = new GLabel("[ CONTINUE ]", 0, 0);
        contLbl.setFont(MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 22f));
        contLbl.setColor(ACCENT);
        contLbl.setLocation(bx + (btnW - contLbl.getWidth()) / 2.0, by + (btnH / 2.0) + 8);
        addContent(contLbl);

        continueRegion = new Rectangle(bx, by, btnW, btnH);

        // Back button (above Continue)
        int backW = 260, backH = 60;
        int backX = bx; // align right with continue
        int backY = by - backH - gap; // place above continue

        GRect back = new GRect(backX, backY, backW, backH);
        back.setFilled(true); back.setFillColor(PANEL_BG); back.setColor(ACCENT);
        addContent(back);

        GLabel backLbl = new GLabel("< BACK TO MENU", 0, 0);
        backLbl.setFont(MainApplication.FONT_ITHACA.deriveFont(Font.BOLD, 18f));
        backLbl.setColor(ACCENT);
        backLbl.setLocation(backX + (backW - backLbl.getWidth()) / 2.0, backY + (backH / 2.0) + 6);
        addContent(backLbl);

        backRegion = new Rectangle(backX, backY, backW, backH);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        if (continueRegion != null && continueRegion.contains(mx, my)) {
            mainScreen.switchToDifficultyScreen();
            return;
        }
        if (backRegion != null && backRegion.contains(mx, my)) {
            mainScreen.switchToWelcomeScreen();
            return;
        }
    }
}
