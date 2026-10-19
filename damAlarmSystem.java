import javax.swing.*;

public class damAlarmSystem  extends JFrame {
    
    public damAlarmSystem(){
        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

         JPanel mainPanel = new JPanel();
         
         add(mainPanel);


    }

    public static void main(String[] args) {
        new damAlarmSystem().setVisible(true);
    }
}


