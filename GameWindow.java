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
    // Display a 9:16 game window
    // Background image of the game
    private BufferedImage background;
    // Stores all loaded Cruz images with a name identifier
    private HashMap<String, BufferedImage> images = new HashMap<>();
    // Keeps track of all Cruz image names
    private List<String> imageNames = new ArrayList<>();
    // Currently displayed Cruz image name
    private String currentImage = "";
    // Input field for user to type messages
    private JTextField inputField;
    // Text that is currently shown in the speech bubble
    private String displayedText = "";
    // Possible responses for Cruz to give
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
        // Set window title and size
        setTitle("Talking Cruz");
        setSize(540, 960);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        // Load background image from file
        try {
            background = ImageIO.read(new File("static/background.png"));
        } catch (IOException e) {
            System.err.println("Could not load background image.");
            background = null;
        }
        // Load all Cruz images from static/cruz directory
        loadAllCruzImages("static/cruz");
        // Set the first image as the default if any are loaded
        if (!imageNames.isEmpty()) {
            currentImage = imageNames.get(0);
        }
        // Create custom drawing panel for background, Cruz, and speech bubble
        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw background if available
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
                }
                // Draw the current Cruz image
                BufferedImage cruzImage = images.get(currentImage);
                if (cruzImage != null) {
                    g.drawImage(cruzImage, 0, 0, getWidth(), getHeight(), null);
                }
                // Draw the speech bubble with the current displayed text
                if (!displayedText.isEmpty()) {
                    g.setFont(new Font("Arial", Font.BOLD, 32));
                    g.setColor(Color.WHITE);
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(displayedText);
                    int x = (getWidth() - textWidth) / 2;
                    int y = 120;
                    // Draw semi-transparent rounded background for speech bubble
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRoundRect(x - 20, y - fm.getAscent(), textWidth + 40, fm.getHeight() + 10, 20, 20);
                    // Draw the text itself
                    g.setColor(Color.WHITE);
                    g.drawString(displayedText, x, y);
                }
            }
        };
        drawPanel.setBounds(0, 0, 540, 910);
        drawPanel.setLayout(null);
        // Input text box for user to interact with Cruz
        inputField = new JTextField();
        inputField.setBounds(0, 840, 540, 90);
        inputField.setFont(new Font("Arial", Font.PLAIN, 36));
        // Handle user input on Enter key press
        inputField.addActionListener(e -> {
            String userInput = inputField.getText();
            // Use hash of input as a seed for deterministic randomness
            long seed = userInput.hashCode();
            Random seededRandom = new Random(seed);
            // Pick a response based on the seed
            int idx = seededRandom.nextInt(responses.length);
            displayedText = responses[idx];
            // Pick a random Cruz image to display
            if (!imageNames.isEmpty()) {
                String randomImage = imageNames.get(seededRandom.nextInt(imageNames.size()));
                setCurrentImage(randomImage);
            }
            // Refresh the screen to show new image and text
            drawPanel.repaint();
            // Clear the input field
            inputField.setText("");
        });
        // Add both drawing and input panels to the main window
        setContentPane(new JPanel(null));
        getContentPane().add(drawPanel);
        getContentPane().add(inputField);
    }
    // Load all image files from the specified folder into the image map
    private void loadAllCruzImages(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
            });
            // For each image file, load it and store it with a simplified key
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

    // Set which Cruz image to display
    public void setCurrentImage(String name) {
        if (images.containsKey(name)) {
            currentImage = name;
            repaint();
        }
    }

    // Replace the list of responses Cruz can give
    public void setResponses(String[] newResponses) {
        this.responses = newResponses;
    }

    // Launch the game window on the Swing event thread
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
