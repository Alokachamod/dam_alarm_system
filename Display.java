import javax.swing.*;
import java.awt.*;

public class Display extends WaterLevelObserver {
    private String id;
    private JFrame frame;
    private JLabel label;

    public Display(String id) {
        this.id = id;
        frame = new JFrame("Display - " + id);
        frame.setSize(320, 160);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(550, 460);

        label = new JLabel(id + " --> Water Level : 0", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(new Color(92, 184, 92));
        label.setForeground(Color.WHITE);

        frame.add(label);
        frame.setVisible(true);
    }

    @Override
    public void update(int waterLevel) {
        label.setText(id + " --> Water Level : " + waterLevel);
    }
}
