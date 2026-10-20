import javax.swing.*;
import java.awt.*;

public class Splitter extends WaterLevelObserver {
    private String id;
    private JFrame frame;
    private JLabel label;

    public Splitter(String id) {
        this.id = id;
        frame = new JFrame("Splitter - " + id);
        frame.setSize(320, 160);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(550, 280);

        label = new JLabel(id + " --> Splitter OFF", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(Color.LIGHT_GRAY);

        frame.add(label);
        frame.setVisible(true);
    }

    @Override
    public void update(int waterLevel) {
        boolean isOn = waterLevel > 75;
        label.setText(isOn ? id + " --> Splitter ON" : id + " --> Splitter OFF");
        label.setBackground(isOn ? new Color(255, 140, 0) : Color.LIGHT_GRAY);
        label.setForeground(isOn ? Color.WHITE : Color.BLACK);
    }
}
