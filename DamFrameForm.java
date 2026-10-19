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

// ---------------- UI observers (reuse WaterLevelObserver & ControlRoom from Demo.java) ----------------

class AlarmView extends WaterLevelObserver {
    private String id;
    private JLabel label = new JLabel();
    private SoundPlayer soundPlayer = new SoundPlayer("alarm.wav");

    AlarmView(String id) {
        this.id = id;
        StatusStyle.apply(label);
        update(0);
    }

    public JLabel getLabel() { return label; }

    public void update(int waterLevel) {
        boolean on = waterLevel > 50;
        label.setText(id + " --> Alarm " + (on ? "ON" : "OFF"));
        label.setBackground(on ? Color.RED : StatusStyle.OFF_COLOR);
        soundPlayer.setPlaying(on);
    }
}

class SplitterView extends WaterLevelObserver {
    private String id;
    private JLabel label = new JLabel();
    private SoundPlayer soundPlayer = new SoundPlayer("splitter.wav");

    SplitterView(String id) {
        this.id = id;
        StatusStyle.apply(label);
        update(0);
    }

    public JLabel getLabel() { return label; }

    public void update(int waterLevel) {
        boolean on = waterLevel > 75;
        label.setText(id + " --> Splitter " + (on ? "ON" : "OFF"));
        label.setBackground(on ? Color.ORANGE : StatusStyle.OFF_COLOR);
        soundPlayer.setPlaying(on);
    }
}

class DisplayView extends WaterLevelObserver {
    private String id;
    private JLabel label = new JLabel();

    DisplayView(String id) {
        this.id = id;
        StatusStyle.apply(label);
        update(0);
    }

    public JLabel getLabel() { return label; }

    public void update(int waterLevel) {
        label.setText(id + " --> Water Level : " + waterLevel);
        label.setBackground(new Color(100, 160, 230));
    }
}

class SMSSenderView extends WaterLevelObserver {
    private String id;
    private JLabel label = new JLabel();

    SMSSenderView(String id) {
        this.id = id;
        StatusStyle.apply(label);
        label.setText(id + " --> Waiting...");
        label.setBackground(StatusStyle.OFF_COLOR);
    }

    public JLabel getLabel() { return label; }

    public void update(int waterLevel) {
        label.setText(id + " --> Sending SMS... " + waterLevel);
        label.setBackground(new Color(120, 200, 120));
    }
}

class StatusStyle {
    static final Color OFF_COLOR = new Color(200, 200, 200);

    static void apply(JLabel label) {
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setPreferredSize(new Dimension(300, 50));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }
}



public class DamFrameForm extends JFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dam Alarm System");
        frame.setSize(800, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color lightGrayColor = new Color(133, 132, 131);

        JPanel windowPanel = new JPanel(new BorderLayout());
        windowPanel.setBackground(lightGrayColor);
        frame.add(windowPanel);

        // Subject
        ControlRoom controlRoom = new ControlRoom();

        // UI observers
        AlarmView alarm = new AlarmView("A0001");
        DisplayView display = new DisplayView("D0001");
        SMSSenderView sms = new SMSSenderView("SM0001");
        SplitterView splitter = new SplitterView("SP001");

        controlRoom.addWaterLevelObserver(alarm);
        controlRoom.addWaterLevelObserver(display);
        controlRoom.addWaterLevelObserver(sms);
        controlRoom.addWaterLevelObserver(splitter);

        // Console observers from Demo.java (optional - also prints to terminal)
        controlRoom.addWaterLevelObserver(new Alarm("A0001"));
        controlRoom.addWaterLevelObserver(new Display("D0001"));
        controlRoom.addWaterLevelObserver(new SMSSender("SM0001"));
        controlRoom.addWaterLevelObserver(new Splitter("SP001"));

        // Slider = water level input
        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setPreferredSize(new Dimension(60, 300));

        slider.addChangeListener(e -> controlRoom.setWaterLevel(slider.getValue()));

        JPanel sliderJPanel = new JPanel(new GridBagLayout());
        sliderJPanel.add(slider);
        windowPanel.add(sliderJPanel, BorderLayout.CENTER);

        // Status panel at the bottom
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statusPanel.setBackground(lightGrayColor);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));
        statusPanel.add(display.getLabel());
        statusPanel.add(alarm.getLabel());
        statusPanel.add(splitter.getLabel());
        statusPanel.add(sms.getLabel());
        windowPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
