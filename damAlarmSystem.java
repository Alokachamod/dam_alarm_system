import javax.swing.*;
import java.awt.*;

public class DamAlarmSystem extends JFrame {
    public static final Color BACKGROUND_LIGHT = new Color(245, 247, 250);
    public static final Color PRIMARY_BLUE = new Color(74, 144, 226);
    public static final Color WARNING_RED = new Color(217, 83, 79);
    public static final Color SUCCESS_GREEN = new Color(92, 184, 92);
    public static final Color WARNING_ORANGE = new Color(255, 140, 0);
    public static final Color PANEL_WHITE = new Color(255, 255, 255);

    private ControlRoom controlRoom = new ControlRoom();
    private int alarmCount = 1;
    private int splitterCount = 1;
    private int displayCount = 1;
    private int smsCount = 1;

    public DamAlarmSystem() {
        setTitle("Dam Water Level Controller");
        setSize(520, 600);
        setLocation(30, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_LIGHT);
        add(mainPanel, BorderLayout.CENTER);

        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(80, 400));

        // When slider moves, notify the control room
        slider.addChangeListener(e -> controlRoom.setWaterLevel(slider.getValue()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 20);
        mainPanel.add(slider, gbc);

        // 2. Buttons panel for adding more devices
        JPanel devicePanel = new JPanel(new GridLayout(4, 1, 12, 12));
        devicePanel.setBackground(PANEL_WHITE);
        devicePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JButton addAlarm = createStyledButton("Add Alarm", WARNING_RED, Color.WHITE);
        JButton addSplitter = createStyledButton("Add Splitter", WARNING_ORANGE, Color.WHITE);
        JButton addDisplay = createStyledButton("Add Display", SUCCESS_GREEN, Color.WHITE);
        JButton addSmsButton = createStyledButton("Add SMS", PRIMARY_BLUE, Color.WHITE);

        devicePanel.add(addAlarm);
        devicePanel.add(addSplitter);
        devicePanel.add(addDisplay);
        devicePanel.add(addSmsButton);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(devicePanel, gbc);

        addAlarm.addActionListener(e -> {
            alarmCount++;
            controlRoom.addWaterLevelObserver(new Alarm("Alarm " + alarmCount));
        });

        addSplitter.addActionListener(e -> {
            splitterCount++;
            controlRoom.addWaterLevelObserver(new Splitter("Splitter " + splitterCount));
        });

        addDisplay.addActionListener(e -> {
            displayCount++;
            controlRoom.addWaterLevelObserver(new Display("Display " + displayCount));
        });

        addSmsButton.addActionListener(e -> {
            smsCount++;
            controlRoom.addWaterLevelObserver(new SMSSender("SMS " + smsCount));
        });

        controlRoom.addWaterLevelObserver(new Alarm("Alarm 1"));
        controlRoom.addWaterLevelObserver(new Splitter("Splitter 1"));
        controlRoom.addWaterLevelObserver(new Display("Display 1"));
        controlRoom.addWaterLevelObserver(new SMSSender("SMS 1"));
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 45));
        return button;
    }

    public static void main(String[] args) {
        new DamAlarmSystem().setVisible(true);
    }
}
