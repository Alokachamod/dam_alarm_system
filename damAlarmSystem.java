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

         JPanel mainPanel = new JPanel(new BorderLayout());
         mainPanel.setBackground(BACKGROUND_LIGHT);

         add(mainPanel, BorderLayout.CENTER);

         JSlider slider = new JSlider(JSlider.VERTICAL, 100, 0);
            slider.setMajorTickSpacing(10);                                         
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            mainPanel.add(slider, BorderLayout.CENTER);  
                           
            

    }

    public static void main(String[] args) {
        new damAlarmSystem().setVisible(true);
    }
}


