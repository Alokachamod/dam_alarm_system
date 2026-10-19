package tt;
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;

// ---------------- Sound Player Helper ----------------

class SoundPlayer {
    private Clip clip;

    SoundPlayer(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } else {
                System.err.println("Sound file not found: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlaying(boolean play) {
        if (clip == null) return;
        if (play) {
            if (!clip.isRunning()) {
                clip.setFramePosition(0);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}

// ---------------- Base UI Device Observer ----------------

abstract class DeviceView extends WaterLevelObserver {
    protected String id;
    protected int lastWaterLevel = 0;
    protected JLabel statusLabel = new JLabel();
    protected JPanel rowPanel = new JPanel(new BorderLayout(10, 0));

    DeviceView(String id) {
        this.id = id;
        StatusStyle.apply(statusLabel);

        JButton editBtn = new JButton("Edit ID");
        editBtn.setFocusPainted(false);
        editBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        editBtn.addActionListener(e -> {
            String newId = JOptionPane.showInputDialog(rowPanel, "Enter new ID for " + this.id + ":", this.id);
            if (newId != null && !newId.trim().isEmpty()) {
                setId(newId.trim());
            }
        });

        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        rowPanel.add(statusLabel, BorderLayout.CENTER);
        rowPanel.add(editBtn, BorderLayout.EAST);
    }

    public JPanel getPanel() {
        return rowPanel;
    }

    public void setId(String newId) {
        this.id = newId;
        update(lastWaterLevel);
    }

    @Override
    public void update(int waterLevel) {
        this.lastWaterLevel = waterLevel;
        refreshUI(waterLevel);
    }

    protected abstract void refreshUI(int waterLevel);
}

// ---------------- Concrete UI Device & User Observers ----------------

class AlarmView extends DeviceView {
    private SoundPlayer soundPlayer = new SoundPlayer("alarm.wav");

    AlarmView(String id) {
        super(id);
        update(0);
    }

    @Override
    protected void refreshUI(int waterLevel) {
        boolean on = waterLevel > 50;
        statusLabel.setText(id + " --> Alarm " + (on ? "ON" : "OFF"));
        statusLabel.setBackground(on ? Color.RED : StatusStyle.OFF_COLOR);
        soundPlayer.setPlaying(on);
        System.out.println(on ? id + "-->Alarm ON" : id + "-->Alarm OFF");
    }
}

class SplitterView extends DeviceView {
    private SoundPlayer soundPlayer = new SoundPlayer("splitter.wav");

    SplitterView(String id) {
        super(id);
        update(0);
    }

    @Override
    protected void refreshUI(int waterLevel) {
        boolean on = waterLevel > 75;
        statusLabel.setText(id + " --> Splitter " + (on ? "ON" : "OFF"));
        statusLabel.setBackground(on ? Color.ORANGE : StatusStyle.OFF_COLOR);
        soundPlayer.setPlaying(on);
        System.out.println(on ? id + "-->Splitter ON" : id + "-->Splitter OFF");
    }
}

class DisplayView extends DeviceView {
    DisplayView(String id) {
        super(id);
        update(0);
    }

    @Override
    protected void refreshUI(int waterLevel) {
        statusLabel.setText(id + " --> Water Level : " + waterLevel);
        statusLabel.setBackground(new Color(100, 160, 230));
        System.out.println(id + "-->Water Level : " + waterLevel);
    }
}

class SMSSenderView extends DeviceView {
    SMSSenderView(String id) {
        super(id);
        statusLabel.setText(id + " --> Waiting...");
        statusLabel.setBackground(StatusStyle.OFF_COLOR);
    }

    @Override
    protected void refreshUI(int waterLevel) {
        statusLabel.setText(id + " --> Sending SMS... " + waterLevel);
        statusLabel.setBackground(new Color(120, 200, 120));
        System.out.println(id + "-->Sending SMS..." + waterLevel);
    }
}

class UserView extends DeviceView {
    UserView(String userId) {
        super(userId);
        update(0);
    }

    @Override
    protected void refreshUI(int waterLevel) {
        boolean danger = waterLevel > 50;
        statusLabel.setText("User [" + id + "] --> Alert: Level " + waterLevel + (danger ? " (WARNING!)" : " (Safe)"));
        statusLabel.setBackground(danger ? new Color(255, 140, 140) : new Color(180, 225, 250));
        System.out.println("User [" + id + "]-->Notified Water Level : " + waterLevel);
    }
}

class StatusStyle {
    static final Color OFF_COLOR = new Color(200, 200, 200);

    static void apply(JLabel label) {
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setPreferredSize(new Dimension(300, 45));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }
}

// ---------------- Main Window ----------------

public class Example extends JFrame {

    private static void registerObserverView(
            DeviceView view,
            ControlRoom controlRoom,
            JPanel listPanel,
            int currentWaterLevel) {
        controlRoom.addWaterLevelObserver(view);
        view.update(currentWaterLevel); // Immediately sync with current slider level

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        wrapper.add(view.getPanel(), BorderLayout.CENTER);

        listPanel.add(wrapper);
        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dam Alarm System");
        frame.setSize(850, 950);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color lightGrayColor = new Color(133, 132, 131);

        JPanel windowPanel = new JPanel(new BorderLayout(10, 10));
        windowPanel.setBackground(lightGrayColor);
        frame.add(windowPanel);

        // Subject
        ControlRoom controlRoom = new ControlRoom();

        // Slider = water level input
        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setPreferredSize(new Dimension(60, 300));

        // Top Panel: Add New Device / User controls
        JPanel addControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        addControlPanel.setBackground(new Color(105, 105, 105));

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        String[] types = {"Alarm", "Display", "SMSSender", "Splitter", "User"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel idLabel = new JLabel("ID / Name:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField idField = new JTextField("A0002", 12);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton addBtn = new JButton("Add Device / User");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        addControlPanel.add(typeLabel);
        addControlPanel.add(typeCombo);
        addControlPanel.add(idLabel);
        addControlPanel.add(idField);
        addControlPanel.add(addBtn);

        windowPanel.add(addControlPanel, BorderLayout.NORTH);

        // Center Panel: Water Level Slider
        JPanel sliderJPanel = new JPanel(new GridBagLayout());
        sliderJPanel.add(slider);
        windowPanel.add(sliderJPanel, BorderLayout.CENTER);

        // Bottom Scrollable Panel: Dynamic list of Devices & Users
        JPanel statusListPanel = new JPanel();
        statusListPanel.setLayout(new BoxLayout(statusListPanel, BoxLayout.Y_AXIS));
        statusListPanel.setBackground(lightGrayColor);
        statusListPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));

        JScrollPane scrollPane = new JScrollPane(statusListPanel);
        scrollPane.setPreferredSize(new Dimension(800, 380));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Connected Devices & Users (Click 'Edit ID' to update ID)"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        windowPanel.add(scrollPane, BorderLayout.SOUTH);

        // Initial default devices
        registerObserverView(new DisplayView("D0001"), controlRoom, statusListPanel, slider.getValue());
        registerObserverView(new AlarmView("A0001"), controlRoom, statusListPanel, slider.getValue());
        registerObserverView(new SplitterView("SP001"), controlRoom, statusListPanel, slider.getValue());
        registerObserverView(new SMSSenderView("SM0001"), controlRoom, statusListPanel, slider.getValue());

        // Slider change listener
        slider.addChangeListener(e -> controlRoom.setWaterLevel(slider.getValue()));

        // Auto-suggest prefix when changing dropdown type
        typeCombo.addActionListener(e -> {
            String selected = (String) typeCombo.getSelectedItem();
            if ("Alarm".equals(selected)) idField.setText("A0002");
            else if ("Display".equals(selected)) idField.setText("D0002");
            else if ("SMSSender".equals(selected)) idField.setText("SM0002");
            else if ("Splitter".equals(selected)) idField.setText("SP002");
            else if ("User".equals(selected)) idField.setText("U0001");
        });

        // Add button action to dynamically add new Devices or Users
        addBtn.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a Device ID or User Name!", "Missing ID", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DeviceView newView = null;
            if ("Alarm".equals(type)) {
                newView = new AlarmView(id);
            } else if ("Display".equals(type)) {
                newView = new DisplayView(id);
            } else if ("SMSSender".equals(type)) {
                newView = new SMSSenderView(id);
            } else if ("Splitter".equals(type)) {
                newView = new SplitterView(id);
            } else if ("User".equals(type)) {
                newView = new UserView(id);
            }

            if (newView != null) {
                registerObserverView(newView, controlRoom, statusListPanel, slider.getValue());
                idField.setText("");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
