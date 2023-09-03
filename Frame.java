import javax.swing.JFrame;

public class Frame extends JFrame {
    Panel panel;

    Frame() {
        panel = new Panel();
        this.add(panel);
        
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("Breakout");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
