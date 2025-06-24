import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private BufferedImage background;
    private HashMap<String, BufferedImage> images = new HashMap<>();
    private List<String> imageNames = new ArrayList<>();
    private String currentImage = "";
    private JTextField inputField;
    private String displayedText = "";
    private String[] responses = {
        "myes", "no", "hell yeah", "hell naw",
        "ohohoho", "im cruzing it rn", "i am a sheep, baaaa", "hello",
        "i am a talking cruz", "yes", "nope", "maybe", "absolutely",
        "definitely not", "i am cruzing", "cruz is the best", "i love cruz",
        "cruz is life", "cruz is love", "i am cruz", "i am the cruz",
        "cruz is my name", "talking cruz", "cruz the great",
        "cruz the magnificent", "cruz the awesome", "cruz the amazing",
        "cruz the incredible", "cruz the unbeatable", "cruz the unstoppable",
        "cruz the invincible", "cruz the legendary", "cruz the mythical",
        "cruz the epic", "cruz the supreme", "cruz the ultimate",
        "yes", "yes", "yes", "yes", "yes", "yes", "yes", "yes", 
        "no", "no", "no", "no", "no", "no", "no", "no",
        "maybe", "maybe", "maybe", "maybe", "maybe", "maybe",
        "myes", "myes", "myes", "myes", "myes", "myes", "myes", "myes", "myes",
        "myes", "myes", "myes", "myes", "myes", "myes", "myes", "myes", "myes", 
    };

    public GameWindow() {
        setTitle("Talking Cruz");
        setSize(540, 960);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        try {
            background = ImageIO.read(new File("static/background.png"));
        } catch (IOException e) {
            System.err.println("Could not load background image.");
            background = null;
        }

        loadAllCruzImages("static/cruz");

        if (!imageNames.isEmpty()) {
            currentImage = imageNames.get(0);
        }

        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
                }
                BufferedImage cruzImage = images.get(currentImage);
                if (cruzImage != null) {
                    g.drawImage(cruzImage, 0, 0, getWidth(), getHeight(), null);
                }
                if (!displayedText.isEmpty()) {
                    g.setFont(new Font("Arial", Font.BOLD, 32));
                    g.setColor(Color.WHITE);
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(displayedText);
                    int x = (getWidth() - textWidth) / 2;
                    int y = 120;
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRoundRect(x - 20, y - fm.getAscent(), textWidth + 40, fm.getHeight() + 10, 20, 20);
                    g.setColor(Color.WHITE);
                    g.drawString(displayedText, x, y);
                }
            }
        };
        drawPanel.setBounds(0, 0, 540, 910);
        drawPanel.setLayout(null);

        inputField = new JTextField();
        inputField.setBounds(0, 840, 540, 90);
        inputField.setFont(new Font("Arial", Font.PLAIN, 36));
        inputField.addActionListener(e -> {
            String userInput = inputField.getText();
            long seed = userInput.hashCode();
            Random seededRandom = new Random(seed);
            int idx = seededRandom.nextInt(responses.length);
            displayedText = responses[idx];
            if (!imageNames.isEmpty()) {
                String randomImage = imageNames.get(seededRandom.nextInt(imageNames.size()));
                setCurrentImage(randomImage);
            }
            drawPanel.repaint();
            inputField.setText("");
        });

        setContentPane(new JPanel(null));
        getContentPane().add(drawPanel);
        getContentPane().add(inputField);
    }

    private void loadAllCruzImages(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
            });
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    String key = name.substring(0, name.lastIndexOf('.'));
                    try {
                        images.put(key, ImageIO.read(file));
                        imageNames.add(key);
                    } catch (IOException e) {
                        System.err.println("Could not load image: " + file.getPath());
                    }
                }
            }
        }
    }

    public void setCurrentImage(String name) {
        if (images.containsKey(name)) {
            currentImage = name;
            repaint();
        }
    }

    public void setResponses(String[] newResponses) {
        this.responses = newResponses;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}