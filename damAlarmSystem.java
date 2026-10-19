import javax.swing.*;
import java.awt.*;

public class damAlarmSystem  extends JFrame {
    public static final Color BACKGROUND_LIGHT = new Color(245, 247, 250);
    
    // 2. Light Blue for Buttons & Primary Accents
    public static final Color PRIMARY_BLUE = new Color(74, 144, 226);
    public static final Color PRIMARY_BLUE_HOVER = new Color(53, 122, 209);
    
    // 3. Warning & Alert Colors
    public static final Color WARNING_RED = new Color(217, 83, 79);      // For Alarm ON / Critical state (>50 or >75)
    public static final Color SUCCESS_GREEN = new Color(92, 184, 92);    // For Normal / OFF state
    
    // 4. Neutral / Text Colors for Contrast
    public static final Color TEXT_DARK = new Color(43, 45, 66);
    public static final Color PANEL_WHITE = new Color(255, 255, 255);
    
    public damAlarmSystem(){
        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Using GridBagLayout centers children vertically and horizontally without stretching
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_LIGHT);

        add(mainPanel, BorderLayout.CENTER);

        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(80, 350));

        GridBagConstraints sliderConstraints = new GridBagConstraints();
        sliderConstraints.gridx = 0;
        sliderConstraints.gridy = 0;
        sliderConstraints.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(slider, sliderConstraints);

        JPanel devicePanel = new JPanel(new GridLayout(2, 2, 12, 12));
        devicePanel.setBackground(PANEL_WHITE);
        devicePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton alarm = createStyledButton("Alarm", WARNING_RED, Color.WHITE);
        JButton splitter = createStyledButton("Splitter", PRIMARY_BLUE, Color.WHITE);
        JButton display = createStyledButton("Display", SUCCESS_GREEN, Color.WHITE);
        JButton smsButton = createStyledButton("SMS", TEXT_DARK, Color.WHITE);

        devicePanel.add(alarm);
        devicePanel.add(splitter);
        devicePanel.add(display);
        devicePanel.add(smsButton);

        GridBagConstraints deviceConstraints = new GridBagConstraints();
        deviceConstraints.gridx = 1;
        deviceConstraints.gridy = 0;
        deviceConstraints.fill = GridBagConstraints.BOTH;
        deviceConstraints.insets = new Insets(20, 0, 20, 20);
        mainPanel.add(devicePanel, deviceConstraints);
    }

    private JButton createStyledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(true);
        return button;
    }
    public static void main(String[] args) {
        new damAlarmSystem().setVisible(true);
    }
}


