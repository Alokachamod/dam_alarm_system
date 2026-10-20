import javax.swing.*;
import java.awt.*;

public class Alarm extends WaterLevelObserver {
    private String id;
    private JFrame frame;
    private JLabel label;

    public Alarm(String id) {
        this.id = id;
        frame = new JFrame("Alarm - " + id);
        frame.setSize(320, 160);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(550, 100);

        label = new JLabel(id + " --> Alarm OFF", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(Color.LIGHT_GRAY);

        frame.add(label);
        frame.setVisible(true);
    }

    @Override
    public void update(int waterLevel) {
        boolean isOn = waterLevel > 50;
        label.setText(isOn ? id + " --> Alarm ON" : id + " --> Alarm OFF");
        label.setBackground(isOn ? new Color(217, 83, 79) : Color.LIGHT_GRAY);
        label.setForeground(isOn ? Color.WHITE : Color.BLACK);
    }
}
