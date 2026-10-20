import javax.swing.*;
import java.awt.*;

public class SMSSender extends WaterLevelObserver {
    private String id;
    private JFrame frame;
    private JLabel label;

    public SMSSender(String id) {
        this.id = id;
        frame = new JFrame("SMS Sender - " + id);
        frame.setSize(320, 160);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(550, 640);

        label = new JLabel(id + " --> Waiting...", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setOpaque(true);
        label.setBackground(new Color(74, 144, 226));
        label.setForeground(Color.WHITE);

        frame.add(label);
        frame.setVisible(true);
    }

    @Override
    public void update(int waterLevel) {
        label.setText(id + " --> Sending SMS... " + waterLevel);
    }
}
